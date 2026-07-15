// SPDX-License-Identifier: MIT 

pragma solidity ^0.8.20; 

contract SimpleStorage {
    uint256 private value; 
    address public owner; 

    event ValueChanged(address indexed changedBy, uint256 oldValue, uint256 newValue);

    constructor(uint256 initialValue) {
        owner = msg.sender;
        value = initialValue;
    }

    function setValue(uint256 newValue) external {
        uint256 oldValue = value;
        value = newValue;

        emit ValueChanged(msg.sender, oldValue, newValue);
    }

    function getValue() external view returns (uint256) {
        return value;
    }

}
