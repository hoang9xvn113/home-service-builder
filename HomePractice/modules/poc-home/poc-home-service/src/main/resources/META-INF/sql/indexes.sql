create index IX_D8CED57F on DG_HomeEntry (floor, square, room);
create index IX_DA3AB0B1 on DG_HomeEntry (uuid_[$COLUMN_LENGTH:75$], companyId);
create unique index IX_4769AF73 on DG_HomeEntry (uuid_[$COLUMN_LENGTH:75$], groupId);