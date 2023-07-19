-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: testlogin
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `banner_file_info`
--

DROP TABLE IF EXISTS `banner_file_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `banner_file_info` (
  `banner_file_info_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `item_id` bigint DEFAULT NULL,
  `original_file` varchar(255) DEFAULT NULL,
  `save_file` varchar(255) DEFAULT NULL,
  `save_folder_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`banner_file_info_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `banner_file_info`
--

LOCK TABLES `banner_file_info` WRITE;
/*!40000 ALTER TABLE `banner_file_info` DISABLE KEYS */;
INSERT INTO `banner_file_info` VALUES (11,'2023-07-19 08:59:22.496694','2023-07-19 08:59:22.496694',1,'banner1.jpg','3f473c13-5318-40d4-b912-934235d59b68.jpg','/img/banner/3f473c13-5318-40d4-b912-934235d59b68.jpg'),(12,'2023-07-19 08:59:32.035813','2023-07-19 08:59:32.035813',2,'banner2.jpg','db5c0465-27ce-41a5-9422-272175059aa1.jpg','/img/banner/db5c0465-27ce-41a5-9422-272175059aa1.jpg'),(13,'2023-07-19 08:59:44.107959','2023-07-19 08:59:44.107959',3,'banner3.jpg','a0f1ca3a-e361-4893-8145-0eca75d79231.jpg','/img/banner/a0f1ca3a-e361-4893-8145-0eca75d79231.jpg'),(14,'2023-07-19 09:00:00.841325','2023-07-19 09:00:00.841325',4,'banner4.jpg','93a47f1b-19b5-4fc6-adab-fedc8701b633.jpg','/img/banner/93a47f1b-19b5-4fc6-adab-fedc8701b633.jpg'),(15,'2023-07-19 13:56:14.184470','2023-07-19 13:56:14.184470',NULL,'banner3.jpg','2ce25c1a-b33d-4afd-b1fa-cb774883bf92.jpg','/img/banner/2ce25c1a-b33d-4afd-b1fa-cb774883bf92.jpg');
/*!40000 ALTER TABLE `banner_file_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-07-19 14:05:38
