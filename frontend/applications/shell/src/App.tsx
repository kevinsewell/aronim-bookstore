import { App as AntdApp } from "antd";
import axiosInstance from "axios";
import { BrowserRouter, Outlet, Route, Routes } from "react-router";
import { Authenticated, Refine } from "@refinedev/core";
import { DevtoolsPanel, DevtoolsProvider } from "@refinedev/devtools";
import { RefineKbar, RefineKbarProvider } from "@refinedev/kbar";

import {
  ErrorComponent,
  ThemedLayoutV2,
  ThemedSiderV2,
  useNotificationProvider,
} from "@refinedev/antd";
import "@refinedev/antd/dist/reset.css";

import routerBindings, {
  CatchAllNavigate,
  DocumentTitleHandler,
  NavigateToResource,
  UnsavedChangesNotifier,
} from "@refinedev/react-router";
import dataProvider from "@refinedev/simple-rest";

import { AppIcon } from "./components/app-icon";
import { Header } from "./components";
import { ColorModeContextProvider } from "./contexts/color-mode";
import { useAuthentication } from "./hooks/useAuthentication";
import {
  BookCreate,
  BookEdit,
  BookList,
  BookShow,
} from "./pages/books";
import { Login } from "./pages/login";

function App() {
  const { authProvider, initialized } = useAuthentication(axiosInstance);

  if (!initialized) {
    return <div>Loading...</div>;
  }

  return (
    <BrowserRouter>
      <RefineKbarProvider>
        <ColorModeContextProvider>
          <AntdApp>
            <DevtoolsProvider>
              <Refine
                dataProvider={dataProvider(
                  "https://api.bookstore.aronim.local/api/v1",
                  axiosInstance,
                )}
                notificationProvider={useNotificationProvider}
                authProvider={authProvider}
                routerProvider={routerBindings}
                resources={[
                  {
                    name: "books",
                    list: "/books",
                    create: "/books/create",
                    edit: "/books/edit/:id",
                    show: "/books/show/:id",
                    meta: {
                      canDelete: true,
                    },
                  },
                ]}
                options={{
                  syncWithLocation: true,
                  warnWhenUnsavedChanges: true,
                  useNewQueryKeys: true,
                  projectId: "pEg9pO-r38cFo-T3iwQG",
                  title: { text: "Aronim Bookstore", icon: <AppIcon /> },
                }}
              >
                <DocumentTitleHandler />
                <Routes>
                  <Route
                    element={
                      <Authenticated
                        key="authenticated-inner"
                        fallback={<CatchAllNavigate to="/login" />}
                      >
                        <ThemedLayoutV2
                          Header={Header}
                          Sider={(props) => <ThemedSiderV2 {...props} fixed />}
                        >
                          <Outlet />
                        </ThemedLayoutV2>
                      </Authenticated>
                    }
                  >
                    <Route
                      index
                      element={<NavigateToResource resource="books" />}
                    />
                    <Route path="/books">
                      <Route index element={<BookList />} />
                      <Route path="create" element={<BookCreate />} />
                      <Route path="edit/:id" element={<BookEdit />} />
                      <Route path="show/:id" element={<BookShow />} />
                    </Route>
                    <Route path="*" element={<ErrorComponent />} />
                  </Route>
                  <Route
                    element={
                      <Authenticated
                        key="authenticated-outer"
                        fallback={<Outlet />}
                      >
                        <NavigateToResource />
                      </Authenticated>
                    }
                  >
                    <Route path="/login" element={<Login />} />
                  </Route>
                </Routes>

                <RefineKbar />
                <UnsavedChangesNotifier />
              </Refine>
              <DevtoolsPanel />
            </DevtoolsProvider>
          </AntdApp>
        </ColorModeContextProvider>
      </RefineKbarProvider>
    </BrowserRouter>
  );
}

export default App;
