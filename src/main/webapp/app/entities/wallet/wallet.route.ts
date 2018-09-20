import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Wallet } from 'app/shared/model/wallet.model';
import { WalletService } from './wallet.service';
import { WalletComponent } from './wallet.component';
import { WalletDetailComponent } from './wallet-detail.component';
import { WalletUpdateComponent } from './wallet-update.component';
import { WalletDeletePopupComponent } from './wallet-delete-dialog.component';
import { IWallet } from 'app/shared/model/wallet.model';

@Injectable({ providedIn: 'root' })
export class WalletResolve implements Resolve<IWallet> {
    constructor(private service: WalletService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((wallet: HttpResponse<Wallet>) => wallet.body));
        }
        return of(new Wallet());
    }
}

export const walletRoute: Routes = [
    {
        path: 'wallet',
        component: WalletComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ethsearchApp.wallet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'wallet/:id/view',
        component: WalletDetailComponent,
        resolve: {
            wallet: WalletResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ethsearchApp.wallet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'wallet/new',
        component: WalletUpdateComponent,
        resolve: {
            wallet: WalletResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ethsearchApp.wallet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'wallet/:id/edit',
        component: WalletUpdateComponent,
        resolve: {
            wallet: WalletResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ethsearchApp.wallet.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const walletPopupRoute: Routes = [
    {
        path: 'wallet/:id/delete',
        component: WalletDeletePopupComponent,
        resolve: {
            wallet: WalletResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ethsearchApp.wallet.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
