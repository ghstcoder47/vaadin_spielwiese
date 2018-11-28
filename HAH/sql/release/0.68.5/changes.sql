/* *****************************
** from develop
** ****************************/

CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `view_protokoll_sendable` AS select 
	COUNT(*) = 	SUM(COALESCE(checked, 0)) as sendable,
	arzt, firm_key, pdf_type, pdf_freq
from view_protokoll__overview

group by arzt, firm_key, pdf_type, pdf_freq  ;


ALTER TABLE `cust_protokoll_pop_daten`
	ADD COLUMN `REGIONAL_MANAGER_DATE` DATETIME NULL DEFAULT NULL AFTER `REGIONAL_MANAGER_OK`,
	ADD COLUMN `REGIONAL_MANAGER_USER` INT NULL DEFAULT NULL AFTER `REGIONAL_MANAGER_DATE`;

ALTER TABLE `cust_protokoll_servicetasking`
	ADD COLUMN `ST_REGIONAL_MANAGER_OK` TINYINT(1) NULL DEFAULT NULL AFTER `ST_STATUS`,
	ADD COLUMN `ST_REGIONAL_MANAGER_DATE` DATETIME NULL DEFAULT NULL AFTER `ST_REGIONAL_MANAGER_OK`,
	ADD COLUMN `ST_REGIONAL_MANAGER_USER` INT NULL DEFAULT NULL AFTER `ST_REGIONAL_MANAGER_DATE`;
  
CALL proc_i18n_rebuild();
set @locale = 'de';
set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.combined.form.CombinedDataView';
CALL proc_i18n_rename(@locale, @pname, 'sendable', 'Versendbar');

/* *****************************
** ./sql/feature/container_rework
** ****************************/



/* *****************************
** ./sql/feature/distance_analytics_tool
** ****************************/



/* *****************************
** ./sql/feature/nurse_changer
** ****************************/



/* *****************************
** ./sql/feature/projectfilter
** ****************************/

