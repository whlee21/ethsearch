import { element, by, ElementFinder } from 'protractor';

export class PostComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-post div table .btn-danger'));
    title = element.all(by.css('jhi-post div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async clickOnLastDeleteButton() {
        await this.deleteButtons.last().click();
    }

    async countDeleteButtons() {
        return this.deleteButtons.count();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class PostUpdatePage {
    pageTitle = element(by.id('jhi-post-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    titleInput = element(by.id('field_title'));
    contentInput = element(by.id('field_content'));
    ownerIdInput = element(by.id('field_ownerId'));
    ownerFirstNameInput = element(by.id('field_ownerFirstName'));
    ownerLastNameInput = element(by.id('field_ownerLastName'));
    ownerAccountInput = element(by.id('field_ownerAccount'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setTitleInput(title) {
        await this.titleInput.sendKeys(title);
    }

    async getTitleInput() {
        return this.titleInput.getAttribute('value');
    }

    async setContentInput(content) {
        await this.contentInput.sendKeys(content);
    }

    async getContentInput() {
        return this.contentInput.getAttribute('value');
    }

    async setOwnerIdInput(ownerId) {
        await this.ownerIdInput.sendKeys(ownerId);
    }

    async getOwnerIdInput() {
        return this.ownerIdInput.getAttribute('value');
    }

    async setOwnerFirstNameInput(ownerFirstName) {
        await this.ownerFirstNameInput.sendKeys(ownerFirstName);
    }

    async getOwnerFirstNameInput() {
        return this.ownerFirstNameInput.getAttribute('value');
    }

    async setOwnerLastNameInput(ownerLastName) {
        await this.ownerLastNameInput.sendKeys(ownerLastName);
    }

    async getOwnerLastNameInput() {
        return this.ownerLastNameInput.getAttribute('value');
    }

    async setOwnerAccountInput(ownerAccount) {
        await this.ownerAccountInput.sendKeys(ownerAccount);
    }

    async getOwnerAccountInput() {
        return this.ownerAccountInput.getAttribute('value');
    }

    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}

export class PostDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-post-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-post'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
