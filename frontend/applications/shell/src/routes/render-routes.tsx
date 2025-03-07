import flattenDeep from "lodash/flattenDeep";
import * as React from "react";
import { Route, Routes as ReactRoutes } from "react-router-dom";
import ProtectedRoute from "./ProtectedRoute";

const generateFlattenRoutes = (routes) => {
  if (!routes) return [];
  return flattenDeep(
    routes.map(({ routes: subRoutes, ...rest }) => [
      rest,
      generateFlattenRoutes(subRoutes),
    ]),
  );
};

export const renderRoutes = (mainRoutes) => {
  return ({ isAuthorized }) => {
    const layouts = mainRoutes.map(({ layout: Layout, routes }, index) => {
      const subRoutes = generateFlattenRoutes(routes);

      const isPublic = subRoutes.isPublic
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
