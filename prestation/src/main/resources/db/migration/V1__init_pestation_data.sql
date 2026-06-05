
-- =========================================================================
-- 1. CREATION DE LA TABLE DES PRESTATIONS
-- =========================================================================
CREATE TABLE IF NOT EXISTS `t_prestation` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `client_prestation` VARCHAR(150) NOT NULL,
    `delai_paiement` INT DEFAULT 30,
    `designation` VARCHAR(255) NOT NULL,
    `numero_commande` VARCHAR(100),
    `tarifht` DECIMAL(10,2) NOT NULL,
    `date_debut` VARCHAR(15),
    `date_fin` VARCHAR(15),
    `client_id` BIGINT,
    `consultant_id` BIGINT,
    `company_id` BIGINT,
    `siret` VARCHAR(14)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table `t_prestation`
INSERT INTO `t_prestation` (`client_prestation`, `delai_paiement`, `designation`, `numero_commande`, `tarifht`, `date_debut`, `date_fin`, `client_id`, `consultant_id`, `company_id`, `siret`)
VALUES ('Odyssey Consulting', 30, 'La Prestation est réalisée pour le compte de', 'xxxxxxxxxxxxxxxxxx', 470.00, '2021-03-01', '2021-06-30', 1, 1, 1, '85292702900011'), -- Format date corrigé en YYYY-MM-DD
       ('Accor Hotels', 30, 'La Prestation est réalisée pour le compte de', 'N°13.21.19.05.14.01', 490.00, '2021-07-08', '2021-12-31', 2, 1, 1, '85292702900011'),
       ('Accor Hotels', 30, 'La Prestation est réalisée pour le compte de', 'N° 13.21.19.05.14.01', 510.00, '2022-01-01', '2022-09-30', 2, 1, 1, '85292702900011'),
       ('Ekino', 30, 'La Prestation est réalisée pour le compte de', 'N° 2022.11.07.00186', 500.00, '2022-11-09', '2022-12-31', 3, 1, 1, '85292702900011'),
       ('CS Group', 60, 'La Prestation est réalisée pour le compte de ', 'N° CS202305', 510.00, '2023-02-20', '2026-12-31', 4, 1, 1, '85292702900011');