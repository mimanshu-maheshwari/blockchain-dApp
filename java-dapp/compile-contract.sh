#!/usr/bin/bash

set -x
set -eo pipefail

pip=$$
this_file_name=$(basename $0)
dir_name=$(dirname $0)

contract_path="$dir_name/src/main/resources/solidity"
contract_build_path="$dir_name/target/contract"
contract_files_glob="$contract_path/*.sol"
wrapper_path="$dir_name/src/main/java"


[[ -d "$contract_build_path" ]] && rm -rf "$contract_build_path"
mkdir -p "$contract_build_path"

for file in $contract_files_glob; do 
  base_file_name=$(basename "$file")
  file_ext="${base_file_name#*.}"
  if [[ "$file_ext" != "sol" ]]; then 
    continue 
  fi
  filename="${base_file_name%.*}"
  # generate bin and abi for contract
  solc "$file" --bin --abi --evm-version london --optimize -o "$contract_build_path"
  # create wrapper using web3j
  web3j generate solidity -a="$contract_build_path/$filename.abi" -b="$contract_build_path/$filename.bin" -o "$wrapper_path" -p="io.zeeve.dapp.contracts.solidity"
done

# web3j generate solidity -a=DocumentRegistry.abi -b=DocumentRegistry.bin -o ../java/ -p="io.zeeve.dapp"
