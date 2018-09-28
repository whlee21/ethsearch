package io.blocko.ethsearch.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.blocko.ethsearch.web.api.BoardApiDelegate;
import io.blocko.ethsearch.web.api.model.Post;

@Service
@Transactional
public class PostApiDelegateService implements BoardApiDelegate {

    private final Logger log = LoggerFactory.getLogger(PostApiDelegateService.class);

    @Autowired
    private PostService postService;

    // @Override
    // public ResponseEntity<List<Post>> getAllPosts(Pageable pageable) {
    //     Page<Post> posts = postService.findAll(pageable);
    //     return ResponseEntity.ok(

    //     );
    // }

}
