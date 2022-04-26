CREATE DATABASE IF NOT EXISTS you_have_uno;
USE you_have_uno;

CREATE TABLE
IF NOT EXISTS
account
(
    uuid               CHAR(36)        PRIMARY KEY NOT NULL,
    username           VARCHAR(16)     UNIQUE      NOT NULL,
    password_hash      CHAR(32)                    NOT NULL,
    password_salt      CHAR(32)                    NOT NULL
);

CREATE TABLE
IF NOT EXISTS
login
(
	ip_address         VARBINARY(16)               NOT NULL,
	uuid               CHAR(36)                    NOT NULL,
	access_token_hash  CHAR(32)                    NOT NULL,
	access_token_salt  CHAR(32)                    NOT NULL,
	expires_on         DATE                        NOT NULL
);

CREATE TABLE
IF NOT EXISTS
visit
(
	ip_address         VARBINARY(16)               NOT NULL,
	uuid               CHAR(36)        UNIQUE      NOT NULL,
	session_token      CHAR(36)                    NOT NULL,
	keep_alive         BIGINT UNSIGNED             NOT NULL
);

CREATE TABLE
IF NOT EXISTS
game
(
    code                CHAR(6)    PRIMARY KEY     NOT NULL,
    host_uuid           CHAR(36)                   NOT NULL,
    lobby_id            CHAR(36)                   NOT NULL,
    tavern_ip           VARBINARY(16)              NOT NULL,
    tavern_port         INT UNSIGNED               NOT NULL,
    public_key          BLOB(256)                  NOT NULL
);

CREATE TABLE
IF NOT EXISTS
tavern
(
	ip_address         VARBINARY(16)   PRIMARY KEY NOT NULL,
	port               INT UNSIGNED                NOT NULL,
    keep_alive         BIGINT UNSIGNED             NOT NULL
);

DELETE FROM visit;
DELETE FROM game;
DELETE FROM tavern;
