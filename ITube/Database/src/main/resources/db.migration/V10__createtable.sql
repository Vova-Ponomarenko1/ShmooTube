CREATE TABLE IF NOT EXISTS public.users
(
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    avatar_base64 TEXT,
    role VARCHAR(255) NOT NULL,
    is_banned BOOLEAN
);
