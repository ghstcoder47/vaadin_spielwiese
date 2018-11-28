

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

set @pname = 'menus';
CALL proc_i18n_rename(@locale, @pname, 'INDV_Projekte_Projektübersicht_Expenses', 'Tätigkeiten & Spesen');

set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.expenses.form.ExpensesDataView';
CALL proc_i18n_rename(@locale, @pname, 'FRM_ADD', 'Hinzufügen');
CALL proc_i18n_rename(@locale, @pname, 'FRM_EDIT', 'Bearbeiten');
CALL proc_i18n_rename(@locale, @pname, 'FRM_READ', 'Betrachten');
CALL proc_i18n_rename(@locale, @pname, 'FRM_DEL', 'Löschen');
CALL proc_i18n_rename(@locale, @pname, 'FRM_FILTER', 'Filter');
CALL proc_i18n_rename(@locale, @pname, 'FRM_CHECK', 'Status setzen');
CALL proc_i18n_rename(@locale, @pname, 'FRM_CHECK_APPROVED', 'Kontrolliert');
CALL proc_i18n_rename(@locale, @pname, 'FRM_CHECK_CLOSED', 'Abgeschlossen');

CALL proc_i18n_rename(@locale, @pname, 'CED_ID', 'ID');
CALL proc_i18n_rename(@locale, @pname, 'USER', 'Mitarbeiter');
CALL proc_i18n_rename(@locale, @pname, 'KSK_COUNTRYCODE', 'Land');
CALL proc_i18n_rename(@locale, @pname, 'CED_DATE', 'Datum');
CALL proc_i18n_rename(@locale, @pname, 'CEA_ACTIVITY', 'Tätigkeit');
CALL proc_i18n_rename(@locale, @pname, 'CED_SERVICEZEIT', 'Dauer');
CALL proc_i18n_rename(@locale, @pname, 'CED_FAHRZEIT', 'Fahrt');
CALL proc_i18n_rename(@locale, @pname, 'CED_KM', 'Strecke');
CALL proc_i18n_rename(@locale, @pname, 'CED_AMOUNT_CURRENCY', 'Betrag');
CALL proc_i18n_rename(@locale, @pname, 'CED_STATUS_BILLABLE', 'Weiterverechenbar');
CALL proc_i18n_rename(@locale, @pname, 'CEC_ROLE', 'beauftragt');
CALL proc_i18n_rename(@locale, @pname, 'PSLV_NAME', 'Projekt');
CALL proc_i18n_rename(@locale, @pname, 'CED_COMMENT', 'Kommentar');
CALL proc_i18n_rename(@locale, @pname, 'CED_STATUS', 'Status');

set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.expenses.form.ExpensesInsertEdit';
CALL proc_i18n_rename(@locale, @pname, 'cancel', 'Abbrechen');
CALL proc_i18n_rename(@locale, @pname, 'save', 'Speichern');
CALL proc_i18n_rename(@locale, @pname, 'continue', 'Speichern & Weiter');
*/

INSERT INTO `cust_projekte_stammdaten_liste` (`PSLV_ID`, `PSLV_NAME`, `PSLV_GROUP`, `PSLV_CODE`, `PSLV_PROJECT_MAIL`)
VALUES ('100', 'Intern', '0', 'INT', 'kundenservice@hah.de.com');

INSERT INTO `cms_user_attributes` (`UA_ID_USER`, `UA_KEY`, `UA_VALUE`) VALUES (73, 'expenses_country', 'AT');
INSERT INTO `cms_auth_liste_gu` (`AL_U_ID`, `AL_G_ID`) VALUES (73, 44);

SET @menuId = 715;
-- create menu
INSERT INTO `cms_auth_stammdaten_menu` (`ASM_ID`, `ASM_U_MEN`, `ASM_BER`) VALUES (@menuId, 'INDV_Projekte_Projektübersicht_Expenses', '1');
-- block all
INSERT INTO cms_auth_liste_menu (ALM_G_ID, ALM_U_ID, ALM_LESEN, ALM_BEARBEITEN, ALM_LOESCHEN, ALM_ERSTELLEN)
	select AGL_ID, @menuId, 0, 0, 0, 0
	from cms_auth_stammdaten_group;
-- allow admin
UPDATE `cms_auth_liste_menu` SET
 `ALM_LESEN`=1, `ALM_BEARBEITEN`=1, `ALM_LOESCHEN`=1, `ALM_ERSTELLEN`=1
WHERE `ALM_U_ID`=@menuId and ALM_G_ID = 1;


INSERT INTO `cms_auth_stammdaten_group` (`AGL_ID`, `AGL_NAME`) VALUES (43, 'Finance');
insert ignore into cms_auth_liste_menu (ALM_G_ID, ALM_U_ID, ALM_LESEN, ALM_BEARBEITEN, ALM_LOESCHEN, ALM_ERSTELLEN)
select 43 as grp, ASM_ID, 0,0,0,0 from cms_auth_stammdaten_menu
where ASM_BER = 1;
update cms_auth_liste_menu
SET 
	ALM_LESEN = 1,
	ALM_BEARBEITEN = 1,
	ALM_LOESCHEN = 1,
	ALM_ERSTELLEN = 1
where ALM_G_ID = 43 and ALM_U_ID = 715;


INSERT INTO `cms_auth_stammdaten_group` (`AGL_ID`, `AGL_NAME`) VALUES (44, 'Spesenprüfung');
insert ignore into cms_auth_liste_menu (ALM_G_ID, ALM_U_ID, ALM_LESEN, ALM_BEARBEITEN, ALM_LOESCHEN, ALM_ERSTELLEN)
select 44 as grp, ASM_ID, 0,0,0,0 from cms_auth_stammdaten_menu
where ASM_BER = 1;
update cms_auth_liste_menu
SET 
	ALM_LESEN = 1,
	ALM_BEARBEITEN = 1,
	ALM_LOESCHEN = 0,
	ALM_ERSTELLEN = 0
where ALM_G_ID = 44 and ALM_U_ID = 715;

INSERT INTO `cms_auth_stammdaten_group` (`AGL_ID`, `AGL_NAME`) VALUES (45, 'Spesenerfassung');
insert ignore into cms_auth_liste_menu (ALM_G_ID, ALM_U_ID, ALM_LESEN, ALM_BEARBEITEN, ALM_LOESCHEN, ALM_ERSTELLEN)
select 45 as grp, ASM_ID, 0,0,0,0 from cms_auth_stammdaten_menu
where ASM_BER = 1;
update cms_auth_liste_menu
SET 
	ALM_LESEN = 1,
	ALM_BEARBEITEN = 1,
	ALM_LOESCHEN = 1,
	ALM_ERSTELLEN = 1
where ALM_G_ID = 45 and ALM_U_ID = 715;

-- cs permissions
update cms_auth_liste_menu
SET 
	ALM_LESEN = 1,
	ALM_BEARBEITEN = 1,
	ALM_LOESCHEN = 1,
	ALM_ERSTELLEN = 1
where ALM_G_ID = 14 and ALM_U_ID = 715;

-- nurse permissions
/*
update cms_auth_liste_menu
SET 
	ALM_LESEN = 1,
	ALM_BEARBEITEN = 1,
	ALM_LOESCHEN = 1,
	ALM_ERSTELLEN = 1
where ALM_G_ID = 12 and ALM_U_ID = 715;
*/
#cust_expenses_activity`

CREATE TABLE `cust_expenses_activity` (
	`CEA_ID` INT(11) NOT NULL AUTO_INCREMENT,
	`CEA_ACTIVITY` VARCHAR(64) NOT NULL COLLATE 'utf8_bin',
	`CEA_COST` INT(1) NULL DEFAULT NULL COMMENT 'NULL no fields, 0 only activities, 1 only amount',
	PRIMARY KEY (`CEA_ID`)
)
COLLATE='utf8_bin'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

INSERT INTO `cust_expenses_activity` (`CEA_ID`, `CEA_ACTIVITY`, `CEA_COST`) VALUES (1, 'Büroarbeit', 0);
INSERT INTO `cust_expenses_activity` (`CEA_ID`, `CEA_ACTIVITY`, `CEA_COST`) VALUES (2, 'Externes Schulungsevent', 0);
INSERT INTO `cust_expenses_activity` (`CEA_ID`, `CEA_ACTIVITY`, `CEA_COST`) VALUES (3, 'Fortbildung zu Hause', 0);
INSERT INTO `cust_expenses_activity` (`CEA_ID`, `CEA_ACTIVITY`, `CEA_COST`) VALUES (4, 'Internes Schulungsevent', 0);
INSERT INTO `cust_expenses_activity` (`CEA_ID`, `CEA_ACTIVITY`, `CEA_COST`) VALUES (5, 'Leitungstätigkeit', 0);
INSERT INTO `cust_expenses_activity` (`CEA_ID`, `CEA_ACTIVITY`, `CEA_COST`) VALUES (6, 'Pflegevisite', 0);
INSERT INTO `cust_expenses_activity` (`CEA_ID`, `CEA_ACTIVITY`, `CEA_COST`) VALUES (7, 'Qualitätsmanagement-Tätigkeit', 0);
INSERT INTO `cust_expenses_activity` (`CEA_ID`, `CEA_ACTIVITY`, `CEA_COST`) VALUES (8, 'Regionales Team Meeting', 0);
INSERT INTO `cust_expenses_activity` (`CEA_ID`, `CEA_ACTIVITY`, `CEA_COST`) VALUES (9, 'Spesen', 1);
INSERT INTO `cust_expenses_activity` (`CEA_ID`, `CEA_ACTIVITY`, `CEA_COST`) VALUES (10, 'T-Con (Intern)', 0);
INSERT INTO `cust_expenses_activity` (`CEA_ID`, `CEA_ACTIVITY`, `CEA_COST`) VALUES (11, 'T-Con (Projekt)', 0);
INSERT INTO `cust_expenses_activity` (`CEA_ID`, `CEA_ACTIVITY`, `CEA_COST`) VALUES (12, 'Urlaub/Krankheit', 0);
INSERT INTO `cust_expenses_activity` (`CEA_ID`, `CEA_ACTIVITY`, `CEA_COST`) VALUES (13, 'Vorstellung/Einarbeitung bei Patient', 0);
INSERT INTO `cust_expenses_activity` (`CEA_ID`, `CEA_ACTIVITY`, `CEA_COST`) VALUES (14, 'interne Audits (ohne Servicezeit bei Patient)', 0);
INSERT INTO `cust_expenses_activity` (`CEA_ID`, `CEA_ACTIVITY`, `CEA_COST`) VALUES (15, 'Strompauschale Kühlschrank', 1);
INSERT INTO `cust_expenses_activity` (`CEA_ID`, `CEA_ACTIVITY`, `CEA_COST`) VALUES (16, 'Strompauschale Kühlbox', 1);


#cust_expenses_coordinators

CREATE TABLE `cust_expenses_coordinators` (
	`CEC_ID` INT NOT NULL AUTO_INCREMENT,
	`CEC_ROLE` VARCHAR(255) NULL DEFAULT NULL,
	PRIMARY KEY (`CEC_ID`)
)
COLLATE='utf8_bin'
ENGINE=InnoDB
;

INSERT INTO `cust_expenses_coordinators` (`CEC_ID`, `CEC_ROLE`) VALUES (1, 'Clinical Director');
INSERT INTO `cust_expenses_coordinators` (`CEC_ID`, `CEC_ROLE`) VALUES (2, 'Country Manager Austria');
INSERT INTO `cust_expenses_coordinators` (`CEC_ID`, `CEC_ROLE`) VALUES (3, 'Customer Service');
INSERT INTO `cust_expenses_coordinators` (`CEC_ID`, `CEC_ROLE`) VALUES (4, 'Fachapotheke für seltene Krankheiten');
INSERT INTO `cust_expenses_coordinators` (`CEC_ID`, `CEC_ROLE`) VALUES (5, 'Finance');
INSERT INTO `cust_expenses_coordinators` (`CEC_ID`, `CEC_ROLE`) VALUES (6, 'Fuhrpark');
INSERT INTO `cust_expenses_coordinators` (`CEC_ID`, `CEC_ROLE`) VALUES (7, 'Geschäftsleitung');
INSERT INTO `cust_expenses_coordinators` (`CEC_ID`, `CEC_ROLE`) VALUES (8, 'Human Ressources');
INSERT INTO `cust_expenses_coordinators` (`CEC_ID`, `CEC_ROLE`) VALUES (9, 'IT Abteilung');
INSERT INTO `cust_expenses_coordinators` (`CEC_ID`, `CEC_ROLE`) VALUES (10, 'Leadnurse');
INSERT INTO `cust_expenses_coordinators` (`CEC_ID`, `CEC_ROLE`) VALUES (11, 'Materialwirtschaft');
INSERT INTO `cust_expenses_coordinators` (`CEC_ID`, `CEC_ROLE`) VALUES (12, 'Projektmanager');
INSERT INTO `cust_expenses_coordinators` (`CEC_ID`, `CEC_ROLE`) VALUES (13, 'Regionalleiter');
INSERT INTO `cust_expenses_coordinators` (`CEC_ID`, `CEC_ROLE`) VALUES (14, 'Team Logistik');


CREATE TABLE `cust_expenses_data` (
	`CED_ID` INT(11) NOT NULL AUTO_INCREMENT,
	`CED_CREATED` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
	`CED_UPDATED` TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
	`CED_STATUS` INT(2) NULL DEFAULT NULL,
	`CED_ID_USER` INT(100) NULL DEFAULT NULL,
	`CED_ID_CEA` INT(100) NULL DEFAULT NULL COMMENT 'action',
	`CED_ID_CEC` INT(100) NULL DEFAULT NULL COMMENT 'coordinators',
	`CED_ID_PROJECT` INT(100) NULL DEFAULT NULL,
	`CED_DATE` DATETIME NULL DEFAULT NULL,
	`CED_FAHRZEIT` TIME NULL DEFAULT NULL,
	`CED_SERVICEZEIT` TIME NULL DEFAULT NULL,
	`CED_KM` INT(100) NULL DEFAULT NULL,
	`CED_AMOUNT` DECIMAL(10,2) NULL DEFAULT NULL,
	`CED_CONTRACT` INT(2) NULL DEFAULT NULL  COMMENT 'see cust_user_employments',
	`CED_DAYS` INT(2) NULL DEFAULT NULL,
	`CED_COMMENT` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_bin',
	`CED_STATUS_BILLABLE` INT(1) NULL DEFAULT NULL,
	`CED_DECLINE_COMMENT` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8_bin',
	PRIMARY KEY (`CED_ID`),
	INDEX `CED_ID_NURSE` (`CED_ID_USER`),
	INDEX `CED_ID_CEA` (`CED_ID_CEA`),
	INDEX `CED_ID_CEC` (`CED_ID_CEC`),
	INDEX `CED_ID_PROJECT` (`CED_ID_PROJECT`)
)
COLLATE='utf8_bin'
ENGINE=InnoDB
AUTO_INCREMENT=1
;


CREATE TABLE `cust_user_employments` (
	`UES_ID` INT(11) NOT NULL AUTO_INCREMENT,
	`UES_TYP` VARCHAR(255) NULL DEFAULT '0' COLLATE 'utf8_bin',
	PRIMARY KEY (`UES_ID`)
)
COLLATE='utf8_bin'
ENGINE=InnoDB
;
INSERT INTO `cust_user_employments` (`UES_ID`, `UES_TYP`) VALUES (1, 'Minijob 450€');
INSERT INTO `cust_user_employments` (`UES_ID`, `UES_TYP`) VALUES (2, 'Freelancer');
INSERT INTO `cust_user_employments` (`UES_ID`, `UES_TYP`) VALUES (3, 'Festanstellung');


INSERT INTO `cust_schulung_status` (`SS_NAME`, `SS_PROZESS`, `SS_PROJEKT`, `SS_CODE`) VALUES ('Bearbeitung', '1', NULL, 'F107');
INSERT INTO `cust_schulung_status` (`SS_NAME`, `SS_PROZESS`, `SS_PROJEKT`, `SS_CODE`) VALUES ('Eingabe Abgeschlossen', '2', NULL, 'F107');
INSERT INTO `cust_schulung_status` (`SS_NAME`, `SS_PROZESS`, `SS_PROJEKT`, `SS_CODE`) VALUES ('Kontrolliert', '3', NULL, 'F107');
INSERT INTO `cust_schulung_status` (`SS_NAME`, `SS_PROZESS`, `SS_PROJEKT`, `SS_CODE`) VALUES ('Abgeschlossen', '4', NULL, 'F107');
INSERT INTO `cust_schulung_status` (`SS_NAME`, `SS_PROZESS`, `SS_PROJEKT`, `SS_CODE`) VALUES ('Zurück zur Bearbeitung', '5', NULL, 'F107');
INSERT INTO `cust_schulung_status` (`SS_NAME`, `SS_PROZESS`, `SS_PROJEKT`, `SS_CODE`) VALUES ('Abgelehnt', '6', NULL, 'F107');






/* *****************************
** ./sql/feature/nurse_changer
** ****************************/



/* *****************************
** ./sql/feature/projectfilter
** ****************************/

