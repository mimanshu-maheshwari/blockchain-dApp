#!/usr/bin/env bash
set -Eeuo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

CONFIG_DIR="${ROOT_DIR}/config"
BESU_DIR="${CONFIG_DIR}/besu"
NODES_DIR="${CONFIG_DIR}/nodes"
LOGS_DIR="${ROOT_DIR}/logs/besu"

NETWORK_FILES_DIR="${BESU_DIR}/networkFiles"

QBFT_CONFIG_FILE="${BESU_DIR}/qbftConfigFile.json"
GENESIS_FILE="${BESU_DIR}/genesis.json"
STATIC_NODES_FILE="${BESU_DIR}/static-nodes.json"
BESU_ENV_FILE="${BESU_DIR}/.env"

BESU="${BESU_BIN_PATH:-besu}"

VALIDATOR_COUNT=4
SUBNET_PREFIX="172.16.239"
P2P_PORT="30303"

log() {
  echo
  echo "==> $*"
}

fail() {
  echo "ERROR: $*" >&2
  exit 1
}

require_command() {
  command -v "$1" >/dev/null 2>&1 || fail "$1 is required but not installed"
}

generate_random_private_key() {
  od -An -N32 -tx1 /dev/urandom | tr -d ' \n'
}

log "Checking required tools"
require_command "${BESU}"
require_command od
require_command find
require_command sort
require_command tr
require_command cp
require_command rm
require_command mkdir
require_command chmod

log "Creating base directories"
mkdir -p "${BESU_DIR}"
mkdir -p "${NODES_DIR}"
mkdir -p "${LOGS_DIR}"

log "Checking QBFT config file"
[[ -f "${QBFT_CONFIG_FILE}" ]] || fail "Missing ${QBFT_CONFIG_FILE}"

log "Cleaning old generated files"
rm -rf "${NETWORK_FILES_DIR}"
rm -rf "${NODES_DIR}"
rm -f "${GENESIS_FILE}"
rm -f "${STATIC_NODES_FILE}"

mkdir -p "${NODES_DIR}"

for i in $(seq 1 "${VALIDATOR_COUNT}"); do
  mkdir -p "${NODES_DIR}/validator${i}"
done

mkdir -p "${NODES_DIR}/rpcnode"

log "Creating config/besu/.env if missing"
if [[ ! -f "${BESU_ENV_FILE}" ]]; then
  cat > "${BESU_ENV_FILE}" <<EOF
# Besu container environment file.
# Runtime nodes are started by Docker Compose.
EOF
fi

log "Generating genesis.json and validator keys using local Besu CLI"
"${BESU}" operator generate-blockchain-config \
  --config-file="${QBFT_CONFIG_FILE}" \
  --to="${NETWORK_FILES_DIR}" \
  --private-key-file-name=key

[[ -f "${NETWORK_FILES_DIR}/genesis.json" ]] || fail "Generated genesis.json not found"

log "Copying generated genesis.json"
cp "${NETWORK_FILES_DIR}/genesis.json" "${GENESIS_FILE}"

log "Collecting generated validator key directories"
mapfile -t KEY_DIRS < <(find "${NETWORK_FILES_DIR}/keys" -mindepth 1 -maxdepth 1 -type d | sort)

if [[ "${#KEY_DIRS[@]}" -ne "${VALIDATOR_COUNT}" ]]; then
  fail "Expected ${VALIDATOR_COUNT} generated key directories, found ${#KEY_DIRS[@]}"
fi

log "Copying validator keys"
for i in $(seq 1 "${VALIDATOR_COUNT}"); do
  SRC_DIR="${KEY_DIRS[$((i - 1))]}"
  DEST_DIR="${NODES_DIR}/validator${i}"

  cp "${SRC_DIR}/key" "${DEST_DIR}/key"
  cp "${SRC_DIR}/key.pub" "${DEST_DIR}/key.pub"

  chmod 600 "${DEST_DIR}/key"
done

log "Creating non-validator RPC node key"
RPC_KEY_FILE="${NODES_DIR}/rpcnode/key"

generate_random_private_key > "${RPC_KEY_FILE}"
chmod 600 "${RPC_KEY_FILE}"

log "Generating static-nodes.json from validator keys and static Docker IPs"
{
  echo "["
  for i in $(seq 1 "${VALIDATOR_COUNT}"); do
    PUBKEY_FILE="${NODES_DIR}/validator${i}/key.pub"
    [[ -f "${PUBKEY_FILE}" ]] || fail "Missing ${PUBKEY_FILE}"

    PUBKEY="$(tr -d '\r\n' < "${PUBKEY_FILE}")"
    PUBKEY="${PUBKEY#0x}"

    IP="${SUBNET_PREFIX}.$((10 + i))"

    COMMA=","
    if [[ "${i}" -eq "${VALIDATOR_COUNT}" ]]; then
      COMMA=""
    fi

    echo "  \"enode://${PUBKEY}@${IP}:${P2P_PORT}\"${COMMA}"
  done
  echo "]"
} > "${STATIC_NODES_FILE}"

log "Validating generated files"
[[ -f "${GENESIS_FILE}" ]] || fail "Missing ${GENESIS_FILE}"
[[ -f "${STATIC_NODES_FILE}" ]] || fail "Missing ${STATIC_NODES_FILE}"

for i in $(seq 1 "${VALIDATOR_COUNT}"); do
  [[ -f "${NODES_DIR}/validator${i}/key" ]] || fail "Missing validator${i} key"
  [[ -f "${NODES_DIR}/validator${i}/key.pub" ]] || fail "Missing validator${i} key.pub"
done

[[ -f "${NODES_DIR}/rpcnode/key" ]] || fail "Missing rpcnode key"

log "Generated files"
echo "QBFT input:    ${QBFT_CONFIG_FILE}"
echo "Runtime config: ${BESU_DIR}/config.toml"
echo "Genesis:       ${GENESIS_FILE}"
echo "Static nodes:  ${STATIC_NODES_FILE}"
echo "Node keys:     ${NODES_DIR}"

log "Static nodes"
cat "${STATIC_NODES_FILE}"

log "Done"
echo
echo "Next commands:"
echo "  docker compose up -d"
echo "  docker compose logs -f validator1"
echo
echo "Check block number:"
echo "  curl -X POST --data '{\"jsonrpc\":\"2.0\",\"method\":\"eth_blockNumber\",\"params\":[],\"id\":1}' http://localhost:8545 -H 'Content-Type: application/json'"
echo
echo "Check validators:"
echo "  curl -X POST --data '{\"jsonrpc\":\"2.0\",\"method\":\"qbft_getValidatorsByBlockNumber\",\"params\":[\"latest\"],\"id\":1}' http://localhost:8545 -H 'Content-Type: application/json'"
