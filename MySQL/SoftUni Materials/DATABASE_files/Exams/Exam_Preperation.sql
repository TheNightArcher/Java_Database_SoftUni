CREATE DATABASE stc;


CREATE TABLE `addresses`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(100) NOT NULL
);

CREATE TABLE `categories`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(10) NOT NULL
);


CREATE TABLE `clients`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`full_name` VARCHAR(50) NOT NULL,
`phone_number` VARCHAR(20) NOT NULL
);

CREATE TABLE `drivers`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`first_name` VARCHAR(30) NOT NULL,
`last_name` VARCHAR(30) NOT NULL,
`age` INT NOT NULL,
`rating` FLOAT DEFAULT(5.5)
);

CREATE TABLE `cars`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`make` VARCHAR(20) NOT NULL,
`model` VARCHAR(20),
`year` INT DEFAULT(0) NOT NULL,
`mileage` INT DEFAULT(0) NOT NULL,
`condition` CHAR(1) NOT NULL,
`category_id` INT NOT NULL,
CONSTRAINT fk_car_category
FOREIGN KEY (`category_id`)
REFERENCES categories(`id`)
);

CREATE TABLE `courses`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`from_address_id` INT NOT NULL,
`start` DATETIME NOT NULL,
`bill` DECIMAL(10,2) DEFAULT(10),
`car_id` INT NOT NULL,
`client_id` INT NOT NULL,
CONSTRAINT fk_course_address
FOREIGN KEY (`from_address_id`)
REFERENCES addresses(`id`),

CONSTRAINT fk_courses_car
FOREIGN KEY (`car_id`)
REFERENCES cars(`id`),

CONSTRAINT fk_courses_client
FOREIGN KEY (`client_id`)
REFERENCES clients(`id`)
);

CREATE TABLE `cars_drivers`(
`car_id` INT NOT NULL,
`driver_id` INT NOT NULL,
CONSTRAINT fk_cars_drivers_car
FOREIGN KEY (`car_id`)
REFERENCES cars(`id`),

CONSTRAINT fk_cars_drivers_driver
FOREIGN KEY (`driver_id`)
REFERENCES drivers(`id`),

CONSTRAINT
PRIMARY KEY(`car_id`,`driver_id`)
);

------------
#2
INSERT INTO `clients` (`full_name`,`phone_number`)
(SELECT CONCAT(`first_name`,' ',`last_name`) AS 'full_name',CONCAT('(088) 9999',`id`* 2) FROM `drivers`
WHERE `id` BETWEEN 10 AND 20);

----------
#3
UPDATE  `cars`
SET `condition` = 'C'
WHERE `mileage` > 800000 OR `mileage` IS NULL AND `year` <= 2010;


#4
DELETE FROM `clients` AS c
WHERE `id` NOT IN(SELECT distinct`client_id` FROM `courses`) AND CHAR_LENGTH(`full_name`) > 3;

--------------

#5
 SELECT `make`,`model`,`condition` FROM `cars`
 ORDER BY `id`;
 
 #6
 SELECT d.`first_name`,d.`last_name`,c.`make`,c.`model`,c.`mileage` FROM `drivers` AS d
 JOIN `cars_drivers` AS cd
 ON d.`id` = cd.`driver_id`
 JOIN `cars` AS c
 ON cd.`car_id` = c.`id`
 WHERE c.`mileage` IS NOT NULL
 ORDER BY c.`mileage` DESC,d.`first_name` ASC;
 
 
 --------
 
 #7
 
 SELECT c.`id`,c.`make`,c.`mileage`,COUNT(co.`car_id`) AS 'count_of_courses', ROUND(AVG(co.`bill`),2) AS 'avg_bill' FROM `cars` AS c
 LEFT JOIN `courses` AS co
 ON c.`id` = co.`car_id`
 GROUP BY c.`id`
 HAVING count_of_courses != 2
 ORDER BY count_of_courses DESC, c.`id`;
 
 ---------

#8

SELECT c.`full_name`, COUNT(co.`client_id`) AS 'count_of_cars',SUM(co.`bill`) AS 'total_sum' FROM `clients` AS c
JOIN `courses` AS co
ON c.`id` = co.`client_id`
GROUP BY c.`full_name`
HAVING (c.`full_name`) LIKE '_a%' AND `count_of_cars` > 1
ORDER BY c.`full_name` ASC;

---------------

#9
SELECT a.`name`,(
CASE
WHEN  HOUR(`start`) BETWEEN 6 AND 20 THEN 'Day'
WHEN  HOUR(`start`) <= 5 THEN 'Night'
WHEN HOUR(`start`) >= 21 THEN 'Night'
END
) AS 'day_time',co.`bill`,cl.`full_name`,c.`make`,c.`model`,cat.`name` AS 'categoty_name' FROM `courses` co
LEFT JOIN `addresses` AS a
ON a.`id` = co.`from_address_id`
LEFT JOIN `clients` AS cl
ON cl.`id` = co.`client_id`
LEFT JOIN `cars` AS c
ON c.`id` = co.`car_id`
LEFT JOIN `categories` AS cat
ON cat.`id` = c.`category_id`
ORDER BY co.`id`;

----------------

#10
DELIMITER !!

CREATE FUNCTION udf_courses_by_client (phone_number VARCHAR(20))

RETURNS INT
DETERMINISTIC

BEGIN

RETURN (SELECT COUNT(co.`client_id`) FROM `courses` AS co
JOIN `clients` AS c
ON c.`id` = co.`client_id`
WHERE c.`phone_number` = phone_number);

END!!

DELIMITER ;
SELECT udf_courses_by_client ('(704) 2502909') as `count`;

---------------

#11
DELIMITER !!


CREATE PROCEDURE udp_courses_by_address (address_name VARCHAR(100))

BEGIN 

SELECT a.`name`,cl.`full_name`,(
CASE
WHEN co.`bill` <= 20 THEN 'Low'
WHEN co.`bill` <= 30 THEN 'Medium'
ELSE'High'
END
) AS 'level_of_bill',c.`make`,c.`condition`,cat.`name` FROM `addresses` AS a
JOIN `courses` AS co
ON a.`id` = co.`from_address_id`
JOIN `clients` AS cl
ON co.`client_id` = cl.`id`
JOIN `cars` AS c
ON co.`car_id` = c.`id`
JOIN `categories` AS cat
ON cat.`id` = c.`category_id`
WHERE a.`name` = address_name
ORDER BY c.`make`,cl.`full_name`;

END !!

CALL udp_courses_by_address('66 Thompson Drive');