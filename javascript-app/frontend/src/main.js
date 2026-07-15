import { ethers } from "ethers";
import contractInfo from "./SimpleStorage.json";
import "./style.css";

const app = document.querySelector("#app");

app.innerHTML = `
  <main>
    <h1>Besu SimpleStorage dApp</h1>

    <button id="connect">Connect MetaMask</button>

    <p><strong>Account:</strong> <span id="account">Not connected</span></p>
    <p><strong>Network:</strong> <span id="network">Unknown</span></p>
    <p><strong>Contract:</strong> ${contractInfo.address}</p>

    <hr />

    <button id="read">Read value</button>
    <p><strong>Current value:</strong> <span id="value">-</span></p>

    <input id="newValue" type="number" placeholder="New value" />
    <button id="write">Set value</button>

    <p id="status"></p>
  </main>
`;

let provider;
let signer;
let contract;

const accountEl = document.querySelector("#account");
const networkEl = document.querySelector("#network");
const valueEl = document.querySelector("#value");
const statusEl = document.querySelector("#status");

async function connect() {
  if (!window.ethereum) {
    alert("MetaMask not found");
    return;
  }

  provider = new ethers.BrowserProvider(window.ethereum);
  signer = await provider.getSigner();

  const account = await signer.getAddress();
  const network = await provider.getNetwork();

  accountEl.textContent = account;
  networkEl.textContent = `${network.name} (${network.chainId})`;

  contract = new ethers.Contract(contractInfo.address, contractInfo.abi, signer);

  statusEl.textContent = "Connected";
}

async function readValue() {
  if (!contract) {
    await connect();
  }

  const value = await contract.getValue();
  valueEl.textContent = value.toString();
}

async function writeValue() {
  if (!contract) {
    await connect();
  }

  const input = document.querySelector("#newValue");
  const newValue = input.value;

  if (newValue === "") {
    alert("Enter a value");
    return;
  }

  statusEl.textContent = "Sending transaction...";

  const tx = await contract.setValue(newValue);
  statusEl.textContent = `Transaction sent: ${tx.hash}`;

  const receipt = await tx.wait();
  statusEl.textContent = `Confirmed in block ${receipt.blockNumber}`;

  await readValue();
}

document.querySelector("#connect").addEventListener("click", connect);
document.querySelector("#read").addEventListener("click", readValue);
document.querySelector("#write").addEventListener("click", writeValue);
