-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 09, 2025 at 09:09 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `nvidia`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `role` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id`, `name`, `password`, `email`, `role`) VALUES
(1, 'ad', '12', 'admin123@gmail.com', 'Software Dev'),
(2, 'admin', '123', 'admin@123', 'System Admin');

-- --------------------------------------------------------

--
-- Table structure for table `bill`
--

CREATE TABLE `bill` (
  `accountNumber` int(11) NOT NULL,
  `billAmount` decimal(10,2) DEFAULT NULL,
  `transactionID` varchar(50) DEFAULT NULL,
  `paymentType` varchar(20) DEFAULT NULL,
  `dueFine` decimal(10,2) DEFAULT NULL,
  `tax` decimal(10,2) DEFAULT NULL,
  `date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bill`
--

INSERT INTO `bill` (`accountNumber`, `billAmount`, `transactionID`, `paymentType`, `dueFine`, `tax`, `date`) VALUES
(513742766, 0.00, '23962376', 'Online', 0.00, 1000.00, '2025-03-04'),
(824052469, 49.99, NULL, '', 0.00, 0.00, '2025-03-04');

-- --------------------------------------------------------

--
-- Table structure for table `broaddband_plans`
--

CREATE TABLE `broaddband_plans` (
  `accountNo` int(11) NOT NULL,
  `mobilenumber` varchar(15) DEFAULT NULL,
  `planType` varchar(50) DEFAULT NULL,
  `planPrice` decimal(10,2) DEFAULT NULL,
  `planData` varchar(20) DEFAULT NULL,
  `speed` varchar(20) DEFAULT NULL,
  `planDuration` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `broaddband_plans`
--

INSERT INTO `broaddband_plans` (`accountNo`, `mobilenumber`, `planType`, `planPrice`, `planData`, `speed`, `planDuration`) VALUES
(513742766, '7057509494', 'Gamer Pro', 149.99, 'Unlimited', '2 Gbps', '30 Days'),
(824052469, '7057544545', 'Basic', 49.99, '500GB', '100 Mbps', '30 Days');

-- --------------------------------------------------------

--
-- Table structure for table `sign_in`
--

CREATE TABLE `sign_in` (
  `name` varchar(50) DEFAULT NULL,
  `mobile` varchar(255) NOT NULL,
  `city` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sign_in`
--

INSERT INTO `sign_in` (`name`, `mobile`, `city`) VALUES
('Karan', '7057544545', 'Texas'),
('Kunal', '7057509494', 'Texas');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--
-- Error reading structure for table nvidia.users: #1932 - Table &#039;nvidia.users&#039; doesn&#039;t exist in engine
-- Error reading data for table nvidia.users: #1064 - You have an error in your SQL syntax; check the manual that corresponds to your MariaDB server version for the right syntax to use near &#039;FROM `nvidia`.`users`&#039; at line 1

-- --------------------------------------------------------

--
-- Table structure for table `vice_request`
--

CREATE TABLE `vice_request` (
  `request_id` int(11) NOT NULL,
  `user` varchar(255) NOT NULL,
  `type` varchar(100) NOT NULL,
  `status` varchar(50) DEFAULT 'Pending',
  `request_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `vice_request`
--

INSERT INTO `vice_request` (`request_id`, `user`, `type`, `status`, `request_date`) VALUES
(6632, 'Kunal', 'Speed Problem', 'In Progress', '2025-02-27');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `bill`
--
ALTER TABLE `bill`
  ADD PRIMARY KEY (`accountNumber`);

--
-- Indexes for table `broaddband_plans`
--
ALTER TABLE `broaddband_plans`
  ADD PRIMARY KEY (`accountNo`),
  ADD KEY `mobilenumber` (`mobilenumber`);

--
-- Indexes for table `sign_in`
--
ALTER TABLE `sign_in`
  ADD PRIMARY KEY (`mobile`);

--
-- Indexes for table `vice_request`
--
ALTER TABLE `vice_request`
  ADD PRIMARY KEY (`request_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bill`
--
ALTER TABLE `bill`
  ADD CONSTRAINT `fk_accountNumber` FOREIGN KEY (`accountNumber`) REFERENCES `broaddband_plans` (`accountNo`);

--
-- Constraints for table `broaddband_plans`
--
ALTER TABLE `broaddband_plans`
  ADD CONSTRAINT `broaddband_plans_ibfk_1` FOREIGN KEY (`mobilenumber`) REFERENCES `sign_in` (`mobile`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
