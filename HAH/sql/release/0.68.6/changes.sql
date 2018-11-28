/* *****************************
** from develop
** ****************************/

ALTER TABLE `cust_protokoll_pop_daten`
  CHANGE COLUMN `INFUSIONSREAKTION_ZUSAMMENHANG` `INFUSION_REACTION_CAUSED_BY` INT(1) NULL DEFAULT NULL,
	ADD COLUMN `INFUSION_REACTION_CORRELATION` INT(11) NULL DEFAULT NULL;
  

  
update cust_protokoll_replagal_daten_archive
left join cust_protokoll_pop_daten on PRD_ID = ID
	SET 
		INFUSION_REACTION_CAUSED_BY = PRD_INFUSIONSREAKTION_ZUSAMMENHANG,
		INFUSION_REACTION_CORRELATION = PRD_INFUSIONSREAKTION_ZUSAMMENHANG_BEWERTUNG;
    
update cust_protokoll_vpriv_daten_archive
left join cust_protokoll_pop_daten on PVD_ID = ID
	SET 
		INFUSION_REACTION_CAUSED_BY = PVD_INFUSIONSREAKTION_ZUSAMMENHANG,
		INFUSION_REACTION_CORRELATION = PVD_INFUSIONSREAKTION_ZUSAMMENHANG_BEWERTUNG;
    
delete from cms_i18n where parent = 45;    
    
CALL proc_i18n_rebuild();
set @locale = 'de';
set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.pop.form.PoPInsertEdit';
CALL proc_i18n_rename(@locale, @pname, 'causedBy', 'Kausaler Zusammenhang zu Produkt');
CALL proc_i18n_rename(@locale, @pname, 'correlation', 'Kausalitätsbewertung');

set @pname = 'archenoah.lib.vaadin.Language.i18n.I18nManager';
CALL proc_i18n_rename(@locale, @pname, 'ntf:incomplete_save.title', 'Speicherfehler in Mehrfachdaten');
CALL proc_i18n_rename(@locale, @pname, 'ntf:incomplete_save.content', 'Nicht alle Inhalte wurden korrekt eingegeben, bitte prüfen!');

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

