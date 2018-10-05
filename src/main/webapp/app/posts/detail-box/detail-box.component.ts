import { PostService } from 'app/posts/post.service';
import { Component, OnInit } from '@angular/core';
import { IPost } from 'app/shared/model/post.model';

@Component({
    selector: 'jhi-detail-box',
    templateUrl: './detail-box.component.html',
    styleUrls: ['detail-box.component.scss']
})
export class DetailBoxComponent implements OnInit {
    post: IPost;

    constructor(private postService: PostService) {
        this.postService.updateSelectedPost.subscribe(selectedPost => {
            this.post = selectedPost;
        });
    }

    ngOnInit() {}
}
