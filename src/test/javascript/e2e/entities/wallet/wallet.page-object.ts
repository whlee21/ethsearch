import { element, by, ElementFinder } from 'protractor';

export class WalletComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-wallet div table .btn-danger'));
    title = element.all(by.css('jhi-wallet div h2#page-heading span')).first();

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

export class WalletUpdatePage {
    pageTitle = element(by.id('jhi-wallet-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    accountInput = element(by.id('field_account'));
    privateKeyInput = element(by.id('field_privateKey'));
    userSelect = element(by.id('field_user'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setAccountInput(account) {
        await this.accountInput.sendKeys(account);
    }

    async getAccountInput() {
        return this.accountInput.getAttribute('value');
    }

    async setPrivateKeyInput(privateKey) {
        await this.privateKeyInput.sendKeys(privateKey);
    }

    async getPrivateKeyInput() {
        return this.privateKeyInput.getAttribute('value');
    }

    async userSelectLastOption() {
        await this.userSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async userSelectOption(option) {
        await this.userSelect.sendKeys(option);
    }

    getUserSelect(): ElementFinder {
        return this.userSelect;
    }

    async getUserSelectedOption() {
        return this.userSelect.element(by.css('option:checked')).getText();
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

export class WalletDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-wallet-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-wallet'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
