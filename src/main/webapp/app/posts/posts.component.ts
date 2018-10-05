import { SearchBoxComponent } from './search-box/search-box.component';
import { Component, OnInit, ViewChild, ViewChildren, QueryList, ElementRef } from '@angular/core';

@Component({
    selector: 'jhi-posts',
    templateUrl: './posts.component.html',
    styleUrls: ['posts.component.scss']
})
export class PostsComponent implements OnInit {
    selectedValue = null;
    displayCategoryName = null;
    postCategory = [{ value: 'all', viewValue: '모든 게시글' }, { value: 'admin', viewValue: '관리자 게시글' }];

    searchTitle = null;

    constructor() {}

    ngOnInit() {}

    changeValue(category: string): void {
        for (const element of this.postCategory) {
            if (element.value === category) {
                this.displayCategoryName = element.viewValue;
            }
        }
    }

    changeTitleBar(searchInfo): void {
        this.searchTitle = `${searchInfo.keyword} ( ${searchInfo.category})`;
        console.log(this.searchTitle);
    }

    // tslint:disable-next-line:member-ordering
    @ViewChild(SearchBoxComponent) searchComp: SearchBoxComponent;
    // tslint:disable-next-line:member-ordering
    @ViewChildren(SearchBoxComponent) searchCompArr: QueryList<SearchBoxComponent>;

    clearCondition(): void {
        this.selectedValue = null;
        this.displayCategoryName = null;
        /*
    @ViewChild를 사용할 경우
    this.searchComp._postCategory = null;
    this.searchComp.keyword = null;
*/
        // @ViewChildren을 사용할 경우
        this.searchCompArr.toArray()[0]._postCategory = null;
        this.searchCompArr.toArray()[0].keyword = null;
    }

    // tslint:disable-next-line:member-ordering
    @ViewChild('resultStatus') resultToolbar: ElementRef;

    changeDOM(): void {
        this.resultToolbar.nativeElement.onclick = function() {
            alert('DOM을 직접 제어할 수 있어요');
        };
        this.resultToolbar.nativeElement.innerHTML = '클릭해보세요';
    }
}
