import { After, Before, Fixtures, Given, Then, When } from "./fixture";

async function cleanAll({ booksApi }: Fixtures) {
  await booksApi.deleteAll();
}

After(cleanAll);

Before(cleanAll);

Given(/^that user is not logged in$/, async function ({}) {});

Given(
  /^that user is signed in using username "([^"]*)" and password "([^"]*)"$/,
  async function (
    { landingPage, signInPage, booksListPage },
    username,
    password,
  ) {
    await landingPage.open();
    await landingPage.wait();
    await landingPage.signin();

    await signInPage.wait();
    await signInPage.signIn(username, password);

    await booksListPage.wait();
  },
);

Given(
  /^the user has clicked on the Sign in button$/,
  async function ({ landingPage, signInPage }) {
    await landingPage.signin();
    await signInPage.wait();
  },
);

Given(
  /^the user has been presented with a page title "([^"]*)"$/,
  async function ({ basePage }, title) {
    await basePage.verifyTitle(title);
  },
);

Given(
  /^the user has navigated to the Landing page$/,
  async function ({ landingPage }) {
    await landingPage.open();
    await landingPage.wait();
  },
);

Then(
  /^the user should be presented with a page titled "([^"]*)"$/,
  async function ({ basePage }, title) {
    await basePage.verifyTitle(title);
  },
);

Then(
  /^the page should display the user's full name "([^"]*)"$/,
  async function ({ booksListPage }, userFullName) {
    await booksListPage.verifyUserFullName(userFullName);
  },
);

When(
  /^the user navigates to the Landing page$/,
  async function ({ landingPage }) {
    await landingPage.open();
    await landingPage.wait();
  },
);

When(
  /^signs in with username "([^"]*)" and password "([^"]*)"$/,
  async function ({ signInPage, booksListPage }, username, password) {
    await signInPage.signIn(username, password);
    await booksListPage.wait()
  },
);

When(
  /^the user clicks the Create button$/,
  async function ({ booksCreatePage, booksListPage }) {
    await booksListPage.create();
    await booksCreatePage.wait();
  },
);

When(
  /^the user enters the following information$/,
  async function ({ booksCreatePage }, table) {
    const book = table.hashes()[0];
    await booksCreatePage.enterIsbn(book["ISBN"]);
    await booksCreatePage.enterTitle(book["Title"]);
    await booksCreatePage.enterAuthorFirstName(book["Author's First Name"]);
    await booksCreatePage.enterAuthorLastName(book["Author's Last Name"]);
    await booksCreatePage.enterPublisherName(book["Publisher's Name"]);
    await booksCreatePage.enterPrice(book["Price"]);
  },
);

When(
  /^the user clicks the Save button$/,
  async function ({ booksCreatePage, booksListPage }) {
    await booksCreatePage.save();
    await booksListPage.wait();
  },
);
