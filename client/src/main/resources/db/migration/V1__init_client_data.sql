
-- =========================================================================
-- 1. CREATION DE LA TABLE DES ADRESSES
-- =========================================================================
CREATE TABLE IF NOT EXISTS `t_adresse` (
                                           `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           `code_postal` VARCHAR(20) NOT NULL,
    `localite` VARCHAR(100) NOT NULL,
    `numero` VARCHAR(20),
    `rue` VARCHAR(255) NOT NULL,
    `pays` VARCHAR(100) DEFAULT 'France'
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table `t_adresse`
INSERT INTO `t_adresse` (`id`, `code_postal`, `localite`, `numero`, `rue`, `pays`)
VALUES (1, '92500', 'Rueil-Malmaison', '13', 'Domaine de la côte noire', 'France'),
       (2, '92500', 'Rueil-Malmaison', '111', 'Boulevard National', 'France'),
       (3, '75005', 'Paris', '5', 'Rue Thénard', 'France'),
       (4, '92400', 'Courbevoie', '4', 'Place des Vosges', 'France'),
       (5, '75009', 'Paris', '15', 'rue Taitbout', 'France'),
       (6, '69001', 'LYON 1ER', '21', 'Rue Algérie', 'France'),
       (7, '75012', 'Paris', '8', 'Rue Marcel Dubois', 'France');


-- =========================================================================
-- 2. CREATION DE LA TABLE DES CLIENTS
-- =========================================================================
CREATE TABLE IF NOT EXISTS `t_client` (
                                          `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          `social_reason` VARCHAR(150) NOT NULL UNIQUE,
    `adresse_id` BIGINT,
    CONSTRAINT `fk_client_adresse` FOREIGN KEY (`adresse_id`) REFERENCES `t_adresse` (`id`) ON DELETE SET NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insertion dans t_client
INSERT INTO `t_client` (`id`, `social_reason`, `adresse_id`)
VALUES (1, 'Odyssey Consulting', 1),
       (2, 'Emagine Consulting', 2),
       (3, 'Easy Partner', 3),
       (4, 'Osircom', 4),
       (5, 'Osiam', 5);


-- =========================================================================
-- 3. CREATION DE LA TABLE DES EMAILS CLIENTS
-- =========================================================================
CREATE TABLE IF NOT EXISTS `t_email_client` (
                                                `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                `email` VARCHAR(100) NOT NULL,
    `client_id` BIGINT NOT NULL,
    CONSTRAINT `fk_email_client` FOREIGN KEY (`client_id`) REFERENCES `t_client` (`id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table `t_email_client`
INSERT IGNORE INTO `t_email_client` (`email`, `client_id`)
VALUES ('odyssey.consulting@odyssey.com', 1),
       ('emagine.consulting@emagine.com', 2),
       ('easy.partner@easy-partner.fr', 3),
       ('moustapha.aliane@gmail.com', 4),
       ('allouchi@hotmail.fr', 4),
       ('allouchi@hotmail.fr', 5);