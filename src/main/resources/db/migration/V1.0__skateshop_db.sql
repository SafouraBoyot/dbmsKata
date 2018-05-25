-- Table: public.category

-- DROP TABLE public.category;

CREATE TABLE public.category
(
  type CHARACTER VARYING(50) NOT NULL,
  CONSTRAINT pk_type PRIMARY KEY (type)
)
WITH (
OIDS = FALSE
);
ALTER TABLE public.category
  OWNER TO postgres;


-- Table: public.skate
-- DROP TABLE public.skate;

CREATE SEQUENCE public.skates_id_seq;

CREATE TABLE public.skate
(
  id       INTEGER NOT NULL DEFAULT nextval('skates_id_seq' :: REGCLASS),
  name     CHARACTER VARYING(100),
  category CHARACTER VARYING(50),
  stock    INTEGER,
  CONSTRAINT pk_skate PRIMARY KEY (id),
  CONSTRAINT fk_category FOREIGN KEY (category)
  REFERENCES public.category (type) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uq_skate UNIQUE (name)
)
WITH (
OIDS = FALSE
);
ALTER TABLE public.skate
  OWNER TO postgres;
