INSERT INTO CLUB (NAME) VALUES('TC Wiesing')
INSERT INTO CLUB (NAME) VALUES('TC Vomp')
INSERT INTO CLUB (NAME) VALUES('TC Terfens Vomperbach')
INSERT INTO CLUB (NAME) VALUES('TC Breitenbach')
INSERT INTO CLUB (NAME) VALUES('TC Schwaz')
INSERT INTO CLUB (NAME) VALUES('TC Weerberg')



INSERT INTO USER (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, CLUB_NAME, PHONE) VALUES(TRUE, 'Daniel', 'Huber', 'passwd', 'admin', 'admin', '2016-01-01 00:00:00', 'TC Schwaz', '+4369910983630')
INSERT INTO USER_USER_ROLE (USER_USERNAME, ROLES) VALUES ('admin', 'ADMIN')
INSERT INTO USER_USER_ROLE (USER_USERNAME, ROLES) VALUES ('admin', 'TRAINER')

INSERT INTO USER (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, CLUB_NAME) VALUES(TRUE, 'John', 'Doe', 'passwd', 'johndoe', 'admin', '2016-01-01 00:00:00', 'TC Vomp')
INSERT INTO USER_USER_ROLE (USER_USERNAME, ROLES) VALUES ('johndoe', 'TRAINER')

INSERT INTO USER (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, CLUB_NAME) VALUES(TRUE, 'Susi', 'Kaufgern', 'passwd', 'susi', 'admin', '2016-01-01 00:00:00', 'TC Vomp')
INSERT INTO USER_USER_ROLE (USER_USERNAME, ROLES) VALUES ('susi', 'PLAYER')

INSERT INTO USER (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, CLUB_NAME) VALUES(TRUE, 'Max', 'Mustermann', 'passwd', 'max', 'admin', '2016-01-01 00:00:00', 'TC Breitenbach')
INSERT INTO USER_USER_ROLE (USER_USERNAME, ROLES) VALUES ('max', 'PLAYER')




INSERT INTO traininggroup (ID, NUM_PLAYED_SESSIONS, TRAINER_USERNAME, CLUB_NAME) VALUES(0, 18, 'johndoe', 'TC Wiesing')
INSERT INTO traininggroup (ID, NUM_PLAYED_SESSIONS, TRAINER_USERNAME, CLUB_NAME) VALUES(1, 18, 'johndoe', 'TC Vomp')
INSERT INTO traininggroup (ID, NUM_PLAYED_SESSIONS, TRAINER_USERNAME, CLUB_NAME) VALUES(2, 18, 'admin', 'TC Wiesing')


INSERT INTO TRAININGGROUP_PLAYERS (TRAINING_GROUP_ID, PLAYERS_USERNAME) VALUES(0, 'susi')
INSERT INTO TRAININGGROUP_PLAYERS (TRAINING_GROUP_ID, PLAYERS_USERNAME) VALUES(0, 'max')
INSERT INTO TRAININGGROUP_PLAYERS (TRAINING_GROUP_ID, PLAYERS_USERNAME) VALUES(1, 'max')

INSERT INTO TRAININGGROUP_PLAYERS (TRAINING_GROUP_ID, PLAYERS_USERNAME) VALUES(2, 'susi')


INSERT INTO TRAINING (ID, TRAINER_USERNAME, ORIGINAL_TRAINER_USERNAME, BULLET_POINTS, COMMENT, COURT, CLUB_NAME, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(0, 'johndoe', 'johndoe', 'some bullet points', 'some comments', 1, 'TC Wiesing', 60, '2022-02-21 18:30', 8, 0)
INSERT INTO TRAINING (ID, TRAINER_USERNAME, ORIGINAL_TRAINER_USERNAME, BULLET_POINTS, COMMENT, COURT, CLUB_NAME, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(1, 'johndoe', 'johndoe', 'some bullet points', 'some comments', 2, 'TC Wiesing', 120, '2022-02-21 14:30', 8, 0)
INSERT INTO TRAINING (ID, TRAINER_USERNAME, ORIGINAL_TRAINER_USERNAME, BULLET_POINTS, COMMENT, COURT, CLUB_NAME, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(2, 'johndoe', 'johndoe', 'some bullet points', 'some comments', 3, 'TC Schwaz', 60, '2022-02-22 13:30', 8, 1)

INSERT INTO TRAINING (ID, TRAINER_USERNAME, ORIGINAL_TRAINER_USERNAME, BULLET_POINTS, COMMENT, COURT, CLUB_NAME, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(3, 'admin', 'admin', 'some bullet points', 'some comments', 3, 'TC Schwaz', 60, '2022-02-22 13:30', 8, 1)
INSERT INTO TRAINING (ID, TRAINER_USERNAME, ORIGINAL_TRAINER_USERNAME, BULLET_POINTS, COMMENT, COURT, CLUB_NAME, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(4, 'admin', 'admin', 'some bullet points', 'some comments', 3, 'TC Schwaz', 60, '2022-02-23 13:30', 8, 1)
INSERT INTO TRAINING (ID, TRAINER_USERNAME, ORIGINAL_TRAINER_USERNAME, BULLET_POINTS, COMMENT, COURT, CLUB_NAME, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(5, 'johndoe', 'johndoe', 'some bullet points', 'some comments', 3, 'TC Schwaz', 60, '2022-02-24 14:30', 8, 1)
INSERT INTO TRAINING (ID, TRAINER_USERNAME, ORIGINAL_TRAINER_USERNAME, BULLET_POINTS, COMMENT, COURT, CLUB_NAME, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(6, 'admin', 'admin', 'some bullet points', 'some comments', 3, 'TC Schwaz', 60, '2022-02-24 13:30', 8, 1)
INSERT INTO TRAINING (ID, TRAINER_USERNAME, ORIGINAL_TRAINER_USERNAME, BULLET_POINTS, COMMENT, COURT, CLUB_NAME, DURATION_MINUTES, DATE_TIME, WEEK_NUM, IS_FREE, TRAINING_GROUP_ID) VALUES(7, 'johndoe', 'johndoe', 'some bullet points', 'some comments', 3, 'TC Schwaz', 60, '2022-02-24 13:30', 8, true, 1)
INSERT INTO TRAINING (ID, TRAINER_USERNAME, ORIGINAL_TRAINER_USERNAME, BULLET_POINTS, COMMENT, COURT, CLUB_NAME, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(8, 'admin', 'johndoe', 'some bullet points', 'some comments', 3, 'TC Schwaz', 60, '2022-02-25 13:30', 8, 2)
INSERT INTO TRAINING (ID, TRAINER_USERNAME, ORIGINAL_TRAINER_USERNAME, BULLET_POINTS, COMMENT, COURT, CLUB_NAME, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(9, 'johndoe', 'johndoe', 'some bullet points', 'some comments', 3, 'TC Schwaz', 60, '2022-02-25 13:30', 8, 2)
INSERT INTO TRAINING (ID, TRAINER_USERNAME, ORIGINAL_TRAINER_USERNAME, BULLET_POINTS, COMMENT, COURT, CLUB_NAME, DURATION_MINUTES, DATE_TIME, WEEK_NUM, IS_FREE, TRAINING_GROUP_ID) VALUES(10, 'admin', 'admin', 'some bullet points', 'some comments', 3, 'TC Schwaz', 60, '2022-02-26 13:30', 9, true, 2)

INSERT INTO TRAINING_ATTENDEES (TRAINING_ID, ATTENDEES_USERNAME) VALUES(0, 'max')
