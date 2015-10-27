--  Changeset migration/changelog.xml::2015-10-27 15:10-1::generated
CREATE TABLE coins.options (name VARCHAR(255) NOT NULL, value VARCHAR(255) NOT NULL, id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NULL, CONSTRAINT PK_OPTIONS PRIMARY KEY (id));

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-27 15:10-1', 'generated', 'migration/changelog.xml', NOW(), 25, '7:22fefc2e15af23b45e031c5335b42d25', 'createTable', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-27 15:10-2::generated
CREATE TABLE coins.sessions (name VARCHAR(255) NOT NULL, created datetime NOT NULL, accessed datetime NOT NULL, userId BIGINT NULL, label VARCHAR(255) NULL, data BLOB NOT NULL, id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NULL, CONSTRAINT PK_SESSIONS PRIMARY KEY (id));

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-27 15:10-2', 'generated', 'migration/changelog.xml', NOW(), 26, '7:7f00214cd9ce37b5703f8a2e661f7e02', 'createTable', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-27 15:10-3::generated
ALTER TABLE coins.sessions ADD CONSTRAINT sessions_name_idx UNIQUE (name);

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-27 15:10-3', 'generated', 'migration/changelog.xml', NOW(), 27, '7:bcad4b4fc7a6240717d3be5327526d3d', 'addUniqueConstraint', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-27 15:10-4::generated
CREATE INDEX options_name_idx ON coins.options(name);

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-27 15:10-4', 'generated', 'migration/changelog.xml', NOW(), 28, '7:01771d56654134ca34e05c8f3e8db48b', 'createIndex', '', 'EXECUTED', NULL, NULL, '3.4.1');

