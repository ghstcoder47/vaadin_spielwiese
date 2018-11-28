/* *****************************
** from develop
** ****************************/

select * from cms_auth_liste_menu where ALM_G_ID = 11 and ALM_U_ID = 14;

ALTER TABLE `cms_auth_liste_menu`
	ADD UNIQUE INDEX `ALM_G_ID_ALM_U_ID` (`ALM_G_ID`, `ALM_U_ID`);
  
-- update view porotokoll__overview: add
--	, cust_patient_stammdaten_liste.PSL_COUNTYCODE as patientCountry

/* *****************************
** ./sql/feature/akquise
** ****************************/

CREATE TABLE `cust_protokoll_servicetasking_module_acquisition_topics` (
	`ID` INT(11) NOT NULL,
	`PARENT` INT(11) NULL DEFAULT NULL,
	`TOPIC` VARCHAR(50) NOT NULL COLLATE 'utf8_bin',
	PRIMARY KEY (`ID`)
)
COLLATE='utf8_bin'
ENGINE=InnoDB
;

CREATE TABLE `cust_organisation_stammdaten_liste` (
	`OSL_ID` INT(100) NOT NULL AUTO_INCREMENT,
	`OSL_CREATED` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Erstellungsdatum',
	`OSL_UPDATED` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Bearbeitungsdatum',
	`OSL_COUNTRYCODE` VARCHAR(2) NULL DEFAULT NULL COLLATE 'utf8_bin',
	`OSL_NAME` VARCHAR(254) NULL DEFAULT NULL COLLATE 'utf8_bin',
	`OSL_STRASSE` VARCHAR(254) NULL DEFAULT NULL COLLATE 'utf8_bin',
	`OSL_ORT` VARCHAR(254) NULL DEFAULT NULL COLLATE 'utf8_bin',
	`OSL_PLZ` VARCHAR(254) NULL DEFAULT NULL COLLATE 'utf8_bin',
	`OSL_ZUSATZ` VARCHAR(254) NULL DEFAULT NULL COLLATE 'utf8_bin',
	`OSL_TEL` VARCHAR(254) NULL DEFAULT NULL COLLATE 'utf8_bin',
	`OSL_FAX` VARCHAR(254) NULL DEFAULT NULL COLLATE 'utf8_bin',
	`OSL_EMAIL` VARCHAR(254) NULL DEFAULT NULL COLLATE 'utf8_bin',
	PRIMARY KEY (`OSL_ID`)
)
COLLATE='utf8_bin'
ENGINE=InnoDB
;

CREATE TABLE `cust_verk_arzt_organisation` (
	`VAO_ID` INT(100) NOT NULL AUTO_INCREMENT,
	`VAO_ARZT_ID` INT(100) NULL DEFAULT NULL,
	`VAO_ORGANISATION_ID` INT(100) NULL DEFAULT NULL,
	`VAO_POSITION` VARCHAR(254) NULL DEFAULT NULL COLLATE 'utf8_bin',
	`VAO_DATE_START` DATE NOT NULL,
	`VAO_DATE_UNTIL` DATE NULL DEFAULT NULL,
	PRIMARY KEY (`VAO_ID`),
	INDEX `VAI_A_ID` (`VAO_ARZT_ID`),
	INDEX `VAI_I_ID` (`VAO_ORGANISATION_ID`)
)
COLLATE='utf8_bin'
ENGINE=InnoDB
;

CREATE TABLE `cust_protokoll_servicetasking_module_acquisition` (
	`PARENT` INT(11) NOT NULL,
	`CREATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`UPDATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`PSMA_CONTACT_ID` INT(8) NULL DEFAULT NULL COMMENT 'ARZT_ID',
	`PSMA_ORGANISATION_ID` INT(8) NULL DEFAULT NULL,
	`PSMA_TODO` TEXT NULL DEFAULT NULL,
	PRIMARY KEY (`PARENT`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `cust_protokoll_servicetasking_module_acquisition_multi` (
	`PARENT` INT(11) NOT NULL,
	`CREATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`UPDATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`KEY` INT(11) NULL DEFAULT NULL,
	`DATA` TINYINT(1) NULL DEFAULT NULL,
	UNIQUE INDEX `PARENT_KEY` (`KEY`, `PARENT`),
	INDEX `KEY` (`KEY`),
	INDEX `PARENT` (`PARENT`),
	CONSTRAINT `PARENT_SA_MODULE_ACQUISITION_MULTI` FOREIGN KEY (`PARENT`) REFERENCES `cust_protokoll_servicetasking` (`ST_ID`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

SET @menuId = 701;
-- create menu
INSERT INTO `cms_auth_stammdaten_menu` (`ASM_ID`, `ASM_U_MEN`, `ASM_BER`) VALUES (@menuId, 'INDV_Stammdaten_Organisationen', '1');
-- block all
INSERT INTO cms_auth_liste_menu (ALM_G_ID, ALM_U_ID, ALM_LESEN, ALM_BEARBEITEN, ALM_LOESCHEN, ALM_ERSTELLEN)
	select AGL_ID, @menuId, 0, 0, 0, 0
	from cms_auth_stammdaten_group;
-- allow admin
UPDATE `cms_auth_liste_menu` SET
 `ALM_LESEN`=1, `ALM_BEARBEITEN`=1, `ALM_LOESCHEN`=1, `ALM_ERSTELLEN`=1
WHERE `ALM_U_ID`=@menuId and ALM_G_ID = 1;

/* *****************************
** ./sql/feature/connectionpool
** ****************************/



/* *****************************
** ./sql/feature/servicetasking_rework
** ****************************/

-- TEMPLATE
/*
CREATE TABLE `cust_protokoll_servicetasking_module_test` (
	`PARENT` INT(11) NOT NULL,
	`CREATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`UPDATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`PARENT`),
	CONSTRAINT `PARENT_SA_MODULE_TEST` FOREIGN KEY (`PARENT`) REFERENCES `cust_protokoll_servicetasking` (`ST_ID`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
*/

CREATE TABLE `cust_protokoll_servicetasking_submodules` (
	`ID_PROJECT` INT(11) NOT NULL,
	`MODULECLASS` VARCHAR(2048) NOT NULL
)
ENGINE=InnoDB
;

insert into cust_protokoll_servicetasking_submodules (ID_PROJECT, MODULECLASS)
select PSLV_ID, 'PatientModule' from cust_projekte_stammdaten_liste
where PSLV_CODE = 'SA';


-- new project

SET @gid = 7001;
SET @gname = 'SA-Akquise';
SET @pname = @gname;

INSERT INTO `cms_auth_stammdaten_group` (`AGL_ID`, `AGL_NAME`)
VALUES (@gid, @gname);

INSERT INTO `cust_projekte_stammdaten_liste` (`PSLV_ID`, `PSLV_NAME`, `PSLV_GROUP`, `PSLV_CODE`)
VALUES (@gid, @pname, @gid, 'SA');

INSERT INTO cust_projekte_blankos (PB_P_ID, PB_T, PB_C, PB_V, PB_F, PB_RRULE_TYPE)
SELECT @gid, PB_T, PB_C, PB_V, PB_F, PB_RRULE_TYPE from cust_projekte_blankos where PB_P_ID = 5022 and PB_RRULE_TYPE IS NOT NULL;

insert into cust_protokoll_servicetasking_submodules (ID_PROJECT, MODULECLASS)
VALUES (@gid, 'SubmoduleAcquisition');

-- config

CREATE TABLE `cust_protokoll_servicetasking_config` (
	`KEY` VARCHAR(50) NULL DEFAULT NULL,
	`ID_PROJECT` INT(11) NULL DEFAULT NULL,
	`VALUE` VARCHAR(50) NULL DEFAULT NULL,
	UNIQUE INDEX `KEY_ID_PROJECT_VALUE` (`KEY`, `ID_PROJECT`, `VALUE`),
	INDEX `KEY_ID_PROJECT` (`KEY`, `ID_PROJECT`)
)
ENGINE=InnoDB
;

insert into cust_protokoll_servicetasking_config (`KEY`, ID_PROJECT, `VALUE`)
select 'patient_required', PSLV_ID, NULL from cust_projekte_stammdaten_liste
where PSLV_CODE = 'SA';

insert into cust_protokoll_servicetasking_config (`KEY`, ID_PROJECT, `VALUE`)
select 'create_own', PSLV_ID, 12 from cust_projekte_stammdaten_liste
where PSLV_CODE = 'SA';

insert into cust_protokoll_servicetasking_config (`KEY`, ID_PROJECT, `VALUE`)
select 'create_all', PSLV_ID, 1 from cust_projekte_stammdaten_liste
where PSLV_CODE = 'SA';

insert into cust_protokoll_servicetasking_config (`KEY`, ID_PROJECT, `VALUE`)
select 'create_all', PSLV_ID, 14 from cust_projekte_stammdaten_liste
where PSLV_CODE = 'SA';

insert into cust_protokoll_servicetasking_config (`KEY`, ID_PROJECT, `VALUE`)
VALUES
	('nurse_required', 7001, NULL);
DELETE FROM cust_protokoll_servicetasking_config WHERE  `KEY`='patient_required' AND `ID_PROJECT`=7001;

-- comments

CREATE TABLE `cust_comments` (
	`C_ID` INT(11) NOT NULL AUTO_INCREMENT,
	`C_CREATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`C_UPDATED` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`C_ID_MAIN` INT(11) NOT NULL,
	`C_ID_AUTHOR` INT(11) NULL DEFAULT NULL,
	`C_COMMENT` TEXT NOT NULL COLLATE 'utf8_bin',
	`C_ID_ORIGIN` INT(11) NULL DEFAULT NULL,
	`C_ID_TYPE` INT(11) NULL DEFAULT NULL,
	`C_DATA` INT(11) NULL DEFAULT NULL,
	PRIMARY KEY (`C_ID`)
)
COLLATE='utf8_bin'
ENGINE=InnoDB
ROW_FORMAT=DYNAMIC
;

CREATE TABLE `cust_comments_origins` (
	`CO_ID` INT(11) NOT NULL AUTO_INCREMENT,
	`CO_KEY` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`CO_ID`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `cust_comments_types` (
	`CT_ID` INT(11) NOT NULL AUTO_INCREMENT,
	`CT_KEY` VARCHAR(50) NULL DEFAULT NULL,
	PRIMARY KEY (`CT_ID`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

-- servicetyp rework

ALTER TABLE `cust_protokoll_servicetasking_master_types`
	ADD COLUMN `STXT_ID` INT NOT NULL AUTO_INCREMENT FIRST,
	DROP PRIMARY KEY,
	ADD PRIMARY KEY (`STXT_ID`);
  
INSERT INTO cust_protokoll_servicetasking_master_types (STXT_KEY) 
VALUES ('servicetype_in_writing');
  
update cust_protokoll_servicetasking
	SET ST_TYPE = (select STXT_ID from cust_protokoll_servicetasking_master_types where STXT_KEY = ST_TYPE);
ALTER TABLE `cust_protokoll_servicetasking`
	CHANGE COLUMN `ST_TYPE` `ST_ID_TYPE` INT(11) NULL DEFAULT NULL AFTER `ST_ID_NURSE`;

update cust_protokoll_servicetasking_meta_dynamic
	SET STMD_TYPE = (select STXT_ID from cust_protokoll_servicetasking_master_types where STXT_KEY = STMD_TYPE);
ALTER TABLE cust_protokoll_servicetasking_meta_dynamic
	CHANGE COLUMN `STMD_TYPE` `STMD_TYPE` INT(11) NULL DEFAULT NULL;
  
CREATE TABLE `cust_protokoll_servicetasking_project_types` (
	`ID_PROJECT` INT(11) NULL DEFAULT NULL,
	`ID_TYPE` INT(11) NULL DEFAULT NULL,
	UNIQUE INDEX `ID_PROJECT_ID_TYPE` (`ID_PROJECT`, `ID_TYPE`),
	INDEX `ID_PROJECT` (`ID_PROJECT`),
	INDEX `ID_TYPE` (`ID_TYPE`),
	CONSTRAINT `FK_project_types_project` FOREIGN KEY (`ID_PROJECT`) REFERENCES `cust_projekte_stammdaten_liste` (`PSLV_ID`) ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT `FK_project_types_type` FOREIGN KEY (`ID_TYPE`) REFERENCES `cust_protokoll_servicetasking_master_types` (`STXT_ID`) ON UPDATE CASCADE ON DELETE CASCADE
)
ENGINE=InnoDB
;

insert into cust_protokoll_servicetasking_project_types (ID_PROJECT, ID_TYPE)
	select PSLV_ID, STXT_ID from
	cust_projekte_stammdaten_liste
	join cust_protokoll_servicetasking_master_types
  WHERE PSLV_CODE = 'SA';
  
-- i18n manual
set @locale = 'de';

set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.form.ServiceTaskingDataView';
CALL proc_i18n_rename(@locale, @pname, 'ST_ID_TYPE', 'Typ');

set @pname = 'cust_protokoll_servicetasking_master_types';
CALL proc_i18n_rename(@locale, @pname, 'servicetype_in_writing', 'Anschreiben');

set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.form.submodules.PatientCommentModule';
CALL proc_i18n_rename(@locale, @pname, 'idFilter', 'Nur protokollspezifische Einträge anzeigen');
set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.form.submodules.SubmoduleAcquisition';
CALL proc_i18n_rename(@locale, @pname, 'idFilter', 'Nur protokollspezifische Einträge anzeigen');

set @pname = 'menus';
CALL proc_i18n_rename(@locale, @pname, 'INDV_Stammdaten_Organisationen', 'Organisationsverwaltung');

set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Organisationsverwaltung.form.OrganisationDataView';
CALL proc_i18n_rename(@locale, @pname, 'FRM_ADD', 'Hinzufügen');
CALL proc_i18n_rename(@locale, @pname, 'FRM_EDIT', 'Bearbeiten');
CALL proc_i18n_rename(@locale, @pname, 'FRM_READ', 'Betrachten');
CALL proc_i18n_rename(@locale, @pname, 'FRM_DEL', 'Löschen');
CALL proc_i18n_rename(@locale, @pname, 'FRM_FILTER', 'Filter');

CALL proc_i18n_rename(@locale, @pname, 'OSL_ID', 'Id');
CALL proc_i18n_rename(@locale, @pname, 'OSL_NAME', 'Name');
CALL proc_i18n_rename(@locale, @pname, 'OSL_STRASSE', 'Straße');
CALL proc_i18n_rename(@locale, @pname, 'OSL_ZUSATZ', 'Zusatz');
CALL proc_i18n_rename(@locale, @pname, 'OSL_PLZ', 'Plz');
CALL proc_i18n_rename(@locale, @pname, 'OSL_ORT', 'Ort');
CALL proc_i18n_rename(@locale, @pname, 'CC_TEXT', 'Land');

set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.Ärztverwaltung.Form.form_arzt_insert_edit';
CALL proc_i18n_rename(@locale, @pname, 'ArztMonitoring', 'Organisationen');

set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.form.submodules.SubmoduleAcquisition';
CALL proc_i18n_rename(@locale, @pname, 'comments', 'Kommentare');
CALL proc_i18n_rename(@locale, @pname, 'contents', 'Inhalte');
CALL proc_i18n_rename(@locale, @pname, 'topic', 'Thema');

-- develop
set @locale = 'de';

set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.vpriv.form.VprivDataView';
CALL proc_i18n_rename(@locale, @pname, 'PSL_COUNTYCODE', 'Land');
CALL proc_i18n_rename(@locale, @pname, 'infType', 'Infusionstyp');

set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.pop.form.PoPDataView';
CALL proc_i18n_rename(@locale, @pname, 'IF_NAME', 'Infusionsfirma');

set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.replagal.form.ReplagalDataView';
CALL proc_i18n_rename(@locale, @pname, 'infType', 'Infusionstyp');

set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.form.ServiceTaskingDataView';
CALL proc_i18n_rename(@locale, @pname, 'PSL_COUNTYCODE', 'Land');

set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.combined.form.CombinedDataView';
CALL proc_i18n_rename(@locale, @pname, 'patientCountry', 'Land');

set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.vpriv.form.vpriv_insert_edit';
CALL proc_i18n_rename(@locale, @pname, 'Gesamt-Infusionsvolumen', 'Gesamt-Infusionsvolumen  <br /> (inkl. Spüllösung)');
set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.replagal.form.replagal_insert_edit';
CALL proc_i18n_rename(@locale, @pname, 'Gesamt-Infusionsvolumen', 'Gesamt-Infusionsvolumen  <br /> (inkl. Spüllösung)');

