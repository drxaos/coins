--  Changeset migration/changelog.xml::2015-10-26 18:50-1::generated
CREATE TABLE coins.accounts (user_id BIGINT NOT NULL, name VARCHAR(255) NOT NULL, type INT NOT NULL, currency VARCHAR(255) NOT NULL, startValue DECIMAL(20, 2) NULL, created datetime NOT NULL, closed datetime NULL, id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NULL, CONSTRAINT PK_ACCOUNTS PRIMARY KEY (id));

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-1', 'generated', 'migration/changelog.xml', NOW(), 25, '7:93fd33f4a9301b47d35e549fbabafa51', 'createTable', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-2::generated
CREATE TABLE coins.categories (user_id BIGINT NOT NULL, name VARCHAR(255) NOT NULL, expense BIT NOT NULL, income BIT NOT NULL, id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NULL, CONSTRAINT PK_CATEGORIES PRIMARY KEY (id));

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-2', 'generated', 'migration/changelog.xml', NOW(), 26, '7:8af410db6eb2996c37b9767b3e9c47b6', 'createTable', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-3::generated
CREATE TABLE coins.transactions (user_id BIGINT NOT NULL, date datetime NOT NULL, category_id BIGINT NULL, outcomeAccount_id BIGINT NULL, outcomeValue DECIMAL(20, 2) NULL, incomeAccount_id BIGINT NULL, incomeValue DECIMAL(20, 2) NULL, comment VARCHAR(255) NULL, id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NULL, CONSTRAINT PK_TRANSACTIONS PRIMARY KEY (id));

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-3', 'generated', 'migration/changelog.xml', NOW(), 27, '7:ba1beac8e1140eb657c0d1ca341fdf97', 'createTable', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-4::generated
CREATE TABLE coins.users (name VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, email VARCHAR(255) NOT NULL, lang VARCHAR(255) NOT NULL, id BIGINT AUTO_INCREMENT NOT NULL, version BIGINT NULL, CONSTRAINT PK_USERS PRIMARY KEY (id));

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-4', 'generated', 'migration/changelog.xml', NOW(), 28, '7:3fd5441be50749e284b8f5d20f54ce31', 'createTable', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-5::generated
ALTER TABLE coins.categories ADD CONSTRAINT user_id UNIQUE (user_id, name);

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-5', 'generated', 'migration/changelog.xml', NOW(), 29, '7:c1075a2fa99062f18b87b4f65aca11d2', 'addUniqueConstraint', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-6::generated
ALTER TABLE coins.users ADD CONSTRAINT users_name_idx UNIQUE (name);

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-6', 'generated', 'migration/changelog.xml', NOW(), 30, '7:2433fb5e52f791006b76527e1d81ed6b', 'addUniqueConstraint', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-7::generated
CREATE INDEX FK_transactions_user_id_users_id ON coins.transactions(user_id);

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-7', 'generated', 'migration/changelog.xml', NOW(), 31, '7:8b44d4b8238fc9d6b5538ab79a13bf81', 'createIndex', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-8::generated
CREATE INDEX accounts_closed_idx ON coins.accounts(closed);

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-8', 'generated', 'migration/changelog.xml', NOW(), 32, '7:d4dde94b47459aa28a69abee57b8c1e3', 'createIndex', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-9::generated
CREATE INDEX accounts_created_idx ON coins.accounts(created);

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-9', 'generated', 'migration/changelog.xml', NOW(), 33, '7:10b0beffb0be1037b709317a87e236ed', 'createIndex', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-10::generated
CREATE INDEX accounts_name_idx ON coins.accounts(name);

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-10', 'generated', 'migration/changelog.xml', NOW(), 34, '7:47a774243e767ec4ac0db602f8058fbd', 'createIndex', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-11::generated
CREATE INDEX accounts_user_idx ON coins.accounts(user_id);

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-11', 'generated', 'migration/changelog.xml', NOW(), 35, '7:8cc3ec4b04d0e18941f83ca2fa2ec6f5', 'createIndex', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-12::generated
CREATE INDEX categories_name_idx ON coins.categories(name);

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-12', 'generated', 'migration/changelog.xml', NOW(), 36, '7:0aef91a25e2ecfd86066392ee368cb64', 'createIndex', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-13::generated
CREATE INDEX categories_user_idx ON coins.categories(user_id);

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-13', 'generated', 'migration/changelog.xml', NOW(), 37, '7:3178bf0df2bbfcf2d661e395536739e5', 'createIndex', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-14::generated
CREATE INDEX transactions_category_idx ON coins.transactions(category_id);

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-14', 'generated', 'migration/changelog.xml', NOW(), 38, '7:9f37e47afaa7d55152a9c68e4c97d33e', 'createIndex', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-15::generated
CREATE INDEX transactions_date_idx ON coins.transactions(date);

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-15', 'generated', 'migration/changelog.xml', NOW(), 39, '7:71e446e82318ab2cd6a064e36b2f7563', 'createIndex', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-16::generated
CREATE INDEX transactions_incomeAccount_idx ON coins.transactions(incomeAccount_id);

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-16', 'generated', 'migration/changelog.xml', NOW(), 40, '7:db80d869fff2720324163129323043c3', 'createIndex', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-17::generated
CREATE INDEX transactions_outcomeAccount_idx ON coins.transactions(outcomeAccount_id);

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-17', 'generated', 'migration/changelog.xml', NOW(), 41, '7:75b6d6e4be5d4050b1815d1f43728abf', 'createIndex', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-18::generated
CREATE INDEX users_email_idx ON coins.users(email);

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-18', 'generated', 'migration/changelog.xml', NOW(), 42, '7:62d82a728540c793227c368502143de0', 'createIndex', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-19::generated
ALTER TABLE coins.accounts ADD CONSTRAINT FK_accounts_user_id_users_id FOREIGN KEY (user_id) REFERENCES coins.users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-19', 'generated', 'migration/changelog.xml', NOW(), 43, '7:ca4e77db18922d768a36a80a7194c960', 'addForeignKeyConstraint', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-20::generated
ALTER TABLE coins.categories ADD CONSTRAINT FK_categories_user_id_users_id FOREIGN KEY (user_id) REFERENCES coins.users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-20', 'generated', 'migration/changelog.xml', NOW(), 44, '7:e3e24de4e456942f4929a093df105c25', 'addForeignKeyConstraint', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-21::generated
ALTER TABLE coins.transactions ADD CONSTRAINT FK_transactions_category_id_categories_id FOREIGN KEY (category_id) REFERENCES coins.categories (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-21', 'generated', 'migration/changelog.xml', NOW(), 45, '7:e8561e2faefcd3b939c83c059bfd3bd9', 'addForeignKeyConstraint', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-22::generated
ALTER TABLE coins.transactions ADD CONSTRAINT FK_transactions_incomeAccount_id_accounts_id FOREIGN KEY (incomeAccount_id) REFERENCES coins.accounts (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-22', 'generated', 'migration/changelog.xml', NOW(), 46, '7:44e1584ab034a1904cc80b6b2e245094', 'addForeignKeyConstraint', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-23::generated
ALTER TABLE coins.transactions ADD CONSTRAINT FK_transactions_outcomeAccount_id_accounts_id FOREIGN KEY (outcomeAccount_id) REFERENCES coins.accounts (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-23', 'generated', 'migration/changelog.xml', NOW(), 47, '7:0eeac6bdbca277a89fb35dd057483b5f', 'addForeignKeyConstraint', '', 'EXECUTED', NULL, NULL, '3.4.1');

--  Changeset migration/changelog.xml::2015-10-26 18:50-24::generated
ALTER TABLE coins.transactions ADD CONSTRAINT FK_transactions_user_id_users_id FOREIGN KEY (user_id) REFERENCES coins.users (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO coins.DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE) VALUES ('2015-10-26 18:50-24', 'generated', 'migration/changelog.xml', NOW(), 48, '7:a6e508264c039af06a5140980d73a331', 'addForeignKeyConstraint', '', 'EXECUTED', NULL, NULL, '3.4.1');

