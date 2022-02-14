
DELIMITER !!

CREATE FUNCTION `ufn_count_employees_by_town` (town_name VARCHAR(30))
RETURNS INTEGER
DETERMINISTIC 
BEGIN

DECLARE e_count INT;

SET e_count :=( SELECT COUNT(t.`name`) FROM `employees` AS e
JOIN `addresses` AS a
ON e.`address_id` = a.`address_id`
JOIN `towns` AS t
ON a.`town_id` = t.`town_id`
WHERE t.`name` = town_name);

RETURN e_count;
END !!

DELIMITER ;

SELECT ufn_count_employees_by_town ('Sofia');

---------------------

#2
DELIMITER !!

CREATE PROCEDURE `usp_raise_salaries` (department_name VARCHAR (50))
BEGIN

UPDATE `employees` AS e
JOIN `departments` AS d
ON e.`department_id` = d.`department_id`
SET e.`salary` = e.`salary` * 1.05
WHERE d.`name`= department_name;
END !!

DELIMITER ;

------------------------

#3
DELIMITER !!

CREATE PROCEDURE `usp_raise_salary_by_id` (id INT)
BEGIN

START TRANSACTION;
 
IF ((SELECT COUNT(`employee_id`) FROM `employees` WHERE `employee_id` = `id`) != 1) THEN ROLLBACK;

ELSE
UPDATE `employees` AS e SET e.`salary` = e.`salary` * 1.05
WHERE e.`employee_id` = `id`;
END IF;

END!!

DELIMITER ;

------------------------

----- More Exer just i writte the mboth at the same place 

---------------------------------

#1
DELIMITER !!
CREATE PROCEDURE `usp_get_employees_salary_above_35000` ()
BEGIN

SELECT `first_name`,`last_name` FROM `employees` 
WHERE `salary` >35000
ORDER BY  `first_name` ASC, `last_name` ASC, `employee_id` ASC;

END !!

DELIMITER ;

CALL usp_get_employees_salary_above_35000();

-------------------------
#2
DELIMITER !!
CREATE PROCEDURE `usp_get_employees_salary_above` (given_number DECIMAL(10,4))
BEGIN

SELECT `first_name`,`last_name` FROM `employees` 
WHERE `salary` >= given_number
ORDER BY  `first_name` ASC, `last_name` ASC, `employee_id` ASC;

END !!

DELIMITER ;

CALL usp_get_employees_salary_above(45000.0000);

-----------------------------

#3
DELIMITER !!
CREATE PROCEDURE `usp_get_towns_starting_with` (given_string VARCHAR(50))
BEGIN

SELECT `name` FROM `towns` 
WHERE  LEFT(`name`,CHAR_LENGTH(given_string)) = given_string
ORDER BY  `name` ASC;

END !!

DELIMITER ;

CALL usp_get_towns_starting_with('b');

---------------------------------

#4
DELIMITER !!
CREATE PROCEDURE `usp_get_employees_from_town` (town_name VARCHAR(50))
BEGIN

SELECT `first_name`,`last_name` FROM `employees` AS e
JOIN `addresses` AS a
ON e.`address_id` = a.`address_id`
JOIN `towns` AS t
ON a.`town_id` = t.`town_id`
WHERE t.`name` = town_name
ORDER BY e.`first_name` ASC,e.`last_name` ASC, e.`employee_id` ASC;

END !!

DELIMITER ;

CALL usp_get_employees_from_town('Sofia');

---------------------------------------

#5
DELIMITER !!

CREATE FUNCTION `ufn_get_salary_level` (salary DECIMAL (19,4))
RETURNS VARCHAR(10)
DETERMINISTIC

RETURN (
CASE
WHEN salary < 30000 THEN 'Low'
WHEN salary <= 50000 THEN 'Average'
ELSE 'High' 
END)!!

DELIMITER ;
SELECT ufn_get_salary_level(32000.0000);

------------------

#6
DELIMITER !!
CREATE PROCEDURE `usp_get_employees_by_salary_level` (salary_lvl VARCHAR(10))

BEGIN

SELECT e.`first_name`,e.`last_name` FROM `employees` AS e
WHERE ufn_get_salary_level(e.`salary`) = salary_lvl
ORDER BY e.`first_name` DESC,e.`last_name` DESC;

END !!

DELIMITER ;

CALL usp_get_employees_by_salary_level('high');

--------------------------------

#7
DELIMITER !!
	CREATE FUNCTION ufn_is_word_comprised(set_of_letters varchar(50), word varchar(50))
    
    RETURNS BIT
   DETERMINISTIC
   
   BEGIN
   
   RETURN (SELECT word REGEXP(CONCAT('^[', set_of_letters, ']+$')));
   
   END !!
   
   DELIMITER ;
   SELECT ufn_is_word_comprised('oistmiahf','Sofia');
   
   --------------------------
   
   #8
   DELIMITER !!
   
   CREATE PROCEDURE usp_get_holders_full_name()
   
   BEGIN 
   
   SELECT CONCAT(`first_name`,' ',`last_name`) AS 'full_name' FROM `account_holders`
   ORDER BY `full_name` ASC, `id` ASC;
   
   END!!
   
   DELIMITER ;
   CALL usp_get_holders_full_name();
   
   ------------------------------------------
   
   #10
      DELIMITER !!
      CREATE FUNCTION ufn_calculate_future_value(initial_sum DECIMAL(19,4) , yearly_interest_rate DOUBLE,number_of_year INT)
      
      RETURNS DECIMAL(19,4)
      DETERMINISTIC
      
      BEGIN
      DECLARE FV DECIMAL(19,4);
      
      SET FV := initial_sum *(POWER((1 + yearly_interest_rate),number_of_year) );
      RETURN FV;
      
      END!!
      
      DELIMITER ;
      
      SELECT ufn_calculate_future_value(1000, 0.5, 5);
    
    ----------------------------
    
    #11
    DELIMITER !!
    CREATE PROCEDURE usp_calculate_future_value_for_account(acc_id INT,interest_rate DECIMAL(19,4))
    
    BEGIN
    
    SELECT a.`id`,ah.`first_name`,ah.`last_name`,a.`balance` AS 'current_balance',
    (SELECT ufn_calculate_future_value(a.`balance`,interest_rate,5) )AS 'balance_in_5_years'
    FROM `accounts` AS a
    JOIN `account_holders` AS ah
    ON a.`account_holder_id` = ah.`id`
    WHERE a.`id` = acc_id;
    
    END!!
    
     DELIMITER ;
    
    CALL usp_calculate_future_value_for_account(1,0.1);
    
    ---------------------------------------------------------
    
    #14
    DELIMITER !!
    CREATE PROCEDURE usp_transfer_money(`from_account_id` INT, `to_account_id` INT, `amount` DECIMAL(20, 4))
BEGIN 
 START TRANSACTION;
    IF `from_account_id` NOT IN(SELECT `id` FROM `accounts` WHERE `id` = `from_account_id`)
       OR `to_account_id` NOT IN(SELECT `id` FROM `accounts` WHERE `id` = `to_account_id`)
       OR (SELECT `balance` FROM `accounts` 
	   WHERE `id` = `from_account_id`) < `amount`
       OR `amount` <= 0 
       THEN ROLLBACK;
    ELSE
       UPDATE `accounts`
       SET `balance` = `balance` - `amount`
       WHERE `id` = `from_account_id`;
 
       UPDATE `accounts`
       SET `balance` = `balance` + `amount`
       WHERE `id` = `to_account_id`;
 
END IF;
END!!


---------------------------------------------------

#12

CREATE PROCEDURE usp_deposit_money(account_id INT, money_amount DECIMAL(19,4))

BEGIN
START TRANSACTION;
IF
money_amount <= 0
THEN ROLLBACK;
ELSE
     UPDATE `accounts`
       SET `balance` = `balance` + `money_amount`
       WHERE `id` = `account_id`;
END IF;
END!!

CALL usp_deposit_money(1,10);


---------------------------

#13

CREATE PROCEDURE usp_withdraw_money(account_id INT, money_amount DECIMAL(19,4))

BEGIN
START TRANSACTION;
IF
money_amount <= 0 OR (SELECT `balance` FROM `accounts`
WHERE `id` = account_id ) < money_amount
THEN ROLLBACK;
ELSE
     UPDATE `accounts`
       SET `balance` = `balance` - `money_amount`
       WHERE `id` = `account_id`;
END IF;
END!!

DELIMITER ;

SET sql_mode = 'ONLY_FULL_GROUP_BY';
