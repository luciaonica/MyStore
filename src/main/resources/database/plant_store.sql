CREATE DATABASE  IF NOT EXISTS `plantstore` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `plantstore`;
-- MySQL dump 10.13  Distrib 8.0.27, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: plantstore
-- ------------------------------------------------------
-- Server version	8.0.27

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
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `image` varchar(128) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_46ccwnsi9409t36lurvtyljak` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'plant','aloe.png'),(2,'soil','soil.png'),(3,'round pot','white.png'),(4,'rectangular pot','square.png');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(45) DEFAULT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_to73biqfrti0j9f7k12l255w5` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,'Chicago','john'),(2,'Wheaton','mary'),(3,'Wheaton','lucy');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_time` datetime(6) DEFAULT NULL,
  `customer_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `quantity` int NOT NULL DEFAULT '0',
  `description` varchar(255) DEFAULT NULL,
  `pot_type_id` int DEFAULT NULL,
  `soil_type_id` int DEFAULT NULL,
  `cost` float NOT NULL DEFAULT '0',
  `price` float NOT NULL DEFAULT '0',
  `total` float NOT NULL DEFAULT '0',
  `image` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpxtb8awmi0dk6smoh2vp1litg` (`customer_id`),
  KEY `FK82ng9tukx1mj8em3qhbsac4ce` (`pot_type_id`),
  KEY `FK787ibr3guwp6xobrpbofnv7le` (`product_id`),
  KEY `FK9gwxk40sp7cbtr6o6lehogaha` (`soil_type_id`),
  CONSTRAINT `FK787ibr3guwp6xobrpbofnv7le` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FK82ng9tukx1mj8em3qhbsac4ce` FOREIGN KEY (`pot_type_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FK9gwxk40sp7cbtr6o6lehogaha` FOREIGN KEY (`soil_type_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKpxtb8awmi0dk6smoh2vp1litg` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'2023-01-26 09:56:01.838000',3,1,1,'',4,6,6.55,12,12,'aloe12.png'),(2,'2023-01-26 10:00:40.711000',1,12,1,'',4,6,8.55,15,15,'monstera1.png'),(3,'2023-01-26 10:01:38.259000',1,2,1,'',4,6,4.55,8,8,'aglaonema small.png'),(4,'2023-01-28 16:16:55.321000',1,1,1,'Aloe 6\" tall',3,6,6.36,12,12,'aloe.png'),(5,'2023-01-28 16:25:47.991000',1,2,1,'Aloe 6\" tall',4,6,3.24,5,5,'agla.png');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `price` float NOT NULL,
  `quantity` int NOT NULL DEFAULT '1',
  `volume` float NOT NULL,
  `unit_price` float NOT NULL,
  `height_inch` float NOT NULL,
  `top_width_inch` float NOT NULL,
  `top_length_inch` float NOT NULL,
  `bottom_width_inch` float NOT NULL,
  `bottom_length_inch` float NOT NULL,
  `category_id` int DEFAULT NULL,
  `image` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_jmivyxk9rmgysrmsqw15lqr5b` (`name`),
  KEY `FK1mtsbur82frn64de7balymq9s` (`category_id`),
  CONSTRAINT `FK1mtsbur82frn64de7balymq9s` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'Aloe','Aloe 5\" tall',3,1,0,3,0,0,0,0,0,1,'aloe.png'),(2,'Aglaonema','Aglaonema baby 2 leaves',1,1,0,1,0,0,0,0,0,1,'agla baby1.png'),(3,'pot 1','round pot',2,1,142.942,2,6,6,0,5,0,3,'white.png'),(4,'pot 2','round pot',2,1,119.151,2,5.5,5.5,0,5,0,3,'brown planters.png'),(5,'pot 3','square pot',1.5,1,296,1.5,6,8,8,6,6,4,'square.png'),(6,'Soil','potting mix',12,5,924,0.012987,0,0,0,0,0,2,'soil.png'),(9,'arrowhead','',3,1,0,3,0,0,0,0,0,1,'arrowhead1.png'),(12,'Mini Monstera','',5,6,142.942,5,6,6,0,5,0,1,'monstera.png');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-01-30 10:18:12
