--记住我使用的表
CREATE TABLE IF NOT EXISTS `persistent_logins`(
	`username` varchar(64) NOT NULL, 
	`series`   varchar(64) NOT NULL, 
	`token`    varchar(64) NOT NULL, 
	`last_used` timestamp  NOT NULL 
	 DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	 PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;