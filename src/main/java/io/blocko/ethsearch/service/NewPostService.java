package io.blocko.ethsearch.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple7;

import io.blocko.ethsearch.config.ApplicationProperties;
import io.blocko.ethsearch.contract.EthBoard;
import io.blocko.ethsearch.domain.User;
import io.blocko.ethsearch.domain.Wallet;
import io.blocko.ethsearch.repository.UserRepository;
import io.blocko.ethsearch.repository.WalletRepository;
import io.blocko.ethsearch.repository.search.PostSearchRepository;
import io.blocko.ethsearch.security.SecurityUtils;
import io.blocko.ethsearch.web.api.model.Post;
import io.blocko.ethsearch.web.rest.errors.InternalServerErrorException;

import static io.blocko.ethsearch.config.Constants.*;
import static org.elasticsearch.index.query.QueryBuilders.*;

@Service
@Transactional
public class NewPostService {

    private final Logger log = LoggerFactory.getLogger(NewPostService.class);

    private final PostSearchRepository postSearchRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final String contractAddress;

	@Autowired
    Web3j web3j;
    //Credentials credentials;

    public NewPostService(PostSearchRepository postSearchRepository, WalletRepository walletRepository,
            UserRepository userRepository, ApplicationProperties applicationProperties) {
        this.postSearchRepository = postSearchRepository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        contractAddress = applicationProperties.getContractAddress();
    }

    private EthBoard loadContract() {
        //get wallet for current user
        List<Wallet> wallets = walletRepository.findByUserIsCurrentUser();
        //temporary code
        String privateKey = new String();
        for(Wallet wallet: wallets) {
            privateKey = wallet.getPrivateKey();
        }
        Credentials credentials = Credentials.create(privateKey);
        return EthBoard.load(contractAddress, web3j, credentials, GAS_PRICE,  GAS_LIMIT);
    }

    /**
     * Save a post.
     *
     * @param post the entity to save
     * @return the persisted entity
     */
    public Post save(Post post) throws Exception {
        log.debug("Request to save Post : {}", post);

        //find current user imformation
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        User user = userRepository.findOneByLogin(userLogin).orElse(new User());
        if (user.getId() == null) {
            throw new InternalServerErrorException("User could not be found");
        }

        //load contract
        EthBoard contract = loadContract();

        //add post
        TransactionReceipt addpost = contract.addPost(post.getTitle(), post.getContent(), user.getLogin(), user.getFirstName(), user.getLastName()).send();
        //temporary code
        //return last added post
        BigInteger numPost = contract.getNumOfPosts().send();
        log.info("=================================================");
        log.info("get num post: " + numPost);
        log.info("get post: " + contract.getPost(numPost.subtract(BigInteger.valueOf(1L))).send());
        log.info("=================================================");
        Post result = new Post();
        Tuple7<BigInteger, String, String, String, String, String, String> contractPost = contract.getPost(numPost.subtract(BigInteger.valueOf(1L))).send();

        result.setId(contractPost.getValue1().longValue());

        return result;
    }

    /**
     * Get all the posts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Post> findAll(Pageable pageable) throws Exception {
        log.debug("Request to get all Posts");
        //load contract
        EthBoard contract = loadContract();
        List<BigInteger> AllPostIds = contract.getAllPostIds().send();
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        int toIndex = (AllPostIds.size() > ((int)offset + pageSize))? ((int)offset + pageSize) : AllPostIds.size();
        List<BigInteger> currentPageIds = AllPostIds.subList((int)offset, toIndex);
        List<Post> posts = new ArrayList<>();
        for(BigInteger id: currentPageIds) {
            Post post = new Post();
            post.load(contract.getPost(id).send());
            posts.add(post);
        }
        Page<Post> result = new PageImpl<Post>(posts, pageable, AllPostIds.size());
        return result;
    }


    /**
     * Get one post by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Post> findOne(Long id) throws Exception {
        log.debug("Request to get Post : {}", id);
        //load contract
        EthBoard contract = loadContract();

        Post post = new Post();
        post.load(contract.getPost(BigInteger.valueOf(id)).send());
        Optional<Post> result = Optional.of(post);
        return result;
        //return postRepository.findById(id);
    }

    /**
     * Delete the post by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Post : {}", id);
        // postRepository.deleteById(id);
        // postSearchRepository.deleteById(id);
    }

    /**
     * Search for the post corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Post> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Posts for query {}", query);
        return postSearchRepository.search(queryStringQuery(query), pageable);    }
}
