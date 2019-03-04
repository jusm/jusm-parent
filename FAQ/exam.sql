/*
SQLyog Ultimate v9.20 
MySQL - 5.5.21 : Database - jusm
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`jusm` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `jusm`;

/*Table structure for table `student` */

DROP TABLE IF EXISTS `student`;

CREATE TABLE `student` (
  `id` varchar(8) NOT NULL,
  `name` varchar(22) DEFAULT NULL,
  `class_id` varchar(22) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `student` */

insert  into `student`(`id`,`name`,`class_id`) values ('1','张三','1'),('10','王城','3'),('11','陈冲','3'),('12','陈述','3'),('2','李四','1'),('3','王伟','1'),('4','朱静','2'),('5','李自成','2'),('6','西路','2'),('7','王其','2'),('8','李志超','3'),('9','朱炯','3');

/*Table structure for table `student_score` */

DROP TABLE IF EXISTS `student_score`;

CREATE TABLE `student_score` (
  `id` varchar(22) DEFAULT NULL,
  `exam_id` varchar(22) DEFAULT NULL,
  `student_id` varchar(22) DEFAULT NULL,
  `score` int(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `student_score` */

insert  into `student_score`(`id`,`exam_id`,`student_id`,`score`) values ('1','1','1',75),('2','1','2',55),('3','1','3',99),('4','1','4',65),('5','1','5',75),('6','1','6',96),('7','1','7',55),('8','1','8',78),('9','1','9',85),('10','1','10',78),('11','1','11',89),('12','1','12',88),('13','2','1',99),(NULL,NULL,NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;




SELECT 
  a.* 
FROM
  (SELECT 
    student.`id`,
    student.`class_id`,
    student.`name`,
    student_score.`exam_id`,
    student_score.`score` 
  FROM
    student 
    LEFT JOIN student_score 
      ON student_score.`student_id` = student.`id`) AS a 
WHERE 
  (SELECT 
    COUNT(*) 
  FROM
    (
      (SELECT 
        student.`id`,
        student.`class_id`,
        student.`name`,
        student_score.`exam_id`,
        student_score.`score` 
      FROM
        student 
        LEFT JOIN student_score 
          ON student_score.`student_id` = student.`id`)
    ) AS aa 
  WHERE aa.class_id = a.class_id AND aa.exam_id=1 AND aa.score > a.score) < 3 ORDER BY a.class_id,a.score DESC