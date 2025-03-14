import react from "@vitejs/plugin-react";
import { defineConfig, splitVendorChunkPlugin } from "vite";

export default defineConfig({
  envPrefix: ["BOOKSTORE_", "KEYCLOAK_", "VITE_"],
  plugins: [react(), splitVendorChunkPlugin()],
  server: {
    allowedHosts: ["bookstore.aronim.local"],
  },
});
