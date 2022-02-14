CREATE DATABASE `soft_uni`;

CREATE TABLE `towns` (
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(40)
);

CREATE TABLE `addresses` (
`id` INT PRIMARY KEY AUTO_INCREMENT,
`address_text` VARCHAR(60),
`town_id` INT,
CONSTRAINT fk_address_town_id
FOREIGN KEY (`town_id`) REFERENCES towns(`id`)
);

CREATE TABLE`departments`(
`id` INT PRIMARY KEY  AUTO_INCREMENT,
`name` VARCHAR(60)
);


CREATE TABLE `employees` (
`id` INT PRIMARY KEY AUTO_INCREMENT,
`first_name` VARCHAR(40),
`middle_name` VARCHAR(40),
`last_name` VARCHAR(40),
`job_title` VARCHAR(60),
`department_id` INT,
`hire_date` DATE,
`salary` DECIMAL,
`address_id` INT,
CONSTRAINT fk_employee_department
FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`),
CONSTRAINT fk_employee_address_id
FOREIGN KEY (`address_id`) REFERENCES `addresses` (`id`)
);

INSERT INTO `towns`
VALUE
(1,'Sofia'),
(2,'Plovdiv'),
(3,'Varna'),
(4,'Burgas');

INSERT INTO `departments`
VALUE
(1,'Engineering'),
(2,'Sales'),
(3,'Marketing'),
(4,'Software Development'),
(5,'Quality Assurance');

INSERT INTO `employees`
VALUE
(1,'Ivan', 'Ivanov', 'Ivanov','.NET Developer',4,'2013-02-01',3500.00,NULL),
(2,'Petar', 'Petrov', 'Petrov','Senior Engineer',1,'2004-03-02',4000.00,NULL),
(3,'Maria', 'Petrova', 'Ivanova','Intern',5,'2016-08-28',525.25,NULL),
(4,'Georgi', 'Terziev', 'Ivanov','CEO',2,'2007-12-09',3000.00,NULL),
(5,'Peter', 'Pan', 'Pan','Intern',3,'2016-08-28',599.88,NULL);

-----------------------------------------------------------------------


SELECT * FROM `towns`
ORDER BY `name`;	

SELECT * FROM `departments`
ORDER BY `name`;

SELECT * FROM `employees`
 ORDER  BY `salary` DESC;


SELECT `name` FROM `towns`
ORDER BY `name`;
SELECT `name` FROM `departments`
ORDER BY `name`;
SELECT `first_name`,`last_name`,`job_title`,`salary` FROM `employees`
 ORDER  BY `salary` DESC;


UPDATE  `employees` 
SET `salary` = `salary`* 1.1;
SELECT `salary` FROM `employees`;


TRUNCATE TABLE `occupancies`;

 ----------------------------------------------------------------
 
 
 