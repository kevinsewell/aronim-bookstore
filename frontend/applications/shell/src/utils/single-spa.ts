import { Activity, ActivityFn, pathToActiveWhen } from "single-spa";

/**
 * Sanitizes the activeWhen parameter to ensure it works consistently with single-spa routing.
 *
 * @param activeWhen - Can be a string path, a function, or an array of strings/functions
 *                     that determine when a microfrontend should be active
 * @returns A unified activity function that returns true if any of the provided
 *          activeWhen conditions are met for the current location
 */
export function sanitizeActiveWhen(activeWhen: Activity): ActivityFn {
  // Convert the input to an array if it's not already one
  const activeWhenMixedArray: (ActivityFn | string)[] = Array.isArray(
    activeWhen,
  )
    ? activeWhen
    : [activeWhen];

  // Convert any string paths to activity functions using single-spa's pathToActiveWhen
  const activeWhenArray: ActivityFn[] = activeWhenMixedArray.map(
    (activeWhenOrPath: string | ActivityFn) =>
      typeof activeWhenOrPath === "function"
        ? activeWhenOrPath
        : pathToActiveWhen(activeWhenOrPath),
  );

  // Return a function that checks if any of the activity functions return true
  // for the current location (using Array.some for "OR" logic)
  return (location) =>
    activeWhenArray.some((activeWhen: ActivityFn) => activeWhen(location));
}
