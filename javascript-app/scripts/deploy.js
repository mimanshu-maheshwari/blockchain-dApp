import hre from "hardhat";
import fs from "node:fs";
import path from "node:path";

async function main() {
  const { ethers } = await hre.network.create();

  const contract = await ethers.deployContract("SimpleStorage", [100]);

  await contract.waitForDeployment();

  const address = await contract.getAddress();

  console.log("SimpleStorage deployed to:", address);

  const artifact = await hre.artifacts.readArtifact("SimpleStorage");

  const outputDir = path.join(process.cwd(), "frontend", "src");
  fs.mkdirSync(outputDir, { recursive: true });

  fs.writeFileSync(
    path.join(outputDir, "SimpleStorage.json"),
    JSON.stringify(
      {
        address,
        abi: artifact.abi
      },
      null,
      2
    )
  );

  console.log("ABI and address written to frontend/src/SimpleStorage.json");
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});
