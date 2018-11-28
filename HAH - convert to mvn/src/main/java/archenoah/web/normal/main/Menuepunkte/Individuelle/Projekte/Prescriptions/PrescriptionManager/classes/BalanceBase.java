package archenoah.web.normal.main.Menuepunkte.Individuelle.Projekte.Prescriptions.PrescriptionManager.classes;

import archenoah.config.CMS_Config_Std;

abstract public class BalanceBase {
	
	protected Boolean debug = false;
	
	protected String patientId;
	protected CMS_Config_Std std;
	
	public static final String PRESCRIPTION = "1";
	public static final String LOGISTICS = "2";
	public static final String INFUSION = "3";
	public static final String MANUAL = "4";
	
	public BalanceBase(){
		std = CMS_Config_Std.getInstance();
	}

	public void setDebug(Boolean debug) {
		this.debug = debug;
	}
	
	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId.toString();
	}
	
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	
	abstract public Integer getBalance(Integer productId) throws IllegalStateException;
	
	abstract public Integer editBalance(Integer productId, Integer delta, String action) throws IllegalStateException;
	
	/**
	 * 
	 * @param productId
	 * @param delta (postitive Integer)
	 * @return Inserted Id on success, 0 otherwise
	 * @throws IllegalArgumentException
	 */
	
	public Integer addBalance(Integer productId, Integer delta, String action) throws NumberFormatException {
		if(delta <= 0){
			throw new NumberFormatException ("delta cannot be negative");
		}
		
		return editBalance(productId, delta, action);
	}
	
	/**
	 * 
	 * @param productId
	 * @param delta (postitive Integer)
	 * @return Inserted Id on success, 0 otherwise
	 * @throws IllegalArgumentException
	 */

	public Integer removeBalance(Integer productId, Integer delta, String action) throws NumberFormatException {
		if(delta <= 0){
			throw new NumberFormatException ("delta cannot be negative");
		}
		
		return editBalance(productId, delta * -1, action);
	}
}
