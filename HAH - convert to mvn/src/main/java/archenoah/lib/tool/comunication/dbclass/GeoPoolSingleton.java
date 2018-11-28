package archenoah.lib.tool.comunication.dbclass;

import java.sql.SQLException;

import archenoah.config.CMS_Config_Std;

public class GeoPoolSingleton {

    private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    
    public final static GeoPoolSingleton INSTANCE = new GeoPoolSingleton();

    private RefreshableConnectionPool connectionPool = null;

    private GeoPoolSingleton() {
    }

    public synchronized RefreshableConnectionPool getPool() {

        if (connectionPool == null) {
            CMS_Config_Std std = CMS_Config_Std.getInstanceXmlOnly();

            try {
                connectionPool = new RefreshableConnectionPool(std.dbDriver, std.dbProtocol + std.Standard_Server_URL + ":"
                        + std.Standard_Server_Port + "/" + "geodb" + std.dbParams, std.Standard_Server_Username, std.Standard_Server_Passwort, 1, 10);
            } catch (SQLException e) {
                log.error("error stablishing conneciton pool ", e);
                e.printStackTrace();
            }

        }
        
        return connectionPool;
        
    }

}
