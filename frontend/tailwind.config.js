export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        "fg-bg": "#080A0F",
        "fg-bg2": "#0E1118",
        "fg-bg3": "#151920",
        "fg-accent": "#FF4D00",
        "fg-accent2": "#FFB800",
        "fg-text": "#F0EEE8",
        "fg-muted": "#6B7280",
      },
      fontFamily: {
        display: ["Bebas Neue", "sans-serif"],
        body: ["DM Sans", "sans-serif"],
        mono: ["DM Mono", "monospace"],
      },
    },
  },
};
