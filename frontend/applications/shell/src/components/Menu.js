import { MegaMenu } from "primereact/megamenu";

const Menu = () => {
  const menuItems = [
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

  return (
    <nav>
      <MegaMenu model={menuItems} />
    </nav>
  );
};

export default Menu;
