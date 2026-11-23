-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.30 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for dbtokodesktop
CREATE DATABASE IF NOT EXISTS `dbtokodesktop` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `dbtokodesktop`;

-- Dumping structure for table dbtokodesktop.barang
CREATE TABLE IF NOT EXISTS `barang` (
  `idBarang` int NOT NULL AUTO_INCREMENT,
  `namaBarang` varchar(50) DEFAULT NULL,
  `kategori` varchar(50) DEFAULT NULL,
  `beratGram` int NOT NULL DEFAULT '250',
  `modal` decimal(12,2) NOT NULL,
  `hargaJual` decimal(12,2) NOT NULL,
  `stok` int NOT NULL,
  `idPemasok` int DEFAULT NULL,
  PRIMARY KEY (`idBarang`),
  KEY `fk_barang_pemasok` (`idPemasok`),
  CONSTRAINT `fk_barang_pemasok` FOREIGN KEY (`idPemasok`) REFERENCES `pemasok` (`idPemasok`),
  CONSTRAINT `barang_chk_1` CHECK ((`stok` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=260 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table dbtokodesktop.barang: ~11 rows (approximately)
INSERT INTO `barang` (`idBarang`, `namaBarang`, `kategori`, `beratGram`, `modal`, `hargaJual`, `stok`, `idPemasok`) VALUES
	(204, 'Lada hitam halus', 'Halus', 250, 13750.00, 17500.00, 251, 2),
	(206, 'Ketumbar halus', 'Halus', 250, 6250.00, 8750.00, 260, 4),
	(218, 'Cabai halus', 'Halus', 250, 12000.00, 15000.00, 209, 6),
	(220, 'Jahe halus', 'Halus', 250, 6000.00, 8000.00, 279, 3),
	(221, 'Kunyit halus', 'Halus', 250, 5000.00, 7000.00, 366, 4),
	(224, 'Lada hitam biji', 'Bulat', 250, 12000.00, 15000.00, 274, 2),
	(239, 'Bawang putih kering', 'Bulat', 250, 8750.00, 11250.00, 147, 2),
	(253, 'Kencur kering', 'Bulat', 250, 9000.00, 12000.00, 40, 2),
	(254, 'Kelapa gongseng', 'Halus', 250, 9500.00, 12500.00, 35, 3),
	(255, 'Kemiri', 'Bulat', 250, 7500.00, 10000.00, 50, 4),
	(256, 'Vetsin', 'Halus', 250, 9500.00, 12500.00, 45, 5);

-- Dumping structure for table dbtokodesktop.detail_transaksi
CREATE TABLE IF NOT EXISTS `detail_transaksi` (
  `idDetail` int NOT NULL AUTO_INCREMENT,
  `idTransaksi` int NOT NULL,
  `idBarang` int NOT NULL,
  `namaBarang` varchar(50) NOT NULL,
  `qty` int NOT NULL,
  `beratGram` int NOT NULL,
  `hargaSatuan` decimal(12,2) NOT NULL,
  `subtotal` decimal(12,2) NOT NULL,
  PRIMARY KEY (`idDetail`),
  KEY `idTransaksi` (`idTransaksi`),
  KEY `idBarang` (`idBarang`),
  CONSTRAINT `detail_transaksi_ibfk_1` FOREIGN KEY (`idTransaksi`) REFERENCES `transaksi` (`idTransaksi`),
  CONSTRAINT `detail_transaksi_ibfk_2` FOREIGN KEY (`idBarang`) REFERENCES `barang` (`idBarang`),
  CONSTRAINT `detail_transaksi_chk_1` CHECK ((`qty` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table dbtokodesktop.detail_transaksi: ~22 rows (approximately)
INSERT INTO `detail_transaksi` (`idDetail`, `idTransaksi`, `idBarang`, `namaBarang`, `qty`, `beratGram`, `hargaSatuan`, `subtotal`) VALUES
	(55, 927, 218, 'Cabai halus', 1, 500, 30000.00, 30000.00),
	(56, 927, 206, 'Ketumbar halus', 1, 250, 8750.00, 8750.00),
	(57, 927, 221, 'Kunyit halus', 1, 250, 7000.00, 7000.00),
	(58, 928, 204, 'Lada hitam halus', 1, 500, 35000.00, 35000.00),
	(59, 928, 220, 'Jahe halus', 1, 250, 8000.00, 8000.00),
	(60, 928, 253, 'Kencur kering', 1, 250, 12000.00, 12000.00),
	(61, 929, 239, 'Bawang putih kering', 1, 1000, 45000.00, 45000.00),
	(62, 929, 206, 'Ketumbar halus', 1, 500, 17500.00, 17500.00),
	(63, 929, 224, 'Lada hitam biji', 1, 250, 15000.00, 15000.00),
	(64, 930, 254, 'Kelapa gongseng', 1, 500, 25000.00, 25000.00),
	(65, 930, 255, 'Kemiri', 1, 500, 20000.00, 20000.00),
	(66, 930, 221, 'Kunyit halus', 1, 250, 7000.00, 7000.00),
	(67, 931, 204, 'Lada hitam halus', 2, 250, 17500.00, 35000.00),
	(68, 931, 206, 'Ketumbar halus', 1, 1000, 35000.00, 35000.00),
	(69, 931, 256, 'Vetsin', 1, 500, 25000.00, 25000.00),
	(70, 932, 204, 'Lada hitam halus', 2, 250, 17500.00, 35000.00),
	(71, 932, 206, 'Ketumbar halus', 1, 1000, 35000.00, 35000.00),
	(72, 932, 256, 'Vetsin', 1, 500, 25000.00, 25000.00),
	(73, 933, 206, 'Ketumbar halus', 1, 250, 8750.00, 8750.00),
	(74, 933, 218, 'Cabai halus', 1, 500, 30000.00, 30000.00),
	(75, 934, 239, 'Bawang putih kering', 1, 250, 11250.00, 11250.00),
	(76, 935, 204, 'Lada hitam halus', 1, 250, 17500.00, 17500.00),
	(77, 936, 224, 'Lada hitam biji', 1, 500, 30000.00, 30000.00),
	(78, 937, 206, 'Ketumbar halus', 1, 250, 8750.00, 8750.00),
	(79, 938, 218, 'Cabai halus', 1, 250, 15000.00, 15000.00),
	(80, 938, 221, 'Kunyit halus', 2, 250, 7000.00, 14000.00),
	(81, 939, 220, 'Jahe halus', 1, 250, 8000.00, 8000.00),
	(82, 939, 206, 'Ketumbar halus', 2, 250, 8750.00, 17500.00);

-- Dumping structure for table dbtokodesktop.pemasok
CREATE TABLE IF NOT EXISTS `pemasok` (
  `idPemasok` int NOT NULL AUTO_INCREMENT,
  `namaPemasok` varchar(50) DEFAULT NULL,
  `alamat` varchar(50) DEFAULT NULL,
  `kontak` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idPemasok`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table dbtokodesktop.pemasok: ~5 rows (approximately)
INSERT INTO `pemasok` (`idPemasok`, `namaPemasok`, `alamat`, `kontak`) VALUES
	(2, 'Budi ', 'Jakarta Timur', '081234567890'),
	(3, 'Rudi', 'Bandung', '082345678901'),
	(4, 'Agus Setiawan', 'Surabaya', '083456789012'),
	(5, 'Dewi Lestari', 'Yogyakarta', '084567890123'),
	(6, 'Rudi Hartono', 'Semarang', '085678901234');

-- Dumping structure for table dbtokodesktop.transaksi
CREATE TABLE IF NOT EXISTS `transaksi` (
  `idTransaksi` int NOT NULL AUTO_INCREMENT,
  `noTransaksi` varchar(20) DEFAULT NULL,
  `namaPembeli` varchar(100) DEFAULT NULL,
  `jmlBarang` int NOT NULL DEFAULT '0',
  `total` decimal(12,2) NOT NULL,
  `tglTransaksi` datetime NOT NULL,
  `bayar` decimal(15,2) DEFAULT NULL,
  `kasir` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idTransaksi`),
  UNIQUE KEY `noTransaksi` (`noTransaksi`)
) ENGINE=InnoDB AUTO_INCREMENT=940 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table dbtokodesktop.transaksi: ~12 rows (approximately)
INSERT INTO `transaksi` (`idTransaksi`, `noTransaksi`, `namaPembeli`, `jmlBarang`, `total`, `tglTransaksi`, `bayar`, `kasir`) VALUES
	(927, 'TX-0927', 'Budi Santoso', 3, 49500.00, '2026-11-19 14:05:00', NULL, NULL),
	(928, 'TX-0928', 'Siti Aminah', 3, 57000.00, '2026-11-19 14:18:00', NULL, NULL),
	(929, 'TX-0929', 'Andi Saputra', 3, 73000.00, '2026-11-19 14:32:00', NULL, NULL),
	(930, 'TX-0930', 'Rina Marlina', 3, 57000.00, '2026-11-19 14:45:00', NULL, NULL),
	(931, 'TX-0931', 'ucup Kurniawan', 4, 100000.00, '2026-11-19 15:02:00', NULL, NULL),
	(932, 'TX-0932', 'jamal', 4, 100000.00, '2026-11-19 15:02:00', NULL, NULL),
	(933, 'TX-0933', 'bagas', 2, 38750.00, '2026-11-19 16:33:42', NULL, NULL),
	(934, 'TX-0934', 'Uya', 1, 11250.00, '2026-11-19 17:12:55', NULL, NULL),
	(935, 'TX-0935', 'heru', 1, 17500.00, '2026-11-19 17:14:25', NULL, NULL),
	(936, 'TX-0936', 'Berry', 1, 30000.00, '2026-11-19 17:20:16', NULL, NULL),
	(937, 'TX-0937', 'ferry', 1, 8750.00, '2026-11-19 17:56:55', NULL, NULL),
	(938, 'TX-0938', 'Hendra', 3, 29000.00, '2026-11-19 18:03:46', 30000.00, 'Admin'),
	(939, 'TX-0939', 'Tono', 3, 25500.00, '2026-11-21 22:10:36', 30000.00, 'Admin');

-- Dumping structure for table dbtokodesktop.user
CREATE TABLE IF NOT EXISTS `user` (
  `idUser` int NOT NULL AUTO_INCREMENT,
  `username` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`idUser`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table dbtokodesktop.user: ~0 rows (approximately)
INSERT INTO `user` (`idUser`, `username`, `password`) VALUES
	(12, 'admin', 'admin123');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
