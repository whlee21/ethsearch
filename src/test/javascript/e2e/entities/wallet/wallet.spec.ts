import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { WalletComponentsPage, WalletDeleteDialog, WalletUpdatePage } from './wallet.page-object';

describe('Wallet e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let walletUpdatePage: WalletUpdatePage;
    let walletComponentsPage: WalletComponentsPage;
    let walletDeleteDialog: WalletDeleteDialog;

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Wallets', async () => {
        await navBarPage.goToEntity('wallet');
        walletComponentsPage = new WalletComponentsPage();
        expect(await walletComponentsPage.getTitle()).toMatch(/ethsearchApp.wallet.home.title/);
    });

    it('should load create Wallet page', async () => {
        await walletComponentsPage.clickOnCreateButton();
        walletUpdatePage = new WalletUpdatePage();
        expect(await walletUpdatePage.getPageTitle()).toMatch(/ethsearchApp.wallet.home.createOrEditLabel/);
        await walletUpdatePage.cancel();
    });

    it('should create and save Wallets', async () => {
        await walletComponentsPage.clickOnCreateButton();
        await walletUpdatePage.setAccountInput('account');
        expect(await walletUpdatePage.getAccountInput()).toMatch('account');
        await walletUpdatePage.setPrivateKeyInput('privateKey');
        expect(await walletUpdatePage.getPrivateKeyInput()).toMatch('privateKey');
        await walletUpdatePage.userSelectLastOption();
        await walletUpdatePage.save();
        expect(await walletUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    it('should delete last Wallet', async () => {
        const nbButtonsBeforeDelete = await walletComponentsPage.countDeleteButtons();
        await walletComponentsPage.clickOnLastDeleteButton();

        walletDeleteDialog = new WalletDeleteDialog();
        expect(await walletDeleteDialog.getDialogTitle()).toMatch(/ethsearchApp.wallet.delete.question/);
        await walletDeleteDialog.clickOnConfirmButton();

        expect(await walletComponentsPage.countDeleteButtons()).toBe(nbButtonsBeforeDelete - 1);
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});
