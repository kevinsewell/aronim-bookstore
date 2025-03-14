import axios, { AxiosInstance } from "axios";
import { Logger } from "winston";
import { logger } from "@utils/logger";
import https from "https";
import process from "node:process";
import querystring from "node:querystring";

function assertDefined<T extends any>(something: T | undefined): T {
  if (something == undefined) {
    throw new Error("Should be defined!");
  }
  return something;
}

export class BaseApi {
  protected readonly httpsAgent = new https.Agent({
    rejectUnauthorized: false,
  });

  protected readonly apiV1Url: string = `${assertDefined(process.env.BOOKSTORE_API_URL)}/v1`;
  protected readonly adminUsername: string = assertDefined(
    process.env.KEYCLOAK_BOOKSTORE_ADMIN_USERNAME,
  );
  protected readonly adminPassword: string = assertDefined(
    process.env.KEYCLOAK_BOOKSTORE_ADMIN_PASSWORD,
  );
  protected readonly clientId: string = assertDefined(
    process.env.KEYCLOAK_BOOKSTORE_BACKEND_CLIENT_ID,
  );
  protected readonly clientSecret: string = assertDefined(
    process.env.KEYCLOAK_BOOKSTORE_BACKEND_CLIENT_SECRET,
  );
  protected readonly logger: Logger = logger;
  protected readonly tokenUrl: string = assertDefined(
    `${process.env.KEYCLOAK_BOOKSTORE_REALM_URL}/protocol/openid-connect/token`,
  );

  constructor(protected readonly axiosInstance: AxiosInstance) {}

  async getAccessToken() {
    const loginRequest = querystring.stringify({
      grant_type: "password",
      client_id: this.clientId,
      client_secret: this.clientSecret,
      username: this.adminUsername,
      password: this.adminPassword,
    });

    const tokenResponse = await axios.post(this.tokenUrl, loginRequest, {
      httpsAgent: this.httpsAgent,
    });
    const tokenData = tokenResponse.data;

    return tokenData.access_token;
  }
}
