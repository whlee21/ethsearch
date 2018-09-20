import { IUser } from 'app/core/user/user.model';

export interface IWallet {
    id?: number;
    account?: string;
    privateKey?: string;
    user?: IUser;
}

export class Wallet implements IWallet {
    constructor(public id?: number, public account?: string, public privateKey?: string, public user?: IUser) {}
}
