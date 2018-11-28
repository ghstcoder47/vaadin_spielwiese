

/* *****************************
** ./sql/feature/container_rework
** ****************************/



/* *****************************
** ./sql/feature/distance_analytics_tool
** ****************************/



/* *****************************
** ./sql/feature/expenses
** ****************************/

/*
CALL proc_i18n_rebuild();
set @locale = 'de';
set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.expenses.form.ExpensesInsertEdit';
CALL proc_i18n_rename(@locale, @pname, 'unknown', 'Unbekannt');
*/

UPDATE cust_schulung_status
SET SS_CODE = 'EXP'
where SS_CODE = 'F107';

ALTER TABLE `cust_expenses_data`
	ADD COLUMN `CED_START` VARCHAR(5) NULL AFTER `CED_AMOUNT`,
	ADD COLUMN `CED_END` VARCHAR(5) NULL AFTER `CED_START`;
ALTER TABLE `cust_expenses_data`
	CHANGE COLUMN `CED_COMMENT` `CED_COMMENT` TEXT NULL DEFAULT NULL COLLATE 'utf8_bin' AFTER `CED_DAYS`;
	
#Project
INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (8001,'H700_MRN ALS','EXP','kundenservice@hah.de.com');
INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (8002,'H701_MRN Synta','EXP','kundenservice@hah.de.com');
INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (8003,'H702_MRN Synageva','EXP','kundenservice@hah.de.com');
INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (8004,'H703_MRN Otsuka','EXP','kundenservice@hah.de.com');
INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (8005,'H704_MRN Onyx','EXP','kundenservice@hah.de.com');
INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (8006,'H705_MRN Roche WN 28745','EXP','kundenservice@hah.de.com');
INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (10000,'H202_Lilly BELA','EXP','kundenservice@hah.de.com');
INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (10010,'H300_Astellas Qutenza','EXP','kundenservice@hah.de.com');
INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (10020,'H400_Merck Saizen','EXP','kundenservice@hah.de.com');
INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (10030,'H500_Pfizer Genotropin','EXP','kundenservice@hah.de.com');
INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (10040,'H800_Enobia','EXP','kundenservice@hah.de.com');
INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (10050,'H1000_Esbriet','EXP','kundenservice@hah.de.com');
INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (10060,'H1200_Ferring Zomacton','EXP','kundenservice@hah.de.com');
INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (10070,'H1600_Gebro_Alprostapint','EXP','kundenservice@hah.de.com');
INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (6061,'SA-OFEV-Apothekenbetreuung','EXP','kundenservice@hah.de.com');
INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (10080,'H1900_Ipsen_Nutropin','EXP','kundenservice@hah.de.com');
INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (10090,'H2100_Zogenix','EXP','kundenservice@hah.de.com');
INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (10100,'H1901 Ipsen Clarinet','EXP','kundenservice@hah.de.com');
INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (10200,'H1700_PD_Studie','EXP','kundenservice@hah.de.com');
INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (10110,'Kyprolis','EXP','kundenservice@hah.de.com');
INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (8007,'MRN-VTL 308','EXP','kundenservice@hah.de.com');
INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (8008,'MRN-PD Studie','EXP','kundenservice@hah.de.com');

INSERT INTO cust_projekte_stammdaten_liste (PSLV_ID, PSLV_NAME, PSLV_CODE, PSLV_PROJECT_MAIL) VALUES (99999,'Beauftragte Tätigkeiten', 'BTUS','kundenservice@hah.de.com');

#Activities
INSERT INTO cust_expenses_activity (`CEA_ID`, `CEA_ACTIVITY`, `CEA_COST`) VALUES ('17', 'Sonstiges', '0');

#Coordinators
INSERT INTO cust_expenses_coordinators (`CEC_ID`, `CEC_ROLE`) VALUES ('15', 'unbekannt');	

/* *****************************
** ./sql/feature/nurse_changer
** ****************************/



/* *****************************
** ./sql/feature/projectfilter
** ****************************/

