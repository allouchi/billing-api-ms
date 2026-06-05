
-- =========================================================================
-- 1. CREATION DE LA TABLE DES CONSULTANTS
-- =========================================================================
CREATE TABLE IF NOT EXISTS `t_consultant` (
                                              `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                                              `last_name` VARCHAR(50) NOT NULL,
    `first_name` VARCHAR(50) NOT NULL,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `fonction` VARCHAR(150)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table `t_consultant`
INSERT INTO `t_consultant` (`id`, `last_name`, `first_name`, `email`, `fonction`)
VALUES (1, 'ALIANE', 'Mustapha', 'mustapha.aliane@free.fr', 'Développeur FullStack JEE/Angular'),
       (2, 'ALIANE', 'Khalid', 'khalid@hotmail.fr', 'Développeur Fullstack JAVA/JEE/React');