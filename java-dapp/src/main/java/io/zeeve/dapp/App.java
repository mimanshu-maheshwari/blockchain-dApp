package io.zeeve.dapp;

import java.io.IOException;
import java.util.List;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeEncoder;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import io.zeeve.dapp.contracts.solidity.DocumentRegistry;
import io.zeeve.dapp.contracts.solidity.DocumentRegistry.NotarizedEventResponse;

public class App {
  public static void main(String[] args) {
    try {
      var web3j = Web3j.build(new HttpService("http://localhost:8545"));

      Web3ClientVersion clientVersion = web3j.web3ClientVersion().send();
      EthBlockNumber blockNumber = web3j.ethBlockNumber().send();
      EthGasPrice gasPrice = web3j.ethGasPrice().send();

      System.out.println("Client Version  : " + clientVersion.getWeb3ClientVersion());
      System.out.println("Block Number    : " + blockNumber.getBlockNumber());
      System.out.println("Gas Price       : " + gasPrice.getGasPrice());

      String pk = "0x8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63";

      Credentials creds = Credentials.create(pk);

      final DocumentRegistry registryContract = DocumentRegistry.deploy(web3j, creds, new DefaultGasProvider()).send();

      String extractAddress = registryContract.getContractAddress();

      System.out.println("Contract Address : " + extractAddress);

      // ExecutorService executorService = Executors.newFixedThreadPool(1);
      // executorService.execute(() -> {
      // App.checkNotarizedEvents(registryContract);
      // });
      String documentHash = "QmXoypizjW3WknFiJnKLwHCnL72vedxjQkDDP1mXWo6uco";
      TransactionReceipt receipt = registryContract.notarizeDocument(documentHash)
          .send();
      App.checkNotarizedEvents(registryContract, receipt);
      String txHash = receipt.getTransactionHash();
      System.out.println("Transaction Hash : " + txHash);

      boolean isNotarized = registryContract.isNotarized(documentHash).send();
      System.out.println("Document notarized confirmation : " + isNotarized);

    } catch (IOException ex) {
      throw new RuntimeException("Error whilst sending json-rpc requests", ex);
    } catch (Exception ex) {
      throw new RuntimeException("Error while deploying smart contract", ex);
    }
  }

  private static void checkNotarizedEvents(DocumentRegistry registryContract, TransactionReceipt receipt) {
    List<NotarizedEventResponse> events = DocumentRegistry.getNotarizedEvents(receipt);
    if (events == null) {
      return;
    }
    for (NotarizedEventResponse event : events) {
      final String notary = event._signer;
      final String documentHash = event._documentHash;
      System.out.println("Notary event: [Notary: %s, Hash: %s]".formatted(notary, documentHash));
    }
  }

  @SuppressWarnings("unused")
  private static void checkNotarizedEvents(DocumentRegistry registryContract) {

    final EthFilter ethFilter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST,
        registryContract.getContractAddress());
    ethFilter.addSingleTopic(EventEncoder.encode(DocumentRegistry.NOTARIZED_EVENT));
    ethFilter.addOptionalTopics("0x" + TypeEncoder.encode(new Address("0x00a329c0648769a73afac7f9381e08fb43dbea72")));
    // registryContract.notarizedEventFlowable(DefaultBlockParameterName.EARLIEST,
    // DefaultBlockParameterName.LATEST)
    registryContract.notarizedEventFlowable(ethFilter)
        .subscribe((NotarizedEventResponse event) -> {
          final String notary = event._signer;
          final String documentHash = event._documentHash;
          System.out.println("Notary event: [Notary: %s, Hash: %s]".formatted(notary, documentHash));
        });

  }
}
