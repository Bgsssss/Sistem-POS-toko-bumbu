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
  UNIQUE KEY `uq_barang_nama` (`namaBarang`),
  KEY `fk_barang_pemasok` (`idPemasok`),
  CONSTRAINT `fk_barang_pemasok` FOREIGN KEY (`idPemasok`) REFERENCES `pemasok` (`idPemasok`),
  CONSTRAINT `barang_chk_1` CHECK ((`stok` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table dbtokodesktop.barang: ~37 rows (approximately)
INSERT INTO `barang` (`idBarang`, `namaBarang`, `kategori`, `beratGram`, `modal`, `hargaJual`, `stok`, `idPemasok`) VALUES
	(1, 'Lada Putih Halus', 'Halus', 250, 13750.00, 17500.00, 106, 2),
	(2, 'Ketumbar', 'Halus', 250, 6250.00, 8750.00, 138, 4),
	(3, 'Cabai halus', 'Halus', 250, 12000.00, 15000.00, 91, 6),
	(4, 'Jahe halus', 'Halus', 250, 6000.00, 8000.00, 125, 3),
	(5, 'Kunyit halus', 'Halus', 250, 5000.00, 7000.00, 134, 4),
	(6, 'Lada hitam biji', 'Bulat', 250, 12000.00, 15000.00, 102, 2),
	(7, 'Bawang Kating', 'Bulat', 250, 7500.00, 8750.00, 82, 2),
	(8, 'Kencur', 'Bulat', 250, 9000.00, 12000.00, 57, 2),
	(9, 'Kelapa gongseng', 'Bulat', 250, 10000.00, 11250.00, 43, 3),
	(10, 'Kemiri', 'Bulat', 250, 10000.00, 11250.00, 2, 4),
	(11, 'Vetsin', 'Halus', 250, 13750.00, 15000.00, 40, 5),
	(12, 'Cap Udang Kecil', 'Halus', 250, 15416.75, 16250.00, 12, 5),
	(13, 'Cap Udang kiloan', 'Halus', 250, 15416.75, 16250.00, 12, 6),
	(14, 'Adas Halus', 'Halus', 250, 15000.00, 17500.00, 78, 4),
	(15, 'Adas Bulat', 'Bulat', 250, 16250.00, 18750.00, 18, 5),
	(16, 'Sari minang Rendang', 'Halus', 250, 12500.00, 15000.00, 60, 6),
	(17, 'Sari minang Gulai', 'Halus', 250, 12500.00, 15000.00, 45, 2),
	(18, 'Serba guna Rendang', 'Halus', 250, 16116.00, 20000.00, 42, 4),
	(19, 'Serba guna Gulai', 'Halus', 250, 16116.00, 20000.00, 53, 4),
	(20, 'Bumbu Minang rendang', 'Halus', 250, 9000.00, 12000.00, 27, 4),
	(21, 'Terasi Juek', 'Bulat', 250, 22500.00, 25000.00, 159, 4),
	(22, 'Biji pala', 'Halus', 250, 75000.00, 83000.00, 64, 4),
	(23, 'Samhok', 'Bulat', 250, 21250.00, 23000.00, 57, 2),
	(24, 'Bumbu Slamet', 'Halus', 250, 12500.00, 13750.00, 27, 2),
	(25, 'Aroma', 'Halus', 250, 20000.00, 22000.00, 35, 5),
	(26, 'Ketumbar Kuning', 'Bulat', 250, 7500.00, 8000.00, 16, 5),
	(27, 'Ketumbar Coklat', 'Bulat', 250, 7500.00, 8000.00, 12, 4),
	(28, 'Awak Juo Rendang', 'Halus', 250, 60000.00, 65000.00, 122, 4),
	(29, 'Awak Juo Gulai', 'Halus', 250, 55000.00, 60000.00, 124, 5),
	(30, 'Cabe', 'Halus', 250, 18750.00, 20000.00, 25, 6),
	(31, 'Jintan Halus', 'Halus', 250, 17000.00, 20000.00, 23, 3),
	(32, 'Jintan Bulat', 'Bulat', 250, 9500.00, 10000.00, 21, 4),
	(33, 'Kapol india', 'Bulat', 250, 187500.00, 200000.00, 7, 2),
	(34, 'Kapol laga', 'Bulat', 250, 25000.00, 30000.00, 12, 6),
	(35, 'Kayu manis', 'Bulat', 250, 22500.00, 25000.00, 44, 3),
	(36, 'Cengkeh', 'Bulat', 250, 37500.00, 45000.00, 23, 5),
	(37, 'Kemiri Giling', 'Bulat', 250, 7500.00, 8750.00, 25, 5);

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
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table dbtokodesktop.detail_transaksi: ~38 rows (approximately)
INSERT INTO `detail_transaksi` (`idDetail`, `idTransaksi`, `idBarang`, `namaBarang`, `qty`, `beratGram`, `hargaSatuan`, `subtotal`) VALUES
	(1, 1, 12, 'Cap Udang Kecil', 1, 1000, 65000.00, 65000.00),
	(2, 1, 1, 'Lada Putih Halus', 1, 250, 17500.00, 17500.00),
	(3, 1, 37, 'Kemiri Giling', 1, 1000, 35000.00, 35000.00),
	(4, 1, 5, 'Kunyit halus', 1, 500, 14000.00, 14000.00),
	(5, 2, 28, 'Awak Juo Rendang', 1, 750, 195000.00, 195000.00),
	(6, 2, 29, 'Awak Juo Gulai', 1, 750, 180000.00, 180000.00),
	(7, 2, 5, 'Kunyit halus', 1, 250, 7000.00, 7000.00),
	(8, 2, 14, 'Adas Halus', 1, 2000, 140000.00, 140000.00),
	(9, 3, 20, 'Bumbu Minang rendang', 1, 750, 36000.00, 36000.00),
	(10, 3, 6, 'Lada hitam biji', 1, 500, 30000.00, 30000.00),
	(11, 3, 19, 'Serba guna Gulai', 1, 750, 60000.00, 60000.00),
	(12, 4, 8, 'Kencur', 1, 250, 12000.00, 12000.00),
	(13, 4, 9, 'Kelapa gongseng', 1, 1000, 45000.00, 45000.00),
	(14, 4, 18, 'Serba guna Rendang', 1, 1000, 80000.00, 80000.00),
	(15, 4, 7, 'Bawang Kating', 1, 1000, 35000.00, 35000.00),
	(16, 5, 15, 'Adas Bulat', 1, 2000, 150000.00, 150000.00),
	(17, 5, 14, 'Adas Halus', 1, 1000, 70000.00, 70000.00),
	(18, 5, 9, 'Kelapa gongseng', 1, 2000, 90000.00, 90000.00),
	(19, 5, 29, 'Awak Juo Gulai', 1, 1000, 240000.00, 240000.00),
	(20, 6, 22, 'Biji pala', 1, 500, 166000.00, 166000.00),
	(21, 6, 2, 'Ketumbar', 1, 1000, 35000.00, 35000.00),
	(22, 6, 16, 'Sari minang Rendang', 1, 250, 15000.00, 15000.00),
	(23, 7, 32, 'Jintan Bulat', 1, 2000, 80000.00, 80000.00),
	(24, 7, 9, 'Kelapa gongseng', 1, 1000, 45000.00, 45000.00),
	(25, 8, 5, 'Kunyit halus', 1, 250, 7000.00, 7000.00),
	(26, 9, 26, 'Ketumbar Kuning', 1, 2000, 64000.00, 64000.00),
	(27, 10, 4, 'Jahe halus', 1, 750, 24000.00, 24000.00),
	(28, 10, 7, 'Bawang Kating', 1, 500, 17500.00, 17500.00),
	(29, 10, 3, 'Cabai halus', 1, 1000, 60000.00, 60000.00),
	(30, 10, 15, 'Adas Bulat', 1, 2000, 150000.00, 150000.00),
	(31, 11, 17, 'Sari minang Gulai', 1, 500, 30000.00, 30000.00),
	(32, 11, 18, 'Serba guna Rendang', 1, 500, 40000.00, 40000.00),
	(33, 11, 14, 'Adas Halus', 1, 2000, 140000.00, 140000.00),
	(34, 12, 37, 'Kemiri Giling', 1, 1000, 35000.00, 35000.00),
	(35, 12, 13, 'Cap Udang kiloan', 1, 1000, 65000.00, 65000.00),
	(36, 12, 5, 'Kunyit halus', 1, 1000, 28000.00, 28000.00),
	(37, 13, 16, 'Sari minang Rendang', 1, 750, 45000.00, 45000.00),
	(38, 13, 2, 'Ketumbar', 1, 1000, 35000.00, 35000.00),
	(39, 13, 5, 'Kunyit halus', 1, 1000, 28000.00, 28000.00),
	(40, 14, 13, 'Cap Udang kiloan', 1, 2000, 130000.00, 130000.00),
	(41, 15, 26, 'Ketumbar Kuning', 1, 1000, 32000.00, 32000.00),
	(42, 16, 3, 'Cabai halus', 1, 1000, 60000.00, 60000.00),
	(43, 16, 2, 'Ketumbar', 1, 750, 26250.00, 26250.00),
	(44, 17, 2, 'Ketumbar', 1, 250, 8750.00, 8750.00),
	(45, 18, 9, 'Kelapa gongseng', 1, 1000, 45000.00, 45000.00),
	(46, 19, 5, 'Kunyit halus', 1, 1000, 28000.00, 28000.00),
	(47, 20, 31, 'Jintan Halus', 1, 250, 20000.00, 20000.00),
	(48, 20, 19, 'Serba guna Gulai', 1, 750, 60000.00, 60000.00);

-- Dumping structure for table dbtokodesktop.pemasok
CREATE TABLE IF NOT EXISTS `pemasok` (
  `idPemasok` int NOT NULL AUTO_INCREMENT,
  `namaPemasok` varchar(50) DEFAULT NULL,
  `alamat` varchar(50) DEFAULT NULL,
  `kontak` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idPemasok`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table dbtokodesktop.transaksi: ~29 rows (approximately)
INSERT INTO `transaksi` (`idTransaksi`, `noTransaksi`, `namaPembeli`, `jmlBarang`, `total`, `tglTransaksi`, `bayar`, `kasir`) VALUES
	(1, 'TX-0001', 'pembeli 1', 4, 131500.00, '2026-11-27 21:36:25', 132000.00, 'admin'),
	(2, 'TX-0002', 'pembeli 2', 4, 522000.00, '2026-11-27 22:10:06', 600000.00, 'admin'),
	(3, 'TX-0003', 'pembeli 3', 3, 126000.00, '2026-11-27 22:17:51', 150000.00, 'admin'),
	(4, 'TX-0004', 'pembeli 4', 4, 172000.00, '2026-11-27 22:30:09', 180000.00, 'admin'),
	(5, 'TX-0005', 'pembeli 5', 4, 550000.00, '2026-11-27 22:33:39', 600000.00, 'admin'),
	(6, 'TX-0006', 'pembeli 6', 3, 216000.00, '2026-11-28 17:17:21', 220000.00, 'admin'),
	(7, 'TX-0007', 'pembeli 7', 2, 125000.00, '2026-11-28 17:20:53', 150000.00, 'admin'),
	(8, 'TX-0008', 'pembeli 8', 1, 7000.00, '2026-11-28 17:25:33', 8000.00, 'admin'),
	(9, 'TX-0009', 'pembeli 9', 1, 64000.00, '2026-11-28 17:28:03', 65000.00, 'admin'),
	(10, 'TX-0010', 'pembeli 10', 4, 251500.00, '2026-11-28 17:30:49', 260000.00, 'admin'),
	(11, 'TX-0011', 'pembeli 11', 3, 210000.00, '2026-11-28 17:39:34', 220000.00, 'admin'),
	(12, 'TX-0012', 'pembeli 12', 3, 128000.00, '2026-11-28 17:43:31', 130000.00, 'admin'),
	(13, 'TX-0013', 'pembeli 13', 3, 108000.00, '2026-11-28 17:46:09', 110000.00, 'admin'),
	(14, 'TX-0014', 'pembeli 14', 1, 130000.00, '2026-11-28 17:47:46', 150000.00, 'admin'),
	(15, 'TX-0015', 'pembeli 15', 1, 32000.00, '2026-11-28 17:50:07', 35000.00, 'admin'),
	(16, 'TX-0016', 'pembeli 16', 2, 86250.00, '2026-11-28 18:03:18', 90000.00, 'admin'),
	(17, 'TX-0017', 'pembeli 17', 1, 8750.00, '2026-11-28 18:04:35', 10000.00, 'admin'),
	(18, 'TX-0018', 'pembeli 18', 1, 45000.00, '2026-11-28 18:09:36', 50000.00, 'admin'),
	(19, 'TX-0019', 'pembeli 19', 1, 28000.00, '2026-11-28 18:11:19', 30000.00, 'admin'),
	(20, 'TX-0020', 'pembeli 20', 2, 80000.00, '2026-11-28 18:14:09', 100000.00, 'admin');

-- Dumping structure for table dbtokodesktop.user
CREATE TABLE IF NOT EXISTS `user` (
  `idUser` int NOT NULL AUTO_INCREMENT,
  `username` varchar(20) DEFAULT NULL,
  `password` varchar(100) NOT NULL,
  PRIMARY KEY (`idUser`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table dbtokodesktop.user: ~1 rows (approximately)
INSERT INTO `user` (`idUser`, `username`, `password`) VALUES
	(1, 'admin', '$2a$10$/3ASDjFIIXkqZ1C2yFjSBuK9Vqd/L0AGr0yj.NExsKIh2onh7qVNS');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
