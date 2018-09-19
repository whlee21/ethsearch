pragma solidity ^0.4.18;
// written for Solidity version 0.4.18 and above that doesnt break functionality

contract ethBoard {

   event AddedPost(uint postID, string title, string content, string ownerName, address account);

   struct Post {
       uint uid;
       string title;
       string content;
       string ownerName;
       address ownerAccount;
   }

   uint numPosts;

   mapping (uint => Post) posts;

   /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    *  These functions perform transactions, editing the mappings *
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

   function addPost(string title, string content, string ownerName) public {
       // user is the return variable
       uint postID = numPosts++;
       // Create new user Struct with name and saves it to storage.
       posts[postID] = Post(postID, title, content, ownerName, msg.sender);
       emit AddedPost(postID, title, content, ownerName, msg.sender);
   }

   function getNumOfPosts() public view returns(uint) {
       return numPosts;
   }

   // returns user information, including its ID, name, and description
   function getPost(uint uid) public view returns (uint, string, string, string, address) {
       return (uid, posts[uid].title, posts[uid].content, posts[uid].ownerName, posts[uid].ownerAccount);
   }
}
