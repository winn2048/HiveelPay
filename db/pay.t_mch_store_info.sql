-- MySQL dump 10.13  Distrib 5.7.21, for macos10.13 (x86_64)
--
-- Host: 69.12.82.234    Database: pay
-- ------------------------------------------------------
-- Server version	5.5.5-10.3.7-MariaDB-1:10.3.7+maria~jessie

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
-- Table structure for table `t_mch_store_info`
--

DROP TABLE IF EXISTS `t_mch_store_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_mch_store_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mchId` varchar(45) NOT NULL,
  `storeId` varchar(45) NOT NULL,
  `storeName` varchar(45) DEFAULT NULL,
  `storePhone` varchar(300) DEFAULT NULL COMMENT '多个电话号码用英文逗号分隔',
  `emails` varchar(500) DEFAULT NULL,
  `createAt` timestamp NOT NULL DEFAULT current_timestamp(),
  `lastUpdateAt` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_mchid_storeId` (`mchId`,`storeId`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_mch_store_info`
--

LOCK TABLES `t_mch_store_info` WRITE;
/*!40000 ALTER TABLE `t_mch_store_info` DISABLE KEYS */;
INSERT INTO `t_mch_store_info` VALUES (1,'10000002','store001','tradein_west_covina','9095339804,9099908811,8625760556,6262571121,4159968517,6508669332','service@tradeinsolutions.com','2018-09-13 17:01:22','2018-10-04 16:57:07'),(2,'10000002','store002','tradein_la','3104206699,9099908811,8625760556,6262571121,4159968517,6508669332','service@tradeinsolutions.com','2018-09-13 17:02:27','2018-10-04 16:57:07'),(8,'10000001','store003','On-Site Automotive Services','8184216311,8182018631','info@on-siteautomotiveservices.com,william@on-siteautomotiveservices.com','2018-09-15 23:40:52','2018-10-04 16:57:07'),(14,'10000000','hiveel','hiveel',NULL,'iris@hiveel.com','2018-10-04 16:55:41','2018-10-04 16:55:41');
/*!40000 ALTER TABLE `t_mch_store_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-10-22 10:06:55
