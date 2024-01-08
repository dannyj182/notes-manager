Good morning, this repository has the Frontend and Backend applications that together form a system to manage notes.

In the Backend folder both projects are already together, you only have to download the repository and implement a MySQL database.

DB = test
PORT = 3306

This is an example configuration:

jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC

When you run the backend, spring will generate the tables, but first you have to create the test database.

-- Create DB test
CREATE DATABASE test;

-- Insert records in the status table
INSERT INTO status (status_id, status) VALUES (1, 'active');
INSERT INTO status (status_id, status) VALUES (2, 'archived');
INSERT INTO status (status_id, status) VALUES (3, 'deleted');

-- Insert records in the user table
INSERT INTO user (last_name, name, password, user_name) VALUES ('Jimenez', 'Danny', '$2y$10$Iwbq5hnfyO1ZI4dTCI/GPeHiLC8gkvgXal/UGeVDG2UgEG5n/69NS', 'dannyj182');
INSERT INTO user (last_name, name, password, user_name) VALUES ('Molina', 'Daniela', '$2y$10$S3ATL2qUs2yx6gos7oa20OXVsovlaM4YJjmc/vTBVZqMrCxmLiWp6', 'daniela');

User: dannyj182
Password: Danny123

User: daniela
Password: Daniela1990

The app has a login that will be authenticated with Spring Security using JWT, phases 1 and 2 have been completed.

I'm sorry I didn't meet a better README and Docker, I tried my best but I didn't have enough time to do it.

I hope you like the solution.