import { createRoot } from "react-dom/client";

import { ReactKeycloakProvider } from "@react-keycloak/web";
import Keycloak from "keycloak-js";

import App from "./App";

const keycloak = new Keycloak({
  clientId: import.meta.env.KEYCLOAK_BOOKSTORE_FRONTEND_CLIENT_ID,
  url: import.meta.env.KEYCLOAK_URL,
  realm: "bookstore",
});

const container = document.getElementById("root") as HTMLElement;
const root = createRoot(container);

root.render(
  <ReactKeycloakProvider authClient={keycloak}>
    <App />
  </ReactKeycloakProvider>,
);
