# TT Platform — Fullstack Web App (Angular SSR + Spring Boot)

Repositorio fullstack de portafolio desarrollado como Trabajo Terminal (UPIIZ).  
Incluye **frontend Angular con SSR (Angular Universal)**, **backend Spring Boot (API REST)** y **PostgreSQL** con servicios de apoyo para desarrollo.

> Enfocado en: arquitectura limpia, buenas prácticas, y un entorno reproducible con Docker.

---

## Demo
- 🎥 Video : <<pendiente>>
- 🖼️ Capturas: `docs/`

---

## Tech Stack
**Frontend**
- Angular + SSR (Angular Universal)
- TypeScript

**Backend**
- Spring Boot (Java)
- API REST
- Flyway (migraciones) *(si aplica en tu backend)*

**Infra / Tooling**
- PostgreSQL (Docker)
- pgAdmin (administración de BD)
- MailHog (SMTP local para pruebas de correo)
- Docker Compose

---

## Funcionalidades clave (resumen para RH)
- Arquitectura **frontend + API REST + base de datos**
- Integración de servicios (BD + correo local para pruebas)
- Estructura modular (separación `frontend/` y `backend/`)
- Configuración reproducible con **Docker Compose** (entorno de desarrollo)

---

## Estructura del repositorio
```txt
tt-platform/
  backend/      # Spring Boot API
  frontend/     # Angular SSR
  docs/         # Documentación relacionada
  docker-compose.yml
  README.md
