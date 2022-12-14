-- S5.1.
-- Welke medewerkers hebben zowel de Java als de XML cursus
-- gevolgd? Geef hun personeelsnummers.
DROP VIEW IF EXISTS s5_1; CREATE OR REPLACE VIEW s5_1 AS                                                     -- [TEST]
SELECT mnr FROM medewerkers where mnr IN (SELECT cursist FROM inschrijvingen WHERE cursus = 'JAV') AND mnr IN (SELECT cursist FROM inschrijvingen  WHERE cursus = 'XML');
-- S5.2.
-- Geef de nummers van alle medewerkers die niet aan de afdeling 'OPLEIDINGEN'
-- zijn verbonden.
DROP VIEW IF EXISTS s5_2; CREATE OR REPLACE VIEW s5_2 AS                                                     -- [TEST]
SELECT mnr FROM medewerkers WHERE afd NOT IN (SELECT anr FROM afdelingen WHERE naam = 'OPLEIDINGEN');
-- S5.3.
-- Geef de nummers van alle medewerkers die de Java-cursus niet hebben
-- gevolgd.
DROP VIEW IF EXISTS s5_3; CREATE OR REPLACE VIEW s5_3 AS                                                     -- [TEST]
SELECT mnr FROM medewerkers where mnr NOT IN (SELECT cursist FROM inschrijvingen WHERE cursus = 'JAV');
-- S5.4.
-- a. Welke medewerkers hebben ondergeschikten? Geef hun naam.
DROP VIEW IF EXISTS s5_4a; CREATE OR REPLACE VIEW s5_4a AS
SELECT naam FROM medewerkers WHERE mnr in (SELECT chef from medewerkers);

-- b. En welke medewerkers hebben geen ondergeschikten? Geef wederom de naam.
DROP VIEW IF EXISTS s5_4b; CREATE OR REPLACE VIEW s5_4b AS
SELECT naam FROM medewerkers WHERE mnr not in (SELECT chef from medewerkers  where chef is not null);


-- S5.5.
-- Geef cursuscode en begindatum van alle uitvoeringen van programmeercursussen
-- ('BLD') in 2020.
DROP VIEW IF EXISTS s5_5; CREATE OR REPLACE VIEW s5_5 AS                                                     -- [TEST]
SELECT cursus, begindatum FROM uitvoeringen WHERE cursus IN (SELECT code FROM cursussen WHERE type = 'BLD') AND begindatum > '2020-01-01' AND begindatum < '2020-12-31';

-- S5.6.
-- Geef van alle cursusuitvoeringen: de cursuscode, de begindatum en het
-- aantal inschrijvingen (`aantal_inschrijvingen`). Sorteer op begindatum.
DROP VIEW IF EXISTS s5_6; CREATE OR REPLACE VIEW s5_6 AS                                                     -- [TEST]
SELECT cursus, begindatum,(SELECT DISTINCT count(cursist) FROM inschrijvingen WHERE uitvoeringen.cursus = inschrijvingen.cursus AND uitvoeringen.begindatum = inschrijvingen.begindatum) FROM uitvoeringen ORDER BY begindatum ASC;

-- S5.7.
-- Geef voorletter(s) en achternaam van alle trainers die ooit tijdens een
-- algemene ('ALG') cursus hun eigen chef als cursist hebben gehad.
DROP VIEW IF EXISTS s5_7; CREATE OR REPLACE VIEW s5_7 AS                                                     -- [TEST]
SELECT med.voorl, med.naam
FROM medewerkers med
WHERE med.functie = 'TRAINER' AND exists
(SELECT cursist FROM inschrijvingen i, uitvoeringen u
WHERE i.begindatum = u.begindatum
AND i.cursus = u.cursus
AND u.docent = med.mnr
AND i.cursist = med.chef
AND u.cursus IN (SELECT c.code FROM cursussen c WHERE type = 'ALG'));

-- S5.8.
-- Geef de naam van de medewerkers die nog nooit een cursus hebben gegeven.
DROP VIEW IF EXISTS s5_8; CREATE OR REPLACE VIEW s5_8 AS                                                     -- [TEST]
SELECT naam FROM medewerkers WHERE NOT mnr IN (SELECT docent FROM uitvoeringen WHERE docent IS NOT NULL);
