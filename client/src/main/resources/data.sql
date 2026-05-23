--
-- Dumping data for table `t_adresse`
--
INSERT INTO t_adresse (code_postal, localite, numero, rue, pays)
VALUES ('92500', 'Rueil-Malmaison', '13', 'Domaine de la côte noire', 'France'),
       ('92500', 'Rueil-Malmaison', '111', 'Boulevard National', 'France'),
       ('75005', 'Paris', '5', 'Rue Thénard', 'France'),
       ('92400', 'Courbevoie', '4', 'Place des Vosges', 'France'),
       ('75009', 'Paris', '15', 'rue Taitbout', 'France'),
       ('69001', 'LYON 1ER', '21', 'Rue Algérie', 'France'),
       ('75012', 'Paris', '8', 'Rue Marcel Dubois', 'France');

-- Insertion dans t_client
INSERT INTO t_client (social_reason, adresse_id)
VALUES ('Odyssey Consulting', 1),
       ('Emagine Consulting', 2),
       ('Easy Partner', 3),
       ('Osircom', 4),
       ('Osiam', 5);


-- Dumping data for table `t_email_client`
--
INSERT INTO t_email_client (email, client_id)
VALUES ('odyssey.consulting@odyssey.com', 1),
       ('emagine.consulting@emagine.com', 2),
       ('easy.partner@easy-partner.fr', 3),
       ('moustapha.aliane@gmail.com', 4),
       ('allouchi@hotmail.fr', 4),
       ('allouchi@hotmail.fr', 5);

