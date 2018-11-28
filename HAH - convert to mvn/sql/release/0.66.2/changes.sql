/* *****************************
** from develop
** ****************************/



/* *****************************
** ./sql/feature/bugbash
** ****************************/

-- caption updater

DROP PROCEDURE IF EXISTS `proc_i18n_rename`;
DELIMITER //
CREATE PROCEDURE `proc_i18n_rename`(
	IN `locale` VARCHAR(2),
	IN `parent` VARCHAR(250),
	IN `identifier` VARCHAR(250),
	IN `caption` VARCHAR(250)
)
LANGUAGE SQL
NOT DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
BEGIN
	
	update cms_i18n
	set CAPTION = caption
	where
		cms_i18n.IDENTIFIER = identifier
		and cms_i18n.PARENT = (select ID from cms_i18n_components where `NAME` = parent)
		and cms_i18n.LOCALE = locale;
	
END//
DELIMITER ;

-- new captions taskmanager

set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.TaskSystem.TaskManager.form.TaskManagerInsertEdit';
set @locale = 'de';

CALL proc_i18n_rename(@locale, @pname, 'capaDescription', 'Ursache');
CALL proc_i18n_rename(@locale, @pname, 'capaSolution', 'Maßnahme');
CALL proc_i18n_rename(@locale, @pname, 'capaFinding', 'Finding');

CALL proc_i18n_rename(@locale, @pname, 'newComment', 'Kommentar schreiben');
