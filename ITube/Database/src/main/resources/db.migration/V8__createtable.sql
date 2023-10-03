CREATE TABLE IF NOT EXISTS public.videos
(
    id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    video_data BYTEA,
    thumbnail BYTEA,
    user_id INTEGER,
    CONSTRAINT fk_user_id
        FOREIGN KEY (user_id)
            REFERENCES public.users(id)
            ON UPDATE NO ACTION
            ON DELETE NO ACTION
);
