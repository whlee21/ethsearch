import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { POST_ROUTE } from 'app/posts/posts.route';
import { PostsComponent } from 'app/posts/posts.component';
import { SearchBoxComponent } from 'app/posts/search-box/search-box.component';
import { DetailBoxComponent } from './detail-box/detail-box.component';
import { ListBoxComponent } from './list-box/list-box.component';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule, MatTableModule, MatPaginatorModule, MatOptionModule, MatSelectModule } from '@angular/material';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';

import { MatCardModule } from '@angular/material/card';
// 양방향 바인딩을 위한 FormsModule import
import { FormsModule } from '@angular/forms';

// COMPOSITION_BUFFER_MODE import
import { COMPOSITION_BUFFER_MODE } from '@angular/forms';
import { PostService } from 'app/posts/post.service';
import { TextColorDirective } from './text-color.directive';
@NgModule({
    imports: [
        CommonModule,
        RouterModule.forChild([POST_ROUTE]),
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        MatToolbarModule,
        MatCardModule,
        FormsModule,
        MatTableModule,
        MatPaginatorModule,
        MatOptionModule,
        MatSelectModule
    ],
    providers: [
        {
            provide: COMPOSITION_BUFFER_MODE,
            useValue: false
        },
        PostService
    ],
    declarations: [PostsComponent, SearchBoxComponent, DetailBoxComponent, TextColorDirective, ListBoxComponent],
    entryComponents: [PostsComponent]
})
export class PostsModule {}
