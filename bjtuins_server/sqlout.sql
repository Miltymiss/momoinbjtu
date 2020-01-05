-- MySQL dump 10.13  Distrib 5.7.28, for macos10.14 (x86_64)
--
-- Host: 47.93.102.137    Database: android_BJTUINS
-- ------------------------------------------------------
-- Server version	5.7.28-0ubuntu0.16.04.2

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
-- Table structure for table `chat_log`
--

DROP TABLE IF EXISTS `chat_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chat_log` (
  `from_id` varchar(20) NOT NULL COMMENT '发信人',
  `to_id` varchar(20) NOT NULL COMMENT '收信人',
  `chat_msg` varchar(512) NOT NULL COMMENT '聊天内容',
  `time` datetime NOT NULL,
  KEY `chat_log_id_ifm_userid_fk` (`from_id`),
  KEY `chat_log_id_ifm_userid_fk_2` (`to_id`),
  CONSTRAINT `chat_log_id_ifm_userid_fk` FOREIGN KEY (`from_id`) REFERENCES `id_ifm` (`userid`),
  CONSTRAINT `chat_log_id_ifm_userid_fk_2` FOREIGN KEY (`to_id`) REFERENCES `id_ifm` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='聊天记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_log`
--

LOCK TABLES `chat_log` WRITE;
/*!40000 ALTER TABLE `chat_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `chat_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment_list`
--

DROP TABLE IF EXISTS `comment_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment_list` (
  `message_id` int(11) NOT NULL,
  `user_id` varchar(20) CHARACTER SET latin1 NOT NULL,
  `comment` varchar(256) DEFAULT NULL,
  `time` datetime NOT NULL,
  `comment_id` int(11) NOT NULL,
  PRIMARY KEY (`comment_id`),
  UNIQUE KEY `comment_list_comment_id_uindex` (`comment_id`),
  KEY `comment_list_id_ifm_userid_fk` (`user_id`),
  KEY `comment_list_messages_list_message_id_fk` (`message_id`),
  CONSTRAINT `comment_list_id_ifm_userid_fk` FOREIGN KEY (`user_id`) REFERENCES `id_ifm` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_list`
--

LOCK TABLES `comment_list` WRITE;
/*!40000 ALTER TABLE `comment_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment_list` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER newcomment AFTER INSERT ON comment_list FOR EACH ROW
    BEGIN
        UPDATE messages_list SET commentSum=commentSum+1 WHERE message_id=NEW.message_id;
    end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER deletecomment AFTER DELETE ON comment_list FOR EACH ROW
    BEGIN
        UPDATE messages_list SET commentSum=commentSum-1 WHERE message_id=OLD.message_id;
    end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `friend_list`
--

DROP TABLE IF EXISTS `friend_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `friend_list` (
  `user_id` varchar(20) NOT NULL,
  `friend_id` varchar(20) NOT NULL,
  KEY `friend_list_user_ifm_userid_fk` (`user_id`),
  KEY `friend_list_user_ifm_userid_fk_2` (`friend_id`),
  CONSTRAINT `friend_list_user_ifm_userid_fk` FOREIGN KEY (`user_id`) REFERENCES `id_ifm` (`userid`),
  CONSTRAINT `friend_list_user_ifm_userid_fk_2` FOREIGN KEY (`friend_id`) REFERENCES `id_ifm` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='好友列表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend_list`
--

LOCK TABLES `friend_list` WRITE;
/*!40000 ALTER TABLE `friend_list` DISABLE KEYS */;
INSERT INTO `friend_list` VALUES ('milty111','milty333'),('milty334','miltylove'),('123333@qq.com','milty111'),('123333@qq.com','milty333'),('123333@qq.com','milty996');
/*!40000 ALTER TABLE `friend_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `id_ifm`
--

DROP TABLE IF EXISTS `id_ifm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `id_ifm` (
  `userid` varchar(20) NOT NULL,
  `password` varchar(18) NOT NULL,
  PRIMARY KEY (`userid`),
  UNIQUE KEY `user_ifm_userid_uindex` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='用户账号列表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `id_ifm`
--

LOCK TABLES `id_ifm` WRITE;
/*!40000 ALTER TABLE `id_ifm` DISABLE KEYS */;
INSERT INTO `id_ifm` VALUES ('123333@qq.com','123456'),('milty111','123456'),('milty333','123456'),('milty334','123456'),('milty996','999'),('miltylove','123456');
/*!40000 ALTER TABLE `id_ifm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `like_list`
--

DROP TABLE IF EXISTS `like_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `like_list` (
  `message_id` int(11) NOT NULL,
  `user_id` varchar(20) NOT NULL,
  `likes` tinyint(1) NOT NULL,
  KEY `like_list_id_ifm_userid_fk` (`user_id`),
  KEY `like_list_messages_list_message_id_fk` (`message_id`),
  CONSTRAINT `like_list_id_ifm_userid_fk` FOREIGN KEY (`user_id`) REFERENCES `id_ifm` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `like_list`
--

LOCK TABLES `like_list` WRITE;
/*!40000 ALTER TABLE `like_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `like_list` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER newlikes AFTER INSERT ON like_list FOR EACH ROW
    BEGIN
        IF(NEW.likes IS TRUE) THEN
            UPDATE messages_list SET likeNum=likeNum+1 WHERE message_id=NEW.message_id;
         ELSEIF (NEW.likes IS FALSE) THEN
            UPDATE messages_list SET dislikeNum=dislikeNum+1 WHERE  message_id=NEW.message_id;
        end if;
    end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER updatelikes AFTER UPDATE ON like_list FOR EACH ROW
    BEGIN
        IF(OLD.likes IS TRUE) THEN
            UPDATE messages_list SET dislikeNum=dislikeNum+1,likeNum=likeNum-1 WHERE message_id=OLD.message_id;
        ELSEIF(OLD.likes IS FALSE ) THEN
            UPDATE messages_list SET dislikeNum=dislikeNum-1,likeNum=likeNum+1 WHERE message_id=OLD.message_id;
        end if;
    end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER droplikes AFTER DELETE ON like_list FOR EACH ROW
    BEGIN
        IF(OLD.likes IS TRUE) THEN
            UPDATE messages_list SET likeNum=likeNum-1 WHERE message_id=OLD.message_id;
        ELSEIF (OLD.likes IS FALSE) THEN
            UPDATE messages_list SET dislikeNum=dislikeNum-1 WHERE  message_id=OLD.message_id;
        end if;
    end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `log_list`
--

DROP TABLE IF EXISTS `log_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log_list` (
  `user_id` varchar(20) NOT NULL,
  `operation` int(11) NOT NULL,
  `operate_time` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='日志列表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_list`
--

LOCK TABLES `log_list` WRITE;
/*!40000 ALTER TABLE `log_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message_list`
--

DROP TABLE IF EXISTS `message_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message_list` (
  `message_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(20) CHARACTER SET latin1 NOT NULL,
  `text` varchar(30) DEFAULT NULL,
  `graph` varchar(128) CHARACTER SET latin1 DEFAULT NULL COMMENT '图片地址',
  `video` varchar(128) CHARACTER SET latin1 DEFAULT NULL COMMENT '视频地址',
  `likeNum` int(11) DEFAULT '0',
  `dislikeNum` int(11) DEFAULT '0',
  `commentSum` int(11) DEFAULT '0',
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`message_id`),
  UNIQUE KEY `messages_message_id_uindex` (`message_id`),
  KEY `messages_id_ifm_userid_fk` (`user_id`),
  CONSTRAINT `messages_id_ifm_userid_fk` FOREIGN KEY (`user_id`) REFERENCES `id_ifm` (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message_list`
--

LOCK TABLES `message_list` WRITE;
/*!40000 ALTER TABLE `message_list` DISABLE KEYS */;
INSERT INTO `message_list` VALUES (1,'milty111','说得好','https://dimg04.c-ctrip.com/images/10041b000001at9r5DFA5_R_800_10000_Q90.jpg?proc=autoorient','null',0,0,0,'2020-01-03 22:08:31'),(2,'milty111','OKK','https://youimg1.c-ctrip.com/target/100c1b000001c1qjs6883_R_1024_10000_Q90.jpg?proc=autoorient','null',0,0,0,'2020-01-03 22:08:34'),(3,'milty996','ossss','https://youimg1.c-ctrip.com/target/100q1a00000197so6D066_R_671_10000_Q90.jpg?proc=autoorient','null',0,0,0,'2020-01-03 22:08:41'),(4,'milty333','ojbk','https://dimg04.c-ctrip.com/images/10041b000001at9r5DFA5_R_800_10000_Q90.jpg?proc=autoorient','null',0,0,0,'2020-01-03 22:09:57'),(5,'milty996','okkkkk','https://youimg1.c-ctrip.com/target/10061a000001974mvB0F5_R_671_10000_Q90.jpg?proc=autoorient\n','null',0,0,0,'2020-01-03 22:12:38'),(6,'123333@qq.com','xxxxxx','/tmp/123333@qq.com/img/0','null',0,0,0,'2020-01-04 10:19:57'),(7,'123333@qq.com','xxxxxx','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/0','null',0,0,0,'2020-01-04 10:25:12'),(8,'123333@qq.com','g','','null',0,0,0,'2020-01-04 11:55:07'),(9,'123333@qq.com','xxxxxx','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/0','null',0,0,0,'2020-01-04 12:48:11'),(10,'123333@qq.com','xxxxxx','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/0','null',0,0,0,'2020-01-04 12:51:27'),(11,'123333@qq.com','xxxxxx','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/0','null',0,0,0,'2020-01-04 14:01:40'),(12,'123333@qq.com','xxxxxx','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/1','null',0,0,0,'2020-01-04 14:02:50'),(13,'123333@qq.com','qqq','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/3','null',0,0,0,'2020-01-04 15:02:00'),(14,'123333@qq.com','xxxxxx','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/4','null',0,0,0,'2020-01-04 15:09:53'),(15,'123333@qq.com','a','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/5','null',0,0,0,'2020-01-04 15:18:48'),(16,'123333@qq.com','a','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/6','null',0,0,0,'2020-01-04 15:18:51'),(17,'123333@qq.com','a','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/7','null',0,0,0,'2020-01-04 15:18:51'),(18,'123333@qq.com','a','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/8','null',0,0,0,'2020-01-04 15:18:52'),(19,'123333@qq.com','a','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/9','null',0,0,0,'2020-01-04 15:18:52'),(20,'123333@qq.com','a','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/10','null',0,0,0,'2020-01-04 15:18:53'),(21,'123333@qq.com','a','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/11','null',0,0,0,'2020-01-04 15:18:53'),(22,'123333@qq.com','a','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/12','null',0,0,0,'2020-01-04 15:21:46'),(23,'123333@qq.com','a','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/13','null',0,0,0,'2020-01-04 15:21:49'),(24,'123333@qq.com','a','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/14','null',0,0,0,'2020-01-04 15:22:25'),(25,'123333@qq.com','a','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/15','null',0,0,0,'2020-01-04 15:22:27'),(26,'123333@qq.com','a','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/16','null',0,0,0,'2020-01-04 15:22:28'),(27,'123333@qq.com','r','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/17','null',0,0,0,'2020-01-04 15:27:09'),(28,'123333@qq.com','a','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/18','null',0,0,0,'2020-01-04 15:29:45'),(29,'123333@qq.com','a','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/19','null',0,0,0,'2020-01-04 15:29:47'),(30,'123333@qq.com','a','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/20','null',0,0,0,'2020-01-04 15:33:37'),(31,'123333@qq.com','a','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/21','null',0,0,0,'2020-01-04 15:35:31'),(32,'123333@qq.com','a','/Users/zhangjiahua/Desktop/BJTU-INS/server/media/123333@qq.com/img/30','null',0,0,0,'2020-01-04 19:58:27');
/*!40000 ALTER TABLE `message_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_ifm`
--

DROP TABLE IF EXISTS `user_ifm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_ifm` (
  `user_id` varchar(20) CHARACTER SET latin1 NOT NULL,
  `user_name` varchar(30) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `sex` int(11) DEFAULT '0' COMMENT '0为未知，1位男性，2为女性',
  `discription` varchar(128) DEFAULT NULL,
  `graph` varchar(128) CHARACTER SET latin1 DEFAULT NULL COMMENT '用户头像',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_ifm_user_id_uindex` (`user_id`),
  CONSTRAINT `user_ifm_id_ifm_userid_fk` FOREIGN KEY (`user_id`) REFERENCES `id_ifm` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_ifm`
--

LOCK TABLES `user_ifm` WRITE;
/*!40000 ALTER TABLE `user_ifm` DISABLE KEYS */;
INSERT INTO `user_ifm` VALUES ('123333@qq.com','mmmmmmm',NULL,0,NULL,NULL),('milty111','guo','2020-01-01',1,'xxxxxx','/local'),('milty333','miltyx',NULL,0,NULL,NULL),('milty334','miltyy',NULL,0,NULL,NULL),('milty996','lalala',NULL,0,NULL,NULL),('miltylove','milty999',NULL,0,NULL,NULL);
/*!40000 ALTER TABLE `user_ifm` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-01-05  0:57:11
