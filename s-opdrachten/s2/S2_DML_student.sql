-- ------------------------------------------------------------------------
-- Data & Persistency
-- Opdracht S2: Data Manipulation Language
--
-- (c) 2020 Hogeschool Utrecht
-- Tijmen Muller (tijmen.muller@hu.nl)
-- André Donk (andre.donk@hu.nl)
--
--
-- Opdracht: schrijf SQL-queries om onderstaande resultaten op te vragen,
-- aan te maken, verwijderen of aan te passen in de database van de
-- bedrijfscasus.
--
-- Codeer je uitwerking onder de regel 'DROP VIEW ...' (bij een SELECT)
-- of boven de regel 'ON CONFLICT DO NOTHING;' (bij een INSERT)
-- Je kunt deze eigen query selecteren en los uitvoeren, en wijzigen tot
-- je tevreden bent.
--
-- Vervolgens kun je je uitwerkingen testen door de testregels
-- (met [TEST] erachter) te activeren (haal hiervoor de commentaartekens
-- weg) en vervolgens het hele bestand uit te voeren. Hiervoor moet je de
-- testsuite in de database hebben geladen (bedrijf_postgresql_test.sql).
-- NB: niet alle opdrachten hebben testregels.
--
-- Lever je werk pas in op Canvas als alle tests slagen. Draai daarna
-- alle wijzigingen in de database terug met de queries helemaal onderaan.
-- ------------------------------------------------------------------------


-- S2.1. Vier-daagse cursussen
--
-- Geef code en omschrijving van alle cursussen die precies vier dagen duren.
DROP VIEW IF EXISTS s2_1;
CREATE
OR REPLACE VIEW s2_1 AS                                                -- [TEST]
select code, omschrijving
from cursussen
where lengte = 4;

-- S2.2. Medewerkersoverzicht
--
-- Geef alle informatie van alle medewerkers, gesorteerd op functie,
-- en per functie op leeftijd (van jong naar oud).

DROP VIEW IF EXISTS s2_2;
CREATE
OR REPLACE VIEW s2_2 AS                                                     -- [TEST]
SELECT *
FROM medewerkers
ORDER BY functie ASC, gbdatum DESC;
-- S2.3. Door het land
--
-- Welke cursussen zijn in Utrecht en/of in Maastricht uitgevoerd? Geef
-- code en begindatum.
DROP VIEW IF EXISTS s2_3;
CREATE
OR REPLACE VIEW s2_3 AS                                                     -- [TEST]
SELECT cursus, begindatum
FROM uitvoeringen
WHERE locatie IN ('UTRECHT', 'MAASTRICHT');
-- S2.4. Namen
--
-- Geef de naam en voorletters van alle medewerkers, behalve van R. Jansen.
DROP VIEW IF EXISTS s2_4;
CREATE
OR REPLACE VIEW s2_4 AS                                                     -- [TEST]
SELECT naam, voorl
FROM medewerkers
WHERE NOT (naam = 'JANSEN' AND voorl = 'R');

-- S2.5. Nieuwe SQL-cursus
--
-- Er wordt een nieuwe uitvoering gepland voor cursus S02, en wel op de
-- komende 2 maart. De cursus wordt gegeven in Leerdam door Nick Smit.
-- Voeg deze gegevens toe.


INSERT INTO uitvoeringen(cursus, begindatum, docent, locatie)
VALUES ('S02', '2022-03-02', (SELECT mnr FROM medewerkers WHERE voorl = 'N' AND naam = 'SMIT'),
        'IJsselstein') ON CONFLICT DO NOTHING;
-- [TEST]

-- S2.6. Stagiairs
--
-- Neem één van je collega-studenten aan als stagiair ('STAGIAIR') en
-- voer zijn of haar gegevens in. Kies een personeelnummer boven de 8000.
INSERT INTO medewerkers(mnr, naam, voorl, functie, chef, gbdatum, maandsal, comm, afd)
VALUES (8001, 'GLANSDORP', 'H', 'STAGIAIR', 7839, '2001-03-30', 0, 0, 20) ON CONFLICT DO NOTHING;
-- [TEST]


-- S2.7. Nieuwe schaal
--
-- We breiden het salarissysteem uit naar zes schalen. Voer een extra schaal in voor mensen die
-- tussen de 3001 en 4000 euro verdienen. Zij krijgen een toelage van 500 euro.
-- table schalen
INSERT INTO schalen(snr, ondergrens, bovengrens, toelage)
VALUES (6, 3001, 4000, 500) ON CONFLICT DO NOTHING;
-- [TEST]


-- S2.8. Nieuwe cursus
--
-- Er wordt een nieuwe 6-daagse cursus 'Data & Persistency' in het programma opgenomen.
-- Voeg deze cursus met code 'D&P' toe, maak twee uitvoeringen in Leerdam en schrijf drie
-- mensen in.

INSERT INTO cursussen(code, omschrijving, type, lengte)
VALUES ('D&P', 'Data & Persistency', 'ALG', 6) ON CONFLICT DO NOTHING; -- [TEST]
INSERT INTO uitvoeringen(cursus, begindatum, docent, locatie)
VALUES ('D&P', '2021-12-07', 7369, 'UTRECHT') ON CONFLICT DO NOTHING; -- [TEST]
INSERT INTO uitvoeringen(cursus, begindatum, docent, locatie)
VALUES ('D&P', '2021-06-07', 7369, 'UTRECHT') ON CONFLICT DO NOTHING; -- [TEST]
INSERT INTO inschrijvingen(cursist, cursus, begindatum, evaluatie)
VALUES (7934, 'D&P', '2021-12-07', NULL) ON CONFLICT DO NOTHING; -- [TEST]
INSERT INTO inschrijvingen(cursist, cursus, begindatum, evaluatie)
VALUES (7499, 'D&P', '2021-06-07', NULL) ON CONFLICT DO NOTHING; -- [TEST]
INSERT INTO inschrijvingen(cursist, cursus, begindatum, evaluatie)
VALUES (7566, 'D&P', '2021-12-07', NULL) ON CONFLICT DO NOTHING;
-- [TEST]

-- S2.9. Salarisverhoging
--
-- De medewerkers van de afdeling VERKOOP krijgen een salarisverhoging
-- van 5.5%, behalve de manager van de afdeling, deze krijgt namelijk meer: 7%.
-- Voer deze verhogingen door.
UPDATE medewerkers
SET maandsal = 1.055 * maandsal
WHERE afd = 30
  AND functie != 'MANAGER';
UPDATE medewerkers
SET maandsal = 1.07 * maandsal
WHERE afd = 30
  AND functie = 'MANAGER';

-- S2.10. Concurrent
--
-- Martens heeft als verkoper succes en wordt door de concurrent
-- weggekocht. Verwijder zijn gegevens.

-- Zijn collega Alders heeft ook plannen om te vertrekken. Verwijder ook zijn gegevens.
-- Waarom lukt dit (niet)?
DELETE
FROM medewerkers
WHERE naam = 'ALDERS'
  AND functie = 'VERKOPER';
-- [23503] ERROR: update or delete on table "medewerkers" violates foreign key constraint "i_cursist_fk" on table "inschrijvingen" Detail: Key (mnr)=(7499) is still referenced from table "inschrijvingen".
DELETE from medewerkers WHERE naam = 'MARTENS';


-- S2.11. Nieuwe afdeling
--
-- Je wordt hoofd van de nieuwe afdeling 'FINANCIEN' te Leerdam,
-- onder de hoede van De Koning. Kies een personeelnummer boven de 8000.
-- Zorg voor de juiste invoer van deze gegevens.
INSERT INTO afdelingen(anr, naam, locatie, hoofd)
VALUES (60, 'FINANCIEN', 'LEERDAM', NULL)
    INSERT
INTO medewerkers(mnr, naam, voorl, functie, chef, gbdatum, maandsal, comm, afd)
VALUES (8080, 'Hidde', 'H', 'ADMIN', 7839, '2001-03-30', 70000.00, null, (SELECT anr FROM afdelingen WHERE naam = 'FINANCIEN'));

INSERT INTO afdelingen(anr, naam, locatie, hoofd)
VALUES (nextval('afdeling_nmr_seq'), 'FINANCIEN', 'LEERDAM', null);

ALTER TABLE medewerkers
    ALTER COLUMN afd SET DATA TYPE numeric(5);

UPDATE medewerkers
SET afd = (SELECT anr FROM afdelingen WHERE naam = 'FINANCIEN')
WHERE mnr = 8080;
UPDATE afdelingen
SET hoofd = (SELECT mnr FROM medewerkers WHERE naam = 'Hidde')
WHERE naam = 'FINANCIEN';



-- [TEST]


-- -------------------------[ HU TESTRAAMWERK ]--------------------------------
-- Met onderstaande query kun je je code testen. Zie bovenaan dit bestand
-- voor uitleg.

SELECT *
FROM test_select('S2.1') AS resultaat
UNION
SELECT 'S2.2 wordt niet getest: geen test mogelijk.' AS resultaat
UNION
SELECT *
FROM test_select('S2.3') AS resultaat
UNION
SELECT *
FROM test_select('S2.4') AS resultaat
UNION
SELECT *
FROM test_exists('S2.5', 1) AS resultaat
UNION
SELECT *
FROM test_exists('S2.6', 1) AS resultaat
UNION
SELECT *
FROM test_exists('S2.7', 6) AS resultaat
ORDER BY resultaat;

