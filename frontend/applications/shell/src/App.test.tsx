import jest from "@testing-library/jest-dom";
import { render, screen } from "@testing-library/react";
import App from "./App";

// Mock react-router-dom
jest.mock("react-router-dom", () => ({
  Routes: ({ children }) => <div data-testid="routes-mock">{children}</div>,
  Route: ({ children }) => <div data-testid="route-mock">{children}</div>,
  Outlet: () => <div data-testid="outlet-mock"></div>,
}));

test("renders learn react link", () => {
  render(<App />);
  const linkElement = screen.getByText(/learn react/i);
  expect(linkElement).toBeInTheDocument();
});
