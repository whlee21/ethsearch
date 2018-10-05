import { Component, OnInit, ViewChild } from '@angular/core';
import { IPost } from 'app/shared/model/post.model';
import { MatTableDataSource, MatPaginator } from '@angular/material';
import { SERVER_API_URL } from 'app/app.constants';
import { PostService } from 'app/posts/post.service';
import { SelectionModel } from '@angular/cdk/collections';

@Component({
    selector: 'jhi-list-box',
    templateUrl: './list-box.component.html',
    styleUrls: ['list-box.component.scss']
})
export class ListBoxComponent implements OnInit {
    // displayedColumns = ['id', 'title', 'content', 'ownerId', 'ownerFirstName', 'ownerLastName', 'ownerAccount'];
    displayedColumns = ['id', 'title', 'ownerId'];
    posts: IPost[];
    dataSource;

    // event 처리
    selection = new SelectionModel<IPost>(false, []);

    @ViewChild(MatPaginator) paginator: MatPaginator;

    constructor(private postService: PostService) {
        this.postService.updatePosts.subscribe(data => {
            this.posts = data;
            this.dataSource = new MatTableDataSource<IPost>(this.posts);
            this.dataSource.paginator = this.paginator;
        });
    }

    ngOnInit() {
        this.postService.loadAll();
    }

    rowSelect(row) {
        this.selection.select(row);
        this.postService.updateSelectedPost.next(this.selection.selected[0]);
    }
}
