-- S3.1.
-- Produceer een overzicht van alle cursusuitvoeringen; geef de
-- code, de begindatum, de lengte en de naam van de docent.
-- DROP VIEW IF EXISTS s3_1; CREATE OR REPLACE VIEW s3_1 AS                                                     -- [TEST]
SELECT code, begindatum, lengte, naam
FROM uitvoeringen
         JOIN cursussen
              ON uitvoeringen.cursus = cursussen.code
         JOIN medewerkers
              ON uitvoeringen.docent = medewerkers.mnr;

-- S3.2.
-- Geef in twee kolommen naast elkaar de achternaam van elke cursist (`cursist`)
-- van alle S02-cursussen, met de achternaam van zijn cursusdocent (`docent`).
-- DROP VIEW IF EXISTS s3_2; CREATE OR REPLACE VIEW s3_2 AS                                                     -- [TEST]
SELECT cursisten.naam as cursist, docenten.naam as docent
FROM inschrijvingen
JOIN medewerkers as cursisten
ON inschrijvingen.cursist = cursisten.mnr
JOIN uitvoeringen ON inschrijvingen.cursus = uitvoeringen.cursus
AND inschrijvingen.begindatum = uitvoeringen.begindatum
JOIN medewerkers as docenten
ON uitvoeringen.docent = docenten.mnr
WHERE uitvoeringen.cursus = 'S02';

-- S3.3.
-- Geef elke afdeling (`afdeling`) met de naam van het hoofd van die
-- afdeling (`hoofd`).
DROP VIEW IF EXISTS s3_3; CREATE OR REPLACE VIEW s3_3 AS
SELECT afdelingen.naam as afdeling, medewerkers.naam as hoofd
FROM afdelingen JOIN medewerkers ON medewerkers.mnr = afdelingen.hoofd;
-- [TEST]


-- S3.4.
-- Geef de namen van alle medewerkers, de naam van hun afdeling (`afdeling`)
-- en de bijbehorende locatie.
DROP VIEW IF EXISTS s3_4; CREATE OR REPLACE VIEW s3_4 AS                                                     -- [TEST]
SELECT medewerkers.naam as naam, afdelingen.naam as afdeling, afdelingen.locatie as locatie
from medewerkers
         JOIN afdelingen ON afdelingen.anr = medewerkers.afd

-- S3.5.
-- Geef de namen van alle cursisten die staan ingeschreven voor de cursus S02 van 12 april 2019
DROP VIEW IF EXISTS s3_5; CREATE OR REPLACE VIEW s3_5 AS                                                     -- [TEST]
SELECT medewerkers.naam
FROM inschrijvingen INNER JOIN medewerkers ON inschrijvingen.cursist = medewerkers.mnr
WHERE inschrijvingen.cursus = 'S02'
  AND begindatum = '2019-04-12';

-- S3.6.
-- Geef de namen van alle medewerkers en hun toelage.
DROP VIEW IF EXISTS s3_6; CREATE OR REPLACE VIEW s3_6 AS                                                     -- [TEST]
SELECT medewerkers.naam, toelage
FROM medewerkers
         INNER JOIN schalen
                    ON medewerkers.maandsal >= ondergrens AND medewerkers.maandsal <= bovengrens;