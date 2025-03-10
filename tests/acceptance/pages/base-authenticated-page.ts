import { expect, Locator, Page } from "@playwright/test";
import { BasePage } from "@pages/base-page";

export class BaseAuthenticatedPage extends BasePage {
  protected readonly logoutButtonSelector: string = "span[aria-label='logout']";
  protected readonly logoutButton: Locator;
  protected readonly userFullNameSpanSelector: string =
    "span[data-testid='user-full-name']";
  protected readonly userFullNameSpan: Locator;

  constructor(page: Page, title: string) {
    super(page, title);
    this.logoutButton = page.locator(this.logoutButtonSelector);
    this.userFullNameSpan = page.locator(this.userFullNameSpanSelector);
  }

  async wait(): Promise<void> {
    await super.wait();
    await this.page.waitForSelector(this.logoutButtonSelector);

    this.logger.info("[BaseAuthenticatedPage] Loaded");
  }

  async verifyUserFullName(expectedUserFullName: string): Promise<void> {
    const actualUserFullName = await this.userFullNameSpan.textContent();

    expect(actualUserFullName).toBe(expectedUserFullName);

    this.logger.info("[BooksPage] User full name verified");
  }
}
