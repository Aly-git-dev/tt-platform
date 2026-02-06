# Plataforma Colaborativa para Apoyar el Aprendizaje Académico Estudiantil en la UPIIZ  
**Fullstack Web App — Angular SSR + Spring Boot + PostgreSQL**

Repositorio fullstack de portafolio desarrollado como **Trabajo Terminal** en la  
**Unidad Profesional Interdisciplinaria de Ingeniería Campus Zacatecas (UPIIZ)**.

La plataforma tiene como objetivo **apoyar el aprendizaje académico estudiantil**
mediante herramientas colaborativas, integrando un frontend moderno con SSR,
una API REST robusta y una base de datos relacional.

> Enfoque del proyecto: arquitectura limpia, separación de responsabilidades,
buenas prácticas de desarrollo y un entorno reproducible con Docker.

---

## Demo
- Video demo: *(pendiente de agregar)*
- Capturas: `docs/` *(login, foros, vista principal, etc.)*

---

## Tech Stack

### Frontend
- Angular con **SSR (Angular Universal)**
- TypeScript
- Arquitectura por módulos

### Backend
- Spring Boot (Java)
- API REST
- **Flyway** para control de versiones de la base de datos
- Arquitectura por capas

### Base de Datos e Infraestructura
- PostgreSQL
- pgAdmin (administración de BD)
- MailHog (SMTP local para pruebas de correo)
- Docker & Docker Compose

---

## Funcionalidades clave
- Autenticación de usuarios (**Auth**)
- Módulo de **Foros académicos** para interacción estudiantil
- Comunicación frontend ↔ backend mediante API REST
- Migraciones automáticas de base de datos con Flyway
- Entorno de desarrollo completo y reproducible con Docker

---

## Estructura del repositorio
```txt
tt-platform/
  backend/        # API REST Spring Boot
  frontend/       # Angular SSR (Angular Universal)
  docs/           # Documentación
  docker-compose.yml
  README.md
