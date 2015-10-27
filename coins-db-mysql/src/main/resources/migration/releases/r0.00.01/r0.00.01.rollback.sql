--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-24::generated
ALTER TABLE coins.transactions DROP FOREIGN KEY FK_transactions_user_id_users_id;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-24' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-23::generated
ALTER TABLE coins.transactions DROP FOREIGN KEY FK_transactions_outcomeAccount_id_accounts_id;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-23' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-22::generated
ALTER TABLE coins.transactions DROP FOREIGN KEY FK_transactions_incomeAccount_id_accounts_id;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-22' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-21::generated
ALTER TABLE coins.transactions DROP FOREIGN KEY FK_transactions_category_id_categories_id;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-21' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-20::generated
ALTER TABLE coins.categories DROP FOREIGN KEY FK_categories_user_id_users_id;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-20' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-19::generated
ALTER TABLE coins.accounts DROP FOREIGN KEY FK_accounts_user_id_users_id;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-19' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-18::generated
DROP INDEX users_email_idx ON coins.users;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-18' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-17::generated
DROP INDEX transactions_outcomeAccount_idx ON coins.transactions;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-17' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-16::generated
DROP INDEX transactions_incomeAccount_idx ON coins.transactions;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-16' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-15::generated
DROP INDEX transactions_date_idx ON coins.transactions;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-15' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-14::generated
DROP INDEX transactions_category_idx ON coins.transactions;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-14' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-13::generated
DROP INDEX categories_user_idx ON coins.categories;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-13' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-12::generated
DROP INDEX categories_name_idx ON coins.categories;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-12' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-11::generated
DROP INDEX accounts_user_idx ON coins.accounts;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-11' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-10::generated
DROP INDEX accounts_name_idx ON coins.accounts;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-10' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-9::generated
DROP INDEX accounts_created_idx ON coins.accounts;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-9' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-8::generated
DROP INDEX accounts_closed_idx ON coins.accounts;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-8' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-7::generated
DROP INDEX FK_transactions_user_id_users_id ON coins.transactions;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-7' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-6::generated
ALTER TABLE coins.users DROP KEY users_name_idx;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-6' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-5::generated
ALTER TABLE coins.categories DROP KEY user_id;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-5' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-4::generated
DROP TABLE coins.users;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-4' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-3::generated
DROP TABLE coins.transactions;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-3' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-2::generated
DROP TABLE coins.categories;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-2' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-26 18:50-1::generated
DROP TABLE coins.accounts;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-26 18:50-1' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

