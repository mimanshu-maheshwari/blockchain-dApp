// SPDX-License-Identifier: MIT 

pragma solidity ^0.8.36;

/**
 * @dev Smart Contract responsible to notarize documents on the Ethereum Blockchain.
 */
contract DocumentRegistry {

  struct Document {
    address signer; // Notary 
    uint date;      // data or notarization 
    string hash;    // Document hash 
  }

  /**
   * @dev Storage space used to record all documents notarized with metadata.
   */
  mapping(bytes32 => Document) registry;

  /**
   * @dev Notrarize a document identified by the hash of teh document hash. The sender and data in the registry. 
   * @dev Emit an event noterized in case of success
   * @param _documentHash Document hash
   */
  function notarizeDocument(string calldata _documentHash) external returns (bool) {
    bytes32 id = keccak256(abi.encodePacked(_documentHash));
    registry[id].signer = msg.sender;
    registry[id].date = block.timestamp;
    registry[id].hash = _documentHash;

    emit Notarized(msg.sender, _documentHash);
    return true;
  }

  /**
   * @dev Verify a document its hash was notarized in the registry previously
   * @param _documentHash Document hash
   * @return bool if document was noterized previously in the registry
   */
  function isNotarized(string calldata _documentHash) external view returns (bool) {
    return registry[keccak256(abi.encodePacked(_documentHash))].signer != address(0);
  }

  /**
   * @dev defination of event triggered when a document is successfully notarized in the registry
   * @param _signer Signer 
   * @param _documentHash Document Hash
   */
  event Notarized(address indexed _signer, string _documentHash);

}
