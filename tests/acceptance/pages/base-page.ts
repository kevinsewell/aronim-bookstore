import { expect, Page } from "@playwright/test";
import { Logger } from "winston";
import { logger } from "@utils/logger";

export class BasePage {
  protected readonly logger: Logger;

  constructor(
    protected readonly page: Page,
    protected readonly title: string | null,
  ) {
    this.logger = logger;

    page.on("console", (msg) => {
      if (msg.type() === "error") {
        this.logger.error(msg.text());
      } else {
        this.logger.info(msg.text());
      }
    });
  }

  async wait(): Promise<void> {
    await this.page.waitForFunction(`document.title === "${this.title}"`);

    this.logger.info("[BasePage] Loaded");
  }

  async verifyTitle(expectedTitle: string): Promise<void> {
    const actualTitle = await this.page.title();

    expect(actualTitle).toBe(expectedTitle);

    this.logger.info("[BasePage] Title verified");
  }
}
