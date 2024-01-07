INSERT INTO users (id, enabled, email, password, role, username, level, points, school, created_at, updated_at)
VALUES ('123e4567-e89b-12d3-a456-426655440000', false, 'admn4testing1234+asd@gmail.com',
        '$2a$12$os/7cWt2mtb0n3FdahZyOePi1w
.xICQ/uXDkLrL/x3bALQg/dS5oe', 'ADMIN',
        'imanadmin', 20, 20000, 'BME', now(), now());
INSERT INTO users (id, enabled, email, password, role, username, level, points, school, created_at, updated_at)
VALUES ('123e4567-e89b-12d3-a456-426655440055', true, 'admn4testing1234+random@gmail.com',
        '$2a$12$os/7cWt2mtb0n3FdahZyOePi1w.xICQ/uXDkLrL/x3bALQg/dS5oe', 'ADMIN',
        'imanadmin2', 10, 5000, 'ELTE', now(), now());

INSERT INTO posts (id, title, content, user_id)
VALUES ('123e4567-e89b-12d3-a456-426655440001', 'Test post 1', 'Test ' ||
                                                               'post ' ||
                                                               '1 content', '123e4567-e89b-12d3-a456-426655440055');