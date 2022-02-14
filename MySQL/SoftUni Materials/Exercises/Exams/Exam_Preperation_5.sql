CREATE DATABASE fsd;

CREATE TABLE `countries`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(45) NOT NULL
);

CREATE TABLE `towns`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(45) NOT NULL,
`country_id` INT,
CONSTRAINT fk_wont_contry
FOREIGN KEY (`country_id`)
REFERENCES countries(`id`)
);

CREATE TABLE `stadiums`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(45) NOT NULL,
`capacity` INT NOT NULL,
`town_id` INT NOT NULL,
CONSTRAINT fk_stadium_town
FOREIGN KEY (`town_id`)
REFERENCES towns(`id`)
);

CREATE TABLE `teams`(
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(45) NOT NULL,
`established` DATE NOT NULL,
`fan_base` BIGINT DEFAULT(0),
`stadium_id` INT NOT NULL,
CONSTRAINT fk_team_stadium
FOREIGN KEY (`stadium_id`)
REFERENCES stadiums(`id`)
);



