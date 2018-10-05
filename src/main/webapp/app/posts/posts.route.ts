import { Route } from '@angular/router';
import { PostsComponent } from './posts.component';
import { UserRouteAccessService } from 'app/core';

export const POST_ROUTE: Route = {
    path: 'posts',
    component: PostsComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'ethsearchApp.post.home.title'
    },
    canActivate: [UserRouteAccessService]
};
