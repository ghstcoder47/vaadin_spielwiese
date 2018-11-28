/* *****************************
** from develop
** ****************************/

CALL proc_i18n_rebuild();
set @locale = 'de';
set @pname = 'archenoah.lib.vaadin.Language.i18n.I18nManager';

CALL proc_i18n_rename(@locale, @pname, 'BOOL_FALSE', 'Nein');
CALL proc_i18n_rename(@locale, @pname, 'BOOL_TRUE', 'Ja');
CALL proc_i18n_rename(@locale, @pname, 'BOOL_UNKNOWN', 'Unbekannt');
CALL proc_i18n_rename(@locale, @pname, 'FILTER_CLEAR', 'Löschen');
CALL proc_i18n_rename(@locale, @pname, 'FILTER_EQ_PROMPT', '=');
CALL proc_i18n_rename(@locale, @pname, 'FILTER_FROM', 'Von');
CALL proc_i18n_rename(@locale, @pname, 'FILTER_GT_PROMPT', '>');
CALL proc_i18n_rename(@locale, @pname, 'FILTER_LT_PROMPT', '<');
CALL proc_i18n_rename(@locale, @pname, 'FILTER_SET', 'Ok');
CALL proc_i18n_rename(@locale, @pname, 'FILTER_TO', 'Bis');



-- --------------------------------------------------------
-- view sendable rework
-- --------------------------------------------------------

DROP VIEW IF EXISTS `view_protokoll_sendable`;
CREATE ALGORITHM=UNDEFINED DEFINER=`isconet-wartung`@`%` SQL SECURITY DEFINER VIEW `view_protokoll_sendable` AS select *, 	COUNT(*), SUM(COALESCE(checked, 0)), COUNT(*) = 	SUM(COALESCE(checked, 0)) as sendable from (

select 
	checked,
	YEAR(date) as yr,
	IF(pdf_freq = 'freq_monthly_6', IF(MONTH(date) <= 6, 1, 2), 0) as semi,
	IF(pdf_freq = 'freq_monthly_3', QUARTER(date), 0) as qrt,
	IF(pdf_freq = 'freq_monthly_1', MONTH(date), 0) as mth,
	arzt, firm_key, pdf_type, pdf_freq
from view_protokoll__overview

) as base

group by arzt, firm_key, pdf_type, pdf_freq, yr, semi, qrt, mth ;


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

