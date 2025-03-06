--liquibase formatted sql logicalFilePath:db/changelog/database-changelog.sql

--changeset akvine:CUSTODIAN-1-1
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'DB_LOCK'
CREATE TABLE DB_LOCK
(
    LOCK_ID      VARCHAR(200)                        NOT NULL,
    PROCESS_ID   VARCHAR(36)                         NOT NULL,
    CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX DB_LOCK_ID_INDX ON DB_LOCK (LOCK_ID);
--rollback not required

--changeset akvine:CUSTODIAN-1-2
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'DB_LOCK_KEEPALIVE'
CREATE TABLE DB_LOCK_KEEPALIVE
(
    PROCESS_ID   VARCHAR(36)                         NOT NULL,
    EXPIRY_DATE  TIMESTAMP                           NOT NULL,
    CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX DB_LOCK_KEEP_INDX ON DB_LOCK_KEEPALIVE (PROCESS_ID);
--rollback not required

--changeset akvine:CUSTODIAN-1-3
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'ASYNC_DB_LOCKS'
CREATE TABLE ASYNC_DB_LOCKS
(
    LOCK_ID    VARCHAR(200) NOT NULL,
    EXPIRE     DECIMAL(22)  NOT NULL,
    CREATOR_ID VARCHAR(36)  NOT NULL,
    LOCK_STATE VARCHAR(50)  NOT NULL
);
CREATE UNIQUE INDEX ASYNC_DB_LOCKS_INDX ON ASYNC_DB_LOCKS (LOCK_ID);
--rollback not required

--changeset akvine:CUSTODIAN-1-4
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'XDUAL'
CREATE TABLE XDUAL
(
    DUMMY VARCHAR(1)
);
INSERT INTO XDUAL (DUMMY)
VALUES ('X');
--rollback not required

--changeset akvine:CUSTODIAN-1-5
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'CLIENT_ENTITY' and table_schema = 'public';
CREATE TABLE CLIENT_ENTITY
(
    ID           BIGINT       NOT NULL,
    UUID         VARCHAR(255) NOT NULL,
    CREATED_DATE TIMESTAMP    NOT NULL,
    UPDATED_DATE TIMESTAMP,
    IS_DELETED   BOOLEAN      NOT NULL,
    DELETED_DATE TIMESTAMP,
    AGE          INTEGER      NOT NULL,
    EMAIL        VARCHAR(255) NOT NULL,
    FIRST_NAME   VARCHAR(255) NOT NULL,
    HASH         VARCHAR(255) NOT NULL,
    LAST_NAME    VARCHAR(255) NOT NULL,
    CONSTRAINT CLIENT_PKEY PRIMARY KEY (id)
);
CREATE SEQUENCE SEQ_CLIENT_ENTITY START WITH 1 INCREMENT BY 1000;
--rollback not required

--changeset akvine:CUSTODIAN-1-6
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'APP_ENTITY' and table_schema = 'public';
CREATE TABLE APP_ENTITY
(
    ID           BIGINT       NOT NULL,
    TITLE        VARCHAR(255) NOT NULL,
    CREATED_DATE TIMESTAMP    NOT NULL,
    UPDATED_DATE TIMESTAMP,
    IS_DELETED   BOOLEAN      NOT NULL,
    DELETED_DATE TIMESTAMP,
    DESCRIPTION VARCHAR(255),
    client_id    bigint,
    CONSTRAINT APP_PKEY PRIMARY KEY (id),
    CONSTRAINT APP_CLIENT_FKEY FOREIGN KEY (CLIENT_ID) REFERENCES CLIENT_ENTITY (ID)
);
CREATE SEQUENCE SEQ_APP_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE INDEX APP_CLIENT_ID_INDX ON APP_ENTITY (client_id);
CREATE UNIQUE INDEX APP_TITLE_CLIENT_ID_INDX ON APP_ENTITY (TITLE, client_id);
--rollback not required

--changeset akvine:CUSTODIAN-1-7
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'PROPERTY_ENTITY' and table_schema = 'public';
CREATE TABLE PROPERTY_ENTITY
(
    ID           BIGINT       NOT NULL,
    CREATED_DATE TIMESTAMP    NOT NULL,
    UPDATED_DATE TIMESTAMP,
    IS_DELETED   BOOLEAN      NOT NULL,
    DELETED_DATE TIMESTAMP,
    KEY          VARCHAR(255) NOT NULL,
    VALUE        VARCHAR(512) NOT NULL,
    PROFILE      VARCHAR(255) NOT NULL,
    DESCRIPTION  VARCHAR(255),
    app_id       bigint,
    CONSTRAINT   PROPERTY_PKEY PRIMARY KEY (id),
    CONSTRAINT   PROPERTY_APP_FKEY FOREIGN KEY (APP_ID) REFERENCES APP_ENTITY (ID)
);
CREATE SEQUENCE SEQ_PROPERTY_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE INDEX PROPERTY_APP_ID_INDX ON PROPERTY_ENTITY (app_id);
CREATE UNIQUE INDEX PROPERTY_KEY_PROFILE_APP_ID ON PROPERTY_ENTITY (KEY, PROFILE, app_id);
--rollback not required

--changeset akvine:CUSTODIAN-1-8
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'SPRING_SESSION' and table_schema = 'public';
CREATE TABLE SPRING_SESSION
(
    PRIMARY_ID            VARCHAR(36)    NOT NULL,
    SESSION_ID            VARCHAR(36),
    CREATION_TIME         NUMERIC(19, 0) NOT NULL,
    LAST_ACCESS_TIME      NUMERIC(19, 0) NOT NULL,
    MAX_INACTIVE_INTERVAL NUMERIC(10, 0) NOT NULL,
    EXPIRY_TIME           NUMERIC(19, 0) NOT NULL,
    PRINCIPAL_NAME        VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);
CREATE INDEX SPRING_SESSION_INDX ON SPRING_SESSION(LAST_ACCESS_TIME);
--rollback not required

--changeset akvine:CUSTODIAN-1-9
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'SPRING_SESSION_ATTRIBUTES' and table_schema = 'public';
CREATE TABLE SPRING_SESSION_ATTRIBUTES
(
    SESSION_PRIMARY_ID VARCHAR(36),
    ATTRIBUTE_NAME     VARCHAR(200),
    ATTRIBUTE_BYTES    BYTEA,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION (PRIMARY_ID) ON DELETE CASCADE
);
CREATE INDEX SPRING_SESSION_ATTRIBUTES_INDX on SPRING_SESSION_ATTRIBUTES (SESSION_PRIMARY_ID);
--rollback not required

--changeset akvine:CUSTODIAN-1-10
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'AUTH_ACTION_ENTITY' and table_schema = 'public';
CREATE TABLE AUTH_ACTION_ENTITY
(
    ID                        BIGINT                              NOT NULL,
    SESSION_ID                VARCHAR(144)                        NOT NULL,
    LOGIN                     VARCHAR(64)                         NOT NULL,
    STARTED_DATE              TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ACTION_EXPIRED_AT         TIMESTAMP                           NOT NULL,
    PWD_INVALID_ATTEMPTS_LEFT INTEGER                             NOT NULL,
    OTP_COUNT_LEFT            INTEGER                             NOT NULL,
    OTP_NUMBER                INTEGER,
    OTP_LAST_UPDATE           TIMESTAMP,
    OTP_EXPIRED_AT            TIMESTAMP,
    OTP_INVALID_ATTEMPTS_LEFT INTEGER                             NOT NULL,
    OTP_VALUE                 VARCHAR(32),
    CONSTRAINT AUTH_ACTION_PK PRIMARY KEY (ID)
);
CREATE SEQUENCE SEQ_AUTH_ACTION_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE UNIQUE INDEX AUTH_ACTION_INDX on AUTH_ACTION_ENTITY (LOGIN);
CREATE INDEX AUTH_ACTION_EXP_INDX on AUTH_ACTION_ENTITY (ACTION_EXPIRED_AT);
--rollback not required

--changeset akvine:CUSTODIAN-1-11
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'REGISTRATION_ACTION_ENTITY' and table_schema = 'public';
CREATE TABLE REGISTRATION_ACTION_ENTITY
(
    ID                        BIGINT                              NOT NULL,
    SESSION_ID                VARCHAR(144)                        NOT NULL,
    LOGIN                     VARCHAR(64)                         NOT NULL,
    STARTED_DATE              TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ACTION_EXPIRED_AT         TIMESTAMP                           NOT NULL,
    STATE                     VARCHAR(32)                         NOT NULL,
    OTP_COUNT_LEFT            INTEGER                             NOT NULL,
    OTP_NUMBER                INTEGER,
    OTP_LAST_UPDATE           TIMESTAMP,
    OTP_EXPIRED_AT            TIMESTAMP,
    OTP_INVALID_ATTEMPTS_LEFT INTEGER                             NOT NULL,
    OTP_VALUE                 VARCHAR(32),
    CONSTRAINT REGISTRATION_ACTION_PK PRIMARY KEY (ID)
);
CREATE SEQUENCE SEQ_REGISTRATION_ACTION_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE UNIQUE INDEX REGISTRATION_ACTION_LOGIN_INDX ON REGISTRATION_ACTION_ENTITY (LOGIN);
CREATE INDEX REGISTRATION_ACTION_AEA_INDX ON REGISTRATION_ACTION_ENTITY (ACTION_EXPIRED_AT);
--rollback not required


--changeset akvine:CUSTODIAN-1-12
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'BLOCKED_CREDENTIALS_ENTITY' and table_schema = 'public';
CREATE TABLE BLOCKED_CREDENTIALS_ENTITY
(
    ID               BIGINT                              NOT NULL,
    LOGIN            VARCHAR(64)                         NOT NULL,
    BLOCK_START_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    BLOCK_END_DATE   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT BLOCKED_CREDENTIALS_PK PRIMARY KEY (ID)
);
CREATE SEQUENCE SEQ_BLOCKED_CREDENTIALS_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE UNIQUE INDEX BLOCKED_CREDENTIALS_LOGIN_INDX ON BLOCKED_CREDENTIALS_ENTITY (LOGIN);
--rollback not required

--changeset akvine:CUSTODIAN-1-13
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'OTP_COUNTER_ENTITY' and table_schema = 'public';
CREATE TABLE OTP_COUNTER_ENTITY
(
    ID           BIGINT                              NOT NULL,
    LOGIN        VARCHAR(64)                         NOT NULL,
    LAST_UPDATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    OTP_VALUE    BIGINT    DEFAULT 1                 NOT NULL,
    CONSTRAINT OTP_COUNTER_PK PRIMARY KEY (ID)
);
CREATE SEQUENCE SEQ_OTP_COUNTER_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE UNIQUE INDEX OTP_COUNTER_LOGIN_INDX ON OTP_COUNTER_ENTITY (LOGIN);
--rollback not required

--changeset akvine:CUSTODIAN-1-14
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'ACCESS_RESTORE_ACTION_ENTITY' and table_schema = 'public';
CREATE TABLE ACCESS_RESTORE_ACTION_ENTITY
(
    ID                        BIGINT                              NOT NULL,
    SESSION_ID                VARCHAR(144)                        NOT NULL,
    LOGIN                     VARCHAR(64)                         NOT NULL,
    STARTED_DATE              TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ACTION_EXPIRED_AT         TIMESTAMP                           NOT NULL,
    STATE                     VARCHAR(32)                         NOT NULL,
    OTP_COUNT_LEFT            INTEGER                             NOT NULL,
    OTP_NUMBER                INTEGER,
    OTP_LAST_UPDATE           TIMESTAMP,
    OTP_EXPIRED_AT            TIMESTAMP,
    OTP_INVALID_ATTEMPTS_LEFT INTEGER                             NOT NULL,
    OTP_VALUE                 VARCHAR(32),
    CONSTRAINT ACCESS_RESTORE_ACTION_PK PRIMARY KEY (ID)
);
CREATE SEQUENCE SEQ_ACCESS_RESTORE_ACTION_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE UNIQUE INDEX ACCESS_RESTORE_LOGIN_INDX on ACCESS_RESTORE_ACTION_ENTITY (LOGIN);
CREATE INDEX ACCESS_RESTORE_EXP_INDX on ACCESS_RESTORE_ACTION_ENTITY (ACTION_EXPIRED_AT);
--rollback not required

--changeset akvine:CUSTODIAN-1-15
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'ACCESS_TOKEN_ENTITY' and table_schema = 'public';
CREATE TABLE ACCESS_TOKEN_ENTITY
(
    ID              BIGINT                             NOT NULL,
    CREATED_DATE    TIMESTAMP                          NOT NULL,
    UPDATED_DATE    TIMESTAMP,
    TOKEN           VARCHAR(40)                        NOT NULL,
    EXPIRED_AT      TIMESTAMP                          NOT NULL,
    app_id          bigint                             NOT NULL,
    CONSTRAINT ACCESS_TOKEN_APP_FKEY FOREIGN KEY (app_id) REFERENCES APP_ENTITY (ID),
    CONSTRAINT ACCESS_TOKEN_PK PRIMARY KEY (ID)
);
CREATE SEQUENCE SEQ_ACCESS_TOKEN_ENTITY START WITH 1 INCREMENT BY 1000;
CREATE UNIQUE INDEX ACCESS_TOKEN_TOKEN_INDX ON ACCESS_TOKEN_ENTITY (TOKEN);
CREATE INDEX ACCESS_TOKEN_APP_ID ON ACCESS_TOKEN_ENTITY (app_id);

--changeset akvine:CUSTODIAN-2-1
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns WHERE table_name = 'PROPERTY_ENTITY' AND column_name = 'MASK';
ALTER TABLE PROPERTY_ENTITY ADD MASK CHAR(1);

--changeset akvine:CUSTODIAN-2-2
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns WHERE table_name = 'PROPERTY_ENTITY' AND column_name = 'MASKING_RADIUS';
ALTER TABLE PROPERTY_ENTITY ADD MASKING_RADIUS INT;