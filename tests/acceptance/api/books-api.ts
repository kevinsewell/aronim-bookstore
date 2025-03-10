import axios, { AxiosInstance } from "axios";
import { BaseApi } from "@api/base-api";

export class BooksApi extends BaseApi {
  protected readonly booksUrl = `${this.apiV1Url}/books`;

  constructor(axiosInstance: AxiosInstance) {
    super(axiosInstance);
  }

  async deleteAll() {

    await axios.delete(this.booksUrl, {
      headers: { Authorization: `Bearer ${await this.getAccessToken()}` },
      httpsAgent: this.httpsAgent,
    });
  }
}
