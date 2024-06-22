-- Teams
INSERT INTO team (id, location, name)
VALUES (3001, 'Pittsburgh', 'Penguins');

INSERT INTO team (id, location, name)
VALUES (3002, 'New York', 'Rangers');

INSERT INTO team (id, location, name)
VALUES (3003, 'Philadelphia', 'Flyers');


-- Players
INSERT INTO player (id, first_name, last_name)
VALUES (2001, 'Jaromir', 'Jagr');

INSERT INTO player (id, first_name, last_name)
VALUES (2002, 'Mario', 'Lemieux');

INSERT INTO player (id, first_name, last_name)
VALUES (2003, 'Wayne', 'Gretzky');

INSERT INTO player (id, first_name, last_name)
VALUES (2004, 'Eric', 'Lindros');


-- Games
INSERT INTO game (id, season, home_team_id, away_team_id, home_goals, away_goals)
VALUES (1001, '19981999', 3001, 3002, 5, 2);

INSERT INTO game (id, season, home_team_id, away_team_id, home_goals, away_goals)
VALUES (1002, '19981999', 3003, 3001, 3, 6);

INSERT INTO game (id, season, home_team_id, away_team_id, home_goals, away_goals)
VALUES (1003, '19981999', 3002, 3003, 3, 2);

INSERT INTO game (id, season, home_team_id, away_team_id, home_goals, away_goals)
VALUES (1004, '20112012', 3001, 3003, 4, 2);


-- Team statistics in games
INSERT INTO game_team_stats (game_id, team_id, head_coach, won)
VALUES (1001, 3001, 'Mike Sullivan', true);

INSERT INTO game_team_stats (game_id, team_id, head_coach, won)
VALUES (1001, 3002, 'John Tortorella', false);

INSERT INTO game_team_stats (game_id, team_id, head_coach, won)
VALUES (1002, 3001, 'Mike Sullivan', true);

INSERT INTO game_team_stats (game_id, team_id, head_coach, won)
VALUES (1002, 3003, 'Peter Laviolette', false);

INSERT INTO game_team_stats (game_id, team_id, head_coach, won)
VALUES (1003, 3002, 'John Tortorella', true);

INSERT INTO game_team_stats (game_id, team_id, head_coach, won)
VALUES (1003, 3003, 'Peter Laviolette', false);

INSERT INTO game_team_stats (game_id, team_id, head_coach, won)
VALUES (1004, 3001, 'Mike Sullivan', true);

INSERT INTO game_team_stats (game_id, team_id, head_coach, won)
VALUES (1004, 3003, 'Peter Laviolette', false);


-- Players statistics in games
INSERT INTO game_skater_stats (game_id, team_id, player_id, goals, assists, face_off_wins, face_offs_taken)
VALUES (1001, 3001, 2001, 4, 1, 10, 15);

INSERT INTO game_skater_stats (game_id, team_id, player_id, goals, assists, face_off_wins, face_offs_taken)
VALUES (1001, 3001, 2002, 1, 3, 5, 10);

INSERT INTO game_skater_stats (game_id, team_id, player_id, goals, assists, face_off_wins, face_offs_taken)
VALUES (1001, 3002, 2003, 2, 0, 2, 3);

INSERT INTO game_skater_stats (game_id, team_id, player_id, goals, assists, face_off_wins, face_offs_taken)
VALUES (1002, 3001, 2001, 1, 5, 4, 5);

INSERT INTO game_skater_stats (game_id, team_id, player_id, goals, assists, face_off_wins, face_offs_taken)
VALUES (1002, 3003, 2004, 2, 1, 1, 4);

INSERT INTO game_skater_stats (game_id, team_id, player_id, goals, assists, face_off_wins, face_offs_taken)
VALUES (1003, 3002, 2003, 1, 1, 2, 10);

INSERT INTO game_skater_stats (game_id, team_id, player_id, goals, assists, face_off_wins, face_offs_taken)
VALUES (1003, 3003, 2004, 0, 1, 3, 5);

INSERT INTO game_skater_stats (game_id, team_id, player_id, goals, assists, face_off_wins, face_offs_taken)
VALUES (1004, 3001, 2001, 2, 1, 5, 10);

