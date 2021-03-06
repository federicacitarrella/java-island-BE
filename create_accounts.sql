DROP DATABASE IF EXISTS `accounts`;

CREATE DATABASE  IF NOT EXISTS `accounts`;
USE `accounts`;

DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `account_number` varchar(45) UNIQUE NOT NULL,
  `balance` decimal(10,2) NOT NULL,
  `status` tinyint NOT NULL,
  `account_owner_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO accounts(first_name,last_name,account_number,balance,status,account_owner_id) VALUES('Mario','Rossi','IT881605436',2500.00,0,1);
INSERT INTO accounts(first_name,last_name,account_number,balance,status,account_owner_id) VALUES('Carlo','Verdi','IT571639434',3500.50,0,2);
INSERT INTO accounts(first_name,last_name,account_number,balance,status,account_owner_id) VALUES('Carlo','Verdi','IT473239644',3549.00,0,2);

SET FOREIGN_KEY_CHECKS = 1;