import react from "@vitejs/plugin-react";
import { defineConfig } from "vite";

export default defineConfig({
  envPrefix: ["BOOKSTORE_", "KEYCLOAK_", "VITE_"],
  plugins: [react()],
  server: {
    allowedHosts: ["bookstore.aronim.local"],
  },
});
