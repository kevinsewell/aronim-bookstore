import type { ReactNode } from "react";
import type {
  AppBreadcrumbProps,
  AppConfigProps,
  AppMenuItem,
  AppMenuItemProps,
  AppTopbarRef,
  Breadcrumb,
  BreadcrumbItem,
  LayoutConfig,
  LayoutContextProps,
  LayoutState,
  MenuContextProps,
  MenuModel,
  MenuProps,
  NodeRef,
} from "./layout";

type ChildContainerProps = {
  children: ReactNode;
};

export type {
  AppBreadcrumbProps,
  Breadcrumb,
  BreadcrumbItem,
  MenuProps,
  MenuModel,
  LayoutConfig,
  LayoutState,
  LayoutContextProps,
  MenuContextProps,
  AppConfigProps,
  NodeRef,
  AppTopbarRef,
  AppMenuItemProps,
  ChildContainerProps,
  AppMenuItem,
};
