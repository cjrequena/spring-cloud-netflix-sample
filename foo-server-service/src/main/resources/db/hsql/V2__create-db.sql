-- ******************** --
-- HSQL --
-- ******************** --
DROP TABLE IF EXISTS FOO;
CREATE TABLE FOO
(
  ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1),
  NAME VARCHAR2(255 CHAR),
  DESCRIPTION VARCHAR2(255 CHAR),
  CREATION_DATE DATE DEFAULT CURRENT_DATE,
  CONSTRAINT FOO_PK PRIMARY KEY (ID),
  CONSTRAINT FOO_UNIQUE UNIQUE  (NAME)
);
