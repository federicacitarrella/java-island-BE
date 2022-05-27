DROP DATABASE IF EXISTS `transactions`;

CREATE DATABASE  IF NOT EXISTS `transactions`;
USE `transactions`;

DROP TABLE IF EXISTS `transactions`;
CREATE TABLE `transactions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` tinyint NOT NULL,
  `account_number_from` varchar(45) NOT NULL,
  `account_number_to` varchar(45) DEFAULT NULL,
  `date` datetime NOT NULL,
  `cause` varchar(100) DEFAULT NULL,
  `amount` double NOT NULL,
  PRIMARY KEY (`id`)
--   CONSTRAINT `account_owner_id_ibfk_1` FOREIGN KEY (`account_owner_id`) REFERENCES `account_owners`.`account_owners` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;