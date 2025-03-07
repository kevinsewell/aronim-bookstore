// Layouts
import AnonymousLayout from "../layouts/AnonymousLayout";
import MainLayout from "../layouts/MainLayout";

// Pages
import Login from "../pages/Login";
import Home from "../pages/Home";
import CreateUser from "../pages/CreateUser";
import ListUsers from "../pages/ListUsers";
import {renderRoutes} from "./render-routes";

/**
 * @fileoverview Defines the routing configuration for the application.
 * This file imports layout components and page components, and exports a routes array
 * that defines the application's routing structure.
 */

/**
 * @typedef {Object} Route
 * @property {string} name - Unique identifier for the route
 * @property {string} title - Display title for the route
 * @property {React.Component} [component] - Component to render for this route
 * @property {string} [path] - URL path for this route
 * @property {boolean} [isPublic] - Whether the route is accessible without authentication
 * @property {boolean} [hasSiderLink] - Whether to show this route in the sidebar navigation
 * @property {Route[]} [routes] - Child routes
 */

/**
 * @typedef {Object} LayoutConfig
 * @property {React.Component} layout - Layout component to use
 * @property {Route[]} routes - Routes that use this layout
 */

/**
 * @type {LayoutConfig[]} routes - Application routing configuration
 */
export const routes = [
  {
    layout: AnonymousLayout,
    routes: [
      {
        name: "login",
        title: "Login page",
        component: Login,
        path: "/login",
        isPublic: true,
      },
    ],
  },
  {
    layout: MainLayout,
    routes: [
      {
        name: "home",
        title: "Home page",
        component: Home,
        path: "/home",
      },
      {
        name: "users",
        title: "Users",
        hasSiderLink: true,
        routes: [
          {
            name: "list-users",
            title: "List of users",
            hasSiderLink: true,
            component: ListUsers,
            path: "/users",
          },
          {
            name: "create-user",
            title: "Add user",
            hasSiderLink: true,
            component: CreateUser,
            path: "/users/new",
          },
        ],
      },
    ],
  },
];

export const Routes = renderRoutes(routes);
