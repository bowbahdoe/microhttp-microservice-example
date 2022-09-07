/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,js}"],
  theme: {
    extend: {},
  },
  plugins: [],
} // npx tailwindcss -i ./src/main/css/input.css -o ./src/main/resources/public/output.css --watch