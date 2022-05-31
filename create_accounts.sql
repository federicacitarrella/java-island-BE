DROP DATABASE IF EXISTS `accounts`;

CREATE DATABASE  IF NOT EXISTS `accounts`;
USE `accounts`;

DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `account_number` varchar(45) UNIQUE NOT NULL,
  `balance` double NOT NULL,
  `status` tinyint NOT NULL,
  `account_owner_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
--   CONSTRAINT `account_owner_id_ibfk_1` FOREIGN KEY (`account_owner_id`) REFERENCES `account_owners`.`account_owners` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;