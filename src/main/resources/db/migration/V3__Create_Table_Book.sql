CREATE TABLE public.books (
  id SERIAL PRIMARY KEY NOT NULL,
  author character varying(80) NOT NULL,
  launch_date DATE NOT NULL,
  price decimal(65,2) NOT NULL,
  title character varying(80)
);
