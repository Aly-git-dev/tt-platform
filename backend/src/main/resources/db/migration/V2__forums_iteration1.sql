-- ============================================================================
-- V2__forums_iteration1.sql
-- Módulo de Foros - Iteración 1
-- Requiere: V1__init_final.sql (roles, users, user_roles, email_verifications)
-- ============================================================================

-- 1) Tabla: forum_category
--    Categorías principales: carreras, eventos, clubes, evaluación docente
-- ============================================================================

CREATE TABLE forum_category (
                                id          BIGSERIAL PRIMARY KEY,
                                code        VARCHAR(20)  NOT NULL UNIQUE,   -- IA, SISC, MECA, ALIM, AMBI, META, EVENTOS, CLUBES, EVALDOC...
                                name        VARCHAR(100) NOT NULL,
                                description TEXT,
                                active      BOOLEAN      NOT NULL DEFAULT TRUE,

                                created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
                                updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

-- Trigger updated_at
CREATE TRIGGER set_timestamp_forum_category
    BEFORE UPDATE ON forum_category
    FOR EACH ROW EXECUTE FUNCTION trg_set_timestamp();

-- Seeds de categorías principales (puedes ajustar nombres/códigos si gustas)
INSERT INTO forum_category (code, name, description)
VALUES
    ('IA',      'Inteligencia Artificial', 'Foros para la carrera de IA'),
    ('SISC',    'Sistemas Computacionales', 'Foros para la carrera de ISC'),
    ('MECA',    'Mecatrónica', 'Foros para la carrera de Mecatrónica'),
    ('ALIM',    'Alimentos', 'Foros para la carrera de Alimentos'),
    ('AMBI',    'Ambiental', 'Foros para la carrera de Ambiental'),
    ('META',    'Metalúrgica', 'Foros para la carrera de Metalúrgica'),
    ('EVENTOS', 'Eventos', 'Eventos, actividades académicas y culturales'),
    ('CLUBES',  'Clubes', 'Clubes estudiantiles y comunidades'),
    ('EVALDOC', 'Evaluación Docente', 'Valoraciones y comentarios sobre experiencia docente')
    ON CONFLICT (code) DO NOTHING;

-- ============================================================================
-- 2) Tabla: forum_subarea
--    Áreas de conocimiento / materias dentro de una categoría
-- ============================================================================

CREATE TABLE forum_subarea (
                               id          BIGSERIAL PRIMARY KEY,
                               category_id BIGINT      NOT NULL REFERENCES forum_category(id)
                                   ON DELETE RESTRICT ON UPDATE CASCADE,
                               name        VARCHAR(150) NOT NULL,      -- p.ej. "Programación", "Cálculo Diferencial"
                               type        VARCHAR(30)  NOT NULL,      -- AREA_CONOCIMIENTO / MATERIA (string simple)
                               active      BOOLEAN      NOT NULL DEFAULT TRUE,

                               created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
                               updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_forum_subarea_category ON forum_subarea(category_id);
CREATE INDEX idx_forum_subarea_type     ON forum_subarea(type);

CREATE TRIGGER set_timestamp_forum_subarea
    BEFORE UPDATE ON forum_subarea
    FOR EACH ROW EXECUTE FUNCTION trg_set_timestamp();

-- ============================================================================
-- 3) Tabla: forum_thread
--    Hilos de foro (preguntas, discusiones, anuncios)
-- ============================================================================

CREATE TABLE forum_thread (
                              id            BIGSERIAL PRIMARY KEY,
                              author_id     UUID        NOT NULL REFERENCES users(id)
                                  ON DELETE RESTRICT ON UPDATE CASCADE,
                              category_id   BIGINT      NOT NULL REFERENCES forum_category(id)
                                  ON DELETE RESTRICT ON UPDATE CASCADE,
                              subarea_id    BIGINT               REFERENCES forum_subarea(id)
                                  ON DELETE SET NULL ON UPDATE CASCADE,

                              title         VARCHAR(200) NOT NULL,
                              body          TEXT         NOT NULL,
                              type          VARCHAR(20)  NOT NULL,     -- PREGUNTA / DISCUSSION / ANUNCIO
                              score         INTEGER      NOT NULL DEFAULT 0,
                              answers_count INTEGER      NOT NULL DEFAULT 0,
                              views         INTEGER      NOT NULL DEFAULT 0,
                              status        VARCHAR(20)  NOT NULL DEFAULT 'ABIERTO',

                              created_at    TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
                              updated_at    TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_thread_category ON forum_thread(category_id);
CREATE INDEX idx_thread_subarea  ON forum_thread(subarea_id);
CREATE INDEX idx_thread_author   ON forum_thread(author_id);
CREATE INDEX idx_thread_status   ON forum_thread(status);
CREATE INDEX idx_thread_score    ON forum_thread(score);
CREATE INDEX idx_thread_created  ON forum_thread(created_at);

CREATE TRIGGER set_timestamp_forum_thread
    BEFORE UPDATE ON forum_thread
    FOR EACH ROW EXECUTE FUNCTION trg_set_timestamp();

-- ============================================================================
-- 4) Tabla: forum_post
--    Respuestas y comentarios dentro de un hilo
-- ============================================================================

CREATE TABLE forum_post (
                            id                 BIGSERIAL PRIMARY KEY,
                            thread_id          BIGINT     NOT NULL REFERENCES forum_thread(id)
                                ON DELETE CASCADE ON UPDATE CASCADE,
                            author_id          UUID       NOT NULL REFERENCES users(id)
                                ON DELETE RESTRICT ON UPDATE CASCADE,
                            parent_post_id     BIGINT              REFERENCES forum_post(id)
                                ON DELETE SET NULL ON UPDATE CASCADE,

                            body               TEXT       NOT NULL,
                            score              INTEGER    NOT NULL DEFAULT 0,
                            is_accepted_answer BOOLEAN    NOT NULL DEFAULT FALSE,
                            status             VARCHAR(20) NOT NULL DEFAULT 'VISIBLE',

                            created_at         TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
                            updated_at         TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_post_thread  ON forum_post(thread_id);
CREATE INDEX idx_post_author  ON forum_post(author_id);
CREATE INDEX idx_post_parent  ON forum_post(parent_post_id);
CREATE INDEX idx_post_status  ON forum_post(status);
CREATE INDEX idx_post_created ON forum_post(created_at);

CREATE TRIGGER set_timestamp_forum_post
    BEFORE UPDATE ON forum_post
    FOR EACH ROW EXECUTE FUNCTION trg_set_timestamp();

-- ============================================================================
-- 5) Tabla: forum_attachment
--    Adjuntos (imágenes, videos, audios, links) asociados a un post
-- ============================================================================

CREATE TABLE forum_attachment (
                                  id        BIGSERIAL PRIMARY KEY,
                                  post_id   BIGINT     NOT NULL REFERENCES forum_post(id)
                                      ON DELETE CASCADE ON UPDATE CASCADE,
                                  kind      VARCHAR(20) NOT NULL,   -- IMAGEN / VIDEO / AUDIO / LINK
                                  url       TEXT        NOT NULL,
                                  metadata  TEXT,

                                  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_attachment_post ON forum_attachment(post_id);
CREATE INDEX idx_attachment_kind ON forum_attachment(kind);

-- ============================================================================
-- 6) Tabla: forum_report
--    Reportes de contenido (hilo o post) hechos por usuarios
-- ============================================================================

CREATE TABLE forum_report (
                              id           BIGSERIAL PRIMARY KEY,
                              reporter_id  UUID       NOT NULL REFERENCES users(id)
                                  ON DELETE RESTRICT ON UPDATE CASCADE,
                              thread_id    BIGINT              REFERENCES forum_thread(id)
                                  ON DELETE CASCADE ON UPDATE CASCADE,
                              post_id      BIGINT              REFERENCES forum_post(id)
                                  ON DELETE CASCADE ON UPDATE CASCADE,
                              reason_code  VARCHAR(30) NOT NULL,   -- SPAM / OFENSIVO / etc.
                              description  TEXT,
                              status       VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
                              handled_by   UUID                 REFERENCES users(id)
                                  ON DELETE SET NULL ON UPDATE CASCADE,
                              handled_at   TIMESTAMP WITHOUT TIME ZONE,

                              created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_report_status   ON forum_report(status);
CREATE INDEX idx_report_thread   ON forum_report(thread_id);
CREATE INDEX idx_report_post     ON forum_report(post_id);
CREATE INDEX idx_report_reporter ON forum_report(reporter_id);

-- (no hace tanta falta updated_at: usamos handled_at para cierre)

-- ============================================================================
-- 7) Tabla: user_interest_tag
--    Intereses del usuario por categoría / subárea (para recomendaciones)
-- ============================================================================

CREATE TABLE user_interest_tag (
                                   id          BIGSERIAL PRIMARY KEY,
                                   user_id     UUID       NOT NULL REFERENCES users(id)
                                       ON DELETE CASCADE ON UPDATE CASCADE,
                                   category_id BIGINT     NOT NULL REFERENCES forum_category(id)
                                       ON DELETE CASCADE ON UPDATE CASCADE,
                                   subarea_id  BIGINT              REFERENCES forum_subarea(id)
                                       ON DELETE SET NULL ON UPDATE CASCADE,
                                   weight      INTEGER    NOT NULL DEFAULT 1,  -- prioridad 1..5 aprox

                                   created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

ALTER TABLE user_interest_tag
    ADD CONSTRAINT uk_interest_user_cat_sub
        UNIQUE (user_id, category_id, subarea_id);

CREATE INDEX idx_interest_user ON user_interest_tag(user_id);

-- ============================================================================
-- 8) Tabla: thread_vote
--    Votos de usuario sobre hilos (-1 / 0 / +1)
-- ============================================================================

CREATE TABLE thread_vote (
                             id        BIGSERIAL PRIMARY KEY,
                             user_id   UUID       NOT NULL REFERENCES users(id)
                                 ON DELETE CASCADE ON UPDATE CASCADE,
                             thread_id BIGINT     NOT NULL REFERENCES forum_thread(id)
                                 ON DELETE CASCADE ON UPDATE CASCADE,
                             value     SMALLINT   NOT NULL,   -- -1, 0, +1

                             created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

ALTER TABLE thread_vote
    ADD CONSTRAINT uk_thread_vote_user_thread
        UNIQUE (user_id, thread_id);

CREATE INDEX idx_thread_vote_thread ON thread_vote(thread_id);

-- ============================================================================
-- 9) Tabla: post_vote
--    Votos de usuario sobre respuestas/posts (-1 / 0 / +1)
-- ============================================================================

CREATE TABLE post_vote (
                           id       BIGSERIAL PRIMARY KEY,
                           user_id  UUID       NOT NULL REFERENCES users(id)
                               ON DELETE CASCADE ON UPDATE CASCADE,
                           post_id  BIGINT     NOT NULL REFERENCES forum_post(id)
                               ON DELETE CASCADE ON UPDATE CASCADE,
                           value    SMALLINT   NOT NULL,    -- -1, 0, +1

                           created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

ALTER TABLE post_vote
    ADD CONSTRAINT uk_post_vote_user_post
        UNIQUE (user_id, post_id);

CREATE INDEX idx_post_vote_post ON post_vote(post_id);

-- ============================================================================
-- FIN V2 – Módulo de Foros (Iteración 1)
-- ============================================================================
