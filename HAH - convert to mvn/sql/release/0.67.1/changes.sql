/* *****************************
** from develop
** ****************************/

#         VIEW: view_prescription_list
#         VIEW: view_project_meta_delegations_sub
#         VIEW: view_project_meta_dynamic
#         VIEW: view_exportcs_active_homecare
#         VIEW: view_project_meta_delegations


ALTER VIEW view_project_meta_combined AS 
SELECT 
		-- colnames
           NULL as vh_id
            , NULL as patient_code
            , NULL as patient_id
            , NULL as rlead_id
            , NULL as firm_key
            , NULL as pdf_type
            , NULL as pdf_freq
            from _ where false

        UNION ALL

            SELECT -- replagal
            PREPMS_VH_ID, PREPMS_PAT_CODE, PREPMS_PAT_Initials,PREPMS_ID_REG_LEAD, NULL, PREPMS_PDF_TYPE, PREPMS_PDF_FREQ
            FROM cust_protokoll_replagal_meta_static

        UNION ALL

            SELECT -- vpriv
            PVMS_VH_ID, PVMS_PAT_CODE, PVMS_PAT_initials, PVMS_ID_REG_LEAD, NULL, PVMS_PDF_TYPE, PVMS_PDF_FREQ
            FROM cust_protokoll_vpriv_meta_static

        UNION ALL

            SELECT -- pop
            PPOPMS_VH_ID, PPOPMS_PAT_CODE, PPOPMS_PAT_initials ,PPOPMS_ID_REG_LEAD, PPOPMS_KEY_FIRM, PPOPMS_PDF_TYPE, PPOPMS_PDF_FREQ
            FROM cust_protokoll_pop_meta_static 
                    
			UNION ALL

            SELECT -- sa
            STMS_VH_ID, STMS_PAT_CODE, STMS_PAT_INITIALS ,STMS_ID_REG_LEAD, STMS_KEY_FIRM, NULL, NULL
            FROM cust_protokoll_servicetasking_meta_static ;


ALTER VIEW view_protokoll_count AS
select
      NULL as project
      , NULL as vh
      , NULL as `date`
      , NULL as `start`
      , NULL as `end`
      , NULL as travel
      , Null as KM
      , NULL as id
      , NULL as nurse
      , NULL as patient
      , NULL as arzt
      , NULL as type 
      , NULL as status
      , NULL as checked
    from _ where 0
    
union all -- FORSTEO
    select 
      1
      , NULL
      , IF(SLF_DATE > '1900-01-01', SLF_DATE, 
			IF(SLF_TERM > '1900-01-01', SLF_TERM, SLF_CREATED)
		)
		, STR_TO_DATE(SLF_VON,'%H:%i')
		, STR_TO_DATE(SLF_BIS,'%H:%i')
		, ADDTIME(SEC_TO_TIME(SLF_FAHRTZEIT_STD_N*60*60), SEC_TO_TIME(SLF_FAHRTZEIT_MIN_N*60))
		, SLF_GES_KM_N
      , SLF_ID
      , SLF_K_ID
      , SLF_P_ID
      , SLF_ARZT_ID
      , NULL
      , SLF_STATUS
      , NULL
    from cust_schulung_liste_forsteopen
    
union all -- REPLAGAL
    select
      2
      , PREPMD_VH_ID
      , PRD_DATE
      , STR_TO_DATE(PRD_VON,'%H:%i')
      , STR_TO_DATE(PRD_BIS,'%H:%i')
  		, ADDTIME(SEC_TO_TIME(PRD_FAHRTZEIT_STD_N*60*60), SEC_TO_TIME(PRD_FAHRTZEIT_MIN_N*60))
  		, PRD_GES_KM_N
      , PRD_ID
      , PRD_K_ID
      , PRD_P_ID
      , PRD_ARZT_ID
      , PRD_INFUSIONSTYP
      , PRD_STATUS
      , PRD_REGIONAL_MANAGER_OK
    from cust_protokoll_replagal_daten
    left join cust_protokoll_replagal_meta_dynamic on PRD_DELEGATION_ID = PREPMD_ID
    
union all -- VPRIV
    select
      3
      , PVMD_VH_ID
      , PVD_DATE
      , STR_TO_DATE(PVD_VON,'%H:%i')
      , STR_TO_DATE(PVD_BIS,'%H:%i')
  		, ADDTIME(SEC_TO_TIME(PVD_FAHRTZEIT_STD_N*60*60), SEC_TO_TIME(PVD_FAHRTZEIT_MIN_N*60))
  		, PVD_GES_KM_N
      , PVD_ID
      , PVD_K_ID
      , PVD_P_ID
      , PVD_ARZT_ID
      , PVD_INFUSIONSTYP
      , PVD_STATUS
      , PVD_REGIONAL_MANAGER_OK
    from cust_protokoll_vpriv_daten
    left join cust_protokoll_vpriv_meta_dynamic on PVD_DELEGATION_ID = PVMD_ID
    
union all -- POP
    select
      VH_PROJEKT_ID
      , PPOPMD_VH_ID
      , DATE
      , STR_TO_DATE(VON,'%H:%i')
      , STR_TO_DATE(BIS,'%H:%i')
  		, ADDTIME(SEC_TO_TIME(FAHRTZEIT_STD_N*60*60), SEC_TO_TIME(FAHRTZEIT_MIN_N*60))
  		, GES_KM_N
      , ID
      , ID_NURSE
      , ID_PATIENT
      , ARZT_ID
      , INFUSIONSTYP
      , STATUS
      , REGIONAL_MANAGER_OK
    from cust_protokoll_pop_daten
    left join cust_protokoll_pop_meta_dynamic on ID_DELEGATION = PPOPMD_ID
    left join cust_verk_haupt on PPOPMD_VH_ID = VH_ID 
    
union all -- SA
    select
      VH_PROJEKT_ID
      , ST_VH_ID
      , ST_DATE
      , STR_TO_DATE(ST_VON,'%H:%i')
      , STR_TO_DATE(ST_BIS,'%H:%i')
      , ST_FAHRTZEIT_NURSE
      , ST_KM_NURSE
      , ST_ID
      , ST_ID_NURSE
      , VH_PATIENT_ID
      , VH_ARZT_ID
      , NULL
      , ST_STATUS
      , NULL
    from cust_protokoll_servicetasking
    #left join cust_protokoll_servicetasking_meta_dynamic on ST_ID_DELEGATION = STMD_ID
    left join cust_verk_haupt on ST_VH_ID = VH_ID ;


ALTER VIEW view_protokoll__overview AS
select 
	view_protokoll_count.project
	, view_protokoll_count.`date`
	, view_protokoll_count.`start`
	, view_protokoll_count.`end`
	, view_protokoll_count.travel
	, view_protokoll_count.KM
	, view_protokoll_count.id
	, view_protokoll_count.nurse
	, view_protokoll_count.patient
	, view_protokoll_count.arzt
	, view_protokoll_count.checked
	, cust_stammdaten_regionalleiter.SRL_ID_USER
	, cust_patient_stammdaten_liste.PSL_GEB_DATUM as patientBirthday
	, cust_patient_stammdaten_liste.PSL_COUNTYCODE as patientCountry
	, view_project_meta_combined.patient_id as patient_code
	###
	, nurse.AUL_ID as nurse_user_id
	, cust_projekte_stammdaten_liste.PSLV_NAME as project_name
	, cust_projekte_stammdaten_liste.PSLV_CODE as project_code
	, cust_schulung_status.SS_NAME as status
   , view_protokoll_inf_types.token as inf_type
	, CONCAT(nurse.AUL_VORNAME,' ',nurse.AUL_NAME) as nurse_name
	, CONCAT(PSL_NAME,', ',PSL_VORNAME) as patient_name
   , CONCAT(arzt.AUL_NAME,', ',arzt.AUL_VORNAME) as arzt_name
   , CONCAT(reglead.AUL_VORNAME,' ',reglead.AUL_NAME) as reglead_name
   , view_project_meta_combined.firm_key
   , view_project_meta_combined.pdf_type
   , view_project_meta_combined.pdf_freq
from
	view_protokoll_count
	left join view_protokoll_inf_types
		on view_protokoll_count.`type` = view_protokoll_inf_types.id
		and  view_protokoll_count.project = view_protokoll_inf_types.pid
	left join view_project_meta_combined 
		on view_protokoll_count.vh = view_project_meta_combined.vh_id
	left join cust_stammdaten_regionalleiter 
		on view_project_meta_combined.rlead_id = cust_stammdaten_regionalleiter.SRL_ID
		
	-- project
	left join cust_projekte_stammdaten_liste on view_protokoll_count.project = cust_projekte_stammdaten_liste.PSLV_ID
	-- status
	left join cust_schulung_status
		on view_protokoll_count.`status` = cust_schulung_status.SS_PROZESS and cust_projekte_stammdaten_liste.PSLV_CODE = SS_CODE
	-- nurse
	left join cust_krankenschwester_stammdaten_ks on view_protokoll_count.nurse = cust_krankenschwester_stammdaten_ks.KSK_ID
	left join cms_auth_stammdaten_user as nurse on cust_krankenschwester_stammdaten_ks.KSK_U_ID = nurse.AUL_ID
	-- patient
	left join cust_patient_stammdaten_liste on view_protokoll_count.patient = cust_patient_stammdaten_liste.PSL_ID
	-- arzt
	left join cust_arzt_stammdaten_arzt on view_protokoll_count.arzt = cust_arzt_stammdaten_arzt.ASA_ID
	left join cms_auth_stammdaten_user as arzt on cust_arzt_stammdaten_arzt.ASA_U_ID = arzt.AUL_ID
	-- reg lead
	left join cms_auth_stammdaten_user as reglead on cust_stammdaten_regionalleiter.SRL_ID_USER = reglead.AUL_ID ;


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Exportiere Struktur von View hah_firm.view_prescription_list
-- Erstelle temporäre Tabelle um View Abhängigkeiten zuvorzukommen
CREATE TABLE `view_prescription_list` (
	`PL_ID` INT(11) NOT NULL,
	`PL_CREATED` TIMESTAMP NOT NULL,
	`PL_UPDATED` TIMESTAMP NOT NULL,
	`PL_ID_PROJECT` INT(11) NOT NULL,
	`PL_ID_USER` INT(11) NOT NULL,
	`PL_ID_PATIENT` INT(11) NOT NULL,
	`PL_ID_NURSE` INT(11) NULL,
	`PL_NR` VARCHAR(1024) NULL COLLATE 'utf8_bin',
	`PL_DATE` DATE NOT NULL,
	`PL_BOOKING` DATE NULL,
	`PL_STATUS` VARCHAR(50) NULL COLLATE 'utf8_bin',
	`PL_FIRM` VARCHAR(50) NULL COLLATE 'utf8_bin',
	`PL_NO_FEE` TINYINT(1) NULL,
	`PL_COMMENT` TEXT NULL COLLATE 'utf8_bin',
	`PL_FILED` TINYINT(1) NULL,
	`PL_FILED_DATE` DATE NULL
) ENGINE=MyISAM;

-- Exportiere Struktur von View hah_firm.view_project_meta_delegations
-- Erstelle temporäre Tabelle um View Abhängigkeiten zuvorzukommen
CREATE TABLE `view_project_meta_delegations` (
	`vh_id` INT(11) NULL,
	`firm_key` VARCHAR(50) NULL COLLATE 'utf8_bin',
	`project_id` INT(100) NULL,
	`patient_id` INT(100) NULL,
	`delegation` DATE NULL,
	`delegation_end` DATE NULL
) ENGINE=MyISAM;

-- Exportiere Struktur von View hah_firm.view_project_meta_delegations_sub
-- Erstelle temporäre Tabelle um View Abhängigkeiten zuvorzukommen
CREATE TABLE `view_project_meta_delegations_sub` (
	`vh_id` INT(11) NULL,
	`firm_key` VARCHAR(50) NULL COLLATE 'utf8_bin',
	`project_id` INT(100) NULL,
	`patient_id` INT(100) NULL,
	`delegation` DATE NULL
) ENGINE=MyISAM;

-- Exportiere Struktur von View hah_firm.view_project_meta_dynamic
-- Erstelle temporäre Tabelle um View Abhängigkeiten zuvorzukommen
CREATE TABLE `view_project_meta_dynamic` (
	`vh_id` INT(11) NULL,
	`dat` DATE NULL
) ENGINE=MyISAM;


-- Exportiere Struktur von View hah_firm.view_prescription_list
-- Entferne temporäre Tabelle und erstelle die eigentliche View
DROP TABLE IF EXISTS `view_prescription_list`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `view_prescription_list` AS select
	PL_ID
	, PL_CREATED
	, PL_UPDATED
	, PL_ID_PROJECT
	, PL_ID_USER
	, PL_ID_PATIENT
	, PL_ID_NURSE
	, PL_NR
	, PL_DATE
	, PL_BOOKING
	, PL_STATUS
	, IF(PL_FIRM IS NULL, firm_key, PL_FIRM) as PL_FIRM
	, PL_NO_FEE
	, PL_COMMENT
	, PL_FILED
	, PL_FILED_DATE
from cust_prescriptions_list
left join view_project_meta_delegations
	on PL_ID_PATIENT = patient_id
	and PL_ID_PROJECT = project_id
	and PL_DATE >= delegation
	and (PL_DATE < delegation_end or delegation_end IS NULL) ;

-- Exportiere Struktur von View hah_firm.view_project_meta_delegations
-- Entferne temporäre Tabelle und erstelle die eigentliche View
DROP TABLE IF EXISTS `view_project_meta_delegations`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `view_project_meta_delegations` AS select r.*, 
	     (SELECT delegation FROM view_project_meta_delegations_sub r2
        WHERE r2.delegation > r.delegation and r2.patient_id = r.patient_id and r2.project_id = r.project_id
        ORDER BY r2.delegation ASC LIMIT 1) as delegation_end
from view_project_meta_delegations_sub as r ;

-- Exportiere Struktur von View hah_firm.view_project_meta_delegations_sub
-- Entferne temporäre Tabelle und erstelle die eigentliche View
DROP TABLE IF EXISTS `view_project_meta_delegations_sub`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `view_project_meta_delegations_sub` AS select c.VH_ID as vh_id, firm_key, VH_PROJEKT_ID as project_id, VH_PATIENT_ID as patient_id, dat as delegation from
view_project_meta_combined as c
left join cust_verk_haupt as vk on c.vh_id = vk.VH_ID
left join view_project_meta_dynamic as d on d.vh_id = c.vh_id ;

-- Exportiere Struktur von View hah_firm.view_project_meta_dynamic
-- Entferne temporäre Tabelle und erstelle die eigentliche View
DROP TABLE IF EXISTS `view_project_meta_dynamic`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `view_project_meta_dynamic` AS select
	NULL as vh_id
	, NULL as dat
	from _ where 0

union 

 	select PVMD_VH_ID, PVMD_DELEGATION
	from cust_protokoll_vpriv_meta_dynamic

union 

 	select PREPMD_VH_ID, PREPMD_DELEGATION
	from cust_protokoll_replagal_meta_dynamic

union 

 	select PPOPMD_VH_ID, PPOPMD_DELEGATION
	from cust_protokoll_pop_meta_dynamic

union

	select STMD_VH_ID, STMD_RRULE_START
	from cust_protokoll_servicetasking_meta_dynamic ;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;



set @locale = 'de';

set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.servicetasking.classes.ServiceTaskingNew';
CALL proc_i18n_rename(@locale, @pname, 'VH_ID', 'ID');
CALL proc_i18n_rename(@locale, @pname, 'nurse', 'Nurse');
CALL proc_i18n_rename(@locale, @pname, 'patient', 'Patient');
CALL proc_i18n_rename(@locale, @pname, 'project', 'Projekt');

set @pname = 'archenoah.lib.vaadin.CustomConverterFactorys.IntegerToBooleanConverter';
CALL proc_i18n_rename(@locale, @pname, 'bool_true', 'ja');
CALL proc_i18n_rename(@locale, @pname, 'bool_false', 'nein');

/* *****************************
** ./sql/feature/forsteo_additional_fields
** ****************************/

ALTER TABLE `cust_schulung_liste_forsteopen`
	ADD COLUMN `SLF_PRODUCT_USED` INT(1) NULL DEFAULT NULL AFTER `SLF_NURSE_CONCERNS`,
	ADD COLUMN `SLF_UE_REPORT_EXIST` INT(1) NULL DEFAULT NULL AFTER `SLF_PRODUCT_USED`,
	ADD COLUMN `SLF_CASENUMBER` VARCHAR(30) NULL DEFAULT NULL AFTER `SLF_UE_REPORT_EXIST`;
	
UPDATE cust_schulung_liste_forsteopen
SET
SLF_UPDATED = SLF_UPDATED,
SLF_PRODUCT_USED = -1,
SLF_UE_REPORT_EXIST = -1;

set @locale = 'de';

set @pname = 'archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.forsteo.Form.form_forsteo_insert_edit';
CALL proc_i18n_rename(@locale, @pname, 'yes', 'Ja');
CALL proc_i18n_rename(@locale, @pname, 'no', 'Nein');
CALL proc_i18n_rename(@locale, @pname, 'unknown', 'Unbekannt');

/* *****************************
** ./sql/feature/ip_check_overflow
** ****************************/



/* *****************************
** ./sql/feature/quickselect
** ****************************/

