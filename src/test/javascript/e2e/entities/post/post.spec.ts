import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PostComponentsPage, PostDeleteDialog, PostUpdatePage } from './post.page-object';

describe('Post e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let postUpdatePage: PostUpdatePage;
    let postComponentsPage: PostComponentsPage;
    let postDeleteDialog: PostDeleteDialog;

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Posts', async () => {
        await navBarPage.goToEntity('post');
        postComponentsPage = new PostComponentsPage();
        expect(await postComponentsPage.getTitle()).toMatch(/ethsearchApp.post.home.title/);
    });

    it('should load create Post page', async () => {
        await postComponentsPage.clickOnCreateButton();
        postUpdatePage = new PostUpdatePage();
        expect(await postUpdatePage.getPageTitle()).toMatch(/ethsearchApp.post.home.createOrEditLabel/);
        await postUpdatePage.cancel();
    });

    it('should create and save Posts', async () => {
        await postComponentsPage.clickOnCreateButton();
        await postUpdatePage.setTitleInput('title');
        expect(await postUpdatePage.getTitleInput()).toMatch('title');
        await postUpdatePage.setContentInput('content');
        expect(await postUpdatePage.getContentInput()).toMatch('content');
        await postUpdatePage.setOwnerIdInput('ownerId');
        expect(await postUpdatePage.getOwnerIdInput()).toMatch('ownerId');
        await postUpdatePage.setOwnerFirstNameInput('ownerFirstName');
        expect(await postUpdatePage.getOwnerFirstNameInput()).toMatch('ownerFirstName');
        await postUpdatePage.setOwnerLastNameInput('ownerLastName');
        expect(await postUpdatePage.getOwnerLastNameInput()).toMatch('ownerLastName');
        await postUpdatePage.setOwnerAccountInput('ownerAccount');
        expect(await postUpdatePage.getOwnerAccountInput()).toMatch('ownerAccount');
        await postUpdatePage.save();
        expect(await postUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    it('should delete last Post', async () => {
        const nbButtonsBeforeDelete = await postComponentsPage.countDeleteButtons();
        await postComponentsPage.clickOnLastDeleteButton();

        postDeleteDialog = new PostDeleteDialog();
        expect(await postDeleteDialog.getDialogTitle()).toMatch(/ethsearchApp.post.delete.question/);
        await postDeleteDialog.clickOnConfirmButton();

        expect(await postComponentsPage.countDeleteButtons()).toBe(nbButtonsBeforeDelete - 1);
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});
