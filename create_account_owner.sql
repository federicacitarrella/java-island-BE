DROP DATABASE IF EXISTS `account_owners`;

CREATE DATABASE  IF NOT EXISTS `account_owners`;
USE `account_owners`;

DROP TABLE IF EXISTS `account_owners`;
CREATE TABLE `account_owners` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `birth_date` date NOT NULL,
  `email` varchar(45) NOT NULL UNIQUE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO account_owners(first_name,last_name,birth_date,email) VALUES('Mario','Rossi','1957-03-20','mario.rossi@gmail.com');
INSERT INTO account_owners(first_name,last_name,birth_date,email) VALUES('Carlo','Verdi','1972-11-13','carlo.verdi@gmail.com');

SET FOREIGN_KEY_CHECKS = 1;
