
SELECT * FROM test_select('S5.1') AS resultaat
UNION
SELECT * FROM test_select('S5.2') AS resultaat
UNION
SELECT * FROM test_select('S5.3') AS resultaat
UNION
SELECT * FROM test_select('S5.4a') AS resultaat
UNION
SELECT * FROM test_select('S5.4b') AS resultaat
UNION
SELECT * FROM test_select('S5.5') AS resultaat
UNION
SELECT * FROM test_select('S5.6') AS resultaat
UNION
SELECT * FROM test_select('S5.7') AS resultaat
UNION
SELECT * FROM test_select('S5.8') AS resultaat
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
CREATE OR REPLACE FUNCTION test_select(_exercise_nr text) RETURNS text AS $$
DECLARE _sol_name text := REPLACE(LOWER(_exercise_nr), '.', '_');
    DECLARE _test_name text := _sol_name || '_test';
    DECLARE _query_res text;
    DECLARE _missing_count int := 0;
    DECLARE _excess_count int := 0;
    DECLARE _missing_query text := 'SELECT * FROM ' || _test_name || ' EXCEPT SELECT * FROM ' || _sol_name;
    DECLARE _excess_query text := 'SELECT * FROM ' || _sol_name || ' EXCEPT SELECT * FROM ' || _test_name;
    DECLARE _rows RECORD;
BEGIN
    BEGIN
        EXECUTE 'SELECT * FROM ' || _sol_name INTO _query_res;
        IF POSITION('heeft nog geen uitwerking' IN _query_res) <> 0
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

    IF _missing_count = 0 AND _excess_count = 0 THEN
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
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS test_exists(_exercise_nr TEXT, _row_test BIGINT, _compare_type TEXT);
CREATE OR REPLACE FUNCTION test_exists(_exercise_nr TEXT, _row_test BIGINT, _compare_type TEXT DEFAULT 'exact') RETURNS TEXT AS $$
DECLARE _sol_name TEXT := REPLACE(LOWER(_exercise_nr), '.', '_');
    DECLARE _test_name TEXT := _sol_name || '_test';
    DECLARE _row_count INT := 0;
BEGIN
    EXECUTE FORMAT('SELECT COUNT(*) FROM %I;', _test_name) INTO _row_count;

    IF _compare_type = 'exact' AND _row_count = _row_test THEN
        RETURN _exercise_nr || ' heeft de juiste uitwerking op de database!';
    ELSIF _compare_type = 'maximaal' AND _row_count <= _row_test THEN
        RETURN _exercise_nr || ' heeft de juiste uitwerking op de database!';
    ELSIF _compare_type = 'minimaal' AND _row_count >= _row_test THEN
        RETURN _exercise_nr || ' heeft de juiste uitwerking op de database!';
    END IF;

    RETURN _exercise_nr || ' heeft niet de juiste uitwerking op de database: er moeten ' || _compare_type || ' ' || _row_test || ' rijen of kolommen zijn, maar de database heeft er ' || _row_count || '.';
END;
$$ LANGUAGE plpgsql;



DROP VIEW IF EXISTS s1_1_test; CREATE OR REPLACE VIEW s1_1_test AS
SELECT column_name FROM information_schema.columns WHERE table_schema='public' AND table_name='medewerkers' AND column_name='geslacht';

DROP VIEW IF EXISTS s1_2_test; CREATE OR REPLACE VIEW s1_2_test AS
SELECT m.naam FROM medewerkers m JOIN afdelingen a ON m.mnr = a.hoofd WHERE a.naam = 'ONDERZOEK' AND m.chef = 7839;

DROP VIEW IF EXISTS s1_4_test; CREATE OR REPLACE VIEW s1_4_test AS
SELECT column_name FROM information_schema.columns WHERE table_schema='public' AND table_name='adressen' AND column_name IN ('postcode', 'huisnummer', 'ingangsdatum', 'einddatum', 'telefoon', 'med_mnr');

-- Ongebruikt.
DROP VIEW IF EXISTS s1_5_test; CREATE OR REPLACE VIEW s1_5_test AS
SELECT naam FROM medewerkers WHERE mnr < 9000;



DROP VIEW IF EXISTS s2_1; CREATE OR REPLACE VIEW s2_1 AS SELECT 'S2.1 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s2_2; CREATE OR REPLACE VIEW s2_2 AS SELECT 'S2.2 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s2_3; CREATE OR REPLACE VIEW s2_3 AS SELECT 'S2.3 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s2_4; CREATE OR REPLACE VIEW s2_4 AS SELECT 'S2.4 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;


DROP VIEW IF EXISTS s2_1_test; CREATE OR REPLACE VIEW s2_1_test AS
SELECT code, omschrijving FROM cursussen WHERE code IN ('S02', 'JAV', 'GEN');

DROP VIEW IF EXISTS s2_2_test; -- CREATE OR REPLACE VIEW s2_2_test AS
-- SELECT * FROM medewerkers;

DROP VIEW IF EXISTS s2_3_test; CREATE OR REPLACE VIEW s2_3_test AS
SELECT * FROM (VALUES
                   ('OAG'::VARCHAR(4), '2019-08-10'::DATE),
                   ('S02',	'2019-10-04'),
                   ('JAV',	'2019-12-13'),
                   ('XML',	'2020-09-18'),
                   ('RSO',	'2021-02-24')
              ) answer (cursus, begindatum);

DROP VIEW IF EXISTS s2_4_test; CREATE OR REPLACE VIEW s2_4_test AS
SELECT naam, voorl FROM medewerkers WHERE mnr != 7900;

DROP VIEW IF EXISTS s2_5_test; CREATE OR REPLACE VIEW s2_5_test AS
SELECT cursus
FROM uitvoeringen JOIN medewerkers ON mnr = docent
WHERE naam = 'SMIT' AND cursus = 'S02' AND
        EXTRACT(MONTH FROM begindatum) = 3 AND
        EXTRACT(DAY FROM begindatum) = 2;

DROP VIEW IF EXISTS s2_6_test; CREATE OR REPLACE VIEW s2_6_test AS
SELECT naam FROM medewerkers WHERE mnr >= 8000 AND functie = 'STAGIAIR';

DROP VIEW IF EXISTS s2_7_test; CREATE OR REPLACE VIEW s2_7_test AS
SELECT snr FROM schalen;



DROP VIEW IF EXISTS s3_1; CREATE OR REPLACE VIEW s3_1 AS SELECT 'S3.1 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s3_2; CREATE OR REPLACE VIEW s3_2 AS SELECT 'S3.2 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s3_3; CREATE OR REPLACE VIEW s3_3 AS SELECT 'S3.3 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s3_4; CREATE OR REPLACE VIEW s3_4 AS SELECT 'S3.4 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s3_5; CREATE OR REPLACE VIEW s3_5 AS SELECT 'S3.5 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s3_6; CREATE OR REPLACE VIEW s3_6 AS SELECT 'S3.6 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;


DROP VIEW IF EXISTS s3_1_test; CREATE OR REPLACE VIEW s3_1_test AS
SELECT * FROM (VALUES
                   ('S02'::VARCHAR(4), '2019-04-12'::DATE, 4::NUMERIC(2), 'SPIJKER'::VARCHAR(12)),
                   ('OAG', '2019-08-10', 1, 'JANSEN'),
                   ('S02', '2019-10-04', 4, 'SMIT'),
                   ('S02', '2019-12-13', 4, 'SMIT'),
                   ('JAV', '2019-12-13', 4, 'JANSEN'),
                   ('XML', '2020-02-03', 2, 'SMIT'),
                   ('JAV', '2020-02-01', 4, 'ADAMS'),
                   ('PLS', '2020-09-11', 1, 'SCHOTTEN'),
                   ('OAG', '2020-09-27', 1, 'SPIJKER'),
                   ('RSO', '2021-02-24', 2, 'SCHOTTEN')
              ) answer (code, begindatum, lengte, naam);

DROP VIEW IF EXISTS s3_2_test; CREATE OR REPLACE VIEW s3_2_test AS
SELECT * FROM (VALUES
                   ('ALDERS'::VARCHAR(12), 'SPIJKER'::VARCHAR(12)),
                   ('BLAAK', 'SMIT'),
                   ('BLAAK', 'SPIJKER'),
                   ('SCHOTTEN', 'SMIT'),
                   ('DE KONING', 'SMIT'),
                   ('ADAMS', 'SPIJKER'),
                   ('SPIJKER', 'SMIT'),
                   ('SPIJKER', 'SMIT'),
                   ('MOLENAAR', 'SPIJKER')
              ) answer (cursist, docent);

DROP VIEW IF EXISTS s3_3_test; CREATE OR REPLACE VIEW s3_3_test AS
SELECT * FROM (VALUES
                   ('HOOFDKANTOOR'::VARCHAR(20), 'CLERCKX'::VARCHAR(12)),
                   ('OPLEIDINGEN', 'JANSEN'),
                   ('VERKOOP', 'BLAAK'),
                   ('PERSONEELSZAKEN', 'DE KONING')
              ) answer (afdeling, hoofd);

DROP VIEW IF EXISTS s3_4_test; CREATE OR REPLACE VIEW s3_4_test AS
SELECT * FROM (VALUES
                   ('ALDERS'::VARCHAR(12), 'VERKOOP'::VARCHAR(20), 'UTRECHT'::VARCHAR(20)),
                   ('DE WAARD',    'VERKOOP',      'UTRECHT'),
                   ('JANSEN',      'OPLEIDINGEN',  'DE MEERN'),
                   ('MARTENS',     'VERKOOP',      'UTRECHT'),
                   ('BLAAK',       'VERKOOP',      'UTRECHT'),
                   ('CLERCKX',     'HOOFDKANTOOR', 'LEIDEN'),
                   ('SCHOTTEN',    'OPLEIDINGEN',  'DE MEERN'),
                   ('DE KONING',   'HOOFDKANTOOR', 'LEIDEN'),
                   ('DEN DRAAIER', 'VERKOOP',      'UTRECHT'),
                   ('ADAMS',       'OPLEIDINGEN',  'DE MEERN'),
                   ('SPIJKER',     'OPLEIDINGEN',  'DE MEERN'),
                   ('MOLENAAR',    'HOOFDKANTOOR', 'LEIDEN'),
                   ('SMIT',        'OPLEIDINGEN',  'DE MEERN'),
                   ('JANSEN',      'VERKOOP',      'UTRECHT')
              ) answer (afdeling, hoofd);

DROP VIEW IF EXISTS s3_5_test; CREATE OR REPLACE VIEW s3_5_test AS
SELECT naam FROM medewerkers WHERE mnr IN (7499, 7934, 7698, 7876);

DROP VIEW IF EXISTS s3_6_test; CREATE OR REPLACE VIEW s3_6_test AS
SELECT * FROM (VALUES
                   ('ALDERS'::VARCHAR(12), 100.00::NUMERIC(6, 2)),
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
                   ('JANSEN', 0.00)
              ) answer (naam, toelage);



DROP VIEW IF EXISTS s4_1; CREATE OR REPLACE VIEW s4_1 AS SELECT 'S4.1 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s4_2; CREATE OR REPLACE VIEW s4_2 AS SELECT 'S4.2 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s4_3; CREATE OR REPLACE VIEW s4_3 AS SELECT 'S4.3 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s4_4; CREATE OR REPLACE VIEW s4_4 AS SELECT 'S4.4 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s4_5; CREATE OR REPLACE VIEW s4_5 AS SELECT 'S4.5 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s4_6; CREATE OR REPLACE VIEW s4_6 AS SELECT 'S4.6 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s4_7; CREATE OR REPLACE VIEW s4_7 AS SELECT 'S4.7 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;


DROP VIEW IF EXISTS s4_1_test; CREATE OR REPLACE VIEW s4_1_test AS
SELECT * FROM (VALUES
                   (7654::NUMERIC(4),	'VERKOPER'::VARCHAR(10),	'1976-09-28'::DATE),
                   (7788, 'TRAINER', 	'1979-11-26'),
                   (7902, 'TRAINER', 	'1979-02-13')
              ) answer (mnr, functie, gbdatum);


DROP VIEW IF EXISTS s4_2_test; CREATE OR REPLACE VIEW s4_2_test AS
SELECT * FROM (VALUES
                   ('DE WAARD'::VARCHAR(12)),
                   ('DE KONING'),
                   ('DEN DRAAIER')
              ) answer (naam);

DROP VIEW IF EXISTS s4_3_test; CREATE OR REPLACE VIEW s4_3_test AS
SELECT * FROM (VALUES
                   ('JAV'::VARCHAR(4),	'2019-12-13'::DATE,	5::BIGINT),
                   ('OAG',	'2019-08-10',	3),
                   ('S02',	'2019-04-12',	4),
                   ('S02',	'2019-10-04',	3)
              ) answer (cursus, begindatum, aantal_inschrijvingen);

DROP VIEW IF EXISTS s4_4_test; CREATE OR REPLACE VIEW s4_4_test AS
SELECT * FROM (VALUES
                   (7788::NUMERIC(4),	'JAV'::VARCHAR(4)),
                   (7902,	'S02'),
                   (7698,	'S02')
              ) answer (cursist, cursus);

DROP VIEW IF EXISTS s4_5_test; CREATE OR REPLACE VIEW s4_5_test AS
SELECT * FROM (VALUES
                   ('JAV'::VARCHAR(4),	2::BIGINT),
                   ('S02',	3),
                   ('PRO',	1),
                   ('OAG',	2),
                   ('RSO',	1),
                   ('ERM',	1),
                   ('PLS',	1),
                   ('XML',	2)
              ) answer (cursus, aantal);

-- Geen test voor S4.6 mogelijk.

DROP VIEW IF EXISTS s4_7_test; CREATE OR REPLACE VIEW s4_7_test AS
SELECT * FROM (VALUES
                   (14::BIGINT, 150::NUMERIC, 525::NUMERIC)
              ) answer (aantal_medewerkers, commissie_medewerkers, commissie_verkopers);



DROP VIEW IF EXISTS s5_1; CREATE OR REPLACE VIEW s5_1 AS SELECT 'S5.1 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s5_2; CREATE OR REPLACE VIEW s5_2 AS SELECT 'S5.2 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s5_3; CREATE OR REPLACE VIEW s5_3 AS SELECT 'S5.3 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s5_4a; CREATE OR REPLACE VIEW s5_4a AS SELECT 'S5.4a heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s5_4b; CREATE OR REPLACE VIEW s5_4b AS SELECT 'S5.4b heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s5_5; CREATE OR REPLACE VIEW s5_5 AS SELECT 'S5.5 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s5_6; CREATE OR REPLACE VIEW s5_6 AS SELECT 'S5.6 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s5_7; CREATE OR REPLACE VIEW s5_7 AS SELECT 'S5.7 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;
DROP VIEW IF EXISTS s5_8; CREATE OR REPLACE VIEW s5_8 AS SELECT 'S5.8 heeft nog geen uitwerking - misschien moet je de DROP VIEW ... regel nog activeren?' AS resultaat;


DROP VIEW IF EXISTS s5_1_test; CREATE OR REPLACE VIEW s5_1_test AS
SELECT * FROM (VALUES
                   (7499::NUMERIC(4))
              ) answer (cursist);

DROP VIEW IF EXISTS s5_2_test; CREATE OR REPLACE VIEW s5_2_test AS
SELECT * FROM (VALUES
                   (7934::NUMERIC(4)),
                   (7839),
                   (7782),
                   (7499),
                   (7900),
                   (7844),
                   (7698),
                   (7521),
                   (7654)
              ) answer (mnr);

DROP VIEW IF EXISTS s5_3_test; CREATE OR REPLACE VIEW s5_3_test AS
SELECT * FROM (VALUES
                   (7902::NUMERIC(4)),
                   (7934),
                   (7369),
                   (7654),
                   (7521),
                   (7844),
                   (7900)
              ) answer (mnr);

DROP VIEW IF EXISTS s5_4a_test; CREATE OR REPLACE VIEW s5_4a_test AS
SELECT * FROM (VALUES
                   ('JANSEN'::VARCHAR(12)),
                   ('CLERCKX'),
                   ('SCHOTTEN'),
                   ('DE KONING'),
                   ('SPIJKER'),
                   ('BLAAK')
              ) answer (naam);

DROP VIEW IF EXISTS s5_4b_test; CREATE OR REPLACE VIEW s5_4b_test AS
SELECT * FROM (VALUES
                   ('SMIT'::VARCHAR(12)),
                   ('ALDERS'),
                   ('DE WAARD'),
                   ('MARTENS'),
                   ('ADAMS'),
                   ('JANSEN'),
                   ('MOLENAAR'),
                   ('DEN DRAAIER')
              ) answer (naam);

DROP VIEW IF EXISTS s5_5_test; CREATE OR REPLACE VIEW s5_5_test AS
SELECT * FROM (VALUES
                   ('XML'::VARCHAR(12), '2020-02-03'::DATE),
                   ('JAV',	'2020-02-01'),
                   ('PLS',	'2020-09-11'),
                   ('XML',	'2020-09-18')
              ) answer (cursus, begindatum);

DROP VIEW IF EXISTS s5_6_test; CREATE OR REPLACE VIEW s5_6_test AS
SELECT * FROM (VALUES
                   ('S02'::VARCHAR(12),	'2019-04-12'::DATE,	4::BIGINT),
                   ('OAG',	'2019-08-10',	3),
                   ('S02',	'2019-10-04',	3),
                   ('S02',	'2019-12-13',	2),
                   ('JAV',	'2019-12-13',	5),
                   ('JAV',	'2020-02-01',	3),
                   ('XML',	'2020-02-03',	2),
                   ('PLS',	'2020-09-11',	3),
                   ('XML',	'2020-09-18',	0),
                   ('OAG',	'2020-09-27',	1),
                   ('ERM',	'2021-01-15',	0),
                   ('PRO',	'2021-02-19',	0),
                   ('RSO',	'2021-02-24',	0)
              ) answer (cursus, begindatum, aantal_inschrijvingen);

DROP VIEW IF EXISTS s5_7_test; CREATE OR REPLACE VIEW s5_7_test AS
SELECT * FROM (VALUES
                   ('N'::VARCHAR(5), 'SMIT'::VARCHAR(12))
              ) answer (cursist);

DROP VIEW IF EXISTS s5_8_test; CREATE OR REPLACE VIEW s5_8_test AS
SELECT * FROM (VALUES
                   ('DE KONING'::VARCHAR(12)),
                   ('MOLENAAR'),
                   ('MARTENS'),
                   ('DE WAARD'),
                   ('BLAAK'),
                   ('JANSEN'),
                   ('ALDERS'),
                   ('CLERCKX'),
                   ('DEN DRAAIER')
              ) answer (naam);




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
