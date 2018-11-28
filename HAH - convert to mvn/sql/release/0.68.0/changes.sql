/* *****************************
** from develop
** ****************************/



/* *****************************
** ./sql/feature/admintools
** ****************************/

SET @menuId = 711;
-- create menu
INSERT INTO `cms_auth_stammdaten_menu` (`ASM_ID`, `ASM_U_MEN`, `ASM_BER`) VALUES (@menuId, 'INDV_Stammdaten_RegionalLeads', '1');
-- block all
INSERT INTO cms_auth_liste_menu (ALM_G_ID, ALM_U_ID, ALM_LESEN, ALM_BEARBEITEN, ALM_LOESCHEN, ALM_ERSTELLEN)
	select AGL_ID, @menuId, 0, 0, 0, 0
	from cms_auth_stammdaten_group;
-- allow admin
UPDATE `cms_auth_liste_menu` SET
 `ALM_LESEN`=1, `ALM_BEARBEITEN`=1, `ALM_LOESCHEN`=1, `ALM_ERSTELLEN`=1
WHERE `ALM_U_ID`=@menuId and ALM_G_ID = 1;

SET @menuId = 712;
-- create menu
INSERT INTO `cms_auth_stammdaten_menu` (`ASM_ID`, `ASM_U_MEN`, `ASM_BER`) VALUES (@menuId, 'INDV_Stammdaten_ProjectLeads', '1');
-- block all
INSERT INTO cms_auth_liste_menu (ALM_G_ID, ALM_U_ID, ALM_LESEN, ALM_BEARBEITEN, ALM_LOESCHEN, ALM_ERSTELLEN)
	select AGL_ID, @menuId, 0, 0, 0, 0
	from cms_auth_stammdaten_group;
-- allow admin
UPDATE `cms_auth_liste_menu` SET
 `ALM_LESEN`=1, `ALM_BEARBEITEN`=1, `ALM_LOESCHEN`=1, `ALM_ERSTELLEN`=1
WHERE `ALM_U_ID`=@menuId and ALM_G_ID = 1;

/* *****************************
** ./sql/feature/projectfilter
** ****************************/



/* *****************************
** ./sql/feature/sent_checklist
** ****************************/

CREATE TABLE `cust_protokoll_sent` (
	`ID_PROJECT` INT(11) NOT NULL,
	`ID_DATA` INT(11) NOT NULL,
	`ID_USER` INT(11) NOT NULL,
	`DATE_SENT` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
	UNIQUE INDEX `ID_PROJECT_ID_DATA` (`ID_PROJECT`, `ID_DATA`),
	INDEX `ID_USER` (`ID_USER`)
)
COLLATE='utf8_bin'
ENGINE=InnoDB
;


/* *****************************
** ./sql/feature/validator
** ****************************/

ALTER TABLE `cms_validator_rules`
	ADD COLUMN `OPTIONAL` TINYINT(1) NOT NULL DEFAULT '0' AFTER `REQUIRED`,
  DROP INDEX `PARENT_IDENTIFIER_CONDITION_TYPE`,
	ADD UNIQUE INDEX `PARENT_IDENTIFIER_CONDITION_TYPE` (`PARENT`, `IDENTIFIER`, `CONDITION_TYPE`, `CONDITION_VALUE`);
	-- ADD COLUMN `ALLOWED_VALUE` VARCHAR(50) NULL DEFAULT NULL AFTER `MAX`;

ALTER TABLE `cust_protokoll_pop_daten`
	ADD COLUMN `DEVIATES` TINYINT(1) NULL DEFAULT NULL;	

ALTER TABLE `cust_prescriptions_products`
	ADD COLUMN `PP_UNIT` VARCHAR(8) NOT NULL DEFAULT '';

INSERT INTO `cms_validator_conditions` (`KEY`) VALUES ('product_id');
INSERT INTO `cms_validator_conditions` (`KEY`) VALUES ('status_id');

set @k = (select `KEY` from cms_validator_components where `NAME` = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.pop.form.PoPInsertEdit');
insert into cms_validator_rules (PARENT, IDENTIFIER, CONDITION_TYPE, CONDITION_VALUE, REQUIRED)
VALUES
	(@k, 'billingHoursNurse', 'status_id', 3, 1),
	(@k, 'billingMinutesNurse', 'status_id', 3, 1),
	(@k, 'billingKilometersNurse', 'status_id', 3, 1),
	(@k, 'pop_date', 'status_id', 3, 1),
	(@k, 'txt_von', 'status_id', 3, 1),
	(@k, 'txt_bis', 'status_id', 3, 1),
	(@k, 'cbx_type', 'status_id', 3, 1),
	
	(@k, 'billingHoursNurse', 'status_id', 4, 1),
	(@k, 'billingMinutesNurse', 'status_id', 4, 1),
	(@k, 'billingKilometersNurse', 'status_id', 4, 1),
	(@k, 'pop_date', 'status_id', 4, 1),
	(@k, 'txt_von', 'status_id', 4, 1),
	(@k, 'txt_bis', 'status_id', 4, 1),
	(@k, 'cbx_type', 'status_id', 4, 1);
  
  
-- i18n

-- Exportiere Struktur von Prozedur hah_2.proc_i18n_rebuild
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `proc_i18n_rebuild`()
    MODIFIES SQL DATA
    COMMENT 'insert missing captions for all locales'
BEGIN

	INSERT INTO cms_i18n (PARENT, IDENTIFIER, LOCALE, CAPTION)
	    SELECT PARENT, IDENTIFIER, lang.LOCALE, src.CAPTION
	    FROM cms_i18n as src
	    JOIN cms_i18n_languages as lang
	    WHERE src.LOCALE = '--'
	ON DUPLICATE KEY UPDATE CAPTION = cms_i18n.CAPTION;

END//
DELIMITER ;

CALL proc_i18n_rebuild();

set @locale = 'de';
set @pname = 'archenoah.lib.vaadin.validator.ValidatorManager';
CALL proc_i18n_rename(@locale, @pname, 'msg:check_invalid.title', 'Unzulässige Daten');
CALL proc_i18n_rename(@locale, @pname, 'invalidDescription', 'Bitte überprüfen Sie folgende Angaben um speichern zu können.');

CALL proc_i18n_rename(@locale, @pname, 'msg:check_optional.title', 'Unerwartete Daten');
CALL proc_i18n_rename(@locale, @pname, 'invalidOptionalDescription', 'Bitte überprüfen Sie folgende Angaben. Falls korrekt, bitte mit "Speichern" bestätigen.');

CALL proc_i18n_rename(@locale, @pname, 'regex_error', 'Formatierungsfehler');

set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.pop.form.PoPInsertEdit';
CALL proc_i18n_rename(@locale, @pname, 'deviatingMainCount', 'Ampullenanzahl weicht von Delegation ab');
CALL proc_i18n_rename(@locale, @pname, 'Button', '');

set @pname = 'menus';
CALL proc_i18n_rename(@locale, @pname, 'INDV_Stammdaten_RegionalLeads', 'Regionalleiterverwaltung');
CALL proc_i18n_rename(@locale, @pname, 'INDV_Stammdaten_ProjectLeads', 'Projektleiterleiterverwaltung');

set @pname = 'archenoah.web.normal.main.Menuepunkte.Unsichtbar.Init_Menüpunkt_Entwicklung';
CALL proc_i18n_rename(@locale, @pname, 'userAttributes', 'Benutzer-Attribute');

set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.RegionalLeadsManager.form.RegionalLeadsDataView';
CALL proc_i18n_rename(@locale, @pname, 'FRM_ADD', 'Hinzufügen');
CALL proc_i18n_rename(@locale, @pname, 'FRM_EDIT', 'Bearbeiten');
CALL proc_i18n_rename(@locale, @pname, 'FRM_READ', 'Betrachten');
CALL proc_i18n_rename(@locale, @pname, 'FRM_DEL', 'Löschen');
CALL proc_i18n_rename(@locale, @pname, 'FRM_FILTER', 'Filter');
CALL proc_i18n_rename(@locale, @pname, 'SRL_ID', 'ID');
CALL proc_i18n_rename(@locale, @pname, 'SRL_ID_USER', 'Benutzer');
CALL proc_i18n_rename(@locale, @pname, 'SRL_ID_PROJEKT', 'Projekt');
CALL proc_i18n_rename(@locale, @pname, 'SRL_REGION', 'Region');

set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Stammdaten.ProjectLeadsManager.form.ProjectLeadsDataView';
CALL proc_i18n_rename(@locale, @pname, 'FRM_ADD', 'Hinzufügen');
CALL proc_i18n_rename(@locale, @pname, 'FRM_EDIT', 'Bearbeiten');
CALL proc_i18n_rename(@locale, @pname, 'FRM_READ', 'Betrachten');
CALL proc_i18n_rename(@locale, @pname, 'FRM_DEL', 'Löschen');
CALL proc_i18n_rename(@locale, @pname, 'FRM_FILTER', 'Filter');
CALL proc_i18n_rename(@locale, @pname, 'SPL_ID', 'ID');
CALL proc_i18n_rename(@locale, @pname, 'SPL_ID_USER', 'Benutzer');
CALL proc_i18n_rename(@locale, @pname, 'SPL_ID_PROJEKT', 'Projekt');


set @pname = 'archenoah.web.normal.main.Menuepunkte.Unsichtbar.UserAttributes.form.UserAttributesDataView';
CALL proc_i18n_rename(@locale, @pname, 'FRM_ADD', 'Hinzufügen');
CALL proc_i18n_rename(@locale, @pname, 'FRM_DEL', 'Löschen');
CALL proc_i18n_rename(@locale, @pname, 'FRM_FILTER', 'Filter');
CALL proc_i18n_rename(@locale, @pname, 'UA_ID_USER', 'Benutzer');
CALL proc_i18n_rename(@locale, @pname, 'UA_KEY', 'Attribut');
CALL proc_i18n_rename(@locale, @pname, 'UA_VALUE', 'Wert');




















