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


--
-- Dumping data for table `t_company`
--
INSERT INTO t_company (code_ape,
                       numero_bic,
                       numero_iban,
                       numero_tva,
                       rcsname,
                       siret,
                       social_reason,
                       status,
                       adresse_id,
                       checked)
VALUES ('6201Z', 'PSSTFRPPSCE', 'FR1720041010125407961J03367', 'FR18831502141',
        'R.C.S. Nanterre 831 502 141', '85292702900011',
        'SBATEC Consulting', 'SASU au capital de 500 Euros', 1, 1),

       ('6201Z', 'CRLYFRPP', 'FR3330002008970000005896J14', 'FR18831502141',
        'R.C.S. Nanterre 831 502 141', '83150214100011',
        'ALIATECK', 'SASU au capital de 500 Euros', 2, 0);