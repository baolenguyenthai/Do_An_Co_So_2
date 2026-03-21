-- MySQL dump 10.13  Distrib 9.5.0, for macos15.7 (arm64)
--
-- Host: mysql-22383302-lenguyenthaibao.g.aivencloud.com    Database: tracnghiem
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '77089fc4-2463-11f1-8ba3-6ee8e611f1e9:1-672,
b43f018c-cd30-11f0-8ca5-e2e8a86f5079:1-82';

--
-- Table structure for table `bai_thi`
--

DROP TABLE IF EXISTS `bai_thi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bai_thi` (
  `bai_thi_id` int NOT NULL AUTO_INCREMENT,
  `nguoi_dung_id` int DEFAULT NULL,
  `bo_cau_hoi_id` int DEFAULT NULL,
  `tong_cau` int DEFAULT NULL,
  `so_cau_dung` int DEFAULT NULL,
  `diem` float DEFAULT NULL,
  `thoi_gian_bat_dau` datetime DEFAULT NULL,
  `thoi_gian_ket_thuc` datetime DEFAULT NULL,
  PRIMARY KEY (`bai_thi_id`),
  KEY `nguoi_dung_id` (`nguoi_dung_id`),
  KEY `bo_cau_hoi_id` (`bo_cau_hoi_id`),
  CONSTRAINT `bai_thi_ibfk_1` FOREIGN KEY (`nguoi_dung_id`) REFERENCES `nguoi_dung` (`nguoi_dung_id`),
  CONSTRAINT `bai_thi_ibfk_2` FOREIGN KEY (`bo_cau_hoi_id`) REFERENCES `bo_cau_hoi` (`bo_cau_hoi_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bai_thi`
--

LOCK TABLES `bai_thi` WRITE;
/*!40000 ALTER TABLE `bai_thi` DISABLE KEYS */;
INSERT INTO `bai_thi` VALUES (1,7,1,10,10,10,'2026-03-20 21:35:04','2026-03-20 21:39:16'),(2,7,2,3,3,10,'2026-03-20 21:41:15','2026-03-20 21:41:35'),(3,18,2,3,2,6.66667,'2026-03-20 21:43:16','2026-03-20 21:43:53'),(4,20,1,10,9,9,'2026-03-20 22:55:49','2026-03-20 22:58:08'),(5,20,1,10,8,8,'2026-03-20 22:59:50','2026-03-20 23:02:10'),(6,20,3,10,6,6,'2026-03-20 23:03:07','2026-03-20 23:04:59'),(7,7,3,5,5,10,'2026-03-20 23:08:20','2026-03-20 23:08:47'),(8,7,1,3,0,0,'2026-03-20 23:26:37','2026-03-20 23:29:38'),(9,7,1,2,1,5,'2026-03-20 23:35:01','2026-03-20 23:36:02'),(10,7,1,1,1,10,'2026-03-20 23:40:56','2026-03-20 23:41:52'),(11,18,1,3,3,10,'2026-03-20 23:45:00','2026-03-20 23:47:27'),(12,2,1,2,1,5,'2026-03-20 23:48:27','2026-03-20 23:50:17'),(13,3,1,2,1,5,'2026-03-20 23:52:11','2026-03-20 23:52:47'),(14,7,2,5,3,6,'2026-03-21 00:10:35','2026-03-21 00:13:44'),(15,31,1,2,1,5,'2026-03-21 00:16:52','2026-03-21 00:17:27'),(16,7,1,3,3,10,'2026-03-21 00:27:06','2026-03-21 00:28:51'),(17,18,1,2,1,5,'2026-03-21 00:30:27','2026-03-21 00:31:13'),(18,7,2,20,14,7,'2026-03-21 00:44:27','2026-03-21 00:48:59'),(19,7,3,3,2,6.66667,'2026-03-21 00:58:45','2026-03-21 00:59:48'),(20,7,3,3,3,10,'2026-03-21 01:49:14','2026-03-21 01:49:42'),(21,7,1,1,0,0,'2026-03-21 03:02:24','2026-03-21 03:02:47'),(22,7,4,5,4,8,'2026-03-21 03:35:08','2026-03-21 03:36:33'),(23,32,1,20,6,3,'2026-03-21 09:23:37','2026-03-21 09:24:40'),(24,32,1,20,13,6.5,'2026-03-21 09:23:37','2026-03-21 09:26:50'),(25,3,1,3,3,10,'2026-03-21 09:30:49','2026-03-21 09:31:53'),(26,32,1,3,3,10,'2026-03-21 09:34:36','2026-03-21 09:35:39'),(27,33,1,1,0,0,'2026-03-21 09:42:25','2026-03-21 09:44:26');
/*!40000 ALTER TABLE `bai_thi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bo_cau_hoi`
--

DROP TABLE IF EXISTS `bo_cau_hoi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bo_cau_hoi` (
  `bo_cau_hoi_id` int NOT NULL AUTO_INCREMENT,
  `ten_bo_cau_hoi` varchar(200) NOT NULL,
  `mo_ta` text,
  `cap_hoc_id` int DEFAULT NULL,
  `mon_hoc_id` int DEFAULT NULL,
  `nguoi_tao` int DEFAULT NULL,
  `trang_thai` varchar(20) DEFAULT 'CHO_DUYET',
  `cong_khai` tinyint DEFAULT '0',
  `ngay_tao` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`bo_cau_hoi_id`),
  KEY `cap_hoc_id` (`cap_hoc_id`),
  KEY `mon_hoc_id` (`mon_hoc_id`),
  KEY `nguoi_tao` (`nguoi_tao`),
  CONSTRAINT `bo_cau_hoi_ibfk_1` FOREIGN KEY (`cap_hoc_id`) REFERENCES `cap_hoc` (`cap_hoc_id`),
  CONSTRAINT `bo_cau_hoi_ibfk_2` FOREIGN KEY (`mon_hoc_id`) REFERENCES `mon_hoc` (`mon_hoc_id`),
  CONSTRAINT `bo_cau_hoi_ibfk_3` FOREIGN KEY (`nguoi_tao`) REFERENCES `nguoi_dung` (`nguoi_dung_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bo_cau_hoi`
--

LOCK TABLES `bo_cau_hoi` WRITE;
/*!40000 ALTER TABLE `bo_cau_hoi` DISABLE KEYS */;
INSERT INTO `bo_cau_hoi` VALUES (1,'AI Tạo: tạo bộ đề môn toán lớp 5','Bộ câu hỏi tạo tự động bởi AI',1,1,7,'DA_DUYET',1,'2026-03-20 14:27:19'),(2,'Bộ câu hỏi từ 20 câu chuyển đổi số chương 1,2.docx','Tự động tạo',4,2,1,'DA_DUYET',1,'2026-03-20 14:32:23'),(3,'AI Tạo: Bộ đề môn tiếng việt lớp 5','Bộ câu hỏi tạo tự động bởi AI',1,3,1,'DA_DUYET',1,'2026-03-20 15:52:30'),(4,'AI Tạo: bộ câu hỏi tin học lớp 9','Bộ câu hỏi tạo tự động bởi AI',2,5,7,'DA_DUYET',1,'2026-03-20 20:04:29');
/*!40000 ALTER TABLE `bo_cau_hoi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cap_hoc`
--

DROP TABLE IF EXISTS `cap_hoc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cap_hoc` (
  `cap_hoc_id` int NOT NULL AUTO_INCREMENT,
  `ten_cap_hoc` varchar(50) NOT NULL,
  PRIMARY KEY (`cap_hoc_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cap_hoc`
--

LOCK TABLES `cap_hoc` WRITE;
/*!40000 ALTER TABLE `cap_hoc` DISABLE KEYS */;
INSERT INTO `cap_hoc` VALUES (1,'Tiểu học'),(2,'THCS'),(3,'THPT'),(4,'Đại học');
/*!40000 ALTER TABLE `cap_hoc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cau_hoi`
--

DROP TABLE IF EXISTS `cau_hoi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cau_hoi` (
  `cau_hoi_id` int NOT NULL AUTO_INCREMENT,
  `bo_cau_hoi_id` int DEFAULT NULL,
  `noi_dung` text NOT NULL,
  `muc_do` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`cau_hoi_id`),
  KEY `bo_cau_hoi_id` (`bo_cau_hoi_id`),
  CONSTRAINT `cau_hoi_ibfk_1` FOREIGN KEY (`bo_cau_hoi_id`) REFERENCES `bo_cau_hoi` (`bo_cau_hoi_id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cau_hoi`
--

LOCK TABLES `cau_hoi` WRITE;
/*!40000 ALTER TABLE `cau_hoi` DISABLE KEYS */;
INSERT INTO `cau_hoi` VALUES (1,1,'Kết quả của phép tính 12,5 + 4,75 là bao nhiêu?','TB'),(2,1,'Tìm 25% của 200.','TB'),(3,1,'Một hình tam giác có độ dài đáy là 8cm và chiều cao là 5cm. Diện tích của hình tam giác đó là:','TB'),(4,1,'Chu vi của hình tròn có bán kính r = 3cm là:','TB'),(5,1,'Đổi 2 giờ 15 phút sang số phút:','TB'),(6,1,'Thể tích của hình lập phương có cạnh dài 4dm là:','TB'),(7,1,'Số thập phân gồm \'Bảy đơn vị, năm phần trăm\' được viết là:','TB'),(8,1,'Một ô tô đi được 90km trong 1,5 giờ. Vận tốc của ô tô là:','TB'),(9,1,'Giá trị của chữ số 5 trong số thập phân 12,354 là:','TB'),(10,1,'Kết quả của phép nhân 4,5 x 1,2 là:','TB'),(11,1,'Một hình thang có độ dài hai đáy lần lượt là 10cm và 6cm, chiều cao là 5cm. Diện tích hình thang đó là:','TB'),(12,1,'Phân số 4/5 được viết dưới dạng số thập phân là:','TB'),(13,1,'Biết 15% của một số là 45. Số đó là:','TB'),(14,1,'Đổi 1,2 m³ sang đơn vị dm³:','TB'),(15,1,'Diện tích toàn phần của hình lập phương có cạnh 2cm là:','TB'),(16,1,'Số thích hợp để điền vào chỗ chấm: 0,25 = ... %','TB'),(17,1,'Một hình hộp chữ nhật có chiều dài 5cm, chiều rộng 4cm và chiều cao 3cm. Thể tích của nó là:','TB'),(18,1,'Kết quả của phép tính 3/4 + 1/2 dưới dạng số thập phân là:','TB'),(19,1,'Tìm x, biết: x - 3,2 = 5,8','TB'),(20,1,'Trung bình cộng của ba số 10, 20 và 30 là:','TB'),(21,2,'Quá trình chuyển đổi thông tin từ dạng vật lý hoặc analog sang dạng số để lưu trữ, xử lý bằng máy tính được gọi là gì?','TB'),(22,2,'Việc ứng dụng công nghệ thông tin để tự động hóa các quy trình công việc, nhưng không làm thay đổi bản chất hoạt động được gọi là gì?','TB'),(23,2,'Khái niệm nào đề cập đến quá trình thay đổi toàn diện mô hình tổ chức, hoạt động, quy trình, văn hóa dựa trên công nghệ số nhằm nâng cao hiệu quả và giá trị?','TB'),(24,2,'Ví dụ nào sau đây minh họa cho quá trình Số hóa?','TB'),(25,2,'Ví dụ nào sau đây minh họa cho quá trình Tin học hóa?','TB'),(26,2,'Lợi ích nào của Chuyển đổi số đối với Chính phủ, cơ quan Nhà nước được đề cập trong bài giảng?','TB'),(27,2,'Cơ hội nào sau đây thể hiện việc Chuyển đổi số giúp tăng khả năng cạnh tranh và đổi mới sáng tạo?','TB'),(28,2,'Thách thức nào sau đây là rủi ro hiện hữu đòi hỏi phải đầu tư mạnh vào an ninh mạng?','TB'),(29,2,'Trong quy trình triển khai chiến lược Chuyển đổi số, bước nào giúp tổ chức xác định điểm mạnh – điểm yếu – cơ hội – thách thức (SWOT)?','TB'),(30,2,'Xu hướng nào của Chuyển đổi số trong thời đại 4.0 sử dụng AI, dữ liệu lớn và tự động hóa ra quyết định?','TB'),(31,2,'Đặc điểm nào của Blockchain cho phép dữ liệu một khi đã ghi vào chuỗi thì không thể bị sửa đổi?','TB'),(32,2,'Cấp độ nào trong Chuyển đổi số mô tả việc các hệ thống được liên kết, chia sẻ dữ liệu với nhau?','TB'),(33,2,'Cấp độ 5 trong Chuyển đổi số có đặc điểm gì?','TB'),(34,2,'Yếu tố nào được xem là then chốt, là người định hướng và đưa ra quyết sách cho quá trình Chuyển đổi số?','TB'),(35,2,'Văn hóa nào được đề cập là rào cản lớn trong quá trình Chuyển đổi số?','TB'),(36,2,'Theo tài liệu, Chính phủ số hoạt động dựa trên mấy trụ cột?','TB'),(37,2,'Mục tiêu nào là của Chuyển đổi số trong lĩnh vực công?','TB'),(38,2,'Xu hướng Chuyển đổi xanh – Số hóa gắn với phát triển bền vững không bao gồm hành động nào?','TB'),(39,2,'Việc phân tích dữ liệu hành vi người dùng để đưa ra các gợi ý sản phẩm phù hợp là cơ hội nào của Chuyển đổi số?','TB'),(40,2,'Thách thức nào liên quan đến việc hệ thống cũ khó tích hợp với nền tảng mới và thiếu đồng bộ về phần cứng/phần mềm?','TB'),(41,3,'Từ nào sau đây đồng nghĩa với từ \'hòa bình\'?','TB'),(42,3,'Trong câu \'Sáng sớm, bà em đi chợ.\', dấu phẩy có tác dụng gì?','TB'),(43,3,'Từ \'quyết tâm\' thuộc từ loại nào?','TB'),(44,3,'Từ nào trái nghĩa với từ \'thật thà\'?','TB'),(45,3,'Câu nào dưới đây là câu ghép?','TB'),(46,3,'Từ nào viết đúng chính tả?','TB'),(47,3,'Điền từ thích hợp vào thành ngữ: \'Kề vai... cánh\'','TB'),(48,3,'Đại từ trong câu \'Chúng tôi đang học bài.\' là từ nào?','TB'),(49,3,'Từ \'câu\' trong \'câu cá\' và \'câu văn\' có mối quan hệ gì?','TB'),(50,3,'Từ \'chân\' trong cụm từ \'chân núi\' được dùng với nghĩa gì?','TB'),(51,3,'Chủ ngữ trong câu \'Những bông hoa hồng đang tỏa hương thơm ngát.\' là gì?','TB'),(52,3,'Cặp quan hệ từ \'Mặc dù... nhưng...\' biểu thị quan hệ gì?','TB'),(53,3,'Câu \'Trăng tròn như cái đĩa.\' sử dụng biện pháp nghệ thuật nào?','TB'),(54,3,'Câu nào dưới đây sử dụng biện pháp nhân hóa?','TB'),(55,3,'Từ nào sau đây là từ láy?','TB'),(56,3,'Nhóm từ nào chỉ gồm các từ ghép?','TB'),(57,3,'Bài thơ \'Hạt gạo làng ta\' là của tác giả nào?','TB'),(58,3,'Câu \'Bạn đang làm gì đấy?\' thuộc kiểu câu nào?','TB'),(59,3,'Trong câu \'Em rất yêu quý ông bà.\', từ \'yêu quý\' là:','TB'),(60,3,'Tìm từ điền vào chỗ trống: \'Ăn quả nhớ kẻ...\'','TB'),(61,4,'Mạng máy tính là tập hợp các máy tính được kết nối với nhau theo một phương thức nào đó nhằm mục đích gì?','TB'),(62,4,'Thiết bị nào sau đây đóng vai trò trung tâm để kết nối các máy tính trong mạng hình sao (Star topology)?','TB'),(63,4,'Giao thức truyền thông phổ biến nhất được sử dụng trên Internet hiện nay là gì?','TB'),(64,4,'Trong các địa chỉ sau, đâu là địa chỉ của một trang web (URL)?','TB'),(65,4,'Dịch vụ nào của Internet cho phép người dùng gửi và nhận thư qua mạng?','TB'),(66,4,'Phần mềm nào sau đây là trình duyệt web?','TB'),(67,4,'Khái niệm \'Đa phương tiện\' (Multimedia) được hiểu là gì?','TB'),(68,4,'Trong Microsoft PowerPoint, \'Transition\' dùng để làm gì?','TB'),(69,4,'Đâu là một ví dụ về phần mềm mã nguồn mở?','TB'),(70,4,'Mạng WAN (Wide Area Network) khác mạng LAN ở đặc điểm chính nào?','TB'),(71,4,'Virus máy tính thực chất là gì?','TB'),(72,4,'Khi tìm kiếm thông tin trên Google, để tìm chính xác một cụm từ, ta nên để cụm từ đó trong ký hiệu nào?','TB'),(73,4,'Tệp tin có đuôi mở rộng là .mp4 thường chứa loại dữ liệu nào?','TB'),(74,4,'Trong trình chiếu, để bắt đầu trình diễn từ slide đầu tiên, ta nhấn phím nào trên bàn phím?','TB'),(75,4,'Phần mềm nào sau đây hỗ trợ tạo trang web đơn giản?','TB'),(76,4,'Thuật ngữ \'Cloud Computing\' trong tin học có nghĩa là gì?','TB'),(77,4,'Tại sao cần phải sao lưu dữ liệu (Backup) định kỳ?','TB'),(78,4,'Lợi ích của việc sử dụng mạng máy tính trong nhà trường là gì?','TB'),(79,4,'Trang web nào sau đây được coi là một công cụ tìm kiếm?','TB'),(80,4,'Để bảo vệ thông tin cá nhân trên mạng, người dùng nên làm gì?','TB');
/*!40000 ALTER TABLE `cau_hoi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cau_hoi_yeu_thich`
--

DROP TABLE IF EXISTS `cau_hoi_yeu_thich`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cau_hoi_yeu_thich` (
  `nguoi_dung_id` int NOT NULL,
  `cau_hoi_id` int NOT NULL,
  PRIMARY KEY (`nguoi_dung_id`,`cau_hoi_id`),
  KEY `cau_hoi_id` (`cau_hoi_id`),
  CONSTRAINT `cau_hoi_yeu_thich_ibfk_1` FOREIGN KEY (`nguoi_dung_id`) REFERENCES `nguoi_dung` (`nguoi_dung_id`),
  CONSTRAINT `cau_hoi_yeu_thich_ibfk_2` FOREIGN KEY (`cau_hoi_id`) REFERENCES `cau_hoi` (`cau_hoi_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cau_hoi_yeu_thich`
--

LOCK TABLES `cau_hoi_yeu_thich` WRITE;
/*!40000 ALTER TABLE `cau_hoi_yeu_thich` DISABLE KEYS */;
INSERT INTO `cau_hoi_yeu_thich` VALUES (33,11),(7,12),(7,13),(20,13),(7,20),(7,30),(7,44);
/*!40000 ALTER TABLE `cau_hoi_yeu_thich` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chi_tiet_bai_thi`
--

DROP TABLE IF EXISTS `chi_tiet_bai_thi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chi_tiet_bai_thi` (
  `bai_thi_id` int NOT NULL,
  `cau_hoi_id` int NOT NULL,
  `dap_an_da_chon` int DEFAULT NULL,
  `dung` tinyint DEFAULT NULL,
  PRIMARY KEY (`bai_thi_id`,`cau_hoi_id`),
  KEY `cau_hoi_id` (`cau_hoi_id`),
  KEY `dap_an_da_chon` (`dap_an_da_chon`),
  CONSTRAINT `chi_tiet_bai_thi_ibfk_1` FOREIGN KEY (`bai_thi_id`) REFERENCES `bai_thi` (`bai_thi_id`),
  CONSTRAINT `chi_tiet_bai_thi_ibfk_2` FOREIGN KEY (`cau_hoi_id`) REFERENCES `cau_hoi` (`cau_hoi_id`),
  CONSTRAINT `chi_tiet_bai_thi_ibfk_3` FOREIGN KEY (`dap_an_da_chon`) REFERENCES `dap_an` (`dap_an_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chi_tiet_bai_thi`
--

LOCK TABLES `chi_tiet_bai_thi` WRITE;
/*!40000 ALTER TABLE `chi_tiet_bai_thi` DISABLE KEYS */;
INSERT INTO `chi_tiet_bai_thi` VALUES (1,2,7,1),(1,3,10,1),(1,4,13,1),(1,8,29,1),(1,11,43,1),(1,12,47,1),(1,13,49,1),(1,15,58,1),(1,18,69,1),(1,19,75,1),(2,32,127,1),(2,33,132,1),(2,37,147,1),(3,21,82,1),(3,29,115,0),(3,34,135,1),(4,6,22,1),(4,8,29,1),(4,11,43,1),(4,13,49,1),(4,14,55,1),(4,15,58,1),(4,16,63,1),(4,17,68,0),(4,19,75,1),(4,20,78,1),(5,1,2,1),(5,4,15,0),(5,6,24,0),(5,8,29,1),(5,11,43,1),(5,13,49,1),(5,15,58,1),(5,16,63,1),(5,18,69,1),(5,19,75,1),(6,43,171,0),(6,45,180,0),(6,48,191,1),(6,49,196,0),(6,51,202,1),(6,52,205,0),(6,54,214,1),(6,56,222,1),(6,59,234,1),(6,60,239,1),(7,43,170,1),(7,51,202,1),(7,52,207,1),(7,54,214,1),(7,56,222,1),(9,4,15,0),(9,13,49,1),(10,20,78,1),(11,7,28,1),(11,16,63,1),(11,20,78,1),(12,3,9,0),(12,14,55,1),(13,3,9,0),(13,12,47,1),(14,22,85,0),(14,25,98,1),(14,30,119,1),(14,37,147,1),(14,39,156,0),(15,15,58,1),(15,17,68,0),(16,5,19,1),(16,14,55,1),(16,16,63,1),(17,6,24,0),(17,13,49,1),(18,21,82,1),(18,22,86,1),(18,23,91,1),(18,24,96,0),(18,25,98,1),(18,26,101,1),(18,27,107,1),(18,28,109,0),(18,29,115,0),(18,30,119,1),(18,31,122,1),(18,32,127,1),(18,33,132,1),(18,34,135,1),(18,35,139,1),(18,36,142,1),(18,37,147,1),(18,38,150,0),(18,39,153,0),(18,40,160,0),(19,42,165,0),(19,44,175,1),(19,48,191,1),(20,45,178,1),(20,54,214,1),(20,56,222,1),(21,11,41,0),(22,61,242,1),(22,62,247,1),(22,63,249,0),(22,75,297,1),(22,78,310,1),(23,17,66,1),(24,1,2,1),(24,2,8,0),(24,3,12,0),(24,4,13,1),(24,5,19,1),(24,6,21,0),(24,7,28,1),(24,8,29,1),(24,9,35,1),(24,10,40,0),(24,11,43,1),(24,12,48,0),(24,13,49,1),(24,14,55,1),(24,15,58,1),(24,16,64,0),(24,17,66,1),(24,18,71,0),(24,19,75,1),(24,20,78,1),(25,2,7,1),(25,4,13,1),(25,9,35,1),(26,4,13,1),(26,12,47,1),(26,13,49,1),(27,11,41,0);
/*!40000 ALTER TABLE `chi_tiet_bai_thi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dap_an`
--

DROP TABLE IF EXISTS `dap_an`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dap_an` (
  `dap_an_id` int NOT NULL AUTO_INCREMENT,
  `cau_hoi_id` int DEFAULT NULL,
  `noi_dung` text NOT NULL,
  `dung` tinyint DEFAULT '0',
  PRIMARY KEY (`dap_an_id`),
  KEY `cau_hoi_id` (`cau_hoi_id`),
  CONSTRAINT `dap_an_ibfk_1` FOREIGN KEY (`cau_hoi_id`) REFERENCES `cau_hoi` (`cau_hoi_id`)
) ENGINE=InnoDB AUTO_INCREMENT=321 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dap_an`
--

LOCK TABLES `dap_an` WRITE;
/*!40000 ALTER TABLE `dap_an` DISABLE KEYS */;
INSERT INTO `dap_an` VALUES (1,1,'16,25',0),(2,1,'17,25',1),(3,1,'17,35',0),(4,1,'16,35',0),(5,2,'40',0),(6,2,'60',0),(7,2,'50',1),(8,2,'25',0),(9,3,'40 cm²',0),(10,3,'20 cm²',1),(11,3,'13 cm²',0),(12,3,'26 cm²',0),(13,4,'18,84 cm',1),(14,4,'9,42 cm',0),(15,4,'28,26 cm',0),(16,4,'15,7 cm',0),(17,5,'125 phút',0),(18,5,'215 phút',0),(19,5,'135 phút',1),(20,5,'150 phút',0),(21,6,'16 dm³',0),(22,6,'64 dm³',1),(23,6,'32 dm³',0),(24,6,'96 dm³',0),(25,7,'7,5',0),(26,7,'7,50',0),(27,7,'7,005',0),(28,7,'7,05',1),(29,8,'60 km/giờ',1),(30,8,'45 km/giờ',0),(31,8,'50 km/giờ',0),(32,8,'70 km/giờ',0),(33,9,'5 đơn vị',0),(34,9,'5 phần mười',0),(35,9,'5 phần trăm',1),(36,9,'5 phần nghìn',0),(37,10,'54',0),(38,10,'5,4',1),(39,10,'0,54',0),(40,10,'5,42',0),(41,11,'80 cm²',0),(42,11,'50 cm²',0),(43,11,'40 cm²',1),(44,11,'30 cm²',0),(45,12,'0,4',0),(46,12,'0,5',0),(47,12,'0,8',1),(48,12,'4,5',0),(49,13,'300',1),(50,13,'675',0),(51,13,'200',0),(52,13,'30',0),(53,14,'12 dm³',0),(54,14,'120 dm³',0),(55,14,'1200 dm³',1),(56,14,'12000 dm³',0),(57,15,'16 cm²',0),(58,15,'24 cm²',1),(59,15,'8 cm²',0),(60,15,'20 cm²',0),(61,16,'0,25%',0),(62,16,'2,5%',0),(63,16,'25%',1),(64,16,'250%',0),(65,17,'12 cm³',0),(66,17,'60 cm³',1),(67,17,'20 cm³',0),(68,17,'94 cm³',0),(69,18,'1,25',1),(70,18,'0,75',0),(71,18,'1,5',0),(72,18,'0,85',0),(73,19,'2,6',0),(74,19,'8,0',0),(75,19,'9,0',1),(76,19,'8,1',0),(77,20,'15',0),(78,20,'20',1),(79,20,'25',0),(80,20,'60',0),(81,21,'Tin học hóa',0),(82,21,'Số hóa',1),(83,21,'Chuyển đổi số',0),(84,21,'Phân tích dữ liệu',0),(85,22,'Số hóa',0),(86,22,'Tin học hóa',1),(87,22,'Chuyển đổi số',0),(88,22,'Tổ chức thông minh',0),(89,23,'Số hóa',0),(90,23,'Tin học hóa',0),(91,23,'Chuyển đổi số',1),(92,23,'Xây dựng chiến lược',0),(93,24,'Dùng phần mềm quản lý điểm thay cho sổ tay',0),(94,24,'Triển khai hệ thống học trực tuyến LMS',0),(95,24,'Máy quét tài liệu giấy thành file PDF',1),(96,24,'Doanh nghiệp sử dụng chatbot chăm sóc khách hàng 24/7',0),(97,25,'Chuyển tiền thông qua ngân hàng số',0),(98,25,'Dùng phần mềm Excel để quản lý điểm sinh viên thay vì ghi sổ tay truyền thống',1),(99,25,'Một trường đại học triển khai hệ thống học trực tuyến LMS',0),(100,25,'Một doanh nghiệp mở rộng sang thương mại điện tử',0),(101,26,'Cải cách thủ tục hành chính theo hướng tinh gọn, nhanh chóng, chính xác, minh bạch',1),(102,26,'Tối ưu hóa quy trình, tiết kiệm chi phí',0),(103,26,'Mở rộng thị trường và tạo mô hình kinh doanh mới',0),(104,26,'Cá nhân hóa dịch vụ',0),(105,27,'Tự động hóa quy trình giúp giảm chi phí',0),(106,27,'Cá nhân hóa dịch vụ dựa trên dữ liệu',0),(107,27,'Dữ liệu lớn và AI cho phép tổ chức phát hiện xu hướng, tối ưu sản phẩm',1),(108,27,'Mô hình kinh doanh số vận hành xuyên biên giới',0),(109,28,'Thiếu nguồn lực về con người và kỹ năng số',0),(110,28,'Hạ tầng và công nghệ chưa đáp ứng',0),(111,28,'Chi phí đầu tư ban đầu cao',0),(112,28,'Rủi ro về bảo mật và an toàn thông tin',1),(113,29,'Nhận thức và cam kết từ lãnh đạo',0),(114,29,'Đánh giá hiện trạng số hóa',1),(115,29,'Xác định tầm nhìn và chiến lược chuyển đổi số',0),(116,29,'Xây dựng kế hoạch hành động cụ thể',0),(117,30,'Ứng dụng điện toán đám mây',0),(118,30,'Blockchain và niềm tin số',0),(119,30,'Chuyển đổi sang mô hình tổ chức thông minh',1),(120,30,'Di động hóa và trải nghiệm đa kênh',0),(121,31,'Minh bạch',0),(122,31,'Bất biến',1),(123,31,'Phân tán',0),(124,31,'An toàn',0),(125,32,'Cấp độ 1: Tin học hóa/Số hóa cơ bản',0),(126,32,'Cấp độ 2: Số hóa quy trình',0),(127,32,'Cấp độ 3: Tích hợp số và kết nối dữ liệu',1),(128,32,'Cấp độ 4: Tổ chức vận hành theo mô hình số',0),(129,33,'Các quy trình được hỗ trợ bởi phần mềm, nhưng chưa tích hợp',0),(130,33,'Chuyển tài liệu giấy sang số, sử dụng phần mềm đơn lẻ',0),(131,33,'Quy trình vận hành linh hoạt theo thời gian thực',0),(132,33,'Tổ chức vận hành chủ yếu dựa vào dữ liệu, AI và tự động hóa',1),(133,34,'Nguồn lực tài chính',0),(134,34,'Nguồn nhân lực và năng lực số',0),(135,34,'Chiến lược và cam kết của lãnh đạo',1),(136,34,'Hạ tầng và hệ thống công nghệ',0),(137,35,'Văn hóa đổi mới liên tục',0),(138,35,'Văn hóa linh hoạt, sẵn sàng học hỏi',0),(139,35,'Văn hóa chống thay đổi, bảo thủ',1),(140,35,'Văn hóa làm việc trực tuyến',0),(141,36,'2',0),(142,36,'3 (Chính quyền số, Kinh tế số, Xã hội số)',1),(143,36,'4',0),(144,36,'5',0),(145,37,'Tối ưu hóa lợi nhuận cho doanh nghiệp.',0),(146,37,'Phát triển phần mềm và thiết bị theo hướng tiêu hao ít năng lượng.',0),(147,37,'Cung cấp dịch vụ công nhanh chóng, minh bạch, không giấy tờ.',1),(148,37,'Khai thác dữ liệu thô.',0),(149,38,'Sử dụng công nghệ để tiết kiệm năng lượng.',0),(150,38,'Khuyến khích hạ tầng công nghệ xanh (trung tâm dữ liệu tiết kiệm điện).',0),(151,38,'Tăng cường sử dụng giấy tờ in ấn thay thế văn bản điện tử.',1),(152,38,'Quản lý rác thải điện tử, tái chế thiết bị công nghệ cũ.',0),(153,39,'Nâng cao hiệu quả hoạt động và quản trị.',0),(154,39,'Cải thiện trải nghiệm khách hàng.',1),(155,39,'Mở rộng thị trường và tạo mô hình kinh doanh mới.',0),(156,39,'Tăng khả năng cạnh tranh và đổi mới sáng tạo.',0),(157,40,'Thiếu nguồn lực về con người và kỹ năng số.',0),(158,40,'Hạ tầng và công nghệ chưa đáp ứng.',1),(159,40,'Chi phí đầu tư ban đầu cao.',0),(160,40,'Rủi ro về bảo mật và an toàn thông tin.',0),(161,41,'Chiến tranh',0),(162,41,'Thanh bình',1),(163,41,'Xung đột',0),(164,41,'Ồn ào',0),(165,42,'Ngăn cách các vế câu ghép',0),(166,42,'Ngăn cách các từ cùng chức vụ',0),(167,42,'Ngăn cách trạng ngữ với chủ ngữ và vị ngữ',1),(168,42,'Dùng để dẫn lời nói trực tiếp',0),(169,43,'Danh từ',0),(170,43,'Động từ',1),(171,43,'Tính từ',0),(172,43,'Đại từ',0),(173,44,'Hiền lành',0),(174,44,'Chăm chỉ',0),(175,44,'Gian dối',1),(176,44,'Dũng cảm',0),(177,45,'Lan và Huệ là đôi bạn thân.',0),(178,45,'Vì trời mưa nên đường rất trơn.',1),(179,45,'Mẹ em đang nấu cơm trong bếp.',0),(180,45,'Những đóa hoa hồng tỏa hương thơm ngát.',0),(181,46,'Xuất sắc',1),(182,46,'Suất sắc',0),(183,46,'Xuất sấc',0),(184,46,'Suất sấc',0),(185,47,'Sát',1),(186,47,'Bên',0),(187,47,'Gần',0),(188,47,'Áp',0),(189,48,'Đang',0),(190,48,'Học bài',0),(191,48,'Chúng tôi',1),(192,48,'Bài',0),(193,49,'Từ đồng nghĩa',0),(194,49,'Từ trái nghĩa',0),(195,49,'Từ đồng âm',1),(196,49,'Từ nhiều nghĩa',0),(197,50,'Nghĩa gốc',0),(198,50,'Nghĩa chuyển',1),(199,50,'Nghĩa đen',0),(200,50,'Nghĩa hẹp',0),(201,51,'Hoa hồng',0),(202,51,'Những bông hoa hồng',1),(203,51,'Tỏa hương thơm ngát',0),(204,51,'Đang tỏa hương',0),(205,52,'Nguyên nhân - Kết quả',0),(206,52,'Điều kiện - Kết quả',0),(207,52,'Tương phản',1),(208,52,'Tăng tiến',0),(209,53,'Nhân hóa',0),(210,53,'So sánh',1),(211,53,'Ẩn dụ',0),(212,53,'Hoán dụ',0),(213,54,'Bầu trời xanh ngắt.',0),(214,54,'Chị Gió đang dạo chơi trên đồng cỏ.',1),(215,54,'Cây cối xanh tươi.',0),(216,54,'Hoa mười giờ nở rộ.',0),(217,55,'Học tập',0),(218,55,'Bạn bè',0),(219,55,'Lung linh',1),(220,55,'Cây cối',0),(221,56,'Xinh xắn, ngoan ngoãn',0),(222,56,'Quần áo, sách vở',1),(223,56,'Lấp lánh, mặn mà',0),(224,56,'Hối hả, rầm rộ',0),(225,57,'Trần Đăng Khoa',1),(226,57,'Huy Cận',0),(227,57,'Xuân Quỳnh',0),(228,57,'Tố Hữu',0),(229,58,'Câu kể',0),(230,58,'Câu khiến',0),(231,58,'Câu cảm',0),(232,58,'Câu hỏi',1),(233,59,'Danh từ',0),(234,59,'Động từ',1),(235,59,'Tính từ',0),(236,59,'Quan hệ từ',0),(237,60,'Gieo hạt',0),(238,60,'Tưới cây',0),(239,60,'Trồng cây',1),(240,60,'Hái quả',0),(241,61,'Để tăng tốc độ xử lý của CPU',0),(242,61,'Để chia sẻ dữ liệu và dùng chung các thiết bị phần cứng',1),(243,61,'Để thay thế hoàn toàn con người trong việc quản lý',0),(244,61,'Để tăng dung lượng bộ nhớ RAM của từng máy',0),(245,62,'Modem',0),(246,62,'Router',0),(247,62,'Switch hoặc Hub',1),(248,62,'Card mạng',0),(249,63,'HTTP/HTTPS',0),(250,63,'FTP',0),(251,63,'TCP/IP',1),(252,63,'SMTP',0),(253,64,'nguyenvana@gmail.com',0),(254,64,'192.168.1.1',0),(255,64,'\\Windows\\System32',0),(256,64,'https://www.moet.gov.vn',1),(257,65,'E-commerce',0),(258,65,'E-mail',1),(259,65,'FTP',0),(260,65,'Telnet',0),(261,66,'Microsoft Word',0),(262,66,'Google Chrome',1),(263,66,'Adobe Photoshop',0),(264,66,'Windows Media Player',0),(265,67,'Sử dụng nhiều máy tính cùng một lúc',0),(266,67,'Sự kết hợp nhiều dạng thông tin như văn bản, âm thanh, hình ảnh, video',1),(267,67,'Một loại máy tính có cấu hình cực mạnh',0),(268,67,'Các chương trình phần mềm chạy trên điện thoại',0),(269,68,'Tạo hiệu ứng chuyển động cho các đối tượng trên slide',0),(270,68,'Thay đổi màu nền của tất cả các slide',0),(271,68,'Tạo hiệu ứng chuyển đổi giữa các slide',1),(272,68,'Chèn thêm bảng biểu vào nội dung slide',0),(273,69,'Microsoft Windows',0),(274,69,'Adobe Illustrator',0),(275,69,'Hệ điều hành Linux',1),(276,69,'Phần mềm diệt virus Kaspersky',0),(277,70,'Sử dụng cáp quang tốc độ cao hơn',0),(278,70,'Phạm vi địa lý kết nối rộng lớn hơn',1),(279,70,'Chỉ dùng cho các cơ quan chính phủ',0),(280,70,'Không thể kết nối Internet',0),(281,71,'Một loại vi khuẩn sinh học lây qua bàn phím',0),(282,71,'Một đoạn mã chương trình có khả năng tự nhân bản và gây hại',1),(283,71,'Sự hỏng hóc vật lý của ổ cứng',0),(284,71,'Bụi bẩn bám vào các linh kiện điện tử',0),(285,72,'Dấu ngoặc đơn ( )',0),(286,72,'Dấu ngoặc vuông [ ]',0),(287,72,'Dấu ngoặc kép \" \"',1),(288,72,'Dấu ngoặc nhọn { }',0),(289,73,'Văn bản thuần túy',0),(290,73,'Âm thanh (nhạc)',0),(291,73,'Hình ảnh tĩnh',0),(292,73,'Phim hoặc video',1),(293,74,'F1',0),(294,74,'F5',1),(295,74,'Shift + F5',0),(296,74,'Enter',0),(297,75,'KompoZer',1),(298,75,'Excel',0),(299,75,'PowerPoint',0),(300,75,'Calculator',0),(301,76,'Máy tính có màu xanh da trời',0),(302,76,'Điện toán đám mây',1),(303,76,'Kỹ thuật dự báo thời tiết',0),(304,76,'Mạng máy tính không dây',0),(305,77,'Để giải phóng dung lượng ổ cứng',0),(306,77,'Để máy tính chạy nhanh hơn',0),(307,77,'Để tránh mất mát thông tin khi có sự cố phần cứng hoặc virus',1),(308,77,'Để thay đổi định dạng của tệp tin',0),(309,78,'Giúp giáo viên quản lý học sinh chặt chẽ hơn qua camera',0),(310,78,'Hỗ trợ tra cứu tài liệu, học tập trực tuyến và trao đổi bài giảng',1),(311,78,'Tăng tiền điện hàng tháng của trường',0),(312,78,'Để học sinh chơi game trong giờ giải lao',0),(313,79,'Facebook.com',0),(314,79,'Youtube.com',0),(315,79,'Google.com',1),(316,79,'Lazada.vn',0),(317,80,'Cung cấp mật khẩu cho bất kỳ ai hỏi',0),(318,80,'Sử dụng mật khẩu mạnh và không chia sẻ thông tin nhạy cảm',1),(319,80,'Đăng mọi hoạt động hàng ngày lên mạng xã hội',0),(320,80,'Không bao giờ cập nhật phần mềm diệt virus',0);
/*!40000 ALTER TABLE `dap_an` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mon_hoc`
--

DROP TABLE IF EXISTS `mon_hoc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mon_hoc` (
  `mon_hoc_id` int NOT NULL AUTO_INCREMENT,
  `ten_mon_hoc` varchar(100) NOT NULL,
  PRIMARY KEY (`mon_hoc_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mon_hoc`
--

LOCK TABLES `mon_hoc` WRITE;
/*!40000 ALTER TABLE `mon_hoc` DISABLE KEYS */;
INSERT INTO `mon_hoc` VALUES (1,'Toán lớp 5'),(2,'Chuyển đổi số'),(3,'Tiếng việt lớp 5'),(5,'Tin học lớp 9');
/*!40000 ALTER TABLE `mon_hoc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nguoi_dung`
--

DROP TABLE IF EXISTS `nguoi_dung`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nguoi_dung` (
  `nguoi_dung_id` int NOT NULL AUTO_INCREMENT,
  `ten_dang_nhap` varchar(50) NOT NULL,
  `mat_khau` varchar(255) NOT NULL,
  `ho_ten` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `vai_tro_id` int DEFAULT NULL,
  `trang_thai` tinyint DEFAULT '1',
  `ngay_tao` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`nguoi_dung_id`),
  UNIQUE KEY `ten_dang_nhap` (`ten_dang_nhap`),
  KEY `vai_tro_id` (`vai_tro_id`),
  CONSTRAINT `nguoi_dung_ibfk_1` FOREIGN KEY (`vai_tro_id`) REFERENCES `vai_tro` (`vai_tro_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nguoi_dung`
--

LOCK TABLES `nguoi_dung` WRITE;
/*!40000 ALTER TABLE `nguoi_dung` DISABLE KEYS */;
INSERT INTO `nguoi_dung` VALUES (1,'admin','12345678','Quản Trị Viên Hệ Thống Thi','lenguyenthaib@gmail.com',1,1,'2026-01-08 19:30:11'),(2,'user1','123456','Nguyễn Văn Anh Tài','nguyenvana@gmail.com',2,1,'2026-01-08 19:30:11'),(3,'user2','123456','Trần Thị Báo','thanthib@gmail.com',2,1,'2026-01-08 19:30:11'),(7,'thaibao','123456','Lê Nguyễn Thái Bảo','lntb230104@gmail.com',2,1,'2026-01-08 22:40:28'),(18,'kieungan','1234567','Lê Nguyễn Kiều Ngân','kieungan838685@gmail.com',2,1,'2026-01-10 19:07:47'),(20,'hoaiem','123456','Mai Chí Hoài Em','Emmaichihoaiem@gmail.com',2,1,'2026-01-13 17:19:07'),(21,'quockhanh','123456','Lê Quốc Khanh','lequockhanh@gmail.com',2,1,'2026-01-15 20:44:53'),(23,'lenguyenthaibao','123456','Lê Nguyễn Thái Bảo','lenguyenthaibo@gmail.com',1,1,'2026-01-18 20:29:20'),(24,'lenguyenkieungan','123456','Lê Nguyễn Kiều Ngân','kieungan@gmail.com',2,1,'2026-01-18 21:04:23'),(30,'lebao','123456','Lê Bảo','lebao@gmail.com',2,1,'2026-03-18 11:17:07'),(31,'lebinh','123456','Lê Thành Long BÌnh','lebinh@gmail.com',2,1,'2026-03-20 17:08:08'),(32,'Tantai','123456','Tấn Tài','vutantai2018@gmail.com',2,1,'2026-03-21 02:22:29'),(33,'pong','pong111','pong','phongwary2004@gmail.com',2,1,'2026-03-21 02:41:48');
/*!40000 ALTER TABLE `nguoi_dung` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vai_tro`
--

DROP TABLE IF EXISTS `vai_tro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vai_tro` (
  `vai_tro_id` int NOT NULL AUTO_INCREMENT,
  `ten_vai_tro` varchar(50) NOT NULL,
  PRIMARY KEY (`vai_tro_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vai_tro`
--

LOCK TABLES `vai_tro` WRITE;
/*!40000 ALTER TABLE `vai_tro` DISABLE KEYS */;
INSERT INTO `vai_tro` VALUES (1,'ADMIN'),(2,'USER');
/*!40000 ALTER TABLE `vai_tro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'tracnghiem'
--

--
-- Dumping routines for database 'tracnghiem'
--
--
-- WARNING: can't read the INFORMATION_SCHEMA.libraries table. It's most probably an old server 8.0.45.
--
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-21 10:18:24
