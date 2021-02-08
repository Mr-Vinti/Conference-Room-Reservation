# SQL Database Backup

## Reasons
In order to save expenses, the database will be created on a need basis.

## Main Details

The sql database link is: `mps-rooms.database.windows.net`

The sql database name is: `MpsRooms`

The sql database user is: `adminMPS`

The sql database pass is: `!passw0rd`

## List of tables

1. FOLLOW
2. RESERVATION
3. ROOM
4. STATUS

## DDLs

1.  FOLLOW

```sql
CREATE TABLE MpsRooms.dbo.FOLLOW (
	ROOM_ID int NOT NULL,
	EMAIL nvarchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	CONSTRAINT FOLLOW_PK PRIMARY KEY (ROOM_ID,EMAIL)
) GO
 CREATE NONCLUSTERED INDEX FOLLOW_EMAIL_IDX ON dbo.FOLLOW (  EMAIL ASC  )  
	 WITH (  PAD_INDEX = OFF ,FILLFACTOR = 100  ,SORT_IN_TEMPDB = OFF , IGNORE_DUP_KEY = OFF , STATISTICS_NORECOMPUTE = OFF , ONLINE = OFF , ALLOW_ROW_LOCKS = ON , ALLOW_PAGE_LOCKS = ON  )
	 ON [PRIMARY ]  GO;


-- MpsRooms.dbo.FOLLOW foreign keys

ALTER TABLE MpsRooms.dbo.FOLLOW ADD CONSTRAINT FOLLOW_FK FOREIGN KEY (ROOM_ID) REFERENCES MpsRooms.dbo.ROOM(ID) GO;
```

2. RESERVATION

```sql
CREATE TABLE MpsRooms.dbo.RESERVATION (
	ID int IDENTITY(0,1) NOT NULL,
	ROOM_ID int NOT NULL,
	NAM nvarchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	EMAIL nvarchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	BEGIN_DT datetime NOT NULL,
	END_DT datetime NOT NULL,
	ACTL_END_DT datetime NULL,
	REASN nvarchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	CONSTRAINT RESERVATION_PK PRIMARY KEY (ID)
) GO;


-- MpsRooms.dbo.RESERVATION foreign keys

ALTER TABLE MpsRooms.dbo.RESERVATION ADD CONSTRAINT RESERVATION_FK FOREIGN KEY (ROOM_ID) REFERENCES MpsRooms.dbo.ROOM(ID) GO;
```

3. ROOM

```sql
CREATE TABLE MpsRooms.dbo.ROOM (
	ID int IDENTITY(0,1) NOT NULL,
	NAM nvarchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	STA_CD int NOT NULL,
	[DESC] nvarchar(1000) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	PENDNG_DT datetime NULL,
	PENDNG_BY nvarchar(100) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	CONSTRAINT Room_PK PRIMARY KEY (ID)
) GO;


-- MpsRooms.dbo.ROOM foreign keys

ALTER TABLE MpsRooms.dbo.ROOM ADD CONSTRAINT ROOM_FK FOREIGN KEY (STA_CD) REFERENCES MpsRooms.dbo.STATUS(CD) GO;
```

4. STATUS

```sql
CREATE TABLE MpsRooms.dbo.STATUS (
	CD int NOT NULL,
	[DESC] nvarchar(30) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	CONSTRAINT STATUS_PK PRIMARY KEY (CD)
) GO;
```

## Model Data

1.  FOLLOW

```sql
INSERT INTO MpsRooms.dbo.FOLLOW
(ROOM_ID, EMAIL)
VALUES(3, 'ion.titulescu@ipsssnieksss.onmicrosoft.com');
INSERT INTO MpsRooms.dbo.FOLLOW
(ROOM_ID, EMAIL)
VALUES(1, 'marius.vintila@stud.acs.upb.ro');
```

2. RESERVATION

```sql
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(1, 1, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-23 22:20:00.000', '2021-01-25 22:28:19.000', '2021-01-25 22:29:47.663', 'Because Apples');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(2, 2, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-23 22:20:00.000', '2021-01-25 22:28:19.000', '2021-01-25 00:08:24.107', 'Because Pears');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(3, 1, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-23 10:00:00.000', '2021-01-23 12:00:00.000', '2021-01-23 12:00:00.000', 'Test');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(4, 1, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-23 12:00:00.000', '2021-01-23 14:00:00.000', '2021-01-23 14:00:00.000', 'Test');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(5, 1, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-23 14:00:00.000', '2021-01-23 15:00:00.000', '2021-01-23 15:00:00.000', 'Test');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(6, 1, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-23 15:00:00.000', '2021-01-23 16:00:00.000', '2021-01-23 16:00:00.000', 'Test');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(7, 1, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-23 17:00:00.000', '2021-01-23 17:00:00.000', '2021-01-23 17:00:00.000', 'Test');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(8, 1, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-23 18:00:00.000', '2021-01-23 18:00:00.000', '2021-01-23 18:00:00.000', 'Test');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(11, 3, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-24 23:24:00.000', '2021-01-24 23:26:00.000', '2021-01-24 23:26:00.000', 'Because Apples');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(14, 3, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-24 23:27:00.000', '2021-01-24 23:30:00.000', '2021-01-24 23:30:00.000', 'Because Apples');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(15, 0, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-26 00:24:52.473', '2021-01-26 00:24:00.000', '2021-01-26 00:24:00.000', 'sdaas');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(16, 0, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-26 00:26:22.063', '2021-01-26 00:30:00.000', '2021-01-26 00:28:32.573', 'dasda');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(17, 0, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-26 00:35:19.007', '2021-01-26 00:35:00.000', '2021-01-26 00:35:00.000', 'dfdsa');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(18, 0, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-26 00:48:07.887', '2021-01-26 01:05:00.000', '2021-01-26 01:01:34.463', 'bvcbx');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(19, 0, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-26 01:01:49.080', '2021-01-26 01:09:00.000', '2021-01-26 01:09:00.000', 'kjhgkgf');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(20, 0, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-26 01:12:48.380', '2021-01-26 01:20:00.000', '2021-01-26 01:20:00.000', 'hnfgdd');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(21, 0, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-26 01:22:24.087', '2021-01-26 01:31:00.000', '2021-01-26 01:31:00.000', 'cxv b');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(22, 0, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-26 01:31:23.920', '2021-01-26 01:40:00.000', '2021-01-26 01:40:00.000', 'mgbmn');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(23, 2, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-26 01:44:19.423', '2021-01-26 01:45:00.000', '2021-01-26 01:45:00.000', 'bvcn');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(25, 2, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-26 01:47:08.600', '2021-01-26 01:48:00.000', '2021-01-26 01:48:00.000', 'gh');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(26, 2, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-26 02:01:39.587', '2021-01-26 02:02:00.000', '2021-01-26 02:02:00.000', 'gfhdg');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(27, 0, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-26 02:10:55.743', '2021-01-26 02:20:00.000', '2021-01-26 02:20:00.000', 'zsgfxdchjvk');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(28, 0, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-28 22:14:52.667', '2021-01-28 22:26:00.000', '2021-01-28 22:26:00.000', 'fdsafds');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(29, 7, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-28 23:12:21.047', '2021-01-28 23:30:00.000', '2021-01-28 23:30:00.000', 'Creating new design concept for project Omnichannel');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(30, 3, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-29 02:01:56.633', '2021-01-29 02:30:00.000', '2021-01-29 02:30:00.000', 'Pișici');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(31, 1, 'Ion Titulescu', 'ion.titulescu@ipsssnieksss.onmicrosoft.com', '2021-01-29 02:05:30.757', '2021-01-29 02:31:00.000', '2021-01-29 02:31:00.000', 'Căței');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(32, 0, 'Marius-Mihai VINTILĂ (94531)', 'marius.vintila@stud.acs.upb.ro', '2021-01-29 08:41:25.070', '2021-01-29 09:00:00.000', '2021-01-29 09:00:00.000', 'Team meeting');
INSERT INTO MpsRooms.dbo.RESERVATION
(ID, ROOM_ID, NAM, EMAIL, BEGIN_DT, END_DT, ACTL_END_DT, REASN)
VALUES(33, 7, 'Ion Titulescu', 'ion.titulescu@ipsssnieksss.onmicrosoft.com', '2021-01-29 08:46:16.637', '2021-01-29 08:47:00.000', '2021-01-29 08:47:00.000', 'ojhgjhfg');
```

3. ROOM

```sql
INSERT INTO MpsRooms.dbo.ROOM
(ID, NAM, STA_CD, [DESC], PENDNG_DT, PENDNG_BY)
VALUES(0, 'Collaboration Conference Room', 2, 'This room can be used for team meetings. It can hold up to 15 people', NULL, NULL);
INSERT INTO MpsRooms.dbo.ROOM
(ID, NAM, STA_CD, [DESC], PENDNG_DT, PENDNG_BY)
VALUES(1, 'Room 2', 0, 'Second Room', NULL, NULL);
INSERT INTO MpsRooms.dbo.ROOM
(ID, NAM, STA_CD, [DESC], PENDNG_DT, PENDNG_BY)
VALUES(2, 'Room 3', 0, 'Third Room', NULL, NULL);
INSERT INTO MpsRooms.dbo.ROOM
(ID, NAM, STA_CD, [DESC], PENDNG_DT, PENDNG_BY)
VALUES(3, 'Room 4', 0, 'Fourth Room', NULL, NULL);
INSERT INTO MpsRooms.dbo.ROOM
(ID, NAM, STA_CD, [DESC], PENDNG_DT, PENDNG_BY)
VALUES(7, 'Creativity Conference Room', 0, 'This room can be used for design team meetings. This room can hold up to 6 people.', NULL, NULL);
INSERT INTO MpsRooms.dbo.ROOM
(ID, NAM, STA_CD, [DESC], PENDNG_DT, PENDNG_BY)
VALUES(8, 'Test Conference Room', 0, 'Test Description', NULL, NULL);
```

4. STATUS

```sql
INSERT INTO MpsRooms.dbo.STATUS
(CD, [DESC])
VALUES(0, 'Free');
INSERT INTO MpsRooms.dbo.STATUS
(CD, [DESC])
VALUES(1, 'Pending Reservation');
INSERT INTO MpsRooms.dbo.STATUS
(CD, [DESC])
VALUES(2, 'Occupied');
```