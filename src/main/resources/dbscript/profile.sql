CREATE TABLE `profile` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(2000) DEFAULT NULL,
  `refer` varchar(2000) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `level` int(10) DEFAULT NULL,
  PRIMARY KEY (`Id`)
)