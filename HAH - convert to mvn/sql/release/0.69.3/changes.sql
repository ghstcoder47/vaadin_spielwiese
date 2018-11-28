

/* *****************************
** ./sql/feature/container_rework
** ****************************/



/* *****************************
** ./sql/feature/distance_analytics_tool
** ****************************/



/* *****************************
** ./sql/feature/expenses
** ****************************/

#NAMEN ANGEPASST
UPDATE `cust_expenses_coordinators` SET `CEC_ROLE`='Fachapotheke' WHERE  `CEC_ID`=4;

#FIRMEN NAMEN KUERZEL HINZUGEFÜGT
ALTER TABLE `cust_internal_firms`
	ADD COLUMN `IF_NAME_ABRV` VARCHAR(5) NULL AFTER `IF_NAME`;
	
UPDATE `cust_internal_firms` SET `IF_NAME_ABRV`='HaH' WHERE  `IF_KEY`='if_hah';
UPDATE `cust_internal_firms` SET `IF_NAME_ABRV`='IaH' WHERE  `IF_KEY`='if_iah';

#TABELLE UM KONTROLLE_DATE UND KONTROLLE_USER ERWEITERT
ALTER TABLE `cust_expenses_data`
	ADD COLUMN `CED_STATUS_CHECKED_DATE` TIMESTAMP NULL DEFAULT NULL AFTER `CED_DECLINE_COMMENT`,
	ADD COLUMN `CED_STATUS_CHECKED_ID_USER` INT(11) NULL DEFAULT NULL AFTER `CED_STATUS_CHECKED_DATE`;


/* *****************************
** ./sql/feature/nurse_changer
** ****************************/



/* *****************************
** ./sql/feature/prescription_checking
** ****************************/



/* *****************************
** ./sql/feature/projectfilter
** ****************************/



/* *****************************
** ./sql/feature/timebutler_integration
** ****************************/

