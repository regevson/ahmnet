INSERT INTO club (ID) VALUES('TC-Wiesing')
    on duplicate key update id = id;
INSERT INTO club (ID) VALUES('TC-Vomp')
    on duplicate key update id = id;
INSERT INTO club (ID) VALUES('TC-Terfens-Vomperbach')
    on duplicate key update id = id;
INSERT INTO club (ID) VALUES('TC-Breitenbach')
    on duplicate key update id = id;
INSERT INTO club (ID) VALUES('TC-Schwaz')
    on duplicate key update id = id;
INSERT INTO club (ID) VALUES('TC-Weerberg')
    on duplicate key update id = id;



INSERT INTO user (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, CLUB_ID, PHONE, BIRTH_YEAR) VALUES(TRUE, 'Daniel', 'Huber', 'passwd', 'admin', 'admin', '2016-01-01 00:00:00', 'TC-Schwaz', '+4369910983630', 1968)
    on duplicate key update username = username;
INSERT INTO user_user_role (USER_USERNAME, ROLES) VALUES ('admin', 'ADMIN')
    on duplicate key update user_username = user_username;
INSERT INTO user_user_role (USER_USERNAME, ROLES) VALUES ('admin', 'TRAINER')
    on duplicate key update user_username = user_username;

INSERT INTO user (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, CLUB_ID, PHONE, BIRTH_YEAR) VALUES(TRUE, 'Juwal', 'Regev', 'passwd', 'jure', 'jure', '2016-01-01 00:00:00', 'TC-Breitenbach', '+4369910983630', 2000)
    on duplicate key update username = username;
INSERT INTO user_user_role (USER_USERNAME, ROLES) VALUES ('jure', 'TRAINER')
    on duplicate key update user_username = user_username;

INSERT INTO user (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, CLUB_ID, PHONE, BIRTH_YEAR) VALUES(TRUE, 'Matan', 'Regev', 'passwd', 'mare', 'mare', '2016-01-01 00:00:00', 'TC-Breitenbach', '+4369910983630', 2000)
    on duplicate key update username = username;
INSERT INTO user_user_role (USER_USERNAME, ROLES) VALUES ('mare', 'TRAINER')
    on duplicate key update user_username = user_username;

INSERT INTO user (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, CLUB_ID, PHONE, BIRTH_YEAR) VALUES(TRUE, 'John', 'Doe', 'passwd', 'johndoe', 'admin', '2016-01-01 00:00:00', 'TC-Vomp', '+4369910983630', 2000)
    on duplicate key update username = username;
INSERT INTO user_user_role (USER_USERNAME, ROLES) VALUES ('johndoe', 'TRAINER')
    on duplicate key update user_username = user_username;

INSERT INTO user (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, CLUB_ID, BIRTH_YEAR) VALUES(TRUE, 'Susi', 'Kaufgern', 'passwd', 'susi', 'admin', '2016-01-01 00:00:00', 'TC-Vomp', 2014)
    on duplicate key update username = username;
INSERT INTO user_user_role (USER_USERNAME, ROLES) VALUES ('susi', 'PLAYER')
    on duplicate key update user_username = user_username;

INSERT INTO user (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE, CLUB_ID, BIRTH_YEAR) VALUES(TRUE, 'Max', 'Mustermann', 'passwd', 'max', 'admin', '2016-01-01 00:00:00', 'TC-Breitenbach', 2012)
    on duplicate key update username = username;
INSERT INTO user_user_role (USER_USERNAME, ROLES) VALUES ('max', 'PLAYER')
    on duplicate key update user_username = user_username;




INSERT INTO traininggroup (ID, TRAINER_USERNAME, CLUB_ID) VALUES(1, 'johndoe', 'TC-Wiesing')
    on duplicate key update id = id;
INSERT INTO traininggroup (ID, TRAINER_USERNAME, CLUB_ID) VALUES(2, 'admin', 'TC-Vomp')
    on duplicate key update id = id;
INSERT INTO traininggroup (ID, TRAINER_USERNAME, CLUB_ID) VALUES(3, 'johndoe', 'TC-Wiesing')
    on duplicate key update id = id;

INSERT INTO ball_color (TRAINING_GROUP_ID, BALL_COLORS) VALUES(1, 'ORANGE')
    on duplicate key update training_group_id = training_group_id;
INSERT INTO ball_color (TRAINING_GROUP_ID, BALL_COLORS) VALUES(1, 'GREEN')
    on duplicate key update training_group_id = training_group_id;

INSERT INTO traininggroup_players (TRAINING_GROUPS_ID, PLAYERS_USERNAME) VALUES(2, 'susi')
    on duplicate key update training_groups_id = training_groups_id;
INSERT INTO traininggroup_players (TRAINING_GROUPS_ID, PLAYERS_USERNAME) VALUES(1, 'max')
    on duplicate key update training_groups_id = training_groups_id;
INSERT INTO traininggroup_players (TRAINING_GROUPS_ID, PLAYERS_USERNAME) VALUES(2, 'max')
    on duplicate key update training_groups_id = training_groups_id;
INSERT INTO traininggroup_players (TRAINING_GROUPS_ID, PLAYERS_USERNAME) VALUES(3, 'max')
    on duplicate key update training_groups_id = training_groups_id;



INSERT INTO training (ID, TRAINER_USERNAME, PREV_TRAINER_USERNAME, BULLET_POINTS, COMMENTS, COURT, CLUB_ID, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(100, 'johndoe', 'johndoe', 'some bullet points', 'some comments', 1, 'TC-Wiesing', 60, '2022-07-04 18:30', 27, 1)
    on duplicate key update id = id;
INSERT INTO training (ID, TRAINER_USERNAME, PREV_TRAINER_USERNAME, BULLET_POINTS, COMMENTS, COURT, CLUB_ID, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(1, 'johndoe', 'johndoe', 'some bullet points', 'some comments', 2, 'TC-Wiesing', 120, '2022-07-04 14:30', 27, 1)
    on duplicate key update id = id;
INSERT INTO training (ID, TRAINER_USERNAME, PREV_TRAINER_USERNAME, BULLET_POINTS, COMMENTS, COURT, CLUB_ID, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(2, 'johndoe', 'admin', 'some bullet points', 'some comments', 3, 'TC-Schwaz', 60, '2022-07-05 13:30', 27, 2)
    on duplicate key update id = id;

INSERT INTO training (ID, TRAINER_USERNAME, PREV_TRAINER_USERNAME, BULLET_POINTS, COMMENTS, COURT, CLUB_ID, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(3, 'admin', 'admin', 'some bullet points', 'some comments', 3, 'TC-Schwaz', 60, '2022-07-05 13:30', 15, 2)
    on duplicate key update id = id;
INSERT INTO training (ID, TRAINER_USERNAME, PREV_TRAINER_USERNAME, BULLET_POINTS, COMMENTS, COURT, CLUB_ID, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(4, 'admin', 'admin', 'some bullet points', 'some comments', 3, 'TC-Schwaz', 60, '2022-07-06 13:30', 27, 2)
    on duplicate key update id = id;
INSERT INTO training (ID, TRAINER_USERNAME, PREV_TRAINER_USERNAME, BULLET_POINTS, COMMENTS, COURT, CLUB_ID, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(5, 'johndoe', 'johndoe', 'some bullet points', 'some comments', 3, 'TC-Schwaz', 60, '2022-07-04 14:30', 27, 2)
    on duplicate key update id = id;
INSERT INTO training (ID, TRAINER_USERNAME, PREV_TRAINER_USERNAME, BULLET_POINTS, COMMENTS, COURT, CLUB_ID, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(6, 'admin', 'admin', 'some bullet points', 'some comments', 3, 'TC-Schwaz', 60, '2022-07-07 13:30', 27, 2)
    on duplicate key update id = id;
INSERT INTO training (ID, TRAINER_USERNAME, PREV_TRAINER_USERNAME, BULLET_POINTS, COMMENTS, COURT, CLUB_ID, DURATION_MINUTES, DATE_TIME, WEEK_NUM, IS_FREE, TRAINING_GROUP_ID) VALUES(7, 'johndoe', 'johndoe', 'some bullet points', 'some comments', 3, 'TC-Schwaz', 60, '2022-04-08 13:30', 27, true, 2)
    on duplicate key update id = id;
INSERT INTO training (ID, TRAINER_USERNAME, PREV_TRAINER_USERNAME, BULLET_POINTS, COMMENTS, COURT, CLUB_ID, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(8, 'admin', 'johndoe', 'some bullet points', 'some comments', 3, 'TC-Schwaz', 60, '2022-07-09 13:30', 27, 3)
    on duplicate key update id = id;
INSERT INTO training (ID, TRAINER_USERNAME, PREV_TRAINER_USERNAME, BULLET_POINTS, COMMENTS, COURT, CLUB_ID, DURATION_MINUTES, DATE_TIME, WEEK_NUM, TRAINING_GROUP_ID) VALUES(9, 'johndoe', 'johndoe', 'some bullet points', 'some comments', 3, 'TC-Schwaz', 60, '2022-07-08 13:30', 27, 3)
    on duplicate key update id = id;
INSERT INTO training (ID, TRAINER_USERNAME, PREV_TRAINER_USERNAME, BULLET_POINTS, COMMENTS, COURT, CLUB_ID, DURATION_MINUTES, DATE_TIME, WEEK_NUM, IS_FREE, TRAINING_GROUP_ID) VALUES(10, 'admin', 'admin', 'some bullet points', 'some comments', 3, 'TC-Schwaz', 60, '2022-07-07 13:30', 15, true, 3)
    on duplicate key update id = id;

INSERT INTO training_attendees (TRAINING_ID, ATTENDEES_USERNAME) VALUES(100, 'max')
    on duplicate key update training_id = training_id;
INSERT INTO training_attendees (TRAINING_ID, ATTENDEES_USERNAME) VALUES(1, 'max')
    on duplicate key update training_id = training_id;
INSERT INTO training_attendees (TRAINING_ID, ATTENDEES_USERNAME) VALUES(2, 'susi')
    on duplicate key update training_id = training_id;
