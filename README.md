# Create a dApp

## Create a private chain

### Using docker compose 
- create the nodes: `setup.sh`
- run `docker compose up` for running nodes
- run checks: 

```bash
RPC=http://localhost:8545
curl -X POST \
    --data '{"jsonrpc":"2.0","method":"eth_blockNumber","params":[],"id":1}' \
    http://localhost:8545 \
    -H 'Content-Type: application/json' | jq

curl -X POST \
    --data '{"jsonrpc":"2.0","method":"qbft_getValidatorsByBlockNumber","params":["latest"],"id":1}' \
    http://localhost:8545 \
    -H 'Content-Type: application/json' | jq

curl -X POST \
    --data '{"jsonrpc":"2.0","method":"net_peerCount","params":[],"id":1}' \
    http://localhost:8545 \
    -H 'Content-Type: application/json' | jq


curl -X POST \
    --data '{"jsonrpc":"2.0","method":"eth_getBalance","params":["0xfe3b557e8fb62b89f4916b721be55ceb828dbd73","latest"],"id":1}' \
    http://localhost:8545 \
    -H "Content-Type: application/json" | jq

curl -s -X POST "$RPC" \
    -H "Content-Type: application/json" \
    --data '{"jsonrpc":"2.0","method":"eth_chainId","params":[],"id":1}' | jq

curl -s -X POST "$RPC" \
    -H "Content-Type: application/json" \
    --data '{"jsonrpc":"2.0","method":"rpc_modules","params":[],"id":1}' | jq


curl -s -X POST "$RPC" \
    -H "Content-Type: application/json" \
    --data '{"jsonrpc":"2.0","method":"eth_getBlockByNumber","params":["latest",true],"id":1}' | jq

curl -s -X POST "$RPC" \
  -H "Content-Type: application/json" \
  --data '{
    "jsonrpc":"2.0",
    "method":"eth_getLogs",
    "params":[{
      "fromBlock":"0x0",
      "toBlock":"latest"
    }],
    "id":1
  }' | jq
```

### Process: 
- Create a wallet with MetaMask
- Create QBFT network using besu
- Add network to MetaMask 
- Add accounts to meta mask using private keys
- send transaction using MetaMask
- Validate transaction in payer and receiver account
- Validate logs for transaction block

#### Run Nodes

```bash 
# Node 1
besu --data-path=data \
    --genesis-file=../genesis.json \
    --rpc-http-enabled \
    --rpc-http-api=ETH,NET,QBFT \
    --host-allowlist="*" \
    --rpc-http-cors-origins="all" \
    --profile=ENTERPRISE
```

```bash 
# Node 2
besu --data-path=data \
    --genesis-file=../genesis.json \
    --bootnodes=enode://bc12f724cdc5f9c7d4965da8cecfd21e777f5e1034b677de1fbdd68330f0f98a10b5b52e06dda98d42890a469da796fe447e49ad01b89654a04987248e7e6a4d@127.0.0.1:30303 \
    --p2p-port=30304 \
    --rpc-http-enabled \
    --rpc-http-api=ETH,NET,QBFT \
    --host-allowlist="*" \
    --rpc-http-cors-origins="all" \
    --rpc-http-port=8546 \
    --profile=ENTERPRISE
```

```bash 
# Node 3
besu --data-path=data \
    --genesis-file=../genesis.json \
    --bootnodes=enode://bc12f724cdc5f9c7d4965da8cecfd21e777f5e1034b677de1fbdd68330f0f98a10b5b52e06dda98d42890a469da796fe447e49ad01b89654a04987248e7e6a4d@127.0.0.1:30303 \
    --p2p-port=30305 \
    --rpc-http-enabled \
    --rpc-http-api=ETH,NET,QBFT \
    --host-allowlist="*" \
    --rpc-http-cors-origins="all" \
    --rpc-http-port=8547 \
    --profile=ENTERPRISE

```

```bash 
# Node 4
besu --data-path=data \
    --genesis-file=../genesis.json \
    --bootnodes=enode://bc12f724cdc5f9c7d4965da8cecfd21e777f5e1034b677de1fbdd68330f0f98a10b5b52e06dda98d42890a469da796fe447e49ad01b89654a04987248e7e6a4d@127.0.0.1:30303 \
    --p2p-port=30306 \
    --rpc-http-enabled \
    --rpc-http-api=ETH,NET,QBFT \
    --host-allowlist="*" \
    --rpc-http-cors-origins="all" \
    --rpc-http-port=8548 \
    --profile=ENTERPRISE
```

### Network details: 

- Name     : Local Besu Dev 
- RPC URL  : http://localhost:8545 
- Chain ID : 1337
- Currency Symbol : ETH 

### Testing: 

- Count nodes:
```bash 
curl -X POST \
--data '{"jsonrpc":"2.0","method":"net_peerCount","params":[],"id":1}' \
http://localhost:8545/ \
-H "Content-Type: application/json"
```

- Get all validators: 
```bash 
curl -X POST \
--data '{"jsonrpc":"2.0","method":"qbft_getValidatorsByBlockNumber","params":["latest"], "id":1}' \
http://localhost:8545/ \
-H "Content-Type: application/json"
```

- Most recent block: 
```bash 
curl -X POST \
--data '{"jsonrpc":"2.0","method":"eth_blockNumber","params":[],"id":1}' \
http://localhost:8545/ \
-H "Content-Type: application/json"
```

## References: 
- [dApp Architecture](https://github.com/gonzalobam/besu-network/blob/master/DAPP_ARCHITECTURE.md)
- [Dapp Architecture Designs](https://github.com/ConsenSysMesh/Ethereum-Development-Best-Practices/wiki/Dapp-Architecture-Designs)
- [The hitchhikers guide to smart contract in Ethereum](https://www.openzeppelin.com/news/the-hitchhikers-guide-to-smart-contracts-in-ethereum-848f08001f05)
- [Send a transaction with metamask](https://docs.besu-eth.org/private-networks/tutorials/quickstart#6-send-a-transaction-with-metamask)
- [Run QBFT with 4 nodes](https://docs.besu-eth.org/private-networks/tutorials/qbft)
- [Configure Local test network](https://support.metamask.io/configure/networks/how-to-add-a-custom-network-rpc/)
- [web3signer](https://github.com/ConsenSys/web3signer)

