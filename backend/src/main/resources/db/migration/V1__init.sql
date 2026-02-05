-- V1__init_final.sql

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE OR REPLACE FUNCTION trg_set_timestamp() RETURNS trigger AS $$
BEGIN
  NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Roles (ya con BIGINT)
CREATE TABLE roles (
                       id   BIGSERIAL PRIMARY KEY,
                       name VARCHAR(30) UNIQUE NOT NULL
);

CREATE TABLE users (
                       id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       email_inst     VARCHAR(120),
                       password_hash  VARCHAR(120),          -- NULL/placeholder en GESCO
                       full_name      VARCHAR(120) NOT NULL,
                       carrera        VARCHAR(120),
                       boleta         VARCHAR(120),
                       active         BOOLEAN NOT NULL DEFAULT TRUE,

                       bio            TEXT,
                       interests      TEXT,
                       links          TEXT,
                       avatar_url     TEXT,
                       cover_url      TEXT,

                       auth_provider  VARCHAR(20) NOT NULL DEFAULT 'LOCAL',
                       external_id    VARCHAR(120),

                       email_verified BOOLEAN NOT NULL DEFAULT FALSE,
                       approved       BOOLEAN NOT NULL DEFAULT FALSE,

                       created_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
                       updated_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

-- Índices útiles
CREATE INDEX idx_users_active        ON users(active);
CREATE INDEX idx_users_external_id   ON users(external_id);
CREATE UNIQUE INDEX ux_users_email_lower ON users (lower(email_inst));

-- Trigger de updated_at
DROP TRIGGER IF EXISTS set_timestamp ON users;
CREATE TRIGGER set_timestamp
    BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION trg_set_timestamp();

CREATE TABLE user_roles (
                            user_id UUID   NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                            role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
                            PRIMARY KEY (user_id, role_id)
);

CREATE TABLE email_verifications (
                                     token      UUID PRIMARY KEY,
                                     user_id    UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                     expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                     used       BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_email_verif_user ON email_verifications(user_id);

-- Seeds de roles
INSERT INTO roles (name) VALUES ('ADMIN'), ('PROFESOR'), ('ALUMNO'), ('ASESOR')
    ON CONFLICT (name) DO NOTHING;

-- Seed de usuario admin + rol ADMIN
-- Admin inicial totalmente validado
WITH admin_user AS (
INSERT INTO users (
    email_inst,
    password_hash,
    full_name,
    active,
    approved,
    email_verified,
    auth_provider
)
VALUES (
    'user@alumno.ipn.mx',
    crypt('Hola123..', gen_salt('bf')),  -- bcrypt con pgcrypto
    'Administrador General',
    TRUE,   -- active
    TRUE,   -- approved (ya aprobado por admin)
    TRUE,   -- email_verified (correo confirmado)
    'LOCAL' -- proveedor local
    )
    RETURNING id
    )
INSERT INTO user_roles (user_id, role_id)
SELECT au.id, r.id
FROM admin_user au
         JOIN roles r ON r.name = 'ADMIN';
