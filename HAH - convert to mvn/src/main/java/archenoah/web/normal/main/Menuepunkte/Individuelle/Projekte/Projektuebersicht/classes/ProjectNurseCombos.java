package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Projektuebersicht.classes;

import java.util.Collection;

import archenoah.config.CMS_Config_Std;
import archenoah.lib.tool.comunication.dbclass.DBClass;

import com.google.common.base.Joiner;
import com.vaadin.data.Container;

public class ProjectNurseCombos {

	private String patientId = null;
	private Boolean getAllFields = false;
	private String projectId = null;
    private Collection<Integer> projectIds = null;
	
	public ProjectNurseCombos(Collection<Integer> projectIds) {
	    this.projectIds  = projectIds;
	}
	
	public ProjectNurseCombos(Integer projectId){
	    this.projectId = Integer.toString(projectId);
	}
	
	public ProjectNurseCombos(){

    }
	
	public Boolean getGetAllFields() {
		return getAllFields;
	}

	public void setGetAllFields(Boolean getAllFields) {
		this.getAllFields = getAllFields;
	}

	public void setPatientId(Integer patientId){
		if(patientId != null){
			this.patientId = Integer.toString(patientId);
		}
	}
	
	public void setPatientId(String patientId){
		if(patientId != null){
			this.patientId = patientId;
		}
	}
	
	public String getPatientId(){
		return patientId;
	}
	
    public Container getNurseComboMain() {

        CMS_Config_Std std = CMS_Config_Std.getInstance();

        DBClass db = new DBClass();
        
        if(getAllFields){
        	db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("*");
        }else{
        	db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("KSK_ID");
        }
        
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("CONCAT('*', KSK_TITEL,' ',KSA_NAME,' ',AUL_NAME,' ',AUL_VORNAME)", "Krankenschwesternname");

        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cms_auth_stammdaten_user", "INNER JOIN", "cust_krankenschwester_stammdaten_anrede",
                "AUL_ANREDE_ID", "KSA_ID");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_krankenschwester_stammdaten_ks", "INNER JOIN", "cms_auth_stammdaten_user", "KSK_U_ID",
                "AUL_ID");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_verk_haupt", "INNER JOIN", "cust_krankenschwester_stammdaten_ks", "VH_NURSE_ID", "KSK_ID");

        if (patientId != null) {
            db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cust_verk_haupt", "VH_PATIENT_ID", "=", patientId, "AND");
            if(projectId != null) {
                db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cust_verk_haupt", "VH_PROJEKT_ID", "=", projectId, "");
            }else if(projectIds != null) {
                db.DB_Data_Get.DB_Filter.DB_WHERE_In("cust_verk_haupt", "VH_PROJEKT_ID", Joiner.on(",").join(projectIds), "");
            }
        }

        db.DB_Data_Get.DB_Ordnen.DB_Ordnen("AUL_NAME", "ASC");

        return db.DB_Data_Get.DB_SEND_AND_GET_Container();

    }

    public Container getNurseComboSubs() {

        CMS_Config_Std std = CMS_Config_Std.getInstance();

        DBClass db = new DBClass();
        
        if(getAllFields){
        	db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("*");
        }else{
        	db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("KSK_ID");
        }
        
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("CONCAT(KSK_TITEL,' ',KSA_NAME,' ',AUL_NAME,' ',AUL_VORNAME)", "Krankenschwesternname");

        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cms_auth_stammdaten_user", "INNER JOIN", "cust_krankenschwester_stammdaten_anrede",
                "AUL_ANREDE_ID", "KSA_ID");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_krankenschwester_stammdaten_ks", "INNER JOIN", "cms_auth_stammdaten_user", "KSK_U_ID",
                "AUL_ID");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_verk_subs", "INNER JOIN", "cust_krankenschwester_stammdaten_ks", "CVS_NURSE_ID", "KSK_ID");

        if (patientId != null) {
            db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cust_verk_subs", "CVS_PATIENT_ID", "=", patientId, "AND");
            if(projectId != null) {
                db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cust_verk_subs", "CVS_PROJECT_ID", "=", projectId, "");
            }else if(projectIds != null) {
                db.DB_Data_Get.DB_Filter.DB_WHERE_In("cust_verk_subs", "CVS_PROJECT_ID",  Joiner.on(",").join(projectIds), "");
            }
        }

        db.DB_Data_Get.DB_Ordnen.DB_Ordnen("AUL_NAME", "ASC");
        
        return db.DB_Data_Get.DB_SEND_AND_GET_Container();

    }
	
    public Container getNurseComboAll(){
    	return getNurseComboSingle(null);
    }
    
    public Container getNurseComboSingle(Integer nid) {

        CMS_Config_Std std = CMS_Config_Std.getInstance();

        DBClass db = new DBClass();
        
        if(getAllFields){
        	db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("*");
        }else{
        	db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("KSK_ID");
        }
        
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("CONCAT(KSK_TITEL,' ',KSA_NAME,' ',AUL_NAME,' ',AUL_VORNAME)", "Krankenschwesternname");
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("CONCAT(AUL_VORNAME, ' ', AUL_NAME)", "nurse");

        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cms_auth_stammdaten_user", "INNER JOIN", "cust_krankenschwester_stammdaten_anrede", "AUL_ANREDE_ID", "KSA_ID");
        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Verknüpfung("cust_krankenschwester_stammdaten_ks", "INNER JOIN", "cms_auth_stammdaten_user", "KSK_U_ID", "AUL_ID");
        
        if(nid != null){
        	db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cust_krankenschwester_stammdaten_ks", "KSK_ID", "=", Integer.toString(nid), "");
        }

        db.DB_Data_Get.DB_Ordnen.DB_Ordnen("AUL_NAME", "ASC");

        return db.DB_Data_Get.DB_SEND_AND_GET_Container();

    }
    
}
