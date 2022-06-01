DROP DATABASE IF EXISTS `transactions`;

CREATE DATABASE  IF NOT EXISTS `transactions`;
USE `transactions`;

DROP TABLE IF EXISTS `transactions`;
CREATE TABLE `transactions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` tinyint NOT NULL,
  `account_number_from` varchar(45) NOT NULL,
  `account_number_to` varchar(45) DEFAULT NULL,
  `account_owner_id` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `cause` varchar(100) DEFAULT NULL,
  `amount` double NOT NULL,
  PRIMARY KEY (`id`)
--   CONSTRAINT `account_owner_id_ibfk_1` FOREIGN KEY (`account_owner_id`) REFERENCES `account_owners`.`account_owners` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO transactions(type,account_number_from,account_number_to,account_owner_id,date,cause,amount) VALUES(2,'IT881605436',null,1,'2022-05-27 11:15:01',null,1000);
INSERT INTO transactions(type,account_number_from,account_number_to,account_owner_id,date,cause,amount) VALUES(2,'IT881605436',null,1,'2022-05-31 17:43:24',null,3500);
INSERT INTO transactions(type,account_number_from,account_number_to,account_owner_id,date,cause,amount) VALUES(3,'IT881605436',null,1,'2022-05-31 17:43:24',null,-1000);
INSERT INTO transactions(type,account_number_from,account_number_to,account_owner_id,date,cause,amount) VALUES(1,'IT881605436','IT571639434',1,'2022-05-31 19:40:20','Regalo',1000);
INSERT INTO transactions(type,account_number_from,account_number_to,account_owner_id,date,cause,amount) VALUES(2,'IT571639434',null,2,'2022-04-21 19:40:20',null,3500.5);
INSERT INTO transactions(type,account_number_from,account_number_to,account_owner_id,date,cause,amount) VALUES(2,'IT473239644',null,2,'2022-05-25 13:45:20',null,3000);
INSERT INTO transactions(type,account_number_from,account_number_to,account_owner_id,date,cause,amount) VALUES(2,'IT473239644',null,2,'2022-05-28 09:32:05',null,549);

SET FOREIGN_KEY_CHECKS = 1;