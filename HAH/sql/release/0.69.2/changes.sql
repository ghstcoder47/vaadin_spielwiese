

/* *****************************
** ./sql/feature/container_rework
** ****************************/



/* *****************************
** ./sql/feature/distance_analytics_tool
** ****************************/



/* *****************************
** ./sql/feature/expenses
** ****************************/



/* *****************************
** ./sql/feature/expenses_cockpit
** ****************************/

ALTER ALGORITHM = UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `view_cockpit` AS SELECT -- colnames
    NULL as project
    , NULL as entries
    , NULL as nurse
    , NULL as time_service
    , NULL as time_driven
    , NULL as distance
    , NULL as admin
    , NULl as unaccepted
    , NULL as date
  from _ where 0

  UNION ALL

	SELECT -- forsteo
		 1 
		 , SLF_ID 
		 , SLF_K_ID
		 , TIME_TO_SEC(SLF_BIS) - TIME_TO_SEC(SLF_VON) 
		 , TIME_TO_SEC(CONCAT(SLF_FAHRTZEIT_STD_N, ':', SLF_FAHRTZEIT_MIN_N))
		 , SLF_GES_KM_N
		 , IF(SLF_STATUS > 1, TIME_TO_SEC('00:10'), TIME_TO_SEC('00:00')) AS ADMINPAUSCHALE
		 , 0 
		 , IF(SLF_DATE > '1900-01-01', SLF_DATE, SLF_CREATED)
	FROM 
		cust_schulung_liste_forsteopen
   
  UNION ALL   

  SELECT -- pop
    VH_PROJEKT_ID 
    , ID 
    , ID_NURSE
    , TIME_TO_SEC(BIS) - TIME_TO_SEC(VON) 
    , TIME_TO_SEC(CONCAT(FAHRTZEIT_STD_N, ':', FAHRTZEIT_MIN_N))
    , GES_KM_N
    , IF(INFUSIONSTYP IN (1,2,3) , TIME_TO_SEC('00:15'), TIME_TO_SEC('00:00')) AS ADMINPAUSCHALE
    , IF(STATUS = 1, 1, NULL) 
    , DATE 
  FROM cust_protokoll_pop_daten
  INNER JOIN cust_protokoll_pop_meta_dynamic on ID_DELEGATION = PPOPMD_ID
  INNER JOIN cust_verk_haupt on PPOPMD_VH_ID = VH_ID 
  
  /*
  UNION ALL
  
  SELECT -- servicetasking
    VH_PROJEKT_ID 
    , ST_ID 
    , ST_ID_NURSE
    , TIME_TO_SEC(ST_BIS) - TIME_TO_SEC(ST_VON) 
    , TIME_TO_SEC(ST_FAHRTZEIT_NURSE)
    , ST_KM_NURSE
    , IF(ST_STATUS > 2, TIME_TO_SEC('00:15'), TIME_TO_SEC('00:00')) AS ADMINPAUSCHALE
    , IF(ST_STATUS = 1, 1, NULL) 
    , ST_DATE 
  FROM cust_protokoll_servicetasking
  INNER JOIN cust_protokoll_servicetasking_meta_dynamic on ST_ID_DELEGATION = STMD_ID
  INNER JOIN cust_verk_haupt on STMD_VH_ID = VH_ID 
  */
  
  UNION ALL
  
  SELECT -- Beauftragte Tätigkeiten
  	99999 AS 'VH_PROJEKT_ID'
  	, CED_ID
  	, KSK_ID
    , TIME_TO_SEC(CED_SERVICEZEIT) 
    , TIME_TO_SEC(CED_FAHRZEIT)
    , CED_KM
    , NULL
    , NULL
    , CED_DATE
   FROM
   	cust_expenses_data
   LEFT JOIN
   	cust_krankenschwester_stammdaten_ks ON CED_ID_USER = KSK_U_ID
   LEFT JOIN
   	cust_expenses_activity ON CED_ID_CEA = CEA_ID
   WHERE
   	CEA_COST = 0
;

/* *****************************
** ./sql/feature/nurse_changer
** ****************************/



/* *****************************
** ./sql/feature/projectfilter
** ****************************/

