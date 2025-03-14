import { createBdd, test as base } from "playwright-bdd";
import { BooksApi } from "@api/books-api";
import { BasePage } from "@pages/base-page";
import { BooksCreatePage } from "@pages/books-create-page";
import { BooksListPage } from "@pages/books-list-page";
import { LandingPage } from "@pages/landing-page";
import { SignInPage } from "@pages/sign-in-page";
import axiosInstance, { AxiosInstance } from "axios";
import * as dotenv from "dotenv";

dotenv.config({path: "../../.env"});

export type Fixtures = {
  axiosInstance: AxiosInstance;
  basePage: BasePage;
  booksApi: BooksApi;
  booksCreatePage: BooksCreatePage;
  booksListPage: BooksListPage;
  landingPage: LandingPage;
  signInPage: SignInPage;
};

export const test = base.extend<Fixtures>({
  axiosInstance: async ({}, use) => {
    await use(axiosInstance);
  },
  basePage: async ({ page }, use) => {
    await use(new BasePage(page, null));
  },
  booksApi: async ({ axiosInstance }, use) => {
    await use(new BooksApi(axiosInstance));
  },
  booksCreatePage: async ({ page }, use) => {
    await use(new BooksCreatePage(page, "Create new Book | Refine"));
  },
  booksListPage: async ({ page }, use) => {
    await use(new BooksListPage(page, "Books | Refine"));
  },
  landingPage: async ({ page }, use) => {
    await use(new LandingPage(page, "Refine"));
  },
  signInPage: async ({ page }, use) => {
    await use(new SignInPage(page, "Sign in to Aronim Bookstore"));
  },
});

export const { After, Before, Given, When, Then } = createBdd(test);
