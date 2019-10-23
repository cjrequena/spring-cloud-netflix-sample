-- Adminer 4.6.3 PostgreSQL dump

CREATE SEQUENCE IF NOT EXISTS foo_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1;
CREATE SEQUENCE IF NOT EXISTS boo_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1;

CREATE TABLE IF NOT EXISTS foo (
    "id" integer DEFAULT nextval('foo_id_seq') NOT NULL,
    "name" text NOT NULL,
    "description" text,
    "creation_date" date DEFAULT CURRENT_DATE,
    PRIMARY KEY ("id")
) WITH (oids = false);


CREATE TABLE IF NOT EXISTS boo (
    "id" integer DEFAULT nextval('boo_id_seq') NOT NULL,
    "foo_id" integer,
    "name" text NOT NULL,
    "description" text,
    "creation_date" date DEFAULT CURRENT_DATE,
    PRIMARY KEY ("id"),
    CONSTRAINT fk_foo_id FOREIGN KEY (foo_id) REFERENCES foo(id),
    UNIQUE ("id", "foo_id")
) WITH (oids = false);

-- 2018-10-28 09:39:07.518463+00
