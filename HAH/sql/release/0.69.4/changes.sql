/* *****************************
** from develop
** ****************************/

INSERT INTO `cms_mailer_groups` (`GROUP_KEY`, `MAIL_ENTRY`, `ENTRY_FIELD`) VALUES ('_noreply', 'noreply@healthcare-at-home.de', 'REC');

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
** ./sql/feature/prescription_checking
** ****************************/

/*

CALL proc_i18n_rebuild();
set @locale = 'de';

set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Prescriptions.PrescriptionManager.form.PrescriptionManagerInsertEdit';
CALL proc_i18n_rename(@locale, @pname, 'checked', 'kontrolliert');
CALL proc_i18n_rename(@locale, @pname, 'checked_date', 'Kontrolldatum');
CALL proc_i18n_rename(@locale, @pname, 'checked_user', 'Kontrolliert durch');


set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Prescriptions.PrescriptionManager.form.PrescriptionManagerDataView';
CALL proc_i18n_rename(@locale, @pname, 'checked', 'kontrolliert');

*/

ALTER TABLE `cust_prescriptions_list`
	ADD COLUMN `PL_CHECKED_ID_USER` INT NULL DEFAULT NULL,
	ADD COLUMN `PL_CHECKED_DATE` DATETIME NULL DEFAULT NULL,
  ADD INDEX `PL_CHECKED_ID_USER` (`PL_CHECKED_ID_USER`);

delete cust_prescriptions_list_entries from cust_prescriptions_list_entries
left join cust_prescriptions_list on PL_ID = PLE_PL_ID
where PL_ID IS NULL;
  
ALTER TABLE `cust_prescriptions_list_entries`
	ADD CONSTRAINT `FK_prescription_entries_parent` FOREIGN KEY (`PLE_PL_ID`) REFERENCES `cust_prescriptions_list` (`PL_ID`) ON UPDATE CASCADE ON DELETE CASCADE;


/* *****************************
** ./sql/feature/projectfilter
** ****************************/

