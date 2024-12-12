/** @type {import('tailwindcss').Config} */

export const content = ["./src/**/*.{js,jsx,ts,tsx}"];
export const theme = {
  extend: {
    // Personalización opcional del tema
    spacing: {
      '96': '24rem', // Ejemplo: Agregar tamaño adicional para espaciados
    },
    colors: {
      modalBackground: "rgba(0, 0, 0, 0.6)", // Fondo semitransparente personalizado
    },
    borderRadius: {
      'lg': '1rem', // Redondear esquinas con mayor tamaño
    },
    keyframes: {
      fadeIn: {
        '0%': { opacity: '0' },
        '100%': { opacity: '1' },
      },
      fadeOut: {
        '0%': { opacity: '1' },
        '100%': { opacity: '0' },
      },
    },
    animation: {
      fadeIn: 'fadeIn 0.3s ease-in-out', // Animación de entrada suave
      fadeOut: 'fadeOut 0.3s ease-in-out', // Animación de salida suave
    },
  }
};
export const plugins = [];


