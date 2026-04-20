-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 30-03-2026 a las 16:19:48
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `federazioa`
--

DELIMITER $$
--
-- Procedimientos
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `aldatu_golak_salbuespenekin` (`p_id` INT, `p_etxeko` INT, `p_kanpoko` INT)   BEGIN
DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
SELECT 'ERROREA: Datu okerrak sartu dituzu edo partidua ez da existitzen.' AS Mezua;
END;
IF p_etxeko IS NULL OR p_kanpoko IS NULL THEN
  SIGNAL SQLSTATE '45000'
  SET MESSAGE_TEXT = 'NULL balioak ez dira onartzen';
ELSE
  UPDATE partiduak
  SET etxeko_golak = p_etxeko, kanpoko_golak = p_kanpoko
  WHERE id_partidua = p_id;
  IF ROW_COUNT() = 0 THEN
    SELECT 'ABISUA: Partidua ez da aurkitu edo datuak berdinak ziren.' AS Mezua;
  ELSE
  SELECT 'ONDO: Partidua eguneratu da.' AS Mezua;
  END IF;
END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `bilatuDenboraldikoTaldeak` (IN `p_urtea` INT)  READS SQL DATA BEGIN
  DECLARE zenbat INT;
  SELECT COUNT(*) INTO zenbat FROM denboraldi_taldeak dt WHERE dt.denboraldia_urtea = p_urtea;
  IF zenbat = 0 THEN SELECT CONCAT('Ez da aurkitu denboraldi ',p_urtea,' gure datu basean') as Mesua; END IF;
  SELECT t.izena AS 'Taldeak' FROM denboraldiak d  
  JOIN denboraldi_taldeak dt on d.urtea = dt.denboraldia_urtea JOIN taldeak t ON dt.id_taldea = t.id_taldea
  WHERE dt.denboraldia_urtea = p_urtea;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `eguneratu_jokalaria` (`p_id_jokalaria` INT, `p_id_taldea` INT, `p_abizena` VARCHAR(100))   BEGIN
  DECLARE EXIT HANDLER FOR 1452 BEGIN
  SELECT 'ERROREA: Sartu duzun taldea ez da existitzen gure sisteman.' AS Mezua;
  END;
DECLARE EXIT HANDLER FOR 1048 BEGIN
  SELECT 'ERROREA: Jokalariaren abizena ezin da NULL izan.' AS Mezua;
  END;
IF p_id_jokalaria <= 0 THEN
  SELECT 'ERROREA: Jokalariaren ID-a positiboa izan behar da.' AS Mezua;
ELSE
  UPDATE jokalariak
  SET id_taldea = p_id_taldea,
  abizena = p_abizena
  WHERE id_jokalaria = p_id_jokalaria;
  IF ROW_COUNT() = 0 THEN
    SELECT 'ABISUA: Ez da jokalaria aurkitu edo datuak berdinak ziren jada.' AS Mezua;
  ELSE
    SELECT CONCAT('ONDO: ', p_id_jokalaria, ' jokalariaren datuak eguneratu dira.') AS Mezua;
  END IF;
END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `eguneratu_jokalari_posizioa` (`p_id_jokalaria` INT, `p_pos_kodea` CHAR(1))  MODIFIES SQL DATA BEGIN
  DECLARE v_posizio_berria VARCHAR(50);
 CASE UPPER(p_pos_kodea)
    WHEN 'A' THEN SET v_posizio_berria = 'Atezaina';
    WHEN 'H' THEN SET v_posizio_berria = 'Hegala';
    WHEN 'I' THEN SET v_posizio_berria = 'Itxiera';
    WHEN 'P' THEN SET v_posizio_berria = 'Pibota';
    ELSE SET v_posizio_berria = 'Ezezaguna';
  END CASE;
  IF v_posizio_berria != 'Ezezaguna' THEN
    UPDATE jokalariak SET posizioa = v_posizio_berria WHERE id_jokalaria = p_id_jokalaria; SELECT CONCAT('Posizioa ', v_posizio_berria, ' -ra eguneratuta') AS Emaitza;
  ELSE SELECT 'Errorea: Kode baliogabea. Erabili A, H, I edo P.' AS Emaitza; END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `eguneratu_partidu_emaitza` (IN `p_id_partidua` INT, IN `p_etxeko_golak` INT, IN `p_kanpoko_golak` INT)  MODIFIES SQL DATA BEGIN
DECLARE v_emaitza_testua VARCHAR(100);
DECLARE v_existitzen_da INT DEFAULT 0;
SELECT COUNT(*) INTO v_existitzen_da
FROM partiduak
WHERE id_partidua = p_id_partidua;
IF v_existitzen_da = 0 THEN
SELECT CONCAT( 'ERROREA: ', p_id_partidua, ' ID-dun partidua ez da aurkitu.') AS Mezua;
ELSE CASE
  WHEN p_etxeko_golak > p_kanpoko_golak THEN
  SET v_emaitza_testua = 'Etxeko taldeak irabazi du.';
WHEN p_etxeko_golak < p_kanpoko_golak THEN
SET v_emaitza_testua = 'Kanpoko taldeak irabazi du.';
WHEN p_etxeko_golak = p_kanpoko_golak THEN
SET v_emaitza_testua = 'Berdinketa izan da.';
ELSE
SET v_emaitza_testua = 'Emaitza ezezaguna.';
END CASE;
UPDATE partiduak
SET etxeko_golak = p_etxeko_golak,
  kanpoko_golak = p_kanpoko_golak
WHERE id_partidua = p_id_partidua;
SELECT CONCAT('ONDO: Partidua eguneratua. ', v_emaitza_testua) AS Mezua;
END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `ezabatu_denboraldia` (`p_urtea` INT)   BEGIN
DECLARE EXIT HANDLER FOR 1451 BEGIN
SELECT 'ERROREA: Ezin da denboraldia ezabatu, oraindik datu lotuak (partiduak edo jokalariak) dituelako.' AS Mezua;
END;
DECLARE EXIT HANDLER FOR 1205 BEGIN
SELECT 'ERROREA: Datu-basea lanpetuta dago. Saiatu berriro beranduago.' AS Mezua;
END;
IF p_urtea < 1900
OR p_urtea > 2100 THEN
   SELECT 'ERROREA: Sartu duzun urtea ez da baliozkoa (1900 eta 2100 artean       egon behar da).' AS Mezua;
ELSE
   DELETE FROM denboraldiak
   WHERE urtea = p_urtea;
   IF ROW_COUNT() = 0 THEN
      SELECT CONCAT('ABISUA: Ez da aurkitu ', p_urtea, ' denboraldia gure sisteman.') AS Mezua;
   ELSE
      SELECT CONCAT('ONDO: ', p_urtea,' denboraldia ondo ezabatu da.') AS Mezua;
   END IF;
END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `ezabatu_jokalaria` (`v_izena` VARCHAR(100), `v_abizena` VARCHAR(100))  MODIFIES SQL DATA BEGIN
DECLARE zenbat INT DEFAULT 0;
SELECT COUNT(*) INTO zenbat
FROM jokalariak
WHERE izena = v_izena
  AND abizena = v_abizena;
SELECT izena, abizena
FROM jokalariak
WHERE izena = v_izena
  AND abizena = v_abizena;
IF zenbat > 0 THEN
DELETE FROM jokalariak
WHERE izena = v_izena
  AND abizena = v_abizena;
SELECT CONCAT('Jokalari ', v_izena, ' ', v_abizena, ' ezabatuta');
ELSE
SELECT CONCAT('Jokalari ', v_izena, ' ', v_abizena, ' ez da aurkitu');
END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `ezabatu_taldea_salbuespenekin` (`p_id_taldea` INT)   BEGIN
  DECLARE EXIT HANDLER FOR 1451
  BEGIN
    SELECT 'ERROREA: Ezin da taldea ezabatu jokalariak dituelako lotuta.' AS Mezua;
  END;
  DECLARE EXIT HANDLER FOR SQLSTATE '45001'
  BEGIN
    SELECT 'ERROREA: Ez da talderik aurkitu ID horrekin.' AS Mezua;
  END;
  IF (SELECT COUNT(*) FROM taldeak WHERE id_taldea = p_id_taldea) = 0 THEN
    SIGNAL SQLSTATE '45001';
  ELSE
    DELETE FROM taldeak WHERE id_taldea = p_id_taldea;
    SELECT 'ONDO: Taldea ezabatu da.' AS Mezua;
  END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `jokalaria_berria_sartu` (IN `p_izena` VARCHAR(50), IN `p_abizena` VARCHAR(50), IN `p_id_taldea` INT)  MODIFIES SQL DATA BEGIN
DECLARE talde_kop INT;
DECLARE v_mezua VARCHAR(255);
SELECT COUNT(*) INTO talde_kop
FROM taldeak
WHERE id_taldea = p_id_taldea;
IF talde_kop = 0 THEN
SET v_mezua = CONCAT('KONTUZ: ', p_id_taldea, ' taldea ez dago. ');
ELSE
SET v_mezua = 'ONDO: Taldea aurkitu da. ';
END IF;
INSERT INTO jokalariak (izena, abizena, id_taldea)
VALUES (p_izena, p_abizena, p_id_taldea);
SELECT CONCAT(
    v_mezua,
    'Jokalaria (',
    p_izena,
    ') erregistratu da.'
  ) AS Mezua;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `jokalaria_kudeatu` (IN `p_id` INT, IN `p_egoera` VARCHAR(20))  MODIFIES SQL DATA BEGIN
  IF p_egoera = 'Lesionatuta' THEN
    UPDATE jokalariak SET aktiboa = 0 WHERE id_jokalaria = p_id; 
    SELECT 'Jokalaria bajan dago (lesioa).' AS Emaitza;
  ELSEIF p_egoera = 'Sankzionatuta' THEN
    UPDATE jokalariak SET aktiboa = 0 WHERE id_jokalaria = p_id; 
    SELECT 'Jokalaria ezin da jokatu (sankzioa).' AS Emaitza;
  ELSE
    UPDATE jokalariak SET aktiboa = 1 WHERE id_jokalaria = p_id; SELECT 'Jokalaria aktibo dago.' AS Emaitza;
  END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `jokalarien_taula_sinplea` ()   BEGIN
    DECLARE amaitu INT DEFAULT FALSE;
    DECLARE v_izena VARCHAR(100);
    DECLARE v_abizena VARCHAR(100);
    DECLARE kurtsorea CURSOR FOR
        SELECT t.izena, j.abizena
        FROM taldeak t
        JOIN jokalariak j ON t.id_taldea = j.id_taldea;   
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET amaitu = TRUE;  
    DROP TEMPORARY TABLE IF EXISTS TempTxostena3;
    CREATE TEMPORARY TABLE TempTxostena3 (Talde_Izena VARCHAR(100), Jokalari_Abizena VARCHAR(100)); 
    OPEN kurtsorea;
    begizta: LOOP
        FETCH kurtsorea INTO v_izena, v_abizena;
        IF amaitu THEN LEAVE begizta; END IF;
        INSERT INTO TempTxostena3 VALUES (v_izena, v_abizena);
    END LOOP;
    CLOSE kurtsorea;  
    SELECT * FROM TempTxostena3;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `kudeatu_jokalari_egoera` (`p_id` INT, `p_akzioa` VARCHAR(10))  MODIFIES SQL DATA BEGIN
  DECLARE v_existitzen_da INT DEFAULT 0;
  DECLARE v_izena VARCHAR(100);
  SELECT COUNT(*), izena INTO v_existitzen_da, v_izena 
  FROM jokalariak 
  WHERE id_jokalaria = p_id;
  IF v_existitzen_da = 0 THEN
    SELECT CONCAT('ERROREA: ', p_id, ' ID-a ez dago datu-basean.') AS Mezua;
    ELSEIF p_akzioa = 'ACT' THEN
    UPDATE jokalariak SET aktiboa = 1 WHERE id_jokalaria = p_id;
    SELECT CONCAT('ONDO: ', v_izena, ' aktibatu da.') AS Mezua;
  ELSEIF p_akzioa = 'DES' THEN
    UPDATE jokalariak SET aktiboa = 0 WHERE id_jokalaria = p_id;
    SELECT CONCAT('ONDO: ', v_izena, ' desaktibatu da.') AS Mezua;
  ELSEIF p_akzioa = 'DEL' THEN
    DELETE FROM jokalariak WHERE id_jokalaria = p_id;
    SELECT CONCAT('KONTUZ: ', v_izena, ' datu-basetik ezabatu da.') AS Mezua;
  ELSE
    SELECT 'ERROREA: Akzio okerra. Erabili: ACT, DES edo DEL.' AS Mezua;
  END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `p_hirien_diagnostiko_txostena` ()   BEGIN
  DECLARE amaitu INT DEFAULT FALSE;
  DECLARE v_hiria VARCHAR(100);
  DECLARE v_diagnostikoa VARCHAR(100);
  DECLARE kurtsorea CURSOR FOR 
    SELECT DISTINCT talde_hiria FROM v_jokalari_taldeak;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET amaitu = TRUE;
  DROP TEMPORARY TABLE IF EXISTS TempHiriTxostena;
  CREATE TEMPORARY TABLE TempHiriTxostena (Hiria VARCHAR(100), Diagnostikoa VARCHAR(100));
  OPEN kurtsorea;
  begizta: LOOP
    FETCH kurtsorea INTO v_hiria;
    IF amaitu THEN LEAVE begizta; END IF;
    SET v_diagnostikoa = f_diagnostikoa_hirika(v_hiria);   
    INSERT INTO TempHiriTxostena VALUES (v_hiria, v_diagnostikoa);
  END LOOP;
  CLOSE kurtsorea;
  SELECT * FROM TempHiriTxostena;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `p_jokalari_aktiboen_zerrenda` ()   BEGIN
    DECLARE amaitu INT DEFAULT FALSE;
    DECLARE v_izena VARCHAR(100);
    DECLARE v_taldea VARCHAR(100);
    DECLARE kurtsorea CURSOR FOR 
        SELECT CONCAT(izena, ' ', abizena), talde_izena FROM v_jokalari_aktiboak;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET amaitu = TRUE;
    DROP TEMPORARY TABLE IF EXISTS TempAktiboak;
    CREATE TEMPORARY TABLE TempAktiboak (Jokalaria VARCHAR(200), Taldea VARCHAR(100));
    OPEN kurtsorea;
    begizta: LOOP
        FETCH kurtsorea INTO v_izena, v_taldea;
        IF amaitu THEN LEAVE begizta; END IF;
        INSERT INTO TempAktiboak VALUES (v_izena, v_taldea);
    END LOOP;
    CLOSE kurtsorea;
    SELECT * FROM TempAktiboak;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `p_taldeen_maila_txostena` ()   BEGIN
    DECLARE amaitu INT DEFAULT FALSE;
    DECLARE v_id INT;
    DECLARE v_izena VARCHAR(100);
    DECLARE v_maila VARCHAR(50);
    DECLARE kurtsorea CURSOR FOR 
        SELECT id_taldea, izena FROM taldeak
        WHERE id_taldea IN (SELECT DISTINCT etxeko_taldea_id FROM partiduak);
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET amaitu = TRUE;
    DROP TEMPORARY TABLE IF EXISTS TempMailaTxostena;
    CREATE TEMPORARY TABLE TempMailaTxostena (Taldea VARCHAR(100), Eraso_Maila VARCHAR(50));
    OPEN kurtsorea;
    begizta: LOOP
        FETCH kurtsorea INTO v_id, v_izena;
        IF amaitu THEN LEAVE begizta; END IF;
        SET v_maila = f_taldearen_maila(v_id);
        INSERT INTO TempMailaTxostena VALUES (v_izena, v_maila);
    END LOOP;
    CLOSE kurtsorea;
    SELECT * FROM TempMailaTxostena;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `p_taldeen_tamaina_txostena` (`p_urtea` INT)   BEGIN
  DECLARE amaitu INT DEFAULT FALSE;
  DECLARE v_id_taldea INT;
  DECLARE v_izena VARCHAR(100);
  DECLARE v_egoera VARCHAR(50);
  DECLARE kurtsorea CURSOR FOR
    SELECT id_taldea, izena FROM taldeak
    WHERE id_taldea IN (SELECT DISTINCT id_taldea FROM denboraldi_jokalariak);   
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET amaitu = TRUE; 
  DROP TEMPORARY TABLE IF EXISTS TempTxostena2;
  CREATE TEMPORARY TABLE TempTxostena2 (Taldea VARCHAR(100), Urtea INT, TaldearenEgoera VARCHAR(50)); 
  OPEN kurtsorea;
  begizta: LOOP
    FETCH kurtsorea INTO v_id_taldea, v_izena;
    IF amaitu THEN LEAVE begizta; END IF;
    SET v_egoera = f_talde_tamaina(v_id_taldea, p_urtea);   
    INSERT INTO TempTxostena2 VALUES (v_izena, p_urtea, v_egoera);
  END LOOP;
  CLOSE kurtsorea; 
  SELECT * FROM TempTxostena2;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `taldeen_etxeko_gol_txostena` ()   BEGIN
  DECLARE amaitu INT DEFAULT FALSE;
  DECLARE v_id_taldea INT;
  DECLARE v_izena VARCHAR(100);
  DECLARE v_partidu_kop INT;
  DECLARE v_gol_totalak INT; 
  DECLARE kurtsorea CURSOR FOR
    SELECT t.id_taldea, t.izena, COUNT(p.id_jardunaldia)
    FROM taldeak t
    LEFT JOIN partiduak p ON t.id_taldea = p.etxeko_taldea_id
    GROUP BY t.id_taldea, t.izena;   
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET amaitu = TRUE; 
  DROP TEMPORARY TABLE IF EXISTS TempTxostena1;
  CREATE TEMPORARY TABLE TempTxostena1 (Taldea VARCHAR(100), EtxeanJokatutakoak INT, EtxekoGolakTotala INT); 
  OPEN kurtsorea;
  begizta: LOOP
    FETCH kurtsorea INTO v_id_taldea, v_izena, v_partidu_kop;
    IF amaitu THEN LEAVE begizta; END IF;  
    SET v_gol_totalak = f_etxeko_gol_guztiak(v_id_taldea);  
    INSERT INTO TempTxostena1 VALUES (v_izena, v_partidu_kop, v_gol_totalak);
  END LOOP;
  CLOSE kurtsorea; 
  SELECT * FROM TempTxostena1;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `txertatu_denboraldia_salbuespenekin` (`p_urtea` INT)   BEGIN
  DECLARE EXIT HANDLER FOR 1062
  BEGIN
    SELECT CONCAT('ERROREA: ', p_urtea, ' denboraldia jada existitzen da.') AS Mezua;
  END;
  DECLARE EXIT HANDLER FOR SQLSTATE '45000'
  BEGIN
    SELECT 'ERROREA: Urteak 2000 eta 2100 artekoa izan behar du.' AS Mezua;
  END;
  IF p_urtea < 2000 OR p_urtea > 2100 THEN
    SIGNAL SQLSTATE '45000';
  ELSE
    INSERT INTO denboraldiak (urtea) VALUES (p_urtea);
    SELECT CONCAT('ONDO: ', p_urtea, ' denboraldia sortu da.') AS Mezua;
  END IF;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `txertatu_denboraldi_berria` (`p_urtea` INT)  MODIFIES SQL DATA BEGIN
  DECLARE v_existitzen_da INT DEFAULT 0;
  SELECT COUNT(*) INTO v_existitzen_da FROM denboraldiak WHERE urtea = p_urtea;
  IF p_urtea < 2000 OR p_urtea > 2100 THEN
    SELECT CONCAT('ERROREA: ', p_urtea, ' urtea ez da baliozkoa. 2000 eta 2100 artean egon behar da.') AS Mezua;
     ELSEIF v_existitzen_da > 0 THEN
    SELECT CONCAT('ABISUA: ', p_urtea, ' denboraldia jada erregistratuta dago gure sisteman.') AS Mezua;   
  ELSE
    INSERT INTO denboraldiak (urtea) VALUES (p_urtea);
    SELECT CONCAT('ONDO: ', p_urtea, ' denboraldi berria ondo erregistratu da.') AS Mezua;   
  END IF; 
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `txertatu_taldea_salbuespenekin` (`p_izena` VARCHAR(100), `p_hiria` VARCHAR(100))   BEGIN
  DECLARE EXIT HANDLER FOR 1062
  BEGIN
    SELECT CONCAT('ERROREA:"', p_izena,'" izeneko taldea jada existitzen da.'
) AS Mezua;
  END;
  DECLARE EXIT HANDLER FOR 1048
  BEGIN
    SELECT 'ERROREA: Taldearen izena eta hiria ezin dira hutsik (NULL) egon.' AS Mezua;
  END;
 INSERT INTO taldeak (izena, hiria) VALUES (p_izena, p_hiria);
  SELECT CONCAT('ONDO: "', p_izena, '" taldea ondo erregistratu da.') AS Mezua;
END$$

--
-- Funciones
--
CREATE DEFINER=`root`@`localhost` FUNCTION `f_diagnostikoa_hirika` (`p_hiria` VARCHAR(100)) RETURNS VARCHAR(100) CHARSET utf8mb4 COLLATE utf8mb4_general_ci DETERMINISTIC BEGIN
  DECLARE v_kontatzailea INT DEFAULT 0;
  DECLARE v_hiri_aurkitua VARCHAR(100);
  DECLARE amaitu INT DEFAULT FALSE;
  DECLARE cur_hiri CURSOR FOR 
    SELECT talde_hiria FROM v_jokalari_taldeak;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET amaitu = TRUE;
  DECLARE EXIT HANDLER FOR SQLEXCEPTION RETURN 'ERROREA: Datu-basean arazo bat egon da';
  IF p_hiria IS NULL OR p_hiria = '' THEN
    RETURN 'ERROREA: Hiriaren izena beharrezkoa da';
  END IF;
  OPEN cur_hiri;
  irakurri: LOOP
    FETCH cur_hiri INTO v_hiri_aurkitua;
    IF amaitu THEN LEAVE irakurri; END IF;
    IF v_hiri_aurkitua = p_hiria THEN
      SET v_kontatzailea = v_kontatzailea + 1;
    END IF;
  END LOOP;
  CLOSE cur_hiri;
  IF v_kontatzailea > 10 THEN
    RETURN CONCAT(v_kontatzailea, ' jokalari: Hiria oso aktiboa');
  ELSEIF v_kontatzailea > 0 THEN
    RETURN CONCAT(v_kontatzailea, ' jokalari: Hiria maila ertainean');
  ELSE
    RETURN 'Ez dago jokalaririk hiri honetan';
  END IF;
END$$

CREATE DEFINER=`root`@`localhost` FUNCTION `f_etxeko_gol_guztiak` (`p_talde` INT, `p_urtea` INT) RETURNS INT(11) DETERMINISTIC BEGIN 
  DECLARE v_guztira INT DEFAULT 0;
  SELECT SUM(etxeko_golak) INTO v_guztira 
  FROM partiduak p
  JOIN jardunaldiak j ON p.id_jardunaldia = j.id_jardunaldia JOIN denboraldiak d ON j.denboraldia_urtea = d.urtea
  WHERE p.etxeko_taldea_id = p_talde AND d.urtea = p_urtea;
  IF v_guztira IS NULL THEN SET v_guztira = 0; END IF;
  RETURN v_guztira;
END$$

CREATE DEFINER=`root`@`localhost` FUNCTION `f_taldearen_maila` (`p_id_taldea` INT) RETURNS VARCHAR(50) CHARSET utf8mb4 COLLATE utf8mb4_general_ci DETERMINISTIC BEGIN
  DECLARE v_golak INT;
  DECLARE v_guztira INT DEFAULT 0;
  DECLARE amaitu INT DEFAULT FALSE;
  DECLARE cur_golak CURSOR FOR 
    SELECT etxeko_golak FROM partiduak WHERE etxeko_taldea_id = p_id_taldea;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET amaitu = TRUE;
  DECLARE EXIT HANDLER FOR SQLEXCEPTION RETURN 'Errorea kalkuluan';
  IF p_id_taldea < 0 THEN
    RETURN 'ID ez-baliozkoa';
  END IF;
  OPEN cur_golak;
  irakurri: LOOP
    FETCH cur_golak INTO v_golak;
    IF amaitu THEN LEAVE irakurri; END IF;
    IF v_golak IS NOT NULL THEN
      SET v_guztira = v_guztira + v_golak;
    END IF;
  END LOOP;
  CLOSE cur_golak;
  CASE 
    WHEN v_guztira >= 15 THEN RETURN 'Eraso indartsua';
    WHEN v_guztira > 0 THEN RETURN 'Eraso normala';
    ELSE RETURN 'Eraso eskasa edo partidurik gabe';
  END CASE;
END$$

CREATE DEFINER=`root`@`localhost` FUNCTION `f_talde_tamaina` (`p_talde` INT, `p_urtea` INT) RETURNS VARCHAR(50) CHARSET utf8mb4 COLLATE utf8mb4_general_ci DETERMINISTIC BEGIN 
  DECLARE v_kopurua INT DEFAULT 0;
  DECLARE v_jokalari_id INT;
  DECLARE amaitu INT DEFAULT FALSE;
  DECLARE kurtsorea CURSOR FOR
    SELECT jokalari_id FROM denboraldi_jokalariak
    WHERE id_taldea = p_talde AND denboraldia_urtea = p_urtea;
     DECLARE CONTINUE HANDLER FOR NOT FOUND SET amaitu = TRUE;
  DECLARE EXIT HANDLER FOR SQLEXCEPTION RETURN 'Errorea';
  
  OPEN kurtsorea;
  irakurri: LOOP
    FETCH kurtsorea INTO v_jokalari_id;
    IF amaitu THEN LEAVE irakurri; END IF;
    SET v_kopurua = v_kopurua + 1;
  END LOOP;
  CLOSE kurtsorea;
  
  IF v_kopurua >= 5 THEN
    RETURN CONCAT(v_kopurua, ' (Talde osatua)');
  ELSEIF v_kopurua > 0 THEN
    RETURN CONCAT(v_kopurua, ' (Jokalariak falta dira)');
  ELSE
    RETURN 'Ez du jokalaririk';
  END IF;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `denboraldiak`
--

CREATE TABLE `denboraldiak` (
  `urtea` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `denboraldiak`
--

INSERT INTO `denboraldiak` (`urtea`) VALUES
(2024);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `denboraldi_jokalariak`
--

CREATE TABLE `denboraldi_jokalariak` (
  `denboraldia_urtea` int(11) NOT NULL,
  `id_taldea` int(11) NOT NULL,
  `id_jokalaria` bigint(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `denboraldi_jokalariak`
--

INSERT INTO `denboraldi_jokalariak` (`denboraldia_urtea`, `id_taldea`, `id_jokalaria`) VALUES
(2024, 1, 1),
(2024, 1, 2),
(2024, 1, 3),
(2024, 1, 4),
(2024, 1, 5),
(2024, 1, 6),
(2024, 1, 7),
(2024, 1, 8),
(2024, 1, 9),
(2024, 1, 10),
(2024, 2, 11),
(2024, 2, 12),
(2024, 2, 13),
(2024, 2, 14),
(2024, 2, 15),
(2024, 2, 16),
(2024, 2, 17),
(2024, 2, 18),
(2024, 2, 19),
(2024, 2, 20),
(2024, 3, 21),
(2024, 3, 22),
(2024, 3, 23),
(2024, 3, 24),
(2024, 3, 25),
(2024, 3, 26),
(2024, 3, 27),
(2024, 3, 28),
(2024, 3, 29),
(2024, 3, 30),
(2024, 4, 31),
(2024, 4, 32),
(2024, 4, 33),
(2024, 4, 34),
(2024, 4, 35),
(2024, 4, 36),
(2024, 4, 37),
(2024, 4, 38),
(2024, 4, 39),
(2024, 4, 40),
(2024, 5, 41),
(2024, 5, 42),
(2024, 5, 43),
(2024, 5, 44),
(2024, 5, 45),
(2024, 5, 46),
(2024, 5, 47),
(2024, 5, 48),
(2024, 5, 49),
(2024, 5, 50),
(2024, 6, 51),
(2024, 6, 52),
(2024, 6, 53),
(2024, 6, 54),
(2024, 6, 55),
(2024, 6, 56),
(2024, 6, 57),
(2024, 6, 58),
(2024, 6, 59),
(2024, 6, 60),
(2024, 7, 61),
(2024, 7, 62),
(2024, 7, 63),
(2024, 7, 64),
(2024, 7, 65),
(2024, 7, 66),
(2024, 7, 67),
(2024, 7, 68),
(2024, 7, 69),
(2024, 7, 70),
(2024, 8, 71),
(2024, 8, 72),
(2024, 8, 73),
(2024, 8, 74),
(2024, 8, 75),
(2024, 8, 76),
(2024, 8, 77),
(2024, 8, 78),
(2024, 8, 79),
(2024, 8, 80),
(2024, 9, 81),
(2024, 9, 82),
(2024, 9, 83),
(2024, 9, 84),
(2024, 9, 85),
(2024, 9, 86),
(2024, 9, 87),
(2024, 9, 88),
(2024, 9, 89),
(2024, 9, 90),
(2024, 10, 91),
(2024, 10, 92),
(2024, 10, 93),
(2024, 10, 94),
(2024, 10, 95),
(2024, 10, 96),
(2024, 10, 97),
(2024, 10, 98),
(2024, 10, 99),
(2024, 10, 100),
(2024, 11, 101),
(2024, 11, 102),
(2024, 11, 103),
(2024, 11, 104),
(2024, 11, 105),
(2024, 11, 106),
(2024, 11, 107),
(2024, 11, 108),
(2024, 11, 109),
(2024, 11, 110),
(2024, 12, 111),
(2024, 12, 112),
(2024, 12, 113),
(2024, 12, 114),
(2024, 12, 115),
(2024, 12, 116),
(2024, 12, 117),
(2024, 12, 118),
(2024, 12, 119),
(2024, 12, 120);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `denboraldi_taldeak`
--

CREATE TABLE `denboraldi_taldeak` (
  `denboraldia_urtea` int(11) NOT NULL,
  `id_taldea` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `denboraldi_taldeak`
--

INSERT INTO `denboraldi_taldeak` (`denboraldia_urtea`, `id_taldea`) VALUES
(2024, 1),
(2024, 2),
(2024, 3),
(2024, 4),
(2024, 5),
(2024, 6);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `jardunaldiak`
--

CREATE TABLE `jardunaldiak` (
  `id_jardunaldia` int(10) UNSIGNED NOT NULL,
  `zenbakia` int(11) DEFAULT NULL,
  `denboraldia_urtea` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `jardunaldiak`
--

INSERT INTO `jardunaldiak` (`id_jardunaldia`, `zenbakia`, `denboraldia_urtea`) VALUES
(1, 1, 2024),
(2, 2, 2024),
(3, 3, 2024),
(4, 4, 2024),
(5, 5, 2024),
(6, 6, 2024),
(7, 7, 2024),
(8, 8, 2024),
(9, 9, 2024),
(10, 10, 2024);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `jokalariak`
--

CREATE TABLE `jokalariak` (
  `id_jokalaria` bigint(20) UNSIGNED NOT NULL,
  `izena` varchar(100) NOT NULL,
  `abizena` varchar(100) NOT NULL,
  `dortsala` int(11) DEFAULT NULL,
  `posizioa` varchar(50) DEFAULT NULL,
  `jaiotze_urtea` int(11) DEFAULT NULL,
  `id_taldea` int(11) DEFAULT NULL,
  `aktiboa` tinyint(1) DEFAULT 1,
  `irudia` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `jokalariak`
--

INSERT INTO `jokalariak` (`id_jokalaria`, `izena`, `abizena`, `dortsala`, `posizioa`, `jaiotze_urtea`, `id_taldea`, `aktiboa`, `irudia`) VALUES
(1, 'Sergio', 'Lozano', 9, 'Hegala', 1988, 1, 1, 'avatar_1.png'),
(2, 'Jean Pierre', 'Pito', 10, 'Pibota', 1991, 1, 1, 'avatar_2.png'),
(3, 'Adolfo', 'Fernández', 8, 'Hegala', 1993, 1, 1, 'avatar_3.png'),
(4, 'Dídac', 'Plana', 21, 'Atezaina', 1990, 1, 1, 'avatar_4.png'),
(5, 'Matheus', 'Rodrigues', 3, 'Hegala', 1996, 1, 1, 'avatar_5.png'),
(6, 'Miquel', 'Feixas', 26, 'Atezaina', 1997, 1, 1, 'avatar_6.png'),
(7, 'Antonio', 'Pérez', 2, 'Itxiera', 2000, 1, 1, 'avatar_7.png'),
(8, 'André', 'Coelho', 4, 'Itxiera', 1993, 1, 1, 'avatar_8.png'),
(9, 'Alex', 'Yepes', 11, 'Pibota', 1989, 1, 1, 'avatar_9.png'),
(10, 'Erick', 'Mendonça', 99, 'Unibertsala', 1995, 1, 1, 'avatar_10.png'),
(11, 'Rafa', 'Santos', 11, 'Pibota', 1990, 2, 1, 'avatar_11.png'),
(12, 'Felipe', 'Valerio', 5, 'Itxiera', 1993, 2, 1, 'avatar_12.png'),
(13, 'Gadeia', 'Fabricio', 13, 'Hegala', 1988, 2, 1, 'avatar_13.png'),
(14, 'Juanjo', 'Angosto', 12, 'Atezaina', 1985, 2, 1, 'avatar_14.png'),
(15, 'Marcel', 'Marques', 10, 'Hegala', 1996, 2, 1, 'avatar_15.png'),
(16, 'Edu', 'Sousa', 1, 'Atezaina', 1992, 2, 1, 'avatar_16.png'),
(17, 'Bruno', 'Taffy', 9, 'Pibota', 1990, 2, 1, 'avatar_17.png'),
(18, 'Artem', 'Niyazov', 96, 'Hegala', 1996, 2, 1, 'avatar_18.png'),
(19, 'David', 'Álvarez', 8, 'Hegala', 1997, 2, 1, 'avatar_19.png'),
(20, 'Ricardo', 'Mayor', 4, 'Itxiera', 1998, 2, 1, 'avatar_20.png'),
(21, 'Jesús', 'Herrero', 1, 'Atezaina', 1986, 3, 1, 'avatar_21.png'),
(22, 'Raúl', 'Gómez', 8, 'Hegala', 1995, 3, 1, 'avatar_22.png'),
(23, 'Cecilio', 'Morales', 2, 'Itxiera', 1992, 3, 1, 'avatar_23.png'),
(24, 'Lucas', 'Tripodi', 3, 'Hegala', 1994, 3, 1, 'avatar_24.png'),
(25, 'Fits', 'Rafael', 12, 'Pibota', 1992, 3, 1, 'avatar_25.png'),
(26, 'Javi', 'Mínguez', 6, 'Hegala', 1996, 3, 1, 'avatar_26.png'),
(27, 'Tomás', 'Drahovsky', 10, 'Pibota', 1990, 3, 1, 'avatar_27.png'),
(28, 'Jhonatan', 'Linhares', 14, 'Itxiera', 1993, 3, 1, 'avatar_28.png'),
(29, 'Humberto', 'Dalmata', 7, 'Hegala', 1987, 3, 1, 'avatar_29.png'),
(30, 'Kaito', 'Yamada', 19, 'Hegala', 2000, 3, 1, 'avatar_30.png'),
(31, 'Luan', 'Muller', 1, 'Atezaina', 1993, 4, 1, 'avatar_31.png'),
(32, 'Cleber', 'Gomes', 10, 'Hegala', 1997, 4, 1, 'avatar_32.png'),
(33, 'Mario', 'Rivillos', 8, 'Hegala', 1989, 4, 1, 'avatar_33.png'),
(34, 'Moslem', 'Oladghobad', 9, 'Hegala', 1995, 4, 1, 'avatar_34.png'),
(35, 'Bruno', 'Gomes', 11, 'Pibota', 1996, 4, 1, 'avatar_35.png'),
(36, 'Carlos', 'Barrón', 21, 'Atezaina', 1987, 4, 1, 'avatar_36.png'),
(37, 'Hossein', 'Tayebi', 15, 'Pibota', 1988, 4, 1, 'avatar_37.png'),
(38, 'Chaguinha', 'Bruno', 2, 'Itxiera', 1988, 4, 1, 'avatar_38.png'),
(39, 'Fabinho', 'Teixeira', 17, 'Hegala', 2001, 4, 1, 'avatar_39.png'),
(40, 'Rómulo', 'Alves', 5, 'Itxiera', 1986, 4, 1, 'avatar_40.png'),
(41, 'Alan', 'Brandi', 10, 'Pibota', 1987, 5, 1, 'avatar_41.png'),
(42, 'Mati', 'Rosa', 41, 'Pibota', 1995, 5, 1, 'avatar_42.png'),
(43, 'Chino', 'Javier', 20, 'Hegala', 1991, 5, 1, 'avatar_43.png'),
(44, 'Michel', 'Moyano', 21, 'Hegala', 1993, 5, 1, 'avatar_44.png'),
(45, 'César', 'Velasco', 8, 'Hegala', 1997, 5, 1, 'avatar_45.png'),
(46, 'Espindola', 'Carlos', 2, 'Atezaina', 1993, 5, 1, 'avatar_46.png'),
(47, 'Pablo', 'Taborda', 14, 'Itxiera', 1986, 5, 1, 'avatar_47.png'),
(48, 'Renato', 'Lopes', 12, 'Hegala', 1997, 5, 1, 'avatar_48.png'),
(49, 'Helder', 'Seminario', 29, 'Hegala', 1999, 5, 1, 'avatar_49.png'),
(50, 'Dudú', 'Rodríguez', 15, 'Atezaina', 1998, 5, 1, 'avatar_50.png'),
(51, 'Boyis', 'Antonio', 4, 'Itxiera', 1989, 6, 1, 'avatar_51.png'),
(52, 'Pol', 'Pacheco', 10, 'Hegala', 1994, 6, 1, 'avatar_52.png'),
(53, 'Eric', 'Martel', 7, 'Hegala', 1992, 6, 1, 'avatar_53.png'),
(54, 'Abassi', 'Amin', 11, 'Pibota', 1998, 6, 1, 'avatar_54.png'),
(55, 'Lolo', 'Manuel', 21, 'Itxiera', 1988, 6, 1, 'avatar_55.png'),
(56, 'Marcao', 'Marcio', 1, 'Atezaina', 1994, 6, 1, 'avatar_56.png'),
(57, 'Solano', 'Francisco', 9, 'Pibota', 1991, 6, 1, 'avatar_57.png'),
(58, 'Bynho', 'Ferraz', 17, 'Hegala', 1992, 6, 1, 'avatar_58.png'),
(59, 'Eloy', 'Rojas', 8, 'Hegala', 1994, 6, 1, 'avatar_59.png'),
(60, 'Nano', 'David', 23, 'Itxiera', 1990, 6, 1, 'avatar_60.png'),
(61, 'Mellado', 'Miguel', 13, 'Hegala', 1999, 7, 1, 'avatar_61.png'),
(62, 'Lucao', 'Lucas', 12, 'Hegala', 1996, 7, 1, 'avatar_62.png'),
(63, 'Waltinho', 'Walter', 11, 'Pibota', 1991, 7, 1, 'avatar_63.png'),
(64, 'Bebe', 'Rafael', 4, 'Itxiera', 1990, 7, 1, 'avatar_64.png'),
(65, 'Chemi', 'José', 1, 'Atezaina', 1996, 7, 1, 'avatar_65.png'),
(66, 'Tomaz', 'Braga', 2, 'Itxiera', 1990, 7, 1, 'avatar_66.png'),
(67, 'Pablo', 'Ramírez', 9, 'Pibota', 2001, 7, 1, 'avatar_67.png'),
(68, 'Juanan', 'Sánchez', 21, 'Pibota', 1998, 7, 1, 'avatar_68.png'),
(69, 'Motta', 'Felipe', 3, 'Hegala', 2000, 7, 1, 'avatar_69.png'),
(70, 'Darío', 'Gil', 16, 'Hegala', 1997, 7, 1, 'avatar_70.png'),
(71, 'Terry', 'Prestjord', 10, 'Hegala', 1995, 8, 1, 'avatar_71.png'),
(72, 'David', 'García', 5, 'Itxiera', 1989, 8, 1, 'avatar_72.png'),
(73, 'Pintinho', 'Gabriel', 17, 'Hegala', 2000, 8, 1, 'avatar_73.png'),
(74, 'Carlos', 'Bartolomé', 2, 'Hegala', 1999, 8, 1, 'avatar_74.png'),
(75, 'Nacho', 'Gómez', 21, 'Itxiera', 2002, 8, 1, 'avatar_75.png'),
(76, 'Adrián', 'Pereira', 1, 'Atezaina', 2001, 8, 1, 'avatar_76.png'),
(77, 'Uge', 'Eugenio', 14, 'Hegala', 1997, 8, 1, 'avatar_77.png'),
(78, 'Gabi', 'Vasques', 7, 'Hegala', 1998, 8, 1, 'avatar_78.png'),
(79, 'Claudino', 'Angel', 23, 'Hegala', 1995, 8, 1, 'avatar_79.png'),
(80, 'Petry', 'João', 9, 'Pibota', 1996, 8, 1, 'avatar_80.png'),
(81, 'Khalid', 'Bouzid', 2, 'Hegala', 1998, 9, 1, 'avatar_81.png'),
(82, 'Cardona', 'David', 7, 'Hegala', 2000, 9, 1, 'avatar_82.png'),
(83, 'Verdejo', 'Víctor', 10, 'Hegala', 2001, 9, 1, 'avatar_83.png'),
(84, 'Corso', 'Sebastián', 5, 'Itxiera', 1992, 9, 1, 'avatar_84.png'),
(85, 'Povill', 'Bernat', 14, 'Hegala', 2001, 9, 1, 'avatar_85.png'),
(86, 'Borja', 'Puerta', 1, 'Atezaina', 1997, 9, 1, 'avatar_86.png'),
(87, 'Uri', 'Santos', 9, 'Pibota', 1993, 9, 1, 'avatar_87.png'),
(88, 'Marc', 'Tolrà', 4, 'Itxiera', 1991, 9, 1, 'avatar_88.png'),
(89, 'Nil', 'Closas', 6, 'Hegala', 1998, 9, 1, 'avatar_89.png'),
(90, 'Hirata', 'Neto', 11, 'Pibota', 1999, 9, 1, 'avatar_90.png'),
(91, 'Asier', 'Llamas', 1, 'Atezaina', 1993, 10, 1, 'avatar_91.png'),
(92, 'Linhares', 'Juninho', 11, 'Hegala', 1996, 10, 1, 'avatar_92.png'),
(93, 'Geraghty', 'Tony', 14, 'Hegala', 1999, 10, 1, 'avatar_93.png'),
(94, 'Fabinho', 'Silva', 17, 'Hegala', 2001, 10, 1, 'avatar_94.png'),
(95, 'Dani', 'Zurdo', 10, 'Hegala', 2000, 10, 1, 'avatar_95.png'),
(96, 'Roberto', 'Martil', 5, 'Itxiera', 1987, 10, 1, 'avatar_96.png'),
(97, 'Ion', 'Cerviño', 2, 'Itxiera', 2003, 10, 1, 'avatar_97.png'),
(98, 'Leo', 'Café', 8, 'Hegala', 2001, 10, 1, 'avatar_98.png'),
(99, 'Vento', 'Alejandro', 21, 'Hegala', 2002, 10, 1, 'avatar_99.png'),
(100, 'Raúl', 'Jiménez', 9, 'Pibota', 1998, 10, 1, 'avatar_100.png'),
(101, 'Fabio', 'Alvira', 1, 'Atezaina', 1990, 11, 1, 'avatar_101.png'),
(102, 'Zequi', 'Ezequiel', 7, 'Hegala', 1996, 11, 1, 'avatar_102.png'),
(103, 'Perin', 'Lucas', 11, 'Hegala', 1997, 11, 1, 'avatar_103.png'),
(104, 'Muhammad', 'Osamanmusa', 9, 'Pibota', 1998, 11, 1, 'avatar_104.png'),
(105, 'Pulinho', 'Victor', 10, 'Hegala', 2000, 11, 1, 'avatar_105.png'),
(106, 'Víctor', 'Arenas', 12, 'Atezaina', 2002, 11, 1, 'avatar_106.png'),
(107, 'Mykytiuk', 'Mykola', 14, 'Itxiera', 1996, 11, 1, 'avatar_107.png'),
(108, 'Kauê', 'Monteiro', 8, 'Hegala', 1999, 11, 1, 'avatar_108.png'),
(109, 'Arnaldo', 'Báez', 5, 'Itxiera', 1996, 11, 1, 'avatar_109.png'),
(110, 'Kenji', 'González', 2, 'Itxiera', 2001, 11, 1, 'avatar_110.png'),
(111, 'Henrique', 'Rafagnin', 1, 'Atezaina', 1992, 12, 1, 'avatar_111.png'),
(112, 'Power', 'Raggiati', 4, 'Itxiera', 1998, 12, 1, 'avatar_112.png'),
(113, 'Altamirano', 'Leandro', 10, 'Hegala', 1999, 12, 1, 'avatar_113.png'),
(114, 'Pirata', 'David', 7, 'Hegala', 1999, 12, 1, 'avatar_114.png'),
(115, 'Edu', 'Jabá', 14, 'Hegala', 1996, 12, 1, 'avatar_115.png'),
(116, 'Nico', 'Sarmiento', 21, 'Atezaina', 1992, 12, 1, 'avatar_116.png'),
(117, 'Matheus', 'Machado', 11, 'Hegala', 2001, 12, 1, 'avatar_117.png'),
(118, 'Attos', 'Mendes', 2, 'Itxiera', 1992, 12, 1, 'avatar_118.png'),
(119, 'Rufino', 'García', 8, 'Hegala', 1998, 12, 1, 'avatar_119.png'),
(120, 'David', 'Pazos', 20, 'Hegala', 1993, 12, 1, 'avatar_120.png');

--
-- Disparadores `jokalariak`
--
DELIMITER $$
CREATE TRIGGER `trg_jokalari_dortsala_balioztatu` BEFORE INSERT ON `jokalariak` FOR EACH ROW BEGIN
  IF NEW.dortsala < 0 THEN
    SET NEW.dortsala = 0;
  ELSEIF NEW.dortsala > 99 THEN
    SIGNAL SQLSTATE '45000' 
    SET MESSAGE_TEXT = 'ERROREA: Dortsala 0 eta 99 artekoa izan behar da.';
  END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `trg_jokalari_fitxaketa_log` AFTER UPDATE ON `jokalariak` FOR EACH ROW BEGIN
  IF OLD.id_taldea <> NEW.id_taldea THEN
    INSERT INTO jokalari_log (jokalari_id, ekintza)
    VALUES (NEW.id_jokalaria, CONCAT('Fitxaketa: ', OLD.id_taldea, ' taldetik ', NEW.id_taldea, ' taldera aldatu da.'));
  END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `jokalari_log`
--

CREATE TABLE `jokalari_log` (
  `id_log` int(11) NOT NULL,
  `jokalari_id` int(11) DEFAULT NULL,
  `ekintza` varchar(100) DEFAULT NULL,
  `data_ordua` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `partiduak`
--

CREATE TABLE `partiduak` (
  `id_partidua` bigint(20) UNSIGNED NOT NULL,
  `id_jardunaldia` int(10) UNSIGNED DEFAULT NULL,
  `etxeko_taldea_id` int(11) DEFAULT NULL,
  `kanpoko_taldea_id` int(11) DEFAULT NULL,
  `etxeko_golak` int(11) DEFAULT -1,
  `kanpoko_golak` int(11) DEFAULT -1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `partiduak`
--

INSERT INTO `partiduak` (`id_partidua`, `id_jardunaldia`, `etxeko_taldea_id`, `kanpoko_taldea_id`, `etxeko_golak`, `kanpoko_golak`) VALUES
(1, 1, 1, 6, 4, 2),
(2, 1, 2, 5, 3, 3),
(3, 1, 3, 4, 5, 4),
(4, 2, 6, 4, 1, 2),
(5, 2, 5, 3, 2, 2),
(6, 2, 1, 2, 3, 1),
(7, 3, 2, 6, 5, 0),
(8, 3, 3, 1, 2, 4),
(9, 3, 4, 5, 1, 1),
(10, 4, 6, 5, 2, 3),
(11, 4, 1, 4, 4, 4),
(12, 4, 2, 3, 3, 2),
(13, 5, 3, 6, 5, 2),
(14, 5, 4, 2, 3, 3),
(15, 5, 5, 1, 1, 4),
(16, 6, 6, 1, 2, 5),
(17, 6, 5, 2, 1, 0),
(18, 6, 4, 3, 4, 3),
(19, 7, 4, 6, 3, 1),
(20, 7, 3, 5, 4, 2),
(21, 7, 2, 1, 2, 2),
(22, 8, 6, 2, 3, 4),
(23, 8, 1, 3, 6, 3),
(24, 8, 5, 4, 2, 2),
(25, 9, 5, 6, 4, 1),
(26, 9, 4, 1, 2, 3),
(27, 9, 3, 2, 1, 1),
(28, 10, 6, 3, 2, 4),
(29, 10, 2, 4, 3, 2),
(30, 10, 1, 5, 5, 1);

--
-- Disparadores `partiduak`
--
DELIMITER $$
CREATE TRIGGER `trg_partidu_emaitza_aldaketa` AFTER UPDATE ON `partiduak` FOR EACH ROW BEGIN
  IF OLD.etxeko_golak != NEW.etxeko_golak OR OLD.kanpoko_golak !=       NEW.kanpoko_golak THEN
    INSERT INTO sistemaren_logak (taula, ekintza, deskribapena)
    VALUES ('partiduak', 'UPDATE', CONCAT('Partiduaren (ID: ', OLD.id_partidua, ') emaitza aldatu da. Lehen: ', OLD.etxeko_golak, '-', OLD.kanpoko_golak, ' -> Orain: ', NEW.etxeko_golak, '-', NEW.kanpoko_golak));
  END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sistemaren_logak`
--

CREATE TABLE `sistemaren_logak` (
  `id_loga` int(11) NOT NULL,
  `taula` varchar(50) DEFAULT NULL,
  `ekintza` varchar(50) DEFAULT NULL,
  `deskribapena` varchar(255) DEFAULT NULL,
  `‘data’` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `taldeak`
--

CREATE TABLE `taldeak` (
  `id_taldea` int(11) NOT NULL,
  `izena` varchar(100) NOT NULL,
  `ezkutua` varchar(255) DEFAULT NULL,
  `futbol_zelaia` varchar(100) DEFAULT NULL,
  `hiria` varchar(100) DEFAULT NULL,
  `aktiboa_dago` tinyint(1) DEFAULT 1,
  `sorrera_urtea` int(11) DEFAULT NULL,
  `informazioa` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `taldeak`
--

INSERT INTO `taldeak` (`id_taldea`, `izena`, `ezkutua`, `futbol_zelaia`, `hiria`, `aktiboa_dago`, `sorrera_urtea`, `informazioa`) VALUES
(1, 'Barça Futsal', 'barcelona.png', 'Palau Blaugrana', 'Barcelona', 1, 1978, 'FC Barcelonaren areto-futbol saila munduko erreferente nagusietako bat da gaur egun. 80ko hamarkadako hastapen apalen ondoren, klubak saila erabat profesionalizatu zuen tituluak irabazteko makina bihurtu arte. Palau Blaugrana gotorleku gisa erabiliz, Champions League eta liga nazional ugari irabazi dituzte. Izarrak fitxatzeko gaitasunagatik ez ezik, joko asoziatibo eta ikusgarriagatik nabarmentzen dira, jardunaldi bakoitzean milaka zale erakarriz.'),
(2, 'ElPozo Murcia', 'elpozo.png', 'Palacio de Deportes', 'Murcia', 1, 1989, 'ElPozo Murcia Costa Cálida areto-futboleko klub bat baino askoz gehiago da; Murtziako Eskualdeko erakunde historiko bat da, konstantzia eta harrobiko lana irudikatzen dituena. Elikadura taldearen babespean sortu zenetik, lehia mitikoa mantendu du Inter Movistarrekin, LNFSko historiako final epikoenak jokatuz. Bere filosofia borroka-izpiritu menderaezinean eta Espainiako selekziorako eliteko talentua etengabe sortzen duen oinarrizko egituran oinarritzen da.'),
(3, 'Inter Movistar', 'inter.png', 'Jorge Garbajosa', 'Torrejon', 1, 1977, 'José María García kazetariak \"Hora XXV\" gisa sortua, Inter Movistar areto-futboleko historiako klub onena bezala aitortzen du FIFAk. \"Makina Berdea\" bezala ezaguna, Ricardinho edo Paulo Roberto bezalako mitoak igaro dira bertatik. Torrejón de Ardozen du egoitza, eta palmares paregabea du, hamar bat Europako titulu eta liga barne, nazioarteko zirkuituan bikaintasun teknikoaren eta profesionaltasunaren estandarra izanik.'),
(4, 'Mallorca Palma Futsal', 'baleares.png', 'Son Moix', 'Palma', 1, 1998, 'Mallorca Palma Futsalek Espainiako kirolaren hazkunde azkar eta mirestuenetako bat irudikatzen du. Manacorreko eskualde-kategorietatik hasita, klubak proiektu sendo bat eraikitzen jakin du, nazioarteko talentua inork baino lehenago aurkitzen duen zuzendaritza tekniko bikain batean oinarrituta. Bere behin betiko lorpena jarraian irabazitako bi Champions Leaguekin iritsi zen, Son Moix kiroldegia balear zaleek areto-futbola amorru biziz bizi duten gune neuroalgiko bihurtuz.'),
(5, 'Jaén Paraíso Interior', 'jaen.png', 'Olivo Arena', 'Jaen', 1, 1980, 'Jaén Paraíso Interior probintzia oso baten harrotasuna da eta talde apal batek erraldoiei nola aurre egin diezaiekeen erakusten duen adibide ezin hobea. Bere nortasuna \"Marea Horiarekin\" lotuta dago, Olivo Arena partida bakoitzean betetzen duen Europako zale oihulari eta fidelienetako bat. Klubak historia egin du Espainiako Kopa batzuk irabaziz, defentsako intentsitatean eta kontraeraso hilgarri batean oinarritutako lehiakortasuna erakutsiz.'),
(6, 'Viña Albali Valdepeñas', 'valdepenas.png', 'Virgen de la Cabeza', 'Valdepeñas', 1, 2002, 'Viña Albali Valdepeñas Ciudad Realeko bihotza da areto-futbolaren elitean. Maila nagusira igo zenetik, klubak liga irauli du zale sutsu bati esker, Virgen de la Cabeza pabiloia presio-eltze bat bihurtuz. Tokiko ardogintzaren babes sendoarekin, taldeak liga eta kopa finalak jokatzea lortu du, bere entrega fisikoagatik, apaltasunagatik eta jokalarien eta Valdepeñasko herritarren arteko lotura bereziagatik nabarmenduz.'),
(7, 'Jimbee Cartagena', 'cartagena.png', 'Palacio Deportes', 'Cartagena', 1, 1993, 'Jimbee Cartagenak asmo handiko proiektu gisa berpiztu da azken urteetan hirian. Inbertsio estrategiko batekin eta joko estilo ausart batekin, klubak muga historikoak apurtzea lortu du, bere lehen liga titulua irabaziz eta talde boteretsuenekin aurrez aurre lehiatuz. Bere proiektua ez da lehen taldean bakarrik zentratzen, Cartagena areto-futbol modernoaren potentzia bihurtzean baizik, punta-puntako instalazioekin eta nazioarteko izarrez betetako plantilla batekin.'),
(8, 'Aspil-Jumpers Ribera', 'ribera.png', 'Ciudad de Tudela', 'Tudela', 1, 2001, 'Tutera kokatua, Ribera Navarra FS kudeaketa eta biziraupen eredu bat da elitean, baliabide mugatuekin baina adimen taktiko handiarekin. Esfortzuaren eta konpromisoaren lelopean, klubak urteak daramatza Lehen Mailan mantentzea lortuz, bere etxean garaitzeko talde oso zaila izanik. Gazteak trebatzen adituak dira, eta jokalari hauek Europako klub handietara jauzi egiten dute askotan, beti ere ordena taktikoan eta baloiaren errespetuan oinarritutako nortasunari eutsiz.'),
(9, 'Industrias Santa Coloma', 'industrias.png', 'Pavelló Nou', 'Sta Coloma', 1, 1975, 'Industrias Santa Colomak estatuko areto-futbol klub zaharrena izatearen ohorea du. Kirola Espainian jaio berria zen garaian sortua, Kataluniako areto-futbolaren motorra izan da ia mende erdi batez. Bere pabiloia, Pavelló Nou, kirolaren tenplu sakratu bat da, non etxeko talentu gazteen prestakuntza lehenesten den. Ligan aldaketak egon arren, Industriasek bere independentziari eta joko alaiari eutsi die beti, kirol estamentu guztien errespetua irabaziz.'),
(10, 'Xota FS', 'xota.png', 'Anaitasuna', 'Pamplona', 1, 1978, 'Xota Kirol Kluba, gaur egun Osasuna Magna bezala ezaguna futbol klubarekin duen aliantzagatik, Nafarroako areto-futbolaren ikurra da. Irurtzunen sortua eta bere partidak Iruñeko Anaitasuna pabiloian jokatuz, klubak egonkortasun harrigarria du: hamarkadak daramatzate maila gorenean lehiatzen kudeaketa hurbil eta familiar baten pean. Ezagunak dira beren kemenagatik eta bloke humanoa indibidualtasunen gainetik jartzen duen egitura batengatik.'),
(11, 'Córdoba Patrimonio', 'cordoba.png', 'Vista Alegre', 'Córdoba', 1, 2013, 'Córdoba Patrimonio de la Humanidad kategoriako klub gazteena da, baina ezinezkoa zirudien zerbait lortu du: futbol tradizio handiko hiri batean areto-futbolaren sukarra piztea. Igoera azkarra izan zuenetik, Vista Alegre kiroldegia behin eta berriz bete du, andaluziar pasioa kudeaketa profesional orekatu batekin uztartzen duen proiektua sendotuz, Kordoba hiria elite nazionaleko derrigorrezko geldialdi gisa finkatzeko asmoz.'),
(12, 'Noia Portus Apostoli', 'noia.png', 'Agustín Mourís', 'Noia', 1, 2008, 'Noia Portus Apostoli galiziar kuraiaren ordezkaria da munduko liga onenean. Herri txiki batetik elitean finkatzea lortu dute joko sendo batekin eta antolakuntza egitura eredugarri batekin. Bere pista, Agustín Mourís, zaleen hurbiltasunagatik eta egiten duten etengabeko presioagatik da ezaguna, etxean jokatutako partida bakoitza gudu taktiko bihurtuz, non Noiako taldeak beti garesti saltzen duen bere porrota.');

--
-- Disparadores `taldeak`
--
DELIMITER $$
CREATE TRIGGER `trg_talde_berria_log` AFTER INSERT ON `taldeak` FOR EACH ROW BEGIN
  INSERT INTO sistemaren_logak (taula, ekintza, deskribapena)
  VALUES ('taldeak',  'INSERT',  CONCAT('Talde berria sortu da: ', NEW.izena));
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `v_etxeko_partiduak`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `v_etxeko_partiduak` (
`etxeko_id` int(11)
,`etxeko_taldea` varchar(100)
,`etxeko_golak` int(11)
);

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `v_jokalari_aktiboak`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `v_jokalari_aktiboak` (
`izena` varchar(100)
,`abizena` varchar(100)
,`talde_izena` varchar(100)
);

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `v_jokalari_taldeak`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `v_jokalari_taldeak` (
`id_jokalari` bigint(20) unsigned
,`izena` varchar(100)
,`abizena` varchar(100)
,`posizioa` varchar(50)
,`aktiboa` tinyint(1)
,`talde_izena` varchar(100)
,`talde_hiria` varchar(100)
);

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `v_partiduak_info`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `v_partiduak_info` (
`id_jardunaldia` int(10) unsigned
,`etxeko_golak` int(11)
,`kanpoko_golak` int(11)
,`etxeko_taldea` varchar(100)
,`etxeko_id` int(11)
,`kanpoko_taldea` varchar(100)
,`kanpoko_id` int(11)
);

-- --------------------------------------------------------

--
-- Estructura para la vista `v_etxeko_partiduak`
--
DROP TABLE IF EXISTS `v_etxeko_partiduak`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_etxeko_partiduak`  AS SELECT `v_partiduak_info`.`etxeko_id` AS `etxeko_id`, `v_partiduak_info`.`etxeko_taldea` AS `etxeko_taldea`, `v_partiduak_info`.`etxeko_golak` AS `etxeko_golak` FROM `v_partiduak_info` ;

-- --------------------------------------------------------

--
-- Estructura para la vista `v_jokalari_aktiboak`
--
DROP TABLE IF EXISTS `v_jokalari_aktiboak`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_jokalari_aktiboak`  AS SELECT `v_jokalari_taldeak`.`izena` AS `izena`, `v_jokalari_taldeak`.`abizena` AS `abizena`, `v_jokalari_taldeak`.`talde_izena` AS `talde_izena` FROM `v_jokalari_taldeak` WHERE `v_jokalari_taldeak`.`aktiboa` = 1 ;

-- --------------------------------------------------------

--
-- Estructura para la vista `v_jokalari_taldeak`
--
DROP TABLE IF EXISTS `v_jokalari_taldeak`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_jokalari_taldeak`  AS SELECT `j`.`id_jokalaria` AS `id_jokalari`, `j`.`izena` AS `izena`, `j`.`abizena` AS `abizena`, `j`.`posizioa` AS `posizioa`, `j`.`aktiboa` AS `aktiboa`, `t`.`izena` AS `talde_izena`, `t`.`hiria` AS `talde_hiria` FROM (`jokalariak` `j` join `taldeak` `t` on(`j`.`id_taldea` = `t`.`id_taldea`)) ;

-- --------------------------------------------------------

--
-- Estructura para la vista `v_partiduak_info`
--
DROP TABLE IF EXISTS `v_partiduak_info`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_partiduak_info`  AS SELECT `p`.`id_jardunaldia` AS `id_jardunaldia`, `p`.`etxeko_golak` AS `etxeko_golak`, `p`.`kanpoko_golak` AS `kanpoko_golak`, `t1`.`izena` AS `etxeko_taldea`, `t1`.`id_taldea` AS `etxeko_id`, `t2`.`izena` AS `kanpoko_taldea`, `t2`.`id_taldea` AS `kanpoko_id` FROM ((`partiduak` `p` join `taldeak` `t1` on(`p`.`etxeko_taldea_id` = `t1`.`id_taldea`)) join `taldeak` `t2` on(`p`.`kanpoko_taldea_id` = `t2`.`id_taldea`)) ;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `denboraldiak`
--
ALTER TABLE `denboraldiak`
  ADD PRIMARY KEY (`urtea`);

--
-- Indices de la tabla `denboraldi_jokalariak`
--
ALTER TABLE `denboraldi_jokalariak`
  ADD PRIMARY KEY (`denboraldia_urtea`,`id_taldea`,`id_jokalaria`),
  ADD KEY `id_talde` (`id_taldea`),
  ADD KEY `id_jokalari` (`id_jokalaria`);

--
-- Indices de la tabla `denboraldi_taldeak`
--
ALTER TABLE `denboraldi_taldeak`
  ADD PRIMARY KEY (`denboraldia_urtea`,`id_taldea`),
  ADD KEY `denboraldi_urtea_taldea` (`id_taldea`);

--
-- Indices de la tabla `jardunaldiak`
--
ALTER TABLE `jardunaldiak`
  ADD PRIMARY KEY (`id_jardunaldia`),
  ADD KEY `jardunaldiaren_denboraldia` (`denboraldia_urtea`);

--
-- Indices de la tabla `jokalariak`
--
ALTER TABLE `jokalariak`
  ADD PRIMARY KEY (`id_jokalaria`),
  ADD KEY `fk_jokalariak_taldeak` (`id_taldea`);

--
-- Indices de la tabla `jokalari_log`
--
ALTER TABLE `jokalari_log`
  ADD PRIMARY KEY (`id_log`);

--
-- Indices de la tabla `partiduak`
--
ALTER TABLE `partiduak`
  ADD PRIMARY KEY (`id_partidua`),
  ADD KEY `fk_partiduak_jardunaldia` (`id_jardunaldia`),
  ADD KEY `etxeko_talde_jardunaldi` (`etxeko_taldea_id`),
  ADD KEY `kanpoko_talde_jardunaldi` (`kanpoko_taldea_id`);

--
-- Indices de la tabla `sistemaren_logak`
--
ALTER TABLE `sistemaren_logak`
  ADD PRIMARY KEY (`id_loga`);

--
-- Indices de la tabla `taldeak`
--
ALTER TABLE `taldeak`
  ADD PRIMARY KEY (`id_taldea`),
  ADD UNIQUE KEY `izena` (`izena`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `jardunaldiak`
--
ALTER TABLE `jardunaldiak`
  MODIFY `id_jardunaldia` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `jokalariak`
--
ALTER TABLE `jokalariak`
  MODIFY `id_jokalaria` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=121;

--
-- AUTO_INCREMENT de la tabla `jokalari_log`
--
ALTER TABLE `jokalari_log`
  MODIFY `id_log` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `partiduak`
--
ALTER TABLE `partiduak`
  MODIFY `id_partidua` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT de la tabla `sistemaren_logak`
--
ALTER TABLE `sistemaren_logak`
  MODIFY `id_loga` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `taldeak`
--
ALTER TABLE `taldeak`
  MODIFY `id_taldea` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `denboraldi_jokalariak`
--
ALTER TABLE `denboraldi_jokalariak`
  ADD CONSTRAINT `denboraldi_jokalariak_ibfk_1` FOREIGN KEY (`denboraldia_urtea`) REFERENCES `denboraldiak` (`urtea`) ON DELETE CASCADE,
  ADD CONSTRAINT `denboraldi_jokalariak_ibfk_2` FOREIGN KEY (`id_taldea`) REFERENCES `taldeak` (`id_taldea`) ON DELETE CASCADE,
  ADD CONSTRAINT `denboraldi_jokalariak_ibfk_3` FOREIGN KEY (`id_jokalaria`) REFERENCES `jokalariak` (`id_jokalaria`) ON DELETE CASCADE;

--
-- Filtros para la tabla `denboraldi_taldeak`
--
ALTER TABLE `denboraldi_taldeak`
  ADD CONSTRAINT `denboraldi_talde_urtea` FOREIGN KEY (`denboraldia_urtea`) REFERENCES `denboraldiak` (`urtea`),
  ADD CONSTRAINT `denboraldi_urtea_taldea` FOREIGN KEY (`id_taldea`) REFERENCES `taldeak` (`id_taldea`);

--
-- Filtros para la tabla `jardunaldiak`
--
ALTER TABLE `jardunaldiak`
  ADD CONSTRAINT `jardunaldiaren_denboraldia` FOREIGN KEY (`denboraldia_urtea`) REFERENCES `denboraldiak` (`urtea`) ON DELETE CASCADE;

--
-- Filtros para la tabla `jokalariak`
--
ALTER TABLE `jokalariak`
  ADD CONSTRAINT `fk_jokalariak_taldeak` FOREIGN KEY (`id_taldea`) REFERENCES `taldeak` (`id_taldea`);

--
-- Filtros para la tabla `partiduak`
--
ALTER TABLE `partiduak`
  ADD CONSTRAINT `etxeko_talde_jardunaldi` FOREIGN KEY (`etxeko_taldea_id`) REFERENCES `taldeak` (`id_taldea`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_partiduak_jardunaldia` FOREIGN KEY (`id_jardunaldia`) REFERENCES `jardunaldiak` (`id_jardunaldia`) ON DELETE CASCADE,
  ADD CONSTRAINT `kanpoko_talde_jardunaldi` FOREIGN KEY (`kanpoko_taldea_id`) REFERENCES `taldeak` (`id_taldea`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
