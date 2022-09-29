-- -------------------------[ HU TESTRAAMWERK ]--------------------------------
-- Met onderstaande query kun je je code testen. Zie bovenaan dit bestand
-- voor uitleg.

SELECT *
FROM test_select('S4.1') AS resultaat
UNION
SELECT *
FROM test_select('S4.2') AS resultaat
UNION
SELECT *
FROM test_select('S4.3') AS resultaat
UNION
SELECT *
FROM test_select('S4.4') AS resultaat
UNION
SELECT *
FROM test_select('S4.5') AS resultaat
UNION
SELECT 'S4.6 wordt niet getest: geen test mogelijk.' AS resultaat
UNION
SELECT *
FROM test_select('S4.7') AS resultaat
ORDER BY resultaat;
-- ------------------------------------------------------------------------
-- Data & Persistency - Casus 'bedrijf'
--
-- Testraamwerk SQL-opdrachten
--
-- (c) 2020 Hogeschool Utrecht
-- Tijmen Muller (tijmen.muller@hu.nl)
-- André Donk (andre.donk@hu.nl)
--
--
-- Voer dit 'testsuite'-bestand uit op de aangemaakte 'bedijf' database.
-- Daarna kun je de opdrachten (b.v. S2_CRUD_student.sql) openen in
-- pgAdmin of een andere IDE. Na het maken van de opdrachten kun je je
-- uitwerkingen (grotendeels) testen. Zie het commentaar in de opdrachten
-- voor meer informatie.
-- ------------------------------------------------------------------------

DROP FUNCTION IF EXISTS test_select(_exercise_nr text);
CREATE
OR REPLACE FUNCTION test_select(_exercise_nr text) RETURNS text AS $$
	DECLARE
_sol_name text := REPLACE(LOWER(_exercise_nr), '.', '_');
	DECLARE
_test_name text := _sol_name || '_test';
	DECLARE
_query_res text;
	DECLARE
_missing_count int := 0;
	DECLARE
_excess_count int := 0;
	DECLARE
_missing_query text := 'SELECT * FROM ' || _test_name || ' EXCEPT SELECT * FROM ' || _sol_name;
	DECLARE
_excess_query text := 'SELECT * FROM ' || _sol_name || ' EXCEPT SELECT * FROM ' || _test_name;
	DECLARE
_rows RECORD;
BEGIN
BEGIN
EXECUTE 'SELECT * FROM ' || _sol_name INTO _query_res;
IF
POSITION('heeft nog geen uitwerking' IN _query_res) <> 0
				THEN RETURN _query_res;
END IF;
EXCEPTION
			WHEN OTHERS THEN
				RAISE notice 'mislukt: %', sqlerrm;
NULL;
END;

FOR _rows IN EXECUTE _missing_query LOOP
			_missing_count := _missing_count + 1;
END LOOP;

FOR _rows IN EXECUTE _excess_query LOOP
			_excess_count := _excess_count + 1;
END LOOP;

		IF
_missing_count = 0 AND _excess_count = 0 THEN
			RETURN _exercise_nr || ' geeft de juiste resultaten!';
ELSE
			RETURN _exercise_nr || ' geeft niet de juiste resultaten: er zijn ' || _excess_count::text || ' verkeerde rijen teveel en ' || _missing_count::text || ' goede rijen ontbreken.';
END IF;
EXCEPTION
		WHEN SQLSTATE '42804' THEN
			RETURN _exercise_nr || ' is niet correct: de kolomnamen of de kolomtypen kloppen niet.';
WHEN SQLSTATE '42601' THEN
			RETURN _exercise_nr || ' is niet correct: het aantal kolommen klopt niet.';
END;
$$
LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS test_exists(_exercise_nr TEXT, _row_test BIGINT, _compare_type TEXT);
CREATE
OR REPLACE FUNCTION test_exists(_exercise_nr TEXT, _row_test BIGINT, _compare_type TEXT DEFAULT 'exact') RETURNS TEXT AS $$
	DECLARE
_sol_name TEXT := REPLACE(LOWER(_exercise_nr), '.', '_');
	DECLARE
_test_name TEXT := _sol_name || '_test';
	DECLARE
_row_count INT := 0;
BEGIN
EXECUTE FORMAT('SELECT COUNT(*) FROM %I;', _test_name) INTO _ row_count;

IF
_compare_type = 'exact' AND _row_count = _row_test THEN
            RETURN _exercise_nr || ' heeft de juiste uitwerking op de database!';
        ELSIF
_compare_type = 'maximaal' AND _row_count <= _row_test THEN
            RETURN _exercise_nr || ' heeft de juiste uitwerking op de database!';
        ELSIF
_compare_type = 'minimaal' AND _row_count >= _row_test THEN
            RETURN _exercise_nr || ' heeft de juiste uitwerking op de database!';
END IF;

        RETURN _exercise_nr
|| ' heeft niet de juiste uitwerking op de database: er moeten ' || _compare_type || ' ' || _row_test || ' rijen of kolommen zijn, maar de database heeft er ' || _row_count || '.';
END;
$$
LANGUAGE plpgsql;



DROP VIEW IF EXISTS s1_1_test;
CREATE
OR REPLACE VIEW s1_1_test AS
SELECT column_name
FROM information_schema.columns
WHERE table_schema = 'public'
  AND table_name = 'medewerkers'
  AND column_name = 'geslacht';

DROP VIEW IF EXISTS s1_2_test;
CREATE
OR REPLACE VIEW s1_2_test AS
SELECT m.naam
FROM medewerkers m
         JOIN afdelingen a ON m.mnr = a.hoofd
WHERE a.naam = 'ONDERZOEK'
  AND m.chef = 7839;

DROP VIEW IF EXISTS s1_4_test;
CREATE
OR REPLACE VIEW s1_4_test AS
SELECT column_name
FROM information_schema.columns
WHERE table_schema = 'public'
  AND table_name = 'adressen'
  AND column_name IN ('postcode', 'huisnummer', 'ingangsdatum', 'einddatum', 'telefoon', 'med_mnr');

-- Ongebruikt.
DROP VIEW IF EXISTS s1_5_test;
CREATE
OR REPLACE VIEW s1_5_test AS
SELECT naam
FROM medewerkers
WHERE mnr < 9000;



DROP VIEW IF EXISTS s2_1;
CREATE
OR REPLACE VIEW s2_1 AS SELECT 'S2.1 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s2_2;
CREATE
OR REPLACE VIEW s2_2 AS SELECT 'S2.2 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s2_3;
CREATE
OR REPLACE VIEW s2_3 AS SELECT 'S2.3 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s2_4;
CREATE
OR REPLACE VIEW s2_4 AS SELECT 'S2.4 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;


DROP VIEW IF EXISTS s2_1_test;
CREATE
OR REPLACE VIEW s2_1_test AS
SELECT code, omschrijving
FROM cursussen
WHERE code IN ('S02', 'JAV', 'GEN');

DROP VIEW IF EXISTS s2_2_test;
-- CREATE OR REPLACE VIEW s2_2_test AS
-- SELECT * FROM medewerkers;

DROP VIEW IF EXISTS s2_3_test;
CREATE
OR REPLACE VIEW s2_3_test AS
SELECT *
FROM (VALUES ('OAG'::VARCHAR(4), '2019-08-10'::DATE),
             ('S02', '2019-10-04'),
             ('JAV', '2019-12-13'),
             ('XML', '2020-09-18'),
             ('RSO', '2021-02-24')) answer (cursus, begindatum);

DROP VIEW IF EXISTS s2_4_test;
CREATE
OR REPLACE VIEW s2_4_test AS
SELECT naam, voorl
FROM medewerkers
WHERE mnr != 7900;

DROP VIEW IF EXISTS s2_5_test;
CREATE
OR REPLACE VIEW s2_5_test AS
SELECT cursus
FROM uitvoeringen
         JOIN medewerkers ON mnr = docent
WHERE naam = 'SMIT'
  AND cursus = 'S02'
  AND EXTRACT(MONTH FROM begindatum) = 3
  AND EXTRACT(DAY FROM begindatum) = 2;

DROP VIEW IF EXISTS s2_6_test;
CREATE
OR REPLACE VIEW s2_6_test AS
SELECT naam
FROM medewerkers
WHERE mnr >= 8000
  AND functie = 'STAGIAIR';

DROP VIEW IF EXISTS s2_7_test;
CREATE
OR REPLACE VIEW s2_7_test AS
SELECT snr
FROM schalen;



DROP VIEW IF EXISTS s3_1;
CREATE
OR REPLACE VIEW s3_1 AS SELECT 'S3.1 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s3_2;
CREATE
OR REPLACE VIEW s3_2 AS SELECT 'S3.2 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s3_3;
CREATE
OR REPLACE VIEW s3_3 AS SELECT 'S3.3 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s3_4;
CREATE
OR REPLACE VIEW s3_4 AS SELECT 'S3.4 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s3_5;
CREATE
OR REPLACE VIEW s3_5 AS SELECT 'S3.5 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s3_6;
CREATE
OR REPLACE VIEW s3_6 AS SELECT 'S3.6 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;


DROP VIEW IF EXISTS s3_1_test;
CREATE
OR REPLACE VIEW s3_1_test AS
SELECT *
FROM (VALUES ('S02'::VARCHAR(4), '2019-04-12'::DATE, 4::NUMERIC(2), 'SPIJKER'::VARCHAR(12)),
             ('OAG', '2019-08-10', 1, 'JANSEN'),
             ('S02', '2019-10-04', 4, 'SMIT'),
             ('S02', '2019-12-13', 4, 'SMIT'),
             ('JAV', '2019-12-13', 4, 'JANSEN'),
             ('XML', '2020-02-03', 2, 'SMIT'),
             ('JAV', '2020-02-01', 4, 'ADAMS'),
             ('PLS', '2020-09-11', 1, 'SCHOTTEN'),
             ('OAG', '2020-09-27', 1, 'SPIJKER'),
             ('RSO', '2021-02-24', 2, 'SCHOTTEN')) answer (code, begindatum, lengte, naam);

DROP VIEW IF EXISTS s3_2_test;
CREATE
OR REPLACE VIEW s3_2_test AS
SELECT *
FROM (VALUES ('ALDERS'::VARCHAR(12), 'SPIJKER'::VARCHAR(12)),
             ('BLAAK', 'SMIT'),
             ('BLAAK', 'SPIJKER'),
             ('SCHOTTEN', 'SMIT'),
             ('DE KONING', 'SMIT'),
             ('ADAMS', 'SPIJKER'),
             ('SPIJKER', 'SMIT'),
             ('SPIJKER', 'SMIT'),
             ('MOLENAAR', 'SPIJKER')) answer (cursist, docent);

DROP VIEW IF EXISTS s3_3_test;
CREATE
OR REPLACE VIEW s3_3_test AS
SELECT *
FROM (VALUES ('HOOFDKANTOOR'::VARCHAR(20), 'CLERCKX'::VARCHAR(12)),
             ('OPLEIDINGEN', 'JANSEN'),
             ('VERKOOP', 'BLAAK'),
             ('PERSONEELSZAKEN', 'DE KONING')) answer (afdeling, hoofd);

DROP VIEW IF EXISTS s3_4_test;
CREATE
OR REPLACE VIEW s3_4_test AS
SELECT *
FROM (VALUES ('ALDERS'::VARCHAR(12), 'VERKOOP'::VARCHAR(20), 'UTRECHT'::VARCHAR(20)),
             ('DE WAARD', 'VERKOOP', 'UTRECHT'),
             ('JANSEN', 'OPLEIDINGEN', 'DE MEERN'),
             ('MARTENS', 'VERKOOP', 'UTRECHT'),
             ('BLAAK', 'VERKOOP', 'UTRECHT'),
             ('CLERCKX', 'HOOFDKANTOOR', 'LEIDEN'),
             ('SCHOTTEN', 'OPLEIDINGEN', 'DE MEERN'),
             ('DE KONING', 'HOOFDKANTOOR', 'LEIDEN'),
             ('DEN DRAAIER', 'VERKOOP', 'UTRECHT'),
             ('ADAMS', 'OPLEIDINGEN', 'DE MEERN'),
             ('SPIJKER', 'OPLEIDINGEN', 'DE MEERN'),
             ('MOLENAAR', 'HOOFDKANTOOR', 'LEIDEN'),
             ('SMIT', 'OPLEIDINGEN', 'DE MEERN'),
             ('JANSEN', 'VERKOOP', 'UTRECHT')) answer (afdeling, hoofd);

DROP VIEW IF EXISTS s3_5_test;
CREATE
OR REPLACE VIEW s3_5_test AS
SELECT naam
FROM medewerkers
WHERE mnr IN (7499, 7934, 7698, 7876);

DROP VIEW IF EXISTS s3_6_test;
CREATE
OR REPLACE VIEW s3_6_test AS
SELECT *
FROM (VALUES ('ALDERS'::VARCHAR(12), 100.00::NUMERIC(6, 2)),
             ('DE WAARD', 50.00),
             ('JANSEN', 200.00),
             ('MARTENS', 50.00),
             ('BLAAK', 200.00),
             ('CLERCKX', 200.00),
             ('SCHOTTEN', 200.00),
             ('DE KONING', 500.00),
             ('DEN DRAAIER', 100.00),
             ('ADAMS', 0.00),
             ('SPIJKER', 200.00),
             ('MOLENAAR', 50.00),
             ('SMIT', 0.00),
             ('JANSEN', 0.00)) answer (naam, toelage);



DROP VIEW IF EXISTS s4_1;
CREATE
OR REPLACE VIEW s4_1 AS SELECT 'S4.1 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s4_2;
CREATE
OR REPLACE VIEW s4_2 AS SELECT 'S4.2 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s4_3;
CREATE
OR REPLACE VIEW s4_3 AS SELECT 'S4.3 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s4_4;
CREATE
OR REPLACE VIEW s4_4 AS SELECT 'S4.4 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s4_5;
CREATE
OR REPLACE VIEW s4_5 AS SELECT 'S4.5 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s4_6;
CREATE
OR REPLACE VIEW s4_6 AS SELECT 'S4.6 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s4_7;
CREATE
OR REPLACE VIEW s4_7 AS SELECT 'S4.7 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;


DROP VIEW IF EXISTS s4_1_test;
CREATE
OR REPLACE VIEW s4_1_test AS
SELECT *
FROM (VALUES (7654::NUMERIC(4), 'VERKOPER'::VARCHAR(10), '1976-09-28'::DATE),
             (7788, 'TRAINER', '1979-11-26'),
             (7902, 'TRAINER', '1979-02-13')) answer (mnr, functie, gbdatum);


DROP VIEW IF EXISTS s4_2_test;
CREATE
OR REPLACE VIEW s4_2_test AS
SELECT *
FROM (VALUES ('DE WAARD'::VARCHAR(12)),
             ('DE KONING'),
             ('DEN DRAAIER')) answer (naam);

DROP VIEW IF EXISTS s4_3_test;
CREATE
OR REPLACE VIEW s4_3_test AS
SELECT *
FROM (VALUES ('JAV'::VARCHAR(4), '2019-12-13'::DATE, 5::BIGINT),
             ('OAG', '2019-08-10', 3),
             ('S02', '2019-04-12', 4),
             ('S02', '2019-10-04', 3)) answer (cursus, begindatum, aantal_inschrijvingen);

DROP VIEW IF EXISTS s4_4_test;
CREATE
OR REPLACE VIEW s4_4_test AS
SELECT *
FROM (VALUES (7788::NUMERIC(4), 'JAV'::VARCHAR(4)),
             (7902, 'S02'),
             (7698, 'S02')) answer (cursist, cursus);

DROP VIEW IF EXISTS s4_5_test;
CREATE
OR REPLACE VIEW s4_5_test AS
SELECT *
FROM (VALUES ('JAV'::VARCHAR(4), 2::BIGINT),
             ('S02', 3),
             ('PRO', 1),
             ('OAG', 2),
             ('RSO', 1),
             ('ERM', 1),
             ('PLS', 1),
             ('XML', 2)) answer (cursus, aantal);

-- Geen test voor S4.6 mogelijk.

DROP VIEW IF EXISTS s4_7_test;
CREATE
OR REPLACE VIEW s4_7_test AS
SELECT *
FROM (VALUES (14::BIGINT, 150::NUMERIC,
              525::NUMERIC)) answer (aantal_medewerkers, commissie_medewerkers, commissie_verkopers);



DROP VIEW IF EXISTS s5_1;
CREATE
OR REPLACE VIEW s5_1 AS SELECT 'S5.1 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s5_2;
CREATE
OR REPLACE VIEW s5_2 AS SELECT 'S5.2 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s5_3;
CREATE
OR REPLACE VIEW s5_3 AS SELECT 'S5.3 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s5_4a;
CREATE
OR REPLACE VIEW s5_4a AS SELECT 'S5.4a heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s5_4b;
CREATE
OR REPLACE VIEW s5_4b AS SELECT 'S5.4b heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s5_5;
CREATE
OR REPLACE VIEW s5_5 AS SELECT 'S5.5 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s5_6;
CREATE
OR REPLACE VIEW s5_6 AS SELECT 'S5.6 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s5_7;
CREATE
OR REPLACE VIEW s5_7 AS SELECT 'S5.7 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s5_8;
CREATE
OR REPLACE VIEW s5_8 AS SELECT 'S5.8 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;


DROP VIEW IF EXISTS s5_1_test;
CREATE
OR REPLACE VIEW s5_1_test AS
SELECT *
FROM (VALUES (7499::NUMERIC(4))) answer (cursist);

DROP VIEW IF EXISTS s5_2_test;
CREATE
OR REPLACE VIEW s5_2_test AS
SELECT *
FROM (VALUES (7934::NUMERIC(4)),
             (7839),
             (7782),
             (7499),
             (7900),
             (7844),
             (7698),
             (7521),
             (7654)) answer (mnr);

DROP VIEW IF EXISTS s5_3_test;
CREATE
OR REPLACE VIEW s5_3_test AS
SELECT *
FROM (VALUES (7902::NUMERIC(4)),
             (7934),
             (7369),
             (7654),
             (7521),
             (7844),
             (7900)) answer (mnr);

DROP VIEW IF EXISTS s5_4a_test;
CREATE
OR REPLACE VIEW s5_4a_test AS
SELECT *
FROM (VALUES ('JANSEN'::VARCHAR(12)),
             ('CLERCKX'),
             ('SCHOTTEN'),
             ('DE KONING'),
             ('SPIJKER'),
             ('BLAAK')) answer (naam);

DROP VIEW IF EXISTS s5_4b_test;
CREATE
OR REPLACE VIEW s5_4b_test AS
SELECT *
FROM (VALUES ('SMIT'::VARCHAR(12)),
             ('ALDERS'),
             ('DE WAARD'),
             ('MARTENS'),
             ('ADAMS'),
             ('JANSEN'),
             ('MOLENAAR'),
             ('DEN DRAAIER')) answer (naam);

DROP VIEW IF EXISTS s5_5_test;
CREATE
OR REPLACE VIEW s5_5_test AS
SELECT *
FROM (VALUES ('XML'::VARCHAR(12), '2020-02-03'::DATE),
             ('JAV', '2020-02-01'),
             ('PLS', '2020-09-11'),
             ('XML', '2020-09-18')) answer (cursus, begindatum);

DROP VIEW IF EXISTS s5_6_test;
CREATE
OR REPLACE VIEW s5_6_test AS
SELECT *
FROM (VALUES ('S02'::VARCHAR(12), '2019-04-12'::DATE, 4::BIGINT),
             ('OAG', '2019-08-10', 3),
             ('S02', '2019-10-04', 3),
             ('S02', '2019-12-13', 2),
             ('JAV', '2019-12-13', 5),
             ('JAV', '2020-02-01', 3),
             ('XML', '2020-02-03', 2),
             ('PLS', '2020-09-11', 3),
             ('XML', '2020-09-18', 0),
             ('OAG', '2020-09-27', 1),
             ('ERM', '2021-01-15', 0),
             ('PRO', '2021-02-19', 0),
             ('RSO', '2021-02-24', 0)) answer (cursus, begindatum, aantal_inschrijvingen);

DROP VIEW IF EXISTS s5_7_test;
CREATE
OR REPLACE VIEW s5_7_test AS
SELECT *
FROM (VALUES ('N'::VARCHAR(5), 'SMIT'::VARCHAR(12))) answer (cursist);

DROP VIEW IF EXISTS s5_8_test;
CREATE
OR REPLACE VIEW s5_8_test AS
SELECT *
FROM (VALUES ('DE KONING'::VARCHAR(12)),
             ('MOLENAAR'),
             ('MARTENS'),
             ('DE WAARD'),
             ('BLAAK'),
             ('JANSEN'),
             ('ALDERS'),
             ('CLERCKX'),
             ('DEN DRAAIER')) answer (naam);

-- ------------------------------------------------------------------------
-- Data & Persistency
-- Opdracht S4: Advanced SQL
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

-- Vervolgens kun je je uitwerkingen testen door de testregels
-- (met [TEST] erachter) te activeren (haal hiervoor de commentaartekens
-- weg) en vervolgens het hele bestand uit te voeren. Hiervoor moet je de
-- testsuite in de database hebben geladen (bedrijf_postgresql_test.sql).
-- NB: niet alle opdrachten hebben testregels.
--
-- Lever je werk pas in op Canvas als alle tests slagen.
-- ------------------------------------------------------------------------

-- S4.1.
-- Geef nummer, functie en geboortedatum van alle medewerkers die vóór 1980
-- geboren zijn, en trainer of verkoper zijn.
DROP VIEW IF EXISTS s4_1;
CREATE
OR REPLACE VIEW s4_1 AS
SELECT mnr, functie, gbdatum
from medewerkers
WHERE gbdatum < '1980-01-01'
  AND (functie = 'TRAINER' OR functie = 'VERKOPER');
-- S4.2.
-- Geef de naam van de medewerkers met een tussenvoegsel (b.v. 'van der').
DROP VIEW IF EXISTS s4_2;
CREATE
OR REPLACE VIEW s4_2 AS                                                     -- [TEST]
SELECT naam
FROM medewerkers
WHERE naam LIKE '% %';

-- S4.3.
-- Geef nu code, begindatum en aantal inschrijvingen (`aantal_inschrijvingen`) van alle
-- cursusuitvoeringen in 2019 met minstens drie inschrijvingen.
DROP VIEW IF EXISTS s4_3;
CREATE
OR REPLACE VIEW s4_3 AS
SELECT cursist, begindatum, cursus
FROM inschrijvingen
GROUP BY cursist, cursus
HAVING count(cursus) > 1;


-- S4.4.
-- Welke medewerkers hebben een bepaalde cursus meer dan één keer gevolgd?
-- Geef medewerkernummer en cursuscode.
-- DROP VIEW IF EXISTS s4_4; CREATE OR REPLACE VIEW s4_4 AS                                                     -- [TEST]


DROP VIEW IF EXISTS s4_4;
CREATE
OR REPLACE VIEW s4_4 AS                                                     -- [TEST]
SELECT cursist, cursus
FROM inschrijvingen
GROUP BY cursist, cursus
HAVING count(cursus) > 1;

-- S4.5.
-- Hoeveel uitvoeringen (`aantal`) zijn er gepland per cursus?
-- Een voorbeeld van het mogelijke resultaat staat hieronder.
--
--   cursus | aantal
--  --------+-----------
--   ERM    | 1
--   JAV    | 4
--   OAG    | 2
-- DROP VIEW IF EXISTS s4_5; CREATE OR REPLACE VIEW s4_5 AS                                                     -- [TEST]
DROP VIEW IF EXISTS s4_5;
CREATE
OR REPLACE VIEW s4_5 AS                                                     -- [TEST]
SELECT cursus, count(cursus) as aantal
FROM uitvoeringen
GROUP BY cursus;

-- S4.6.
-- Bepaal hoeveel jaar leeftijdsverschil er zit tussen de oudste en de
-- jongste medewerker (`verschil`) en bepaal de gemiddelde leeftijd van
-- de medewerkers (`gemiddeld`).
-- Je mag hierbij aannemen dat elk jaar 365 dagen heeft.
-- DROP VIEW IF EXISTS s4_6; CREATE OR REPLACE VIEW s4_6 AS                                                     -- [TEST]
SELECT (max(gbdatum) - min(gbdatum)) / 365 AS verschil,
       avg(current_date - gbdatum) / 365   AS gemiddeld
FROM medewerkers;

-- S4.7.
-- Geef van het hele bedrijf een overzicht van het aantal medewerkers dat
-- er werkt (`aantal_medewerkers`), de gemiddelde commissie die ze
-- krijgen (`commissie_medewerkers`), en hoeveel dat gemiddeld
-- per verkoper is (`commissie_verkopers`).
-- DROP VIEW IF EXISTS s4_7; CREATE OR REPLACE VIEW s4_7 AS                                                     -- [TEST]
DROP VIEW IF EXISTS s4_7;
CREATE
OR REPLACE VIEW s4_7 AS                                                     -- [TEST]
SELECT sum(1)                                                            AS aantal_medewerkers,
       sum(comm) / sum(1)                                                AS commissie_medewerkers,
       sum(comm) / sum(case when functie = 'VERKOPER' then 1 else 0 end) AS commissie_verkopers
FROM medewerkers;

