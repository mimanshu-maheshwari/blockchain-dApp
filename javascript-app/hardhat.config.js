import "dotenv/config";
import { defineConfig } from "hardhat/config";
import hardhatEthers from "@nomicfoundation/hardhat-ethers";

const privateKey = process.env.PRIVATE_KEY;

export default defineConfig({
  plugins: [hardhatEthers],

  solidity: {
    version: "0.8.20",
    settings: {
      evmVersion: "london"
    }
  },

  networks: {
    besu: {
      type: "http",
      chainType: "l1",
      url: process.env.BESU_RPC_URL || "http://127.0.0.1:8545",
      chainId: 1337,
      accounts: privateKey ? [privateKey] : []
    }
  }
});
