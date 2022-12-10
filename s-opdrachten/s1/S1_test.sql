SELECT *
FROM test_exists('S1.1', 1) AS resultaat
UNION
SELECT *
FROM test_exists('S1.2', 1) AS resultaat
UNION
SELECT 'S1.3 wordt niet getest: geen test mogelijk.' AS resultaat
UNION
SELECT *
FROM test_exists('S1.4', 6) AS resultaat
UNION
SELECT 'S1.5 wordt niet getest: handmatige test beschikbaar.' AS resultaat
ORDER BY resultaat;


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
