INSERT INTO CLUB (ID) VALUES('TC-Wiesing')
INSERT INTO CLUB (ID) VALUES('TC-Vomp')
INSERT INTO CLUB (ID) VALUES('TC-Terfens-Vomperbach')
INSERT INTO CLUB (ID) VALUES('TC-Breitenbach')
INSERT INTO CLUB (ID) VALUES('TC-Schwaz')
INSERT INTO CLUB (ID) VALUES('TC-Weerberg')



INSERT INTO USER (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, CLUB_ID, PHONE, BIRTH_YEAR) VALUES(TRUE, 'Daniel', 'Huber', 'passwd', 'admin', 'admin', '2016-01-01 00:00:00', 'TC-Schwaz', '+4369910983630', 1968)
INSERT INTO USER_USER_ROLE (USER_USERNAME, ROLES) VALUES ('admin', 'ADMIN')
INSERT INTO USER_USER_ROLE (USER_USERNAME, ROLES) VALUES ('admin', 'TRAINER')

INSERT INTO USER (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, CLUB_ID, PHONE, BIRTH_YEAR) VALUES(TRUE, 'John', 'Doe', 'passwd', 'johndoe', 'admin', '2016-01-01 00:00:00', 'TC-Vomp', '+4369910983630', 2000)
INSERT INTO USER_USER_ROLE (USER_USERNAME, ROLES) VALUES ('johndoe', 'TRAINER')

INSERT INTO USER (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, CLUB_ID, BIRTH_YEAR) VALUES(TRUE, 'Susi', 'Kaufgern', 'passwd', 'susi', 'admin', '2016-01-01 00:00:00', 'TC-Vomp', 2014)
INSERT INTO USER_USER_ROLE (USER_USERNAME, ROLES) VALUES ('susi', 'PLAYER')

INSERT INTO USER (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, CLUB_ID, BIRTH_YEAR) VALUES(TRUE, 'Max', 'Mustermann', 'passwd', 'max', 'admin', '2016-01-01 00:00:00', 'TC-Breitenbach', 2012)
INSERT INTO USER_USER_ROLE (USER_USERNAME, ROLES) VALUES ('max', 'PLAYER')




INSERT INTO traininggroup (ID, TRAINER_USERNAME, CLUB_ID) VALUES(1, 'johndoe', 'TC-Wiesing')
INSERT INTO traininggroup (ID, TRAINER_USERNAME, CLUB_ID) VALUES(2, 'admin', 'TC-Vomp')
INSERT INTO traininggroup (ID, TRAINER_USERNAME, CLUB_ID) VALUES(3, 'johndoe', 'TC-Wiesing')


INSERT INTO TRAININGGROUP_PLAYERS (TRAINING_GROUPS_ID, PLAYERS_USERNAME) VALUES(1, 'susi')
INSERT INTO TRAININGGROUP_PLAYERS (TRAINING_GROUPS_ID, PLAYERS_USERNAME) VALUES(1, 'max')
INSERT INTO TRAININGGROUP_PLAYERS (TRAINING_GROUPS_ID, PLAYERS_USERNAME) VALUES(2, 'max')
INSERT INTO TRAININGGROUP_PLAYERS (TRAINING_GROUPS_ID, PLAYERS_USERNAME) VALUES(3, 'max')


INSERT INTO TRAINING (ID, TRAINER_USERNAME, PREV_TRAINER_USERNAME, BULLET_POINTS, COMMENTS, COURT, CLUB_ID, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(100, 'johndoe', 'johndoe', 'some bullet points', 'some comments', 1, 'TC-Wiesing', 60, '2022-07-04 18:30', 27, 1)
INSERT INTO TRAINING (ID, TRAINER_USERNAME, PREV_TRAINER_USERNAME, BULLET_POINTS, COMMENTS, COURT, CLUB_ID, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(1, 'johndoe', 'johndoe', 'some bullet points', 'some comments', 2, 'TC-Wiesing', 120, '2022-07-04 14:30', 27, 1)
INSERT INTO TRAINING (ID, TRAINER_USERNAME, PREV_TRAINER_USERNAME, BULLET_POINTS, COMMENTS, COURT, CLUB_ID, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(2, 'johndoe', 'admin', 'some bullet points', 'some comments', 3, 'TC-Schwaz', 60, '2022-07-05 13:30', 27, 2)

INSERT INTO TRAINING (ID, TRAINER_USERNAME, PREV_TRAINER_USERNAME, BULLET_POINTS, COMMENTS, COURT, CLUB_ID, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(3, 'admin', 'admin', 'some bullet points', 'some comments', 3, 'TC-Schwaz', 60, '2022-07-05 13:30', 15, 2)
INSERT INTO TRAINING (ID, TRAINER_USERNAME, PREV_TRAINER_USERNAME, BULLET_POINTS, COMMENTS, COURT, CLUB_ID, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(4, 'admin', 'admin', 'some bullet points', 'some comments', 3, 'TC-Schwaz', 60, '2022-07-06 13:30', 27, 2)
INSERT INTO TRAINING (ID, TRAINER_USERNAME, PREV_TRAINER_USERNAME, BULLET_POINTS, COMMENTS, COURT, CLUB_ID, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(5, 'johndoe', 'johndoe', 'some bullet points', 'some comments', 3, 'TC-Schwaz', 60, '2022-07-04 14:30', 27, 2)
INSERT INTO TRAINING (ID, TRAINER_USERNAME, PREV_TRAINER_USERNAME, BULLET_POINTS, COMMENTS, COURT, CLUB_ID, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(6, 'admin', 'admin', 'some bullet points', 'some comments', 3, 'TC-Schwaz', 60, '2022-07-07 13:30', 27, 2)
INSERT INTO TRAINING (ID, TRAINER_USERNAME, PREV_TRAINER_USERNAME, BULLET_POINTS, COMMENTS, COURT, CLUB_ID, DURATION_MINUTES, DATE_TIME, WEEK_NUM, IS_FREE, TRAINING_GROUP_ID) VALUES(7, 'johndoe', 'johndoe', 'some bullet points', 'some comments', 3, 'TC-Schwaz', 60, '2022-04-08 13:30', 27, true, 2)
INSERT INTO TRAINING (ID, TRAINER_USERNAME, PREV_TRAINER_USERNAME, BULLET_POINTS, COMMENTS, COURT, CLUB_ID, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(8, 'admin', 'johndoe', 'some bullet points', 'some comments', 3, 'TC-Schwaz', 60, '2022-07-09 13:30', 27, 3)
INSERT INTO TRAINING (ID, TRAINER_USERNAME, PREV_TRAINER_USERNAME, BULLET_POINTS, COMMENTS, COURT, CLUB_ID, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(9, 'johndoe', 'johndoe', 'some bullet points', 'some comments', 3, 'TC-Schwaz', 60, '2022-07-08 13:30', 27, 3)
INSERT INTO TRAINING (ID, TRAINER_USERNAME, PREV_TRAINER_USERNAME, BULLET_POINTS, COMMENTS, COURT, CLUB_ID, DURATION_MINUTES, DATE_TIME, WEEK_NUM, IS_FREE, TRAINING_GROUP_ID) VALUES(10, 'admin', 'admin', 'some bullet points', 'some comments', 3, 'TC-Schwaz', 60, '2022-07-07 13:30', 15, true, 3)

INSERT INTO TRAINING_ATTENDEES (TRAINING_ID, ATTENDEES_USERNAME) VALUES(100, 'max')
