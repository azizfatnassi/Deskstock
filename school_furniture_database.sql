-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Sep 21, 2025 at 01:05 AM
-- Server version: 5.6.17
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `school_furniture_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `carts`
--

CREATE TABLE IF NOT EXISTS `carts` (
  `cart_id` int(11) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`cart_id`),
  UNIQUE KEY `UK_64t7ox312pqal3p7fg9o503c2` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Dumping data for table `carts`
--

INSERT INTO `carts` (`cart_id`, `created_at`, `updated_at`, `user_id`) VALUES
(1, '2025-09-09 23:15:27.313000', '2025-09-09 23:15:27.313000', 3),
(2, '2025-09-10 16:49:43.594000', '2025-09-10 16:49:43.595000', 2),
(3, '2025-09-11 17:00:09.418000', '2025-09-11 17:00:09.418000', 5),
(4, '2025-09-11 17:40:37.101000', '2025-09-11 17:40:37.101000', 6),
(6, '2025-09-18 16:28:03.425000', '2025-09-18 16:28:03.425000', 12);

-- --------------------------------------------------------

--
-- Table structure for table `cart_items`
--

CREATE TABLE IF NOT EXISTS `cart_items` (
  `cart_item_id` int(11) NOT NULL AUTO_INCREMENT,
  `added_at` datetime(6) NOT NULL,
  `quantity` int(11) NOT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `cart_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  PRIMARY KEY (`cart_item_id`),
  KEY `FKpcttvuq4mxppo8sxggjtn5i2c` (`cart_id`),
  KEY `FK1re40cjegsfvw58xrkdp6bac6` (`product_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=13 ;

--
-- Dumping data for table `cart_items`
--

INSERT INTO `cart_items` (`cart_item_id`, `added_at`, `quantity`, `unit_price`, `updated_at`, `cart_id`, `product_id`) VALUES
(1, '2025-09-09 23:15:27.360000', 6, '10.99', '2025-09-10 12:51:27.085000', 1, 3),
(2, '2025-09-10 12:57:19.300000', 2, '299.99', '2025-09-10 12:57:19.300000', 1, 1),
(5, '2025-09-11 17:00:09.430000', 2, '10.99', '2025-09-11 17:14:32.839000', 3, 3),
(6, '2025-09-11 17:40:37.111000', 4, '299.99', '2025-09-11 17:41:10.961000', 4, 1),
(12, '2025-09-18 16:28:03.438000', 2, '6.04', '2025-09-18 16:28:03.438000', 6, 965485);

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE IF NOT EXISTS `orders` (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `delivered_date` datetime(6) DEFAULT NULL,
  `notes` text,
  `order_date` datetime(6) NOT NULL,
  `order_number` varchar(255) NOT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `shipped_date` datetime(6) DEFAULT NULL,
  `shipping_address` varchar(500) NOT NULL,
  `shipping_city` varchar(100) DEFAULT NULL,
  `shipping_postal_code` varchar(20) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `UK_nthkiu7pgmnqnu86i2jyoe2v7` (`order_number`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`order_id`, `delivered_date`, `notes`, `order_date`, `order_number`, `phone_number`, `shipped_date`, `shipping_address`, `shipping_city`, `shipping_postal_code`, `status`, `total_amount`, `updated_at`, `user_id`) VALUES
(1, NULL, '11', '2025-09-19 17:10:41.290000', 'ORD-1758301841267', '5145256344', NULL, 'kkkkajzhedndnndnnnn', NULL, NULL, 'PENDING', '5.67', '2025-09-19 17:10:41.290000', 2),
(2, NULL, NULL, '2025-09-19 17:18:55.461000', 'ORD-1758302335449', '4556666666', NULL, '55555555555555555555555555', NULL, NULL, 'PENDING', '8.14', '2025-09-19 17:18:55.461000', 2);

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE IF NOT EXISTS `order_items` (
  `order_item_id` int(11) NOT NULL AUTO_INCREMENT,
  `product_description` varchar(255) DEFAULT NULL,
  `product_name` varchar(255) NOT NULL,
  `quantity` int(11) NOT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `product_category` varchar(50) DEFAULT NULL,
  `product_color` varchar(50) DEFAULT NULL,
  `product_image_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`order_item_id`),
  KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
  KEY `FKocimc7dtr037rh4ls4l95nlfi` (`product_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`order_item_id`, `product_description`, `product_name`, `quantity`, `unit_price`, `order_id`, `product_id`, `product_category`, `product_color`, `product_image_url`) VALUES
(1, 'pochette geometrique PM coq 20cm', '000220', 1, '1.82', 1, 965478, 'Tracage', NULL, NULL),
(2, 'regle de 30cm office', '000237', 1, '3.85', 1, 965468, 'Tracage', NULL, NULL),
(3, 'regle de 30cm office', '000237', 1, '3.85', 2, 965468, 'Tracage', NULL, NULL),
(4, 'boite de 10 stylo cristal soft BIC', '001503', 1, '4.29', 2, 965433, 'Stylos', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE IF NOT EXISTS `products` (
  `product_id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(255) NOT NULL,
  `color` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `description` text,
  `image_url` varchar(255) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `low_stock_threshold` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `stock_quantity` int(11) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `code_article` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `UK_mk0est25xmr181qa225uy7xqo` (`code_article`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=965495 ;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`product_id`, `category`, `color`, `created_at`, `description`, `image_url`, `is_active`, `low_stock_threshold`, `name`, `price`, `stock_quantity`, `updated_at`, `code_article`) VALUES
(1, 'CHAIRS', 'BLUE', '2025-09-05 22:09:51.623000', 'A comfortable test chair', NULL, b'0', 10, 'Test Chair', '299.99', 50, '2025-09-16 17:17:07.117000', NULL),
(2, 'DESKS', 'BLUE', '2025-09-05 22:16:15.423000', 'Stylo Bic Bleu', NULL, b'0', 10, 'Stylo', '10.99', 50, '2025-09-16 17:06:15.775000', NULL),
(3, 'DESKS', 'BLUE', '2025-09-05 22:16:50.773000', 'cahier spiral', NULL, b'1', 10, 'cahier', '10.99', 48, '2025-09-16 13:27:54.715000', NULL),
(10, 'Agrafeuses, Arraches-agrafes et Agrafes', NULL, '2025-09-16 19:50:41.000000', 'agrafeuse vivo 24/6 MAPED', NULL, b'1', 0, 'agrafeuse vivo 24/6 MAPED', '3.95', 0, '2025-09-16 19:50:41.000000', '404005'),
(11, 'Agrafeuses, Arraches-agrafes et Agrafes', NULL, '2025-09-16 19:50:41.000000', 'agrafeuse mini 24/6 DL0134 CH', NULL, b'1', 0, 'agrafeuse mini 24/6 DL0134 CH', '2.92', 0, '2025-09-16 19:50:41.000000', '860443'),
(12, 'Agrafeuses, Arraches-agrafes et Agrafes', NULL, '2025-09-16 19:50:41.000000', 'agrafeuse Essentials half strip maped', NULL, b'1', 0, 'agrafeuse Essentials half strip maped', '10.89', 0, '2025-09-16 19:50:41.000000', '543119'),
(13, 'Agrafeuses, Arraches-agrafes et Agrafes', NULL, '2025-09-16 19:50:41.000000', 'agrafeuse essentials half strip GM maped', NULL, b'1', 0, 'agrafeuse essentials half strip GM maped', '14.68', 0, '2025-09-16 19:50:41.000000', '544116'),
(14, 'Agrafeuses, Arraches-agrafes et Agrafes', NULL, '2025-09-16 19:50:41.000000', 'agrafeuse essentials A17 maped', NULL, b'1', 0, 'agrafeuse essentials A17 maped', '10.90', 0, '2025-09-16 19:50:41.000000', '535118'),
(15, 'Agrafeuses, Arraches-agrafes et Agrafes', NULL, '2025-09-16 19:50:41.000000', 'agrafeuse first métal HS 24/6 maped', NULL, b'1', 0, 'agrafeuse first métal HS 24/6 maped', '8.55', 0, '2025-09-16 19:50:41.000000', '927100'),
(16, 'Agrafeuses, Arraches-agrafes et Agrafes', NULL, '2025-09-16 19:50:41.000000', 'agrafeuse DL0335', NULL, b'1', 0, 'agrafeuse DL0335', '7.28', 0, '2025-09-16 19:50:41.000000', '860771'),
(17, 'Agrafeuses, Arraches-agrafes et Agrafes', NULL, '2025-09-16 19:50:41.000000', 'agrafeuse DL568', NULL, b'1', 0, 'agrafeuse DL568', '5.47', 0, '2025-09-16 19:50:41.000000', '585688'),
(18, 'Agrafeuses, Arraches-agrafes et Agrafes', NULL, '2025-09-16 19:50:41.000000', 'agrafeuse DL-5861', NULL, b'1', 0, 'agrafeuse DL-5861', '10.48', 0, '2025-09-16 19:50:41.000000', '558613'),
(19, 'Agrafeuses, Arraches-agrafes et Agrafes', NULL, '2025-09-16 19:50:41.000000', 'agrafeuse hoobn 25f', NULL, b'1', 0, 'agrafeuse hoobn 25f', '4.90', 0, '2025-09-16 19:50:41.000000', '251350'),
(20, 'Agrafeuses, Arraches-agrafes et Agrafes', NULL, '2025-09-16 19:50:41.000000', 'agrafeuse hoobn 50f', NULL, b'1', 0, 'agrafeuse hoobn 50f', '10.90', 0, '2025-09-16 19:50:41.000000', '250797'),
(21, 'Agrafeuses, Arraches-agrafes et Agrafes', NULL, '2025-09-16 19:50:41.000000', 'agrafeuse hoobn', NULL, b'1', 0, 'agrafeuse hoobn', '8.60', 0, '2025-09-16 19:50:41.000000', '250780'),
(22, 'Agrafeuses, Arraches-agrafes et Agrafes', NULL, '2025-09-16 19:50:41.000000', 'arrache agrafes CH', NULL, b'1', 0, 'arrache agrafes CH', '0.90', 0, '2025-09-16 19:50:41.000000', '502289'),
(23, 'Aquarelles', NULL, '2025-09-16 19:50:41.000000', 'aquarelle de 6 COQ boite ovale', NULL, b'1', 0, 'aquarelle de 6 COQ boite ovale', '1.26', 0, '2025-09-16 19:50:41.000000', '100826'),
(24, 'Aquarelles', NULL, '2025-09-16 19:50:41.000000', 'aquarelle de 12 COQ boite ovale COQ', NULL, b'1', 0, 'aquarelle de 12 COQ boite ovale COQ', '2.12', 0, '2025-09-16 19:50:41.000000', '100833'),
(25, 'Aquarelles', NULL, '2025-09-16 19:50:41.000000', 'aquarelle de 12 BIC', NULL, b'1', 0, 'aquarelle de 12 BIC', '7.52', 0, '2025-09-16 19:50:41.000000', '485440'),
(26, 'Aquarelles', NULL, '2025-09-16 19:50:41.000000', 'aquarelle de 12+palette d''artiste PM', NULL, b'1', 0, 'aquarelle de 12+palette d''artiste PM', '1.35', 0, '2025-09-16 19:50:41.000000', 'AQ-PAL-PM'),
(27, 'Aquarelles', NULL, '2025-09-16 19:50:41.000000', 'aquarelle de 12+Palette d''artiste GM', NULL, b'1', 0, 'aquarelle de 12+Palette d''artiste GM', '1.65', 0, '2025-09-16 19:50:41.000000', 'AQ-PALGM-'),
(28, 'Aquarelles', NULL, '2025-09-16 19:50:41.000000', 'aquarelle valise de12', NULL, b'1', 0, 'aquarelle valise de12', '2.00', 0, '2025-09-16 19:50:41.000000', '082085'),
(29, 'Ardoises', NULL, '2025-09-16 19:50:41.000000', 'ardoise simple cadre plastique coq', NULL, b'1', 0, 'ardoise simple cadre plastique coq', '1.82', 0, '2025-09-16 19:50:41.000000', 'ARD BCP'),
(30, 'Ardoises', NULL, '2025-09-16 19:50:41.000000', 'ardoise magique sans stylo coq', NULL, b'1', 0, 'ardoise magique sans stylo coq', '2.63', 0, '2025-09-16 19:50:41.000000', 'ARD MAG SS'),
(31, 'Ardoises', NULL, '2025-09-16 19:50:41.000000', 'ardoise 2/1 avec stylo ELEGANCE coq', NULL, b'1', 0, 'ardoise 2/1 avec stylo ELEGANCE coq', '3.33', 0, '2025-09-16 19:50:41.000000', 'ARD-2/1AS EL'),
(32, 'Ardoises', NULL, '2025-09-16 19:50:41.000000', 'ardoise 2/1 avec stylo+chiffon amir', NULL, b'1', 0, 'ardoise 2/1 avec stylo+chiffon amir', '3.64', 0, '2025-09-16 19:50:41.000000', '500012'),
(33, 'Ardoises', NULL, '2025-09-16 19:50:41.000000', 'ardoise magique avec stylo coq', NULL, b'1', 0, 'ardoise magique avec stylo coq', '3.23', 0, '2025-09-16 19:50:41.000000', 'ARD MAG AS'),
(34, 'Ardoises', NULL, '2025-09-16 19:50:41.000000', 'ardoise magique +stylo+chiffon amir', NULL, b'1', 0, 'ardoise magique +stylo+chiffon amir', '4.62', 0, '2025-09-16 19:50:41.000000', '500098'),
(35, 'Ardoises', NULL, '2025-09-16 19:50:41.000000', 'ardoise bic + accessoire incasable', NULL, b'1', 0, 'ardoise bic + accessoire incasable', '8.78', 0, '2025-09-16 19:50:41.000000', 'ARD-BIC'),
(36, 'Ardoises', NULL, '2025-09-16 19:50:41.000000', 'ardoise offi + accessoire incasable', NULL, b'1', 0, 'ardoise offi + accessoire incasable', '5.82', 0, '2025-09-16 19:50:41.000000', '150868'),
(37, 'Ardoises', NULL, '2025-09-16 19:50:41.000000', 'ardoise cadre plastique office', NULL, b'1', 0, 'ardoise cadre plastique office', '1.61', 0, '2025-09-16 19:50:41.000000', '113764'),
(38, 'Ardoises', NULL, '2025-09-16 19:50:41.000000', 'ardoise magique+stylo+chiffon office', NULL, b'1', 0, 'ardoise magique+stylo+chiffon office', '2.87', 0, '2025-09-16 19:50:41.000000', '143778'),
(39, 'Ardoises', NULL, '2025-09-16 19:50:41.000000', 'ardoise 2/1 office+stylo', NULL, b'1', 0, 'ardoise 2/1 office+stylo', '3.30', 0, '2025-09-16 19:50:41.000000', 'ARD-2/1 Office'),
(40, 'Blancos', NULL, '2025-09-16 19:50:41.000000', 'blanco stylo vneed B 24', NULL, b'1', 0, 'blanco stylo vneed B 24', '1.00', 0, '2025-09-16 19:50:41.000000', '002407'),
(41, 'Blancos', NULL, '2025-09-16 19:50:41.000000', 'blanco stylo wilson B24', NULL, b'1', 0, 'blanco stylo wilson B24', '1.10', 0, '2025-09-16 19:50:41.000000', '122855'),
(42, 'Blancos', NULL, '2025-09-16 19:50:41.000000', 'blanco stylo vneed B 12', NULL, b'1', 0, 'blanco stylo vneed B 12', '1.00', 0, '2025-09-16 19:50:41.000000', '003237'),
(43, 'Blancos', NULL, '2025-09-16 19:50:41.000000', 'blanco SHAKE N SQUEEZE TIPP-EX gros', NULL, b'1', 0, 'blanco SHAKE N SQUEEZE TIPP-EX gros', '3.98', 0, '2025-09-16 19:50:41.000000', '100845'),
(44, 'Blancos', NULL, '2025-09-16 19:50:41.000000', 'blanco stylo Bic', NULL, b'1', 0, 'blanco stylo Bic', '2.72', 0, '2025-09-16 19:50:41.000000', '340220'),
(45, 'Blancos', NULL, '2025-09-16 19:50:41.000000', 'blanco 2bouteille 20ml wilson', NULL, b'1', 0, 'blanco 2bouteille 20ml wilson', '1.99', 0, '2025-09-16 19:50:41.000000', '131703'),
(46, 'Ciseaux', NULL, '2025-09-16 19:50:41.000000', 'ciseaux écolier ref-6304 rond', NULL, b'1', 0, 'ciseaux écolier ref-6304 rond', '1.25', 0, '2025-09-16 19:50:41.000000', '250048-R'),
(47, 'Ciseaux', NULL, '2025-09-16 19:50:41.000000', 'ciseaux écolier ref-6010 frein', NULL, b'1', 0, 'ciseaux écolier ref-6010 frein', '1.50', 0, '2025-09-16 19:50:41.000000', '250048-F'),
(48, 'Ciseaux', NULL, '2025-09-16 19:50:41.000000', 'ciseaux écolier jinling', NULL, b'1', 0, 'ciseaux écolier jinling', '1.36', 0, '2025-09-16 19:50:41.000000', '586123'),
(49, 'Ciseaux', NULL, '2025-09-16 19:50:41.000000', 'ciseaux écolier chat skyglory b24', NULL, b'1', 0, 'ciseaux écolier chat skyglory b24', '1.20', 0, '2025-09-16 19:50:41.000000', '551180'),
(50, 'Ciseaux', NULL, '2025-09-16 19:50:41.000000', 'ciseaux écolier skyglory p24', NULL, b'1', 0, 'ciseaux écolier skyglory p24', '1.15', 0, '2025-09-16 19:50:41.000000', '551128'),
(51, 'Ciseaux', NULL, '2025-09-16 19:50:41.000000', 'ciseaux écolier skyglory p24', NULL, b'1', 0, 'ciseaux écolier skyglory p24', '1.20', 0, '2025-09-16 19:50:41.000000', '551111'),
(52, 'Ciseaux', NULL, '2025-09-16 19:50:41.000000', 'ciseaux écolier skyglory p24', NULL, b'1', 0, 'ciseaux écolier skyglory p24', '1.30', 0, '2025-09-16 19:50:41.000000', '551173'),
(53, 'Ciseaux', NULL, '2025-09-16 19:50:41.000000', 'ciseaux écolier 13cm essentiel soft maped', NULL, b'1', 0, 'ciseaux écolier 13cm essentiel soft maped', '2.48', 0, '2025-09-16 19:50:41.000000', '464412'),
(54, 'Ciseaux', NULL, '2025-09-16 19:50:41.000000', 'ciseaux scissors 13cm', NULL, b'1', 0, 'ciseaux scissors 13cm', '1.82', 0, '2025-09-16 19:50:41.000000', '535556'),
(55, 'Ciseaux', NULL, '2025-09-16 19:50:41.000000', 'ciseaux bureautique GM', NULL, b'1', 0, 'ciseaux bureautique GM', '2.50', 0, '2025-09-16 19:50:41.000000', '250093'),
(56, 'Ciseaux', NULL, '2025-09-16 19:50:41.000000', 'ciseaux MAPED essentiel 17cm', NULL, b'1', 0, 'ciseaux MAPED essentiel 17cm', '4.14', 0, '2025-09-16 19:50:41.000000', '682107'),
(57, 'Ciseaux', NULL, '2025-09-16 19:50:41.000000', 'ciseaux MAPED essentiel 21cm', NULL, b'1', 0, 'ciseaux MAPED essentiel 21cm', '4.63', 0, '2025-09-16 19:50:41.000000', '681100'),
(58, 'Ciseaux', NULL, '2025-09-16 19:50:41.000000', 'ciseaux MAPED soft 21cm', NULL, b'1', 0, 'ciseaux MAPED soft 21cm', '5.21', 0, '2025-09-16 19:50:41.000000', '683104'),
(59, 'Colles', NULL, '2025-09-16 19:52:34.000000', 'colle blanche COQ 28ml', NULL, b'1', 10, 'colle blanche COQ 28ml', '0.44', 0, '2025-09-16 19:52:34.000000', '000695'),
(60, 'Colles', NULL, '2025-09-16 19:52:34.000000', 'colle blanche 250 ml coq', NULL, b'1', 10, 'colle blanche 250 ml coq', '1.62', 0, '2025-09-16 19:52:34.000000', '001494'),
(61, 'Colles', NULL, '2025-09-16 19:52:34.000000', 'colle blanche 500 ml coq', NULL, b'1', 10, 'colle blanche 500 ml coq', '2.83', 0, '2025-09-16 19:52:34.000000', '001401'),
(62, 'Colles', NULL, '2025-09-16 19:52:34.000000', 'colle blanche 1L coq', NULL, b'1', 10, 'colle blanche 1L coq', '5.35', 0, '2025-09-16 19:52:34.000000', '000602'),
(63, 'Colles', NULL, '2025-09-16 19:52:34.000000', 'colle forte PM metal COQ', NULL, b'1', 10, 'colle forte PM metal COQ', '0.68', 0, '2025-09-16 19:52:34.000000', '000015'),
(64, 'Colles', NULL, '2025-09-16 19:52:34.000000', 'colle forte MM metal COQ', NULL, b'1', 10, 'colle forte MM metal COQ', '0.91', 0, '2025-09-16 19:52:34.000000', '000022'),
(65, 'Colles', NULL, '2025-09-16 19:52:34.000000', 'colle forte GM metal COQ', NULL, b'1', 10, 'colle forte GM metal COQ', '1.03', 0, '2025-09-16 19:52:34.000000', '000039'),
(66, 'Colles', NULL, '2025-09-16 19:52:34.000000', 'colle stick UHU 8,2 gr', NULL, b'1', 10, 'colle stick UHU 8,2 gr', '1.73', 0, '2025-09-16 19:52:34.000000', '337716'),
(67, 'Colles', NULL, '2025-09-16 19:52:34.000000', 'colle stick UHU 21 gr', NULL, b'1', 10, 'colle stick UHU 21 gr', '2.78', 0, '2025-09-16 19:52:34.000000', '337778'),
(68, 'Colles', NULL, '2025-09-16 19:52:34.000000', 'colle stick UHU 40 gr', NULL, b'1', 10, 'colle stick UHU 40 gr', '4.25', 0, '2025-09-16 19:52:34.000000', '373721'),
(69, 'Colles', NULL, '2025-09-16 19:53:04.000000', 'colle stick bic 8g boite de 30', NULL, b'1', 10, 'colle stick bic 8g boite de 30', '1.70', 0, '2025-09-16 19:53:04.000000', '621760'),
(70, 'Colles', NULL, '2025-09-16 19:53:04.000000', 'colle stick bic 21g boite de 20', NULL, b'1', 10, 'colle stick bic 21g boite de 20', '3.70', 0, '2025-09-16 19:53:04.000000', '621784'),
(71, 'Colles', NULL, '2025-09-16 19:53:04.000000', 'colle stick rapido emeraude 8g', NULL, b'1', 10, 'colle stick rapido emeraude 8g', '0.90', 0, '2025-09-16 19:53:04.000000', '113274'),
(72, 'Colles', NULL, '2025-09-16 19:53:04.000000', 'colle stick rapido emeraude 15g', NULL, b'1', 10, 'colle stick rapido emeraude 15g', '1.49', 0, '2025-09-16 19:53:04.000000', '113304'),
(73, 'Colles', NULL, '2025-09-16 19:53:04.000000', 'colle stick wilson 21g', NULL, b'1', 10, 'colle stick wilson 21g', '1.67', 0, '2025-09-16 19:53:04.000000', '200899'),
(74, 'Colles', NULL, '2025-09-16 19:53:04.000000', 'colle stick wilson 36g', NULL, b'1', 10, 'colle stick wilson 36g', '2.10', 0, '2025-09-16 19:53:04.000000', '200905'),
(75, 'Colles', NULL, '2025-09-16 19:53:04.000000', 'colle stick 15gr bertand', NULL, b'1', 10, 'colle stick 15gr bertand', '0.78', 0, '2025-09-16 19:53:04.000000', '410159'),
(76, 'Colles', NULL, '2025-09-16 19:53:04.000000', 'colle stick 21gr bertand', NULL, b'1', 10, 'colle stick 21gr bertand', '1.09', 0, '2025-09-16 19:53:04.000000', '430218'),
(77, 'Colles', NULL, '2025-09-16 19:53:04.000000', 'patafix UHU simple', NULL, b'1', 10, 'patafix UHU simple', '3.78', 0, '2025-09-16 19:53:04.000000', '443905'),
(78, 'Colles', NULL, '2025-09-16 19:53:04.000000', 'patafix UHU invisible', NULL, b'1', 10, 'patafix UHU invisible', '6.41', 0, '2025-09-16 19:53:04.000000', '371550'),
(79, 'Compas', NULL, '2025-09-16 19:53:52.000000', 'compas Essentials - Bague MAPED', NULL, b'1', 5, 'compas Essentials - Bague MAPED', '2.35', 0, '2025-09-16 19:53:52.000000', '181104'),
(80, 'Compas', NULL, '2025-09-16 19:53:52.000000', 'compas+crayon precision', NULL, b'1', 5, 'compas+crayon precision', '2.50', 0, '2025-09-16 19:53:52.000000', '400044'),
(81, 'Compas', NULL, '2025-09-16 19:53:52.000000', 'compas+crayon wilson', NULL, b'1', 5, 'compas+crayon wilson', '2.95', 0, '2025-09-16 19:53:52.000000', '681470'),
(82, 'Compas', NULL, '2025-09-16 19:53:52.000000', 'compas MAPED Bague ref-410', NULL, b'1', 5, 'compas MAPED Bague ref-410', '5.00', 0, '2025-09-16 19:53:52.000000', '119410'),
(83, 'Compas', NULL, '2025-09-16 19:53:52.000000', 'compas stop system crayon MAPED', NULL, b'1', 5, 'compas stop system crayon MAPED', '10.77', 0, '2025-09-16 19:53:52.000000', '965104'),
(84, 'Compas', NULL, '2025-09-16 19:53:52.000000', 'compas study bague maped', NULL, b'1', 5, 'compas study bague maped', '5.09', 0, '2025-09-16 19:53:52.000000', '941023'),
(85, 'Craies', NULL, '2025-09-16 19:53:52.000000', 'craie banche de 10 sakr', NULL, b'1', 20, 'craie banche de 10 sakr', '0.42', 0, '2025-09-16 19:53:52.000000', 'CRAIE 10B'),
(86, 'Craies', NULL, '2025-09-16 19:53:52.000000', 'craie couleur de 10 sakr', NULL, b'1', 20, 'craie couleur de 10 sakr', '0.48', 0, '2025-09-16 19:53:52.000000', 'CRAIE 10C'),
(87, 'Craies', NULL, '2025-09-16 19:53:52.000000', 'craie blanche de 80 sakr', NULL, b'1', 20, 'craie blanche de 80 sakr', '1.34', 0, '2025-09-16 19:53:52.000000', 'CRAIE 80B'),
(88, 'Craies', NULL, '2025-09-16 19:53:52.000000', 'craie couleur de 80 sakr', NULL, b'1', 20, 'craie couleur de 80 sakr', '1.69', 0, '2025-09-16 19:53:52.000000', 'CRAIE 80C'),
(89, 'Craies', NULL, '2025-09-16 19:54:42.000000', 'craie blanche de 10 jovi', NULL, b'1', 20, 'craie blanche de 10 jovi', '2.18', 0, '2025-09-16 19:54:42.000000', '007627'),
(90, 'Craies', NULL, '2025-09-16 19:54:42.000000', 'craie couleur de 10 jovi', NULL, b'1', 20, 'craie couleur de 10 jovi', '2.78', 0, '2025-09-16 19:54:42.000000', '007634'),
(91, 'Craies', NULL, '2025-09-16 19:54:42.000000', 'craie de 10 couleur maped', NULL, b'1', 20, 'craie de 10 couleur maped', '2.81', 0, '2025-09-16 19:54:42.000000', '935011'),
(92, 'Crayons Couleurs', NULL, '2025-09-16 19:54:42.000000', 'crayon de couleur 6/9 vneeds', NULL, b'1', 10, 'crayon de couleur 6/9 vneeds', '0.89', 0, '2025-09-16 19:54:42.000000', 'CC 6/9'),
(93, 'Crayons Couleurs', NULL, '2025-09-16 19:54:42.000000', 'crayon de couleur 6/9 joy', NULL, b'1', 10, 'crayon de couleur 6/9 joy', '0.89', 0, '2025-09-16 19:54:42.000000', '781896'),
(94, 'Crayons Couleurs', NULL, '2025-09-16 19:54:42.000000', 'crayon de couleur 6/9 vertex', NULL, b'1', 10, 'crayon de couleur 6/9 vertex', '1.13', 0, '2025-09-16 19:54:42.000000', 'CC 6/9 V'),
(95, 'Crayons Couleurs', NULL, '2025-09-16 19:54:42.000000', 'crayon de couleur 12/9 vneeds', NULL, b'1', 10, 'crayon de couleur 12/9 vneeds', '1.29', 0, '2025-09-16 19:54:42.000000', '005026'),
(96, 'Crayons Couleurs', NULL, '2025-09-16 19:54:42.000000', 'crayon de couleur 12/9 wilson', NULL, b'1', 10, 'crayon de couleur 12/9 wilson', '1.59', 0, '2025-09-16 19:54:42.000000', '237750'),
(97, 'Crayons Couleurs', NULL, '2025-09-16 19:54:42.000000', 'crayon de couleur 12/18 vneeds', NULL, b'1', 10, 'crayon de couleur 12/18 vneeds', '2.55', 0, '2025-09-16 19:54:42.000000', '005033'),
(98, 'Crayons Couleurs', NULL, '2025-09-16 19:54:42.000000', 'crayon de couleur 12/18 wilson', NULL, b'1', 10, 'crayon de couleur 12/18 wilson', '3.18', 0, '2025-09-16 19:54:42.000000', '716487'),
(99, 'Crayons Couleurs', NULL, '2025-09-16 19:54:59.000000', 'crayon de couleur 12/18 Vneeds V662-12 crayon couleur 12/18 vertex', NULL, b'1', 10, 'crayon de couleur 12/18 vertex', '4.49', 0, '2025-09-16 19:54:59.000000', 'cc12/18 vertex'),
(100, 'Crayons Couleurs', NULL, '2025-09-16 19:54:59.000000', 'crayon de couleur 12/18 c,pastel vneeds', NULL, b'1', 10, 'crayon de couleur 12/18 c,pastel', '3.68', 0, '2025-09-16 19:54:59.000000', '001233'),
(101, 'Crayons Couleurs', NULL, '2025-09-16 19:54:59.000000', 'crayon de couleur 12/18 pastel vneeds', NULL, b'1', 10, 'crayon de couleur 12/18 pastel', '4.38', 0, '2025-09-16 19:54:59.000000', '647993'),
(102, 'Crayons Couleurs', NULL, '2025-09-16 19:54:59.000000', 'crayon de couleur 24/18 vertex', NULL, b'1', 10, 'crayon de couleur 24/18 vertex', '7.72', 0, '2025-09-16 19:54:59.000000', 'CC24/18 vertex'),
(103, 'Crayons Couleurs', NULL, '2025-09-16 19:54:59.000000', 'crayon de couleur 12/18 staedtler+crayon+gomme', NULL, b'1', 10, 'crayon de couleur 12/18 staedtler', '5.18', 0, '2025-09-16 19:54:59.000000', '61set 6'),
(104, 'Crayons Couleurs', NULL, '2025-09-16 19:54:59.000000', 'crayon de couleur 12/18 bic evolution', NULL, b'1', 10, 'crayon de couleur 12/18 bic evolution', '5.01', 0, '2025-09-16 19:54:59.000000', '499102'),
(105, 'Crayons Couleurs', NULL, '2025-09-16 19:54:59.000000', 'crayon couleur 12/9 MAPED', NULL, b'1', 10, 'crayon couleur 12/9 MAPED', '5.14', 0, '2025-09-16 19:54:59.000000', '325000'),
(106, 'Crayons Couleurs', NULL, '2025-09-16 19:54:59.000000', 'crayon couleur 12/18 MAPED strong', NULL, b'1', 10, 'crayon couleur 12/18 MAPED strong', '6.45', 0, '2025-09-16 19:54:59.000000', '627128'),
(107, 'Crayons Couleurs', NULL, '2025-09-16 19:54:59.000000', 'crayon couleur 12/18 triangulaire+taille crayon MAPED', NULL, b'1', 10, 'crayon couleur 12/18 triangulaire MAPED', '7.60', 0, '2025-09-16 19:54:59.000000', '832130'),
(108, 'Crayons Couleurs', NULL, '2025-09-16 19:54:59.000000', 'crayon couleur 12/18 +1duo MAPED', NULL, b'1', 10, 'crayon couleur 12/18 +1duo MAPED', '9.47', 0, '2025-09-16 19:54:59.000000', '320210'),
(109, 'Crayons Noirs', NULL, '2025-09-16 19:56:26.000000', 'crayon noir prestige 12p', NULL, b'1', 10, 'crayon noir prestige 12p', '0.18', 0, '2025-09-16 19:56:26.000000', '7000'),
(110, 'Crayons Noirs', NULL, '2025-09-16 19:56:26.000000', 'crayon noir wilson 12p', NULL, b'1', 10, 'crayon noir wilson 12p', '0.19', 0, '2025-09-16 19:56:26.000000', '105122'),
(111, 'Crayons Noirs', NULL, '2025-09-16 19:56:26.000000', 'Crayon noir STAEDTLER 12p', NULL, b'1', 10, 'Crayon noir STAEDTLER 12p', '0.79', 0, '2025-09-16 19:56:26.000000', '120-'),
(112, 'Crayons Noirs', NULL, '2025-09-16 19:56:26.000000', 'Crayon EL KALEM 12p', NULL, b'1', 10, 'Crayon EL KALEM 12p', '0.25', 0, '2025-09-16 19:56:26.000000', '6000'),
(113, 'Crayons Noirs', NULL, '2025-09-16 19:56:26.000000', 'crayon noir maped 12p', NULL, b'1', 10, 'crayon noir maped 12p', '0.74', 0, '2025-09-16 19:56:26.000000', '500-'),
(114, 'Crayons Noirs', NULL, '2025-09-16 19:56:26.000000', 'crayon noir evolution rose BIC 12p', NULL, b'1', 10, 'crayon noir evolution rose BIC 12p', '0.28', 0, '2025-09-16 19:56:26.000000', '304895'),
(115, 'Crayons Noirs', NULL, '2025-09-16 19:56:26.000000', 'crayon noir evolution vert BIC 12p', NULL, b'1', 10, 'crayon noir evolution vert BIC 12p', '0.37', 0, '2025-09-16 19:56:26.000000', '004608'),
(116, 'Crayons Noirs', NULL, '2025-09-16 19:56:26.000000', 'crayon noir AVEC gomme BIC 12p', NULL, b'1', 10, 'crayon noir AVEC gomme BIC 12p', '0.49', 0, '2025-09-16 19:56:26.000000', '083924'),
(117, 'Crayons Noirs', NULL, '2025-09-16 19:56:26.000000', 'crayon noir BIC STRIPES 12p', NULL, b'1', 10, 'crayon noir BIC STRIPES 12p', '0.42', 0, '2025-09-16 19:56:26.000000', '340312'),
(118, 'Crayons Noirs', NULL, '2025-09-16 19:56:26.000000', 'pot de 36 crayon noir avec gomme ref v0155 couleur fluo', NULL, b'1', 10, 'pot de 36 crayon noir avec gomme fluo', '13.13', 0, '2025-09-16 19:56:26.000000', '004388'),
(119, 'Crayons Noirs', NULL, '2025-09-16 19:58:13.000000', 'pot de 36 crayon noir avec gomme ref v0151 couleur past', NULL, b'1', 10, 'pot de 36 crayon noir avec gomme pastel', '13.13', 0, '2025-09-16 19:58:13.000000', '004418'),
(120, 'Crayons Noirs', NULL, '2025-09-16 19:58:13.000000', 'pot de 30 crayon noir+1gomme+1T-CRAY', NULL, b'1', 10, 'pot de 30 crayon noir+1gomme+1T-CRAY', '11.91', 0, '2025-09-16 19:58:13.000000', '771491'),
(121, 'Crayons Noirs', NULL, '2025-09-16 19:58:13.000000', 'pot de 144 crayon noir STAEDTLER', NULL, b'1', 10, 'pot de 144 crayon noir STAEDTLER', '113.51', 0, '2025-09-16 19:58:13.000000', '120-BT1'),
(122, 'Etiquettes', NULL, '2025-09-16 19:58:13.000000', 'Jetiquette de nom fantaisie 5p', NULL, b'1', 10, 'Jetiquette de nom fantaisie 5p', '1.49', 0, '2025-09-16 19:58:13.000000', 'ETIQ-FAN'),
(123, 'Etiquettes', NULL, '2025-09-16 19:58:13.000000', 'etiquette de faute', NULL, b'1', 10, 'etiquette de faute', '0.25', 0, '2025-09-16 19:58:13.000000', 'ETIQ-FAUTE'),
(124, 'Etiquettes', NULL, '2025-09-16 19:58:13.000000', 'etiquette adhésive 2x4 blanche 25 poch', NULL, b'1', 10, 'etiquette adhésive 2x4 blanche', '0.95', 0, '2025-09-16 19:58:13.000000', 'ET-BY'),
(125, 'Etiquettes', NULL, '2025-09-16 19:58:13.000000', 'etiquette adhésive 2x4 fluo 25 poch', NULL, b'1', 10, 'etiquette adhésive 2x4 fluo', '0.95', 0, '2025-09-16 19:58:13.000000', 'ET-CY'),
(126, 'Feutres', NULL, '2025-09-16 19:58:13.000000', 'feutre de 6 coco hauser germany carton', NULL, b'1', 10, 'feutre de 6 coco hauser germany', '2.08', 0, '2025-09-16 19:58:13.000000', '965433'),
(127, 'Feutres', NULL, '2025-09-16 19:58:13.000000', 'feutre de 12 hauser germany PVC', NULL, b'1', 10, 'feutre de 12 hauser germany PVC', '3.88', 0, '2025-09-16 19:58:13.000000', '965983'),
(128, 'Feutres', NULL, '2025-09-16 19:58:13.000000', 'feutre mini de 12 hauser germany', NULL, b'1', 10, 'feutre mini de 12 hauser germany', '2.08', 0, '2025-09-16 19:58:13.000000', '965082'),
(129, 'Feutres', NULL, '2025-09-16 20:00:11.000000', 'feutre de 6 DOMS', NULL, b'1', 10, 'feutre de 6 DOMS', '2.95', 0, '2025-09-16 20:00:11.000000', '784354'),
(130, 'Feutres', NULL, '2025-09-16 20:00:11.000000', 'feutre de 12 DOMS', NULL, b'1', 10, 'feutre de 12 DOMS', '2.95', 0, '2025-09-16 20:00:11.000000', '784170'),
(131, 'Feutres', NULL, '2025-09-16 20:00:11.000000', 'feutre de 6 Wilson', NULL, b'1', 10, 'feutre de 6 Wilson', '1.88', 0, '2025-09-16 20:00:11.000000', '385817'),
(132, 'Feutres', NULL, '2025-09-16 20:00:11.000000', 'feutre de 12 Wilson', NULL, b'1', 10, 'feutre de 12 Wilson', '3.95', 0, '2025-09-16 20:00:11.000000', '858113'),
(133, 'Feutres', NULL, '2025-09-16 20:00:11.000000', 'feutre de 6 ocean MAPED', NULL, b'1', 10, 'feutre de 6 ocean MAPED', '3.28', 0, '2025-09-16 20:00:11.000000', '457237'),
(134, 'Feutres', NULL, '2025-09-16 20:00:11.000000', 'feutre de 12 ocean MAPED', NULL, b'1', 10, 'feutre de 12 ocean MAPED', '6.56', 0, '2025-09-16 20:00:11.000000', '457206'),
(135, 'Feutres', NULL, '2025-09-16 20:00:11.000000', 'feutre de 12 BIC', NULL, b'1', 10, 'feutre de 12 BIC', '8.13', 0, '2025-09-16 20:00:11.000000', '103226'),
(136, 'Feutres', NULL, '2025-09-16 20:00:11.000000', 'jovi pot de 48 feutre pointe extra large super lavable', NULL, b'1', 10, 'jovi pot de 48 feutres extra large', '55.86', 0, '2025-09-16 20:00:11.000000', '007412'),
(137, 'Feutres-pointe fine', NULL, '2025-09-16 20:00:11.000000', 'feutre pointe fine STEADTLER', NULL, b'1', 10, 'feutre pointe fine STEADTLER', '1.66', 0, '2025-09-16 20:00:11.000000', '334-'),
(138, 'Feutres-pointe fine', NULL, '2025-09-16 20:00:11.000000', 'feutre pointe fine STEADTLER b13', NULL, b'1', 10, 'feutre pointe fine STEADTLER b13', '17.62', 0, '2025-09-16 20:00:11.000000', '334TB13'),
(139, 'Fluo-Marqueurs', NULL, '2025-09-16 20:02:09.000000', 'marqueur fluo purple', NULL, b'1', 10, 'marqueur fluo purple', '1.29', 0, '2025-09-16 20:02:09.000000', '180-'),
(140, 'Fluo-Marqueurs', NULL, '2025-09-16 20:02:09.000000', 'marqueur fluo maped', NULL, b'1', 10, 'marqueur fluo maped', '1.84', 0, '2025-09-16 20:02:09.000000', '425-'),
(141, 'Fluo-Marqueurs', NULL, '2025-09-16 20:02:09.000000', 'marqueur fluo penmark', NULL, b'1', 10, 'marqueur fluo penmark', '1.30', 0, '2025-09-16 20:02:09.000000', 'HS-505'),
(142, 'Fluo-Marqueurs', NULL, '2025-09-16 20:02:09.000000', 'marqueur fluo shneider', NULL, b'1', 10, 'marqueur fluo shneider', '1.35', 0, '2025-09-16 20:02:09.000000', '150-fluo'),
(143, 'Fluo-Marqueurs', NULL, '2025-09-16 20:02:09.000000', 'marqueur staedtler', NULL, b'1', 10, 'marqueur staedtler', '1.89', 0, '2025-09-16 20:02:09.000000', '364'),
(144, 'Fluo-Marqueurs', NULL, '2025-09-16 20:02:09.000000', 'pochette de 4 fluo maped', NULL, b'1', 10, 'pochette de 4 fluo maped', '8.38', 0, '2025-09-16 20:02:09.000000', '425-WP4'),
(145, 'Fluo-Marqueurs', NULL, '2025-09-16 20:02:09.000000', 'pochette de 4 fluo pastel wilson', NULL, b'1', 10, 'pochette de 4 fluo pastel wilson', '3.60', 0, '2025-09-16 20:02:09.000000', '161311'),
(146, 'Fluo-Marqueurs', NULL, '2025-09-16 20:02:09.000000', 'pochette de 4 fluo wilson', NULL, b'1', 10, 'pochette de 4 fluo wilson', '3.60', 0, '2025-09-16 20:02:09.000000', '161335'),
(147, 'Fluo-Marqueurs', NULL, '2025-09-16 20:02:09.000000', 'présentoire de 48 fluo wilson', NULL, b'1', 10, 'présentoire de 48 fluo wilson', '39.00', 0, '2025-09-16 20:02:09.000000', '161304'),
(148, 'Fluo-Marqueurs', NULL, '2025-09-16 20:02:09.000000', 'pot de 45 mini fluo wilson', NULL, b'1', 10, 'pot de 45 mini fluo wilson', '33.00', 0, '2025-09-16 20:02:09.000000', '161854'),
(149, 'Fluo-Marqueurs', NULL, '2025-09-16 20:03:01.000000', 'pot de 24 mini fluo emeraude 4 couleurs', NULL, b'1', 10, 'pot de 24 mini fluo emeraude 4 couleurs', '0.48', 0, '2025-09-16 20:03:01.000000', '160468'),
(150, 'Fluo-Marqueurs', NULL, '2025-09-16 20:03:01.000000', 'pochette de 4 fluo pastel penmark', NULL, b'1', 10, 'pochette de 4 fluo pastel penmark', '7.20', 0, '2025-09-16 20:03:01.000000', '701748'),
(151, 'Fluo-Marqueurs', NULL, '2025-09-16 20:03:01.000000', 'présentoire de 36 fluo penmark', NULL, b'1', 10, 'présentoire de 36 fluo penmark', '61.20', 0, '2025-09-16 20:03:01.000000', '701113'),
(152, 'Fluo-Marqueurs', NULL, '2025-09-16 20:03:01.000000', 'présentoire de 36 fluo pastel penmark', NULL, b'1', 10, 'présentoire de 36 fluo pastel penmark', '61.20', 0, '2025-09-16 20:03:01.000000', '701106'),
(153, 'Gommes', NULL, '2025-09-16 20:03:01.000000', 'gomme blanche classique', NULL, b'1', 10, 'gomme blanche classique', '0.12', 0, '2025-09-16 20:03:01.000000', '001'),
(154, 'Gommes', NULL, '2025-09-16 20:03:01.000000', 'gomme rose/stylo mixte', NULL, b'1', 10, 'gomme rose/stylo mixte', '0.15', 0, '2025-09-16 20:03:01.000000', '002'),
(155, 'Gommes', NULL, '2025-09-16 20:03:01.000000', 'gomme fantaisie', NULL, b'1', 10, 'gomme fantaisie', '0.25', 0, '2025-09-16 20:03:01.000000', '003'),
(156, 'Gommes', NULL, '2025-09-16 20:03:01.000000', 'gomme technique 2B', NULL, b'1', 10, 'gomme technique 2B', '0.40', 0, '2025-09-16 20:03:01.000000', '004'),
(157, 'Gommes', NULL, '2025-09-16 20:03:01.000000', 'gomme électrique pour crayon', NULL, b'1', 10, 'gomme électrique pour crayon', '2.50', 0, '2025-09-16 20:03:01.000000', '005'),
(158, 'Gommes', NULL, '2025-09-16 20:03:01.000000', 'gomme poudre blanche', NULL, b'1', 10, 'gomme poudre blanche', '1.80', 0, '2025-09-16 20:03:01.000000', '006'),
(34389, 'Gommes', NULL, '2025-09-16 20:06:28.000000', 'gomme mini colorée de 100p DOMS', NULL, b'1', 5, 'gomme mini colorée de 100p DOMS', '0.21', 0, '2025-09-16 20:06:28.000000', '034389'),
(34396, 'Gommes', NULL, '2025-09-16 20:06:28.000000', 'gomme mini blanche jarre de 100p DOMS', NULL, b'1', 5, 'gomme mini blanche jarre de 100p DOMS', '0.20', 0, '2025-09-16 20:06:28.000000', '034396'),
(34709, 'Gommes', NULL, '2025-09-16 20:06:28.000000', 'gomme colorée DOMS b20', NULL, b'1', 5, 'gomme colorée DOMS b20', '0.38', 0, '2025-09-16 20:06:28.000000', '034709'),
(34808, 'Gommes', NULL, '2025-09-16 20:06:28.000000', 'gomme fragrance triangulaire de 50p DOMS', NULL, b'1', 5, 'gomme fragrance triangulaire de 50p DOMS', '0.50', 0, '2025-09-16 20:06:28.000000', '034808'),
(34983, 'Gommes', NULL, '2025-09-16 20:06:28.000000', 'gomme fragrance colorée GM DOMS 620', NULL, b'1', 5, 'gomme fragrance colorée GM DOMS 620', '0.68', 0, '2025-09-16 20:06:28.000000', '034983'),
(100086, 'Gouaches', NULL, '2025-09-16 20:06:28.000000', 'gouache de 6 carton coq', NULL, b'1', 5, 'gouache de 6 carton coq', '2.53', 0, '2025-09-16 20:06:28.000000', '100086'),
(100109, 'Gouaches', NULL, '2025-09-16 20:06:28.000000', 'Gouache de 6 cristal coq', NULL, b'1', 5, 'Gouache de 6 cristal coq', '2.68', 0, '2025-09-16 20:06:28.000000', '100109'),
(100116, 'Gouaches', NULL, '2025-09-16 20:06:28.000000', 'gouache de 5 COQ', NULL, b'1', 5, 'gouache de 5 COQ', '2.17', 0, '2025-09-16 20:06:28.000000', '100116'),
(101366, 'Gouaches', NULL, '2025-09-16 20:06:28.000000', 'gouache de 12 cristal coq', NULL, b'1', 5, 'gouache de 12 cristal coq', '5.25', 0, '2025-09-16 20:06:28.000000', '101366'),
(113008, 'Gommes', NULL, '2025-09-16 20:06:28.000000', 'gomme mini tehnic PLUS MAPED b36', NULL, b'1', 5, 'gomme mini tehnic PLUS MAPED b36', '0.56', 0, '2025-09-16 20:06:28.000000', '113008'),
(113050, 'Gommes', NULL, '2025-09-16 20:06:28.000000', 'gomme mini tehnic PLUS MAPED b36', NULL, b'1', 5, 'gomme mini tehnic PLUS MAPED b36', '0.50', 0, '2025-09-16 20:06:28.000000', '113050'),
(116009, 'Gommes', NULL, '2025-09-16 20:06:28.000000', 'gomme technic MAPED b20', NULL, b'1', 5, 'gomme technic MAPED b20', '1.15', 0, '2025-09-16 20:06:28.000000', '116009'),
(116050, 'Gommes', NULL, '2025-09-16 20:06:28.000000', 'gomme technic plus MAPED 620', NULL, b'1', 5, 'gomme technic plus MAPED 620', '0.88', 0, '2025-09-16 20:06:28.000000', '116050'),
(323330, 'Gommes', NULL, '2025-09-16 20:06:28.000000', 'gomme prestige B20', NULL, b'1', 5, 'gomme prestige B20', '0.40', 0, '2025-09-16 20:06:28.000000', '323330'),
(388529, 'Gommes', NULL, '2025-09-16 20:06:28.000000', 'gomme blanche GM Bic', NULL, b'1', 5, 'gomme blanche GM Bic', '1.15', 0, '2025-09-16 20:06:28.000000', '388529'),
(511790, 'Gommes', NULL, '2025-09-16 20:06:28.000000', 'gomme technic softy GM maped b20', NULL, b'1', 5, 'gomme technic softy GM maped b20', '1.24', 0, '2025-09-16 20:06:28.000000', '511790'),
(512565, 'Gommes', NULL, '2025-09-16 20:06:28.000000', 'gomme rougebleu /WINDSOR', NULL, b'1', 5, 'gomme rougebleu /WINDSOR', '0.16', 0, '2025-09-16 20:06:28.000000', '512565'),
(525635, 'Gommes', NULL, '2025-09-16 20:06:28.000000', 'gomme blanche PM HAUSER germany b30', NULL, b'1', 5, 'gomme blanche PM HAUSER germany b30', '0.35', 0, '2025-09-16 20:06:28.000000', '525635'),
(701312, 'Gouaches', NULL, '2025-09-16 20:06:28.000000', 'gouache de 6 majed', NULL, b'1', 5, 'gouache de 6 majed', '2.99', 0, '2025-09-16 20:06:28.000000', '701312'),
(701329, 'Gouaches', NULL, '2025-09-16 20:06:28.000000', 'gouache de 12 cristal majed', NULL, b'1', 5, 'gouache de 12 cristal majed', '5.67', 0, '2025-09-16 20:06:28.000000', '701329'),
(771705, 'Gommes', NULL, '2025-09-16 20:06:28.000000', 'gomme neon DOMS 660', NULL, b'1', 5, 'gomme neon DOMS 660', '0.88', 0, '2025-09-16 20:06:28.000000', '771705'),
(772566, 'Gommes', NULL, '2025-09-16 20:06:28.000000', 'gomme colorée jarre de 30p DOMS', NULL, b'1', 5, 'gomme colorée jarre de 30p DOMS', '0.24', 0, '2025-09-16 20:06:28.000000', '772566'),
(781728, 'Gommes', NULL, '2025-09-16 20:06:28.000000', 'gomme blanche dust free de 20p DOMS', NULL, b'1', 5, 'gomme blanche dust free de 20p DOMS', '0.40', 0, '2025-09-16 20:06:28.000000', '781728'),
(784248, 'Gommes', NULL, '2025-09-16 20:06:28.000000', 'gomme cubo dost free DOMS b20', NULL, b'1', 5, 'gomme cubo dost free DOMS b20', '0.69', 0, '2025-09-16 20:06:28.000000', '784248'),
(965136, 'Gouaches', NULL, '2025-09-16 20:06:28.000000', 'gouache liquide de 6 couleurs HAUSER 10ml x6', NULL, b'1', 5, 'gouache liquide de 6 couleurs HAUSER 10ml x6', '9.85', 0, '2025-09-16 20:06:28.000000', '965136'),
(965143, 'Gouaches', NULL, '2025-09-16 20:06:28.000000', 'gouache liquide de 12 couleurs HAUSER10ml x12', NULL, b'1', 5, 'gouache liquide de 12 couleurs HAUSER10ml x12', '18.98', 0, '2025-09-16 20:06:28.000000', '965143'),
(965216, 'Gouaches', NULL, '2025-09-16 20:15:47.000000', 'acrylique 75ml boite de 5 couleur Majed', NULL, b'1', 5, 'acrylique 75ml boite de 5 couleur Majed', '12.19', 50, '2025-09-16 20:15:47.000000', '701091'),
(965217, 'Gouaches', NULL, '2025-09-16 20:15:47.000000', 'acrylique boite de 12 couleurs wneeds', NULL, b'1', 5, 'acrylique boite de 12 couleurs wneeds', '7.35', 50, '2025-09-16 20:15:47.000000', '004876'),
(965218, 'Gouaches', NULL, '2025-09-16 20:15:47.000000', 'super glue pasco 1gr x3', NULL, b'1', 5, 'super glue pasco 1gr x3', '3.11', 50, '2025-09-16 20:15:47.000000', '100656'),
(965219, 'Gouaches', NULL, '2025-09-16 20:15:47.000000', 'super glue pasco 5 gr', NULL, b'1', 5, 'super glue pasco 5 gr', '2.29', 50, '2025-09-16 20:15:47.000000', '102186'),
(965220, 'Marqueurs Tableau', NULL, '2025-09-16 20:15:47.000000', 'marq tableau double face', NULL, b'1', 5, 'marq tableau double face', '1.00', 50, '2025-09-16 20:15:47.000000', '810086'),
(965221, 'Marqueurs Tableau', NULL, '2025-09-16 20:15:47.000000', 'marqueur tableau bronsPROMO', NULL, b'1', 5, 'marqueur tableau bronsPROMO', '0.62', 50, '2025-09-16 20:15:47.000000', '0085'),
(965222, 'Marqueurs Tableau', NULL, '2025-09-16 20:15:47.000000', 'marqueur tableau DOMSPROMO', NULL, b'1', 5, 'marqueur tableau DOMSPROMO', '1.09', 50, '2025-09-16 20:15:47.000000', '7743'),
(965223, 'Marqueurs Tableau', NULL, '2025-09-16 20:15:47.000000', 'margeur tableau blanc fine purple', NULL, b'1', 5, 'margeur tableau blanc fine purple', '0.89', 50, '2025-09-16 20:15:47.000000', '1809'),
(965224, 'Marqueurs Tableau', NULL, '2025-09-16 20:15:47.000000', 'margeur tableau blanc fine BIC', NULL, b'1', 5, 'margeur tableau blanc fine BIC', '1.36', 50, '2025-09-16 20:15:47.000000', '1721'),
(965225, 'Marqueurs Tableau', NULL, '2025-09-16 20:15:47.000000', 'marqueur tableau 2 couleur', NULL, b'1', 5, 'marqueur tableau 2 couleur', '0.40', 50, '2025-09-16 20:15:47.000000', '911004'),
(965226, 'Marqueurs Tableau', NULL, '2025-09-16 20:15:47.000000', 'margeur tableau blanc BIC', NULL, b'1', 5, 'margeur tableau blanc BIC', '1.94', 50, '2025-09-16 20:15:47.000000', '1701'),
(965227, 'Marqueurs Tableau', NULL, '2025-09-16 20:15:47.000000', 'marqueur tableau STEADTLER', NULL, b'1', 5, 'marqueur tableau STEADTLER', '1.82', 50, '2025-09-16 20:15:47.000000', '351'),
(965228, 'Marqueurs - permenant - CD', NULL, '2025-09-16 20:15:47.000000', 'marqueur permenant Hitext-', NULL, b'1', 5, 'marqueur permenant Hitext-', '0.95', 50, '2025-09-16 20:15:47.000000', '001125'),
(965229, 'Marqueurs - permenant - CD', NULL, '2025-09-16 20:15:47.000000', 'marqueur permanent BIC', NULL, b'1', 5, 'marqueur permanent BIC', '1.39', 50, '2025-09-16 20:15:47.000000', '2300'),
(965230, 'Marqueurs - permenant - CD', NULL, '2025-09-16 20:15:47.000000', 'marqueur permanent GIXIN', NULL, b'1', 5, 'marqueur permanent GIXIN', '0.85', 50, '2025-09-16 20:15:47.000000', '711133'),
(965231, 'Marqueurs - permenant - CD', NULL, '2025-09-16 20:15:47.000000', 'marqueur CD permenant SHNEIDER', NULL, b'1', 5, 'marqueur CD permenant SHNEIDER', '2.55', 50, '2025-09-16 20:15:47.000000', '224M'),
(965293, 'Pastels', NULL, '2025-09-16 20:24:10.000000', 'crayon pastel 06 VNEEDS', NULL, b'1', 5, 'crayon pastel 06 VNEEDS', '0.73', 50, '2025-09-16 20:24:10.000000', '002315'),
(965294, 'Pastels', NULL, '2025-09-16 20:24:10.000000', 'pastel de 12 BIC', NULL, b'1', 5, 'pastel de 12 BIC', '7.84', 50, '2025-09-16 20:24:10.000000', '000341'),
(965295, 'Pastels', NULL, '2025-09-16 20:24:10.000000', 'pastel gros de 12 BIC', NULL, b'1', 5, 'pastel gros de 12 BIC', '9.68', 50, '2025-09-16 20:24:10.000000', '000789'),
(965296, 'Pates a modeler', NULL, '2025-09-16 20:24:10.000000', 'pate a modeler 5/9 majed', NULL, b'1', 5, 'pate a modeler 5/9 majed', '1.03', 50, '2025-09-16 20:24:10.000000', '700407'),
(965297, 'Pates a modeler', NULL, '2025-09-16 20:24:10.000000', 'pate a modeler de 10 avec kalkal Majed', NULL, b'1', 5, 'pate a modeler de 10 avec kalkal Majed', '2.23', 50, '2025-09-16 20:24:10.000000', '701077'),
(965305, 'Pates a modeler', NULL, '2025-09-16 20:30:55.000000', 'pate a modeler pastel de 6 kiddy clay', NULL, b'1', 5, 'pate a modeler pastel de 6 kiddy clay', '1.80', 50, '2025-09-16 20:30:55.000000', '026815'),
(965306, 'Pates a modeler', NULL, '2025-09-16 20:30:55.000000', 'pate a modeler de 8 fluo kid art', NULL, b'1', 5, 'pate a modeler de 8 fluo kid art', '2.47', 50, '2025-09-16 20:30:55.000000', '777757-'),
(965307, 'Pates a modeler', NULL, '2025-09-16 20:30:55.000000', 'pate a modeler de 8 kiddy clay', NULL, b'1', 5, 'pate a modeler de 8 kiddy clay', '1.91', 50, '2025-09-16 20:30:55.000000', '000310'),
(965308, 'Pates a modeler', NULL, '2025-09-16 20:30:55.000000', 'pate a modeler de 8 kid art ref-T108B-DI', NULL, b'1', 5, 'pate a modeler de 8 kid art ref-T108B-DI', '2.26', 50, '2025-09-16 20:30:55.000000', '462011'),
(965309, 'Pates a modeler', NULL, '2025-09-16 20:30:55.000000', 'pate a modeler de 12 kid art', NULL, b'1', 5, 'pate a modeler de 12 kid art', '3.26', 50, '2025-09-16 20:30:55.000000', '862002'),
(965310, 'Pates a modeler', NULL, '2025-09-16 20:30:55.000000', 'pate a modeler de 12 fluo kid art', NULL, b'1', 5, 'pate a modeler de 12 fluo kid art', '3.52', 50, '2025-09-16 20:30:55.000000', '776866'),
(965311, 'Pates a modeler', NULL, '2025-09-16 20:30:55.000000', 'pate a modeler 12 glitter kid art', NULL, b'1', 5, 'pate a modeler 12 glitter kid art', '3.73', 50, '2025-09-16 20:30:55.000000', '819945'),
(965312, 'Pates a modeler', NULL, '2025-09-16 20:30:55.000000', 'pate a modeler D-1440-24 kiddy clay', NULL, b'1', 5, 'pate a modeler D-1440-24 kiddy clay', '36.15', 50, '2025-09-16 20:30:55.000000', '002383'),
(965313, 'Pates a modeler', NULL, '2025-09-16 20:30:55.000000', 'pate a modeler D-1440-24N kiddy clay', NULL, b'1', 5, 'pate a modeler D-1440-24N kiddy clay', '37.85', 50, '2025-09-16 20:30:55.000000', '027027'),
(965314, 'Pates a modeler', NULL, '2025-09-16 20:30:55.000000', 'pate a modeler D-1440-24P kiddy clay', NULL, b'1', 5, 'pate a modeler D-1440-24P kiddy clay', '37.85', 50, '2025-09-16 20:30:55.000000', '027034'),
(965315, 'Pates a modeler', NULL, '2025-09-16 20:30:55.000000', 'pate a modeler 200-12N+2M kiddy clay', NULL, b'1', 5, 'pate a modeler 200-12N+2M kiddy clay', '6.86', 50, '2025-09-16 20:30:55.000000', '026891'),
(965316, 'Pates a modeler', NULL, '2025-09-16 20:30:55.000000', 'pate a modeler 200-12P+2M kiddy clay', NULL, b'1', 5, 'pate a modeler 200-12P+2M kiddy clay', '6.86', 50, '2025-09-16 20:30:55.000000', 'V80-'),
(965317, 'Pates a modeler', NULL, '2025-09-16 20:30:55.000000', 'pate a modeler 10 barres JOVI', NULL, b'1', 5, 'pate a modeler 10 barres JOVI', '7.90', 50, '2025-09-16 20:30:55.000000', '002097'),
(965318, 'Pates a modeler', NULL, '2025-09-16 20:30:55.000000', 'pate a modeler 10 barres JOVI', NULL, b'1', 5, 'pate a modeler 10 barres JOVI', '7.90', 50, '2025-09-16 20:30:55.000000', '000864'),
(965319, 'Pates a modeler', NULL, '2025-09-16 20:30:55.000000', 'pate 650gr + accessoires JOVI', NULL, b'1', 5, 'pate 650gr + accessoires JOVI', '15.15', 50, '2025-09-16 20:30:55.000000', '001038'),
(965320, 'Pates a modeler', NULL, '2025-09-16 20:30:55.000000', 'jovi pate a modeler de 15 batons JOVI', NULL, b'1', 5, 'jovi pate a modeler de 15 batons JOVI', '7.70', 50, '2025-09-16 20:30:55.000000', '002158'),
(965321, 'Pates a modeler', NULL, '2025-09-16 20:30:55.000000', 'pate a modeler de 8 batons JOVI', NULL, b'1', 5, 'pate a modeler de 8 batons JOVI', '7.70', 50, '2025-09-16 20:30:55.000000', '031578'),
(965322, 'Pates a modeler', NULL, '2025-09-16 20:30:55.000000', 'pate a modeler 30 50gr assortis JOVI', NULL, b'1', 5, 'pate a modeler 30 50gr assortis JOVI', '5.20', 50, '2025-09-16 20:30:55.000000', '002875'),
(965323, 'Pates a modeler', NULL, '2025-09-16 20:30:55.000000', 'pate a modeler 30 50gr assortis JOVI', NULL, b'1', 5, 'pate a modeler 30 50gr assortis JOVI', '8.27', 50, '2025-09-16 20:30:55.000000', '003377'),
(965324, 'Pates a modeler', NULL, '2025-09-16 20:30:55.000000', 'pate a modeler 30 50 / 6 couleur fluo JOVI', NULL, b'1', 5, 'pate a modeler 30 50 / 6 couleur fluo JOVI', '42.40', 50, '2025-09-16 20:30:55.000000', '031936'),
(965325, 'Pates a modeler', NULL, '2025-09-16 20:30:55.000000', 'pate a modeler 3050 6 couleur pastel JOVI', NULL, b'1', 5, 'pate a modeler 3050 6 couleur pastel JOVI', '40.20', 50, '2025-09-16 20:30:55.000000', '002547'),
(965326, 'Pates a modeler', NULL, '2025-09-16 20:30:55.000000', 'pate a modeler 15150 gr 5 couleur JOVI', NULL, b'1', 5, 'pate a modeler 15150 gr 5 couleur JOVI', '46.20', 50, '2025-09-16 20:30:55.000000', '002707'),
(965327, 'Pinceaux', NULL, '2025-09-16 20:32:23.000000', 'pinceau 4', NULL, b'1', 5, 'pinceau 4', '0.53', 50, '2025-09-16 20:32:23.000000', 'P4-CH'),
(965328, 'Pinceaux', NULL, '2025-09-16 20:32:23.000000', 'pinceau 6', NULL, b'1', 5, 'pinceau 6', '0.53', 50, '2025-09-16 20:32:23.000000', 'P6-CH'),
(965329, 'Pinceaux', NULL, '2025-09-16 20:32:23.000000', 'pinceau 8', NULL, b'1', 5, 'pinceau 8', '0.58', 50, '2025-09-16 20:32:23.000000', 'P8-CH'),
(965330, 'Pinceaux', NULL, '2025-09-16 20:32:23.000000', 'pinceau 10', NULL, b'1', 5, 'pinceau 10', '0.62', 50, '2025-09-16 20:32:23.000000', 'P10-CH'),
(965331, 'Pinceaux', NULL, '2025-09-16 20:32:23.000000', 'pinceau 12', NULL, b'1', 5, 'pinceau 12', '0.67', 50, '2025-09-16 20:32:23.000000', 'P12-CH'),
(965332, 'Pinceaux', NULL, '2025-09-16 20:32:23.000000', 'pinceau 12', NULL, b'1', 5, 'pinceau 12', '0.72', 50, '2025-09-16 20:32:23.000000', 'P14-CH'),
(965333, 'Pinceaux', NULL, '2025-09-16 20:32:23.000000', 'pinceau 14', NULL, b'1', 5, 'pinceau 14', '0.78', 50, '2025-09-16 20:32:23.000000', 'P16-CH'),
(965334, 'Pinceaux', NULL, '2025-09-16 20:32:23.000000', 'pinceau 16', NULL, b'1', 5, 'pinceau 16', '0.82', 50, '2025-09-16 20:32:23.000000', 'P18-CH'),
(965335, 'Pinceaux', NULL, '2025-09-16 20:32:23.000000', 'pochette de 06 pinceaux', NULL, b'1', 5, 'pochette de 06 pinceaux', '0.87', 50, '2025-09-16 20:32:23.000000', 'P6P'),
(965336, 'Pinceaux', NULL, '2025-09-16 20:32:23.000000', 'pochette de 12 pinceaux', NULL, b'1', 5, 'pochette de 12 pinceaux', '2.06', 50, '2025-09-16 20:32:23.000000', '706925'),
(965337, 'Porte Documents', NULL, '2025-09-16 20:32:23.000000', 'porte documents 20 v office', NULL, b'1', 5, 'porte documents 20 v office', '2.22', 50, '2025-09-16 20:32:23.000000', 'V20-'),
(965338, 'Porte Documents', NULL, '2025-09-16 20:32:23.000000', 'porte documents 40 v office', NULL, b'1', 5, 'porte documents 40 v office', '3.04', 50, '2025-09-16 20:32:23.000000', 'V40'),
(965339, 'Porte Documents', NULL, '2025-09-16 20:32:23.000000', 'porte documents 60 v office', NULL, b'1', 5, 'porte documents 60 v office', '3.04', 50, '2025-09-16 20:32:23.000000', 'V60-'),
(965340, 'Porte Documents', NULL, '2025-09-16 20:32:23.000000', 'porte documents 80 v office', NULL, b'1', 5, 'porte documents 80 v office', '4.68', 50, '2025-09-16 20:32:23.000000', 'V100-'),
(965341, 'Porte Documents', NULL, '2025-09-16 20:32:23.000000', 'porte documents 100 v office', NULL, b'1', 5, 'porte documents 100 v office', '5.49', 50, '2025-09-16 20:32:23.000000', 'V120T-'),
(965342, 'Porte Documents', NULL, '2025-09-16 20:32:23.000000', 'porte documents 120 v trambo', NULL, b'1', 5, 'porte documents 120 v trambo', '5.59', 50, '2025-09-16 20:32:23.000000', 'V120-'),
(965343, 'Porte Documents', NULL, '2025-09-16 20:32:23.000000', 'porte documents 120 v office', NULL, b'1', 5, 'porte documents 120 v office', '6.31', 50, '2025-09-16 20:32:23.000000', 'V140-'),
(965344, 'Porte Documents', NULL, '2025-09-16 20:32:23.000000', 'porte documents 160 v trambo', NULL, b'1', 5, 'porte documents 160 v trambo', '7.13', 50, '2025-09-16 20:32:23.000000', 'V160T'),
(965345, 'Porte Documents', NULL, '2025-09-16 20:32:23.000000', 'porte documents 160 v office', NULL, b'1', 5, 'porte documents 160 v office', '6.63', 50, '2025-09-16 20:32:23.000000', 'V160-'),
(965346, 'Porte Documents', NULL, '2025-09-16 20:32:23.000000', 'porte documents 180 v office', NULL, b'1', 5, 'porte documents 180 v office', '7.94', 50, '2025-09-16 20:32:23.000000', 'V180-'),
(965347, 'Porte Documents', NULL, '2025-09-16 20:32:23.000000', 'porte documents 200 v office', NULL, b'1', 5, 'porte documents 200 v office', '8.82', 50, '2025-09-16 20:32:23.000000', 'V200-'),
(965348, 'Porte Documents', NULL, '2025-09-16 20:32:23.000000', 'porte documents 260 v office', NULL, b'1', 5, 'porte documents 260 v office', '13.21', 50, '2025-09-16 20:32:23.000000', 'V260-'),
(965349, 'Porte Documents', NULL, '2025-09-16 20:32:23.000000', 'porte documents 300 v office', NULL, b'1', 5, 'porte documents 300 v office', '15.27', 50, '2025-09-16 20:32:23.000000', 'V300-'),
(965350, 'Porte Documents', NULL, '2025-09-16 20:32:23.000000', 'porte documents 400v office', NULL, b'1', 5, 'porte documents 400v office', '18.98', 50, '2025-09-16 20:32:23.000000', 'V400'),
(965351, 'Porte Documents', NULL, '2025-09-16 20:32:23.000000', 'porte documents 500V office', NULL, b'1', 5, 'porte documents 500V office', '18.98', 50, '2025-09-16 20:32:23.000000', 'V500'),
(965352, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'protege cahier plume PM', NULL, b'1', 5, 'protege cahier plume PM', '0.46', 50, '2025-09-16 20:34:10.000000', 'PC PLUME'),
(965353, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'protege cahier LINO PM', NULL, b'1', 5, 'protege cahier LINO PM', '0.48', 50, '2025-09-16 20:34:10.000000', 'PC LINO PM'),
(965354, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'protege cahier transparant PM', NULL, b'1', 5, 'protege cahier transparant PM', '0.38', 50, '2025-09-16 20:34:10.000000', 'PCT PM'),
(965355, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'protege cahier cristal couleur PM', NULL, b'1', 5, 'protege cahier cristal couleur PM', '0.38', 50, '2025-09-16 20:34:10.000000', 'PCTC PM'),
(965356, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'protege cahier VICHY pm (à carreaux)', NULL, b'1', 5, 'protege cahier VICHY pm (à carreaux)', '0.58', 50, '2025-09-16 20:34:10.000000', 'PC VICHY PM'),
(965357, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'protege cahier glossy pm', NULL, b'1', 5, 'protege cahier glossy pm', '0.55', 50, '2025-09-16 20:34:10.000000', 'PC GLOSSY PN'),
(965358, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'protege cahier laser fantaisie', NULL, b'1', 5, 'protege cahier laser fantaisie', '0.55', 50, '2025-09-16 20:34:10.000000', '897887'),
(965359, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'protége cahier musique', NULL, b'1', 5, 'protége cahier musique', '0.42', 50, '2025-09-16 20:34:10.000000', 'PCM'),
(965360, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'Protege cahier transp GM', NULL, b'1', 5, 'Protege cahier transp GM', '0.62', 50, '2025-09-16 20:34:10.000000', 'PCT GM'),
(965361, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'protege cahier uni GM diamement', NULL, b'1', 5, 'protege cahier uni GM diamement', '0.50', 50, '2025-09-16 20:34:10.000000', 'PCU GM'),
(965362, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'protege cahier LINO GM', NULL, b'1', 5, 'protege cahier LINO GM', '0.69', 50, '2025-09-16 20:34:10.000000', 'PCL GM'),
(965363, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'protege cahier transp coul GM', NULL, b'1', 5, 'protege cahier transp coul GM', '0.69', 50, '2025-09-16 20:34:10.000000', 'PCTC GM'),
(965364, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'protege cahier VICHY GM (à carreaux)', NULL, b'1', 5, 'protege cahier VICHY GM (à carreaux)', '0.83', 50, '2025-09-16 20:34:10.000000', 'PC VICHY GM'),
(965365, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'protege cahier 2432 transparant', NULL, b'1', 5, 'protege cahier 2432 transparant', '1.01', 50, '2025-09-16 20:34:10.000000', 'PCT 24 32'),
(965366, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'protege cahier 2432 transparant couleur', NULL, b'1', 5, 'protege cahier 2432 transparant couleur', '1.01', 50, '2025-09-16 20:34:10.000000', 'PCT 24 32C'),
(965367, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'protege livre transparant CNP', NULL, b'1', 5, 'protege livre transparant CNP', '0.57', 50, '2025-09-16 20:34:10.000000', 'PL-TR CNP'),
(965368, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'protege livre couleur CNP', NULL, b'1', 5, 'protege livre couleur CNP', '0.57', 50, '2025-09-16 20:34:10.000000', 'PL-CL CNP'),
(965369, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'protege livre laser fantaisie', NULL, b'1', 5, 'protege livre laser fantaisie', '0.64', 50, '2025-09-16 20:34:10.000000', '897894');
INSERT INTO `products` (`product_id`, `category`, `color`, `created_at`, `description`, `image_url`, `is_active`, `low_stock_threshold`, `name`, `price`, `stock_quantity`, `updated_at`, `code_article`) VALUES
(965370, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'couvre livre 60 mic', NULL, b'1', 5, 'couvre livre 60 mic', '0.43', 50, '2025-09-16 20:34:10.000000', 'CL-60 MIC'),
(965371, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'couvre livre 80 mic', NULL, b'1', 5, 'couvre livre 80 mic', '0.55', 50, '2025-09-16 20:34:10.000000', 'CL-80 MIC'),
(965372, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'couvre livre adhésif plat office', NULL, b'1', 5, 'couvre livre adhésif plat office', '8.61', 50, '2025-09-16 20:34:10.000000', '146946'),
(965373, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'couvre livre adhesif pq10 fant', NULL, b'1', 5, 'couvre livre adhesif pq10 fant', '7.86', 50, '2025-09-16 20:34:10.000000', '771232'),
(965374, 'Protege Cahiers - Livres', NULL, '2025-09-16 20:34:10.000000', 'couvre livre adhesif transparant couleur', NULL, b'1', 5, 'couvre livre adhesif transparant couleur', '7.39', 50, '2025-09-16 20:34:10.000000', '54321-'),
(965400, 'Scotchs', NULL, '2025-09-16 20:39:37.000000', 'Scotch ecolier', NULL, b'1', 5, 'SCOT EC', '0.22', 50, '2025-09-16 20:39:37.000000', 'SCOT EC'),
(965401, 'Scotchs', NULL, '2025-09-16 20:39:37.000000', 'scotch ecolier fluo', NULL, b'1', 5, 'SCOT EC FL', '0.19', 50, '2025-09-16 20:39:37.000000', 'SCOT EC FL'),
(965402, 'Scotchs', NULL, '2025-09-16 20:39:37.000000', 'scotch 20/18', NULL, b'1', 5, 'SCOT20/18', '0.40', 50, '2025-09-16 20:39:37.000000', 'SCOT20/18'),
(965403, 'Scotchs', NULL, '2025-09-16 20:39:37.000000', 'Scotch 30/18', NULL, b'1', 5, 'SCOT3018/', '0.50', 50, '2025-09-16 20:39:37.000000', 'SCOT3018/'),
(965404, 'Scotchs', NULL, '2025-09-16 20:39:37.000000', 'scotch 60/18 diamétre GM', NULL, b'1', 5, 'SCOT6018/', '1.13', 50, '2025-09-16 20:39:37.000000', 'SCOT6018/'),
(965405, 'Scotchs', NULL, '2025-09-16 20:39:37.000000', 'scotch 60/18 diamétre PM', NULL, b'1', 5, 'SCOT60/18', '0.76', 50, '2025-09-16 20:39:37.000000', 'SCOT60/18'),
(965406, 'Scotchs', NULL, '2025-09-16 20:39:37.000000', 'scotch havan Transparent / Marron', NULL, b'1', 5, 'HAVAN', '1.80', 50, '2025-09-16 20:39:37.000000', 'HAVAN'),
(965407, 'Scotchs', NULL, '2025-09-16 20:39:37.000000', 'scotch de bord 45/5 coloré', NULL, b'1', 5, 'SCOT-BORD', '2.63', 50, '2025-09-16 20:39:37.000000', 'SCOT-BORD'),
(965408, 'Stylos', NULL, '2025-09-16 20:39:37.000000', 'stylo TRIO unimax (en promotion)', NULL, b'1', 5, 'TRIO', '0.43', 50, '2025-09-16 20:39:37.000000', 'TRIO'),
(965409, 'Stylos', NULL, '2025-09-16 20:39:37.000000', 'stylo TRIO fifty unimax (en promotion)', NULL, b'1', 5, 'TRIO FIFTY', '0.49', 50, '2025-09-16 20:39:37.000000', 'TRIO FIFTY'),
(965410, 'Stylos', NULL, '2025-09-16 20:39:37.000000', 'stylo a bille SHNEIDER', NULL, b'1', 5, '505M', '0.50', 50, '2025-09-16 20:39:37.000000', '505M'),
(965411, 'Stylos', NULL, '2025-09-16 20:39:37.000000', 'stylo SLIDER XB BASIC SHNEIDER', NULL, b'1', 5, '1512-', '2.75', 50, '2025-09-16 20:39:37.000000', '1512-'),
(965412, 'Stylos', NULL, '2025-09-16 20:39:37.000000', 'stylo slider edge xb SHNEIDER', NULL, b'1', 5, '1522', '2.50', 50, '2025-09-16 20:39:37.000000', '1522'),
(965413, 'Stylos', NULL, '2025-09-16 20:39:37.000000', 'stylo a bille STEADTLER 430', NULL, b'1', 5, '430-', '0.56', 50, '2025-09-16 20:39:37.000000', '430-'),
(965414, 'Stylos', NULL, '2025-09-16 20:39:37.000000', 'stylo point fine stedtler', NULL, b'1', 5, '334', '1.67', 50, '2025-09-16 20:39:37.000000', '334'),
(965415, 'Stylos', NULL, '2025-09-16 20:39:37.000000', 'poch de 13 sylos point fine steadtler', NULL, b'1', 5, '334-TB13', '17.62', 50, '2025-09-16 20:39:37.000000', '334-TB13'),
(965416, 'Stylos', NULL, '2025-09-16 20:39:37.000000', 'stylo a bille PENSAN', NULL, b'1', 5, '2021', '0.38', 50, '2025-09-16 20:39:37.000000', '2021'),
(965417, 'Stylos', NULL, '2025-09-16 20:39:37.000000', 'stylo MY-TECH PENSAN', NULL, b'1', 5, '2240', '0.50', 50, '2025-09-16 20:39:37.000000', '2240'),
(965418, 'Stylos', NULL, '2025-09-16 20:39:37.000000', 'stylo TRIBAL PENSAN', NULL, b'1', 5, '1003-', '0.52', 50, '2025-09-16 20:39:37.000000', '1003-'),
(965419, 'Stylos', NULL, '2025-09-16 20:39:37.000000', 'stylo STAR TECH PENSAN', NULL, b'1', 5, '2260', '0.50', 50, '2025-09-16 20:39:37.000000', '2260'),
(965420, 'Stylos', NULL, '2025-09-16 20:39:37.000000', 'stylo gel glitter PENSAN', NULL, b'1', 5, '2280', '0.95', 50, '2025-09-16 20:39:37.000000', '2280'),
(965421, 'Stylos', NULL, '2025-09-16 20:39:37.000000', 'presentoire de 60 stylo MY-TECH PENSAN', NULL, b'1', 5, '2240-PRES', '32.26', 50, '2025-09-16 20:39:37.000000', '2240-PRES'),
(965422, 'Stylos', NULL, '2025-09-16 20:39:37.000000', 'presentoire de 60 stylo TRIBALL PENSAN', NULL, b'1', 5, '1003-PRES', '32.40', 50, '2025-09-16 20:39:37.000000', '1003-PRES'),
(965423, 'Stylos', NULL, '2025-09-16 20:39:37.000000', 'presentoire de 60 stylo SOFT GEL PENSAN', NULL, b'1', 5, '2400-', '57.08', 50, '2025-09-16 20:39:37.000000', '2400-'),
(965424, 'Stylos', NULL, '2025-09-16 20:39:37.000000', 'pochette de 10 stylo my-tech PENSAN', NULL, b'1', 5, '2240-W10', '6.19', 50, '2025-09-16 20:39:37.000000', '2240-W10'),
(965425, 'Stylos', NULL, '2025-09-16 20:39:37.000000', 'pochette de 10 stylo triball PENSAN', NULL, b'1', 5, '1003-W10', '6.33', 50, '2025-09-16 20:39:37.000000', '1003-W10'),
(965426, 'Stylos', NULL, '2025-09-16 20:39:37.000000', 'stylo a bille cristal bic', NULL, b'1', 5, '3000', '0.44', 50, '2025-09-16 20:39:37.000000', '3000'),
(965427, 'Stylos', NULL, '2025-09-16 20:39:37.000000', 'stylo a bille soft bic', NULL, b'1', 5, '3553', '0.51', 50, '2025-09-16 20:39:37.000000', '3553'),
(965428, 'Stylos', NULL, '2025-09-16 20:39:37.000000', 'stylo GRIP BIC', NULL, b'1', 5, '0040-GRIP', '0.56', 50, '2025-09-16 20:39:37.000000', '0040-GRIP'),
(965429, 'Stylos', NULL, '2025-09-16 20:39:37.000000', 'pochette de 4 stylo cristal bic', NULL, b'1', 5, '601033', '1.90', 50, '2025-09-16 20:39:37.000000', '601033'),
(965430, 'Stylos', NULL, '2025-09-16 20:40:57.000000', 'stylo fachion bic', NULL, b'1', 5, '462-fachion', '0.57', 50, '2025-09-16 20:40:57.000000', '462-fachion'),
(965431, 'Stylos', NULL, '2025-09-16 20:40:57.000000', 'pochette de 4 stylo soft bic', NULL, b'1', 5, '002012', '2.42', 50, '2025-09-16 20:40:57.000000', '002012'),
(965432, 'Stylos', NULL, '2025-09-16 20:40:57.000000', 'stylo tic tac 4 couleur de base bic', NULL, b'1', 5, '246232', '3.47', 50, '2025-09-16 20:40:57.000000', '246232'),
(965433, 'Stylos', NULL, '2025-09-16 20:40:57.000000', 'boite de 10 stylo cristal soft BIC', NULL, b'1', 5, '001503', '4.29', 50, '2025-09-16 20:40:57.000000', '001503'),
(965434, 'Stylos', NULL, '2025-09-16 20:40:57.000000', 'stylo cristal up bleu B20 BIC', NULL, b'1', 5, '494725', '0.61', 50, '2025-09-16 20:40:57.000000', '494725'),
(965435, 'Stylos', NULL, '2025-09-16 20:40:57.000000', 'stylo cristal up fifty B20 BIC', NULL, b'1', 5, '498341', '0.96', 50, '2025-09-16 20:40:57.000000', '498341'),
(965436, 'Stylos', NULL, '2025-09-16 20:40:57.000000', 'stylo gel-ocity stic bleu BIC', NULL, b'1', 5, '546295', '1.34', 50, '2025-09-16 20:40:57.000000', '546295'),
(965437, 'Stylos', NULL, '2025-09-16 20:40:57.000000', 'stylo gel-ocity stic noir BIC', NULL, b'1', 5, '546301', '1.34', 50, '2025-09-16 20:40:57.000000', '546301'),
(965438, 'Stylos', NULL, '2025-09-16 20:40:57.000000', 'stylo two tone Montex b50promo', NULL, b'1', 5, '0019', '0.42', 50, '2025-09-16 20:40:57.000000', '0019'),
(965439, 'Stylos', NULL, '2025-09-16 20:40:57.000000', 'stylo two tone Montex fifty b50promo', NULL, b'1', 5, 'FIFTY', '0.44', 50, '2025-09-16 20:40:57.000000', 'FIFTY'),
(965440, 'Stylos', NULL, '2025-09-16 20:40:57.000000', 'pochette de 10 stylo montex triconepromo', NULL, b'1', 5, '285012-P', '4.54', 50, '2025-09-16 20:40:57.000000', '285012-P'),
(965441, 'Stylos', NULL, '2025-09-16 20:40:57.000000', 'stylo tricon transparentpromo', NULL, b'1', 5, '285012-S', '0.36', 50, '2025-09-16 20:40:57.000000', '285012-S'),
(965442, 'Stylos', NULL, '2025-09-16 20:40:57.000000', 'pochette de 6 stylo montex two tonepromo', NULL, b'1', 5, '029821', '3.44', 50, '2025-09-16 20:40:57.000000', '029821'),
(965443, 'Stylos', NULL, '2025-09-16 20:40:57.000000', 'pochette de 10 stylo two tone montexpromo', NULL, b'1', 5, '029838', '4.78', 50, '2025-09-16 20:40:57.000000', '029838'),
(965444, 'Stylos', NULL, '2025-09-16 20:40:57.000000', 'pres stylo fifty two tone montex boite de 80promo', NULL, b'1', 5, '029814', '39.68', 50, '2025-09-16 20:40:57.000000', '029814'),
(965445, 'Stylos', NULL, '2025-09-16 20:40:57.000000', 'pres stylo 4 c tricone montex boite de 120promo', NULL, b'1', 5, '285128', '50.06', 50, '2025-09-16 20:40:57.000000', '285128'),
(965446, 'Stylos', NULL, '2025-09-16 20:40:57.000000', 'pres stylo fifty tricone montex boite de 120promo', NULL, b'1', 5, '285135', '54.43', 50, '2025-09-16 20:40:57.000000', '285135'),
(965447, 'Stylos', NULL, '2025-09-16 20:40:57.000000', 'stylo tic tac 4 couleur neon hauser', NULL, b'1', 5, '516992', '3.63', 50, '2025-09-16 20:40:57.000000', '516992'),
(965448, 'Stylos', NULL, '2025-09-16 20:40:57.000000', 'stylo dream trenz de 10 hauser', NULL, b'1', 5, '521163', '6.55', 50, '2025-09-16 20:40:57.000000', '521163'),
(965449, 'Stylos', NULL, '2025-09-16 20:40:57.000000', 'poch de 4 stylo dream B/P hauser', NULL, b'1', 5, '528407', '5.86', 50, '2025-09-16 20:40:57.000000', '528407'),
(965450, 'Taille Crayons', NULL, '2025-09-16 20:40:57.000000', 'Taille crayon plastique simple', NULL, b'1', 5, '641T', '0.14', 50, '2025-09-16 20:40:57.000000', '641T'),
(965451, 'Taille Crayons', NULL, '2025-09-16 20:40:57.000000', 'taille crayon metal 1T B24', NULL, b'1', 5, '071018', '0.46', 50, '2025-09-16 20:40:57.000000', '071018'),
(965452, 'Taille Crayons', NULL, '2025-09-16 20:40:57.000000', 'taille crayon metal 2T B12', NULL, b'1', 5, '071063', '0.82', 50, '2025-09-16 20:40:57.000000', '071063'),
(965453, 'Taille Crayons', NULL, '2025-09-16 20:40:57.000000', 'taille crayon double ZHI B24', NULL, b'1', 5, '567253', '0.75', 50, '2025-09-16 20:40:57.000000', '567253'),
(965454, 'Taille Crayons', NULL, '2025-09-16 20:40:57.000000', 'taille crayon robust ref 7460 jarre de 25p DOMS', NULL, b'1', 5, '774607', '0.36', 50, '2025-09-16 20:40:57.000000', '774607'),
(965455, 'Taille Crayons', NULL, '2025-09-16 20:40:57.000000', 'taille crayon ref 8174 jarre de 20p DOMS', NULL, b'1', 5, '781742', '0.36', 50, '2025-09-16 20:40:57.000000', '781742'),
(965456, 'Taille Crayons', NULL, '2025-09-16 20:40:57.000000', 'taille crayon neon-tri ref 7192 jarre de 30p DOMS', NULL, b'1', 5, '771927', '0.39', 50, '2025-09-16 20:40:57.000000', '771927'),
(965457, 'Taille Crayons', NULL, '2025-09-16 20:40:57.000000', 'taille crayon hauser 32p', NULL, b'1', 5, '965549', '0.47', 50, '2025-09-16 20:40:57.000000', '965549'),
(965458, 'Taille Crayons', NULL, '2025-09-16 20:40:57.000000', 'taille crayon +gomme 24p', NULL, b'1', 5, '728528', '0.65', 50, '2025-09-16 20:40:57.000000', '728528'),
(965459, 'Taille Crayons', NULL, '2025-09-16 20:40:57.000000', 'taille crayon montre hello kitty', NULL, b'1', 5, '673', '0.60', 50, '2025-09-16 20:40:57.000000', '673'),
(965460, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'rapporteur OFFICE', NULL, b'1', 5, '114174', '0.45', 50, '2025-09-16 20:42:41.000000', '114174'),
(965461, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'rapporteur coq', NULL, b'1', 5, '301000', '0.42', 50, '2025-09-16 20:42:41.000000', '301000'),
(965462, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'equerre 16/60 OFFICE', NULL, b'1', 5, '114235', '0.50', 50, '2025-09-16 20:42:41.000000', '114235'),
(965463, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'equerre 15/60 coq', NULL, b'1', 5, 'EQ-15/60', '0.50', 50, '2025-09-16 20:42:41.000000', 'EQ-15/60'),
(965464, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'equerre 20/60 OFFICE', NULL, b'1', 5, '129574', '0.43', 50, '2025-09-16 20:42:41.000000', '129574'),
(965465, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'equerre 20/60 coq', NULL, b'1', 5, 'EQ20/60', '0.61', 50, '2025-09-16 20:42:41.000000', 'EQ20/60'),
(965466, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'double decimetre office', NULL, b'1', 5, '129499', '0.53', 50, '2025-09-16 20:42:41.000000', '129499'),
(965467, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'regle de 20cm office', NULL, b'1', 5, '129529', '1.73', 50, '2025-09-16 20:42:41.000000', '129529'),
(965468, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'regle de 30cm office', NULL, b'1', 5, '000237', '3.85', 50, '2025-09-16 20:42:41.000000', '000237'),
(965469, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'double decimetre coq', NULL, b'1', 5, 'DD COQ', '0.46', 50, '2025-09-16 20:42:41.000000', 'DD COQ'),
(965470, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'double decimetre maped', NULL, b'1', 5, 'DD MAPED', '0.94', 50, '2025-09-16 20:42:41.000000', 'DD MAPED'),
(965471, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'regle de 15cm maped', NULL, b'1', 5, 'R-15 MAPED', '0.60', 50, '2025-09-16 20:42:41.000000', 'R-15 MAPED'),
(965472, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'regle de 20cm coq', NULL, b'1', 5, 'R-20 COQ', '0.43', 50, '2025-09-16 20:42:41.000000', 'R-20 COQ'),
(965473, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'regle de 20cm coq', NULL, b'1', 5, '129512', '0.44', 50, '2025-09-16 20:42:41.000000', '129512'),
(965474, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'regle de 30cm coq', NULL, b'1', 5, 'R-30 COQ', '0.77', 50, '2025-09-16 20:42:41.000000', 'R-30 COQ'),
(965475, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'regle de 40cm coq', NULL, b'1', 5, 'R-40 COQ', '1.06', 50, '2025-09-16 20:42:41.000000', 'R-40 COQ'),
(965476, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'pochette géométrique PM office', NULL, b'1', 5, '129628', '2.28', 50, '2025-09-16 20:42:41.000000', '129628'),
(965477, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'pochette géométrique GM office', NULL, b'1', 5, '129611', '2.28', 50, '2025-09-16 20:42:41.000000', '129611'),
(965478, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'pochette geometrique PM coq 20cm', NULL, b'1', 10, '000220', '1.82', 51, '2025-09-16 22:20:09.654000', '000220'),
(965479, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'pochette géométrique 20 cm MAPED', NULL, b'1', 5, '428202', '3.44', 50, '2025-09-16 20:42:41.000000', '428202'),
(965480, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'pochette géométrique 30 cm MAPED', NULL, b'1', 5, '428301', '3.28', 50, '2025-09-16 20:42:41.000000', '428301'),
(965481, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'regle twist flex 30 cm MAPED', NULL, b'1', 5, '279004', '7.28', 50, '2025-09-16 20:42:41.000000', '279004'),
(965482, 'Tracage', NULL, '2025-09-16 20:42:41.000000', 'kit tracage twist MAPED 30CM', NULL, b'1', 5, '950554', '6.92', 50, '2025-09-16 20:42:41.000000', '950554'),
(965483, 'Trousses+Sac a Dos', NULL, '2025-09-16 20:42:41.000000', 'trousse scolaire 3D ref-136-6PROMO', NULL, b'1', 5, 'sat136-6', '3.21', 50, '2025-09-16 20:42:41.000000', 'sat136-6'),
(965484, 'Trousses+Sac a Dos', NULL, '2025-09-16 20:42:41.000000', 'trousse scolaire 3D GM PROMO', NULL, b'1', 5, '1018', '4.90', 50, '2025-09-16 20:42:41.000000', '1018'),
(965485, 'Trousses+Sac a Dos', NULL, '2025-09-16 20:42:41.000000', 'trousse BRONS ref BR-3032 présentoire de 12p', NULL, b'1', 5, '003342', '6.04', 50, '2025-09-16 20:42:41.000000', '003342'),
(965486, 'Trousses+Sac a Dos', NULL, '2025-09-16 20:42:41.000000', 'trousse triangulaire BRONS ref BR-3022PROMO', NULL, b'1', 5, '006275', '6.04', 50, '2025-09-16 20:42:41.000000', '006275'),
(965487, 'Trousses+Sac a Dos', NULL, '2025-09-16 20:42:41.000000', 'trousse rond capitonné BRONS ref BR-3032', NULL, b'1', 5, '006312', '10.92', 50, '2025-09-16 20:42:41.000000', '006312'),
(965488, 'Trousses+Sac a Dos', NULL, '2025-09-16 20:42:41.000000', 'trousse double capitonné BRONS', NULL, b'1', 5, '012900', '13.28', 50, '2025-09-16 20:42:41.000000', '012900'),
(965489, 'Trousses+Sac a Dos', NULL, '2025-09-16 20:42:41.000000', 'trousse BRONS ref BR-3055', NULL, b'1', 5, '010647', '10.92', 50, '2025-09-16 20:42:41.000000', '010647'),
(965490, 'Trousses+Sac a Dos', NULL, '2025-09-16 20:42:41.000000', 'sac a dos+trousse+porte gouté', NULL, b'1', 5, '901', '63.58', 50, '2025-09-16 20:42:41.000000', '901'),
(965491, 'Trousses+Sac a Dos', NULL, '2025-09-16 20:42:41.000000', 'sac a dos+trousse+porte gouté', NULL, b'1', 5, '902', '63.58', 50, '2025-09-16 20:42:41.000000', '902'),
(965492, 'Trousses+Sac a Dos', NULL, '2025-09-16 20:42:41.000000', 'sac a dos+trousse', NULL, b'1', 5, '1903', '44.90', 50, '2025-09-16 20:42:41.000000', '1903'),
(965493, 'Trousses+Sac a Dos', NULL, '2025-09-16 20:42:41.000000', 'sac a dos+trousse', NULL, b'1', 5, '904', '44.90', 50, '2025-09-16 20:42:41.000000', '904'),
(965494, 'Trousses+Sac a Dos', NULL, '2025-09-16 20:42:41.000000', 'sac a dos ref-', NULL, b'1', 5, '905', '42.50', 50, '2025-09-16 20:42:41.000000', '905');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=13 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `email`, `name`, `password`, `role`) VALUES
(2, 'zzz@epsrit.tn', 'zzz kk', '$2a$10$7lOF85VMq3ZLRg/n3mqlcu5HTBFw5utDeRPfzLXwDcuVM3dOBwVGa', 'CUSTOMER'),
(3, 'test@example.com', 'Test User', '$2a$10$mBHfjfVfMfPQUY8wTFYOW.4Ciy2gJqNi8f1tWNHXEL0d0mwsYcg/u', 'CUSTOMER'),
(4, 'mysqltest@example.com', 'MySQL Test User', '$2a$10$dh.H9a8nAH6KeD3TKqcSr.9zqcq7OnKLklmHMLmaDBJmR.0w8RZIa', 'CUSTOMER'),
(5, 'carttest@example.com', 'Cart Test User', '$2a$10$BwmJkcKjbkwxuuuf9VXZUOHNmzTQGYPBVbueHeTv57gJougGKVxkK', 'CUSTOMER'),
(6, 'john.doe@example.com', 'John Doe', '$2a$10$3sBNnm.fWeOnY846n17Bq.3jA6UuFdYLmqx3UiWI8jEDjzdzrqywC', 'CUSTOMER'),
(10, 'fat@desk.tn', 'aziz', '181JMT1163', 'ADMIN'),
(11, 'admin@schoolfurniture.com', 'Admin User', '$2a$10$THej.oArnpe2vBhC1E.tBOsBJxVYP52rqXF2hj/xzImtEDNrQjR7u', 'ADMIN'),
(12, 'testuser@example.com', 'Test User', '$2a$10$u0LRWPCuTAVBidZAC4ros.fYw1rWvHjy6EhFW.YZqP/FpY2bVqnAS', 'CUSTOMER');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `carts`
--
ALTER TABLE `carts`
  ADD CONSTRAINT `FKb5o626f86h46m4s7ms6ginnop` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `cart_items`
--
ALTER TABLE `cart_items`
  ADD CONSTRAINT `FK1re40cjegsfvw58xrkdp6bac6` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`),
  ADD CONSTRAINT `FKpcttvuq4mxppo8sxggjtn5i2c` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`cart_id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  ADD CONSTRAINT `FKocimc7dtr037rh4ls4l95nlfi` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
