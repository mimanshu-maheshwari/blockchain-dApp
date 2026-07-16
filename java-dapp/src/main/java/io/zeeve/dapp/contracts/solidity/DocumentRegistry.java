package io.zeeve.dapp.contracts.solidity;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/LFDT-web3j/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.8.0.
 */
@SuppressWarnings("rawtypes")
@Generated("org.web3j.codegen.SolidityFunctionWrapperGenerator")
public class DocumentRegistry extends Contract {
    public static final String BINARY = "6080604052348015600f57600080fd5b506103d38061001f6000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c80636a33bf871461003b57806373ca4a5b14610062575b600080fd5b61004e61004936600461017d565b610075565b604051901515815260200160405180910390f35b61004e61007036600461017d565b610123565b600080838360405160200161008b9291906101f1565b60408051601f198184030181529181528151602092830120600081815292839052912080546001600160a01b031916331781554260018201559091506002016100d58486836102ad565b50336001600160a01b03167f4208034e4449bb439a4b1ba16ff193bec2c83ec39cd969a629aa475c7f0b8047858560405161011192919061036e565b60405180910390a25060019392505050565b6000806001600160a01b031660008085856040516020016101459291906101f1565b60408051808303601f19018152918152815160209283012083529082019290925201600020546001600160a01b031614159392505050565b6000806020838503121561019057600080fd5b823567ffffffffffffffff8111156101a757600080fd5b8301601f810185136101b857600080fd5b803567ffffffffffffffff8111156101cf57600080fd5b8560208284010111156101e157600080fd5b6020919091019590945092505050565b8183823760009101908152919050565b634e487b7160e01b600052604160045260246000fd5b600181811c9082168061022b57607f821691505b60208210810361024b57634e487b7160e01b600052602260045260246000fd5b50919050565b601f8211156102a857828211156102a857806000526020600020601f840160051c602085101561027f575060005b90810190601f840160051c0360005b818110156102a45760008382015560010161028e565b5050505b505050565b67ffffffffffffffff8311156102c5576102c5610201565b6102d9836102d38354610217565b83610251565b6000601f84116001811461030d57600085156102f55750838201355b600019600387901b1c1916600186901b178355610367565b600083815260209020601f19861690835b8281101561033e578685013582556020948501946001909201910161031e565b508682101561035b5760001960f88860031b161c19848701351681555b505060018560011b0183555b5050505050565b60208152816020820152818360408301376000818301604090810191909152601f909201601f1916010191905056fea264697066735822122005b44c6d857763a284929ddb1268c4f035002e884ec7823e476dcb6b723627e164736f6c63430008240033";

    private static String librariesLinkedBinary;

    public static final String FUNC_ISNOTARIZED = "isNotarized";

    public static final String FUNC_NOTARIZEDOCUMENT = "notarizeDocument";

    public static final Event NOTARIZED_EVENT = new Event("Notarized", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected DocumentRegistry(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected DocumentRegistry(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected DocumentRegistry(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected DocumentRegistry(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<NotarizedEventResponse> getNotarizedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(NOTARIZED_EVENT, transactionReceipt);
        ArrayList<NotarizedEventResponse> responses = new ArrayList<NotarizedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NotarizedEventResponse typedResponse = new NotarizedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._signer = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._documentHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static NotarizedEventResponse getNotarizedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(NOTARIZED_EVENT, log);
        NotarizedEventResponse typedResponse = new NotarizedEventResponse();
        typedResponse.log = log;
        typedResponse._signer = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse._documentHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<NotarizedEventResponse> notarizedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getNotarizedEventFromLog(log));
    }

    public Flowable<NotarizedEventResponse> notarizedEventFlowable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NOTARIZED_EVENT));
        return notarizedEventFlowable(filter);
    }

    public RemoteFunctionCall<Boolean> isNotarized(String _documentHash) {
        final Function function = new Function(FUNC_ISNOTARIZED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_documentHash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> notarizeDocument(String _documentHash) {
        final Function function = new Function(
                FUNC_NOTARIZEDOCUMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_documentHash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static DocumentRegistry load(String contractAddress, Web3j web3j,
            Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new DocumentRegistry(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static DocumentRegistry load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new DocumentRegistry(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static DocumentRegistry load(String contractAddress, Web3j web3j,
            Credentials credentials, ContractGasProvider contractGasProvider) {
        return new DocumentRegistry(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static DocumentRegistry load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new DocumentRegistry(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<DocumentRegistry> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(DocumentRegistry.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<DocumentRegistry> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DocumentRegistry.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<DocumentRegistry> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(DocumentRegistry.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<DocumentRegistry> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DocumentRegistry.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static void linkLibraries(List<Contract.LinkReference> references) {
        librariesLinkedBinary = linkBinaryWithReferences(BINARY, references);
    }

    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class NotarizedEventResponse extends BaseEventResponse {
        public String _signer;

        public String _documentHash;
    }
}
