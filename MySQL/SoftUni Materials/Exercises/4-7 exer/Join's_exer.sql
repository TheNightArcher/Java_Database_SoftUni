
#1

SELECT e.`employee_id`,CONCAT(`first_name`, ' ',`last_name`) AS 'full_name', d.`department_id`, CONCAT(`name`) AS 'department_name'
FROM `employees` AS e
JOIN `departments` AS d
ON e.`employee_id` = d.`manager_id`
ORDER BY e.`employee_id`
LIMIT 5;

--------------------------------------
#2

SELECT t.`town_id`,CONCAT(`name`) AS 'town_name',a.`address_text`
FROM `towns` AS t
JOIN `addresses` AS a
ON t.`town_id` = a.`town_id`
WHERE t.`name` IN ('San Francisco','Sofia','Carnation')
ORDER BY t.`town_id` ASC, a.`address_id` ASC;

--------------------------------------------
#3

SELECT `employee_id`,`first_name`,`last_name`,`department_id`,`salary` FROM `employees`
WHERE `manager_id` IS NULL;

-----------------------------------------
#4

SELECT COUNT(`salary`) AS 'count' FROM `employees`
WHERE `salary` > (SELECT AVG(`salary`) FROM `employees`);

---------------------------------------
-- More exersices here i connet them all  cuz its seems way better then slplit them as tow notpads ;)

#1

SELECT e.`employee_id`,e.`job_title`,a.`address_id`,a.`address_text` 
FROM `employees` AS e
JOIN `addresses` AS a
ON e.`address_id` = a.`address_id`
ORDER BY a.`address_id` ASC LIMIT 5;

---------------------------

#2

SELECT e.`first_name`, e.`last_name`, t.`name`, a.`address_text` FROM `employees` AS e
JOIN `addresses` AS a
ON e.`address_id` = a.`address_id`
JOIN `towns` AS t
ON a.`town_id` = t.`town_id`
ORDER BY e.`first_name` ASC, e.`last_name` ASC LIMIT 5;

---------------------------

#3

SELECT e.`employee_id`, e.`first_name`, e.`last_name`, d.`name` AS 'deprtment_name' FROM `employees` AS e
JOIN `departments` AS d
ON e.`department_id` = d.`department_id`
WHERE d.`name` = 'Sales'
ORDER BY e.`employee_id` DESC;

-----------------------------------

#4

SELECT e.`employee_id`, e.`first_name`, e.`salary`, d.`name` AS 'deprtment_name' FROM `employees` AS e
JOIN `departments` AS d
ON e.`department_id` = d.`department_id`
WHERE e.`salary` > 15000
ORDER BY d.`department_id` DESC LIMIT 5;

--------------------------------------

#5

SELECT e.`employee_id`, e.`first_name` FROM `employees` AS e
LEFT JOIN `employees_projects` AS e_p
ON e.`employee_id` = e_p.`employee_id`
WHERE e_p.`project_id` IS NULL
ORDER BY e.`employee_id` DESC LIMIT 3;

------------------------------------

#6

SELECT e.`first_name`, e.`last_name`, e.`hire_date`, d.`name` AS 'dept_name' FROM `employees` AS e
JOIN `departments` AS d
ON e.`department_id` = d.`department_id`
WHERE d.`name` IN ('Sales','Finance') AND e.`hire_date` > '1999-01-01'
ORDER BY e.`hire_date` ASC;

----------------------------------

#7

SELECT e.`employee_id`, e.`first_name`, p.`name` AS 'project_name' FROM `employees` AS e
JOIN `employees_projects` AS e_p
ON e.`employee_id` = e_p.`employee_id`
JOIN `projects` AS p
ON e_p.`project_id` = p.`project_id`
WHERE DATE(p.`start_date`) > '2002-08-13' AND p.`end_date` IS NULL
ORDER BY e.`first_name`, p.`name`
LIMIT 5;

-----------------------------

#8

SELECT e.`employee_id`, e.`first_name`,
IF(YEAR(p.`start_date`) >= '2005',NULL,p.`name`) AS 'project_name' 
FROM `employees` AS e
JOIN `employees_projects` AS ep
ON e.`employee_id` = ep.`employee_id`
JOIN `projects` AS p
ON ep.`project_id` = p.`project_id`
WHERE ep.`employee_id` = 24
ORDER BY p.`name`;

-----------------------------

#9

SELECT e.`employee_id`, e.`first_name`,e.`manager_id`, m.`first_name` AS 'manager_name' FROM `employees` AS e
JOIN `employees` AS m
ON e.`manager_id` = m.`employee_id`
WHERE e.`manager_id` IN (3,7)
ORDER BY e.`first_name`;

---------------------------------

#10

SELECT e.`employee_id`,CONCAT(e.`first_name`,' ',e.`last_name`) AS 'employee_name',CONCAT(m.`first_name`,' ',m.`last_name`) AS 'manager_name', d.`name` AS 'department_name'
 FROM `employees` AS e
 JOIN `employees` AS m
 ON e.`manager_id` = m.`employee_id`
 JOIN `departments` AS d
 ON e.`department_id` = d.`department_id`
 WHERE e.`manager_id` IS NOT NULL
 ORDER BY e.`employee_id` ASC
 LIMIT 5;
 
 ------------------
 
 #11
 
 SELECT AVG(`salary`) AS 'min_average_salary' FROM `employees`
 GROUP BY `department_id`
 ORDER BY `min_average_salary`
 LIMIT 1;

------------------

#12

SELECT c.`country_code`, m.`mountain_range`, p.`peak_name`, p.`elevation` FROM `countries` AS c
JOIN `mountains_countries` AS mc
ON c.`country_code` = mc.`country_code`
JOIN `mountains` AS m
ON m.`id` = mc.`mountain_id`
JOIN `peaks` AS p
ON m.`id` = p.`mountain_id`
WHERE c.`country_code` = 'BG' AND p.`elevation` > 2835
ORDER BY p.`elevation` DESC;
 
 --------------------------
 
 #13
 
 SELECT c.`country_code`, COUNT(m.`mountain_range`) FROM `countries` AS c
JOIN `mountains_countries` AS mc
ON c.`country_code` = mc.`country_code`
JOIN `mountains` AS m
ON m.`id` = mc.`mountain_id`
group by c.`country_code`
HAVING `country_code` IN('BG','RU','US')
ORDER BY COUNT(mountain_range) DESC;

------------------------

#14

SELECT c.`country_name`, r.`river_name` FROM `countries` AS c
LEFT JOIN `countries_rivers` AS cr
ON  c.`country_code` = cr.`country_code`
LEFT JOIN `rivers` AS r
ON cr.`river_id` = r.`id`
WHERE c.`continent_code` = 'AF'
ORDER BY c.`country_name` ASC
LIMIT 5;

 --------------------
 
 #16
 
 SELECT COUNT(c.`country_code`) FROM `countries` AS c
LEFT JOIN `mountains_countries` AS mc
 ON c.`country_code` = mc.`country_code`
LEFT JOIN `mountains` AS m
 ON mc.`mountain_id` = m.`id`
 WHERE mc.`mountain_id` IS NULL;
 
 ---------------------------
 
 #17
 
 SELECT c.`country_name`,MAX(p.`elevation`) AS `highest_peak_elevation` ,MAX(r.`length`) AS `longest_river_length` 
 FROM `countries` AS c
LEFT JOIN `mountains_countries` AS mc
 ON c.`country_code` = mc.`country_code`
LEFT JOIN `peaks` AS p
 ON mc.`mountain_id` = p.`mountain_id`
 LEFT JOIN `countries_rivers` AS cr
 ON c.`country_code` = cr.`country_code`
LEFT JOIN `rivers` AS r
 ON cr.`river_id` = r.`id`
 GROUP BY c.`country_name`
 ORDER BY p.`highest_peak_elevation` DESC, r.`longest_river_length` DESC, c.`country_name`  LIMIT 5;
