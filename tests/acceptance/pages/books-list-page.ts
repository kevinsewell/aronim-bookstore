import { Locator, Page } from "@playwright/test";
import { BaseAuthenticatedPage } from "@pages/base-authenticated-page";

export class BooksListPage extends BaseAuthenticatedPage {
  readonly createButtonSelector = "button.refine-create-button";
  readonly createButton: Locator;

  constructor(page: Page, title: string) {
    super(page, title);
    this.createButton = page.locator(this.createButtonSelector);
  }

  async create(): Promise<void> {
    await this.createButton.click();
  }
}
