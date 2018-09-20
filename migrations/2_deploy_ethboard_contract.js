var ethboard = artifacts.require("./Ethboard.sol")

module.exports = function(deployer,network, accounts) {
  deployer.deploy(ethboard, {from: accounts[1]})
}