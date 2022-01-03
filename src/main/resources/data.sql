INSERT INTO USER (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE) VALUES(TRUE, 'Daniel', 'Huber', 'passwd', 'admin', 'admin', '2016-01-01 00:00:00')
INSERT INTO USER_USER_ROLE (USER_USERNAME, ROLES) VALUES ('admin', 'ADMIN')
INSERT INTO USER_USER_ROLE (USER_USERNAME, ROLES) VALUES ('admin', 'TRAINER')

INSERT INTO USER (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE) VALUES(TRUE, 'John', 'Doe', 'passwd', 'johndoe', 'admin', '2016-01-01 00:00:00')
INSERT INTO USER_USER_ROLE (USER_USERNAME, ROLES) VALUES ('johndoe', 'TRAINER')

INSERT INTO USER (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE) VALUES(TRUE, 'Susi', 'Kaufgern', 'passwd', 'user1', 'admin', '2016-01-01 00:00:00')
INSERT INTO USER_USER_ROLE (USER_USERNAME, ROLES) VALUES ('user1', 'PLAYER')

INSERT INTO USER (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE) VALUES(TRUE, 'Max', 'Mustermann', 'passwd', 'user2', 'admin', '2016-01-01 00:00:00')
INSERT INTO USER_USER_ROLE (USER_USERNAME, ROLES) VALUES ('user2', 'PLAYER')


INSERT INTO CLUB (NAME) VALUES('TC Wiesing')
INSERT INTO CLUB (NAME) VALUES('TC Vomp')


INSERT INTO traininggroup (ID, NUM_REMAINING_SESSIONS, TRAINER_USERNAME) VALUES(1, 18, 'johndoe')
INSERT INTO traininggroup (ID, NUM_REMAINING_SESSIONS, TRAINER_USERNAME) VALUES(2, 18, 'johndoe')


INSERT INTO TRAININGGROUP_PLAYERS (TRAINING_GROUP_ID, PLAYERS_USERNAME) VALUES(1, 'user1')
INSERT INTO TRAININGGROUP_PLAYERS (TRAINING_GROUP_ID, PLAYERS_USERNAME) VALUES(1, 'user2')


INSERT INTO TRAINING (ID, BULLET_POINTS, COMMENT, COURT, DURATION_MINUTES, START_TIME, CLUB_NAME, TRAINING_GROUP_ID) VALUES(1, 'some bullet points', 'some comments', 'Court2', 18, '2022-01-03 18:30', 'TC Wiesing', 1)
INSERT INTO TRAINING (ID, BULLET_POINTS, COMMENT, COURT, DURATION_MINUTES, START_TIME, CLUB_NAME, TRAINING_GROUP_ID) VALUES(2, 'some bullet points', 'some comments', 'Court2', 15, '2022-01-01 13:30', 'TC Vomp', 1)
INSERT INTO TRAINING (ID, BULLET_POINTS, COMMENT, COURT, DURATION_MINUTES, START_TIME, CLUB_NAME, TRAINING_GROUP_ID) VALUES(3, 'some bullet points', 'some comments', 'Court2', 15, '2022-01-03 13:30', 'TC Vomp', 2)
