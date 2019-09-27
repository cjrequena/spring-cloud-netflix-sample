-- Adminer 4.6.3 PostgreSQL dump

CREATE SEQUENCE IF NOT EXISTS foo_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1;

CREATE TABLE IF NOT EXISTS foo (
    "id" integer DEFAULT nextval('foo_id_seq') NOT NULL,
    "name" text NOT NULL,
    "description" text,
    "creation_date" date DEFAULT CURRENT_DATE,
    CONSTRAINT "foo_id_pk" PRIMARY KEY ("id"),
    CONSTRAINT "foo_name_uk" UNIQUE ("name")
) WITH (oids = false);


-- 2018-10-28 09:39:07.518463+00