CREATE TABLE public.person (
    id SERIAL PRIMARY KEY NOT NULL,
    first_name character varying(80) NOT NULL,
    last_name character varying(80) NOT NULL,
    gender character varying(6) NOT NULL,
    address character varying(100) NOT NULL
);

ALTER TABLE public.person OWNER TO postgres;


