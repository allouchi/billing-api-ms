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
       (7, '75012', 'Paris', '8', 'Rue Marcel Dubois', 'France'); -- <-- FIX : Point-virgule indispensable pour Flyway !

-- =========================================================================
-- 2. CREATION DE LA TABLE DES ENTREPRISES (COMPANIES)
-- =========================================================================
CREATE TABLE IF NOT EXISTS `t_company` (
                                           `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           `code_ape` VARCHAR(10),
    `numero_bic` VARCHAR(20),
    `numero_iban` VARCHAR(34),
    `numero_tva` VARCHAR(20),
    `rcsname` VARCHAR(100),
    `siret` VARCHAR(14) NOT NULL UNIQUE,
    `social_reason` VARCHAR(150) NOT NULL,
    `status` VARCHAR(100),
    `adresse_id` BIGINT,
    `checked` BOOLEAN DEFAULT FALSE,
    CONSTRAINT `fk_company_adresse` FOREIGN KEY (`adresse_id`) REFERENCES `t_adresse` (`id`) ON DELETE SET NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table `t_company`
INSERT INTO `t_company` (`id`, `code_ape`, `numero_bic`, `numero_iban`, `numero_tva`, `rcsname`, `siret`, `social_reason`, `status`, `adresse_id`, `checked`)
VALUES (1, '6201Z', 'PSSTFRPPSCE', 'FR1720041010125407961J03367', 'FR18831502141', 'R.C.S. Nanterre 831 502 141', '85292702900011', 'SBATEC Consulting', 'SASU au capital de 500 Euros', 1, 1),
       (2, '6201Z', 'CRLYFRPP', 'FR3330002008970000005896J14', 'FR18831502141', 'R.C.S. Nanterre 831 502 141', '83150214100011', 'ALIATECK', 'SASU au capital de 500 Euros', 2, 0); -- <-- FIX : Fin propre sans parenthèse parasite