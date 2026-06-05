-- 1. Création des bases de données (avec backticks pour gérer les tirets)
CREATE DATABASE IF NOT EXISTS `user-db`;
CREATE DATABASE IF NOT EXISTS `company-db`;
CREATE DATABASE IF NOT EXISTS `client-db`;
CREATE DATABASE IF NOT EXISTS `prestation-db`;
CREATE DATABASE IF NOT EXISTS `consultant-db`; -- Faute de frappe corrigée ici
CREATE DATABASE IF NOT EXISTS `facture-db`;


-- 2. Sécurité : On s'assure que l'utilisateur existe avant de lui donner des droits
CREATE USER IF NOT EXISTS 'app_user'@'%' IDENTIFIED BY 'app_pass';
-- 3. Attribution des privilèges (avec backticks également)
GRANT ALL PRIVILEGES ON `user-db`.* TO 'app_user'@'%';
GRANT ALL PRIVILEGES ON `company-db`.* TO 'app_user'@'%';
GRANT ALL PRIVILEGES ON `client-db`.* TO 'app_user'@'%';
GRANT ALL PRIVILEGES ON `consultant-db`.* TO 'app_user'@'%';
GRANT ALL PRIVILEGES ON `prestation-db`.* TO 'app_user'@'%';
GRANT ALL PRIVILEGES ON `facture-db`.* TO 'app_user'@'%';
-- 4. Application des nouveaux droits
FLUSH PRIVILEGES;