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
-- Table structure for table `item_file_info`
--

DROP TABLE IF EXISTS `item_file_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_file_info` (
  `item_file_info_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `original_file` varchar(255) DEFAULT NULL,
  `save_file` varchar(255) DEFAULT NULL,
  `save_folder_path` varchar(255) DEFAULT NULL,
  `item_id` bigint DEFAULT NULL,
  PRIMARY KEY (`item_file_info_id`),
  KEY `FKg7vi2dp9ex7p2gtc60mfvhxrf` (`item_id`),
  CONSTRAINT `FKg7vi2dp9ex7p2gtc60mfvhxrf` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_file_info`
--

LOCK TABLES `item_file_info` WRITE;
/*!40000 ALTER TABLE `item_file_info` DISABLE KEYS */;
INSERT INTO `item_file_info` VALUES (1,'2023-07-18 11:15:58.210971','2023-07-18 11:15:58.210971','catcafe.PNG','bbe040cf-a831-45ec-957e-b82a0e5fb13e.PNG','file:///D:/Programming/images/item/bbe040cf-a831-45ec-957e-b82a0e5fb13e.PNG',42),(2,'2023-07-18 12:46:22.202241','2023-07-18 12:46:22.202241','catcafe.PNG','cf0b247f-8bea-470e-9898-64f3a5d6dd1a.PNG','/images/item/cf0b247f-8bea-470e-9898-64f3a5d6dd1a.PNG',43),(3,'2023-07-18 12:47:29.461628','2023-07-18 12:47:29.461628','catcafe.PNG','7666dae1-5d0e-49e0-8b54-feeafbf4b7ee.PNG','/images/item/7666dae1-5d0e-49e0-8b54-feeafbf4b7ee.PNG',44),(4,'2023-07-18 12:49:49.799680','2023-07-18 12:49:49.799680','catcafe.PNG','b600679f-bd79-4723-8491-c0b9134f66d0.PNG','/images/item/b600679f-bd79-4723-8491-c0b9134f66d0.PNG',45),(5,'2023-07-18 12:57:53.570922','2023-07-18 12:57:53.570922','catcafe.PNG','f8deca9d-0cb0-4b59-938e-2278ad2e9dd8.PNG','/img/item/f8deca9d-0cb0-4b59-938e-2278ad2e9dd8.PNG',47),(6,'2023-07-18 13:12:12.344712','2023-07-18 13:12:12.344712','catcafe.PNG','99e4b657-6624-4b45-a3ab-92f492fece71.PNG','/img/item/99e4b657-6624-4b45-a3ab-92f492fece71.PNG',49),(7,'2023-07-18 13:20:28.576056','2023-07-18 13:20:28.576056','defaultItemImg.png','91962888-3f1d-4325-991b-d4217504c79d.png','/img/item/91962888-3f1d-4325-991b-d4217504c79d.png',50),(8,'2023-07-18 13:24:26.343042','2023-07-18 13:24:26.343042','defaultItemImg.png','b5560bb5-cdbf-408a-8fbd-18247df3a5ae.png','/img/item/b5560bb5-cdbf-408a-8fbd-18247df3a5ae.png',51),(9,'2023-07-18 13:26:06.138382','2023-07-18 13:26:06.138382','defaultItemImg.png','47a98e28-24ad-461e-bccf-4361980cf9e6.png','/img/item/47a98e28-24ad-461e-bccf-4361980cf9e6.png',52),(10,'2023-07-18 13:35:50.708198','2023-07-18 13:35:50.708198','defaultItemImg.png','d66a4ea9-ff9e-40ac-a222-3781917ecfb8.png','/img/item/d66a4ea9-ff9e-40ac-a222-3781917ecfb8.png',53),(11,'2023-07-18 13:42:27.805232','2023-07-18 13:42:27.805232','defaultItemImg.png','e488dbfb-a63d-48a2-bf6e-3fbac587176f.png','/img/item/e488dbfb-a63d-48a2-bf6e-3fbac587176f.png',54),(13,'2023-07-18 14:53:51.603016','2023-07-18 14:53:51.603016','캡처.PNG','636c8f7a-34da-4806-a27c-0d443418199d.PNG','/img/item/636c8f7a-34da-4806-a27c-0d443418199d.PNG',5);
/*!40000 ALTER TABLE `item_file_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-07-19 14:05:39
