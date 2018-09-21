pragma solidity ^0.4.18;
// written for Solidity version 0.4.18 and above that doesnt break functionality

contract ethBoard {
    
    event AddedPost(uint postID, string title, string content, string ownerId, string firstName, string lastName, address ownerAccount);

    struct Post {
        uint uid;
        string title;
        string content;
        string ownerId;
        string ownerFirstName;
        string ownerLastName;
        address ownerAccount;
    }

    uint lastPostId;
    uint numPosts;
    uint numOwnerAccount;

    uint[] postIds;
    address[] ownerAccounts;

    mapping (uint => Post) posts;       
    mapping (address => uint[]) userToPostMap;

    
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *  These functions perform transactions, editing the mappings *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    function addPost(string title, string content, string ownerId, string ownerFirstName, string ownerLastName) public {
        // user is the return variable
        uint postID = lastPostId++;
        numPosts++;
        // Create new user Struct with name and saves it to storage.
        posts[postID] = Post(postID, title, content, ownerId, ownerFirstName, ownerLastName, msg.sender);
        if(userToPostMap[msg.sender].length == 0) {
            ownerAccounts.push(msg.sender);
            numOwnerAccount++;
        }
        userToPostMap[msg.sender].push(postID);
        postIds.push(postID);
        emit AddedPost(postID, title, content, ownerId, ownerFirstName, ownerLastName, msg.sender);
    }

    function getAllPostIds() public view returns(uint[]) {
        return postIds;
    }

    function getAllOwnerAccount() public view returns(address[]) {
        return ownerAccounts;
    }

    function getNumOfPosts() public view returns(uint) {
        return numPosts;
    }

    function getNumOfOwnerAccount() public view returns(uint) {
        return numOwnerAccount;
    }

    // returns user information, including its ID, name, and description
    function getPost(uint uid) public view returns (uint, string, string, string, string, string, address) {
        return (uid, posts[uid].title, posts[uid].content, posts[uid].ownerId, posts[uid].ownerFirstName ,posts[uid].ownerLastName, posts[uid].ownerAccount);
    }

    // returns post IDs by owner account
    function getPostIdsByAccount(address ownerAccount) public view returns (uint[]) {
        return userToPostMap[ownerAccount];
    }
}