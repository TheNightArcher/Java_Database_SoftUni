CREATE DATABASE `instd`;

CREATE TABLE `users`(
`id` INT PRIMARY KEY,
`username` VARCHAR(30) NOT NULL UNIQUE,
`password` VARCHAR(30) NOT NULL,
`email` VARCHAR(50) NOT NULL,
`gender` CHAR(1) NOT NULL,
`age` INT NOT NULL,
`job_title` VARCHAR(40) NOT NULL,
`ip` VARCHAR(30) NOT NULL
);

CREATE TABLE `addresses`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`address`VARCHAR(30) NOT NULL,
`town` VARCHAR(30) NOT NULL,
`country` VARCHAR(30) NOT NULL,
`user_id` INT NOT NULL,
CONSTRAINT fk_address_user
FOREIGN KEY (`user_id`)
REFERENCES users(`id`)
);

CREATE TABLE `photos`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`description`TEXT NOT NULL,
`date` DATETIME NOT NULL,
`views` INT NOT NULL DEFAULT(0)
);

CREATE TABLE `comments`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`comment` VARCHAR(255) NOT NULL,
`date` DATETIME NOT NULL,
`photo_id` INT NOT NULL ,
CONSTRAINT fk_comment_photo
FOREIGN KEY (`photo_id`)
REFERENCES photos(`id`)
);

CREATE TABLE `users_photos`(
`user_id` INT NOT NULL,
`photo_id` INT NOT NULL,
CONSTRAINT fk_users_photos_user
FOREIGN KEY (`user_id`)
REFERENCES users(`id`),

CONSTRAINT fk_users_photos_photos
FOREIGN KEY (`photo_id`)
REFERENCES photos(`id`)
);

CREATE TABLE `likes`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`photo_id` INT,
`user_id` INT,

CONSTRAINT fk_likes_user
FOREIGN KEY (`user_id`)
REFERENCES users(`id`),

CONSTRAINT fk_likes_photo
FOREIGN KEY (`photo_id`)
REFERENCES photos(`id`)
);

--------------

#2
INSERT INTO `addresses` (`address`,`town`,`country`,`user_id`)
(SELECT `username`,`password`,`ip`,`age` FROM `users`
WHERE `gender` = 'm');

-------------------

#3
UPDATE `addresses`
SET `country` = 'Blocked'
WHERE `country` LIKE 'B%';

UPDATE `addresses`
SET `country` = 'Test'
WHERE `country` LIKE 'T%';

UPDATE `addresses`
SET `country` = 'In Progress'
WHERE `country` LIKE 'P%';

--------------------------------

#4
DELETE FROM `addresses`
WHERE `id` % 3 = 0;

---------------------

#5
SELECT `username`,`gender`,`age` FROM `users`
ORDER BY `age` DESC, `username`;

---------------------

#6
SELECT p.`id`,p.`date`,p.`description`, COUNT(p.`id` = c.`photo_id`) AS 'commentCount' FROM `photos` AS p
JOIN `comments` AS c
ON p.`id` = c.`photo_id`
GROUP BY p.`id`
ORDER BY `commentCount` DESC, p.`id` LIMIT 1;

-----------------------

#7
SELECT CONCAT(u.`id`,' ',u.`username`) AS 'id_username',u.`email` FROM `users` AS u
JOIN `users_photos` AS up
ON u.`id` = up.`user_id`
JOIN `photos` AS p
ON up.`photo_id` = p.`id`
WHERE u.`id` = p.`id`
ORDER BY u.`id`;

------------------------------

#8
SELECT p.`id`,COUNT(DISTINCT l.`id`) AS like_counts,COUNT( DISTINCT c.`id`) AS comment_count FROM `photos` AS p
LEFT JOIN `likes` AS l
ON p.`id` = l.`photo_id`
LEFT JOIN `comments` AS c
ON p.`id` = c.`photo_id`
GROUP BY p.`id`
ORDER BY like_counts DESC,comment_count DESC, p.`id`;

--------------------------------------

#9
SELECT CONCAT(SUBSTRING(`description`,1,30),'..') AS summary,`date` FROM `photos`
WHERE (DAY(`date`) = 10)
ORDER BY `date` DESC;

-------------------------------

#10
DELIMITER !!

CREATE FUNCTION udf_users_photos_count(username VARCHAR(30))

RETURNS INT
DETERMINISTIC

BEGIN

RETURN (SELECT COUNT(p.`id`) FROM `photos` AS p
JOIN `users_photos` AS up
ON p.`id` = up.`photo_id`
JOIN `users` AS u
ON u.`id` = up.`user_id`
WHERE u.`username` = username);

END!!

DELIMITER ;

SELECT udf_users_photos_count('ssantryd') AS photosCount;

--------------------

#11

DELIMITER !!

CREATE PROCEDURE udp_modify_user (address VARCHAR(30), town VARCHAR(30)) 

BEGIN

UPDATE `users` AS u
JOIN `addresses` AS a
ON a.`user_id` = u.`id`
SET u.`age` = u.`age` + 10
WHERE 
a.`address` = address AND a.`town` = town;

END!!

DELIMITER ;

CALL udp_modify_user ('97 Valley Edge Parkway', 'Divinópolis');
SELECT u.username, u.email,u.gender,u.age,u.job_title FROM users AS u
WHERE u.username = 'eblagden21';