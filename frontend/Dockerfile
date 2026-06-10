# ─── Stage 1: Build Angular ───────────────────────────────────────────────────
FROM node:22-alpine AS build
WORKDIR /app

# Copiar archivos de dependencias primero (cache de capas)
COPY package*.json ./
RUN npm ci

# Copiar el resto del código fuente y compilar en modo producción
COPY . .
RUN npm run build

# ─── Stage 2: Servir con Nginx ────────────────────────────────────────────────
FROM nginx:alpine

# Copiar el build de Angular (Angular 20 con @angular/build genera en dist/<proyecto>/browser)
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html

# Copiar configuración de Nginx con soporte para HTML5 routing
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
