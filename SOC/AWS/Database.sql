USE pdb;
DROP table IF EXISTS photos;
DROP table IF EXISTS users;
CREATE TABLE `users` (
  `uid` mediumint(10) NOT NULL AUTO_INCREMENT,
  `email` varchar(30) DEFAULT NULL,
  `pass` varchar(32) DEFAULT NULL,
  `uname` varchar(20) NOT NuLL,
  `verified` boolean DEFAULT FALSE,
  PRIMARY KEY (`uid`)
);

CREATE TABLE `photos` (
  `pid` mediumint(9) NOT NULL AUTO_INCREMENT,
  `url` varchar(100) DEFAULT NULL,
  `uid` mediumint(10) NOT NULL,
  `name` varchar(30),
  `description` varchar(140),
  `categories` varchar(255),
  `public` boolean DEFAULT FALSE,
  `date` DATETIME,
  PRIMARY KEY (`pid`),
   KEY (`uid`),
  CONSTRAINT FOREIGN KEY (`uid`) REFERENCES `users` (`uid`)
);

INSERT INTO users VALUES (1, 'sa17419@essex.ac.uk', md5('Books'), 'Sam', true);
INSERT INTO users VALUES (NULL, 'sam200117@outlook.com', md5('Games'), 'Ben', true);
INSERT INTO users VALUES (NULL, 'samabley@hotmail.com', md5('Videos'), 'Jess', false);


INSERT INTO photos VALUES (1,'http://d3ci5xc1bwzcds.cloudfront.net/1/photo1',1,'First day over','','sun people beach',1,'2016-11-16 12:08:43');
INSERT INTO photos VALUES (2,'http://d3ci5xc1bwzcds.cloudfront.net/1/photo2',1,'Break?','We never get a break, we are even working during lunch','people computer drink happy',0,'2017-04-21 13:09:40');
INSERT INTO photos VALUES (3,'http://d3ci5xc1bwzcds.cloudfront.net/2/photo3',2, 'New York','','car building light train',1,'2017-10-20 21:23:22');
INSERT INTO photos VALUES (4,'http://d3ci5xc1bwzcds.cloudfront.net/1/photo4',1,'','Can you guess where we went?','',1,'2018-03-10 14:43:01');
INSERT INTO photos VALUES (5,'http://d3ci5xc1bwzcds.cloudfront.net/2/photo5',2,'Kitty','This is the happiest ive ever seen him','cat',1,'2018-12-12 08:55:11');
INSERT INTO photos VALUES (6,'http://d3ci5xc1bwzcds.cloudfront.net/2/photo6',2,'One wet dog','John came home very wet','dog',1,'2019-06-01 11:01:24');
INSERT INTO photos VALUES (7,'http://d3ci5xc1bwzcds.cloudfront.net/2/photo7',2,'Joy','We were up early for this one','',0,'2019-07-06 08:10:37');
INSERT INTO photos VALUES (8,'http://d3ci5xc1bwzcds.cloudfront.net/1/photo8',1,'Jeffrey','Green Eyed monster','cat green eye ears',1,'2019-08-04 20:11:55');
INSERT INTO photos VALUES (9,'http://d3ci5xc1bwzcds.cloudfront.net/1/photo9',1,'A dream','Youd think this was photoshopped','dog lake mountains trees',1,'2019/11/30 16:27');
INSERT INTO photos VALUES (10,'http://d3ci5xc1bwzcds.cloudfront.net/1/photo10',1,'Last night','From sun up, to sun down','people dance sunset evening',1,'2019/11/30 16:34');
INSERT INTO photos VALUES (12,'http://d3ci5xc1bwzcds.cloudfront.net/1/photo12',1, 'Dog','A good boy','dog golden happy',1,'2019/11/30 18:14');
INSERT INTO photos VALUES (13,'http://d3ci5xc1bwzcds.cloudfront.net/1/photo13',1,'Mountains','','vally mountains fog',1,'2019/12/02 14:24');
INSERT INTO photos VALUES (15,'http://d3ci5xc1bwzcds.cloudfront.net/2/photo15',2,'Canada','','night trees waves',1,'2019/12/09 22:07');
INSERT INTO photos VALUES (16,'http://d3ci5xc1bwzcds.cloudfront.net/2/photo16',2,'New York','','cars buildings lights',1,'2019/12/09 22:16');
INSERT INTO photos VALUES (17,'http://d3ci5xc1bwzcds.cloudfront.net/2/photo17',2,'','arent we happy here?','',0,'2019/12/10 13:15');