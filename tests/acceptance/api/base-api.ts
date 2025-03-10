import axios, { AxiosInstance } from "axios";
import { Logger } from "winston";
import { logger } from "@utils/logger";
import https from "https";
import process from "node:process";
import querystring from "node:querystring";

export class BaseApi {
  protected readonly httpsAgent = new https.Agent({
    rejectUnauthorized: false,
  });
  protected readonly logger: Logger = logger;
  protected readonly tokenUrl = `${process.env.KC_BOOKSTORE_REALM_URL}/protocol/openid-connect/token`;
  protected readonly apiV1Url = `${process.env.KC_BOOKSTORE_BACKEND_API_URL}/v1`;

  constructor(protected readonly axiosInstance: AxiosInstance) {}

  async getAccessToken() {
    const loginRequest = querystring.stringify({
      grant_type: "password",
      client_id: process.env.KC_BOOKSTORE_BACKEND_CLIENT_ID,
      client_secret: process.env.KC_BOOKSTORE_BACKEND_CLIENT_SECRET,
      username: process.env.KC_BOOKSTORE_BACKEND_ADMIN_USERNAME,
      password: process.env.KC_BOOKSTORE_BACKEND_ADMIN_PASSWORD,
    });

    const tokenResponse = await axios.post(this.tokenUrl, loginRequest, {
      httpsAgent: this.httpsAgent,
    });
    const tokenData = tokenResponse.data;

    return tokenData.access_token;
  }
}
