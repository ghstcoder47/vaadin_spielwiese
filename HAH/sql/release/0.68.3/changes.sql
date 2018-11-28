

/* *****************************
** ./sql/feature/assignment_tool
** ****************************/

CALL proc_i18n_rebuild();
set @locale = 'de';
set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektzuweisung.Form.ProjektzuweisungDataView';
CALL proc_i18n_rename(@locale, @pname, 'FRM_DUPLICATE', 'Duplizieren');

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
** ./sql/feature/pdf-downloader-rework
** ****************************/

delete cust_taskmanager_meeting_topic_tasks from cust_taskmanager_meeting_topic_tasks
left join cust_taskmanager_tasks on TMETT_ID_TASK = TMT_ID
where TMT_ID IS NULL;

ALTER TABLE `cust_taskmanager_meeting_topic_tasks`
	ADD CONSTRAINT `FK_cust_taskmanager_meeting_topic_tasks_cust_taskmanager_tasks` FOREIGN KEY (`TMETT_ID_TASK`) REFERENCES `cust_taskmanager_tasks` (`TMT_ID`) ON UPDATE CASCADE ON DELETE CASCADE;


/* *****************************
** ./sql/feature/projectfilter
** ****************************/

