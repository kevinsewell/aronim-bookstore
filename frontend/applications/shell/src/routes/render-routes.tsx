import flattenDeep from "lodash/flattenDeep";
import React from "react";
import {Route, Routes as ReactRoutes} from "react-router-dom";
import ProtectedRoute from "./ProtectedRoute";

/**
 * Flattens a nested routes structure into a single-level array
 *
 * @param {Array} routes - The nested routes configuration
 * @returns {Array} A flattened array of route objects
 */
const generateFlattenRoutes = (routes) => {
  if (!routes) return [];
  return flattenDeep(
    routes.map(({ routes: subRoutes, ...rest }) => [
      rest,
      generateFlattenRoutes(subRoutes),
    ]),
  );
};

/**
 * Creates a routing component based on the provided route configuration
 *
 * @param {Array} mainRoutes - The main routes configuration containing layouts and nested routes
 * @returns {Function} A component function that accepts authorization props and renders the routes
 */
export const renderRoutes = (mainRoutes) => {
  /**
   * Component function that renders the route hierarchy
   *
   * @param {Object} props - Component properties
   * @param {boolean} props.isAuthorized - Whether the current user is authorized
   * @returns {React.Element} The rendered routes component
   */
  return ({ isAuthorized }) => {
    const layouts = mainRoutes.map(({ layout: Layout, routes }, index) => {
      const subRoutes = generateFlattenRoutes(routes);
      const isPublic = routes[0].isPublic ?? false;

      return (
        <Route key={index} element={<Layout />}>
          <Route
            element={
              <ProtectedRoute isPublic={isPublic} isAuthorized={isAuthorized} />
            }
          >
            {subRoutes.map(({ component: Component, path, name }) => {
              return (
                Component &&
                path && <Route key={name} element={<Component />} path={path} />
              );
            })}
          </Route>
        </Route>
      );
    });

    return <ReactRoutes>{layouts}</ReactRoutes>;
  };
};
