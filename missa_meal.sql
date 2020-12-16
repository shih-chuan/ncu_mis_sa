-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- 主機： 127.0.0.1
-- 產生時間： 2020-12-16 09:29:45
-- 伺服器版本： 10.4.14-MariaDB
-- PHP 版本： 7.4.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `missa`
--

-- --------------------------------------------------------

--
-- 資料表結構 `meal`
--

CREATE TABLE `meal` (
  `meal_id` int(11) NOT NULL,
  `meal_name` varchar(25) NOT NULL,
  `meal_price` double NOT NULL,
  `meal_image` varchar(100) NOT NULL,
  `meal_content` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 傾印資料表的資料 `meal`
--

INSERT INTO `meal` (`meal_id`, `meal_name`, `meal_price`, `meal_image`, `meal_content`) VALUES
(1, 'b_cola', 60, 'b_cola.jpg', '這是大杯可樂'),
(2, 'm_cola', 45, 'm_cola.jpg', '這是中杯可樂'),
(3, 's_cola', 37, 's_cola.jpg', '這是小杯可樂'),
(5, 'cho_Gina_Rod', 50, 'cho_Gina_Rod.jpg', '這是巧克力吉拿棒'),
(8, 'Gina_Rod', 45, 'Gina_Rod.jpg', '這是吉拿棒');

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `meal`
--
ALTER TABLE `meal`
  ADD PRIMARY KEY (`meal_id`);

--
-- 在傾印的資料表使用自動遞增(AUTO_INCREMENT)
--

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `meal`
--
ALTER TABLE `meal`
  MODIFY `meal_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
