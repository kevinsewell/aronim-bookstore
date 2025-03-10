import { expect, Locator, Page } from "@playwright/test";
import { BasePage } from "@pages/base-page";

export class SignInPage extends BasePage {
  readonly loginButtonSelector = "button[id='kc-login']";

  readonly loginButton: Locator;
  readonly passwordInput: Locator;
  readonly usernameInput: Locator;

  constructor(page: Page, title: string) {
    super(page, title);
    this.loginButton = page.locator(this.loginButtonSelector);
    this.passwordInput = page.locator("input[id='password']");
    this.usernameInput = page.locator("input[id='username']");
  }

  async wait(): Promise<void> {
    await this.page.waitForSelector(this.loginButtonSelector);

    this.logger.info("[SignInPage] Loaded");
  }

  async signIn(username: string, password: string): Promise<void> {
    await this.usernameInput.fill(username);
    this.logger.info("[SignInPage] Filled in 'username' input");

    await this.passwordInput.fill(password);
    this.logger.info("[SignInPage] Filled in 'password' input");

    await this.loginButton.click();
    this.logger.info("[SignInPage] 'kc-login' button clicked");

    await this.page.waitForURL(/https:\/\/bookstore.aronim.local\/.*/);
  }
}
