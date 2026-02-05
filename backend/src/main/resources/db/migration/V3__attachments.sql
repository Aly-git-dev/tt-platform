ALTER TABLE forum_attachment
    ADD COLUMN thread_id BIGINT;

ALTER TABLE forum_attachment
    ADD CONSTRAINT fk_attachment_thread
        FOREIGN KEY (thread_id)
            REFERENCES forum_thread (id)
            ON DELETE CASCADE;

ALTER TABLE forum_attachment
    ALTER COLUMN post_id DROP NOT NULL;
