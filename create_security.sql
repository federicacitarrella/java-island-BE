DROP DATABASE IF EXISTS `spring_security_final_challenge`;

CREATE DATABASE  IF NOT EXISTS `spring_security_final_challenge`;
USE `spring_security_final_challenge`;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` char(80) NOT NULL,
  `account_owner_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO users(username,password,account_owner_id) VALUES('dip@gmail.com','1f2f8338fcde644238e7c2d3be5ea75d8281df0c301c1db5b23bbf663ddd20c1f67caab584d2c5b4',-1);
INSERT INTO users(username,password,account_owner_id) VALUES('mario.rossi@gmail.com','1f2f8338fcde644238e7c2d3be5ea75d8281df0c301c1db5b23bbf663ddd20c1f67caab584d2c5b4',1);
INSERT INTO users(username,password,account_owner_id) VALUES('carlo.verdi@gmail.com','1f2f8338fcde644238e7c2d3be5ea75d8281df0c301c1db5b23bbf663ddd20c1f67caab584d2c5b4',2);

DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO roles(name) VALUES('C');
INSERT INTO roles(name) VALUES('D');

DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (user_id, role_id),
  KEY `FK_user_idx` (`user_id`),
  CONSTRAINT `FK_USER` FOREIGN KEY (`user_id`) 
  REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  KEY `FK_role_idx` (`role_id`),
  CONSTRAINT `FK_role` FOREIGN KEY (`role_id`) 
  REFERENCES `roles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO user_roles(user_id,role_id) VALUES(1,2);
INSERT INTO user_roles(user_id,role_id) VALUES(2,1);
INSERT INTO user_roles(user_id,role_id) VALUES(3,1);

SET FOREIGN_KEY_CHECKS = 1;
