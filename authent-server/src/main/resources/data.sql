-- Roles
INSERT INTO t_role (role_name, description)
VALUES ('ROLE_ADMIN', 'Administrateur'),
       ('ROLE_CONSULT', 'Consultation'),
       ('ROLE_EDIT', 'Edition');

-- Users
INSERT INTO t_user (email, first_name, last_name, password, siret, activated, language)
VALUES ('allouchi@hotmail.fr', 'Mustapha', 'Aliane',
        '$2a$10$7XzFwbCwSWcAhVZQSF742eW2f0MZ6LOEcwRSAbOZa8bgrU9XVYK0u',
        '85292702900011', true, 'fr'),
       ('khalid@hotmail.fr', 'Khalid', 'Aliane',
        '$2a$10$7XzFwbCwSWcAhVZQSF742eW2f0MZ6LOEcwRSAbOZa8bgrU9XVYK0u',
        '85292702900011', false, 'en'),
       ('salma@hotmail.fr', 'Salma', 'Aliane',
        '$2a$10$7XzFwbCwSWcAhVZQSF742eW2f0MZ6LOEcwRSAbOZa8bgrU9XVYK0u',
        '85292702900011', false, 'fr'),
       ('btissame@hotmail.fr', 'Btissame', 'Aliane',
        '$2a$10$7XzFwbCwSWcAhVZQSF742eW2f0MZ6LOEcwRSAbOZa8bgrU9XVYK0u',
        '85292702900011', false, 'fr');

INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1),
       (1, 2);