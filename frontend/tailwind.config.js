/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Inter', 'system-ui', '-apple-system', 'sans-serif'],
      },
      colors: {
        border: "hsl(var(--border, 263 20% 88%))",
        input: "hsl(var(--input, 263 20% 88%))",
        ring: "hsl(var(--ring, 263 70% 58%))",
        background: "hsl(var(--background, 270 100% 99%))",
        foreground: "hsl(var(--foreground, 274 96% 14%))",
        surface: "hsl(var(--surface, 270 100% 99%))",
        muted: {
          DEFAULT: "hsl(var(--muted, 270 50% 96%))",
          foreground: "hsl(var(--muted-foreground, 220 9% 46%))",
        },
        card: {
          DEFAULT: "hsl(var(--card, 0 0% 100%))",
          foreground: "hsl(var(--card-foreground, 274 96% 14%))",
        },
        destructive: {
          DEFAULT: "hsl(var(--destructive, 343 81% 53%))",
          foreground: "hsl(var(--destructive-foreground, 270 100% 99%))",
        },
        secondary: {
          DEFAULT: "hsl(var(--secondary, 270 50% 96%))",
          foreground: "hsl(var(--secondary-foreground, 274 96% 14%))",
        },
        primary: {
          50: '#faf5ff',
          100: '#f3e8ff',
          200: '#e9d5ff',
          300: '#d8b4fe',
          400: '#c084fc',
          500: '#a855f7',
          600: '#9333ea',
          700: '#7c3aed',
          800: '#6b21a8',
          900: '#581c87',
          950: '#3b0764',
        },
        accent: {
          50: '#fff1f2',
          100: '#ffe4e6',
          200: '#fecdd3',
          300: '#fda4af',
          400: '#fb7185',
          500: '#f43f5e',
          600: '#e11d48',
          700: '#be123c',
          800: '#9f1239',
          900: '#881337',
          950: '#4c0519',
        },
      },
    },
  },
  plugins: [],
};
