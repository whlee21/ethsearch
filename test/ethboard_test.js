var ethBoard = artifacts.require("ethBoard");

contract('ethboardTest', function(accounts) {
  const contractAddress = "0x1b80bb3cc5fcf732f7b3e061e99c2fe49450cc34";
  it('should deploy the ethBoard the address', function(done){
    ethBoard.at(contractAddress).then(async function(instance) {
        const ethBoard = await instance;
        console.log(ethBoard);
        assert(ethBoard, 'ethBoard address couldn\'t be stored');
        done();
   });
  });

  it('get numbaer of posts', function(done){
    ethBoard.at(contractAddress).then(async function(instance) {
        assert(ethBoard, 'ethBoard address couldn\'t be stored');
        done();
   });
  });

  it('add post', function(done){
    ethBoard.at(contractAddress).then(async function(instance) {
        const data = await instance.addPost('post1', 'post1 contest', 'testID1', 'first1', 'second1');
        done();
   });
});

it('add post2', function(done){
  ethBoard.at(contractAddress).then(async function(instance) {
      const data = await instance.addPost('post2', 'post2 contest', 'testID2', 'first2', 'second2');
      done();
 });
});

it('get numbaer of posts', function(done){
  ethBoard.at(contractAddress).then(async function(instance) {
      const ethBoard = await instance.getNumOfPosts.call();
      console.log(ethBoard);
      done();
 });
});

it('get numbaer of account', function(done){
  ethBoard.at(contractAddress).then(async function(instance) {
      const ethBoard = await instance.getNumOfOwnerAccount.call();
      console.log(ethBoard);
      done();
 });
});

it('get All PostIds', function(done){
  ethBoard.at(contractAddress).then(async function(instance) {
      const ethBoard = await instance.getAllPostIds.call();
      console.log(ethBoard);
      done();
 });
});

it('get All OwnerAccount', function(done){
  ethBoard.at(contractAddress).then(async function(instance) {
      const ethBoard = await instance.getAllOwnerAccount.call();
      console.log(ethBoard);
      done();
 });
});


it('get post', function(done){
  ethBoard.at(contractAddress).then(async function(instance) {
      const post = await instance.getPost(0);
      console.log("ID: " ,post[0].toNumber());
      console.log("Title: " ,post[1]);
      console.log("Content: " ,post[2]);
      console.log("Owner Id " ,post[3]);
      console.log("Owner first name " ,post[4]);
      console.log("Owner last name " ,post[5]);
      console.log("Owner account " ,post[6]);
      done();
 });
});
it('get post', function(done){
  ethBoard.at(contractAddress).then(async function(instance) {
      const post = await instance.getPost(1);
      console.log("ID: " ,post[0].toNumber());
      console.log("Title: " ,post[1]);
      console.log("Content: " ,post[2]);
      console.log("Owner Id " ,post[3]);
      console.log("Owner first name " ,post[4]);
      console.log("Owner last name " ,post[5]);
      console.log("Owner account " ,post[6]);
      done();
 });
});


});
