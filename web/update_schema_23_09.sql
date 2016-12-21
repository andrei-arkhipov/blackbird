-- MySQL dump 10.13  Distrib 5.6.12, for Win64 (x86_64)
--
-- Host: localhost    Database: mifare
-- ------------------------------------------------------
-- Server version	5.6.12-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


--
-- Table structure for table `incorrect_events`
--

DROP TABLE IF EXISTS `incorrect_events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `incorrect_events` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` longtext,
  `internal_name` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `incorrect_events`
--

LOCK TABLES `incorrect_events` WRITE;
/*!40000 ALTER TABLE `incorrect_events` DISABLE KEYS */;
INSERT INTO `incorrect_events` VALUES (1,'','wrong_card','Неизвестный номер карты'),(2,'','wrong_reader','Неизвестный номер считывателя');
/*!40000 ALTER TABLE `incorrect_events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `incorrect_event_log`
--

DROP TABLE IF EXISTS `incorrect_event_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `incorrect_event_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `card_number` varchar(16) NOT NULL,
  `event_date` datetime NOT NULL,
  `device_number` varchar(16) NOT NULL,
  `event_id` bigint(20) NOT NULL,
  `search_field` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `FK9E255005FAC3DFF2` (`event_id`),
  CONSTRAINT `FK9E255005FAC3DFF2` FOREIGN KEY (`event_id`) REFERENCES `incorrect_events` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `incorrect_event_log`
--

LOCK TABLES `incorrect_event_log` WRITE;
/*!40000 ALTER TABLE `incorrect_event_log` DISABLE KEYS */;

/*!40000 ALTER TABLE `incorrect_event_log` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER insert_search BEFORE INSERT ON incorrect_event_log
FOR EACH ROW
    BEGIN
    SET NEW.search_field=(SELECT CONCAT_WS(' ', (NEW.card_number),
                                                (NEW.device_number),
                                                (SELECT name FROM incorrect_events where id=NEW.event_id),
                                                (SELECT DATE_FORMAT((NEW.event_date),'%d.%m.%Y %H:%i'))));
    END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER update_search BEFORE UPDATE ON incorrect_event_log
FOR EACH ROW
    BEGIN
    SET NEW.search_field=(SELECT CONCAT_WS(' ', (NEW.card_number),
                                                (NEW.device_number),
                                                (SELECT name FROM incorrect_events where id=NEW.event_id),
                                                (SELECT DATE_FORMAT((NEW.event_date),'%d.%m.%Y %H:%i'))));
    END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

ALTER TABLE `permissions` ADD COLUMN `order_number` int(11) DEFAULT NULL AFTER `name`;
--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
INSERT INTO `permissions` VALUES  (8,'','permission_devices','Разрешения для справочника устройств',8), (9,'','permission_organizations','Разрешения для справочника организаций',9), (10,'','permission_cardreaders','Разрешения для доступа к считывателям карт',10);
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
UNLOCK TABLES;

UPDATE `permissions` SET name='Разрешения для доступа к журналу событий', internal_name='permission_eventlog', order_number=1 WHERE id=1;
UPDATE `permissions` SET name='Разрешения для доступа к репортам', internal_name='permission_reports', order_number=2 WHERE id=2;
UPDATE `permissions` SET name='Разрешения для доступа к справочникам', internal_name='permission_catalogs', order_number=3 WHERE id=3;
UPDATE `permissions` SET name='Разрешения для доступа к меню Администратора', internal_name='permission_admin', order_number=4 WHERE id=4;
UPDATE `permissions` SET name='Разрешение для работы с пользователями', internal_name='permission_users', order_number=5 WHERE id=5;
UPDATE `permissions` SET name='Разрешение для работы с ролями пользователей', internal_name='permission_roles', order_number=6 WHERE id=6;
UPDATE `permissions` SET name='Разрешения для справочника рабочих', internal_name='permission_cardholders', order_number=7 WHERE id=7;

--
-- Dumping data for table `roles_permissions`
--

LOCK TABLES `roles_permissions` WRITE;
/*!40000 ALTER TABLE `roles_permissions` DISABLE KEYS */;
INSERT INTO `roles_permissions` VALUES (15,0,0,8,1),(16,0,0,9,1),(17,0,0,10,1),(18,1,1,8,2),(19,1,1,9,2),(20,1,1,10,2);
/*!40000 ALTER TABLE `roles_permissions` ENABLE KEYS */;
UNLOCK TABLES;

DROP TRIGGER hash_pass;
DROP TRIGGER hash_pass_update;