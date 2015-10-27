--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-27 15:10-4::generated
DROP INDEX options_name_idx ON coins.options;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-27 15:10-4' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-27 15:10-3::generated
ALTER TABLE coins.sessions DROP KEY sessions_name_idx;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-27 15:10-3' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-27 15:10-2::generated
DROP TABLE coins.sessions;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-27 15:10-2' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

--  Rolling Back ChangeSet: migration/changelog.xml::2015-10-27 15:10-1::generated
DROP TABLE coins.options;

DELETE FROM coins.DATABASECHANGELOG WHERE ID = '2015-10-27 15:10-1' AND AUTHOR = 'generated' AND FILENAME = 'migration/changelog.xml';

