import React from "react";

import { MegaMenu } from "primereact/megamenu";
import type { MenuItem } from "primereact/menuitem";

export const NavbarComponent = () => {
  const items: MenuItem[] = [
    {
      id: "home",
      label: "Home",
      url: "/home",
    },
    {
      id: "books",
      label: "Books",
      url: "/books",
    },
  ];

  return <MegaMenu model={items} />;
};
