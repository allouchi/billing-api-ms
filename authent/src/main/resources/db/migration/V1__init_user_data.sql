
-- =========================================================================
-- 1. CREATION DE LA TABLE DES ROLES
-- =========================================================================
CREATE TABLE IF NOT EXISTS `t_role` (
                                        `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        `role_name` VARCHAR(50) NOT NULL UNIQUE,
    `description` VARCHAR(255)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insertion des Roles
INSERT INTO `t_role` (`role_name`, `description`)
VALUES ('ROLE_ADMIN', 'Administrateur'),
       ('ROLE_CONSULT', 'Consultation'),
       ('ROLE_EDIT', 'Edition');


-- =========================================================================
-- 2. CREATION DE LA TABLE DES UTILISATEURS
-- =========================================================================
CREATE TABLE IF NOT EXISTS `t_user` (
                                        `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        `email` VARCHAR(100) NOT NULL UNIQUE,
    `first_name` VARCHAR(50) NOT NULL,
    `last_name` VARCHAR(50) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `siret` VARCHAR(14),
    `activated` BOOLEAN DEFAULT FALSE,
    `language` VARCHAR(5) DEFAULT 'fr'
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insertion des Users
INSERT INTO `t_user` (`email`, `first_name`, `last_name`, `password`, `siret`, `activated`, `language`)
VALUES ('allouchi@hotmail.fr', 'Mustapha', 'Aliane', '$2a$10$7XzFwbCwSWcAhVZQSF742eW2f0MZ6LOEcwRSAbOZa8bgrU9XVYK0u', '85292702900011', true, 'fr'),
       ('khalid@hotmail.fr', 'Khalid', 'Aliane', '$2a$10$7XzFwbCwSWcAhVZQSF742eW2f0MZ6LOEcwRSAbOZa8bgrU9XVYK0u', '85292702900011', false, 'en'),
       ('salma@hotmail.fr', 'Salma', 'Aliane', '$2a$10$7XzFwbCwSWcAhVZQSF742eW2f0MZ6LOEcwRSAbOZa8bgrU9XVYK0u', '85292702900011', false, 'fr'),
       ('aziza@hotmail.fr', 'Aziza', 'Aliane', '$2a$10$7XzFwbCwSWcAhVZQSF742eW2f0MZ6LOEcwRSAbOZa8bgrU9XVYK0u', '85292702900011', true, 'fr'),
       ('btissame@hotmail.fr', 'Btissame', 'Aliane', '$2a$10$7XzFwbCwSWcAhVZQSF742eW2f0MZ6LOEcwRSAbOZa8bgrU9XVYK0u', '85292702900011', false, 'fr')
    ;


-- =========================================================================
-- 3. CREATION DE LA TABLE DE JOINTURE (USER_ROLES)
-- =========================================================================
CREATE TABLE IF NOT EXISTS `user_roles` (
                                            `user_id` BIGINT NOT NULL,
                                            `role_id` BIGINT NOT NULL,
                                            PRIMARY KEY (`user_id`, `role_id`),
    CONSTRAINT `fk_user_roles_user` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_user_roles_role` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Association des Rôles (Mustapha est ADMIN et CONSULT)
INSERT INTO `user_roles` VALUES (1,1),(2,1),(1,2),(3,2),(4,2),(1,3),(3,3),(4,3);