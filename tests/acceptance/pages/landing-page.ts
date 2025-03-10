import { Locator, Page } from "@playwright/test";
import { BasePage } from "@pages/base-page";

export class LandingPage extends BasePage {
  readonly loginButtonSelector = "button[data-testid='login']";
  readonly loginButton: Locator;

  constructor(page: Page, title: string) {
    super(page, title);
    this.loginButton = page.locator(this.loginButtonSelector);
  }

  async open(): Promise<void> {
    await this.page.goto("/");

    this.logger.info("[LandingPage] Opened");

    await this.wait();
  }

  async wait(): Promise<void> {
    await super.wait();
    await this.page.waitForSelector(this.loginButtonSelector);

    this.logger.info("[LandingPage] Loaded");
  }

  async signin(): Promise<void> {
    await this.loginButton.click();

    this.logger.info("[LandingPage] 'login' button clicked");
  }
}
