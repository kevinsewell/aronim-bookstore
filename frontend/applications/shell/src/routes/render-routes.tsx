import flattenDeep from "lodash/flattenDeep";
import { ReactElement } from "react";
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
  return ({ isAuthorized }): ReactElement => {
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
