import React from "react";
import { PrimeReactProvider } from "primereact/api";
import "primereact/resources/themes/lara-light-cyan/theme.css";
import type { Meta, StoryObj } from "@storybook/react";
import { BooksComponent } from "./BooksComponent";

const meta = {
  title: "Components/BooksComponent",
  component: () => (
    <PrimeReactProvider>
      <BooksComponent />
    </PrimeReactProvider>
  ),
  parameters: {
    layout: "centered",
  },
  tags: ["autodocs"],
} satisfies Meta<typeof BooksComponent>;

export default meta;
type Story = StoryObj<typeof meta>;

// Define your stories
export const Default: Story = {
  args: {
    // Add props here based on your component's props
  },
};

// Add more stories as needed for different states/variants
