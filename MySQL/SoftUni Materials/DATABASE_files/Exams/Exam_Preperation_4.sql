
CREATE DATABASE `sgd`;

CREATE TABLE `addresses`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(50) NOT NULL
);

CREATE TABLE `categories`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(10) NOT NULL
);

CREATE TABLE `offices`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`workspace_capacity` INT NOT NULL,
`website` VARCHAR(50),
`address_id` INT NOT NULL,
CONSTRAINT fk_office_address
FOREIGN KEY (`address_id`)
REFERENCES addresses(`id`)
);

CREATE TABLE `employees`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`first_name` VARCHAR(30) NOT NULL,
`last_name` VARCHAR(30) NOT NULL,
`age` INT NOT NULL,
`salary` DECIMAL(10,2) NOT NULL,
`job_title` VARCHAR(20) NOT NULL,
`happiness_level` CHAR(1) NOT NULL
);

CREATE TABLE `teams`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(40) NOT NULL,
`office_id` INT NOT NULL,
`leader_id` INT NOT NULL UNIQUE,
CONSTRAINT fk_team_office
FOREIGN KEY (`office_id`)
REFERENCES offices(`id`),

CONSTRAINT fk_team_leader
FOREIGN KEY (`leader_id`)
REFERENCES employees(`id`)
);

CREATE TABLE `games`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(50) NOT NULL UNIQUE,
`description` TEXT,
`rating` FLOAT NOT NULL DEFAULT(5.5),
`budget` DECIMAL(10,2) NOT NULL,
`release_date` DATE,
`team_id` INT,
CONSTRAINT fk_game_team
FOREIGN KEY (`team_id`)
REFERENCES teams(`id`)
);

CREATE TABLE `games_categories`(
`game_id` INT NOT NULL,
`category_id` INT NOT NULL,

CONSTRAINT fk_games_categories_game
FOREIGN KEY (`game_id`)
REFERENCES games(`id`),
 
CONSTRAINT fk_games_categories_categotry
FOREIGN KEY (`category_id`)
REFERENCES categories(`id`),

CONSTRAINT pk_games_categories
PRIMARY KEY(`game_id`,`category_id`)
);

-----------------

#2
INSERT INTO `games` (`name`,`rating`,`budget`,`team_id`)
(SELECT REVERSE(LOWER(SUBSTRING(`name`,2))) AS 'name',`id`,(`leader_id` * 1000) AS 'budget',`id` AS 'team_id' FROM `teams`
WHERE `id` BETWEEN 1 AND 9);

-----------------------

#3
UPDATE `employees`
SET `salary` = `salary` + 1000
WHERE `id` IN (SELECT `leader_id` FROM `teams`
) AND `age` < 40 AND `salary` <= 5000;

----------------------------

#4
DELETE FROM `games`
WHERE `id` NOT IN (SELECT `game_id` FROM `games_categories`) 
AND `release_date` IS NULL;

---------------------

#5
SELECT `first_name`,`last_name`,`age`,`salary`,`happiness_level` FROM `employees`
ORDER BY `salary`;	

---------------------

#6
SELECT t.`name`,a.`name`,CHAR_LENGTH(a.`name`) FROM `teams` AS t
JOIN `offices` AS o
ON t.`office_id` = o.`id`
JOIN `addresses` AS a
ON o.`address_id` = a.`id`
WHERE o.`website` IS NOT NULL
ORDER BY t.`name`,a.`name`;

------------------------------

#7
SELECT c.`name`,COUNT(g.`id`)AS 'game_count' ,ROUND(AVG(g.`budget`),2)AS 'avg_budget' ,MAX(g.`rating`) AS 'max_raitng' FROM `categories` AS c
LEFT JOIN `games_categories` AS gc
ON c.`id` = gc.`category_id` 
LEFT JOIN `games` AS g
ON gc.`game_id` = g.`id`
GROUP BY c.`name`
HAVING max_raitng >= 9.5
ORDER BY game_count DESC, c.`name`;

----------------------------------

#8
SELECT g.`name`,g.`release_date`,CONCAT(SUBSTRING(g.`description`,1,10),'...')AS 'summary',(
CASE
WHEN MONTH(release_date) IN(1,2,3) THEN 'Q1'
WHEN MONTH(release_date) IN(4,5,6) THEN 'Q2'
WHEN MONTH(release_date) IN(7,8,9) THEN 'Q3'
WHEN MONTH(release_date) IN(10,11,12) THEN 'Q4'
END
) AS 'quarter',t.`name` FROM `games` AS g
JOIN `teams` AS t
ON g.`team_id` = t.`id`
WHERE g.`name` LIKE '%2' AND month(g.`release_date`) % 2 = 0 AND YEAR(g.`release_date`) = 2022
ORDER BY `quarter`;

-----------------------------

#9
SELECT g.`name`,(
CASE
WHEN g.`budget` < 50000 THEN 'Normal budget'
WHEN g.`budget` >= 50000 THEN 'Insufficient budget'
END
) AS'budget_level' ,t.`name` AS 'team_name',a.`name` AS 'address_name' FROM `games` AS g
JOIN `teams` AS t
ON t.`id` = g.`team_id`
JOIN `offices` AS o
ON o.`id` = t.`office_id`
JOIN `addresses` AS a
ON a.`id` = o.`address_id`
WHERE g.`release_date` IS NULL AND g.`id` NOT IN(SELECT `game_id` FROM `games_categories`)
ORDER BY g.`name`;

--------------

#10
DELIMITER !!
CREATE FUNCTION udf_game_info_by_name (game_name VARCHAR (20))

RETURNS TEXT
DETERMINISTIC

BEGIN

RETURN(SELECT CONCAT(CONCAT('The ', g.`name`,' is developed by a ',t.`name`,' in an office with an address ',a.`name`)) FROM `games` AS g
 JOIN `teams` AS t
 ON t.`id` = g.`team_id`
 JOIN `offices` AS o
 ON o.`id` = t.`office_id`
 JOIN `addresses` AS a
 ON a.`id` = o.`address_id`
 WHERE g.`name` = game_name);

END !!

DELIMITER ;
SELECT udf_game_info_by_name('Bitwolf') AS info;

-----------------------

#11
DELIMITER !!

CREATE PROCEDURE udp_update_budget (min_rating FLOAT )

BEGIN

UPDATE `games`
SET `budget` = `budget` + 100000,`release_date` = DATE_ADD(`release_date`,INTERVAL 1 YEAR)
WHERE `id` NOT IN (SELECT `game_id` FROM `games_categories`) AND `rating` > min_rating AND `release_date` IS NOT NULL;

END !!

DELIMITER ;

CALL udp_update_budget (8);