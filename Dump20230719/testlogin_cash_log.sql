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
-- Table structure for table `cash_log`
--

DROP TABLE IF EXISTS `cash_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cash_log` (
  `cash_log_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `cash_charge_after` int NOT NULL,
  `cash_charge_amount` int NOT NULL,
  `cash_charge_before` int DEFAULT NULL,
  `cash_charge_date` datetime(6) NOT NULL,
  `cash_charge_type` varchar(255) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`cash_log_id`),
  KEY `FKpyhnmhkdh5q728wcsh79dqoo1` (`user_id`),
  CONSTRAINT `FKpyhnmhkdh5q728wcsh79dqoo1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cash_log`
--

LOCK TABLES `cash_log` WRITE;
/*!40000 ALTER TABLE `cash_log` DISABLE KEYS */;
INSERT INTO `cash_log` VALUES (1,'2023-07-14 15:24:37.841507','2023-07-14 15:24:37.841507',100000,100000,0,'2023-07-14 15:24:37.831505','무통장\n                                                        입금(가상계좌)',1),(2,'2023-07-14 17:25:10.178757','2023-07-14 17:25:10.178757',63000,30000,33000,'2023-07-14 17:25:10.176756','문화상품권',1),(3,'2023-07-17 16:39:11.200891','2023-07-17 16:39:11.200891',64000,1000,63000,'2023-07-17 16:39:11.191889','문화상품권',1),(4,'2023-07-17 16:39:22.383376','2023-07-17 16:39:22.383376',69000,5000,64000,'2023-07-17 16:39:22.383376','문화상품권',1);
/*!40000 ALTER TABLE `cash_log` ENABLE KEYS */;
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
