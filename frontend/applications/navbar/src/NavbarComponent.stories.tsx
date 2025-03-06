import React from "react";
import { PrimeReactProvider } from "primereact/api";
import "primereact/resources/themes/lara-light-cyan/theme.css";
import type { Meta, StoryObj } from "@storybook/react";
import { NavbarComponent } from "./NavbarComponent";

const meta = {
  title: "Components/HomeComponent",
  component: () => (
    <PrimeReactProvider>
      <NavbarComponent />
    </PrimeReactProvider>
  ),
  parameters: {
    layout: "centered",
  },
  tags: ["autodocs"],
} satisfies Meta<typeof NavbarComponent>;

export default meta;
type Story = StoryObj<typeof meta>;

// Define your stories
export const Default: Story = {
  args: {
    // Add props here based on your component's props
  },
};

// Add more stories as needed for different states/variants
