import { BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { IPost } from 'app/shared/model/post.model';
import { createRequestOption } from 'app/shared';

@Injectable({
    providedIn: 'root'
})
export class PostService {
    private resourceUrl = SERVER_API_URL + 'api/posts';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/posts';

    posts: IPost[];
    selectedPost: IPost = {
        id: null,
        title: '',
        content: '',
        ownerId: '',
        ownerFirstName: '',
        ownerLastName: '',
        ownerAccount: ''
    };

    updatePosts: BehaviorSubject<IPost[]> = new BehaviorSubject<IPost[]>(this.posts);

    updateSelectedPost: BehaviorSubject<IPost> = new BehaviorSubject<IPost>(this.selectedPost);

    constructor(private http: HttpClient) {}

    search(req?: any) {
        console.log(req);
        const options = createRequestOption(req);
        this.http.get<IPost[]>(this.resourceSearchUrl, { params: options }).subscribe(res => {
            this.updatePosts.next(res);
            this.posts = res;
            console.log(this.posts);
        });
    }

    loadAll() {
        this.http.get<IPost[]>(this.resourceUrl).subscribe(res => {
            this.updatePosts.next(res);
            this.posts = res;
            console.log(this.posts);
        });
    }

    getPosts(): IPost[] {
        return this.posts;
    }
}
