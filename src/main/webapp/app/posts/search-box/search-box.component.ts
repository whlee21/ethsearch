import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { PostService } from 'app/posts/post.service';

@Component({
    selector: 'jhi-search-box',
    templateUrl: './search-box.component.html',
    styleUrls: ['search-box.component.scss'],
    providers: [
        // PostService
    ]
})
export class SearchBoxComponent implements OnInit {
    _postCategory: string;

    // @Input() postCategory: string;

    page: any;
    predicate: any;
    reverse: any;

    @Input('selectedValue') selectedValue: string;
    @Input()
    set postCategory(value: string) {
        if (value != null) {
            this._postCategory = 'category' + value;
        } else {
            this._postCategory = value;
        }
    }

    @Output() searchEvent = new EventEmitter();

    keyword = null;

    constructor(private postService: PostService) {}

    ngOnInit() {}

    setKeyword(keyword: string): void {
        this.keyword = keyword;
        this.searchEvent.emit({
            keyword: `${this.keyword}`
            /*
      keyword: `${this.keyword}`,
      category: `${this._postCategory.replace('category: ', '')}`
      */
        });

        this.postService.search({
            page: this.page - 1,
            query: this.keyword,
            sort: ['id,' + (this.reverse ? 'asc' : 'desc')]
        });
    }

    inputChange(): void {}
}
