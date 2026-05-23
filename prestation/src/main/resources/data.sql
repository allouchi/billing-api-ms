--
-- Dumping data for table `t_prestation`
--
INSERT INTO t_prestation (client_prestation,
                          delai_paiement,
                          designation,
                          numero_commande,
                          tarifht,
                          date_debut,
                          date_fin,
                          client_id,
                          consultant_id,
                          company_id,
                          siret)
VALUES ('Odyssey Consulting', 30, 'La Prestation est réalisée pour le compte de', 'xxxxxxxxxxxxxxxxxx', 470,
        '01/03/2021', '30/06/2021', 1, 1, 1, '85292702900011'),
       ('Accor Hotels', 30, 'La Prestation est réalisée pour le compte de', 'N°13.21.19.05.14.01', 490, '08/07/2021',
        '31/12/2021', 2, 1, 1, '85292702900011'),
       ('Accor Hotels', 30, 'La Prestation est réalisée pour le compte de', 'N° 13.21.19.05.14.01', 510, '01/01/2022',
        '30/09/2022', 2, 1, 1, '85292702900011'),
       ('Ekino', 30, 'La Prestation est réalisée pour le compte de', 'N° 2022.11.07.00186', 500, '09/11/2022',
        '31/12/2022', 3, 1, 1, '85292702900011'),
       ('CS Group', 60, 'La Prestation est réalisée pour le compte de ', 'N° CS202305', 510, '20/02/2023', '31/12/2026',
        4, 1, 1, '85292702900011');


