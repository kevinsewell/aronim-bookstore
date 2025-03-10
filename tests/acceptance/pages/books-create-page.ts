import { Locator, Page } from "@playwright/test";
import { BaseAuthenticatedPage } from "@pages/base-authenticated-page";

export class BooksCreatePage extends BaseAuthenticatedPage {
  readonly isbnInputSelector = "input#isbn";
  readonly isbnInput: Locator;

  readonly titleInputSelector = "input#title";
  readonly titleInput: Locator;

  readonly authorFirstNameInputSelector = "input#authorFirstName";
  readonly authorFirstNameInput: Locator;

  readonly authorLastNameInputSelector = "input#authorLastName";
  readonly authorLastNameInput: Locator;

  readonly publisherNameInputSelector = "input#publisherName";
  readonly publisherNameInput: Locator;

  readonly priceInputSelector = "input#price";
  readonly priceInput: Locator;

  readonly saveButtonSelector = "button.refine-save-button";
  readonly saveButton: Locator;

  constructor(page: Page, title: string) {
    super(page, title);
    this.isbnInput = page.locator(this.isbnInputSelector);
    this.titleInput = page.locator(this.titleInputSelector);
    this.authorFirstNameInput = page.locator(this.authorFirstNameInputSelector);
    this.authorLastNameInput = page.locator(this.authorLastNameInputSelector);
    this.publisherNameInput = page.locator(this.publisherNameInputSelector);
    this.priceInput = page.locator(this.priceInputSelector);
    this.saveButton = page.locator(this.saveButtonSelector);
  }

  async wait(): Promise<void> {
    await this.page.waitForSelector(this.saveButtonSelector);

    this.logger.info("[BooksCreatePage] Loaded");
  }

  async enterIsbn(isbn: string): Promise<void> {
    await this.isbnInput.fill(isbn);
  }

  async enterTitle(title: string): Promise<void> {
    await this.titleInput.fill(title);
  }

  async enterAuthorFirstName(authorFirstName: string): Promise<void> {
    await this.authorFirstNameInput.fill(authorFirstName);
  }

  async enterAuthorLastName(authorLastName: string): Promise<void> {
    await this.authorLastNameInput.fill(authorLastName);
  }

  async enterPublisherName(publisher: string): Promise<void> {
    await this.publisherNameInput.fill(publisher);
  }

  async enterPrice(price: string): Promise<void> {
    await this.priceInput.fill(price);
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }
}
