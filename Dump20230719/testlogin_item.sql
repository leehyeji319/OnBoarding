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
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item` (
  `item_id` bigint NOT NULL AUTO_INCREMENT,
  `item_description` varchar(255) DEFAULT NULL,
  `is_main` bit(1) DEFAULT NULL,
  `is_remove` bit(1) DEFAULT NULL,
  `item_img_url` varchar(255) DEFAULT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `item_price` int DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  PRIMARY KEY (`item_id`),
  KEY `FK2n9w8d0dp4bsfra9dcg0046l4` (`category_id`),
  CONSTRAINT `FK2n9w8d0dp4bsfra9dcg0046l4` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` VALUES (1,'매우 좋은거',_binary '',_binary '\0','','프리미엄 상품',100000,9),(2,'대신 잡혀갈 수 있음',_binary '',_binary '\0','','점퍼가 되고싶다면 도전하세요',800000,17),(3,'왜 누군가의 일까',_binary '',_binary '','','누군가의 성수_Remove_1',10000,22),(4,'아이가 되고싶다면',_binary '',_binary '\0','','최애의 아이 코스튬',50000,24),(5,'매우 빨라요. 대신 좀 사나워요',_binary '',_binary '\0',NULL,'타조',1000,12),(6,'산지직송',_binary '',_binary '\0','','유기농 포스잼',34000,23),(7,'선물하러면 1원 비쌈',_binary '',_binary '\0','','상품권 선물용 10만원',100001,10),(8,'짭이라 쫌 쌉니다',_binary '',_binary '\0','','맹고워치',88888,20),(9,'지금 자고있을껄',_binary '',_binary '\0','','국가스탠 복장',9000,15),(10,'징박힌 장갑이 오천오백원',_binary '',_binary '\0','','주먹이 쎄지고싶다면',5500,16),(11,'부작용 : 대신 나이를 먹게됨',_binary '',_binary '\0','','인생의 경험치를 늘려주는 템',999999,21),(12,'포스잼보다 마딛더',_binary '\0',_binary '\0','','메이플잼',66000,23),(13,'잡아먹힐 수 있음 주의',_binary '\0',_binary '\0','','호랑이',6000000,12),(14,'랜덤 박스~ ',_binary '',_binary '\0','','랜덤으로 쎄짐',1000,20),(15,'123123',_binary '\0',_binary '','','테스트1',1231,9),(16,'1112',_binary '\0',_binary '\0','','test2',2222,9),(17,'12',_binary '\0',_binary '\0','','test3',12123123,9),(18,'12',_binary '\0',_binary '\0','','test4',111,9),(19,'2',_binary '\0',_binary '\0','','test5',2342342,9),(20,'6',_binary '\0',_binary '\0','','test6',666,9),(21,'77',_binary '',_binary '\0','','test7',7777,9),(22,'8',_binary '\0',_binary '\0','','test8',888,9),(23,'999',_binary '\0',_binary '\0','','test99',99,9),(24,'11',_binary '\0',_binary '\0','','test10',1010,9),(25,'1213123',_binary '\0',_binary '\0','','test11',123123,9),(26,'1313',_binary '\0',_binary '\0','','test',13,9),(27,'a123',_binary '\0',_binary '\0','','test123123',1212,9),(28,'3214',_binary '\0',_binary '\0','','test3242',12312312,9),(29,'test444',_binary '\0',_binary '\0','','test4444',4444,9),(42,'이미지 테스트1',_binary '\0',_binary '',NULL,'이미지 테스트1',1000,11),(43,'test2',_binary '\0',_binary '',NULL,'이미지 테스트2_Removed',1211,13),(44,'test2',_binary '\0',_binary '',NULL,'이미지 테스트2_Removed',111,13),(45,'test2',_binary '\0',_binary '',NULL,'이미지 테스트2_Removed',112,9),(46,'test',_binary '\0',_binary '\0',NULL,'이미지 테스트2',11231,13),(47,'test',_binary '\0',_binary '\0',NULL,'이미지 테스트3',11231,12),(48,'image',_binary '\0',_binary '\0',NULL,'image1',111,15),(49,'image',_binary '\0',_binary '\0',NULL,'image2',111,15),(50,'te',_binary '\0',_binary '\0',NULL,'image2342',3234,12),(51,'test11',_binary '\0',_binary '\0',NULL,'이미지 테스트5',1000,9),(52,'test1',_binary '\0',_binary '\0',NULL,'킹왕짱아이템1수정본4',1000,23),(53,'dfdd',_binary '\0',_binary '\0',NULL,'묘약오브러브tes',500,9),(54,'32142',_binary '\0',_binary '\0',NULL,'이매진테슷트',123123,17);
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
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
