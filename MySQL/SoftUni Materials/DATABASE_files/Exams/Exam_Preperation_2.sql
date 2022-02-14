	CREATE DATABASE `softuni_stores_system`;
    
    CREATE TABLE `pictures`(
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `url` VARCHAR(100) NOT NULL,
    `added_on` DATETIME NOT NULL
    );
    
	CREATE TABLE `categories`(
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(40) NOT NULL UNIQUE
    );
    
    CREATE TABLE `products`(
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(40) NOT NULL UNIQUE, 
    `best_before` DATE,
    `price` DECIMAL(10,2) NOT NULL,
    `description` TEXT,
    `category_id` INT NOT NULL,
    `picture_id` INT NOT NULL,
    CONSTRAINT fk_product_category
    FOREIGN KEY (category_id)
    REFERENCES categories(`id`),
	CONSTRAINT fk_product_picture
    FOREIGN KEY (picture_id)
    REFERENCES pictures(`id`)
    );
    
    CREATE TABLE `towns`(
      `id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(20) NOT NULL UNIQUE
    );
    
	CREATE TABLE `addresses`(
	`id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL UNIQUE,
    `town_id` INT NOT NULL,
    CONSTRAINT fk_address_town
    FOREIGN KEY (`town_id`)
    REFERENCES towns(`id`)
    );
    
    CREATE TABLE `stores`(
	`id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(20) NOT NULL UNIQUE,
    `rating` FLOAT NOT NULL,
    `has_parking` BOOLEAN DEFAULT(FALSE),
    `address_id` INT NOT NULL,
    CONSTRAINT fk_store_address
    FOREIGN KEY (`address_id`)
    REFERENCES addresses(`id`)
    );
    
    CREATE TABLE `products_stores`(
    `product_id` INT NOT NULL,
    `store_id` INT NOT NULL,
    
	CONSTRAINT fk_products_stores_product
    FOREIGN KEY (`product_id`)
    REFERENCES products(`id`),
    
    CONSTRAINT fk_products_stores_store
    FOREIGN KEY (`store_id`)
    REFERENCES stores(`id`),
    
    CONSTRAINT pk_product_sotre
    PRIMARY KEY(`product_id`,`store_id`)
    );
    
	CREATE TABLE `employees`(
	`id` INT PRIMARY KEY AUTO_INCREMENT,
    `first_name` VARCHAR(15) NOT NULL,
    `middle_name` CHAR(1),
    `last_name` VARCHAR(20) NOT NULL,
    `salary` DECIMAL(19,2) NOT NULL DEFAULT(0),
    `hire_date` DATE NOT NULL,
    `manager_id` INT,
    `store_id` INT NOT NULL,
     CONSTRAINT fk_employees_store
    FOREIGN KEY (`store_id`)
    REFERENCES stores(`id`),
    CONSTRAINT fk_manager_employee
    FOREIGN KEY (`manager_id`)
    REFERENCES employees(`id`)
    );
    
    
    --------
    #2
INSERT INTO `products_stores` (`product_id`,`store_id`)
SELECT `id`,(`category_id`) AS 'store_id' FROM `products`
GROUP BY `id`
HAVING `id` NOT IN(SELECT DISTINCT(`product_id`) FROM `products_stores`) AND IF(`store_id` != 1,1,1);

-----------------

#3
UPDATE `employees`
SET `manager_id` = 3 
WHERE YEAR(`hire_date`) > 2003 AND `store_id` NOT IN (5,14);

UPDATE `employees`
SET `salary` = `salary` - 500
WHERE YEAR(`hire_date`) > 2003 AND `store_id` NOT IN (5,14);

-----------------

#4
DELETE FROM `employees`
WHERE `manager_id` IS NOT NULL AND `salary` >=6000;

----------------

#5

SELECT `first_name`,`middle_name`,`last_name`,`salary`,`hire_date` FROM `employees`
ORDER BY `hire_date` DESC;

----------------

#6
SELECT p.`name`,p.`price`,p.`best_before`,CONCAT(SUBSTRING(p.`description`,1,10),'...') AS short_description,pc.`url` FROM `products` AS p
JOIN `pictures` AS pc
ON pc.`id` = p.`picture_id`
WHERE CHAR_LENGTH(p.`description`) > 100 
AND YEAR(pc.`added_on`) < 2019 
AND p.`price` > 20
ORDER BY p.`price` DESC;

-----------------------

#7
SELECT s.`name`, COUNT(ps.`product_id`) AS 'produtc_count',ROUND(AVG(p.`price`),2) AS 'avg' FROM `stores` AS s
LEFT JOIN `products_stores` AS ps
ON ps.`store_id` = s.`id`
LEFT JOIN `products` AS p
ON p.`id` = ps.`product_id`
GROUP BY s.`name`
ORDER BY `produtc_count` DESC,`avg` DESC, s.`id`;

-----------------------

#8
SELECT CONCAT(e.`first_name`,' ',e.`last_name`) AS 'full_name',s.`name`,a.`name` AS 'address',e.`salary` FROM `employees` AS e
JOIN `stores`AS s
ON s.`id` = e.`store_id`
JOIN `addresses` AS a
ON s.`address_id` = a.`id`
WHERE e.`salary` < 4000 
AND a.`name` LIKE '%5%' 
AND CHAR_LENGTH(s.`name`) > 8 
AND e.`last_name` LIKE '%n';

---------------

#9
SELECT REVERSE(s.`name`) AS 'reversed_name',CONCAT(UPPER(t.`name`),'-',a.`name`) AS 'full_address', COUNT(e.`id`) AS 'employees_count' FROM `stores` AS s
LEFT JOIN `employees` AS e
ON e.`store_id` = s.`id`
LEFT JOIN `addresses` AS a
ON s.`address_id` = a.`id`
LEFT JOIN `towns` AS t
ON a.`town_id` = t.`id`
GROUP BY s.`name`
HAVING employees_count >= 1
ORDER BY full_address ASC;

----------------------------------

#10
DELIMITER !!

CREATE FUNCTION udf_top_paid_employee_by_store(store_name VARCHAR(50)) 

RETURNS TEXT
DETERMINISTIC

BEGIN

  return (select concat(concat
                              (e.first_name, ' ', e.middle_name, '. ', e.last_name), ' works in store for ',
                          (2020 - year(e.hire_date)),
                          ' years'
                       )
            from employees e
                     join stores s on s.id = e.store_id
            where s.name = store_name
            order by salary desc limit 1);

END!!
DELIMITER ;

SELECT udf_top_paid_employee_by_store('Stronghold') as 'full_info';

------------------------

#11
DELIMITER !!

CREATE PROCEDURE udp_update_product_price (address_name VARCHAR (50))

BEGIN

IF  address_name LIKE '0%'
THEN UPDATE `products` AS p
JOIN `products_stores` AS ps
ON ps.`product_id` = p.`id`
JOIN `stores` AS s
ON ps.`store_id` = s.`id`
JOIN `addresses` AS a
ON s.`address_id` = a.`id`
SET p.`price` = p.`price` + 100
WHERE a.`name` = address_name;

ELSE
UPDATE `products` AS p
JOIN `products_stores` AS ps
ON ps.`product_id` = p.`id`
JOIN `stores` AS s
ON ps.`store_id` = s.`id`
JOIN `addresses` AS a
ON s.`address_id` = a.`id`
SET p.`price` = p.`price` + 200
WHERE a.`name` = address_name;

END IF;
END !!

DELIMITER ;

CALL udp_update_product_price('07 Armistice Parkway');
SELECT name, price FROM products WHERE id = 15;