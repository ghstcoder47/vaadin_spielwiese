package archenoah.lib.tool.comunication.dbclass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import archenoah.config.CMS_Config_Std;
import archenoah.lib.tool.debug.ExecutionTimeLogger;
import archenoah.lib.tool.java_plugin.stringmanager.classes.StringManager;
import archenoah.lib.vaadin.Components.table.Enum_Generator;

// import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
// import com.vaadin.addon.sqlcontainer.SQLContainer;
// import com.vaadin.addon.sqlcontainer.connection.JDBCConnectionPool;
// import com.vaadin.addon.sqlcontainer.connection.SimpleJDBCConnectionPool;
// import com.vaadin.addon.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;

/**************************************************************************************************************************************
 * Isconet Datenbank Klasse Version 0.1 (C) * * Author: Asan Kaceri * * Stand :
 * 25.09.2012 * *
 * ************************************************************************************************************************************/

public class DBClass {
    
    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    
    /*******************************************************************************************
     * * Public Objekte * *
     * *****************************************************************************************/
    
    protected JDBCConnectionPool pool;
    
    public Database_Get DB_Data_Get;
    public Database_Update DB_Data_Update;
    public Database_Insert DB_Data_Insert;
    public Database_Delete DB_Data_Delete;

    public Database_Advanced DB_Data_Advanced;

    public CustomQuery CustomQuery;
    
    private static Integer retries = 100;
    
    /*******************************************************************************************
     * * Private Variablen * *
     * *****************************************************************************************/

    private final String SQL_Config_Treiber = "com.mysql.jdbc.Driver";
    private final String SQL_Config_URL_Driver = "jdbc:mysql://";

    private Boolean debugNextQuery = false;
    private Boolean skipNextQuery = false;

    // ************************************* Init Klasse ************************************************


    public DBClass(){
    	
        ConnectionPoolSingleton poolSingleton = ConnectionPoolSingleton.INSTANCE;
       
        init(poolSingleton.getPool());
        
    }
    
    public DBClass(JDBCConnectionPool pool) {
        init(pool);
    }
    
    private void init(JDBCConnectionPool pool) {
        
        if(pool == null) {
            log.warn("pool is null");
            return;
        }
        
        this.pool = pool;
        
        DB_Data_Get = new Database_Get();
        CustomQuery = new CustomQuery(this);
        DB_Data_Update = new Database_Update();
        DB_Data_Insert = new Database_Insert();
        DB_Data_Delete = new Database_Delete();
    }
    
    /***************************************************************************************************
     * 
     * DB Config Funktionen
     * 
     ************************************************************************************************* */

    /***************************************************************************************************
     * 
     * DB Klass Functionen
     * 
     ************************************************************************************************* */
    public void DB_Data_Leeren() {

        // Objekte
        DB_Data_Get = new Database_Get();
        DB_Data_Update = new Database_Update();
        CustomQuery = new CustomQuery(this);
        DB_Data_Insert = new Database_Insert();
        DB_Data_Delete = new Database_Delete();
        
    }
    
    protected Connection conn() throws SQLException {
        Connection conn = pool.reserveConnection();
        conn.setAutoCommit(true);
        return conn;
    }
    
    protected void release(Connection conn) {
        pool.releaseConnection(conn);
    }
    
    public void debugNextQuery(Boolean debug) {
        debugNextQuery = debug;
    }

    public void debugNextQuery(Boolean debug, Boolean skip) {
        debugNextQuery(debug);
        skipNextQuery = skip;
    }

    /***************************************************************************************************
     * 
     * Get Objekte
     * 
     ************************************************************************************************* */
    public class Database_Get {
        // Objekte
        public Data_DB_Tabellen DB_Tabellen;
        public Data_DB_Spalten DB_Spalten;
        public Data_DB_Filter DB_Filter;
        public Data_DB_Gruppieren DB_Gruppieren;
        public Data_DB_Ordnen DB_Ordnen;
        public Data_DB_Limit DB_Limit;

        // Private

        private String SQL_String = "";

        private SQLContainer container;
        private Connection conaufb;

        public Map<String, Object> Typ_list = null;

        public Database_Get() {
            DB_Tabellen = new Data_DB_Tabellen();
            DB_Spalten = new Data_DB_Spalten();
            DB_Filter = new Data_DB_Filter();
            DB_Gruppieren = new Data_DB_Gruppieren();
            DB_Ordnen = new Data_DB_Ordnen();
            DB_Limit = new Data_DB_Limit();
            DB_Data_Advanced = new Database_Advanced();

            Typ_list = new HashMap<>();

        }

        public void Set_Type(String Spaltenname, Object Typ) {
            Typ_list.put(Spaltenname, Typ);

        }

        @Deprecated
        public void DB_DEBUG_SQL_STRING() {

            String tbl = "";
            if (this.DB_Tabellen.CustomSQL != "") {
                tbl = this.DB_Tabellen.CustomSQL;
            } else {
                tbl = this.DB_Tabellen.func_SQL_Abfrage();
            }
            String SP = this.DB_Spalten.func_SQL_Spalten();
            String Filter = this.DB_Filter.func_SQL_Where();
            String GRP = this.DB_Gruppieren.func_Gruppieren();
            String Ordnen = this.DB_Ordnen.func_Ordnen();
            String Limit = this.DB_Limit.func_LIMIT();

            System.out.println("******************** DEBUG SQL GET *****************************************\n\n");

            System.out.println("SELECT " + SP + " FROM " + tbl + " " + Filter + " " + GRP + " " + Ordnen + " " + Limit + " \n\n");

            System.out.println("******************** DEBUG ENDE *****************************************\n\n");

        }

        public void connclose() {
//            // cona.destroy(); //persistentPooling!
        }
        
        public String getSQL(){
            
            String tbl = this.DB_Tabellen.func_SQL_Abfrage();
            String SP = this.DB_Spalten.func_SQL_Spalten();
            String Filter = this.DB_Filter.func_SQL_Where();
            String GRP = this.DB_Gruppieren.func_Gruppieren();
            String Ordnen = this.DB_Ordnen.func_Ordnen();
            String Limit = this.DB_Limit.func_LIMIT();
            
            return "SELECT " + SP + " FROM " + tbl + " " + Filter + " " + GRP + " " + Ordnen + " " + Limit;
        }
        
        public Container DB_SEND_AND_GET_Container() {
            String tbl = this.DB_Tabellen.func_SQL_Abfrage();
            String SP = this.DB_Spalten.func_SQL_Spalten();
            String Filter = this.DB_Filter.func_SQL_Where();
            String GRP = this.DB_Gruppieren.func_Gruppieren();
            String Ordnen = this.DB_Ordnen.func_Ordnen();
            String Limit = this.DB_Limit.func_LIMIT();
            
            ExecutionTimeLogger exDb = new ExecutionTimeLogger("mysql query");
            ExecutionTimeLogger exCon = new ExecutionTimeLogger("container builder");
            
            Boolean debugTiming = debugNextQuery;
            
            if(debugTiming){
                exDb.start();
            }
            
            try {

                if (SQL_String == "") {

                    
                    if (debugNextQuery) {
                        System.out.println("## Debugging Select Query");

                        System.out.println("  " + "SELECT " + SP + " FROM " + tbl + " " + Filter + " " + GRP + " " + Ordnen + " " + Limit);
                        debugNextQuery = false;
                    }

                    if (skipNextQuery) {
                        System.out.println("## Skipping Select Query");
                        skipNextQuery = false;
                        return null;
                    }
                    
                    container = new SQLContainer(new FreeformQuery("SELECT " + SP + " FROM " + tbl + " " + Filter + " " + GRP + " " + Ordnen + " " + Limit,
                            pool));
                    

                    
                } else {
                    
                    if (debugNextQuery) {
                        System.out.println("## Debugging Select Query");

                        System.out.println("  " + this.SQL_String + " SELECT " + SP + " FROM " + tbl + " " + Filter + " " + GRP + " "
                                + Ordnen + " " + Limit);
                        debugNextQuery = false;
                    }

                    if (skipNextQuery) {
                        System.out.println("## Skipping Select Query");
                        skipNextQuery = false;
                        return null;
                    }
                    
                    container = new SQLContainer(new FreeformQuery(this.SQL_String + " SELECT " + SP + " FROM " + tbl + " " + Filter + " " + GRP + " "
                            + Ordnen + " " + Limit, pool));

                }

                if(debugTiming){
                    exDb.stop();
                    exCon.start();
                }
                
                
                Container c = new IndexedContainer();

                if (container.size() == 0) {

                    connclose();

                    if(debugTiming){
                        exCon.stop();
                    }
                    return c;

                }

                String Spalten = container.getContainerPropertyIds().toString();

                StringManager sm = new StringManager();

                Spalten = sm.Mid(Spalten, 1, Spalten.length() - 1);
                Spalten = Spalten.replace(", ", ",");

                String[] arr;
                arr = Spalten.split(",");

                Enum_Generator eg = new Enum_Generator();

                for (int i = 0; i < arr.length; i++) {
                    // System.out.println( container.getType(arr[i]));
                    if (Typ_list == null) {
                        c.addContainerProperty(arr[i], container.getType(arr[i]), null);
                    } else {

                        Set<String> keys = Typ_list.keySet();

                        boolean gefunden = false;

                        Object typus = null;

                        for (Object singleKey : keys) {

                            if (arr[i].equals(singleKey) == true) {
                                gefunden = true;
                                typus = singleKey;
                            }

                        }

                        if (gefunden == true) {

                            c.addContainerProperty(arr[i], (Class<?>) Typ_list.get(typus), null);

                        } else {

                            c.addContainerProperty(arr[i], container.getType(arr[i]), null);
                        }

                    }

                }

                Object itemId;
                Item item;

                for (Object ItemIda : container.getItemIds()) {
                    // UserId = (int) result.getItem(ItemIda).getItemProperty(this.Spalte_ID)
                    // .getValue();

                    itemId = c.addItem();

                    for (int d = 0; d < arr.length; d++) {
                        item = c.getItem(itemId);
                        // System.out.println(item.getItemProperty(arr[d]).getType());
                        Property prop = item.getItemProperty(arr[d]);

                        switch (item.getItemProperty(arr[d]).getType().toString()) {

                        case "class java.util.Date":

                            String dateString = (String) container.getItem(ItemIda).getItemProperty(arr[d]).getValue();
                            Date convertedDate = null;
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy HH:MM");
                            try {
                                convertedDate = dateFormat.parse(dateString);
                            } catch (ParseException e) {
                                // TODO Automatisch generierter Erfassungsblock
                                e.printStackTrace();
                            }
                            prop.setValue(convertedDate);
                            break;

                        default:

                            Class ctemp = item.getItemProperty(arr[d]).getType();
                            if (ctemp.isEnum() == true) {
                            	
                                if ((container.getItem(ItemIda).getItemProperty(arr[d]).getValue() != null)
                                		&& eg.is_enumvorhanden(ctemp, container.getItem(ItemIda).getItemProperty(arr[d]).getValue() + "") == false) {
                                    eg.addEnum(ctemp, container.getItem(ItemIda).getItemProperty(arr[d]).getValue() + "", ctemp.getDeclaredFields());
                                }

                                prop.setValue(eg.Get_enum_dat(ctemp, container.getItem(ItemIda).getItemProperty(arr[d]).getValue() + ""));

                            } else {
                                Property p = null;
                                if(container != null && container.getItem(ItemIda) != null) {
                                    p = container.getItem(ItemIda).getItemProperty(arr[d]);
                                }
                                prop.setValue(p == null ? null : p.getValue());
                            }

                        }

                    }
                }
                connclose();
                
                if(debugTiming){
                    exCon.stop();
                }
                return c;

            } catch (SQLException e) {
                // TODO Automatisch generierter Erfassungsblock
                e.printStackTrace();
                connclose();
                
                if(debugTiming){
                    exCon.stop();
                }
                return null;
            }

        }

        public SQLContainer DB_SEND_AND_GET() {
            String tbl = "";
            if (this.DB_Tabellen.CustomSQL != "") {
                tbl = this.DB_Tabellen.CustomSQL;
            } else {
                tbl = this.DB_Tabellen.func_SQL_Abfrage();
            }

            String SP = this.DB_Spalten.func_SQL_Spalten();
            String Filter = this.DB_Filter.func_SQL_Where();
            String GRP = this.DB_Gruppieren.func_Gruppieren();
            String Ordnen = this.DB_Ordnen.func_Ordnen();
            String Limit = this.DB_Limit.func_LIMIT();

            try {

                if (SQL_String == "") {
                    
                    if (debugNextQuery) {
                        System.out.println("## Debugging Select Query");

                        System.out.println("  " + "SELECT " + SP + " FROM " + tbl + " " + Filter + " " + GRP + " " + Ordnen + " " + Limit);
                        debugNextQuery = false;
                    }

                    if (skipNextQuery) {
                        System.out.println("## Skipping Select Query");
                        skipNextQuery = false;
                        return null;
                    }
                    
                    container = new SQLContainer(new FreeformQuery("SELECT " + SP + " FROM " + tbl + " " + Filter + " " + GRP + " " + Ordnen + " " + Limit,
                            pool));
                    // // cona.destroy(); //persistentPooling!
                    return container;
                } else {
                    
                    if (debugNextQuery) {
                        System.out.println("## Debugging Select Query");

                        System.out.println("  " + this.SQL_String + " SELECT " + SP + " FROM " + tbl + " " + Filter + " " + GRP + " "
                                + Ordnen + " " + Limit);
                        debugNextQuery = false;
                    }

                    if (skipNextQuery) {
                        System.out.println("## Skipping Select Query");
                        skipNextQuery = false;
                        return null;
                    }
                    
                    container = new SQLContainer(new FreeformQuery(this.SQL_String + " SELECT " + SP + " FROM " + tbl + " " + Filter + " " + GRP + " "
                            + Ordnen + " " + Limit, pool));
                    // // cona.destroy(); //persistentPooling!
                    return container;
                }

            } catch (SQLException e) {
                // TODO Automatisch generierter Erfassungsblock
                e.printStackTrace();
                // cona.destroy(); //persistentPooling!
                return null;
            }

        }

        public Item DB_SEND_AND_GET_FIRST_ITEM() {
            SQLContainer con = DB_SEND_AND_GET();
            if (con != null && con.size() > 0) {
                Item item = con.getItem(con.getItemIds().toArray()[0]);
                return item;
            } else {
                return null;
            }
        }

        public void DB_Data_Leeren() {
            DB_Tabellen = new Data_DB_Tabellen();
            DB_Spalten = new Data_DB_Spalten();
            DB_Filter = new Data_DB_Filter();
            DB_Gruppieren = new Data_DB_Gruppieren();
            DB_Ordnen = new Data_DB_Ordnen();
            DB_Limit = new Data_DB_Limit();

            SQL_String = "";
        }

        public void DB_Union() {
            String tbl = this.DB_Tabellen.func_SQL_Abfrage();
            String SP = this.DB_Spalten.func_SQL_Spalten();
            String Filter = this.DB_Filter.func_SQL_Where();
            String GRP = this.DB_Gruppieren.func_Gruppieren();
            String Ordnen = this.DB_Ordnen.func_Ordnen();
            String Limit = this.DB_Limit.func_LIMIT();

            SQL_String = SQL_String + "SELECT " + SP + " FROM " + tbl + Filter + GRP + Ordnen + Limit + "UNION ";

            DB_Tabellen = new Data_DB_Tabellen();
            DB_Spalten = new Data_DB_Spalten();
            DB_Filter = new Data_DB_Filter();
            DB_Gruppieren = new Data_DB_Gruppieren();
            DB_Ordnen = new Data_DB_Ordnen();
            DB_Limit = new Data_DB_Limit();

        }

        public void DB_Union_All() {
            String tbl = this.DB_Tabellen.func_SQL_Abfrage();
            String SP = this.DB_Spalten.func_SQL_Spalten();
            String Filter = this.DB_Filter.func_SQL_Where();
            String GRP = this.DB_Gruppieren.func_Gruppieren();
            String Ordnen = this.DB_Ordnen.func_Ordnen();
            String Limit = this.DB_Limit.func_LIMIT();

            SQL_String = SQL_String + "SELECT " + SP + " FROM " + tbl + Filter + GRP + Ordnen + Limit + "UNION ALL";

            DB_Tabellen = new Data_DB_Tabellen();
            DB_Spalten = new Data_DB_Spalten();
            DB_Filter = new Data_DB_Filter();
            DB_Gruppieren = new Data_DB_Gruppieren();
            DB_Ordnen = new Data_DB_Ordnen();
            DB_Limit = new Data_DB_Limit();

        }

    }

    /***************************************************************************************************
     * 
     * Update Objekte
     * 
     ************************************************************************************************* */
    public class Database_Update {
        public Data_DB_Tabellen DB_Tabellen;
        public Data_DB_Data DB_Daten;
        public Data_DB_Filter DB_Filter;

        

        public Database_Update() {

            DB_Tabellen = new Data_DB_Tabellen();
            DB_Daten = new Data_DB_Data();
            DB_Filter = new Data_DB_Filter();
        }

        //TASK Debug
        @Deprecated
        public void DB_DEBUG_SQL_STRING() {

            System.out.println("******************** DEBUG SQL GET *****************************************\n\n");

            System.out.println("UPDATE " + DB_Tabellen.func_SQL_Abfrage() + " " + DB_Daten.func_Daten_Update() + " " + DB_Filter.func_SQL_Where() + " "
                    + " \n\n");

            System.out.println("******************** DEBUG ENDE *****************************************\n\n");

        }

        public void DB_Update() {

            Connection conn = null;

            try {
                long key = -1L;

                // Then we ask a connection from the DriverManager by passing the
                // connection URL and the password.
                conn = conn();

                // To delete records from tables we create an SQL delete command.
                // The question mark that we used in the where clause will be the
                // holder of value that will be assigned by PreparedStatement class.
                String sql = "UPDATE " + DB_Tabellen.func_SQL_Abfrage() + " " + DB_Daten.func_Daten_Update() + " " + DB_Filter.func_SQL_Where();

                if (debugNextQuery) {
                    System.out.println("## Debugging Update Query");

                    System.out.println("  " + sql);
                    debugNextQuery = false;
                }

                if (skipNextQuery) {
                    System.out.println("## Skipping Update Query");
                    skipNextQuery = false;
                    return;
                }

                // Create a statement object. We use PreparedStatement here.
                PreparedStatement statement = conn.prepareStatement(sql);

                // Pass a value of a userId that will tell the database which
                // records in the database to be deleted. Remember that when
                // using a statement object the indext parameter is start from
                // 1 not 0 as in the Java array data type index.
                int a = 1;
                File file;
                for (int i = 0; i < DB_Daten.SQL_Daten.length; i++) {
                    if (DB_Daten.SQL_Daten[i][2] != null && DB_Daten.SQL_Daten[i][2].equals("?") == true) {

                        file = new File(DB_Daten.SQL_Daten[i][3]);
                        FileInputStream io = new FileInputStream(file);
                        statement.setBinaryStream(a, io, (int) file.length());
                        a++;
                    }
                }

                // Tell the statement to execute the command. The executeUpdate()
                // method for a delete command returns number of records deleted
                // as the command executed in the database. If no records was
                // deleted it will simply return 0
                int id = statement.executeUpdate();

                release(conn);

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                release(conn);
            }

        }

        public void DB_Data_Leeren() {
            DB_Tabellen = new Data_DB_Tabellen();
            DB_Daten = new Data_DB_Data();
            DB_Filter = new Data_DB_Filter();
        }

    }

    /***************************************************************************************************
     * 
     * Insert Objekte
     * 
     ************************************************************************************************* */
    public class Database_Insert {
        public Data_DB_Tabellen DB_Tabellen;
        public Data_DB_Data DB_Daten;


        public Database_Insert() {

            DB_Tabellen = new Data_DB_Tabellen();
            DB_Daten = new Data_DB_Data();

        }

        //TASK Debug
        @Deprecated
        public void DB_DEBUG_SQL_STRING() {

            System.out.println("******************** DEBUG SQL GET *****************************************\n\n");

            System.out.println("INSERT INTO " + DB_Tabellen.func_SQL_Abfrage() + " " + DB_Daten.func_Daten_INSERT() + " " + " \n\n");

            System.out.println("******************** DEBUG ENDE *****************************************\n\n");

        }

        public int DB_Insert() {

            Connection conn = null;

            try {
                long key = -1L;

                // Then we ask a connection from the DriverManager by passing the
                // connection URL and the password.
                conn = conn();

                // To delete records from tables we create an SQL delete command.
                // The question mark that we used in the where clause will be the
                // holder of value that will be assigned by PreparedStatement class.
                String sql = "INSERT INTO " + DB_Tabellen.func_SQL_Abfrage() + " " + DB_Daten.func_Daten_INSERT() + " ";

                if (debugNextQuery) {
                    System.out.println("## Debugging Insert Query");

                    System.out.println("  " + sql);
                    debugNextQuery = false;
                }

                if (skipNextQuery) {
                    System.out.println("## Skipping Insert Query");
                    skipNextQuery = false;
                    return 0;
                }

                // Create a statement object. We use PreparedStatement here.
                PreparedStatement statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                // Pass a value of a userId that will tell the database which
                // records in the database to be deleted. Remember that when
                // using a statement object the indext parameter is start from
                // 1 not 0 as in the Java array data type index.

                int a = 1;
                File file;
                for (int i = 0; i < DB_Daten.SQL_Daten.length; i++) {
                    if (DB_Daten.SQL_Daten[i][2] != null && DB_Daten.SQL_Daten[i][2].equals("?") == true) {

                        file = new File(DB_Daten.SQL_Daten[i][3]);
                        FileInputStream io = new FileInputStream(file);
                        statement.setBinaryStream(a, io, (int) file.length());
                        a++;
                    }
                }

                // Tell the statement to execute the command. The executeUpdate()
                // method for a delete command returns number of records deleted
                // as the command executed in the database. If no records was
                // deleted it will simply return 0
                int id = statement.executeUpdate();
                int ids = 0;
                ResultSet rs = statement.getGeneratedKeys();

                if (rs.next()) {
                    ids = rs.getInt(1);
                }

                release(conn);
                return ids;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                release(conn);
            }
            return 0;

        }

        public void DB_Data_Leeren() {
            DB_Tabellen = new Data_DB_Tabellen();
            DB_Daten = new Data_DB_Data();
        }

    }

    /***************************************************************************************************
     * 
     * Delete Objekte
     * 
     ************************************************************************************************* */
    public class Database_Delete extends Data_DB_Sammlung {
        public Data_DB_Tabellen DB_Tabellen;
        public Data_DB_Filter DB_Filter;

        private String[][] Menge_DEL = null;
        private String[][] Menge = null;

        public Database_Delete() {

            DB_Tabellen = new Data_DB_Tabellen();
            DB_Filter = new Data_DB_Filter();
        }

        //TASK Debug

        public long DB_Delete() {

            Connection conn = null;

            // Pürfung ob einzeln oder nicht
            if (DB_Tabellen.SQL_Abfrage[0][1] != "Einzeln") {

                DBClass db = new DBClass();
                db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_All();
                db.DB_Data_Get.DB_Tabellen.DB_CustomSQL(DB_Tabellen.func_SQL_Abfrage());// SQL von Tabellen
                db.DB_Data_Get.DB_Filter.DB_WHERE_CUSTOM(DB_Filter.func_SQL_Where(), "");

                final SQLContainer container = db.DB_Data_Get.DB_SEND_AND_GET();

                // Bildung einer Menge

                Mengebilden();

                int Count = this.Menge.length;
                int Dat_C = 0;

                // Menge DEL Bilden
                for (Object DataItemId : container.getItemIds()) {

                    for (int i = 0; Count > i; i++) {

                        this.Menge_DEL = ArrayBuilderString(this.Menge_DEL, 3);
                        this.Menge_DEL[Dat_C][0] = this.Menge[i][0];
                        this.Menge_DEL[Dat_C][1] = this.Menge[i][1];

                        try {
                            this.Menge_DEL[Dat_C][2] = (String) container.getItem(DataItemId).getItemProperty(this.Menge[i][1]).getValue();
                        } catch (Exception e) {
                            this.Menge_DEL[Dat_C][2] = container.getItem(DataItemId).getItemProperty(this.Menge[i][1]).getValue().toString();

                        }

                        Dat_C = Dat_C + 1;
                    }

                }

                // Nachschauen was ist verknüpft als Array und die Daten auslesen
                // Dann Löschen Fertig

                int Count_Del = this.Menge_DEL.length;

                DBClass dbd = new DBClass();

                Boolean debug = false;
                Boolean skip = false;

                if (debugNextQuery) {
                    debug = true;
                }

                if (skipNextQuery) {
                    skip = true;
                }

                for (int T = 0; Count_Del > T; T++) {
                    // Einzel Löschung

                    dbd.DB_Data_Delete.DB_Tabellen.DB_set_Tabelle_Einzeln(this.Menge_DEL[T][0]);
                    dbd.DB_Data_Delete.DB_Filter.DB_WHERE_Allgemein(this.Menge_DEL[T][0], this.Menge_DEL[T][1], "=", this.Menge_DEL[T][2], "");

                    if (debug) {
                        debugNextQuery = true;
                    }

                    if (skip) {
                        skipNextQuery = true;
                    }

                    dbd.DB_Data_Delete.DB_Delete();
                    dbd.DB_Data_Delete.DB_Data_Leeren();
                    
                }
                
                debugNextQuery = false;
                skipNextQuery = false;

            } else {
                try {

                    conn = conn();

                    // To delete records from tables we create an SQL delete command.
                    // The question mark that we used in the where clause will be the
                    // holder of value that will be assigned by PreparedStatement class.
                    String sql = "DELETE FROM " + DB_Tabellen.func_SQL_Abfrage() + " " + DB_Filter.func_SQL_Where() + " ";

                    if (debugNextQuery) {
                        System.out.println("## Debugging Delete Query");

                        System.out.println("  " + sql);
                        debugNextQuery = false;
                    }

                    if (skipNextQuery) {
                        System.out.println("## Skipping Delete Query");
                        skipNextQuery = false;
                        return 0;
                    }

                    // Create a statement object. We use PreparedStatement here.
                    PreparedStatement statement = conn.prepareStatement(sql);

                    // Pass a value of a userId that will tell the database which
                    // records in the database to be deleted. Remember that when
                    // using a statement object the indext parameter is start from
                    // 1 not 0 as in the Java array data type index.

                    // Tell the statement to execute the command. The executeUpdate()
                    // method for a delete command returns number of records deleted
                    // as the command executed in the database. If no records was
                    // deleted it will simply return 0
                    int rows = statement.executeUpdate();

                    release(conn);
                    return rows;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    release(conn);
                }

                // Normales Löschen

            }
            return 0;

        }

        public void DB_Data_Leeren() {
            DB_Tabellen = new Data_DB_Tabellen();
            DB_Filter = new Data_DB_Filter();
            Menge_DEL = null;
            Menge = null;
        }

        private void Mengebilden() {
            int Count = DB_Tabellen.SQL_Abfrage.length;
            int Count_var = 0;
            String[][] temp_array = DB_Tabellen.SQL_Abfrage;

            for (int i = 0; Count > i; i++) {
                if (i == 0)

                {
                    this.Menge = ArrayBuilderString(this.Menge, 2);
                    this.Menge[0][0] = temp_array[i][0];
                    this.Menge[0][1] = temp_array[i][3];
                    this.Menge = ArrayBuilderString(this.Menge, 2);
                    this.Menge[1][0] = temp_array[i][1];
                    this.Menge[1][1] = temp_array[i][4];

                    func_Array_Leerung(temp_array, 0);
                    func_Array_Leerung(temp_array, 1);

                } else {

                    Count_var = temp_array.length;

                    for (int k = 0; Count_var > k; k++) {

                        if (func_P(temp_array[k][0]) == true) {
                            this.Menge = ArrayBuilderString(this.Menge, 2);
                            this.Menge[this.Menge.length][0] = temp_array[i][1];
                            this.Menge[this.Menge.length][1] = temp_array[i][4];
                            break;
                        }
                        if (func_P(temp_array[k][1]) == true) {
                            this.Menge = ArrayBuilderString(this.Menge, 2);
                            this.Menge[this.Menge.length][0] = temp_array[i][0];
                            this.Menge[this.Menge.length][1] = temp_array[i][3];
                            break;
                        }

                    }
                }

            }

        }

        private boolean func_P(String Tabelle) {
            int Count = this.Menge.length;
            boolean test = false;
            for (int i = 0; Count > i; i++) {
                if (this.Menge[i][0] == Tabelle) {
                    test = true;

                }
            }
            return test;

        }

        private String[][] func_Array_Leerung(String[][] arr, int id) {

            int Count = arr.length;
            String[][] temp = null;
            for (int i = 0; Count > i; i++) {

                if (Count == 1) {
                    return null;

                }

                if (i != id) {

                    temp = ArrayBuilderString(temp, 5);

                    temp[temp.length - 1] = arr[i];

                }
            }

            return temp;

        }

    }

    /***************************************************************************************************
     * 
     * Custom Objekte
     * 
     ************************************************************************************************* */
    
    //TASK all of this needs reworking!
    public class CustomQuery {
        public Data_DB_Tabellen DB_Tabellen;
        public Data_DB_Data DB_Daten;

        private Connection persistentConnection;
        
        private String CUSTOM_SQL_STRING;
        
        private final Logger LOG = LoggerFactory.getLogger(CustomQuery.class);
        
        public DBClass db;
        
        public CustomQuery(DBClass db) {

            DB_Tabellen = new Data_DB_Tabellen();
            DB_Daten = new Data_DB_Data();
            this.db = db;

        }
        
        public void setSqlString(String sql){
            CUSTOM_SQL_STRING = sql;
        }
        
        
        protected Class<?> getClassForSqlType(String name) {
            try {
                return Class.forName(name);
            } catch (Exception ex) {
                LOG.warn("unable to get class for name " + name + " ", ex);
                return null;
            }
        }
        
        public void generateContainerFromResultSet(ResultSet result, Container c) throws UnsupportedOperationException, SQLException{
        	
    		ResultSetMetaData md = result.getMetaData();
            
            for (int i = 0; i < md.getColumnCount(); ++i) {
                String label = md.getColumnLabel(i + 1);
                Class<?> clazz = getClassForSqlType(md.getColumnClassName(i + 1));
                c.addContainerProperty(label, clazz, null);
            }

            int i = 0;
            
            try {
            	
                while (result.next()) {
                    Item item = c.addItem(i++);
                    for (Object propertyId : c.getContainerPropertyIds()) {
                        item.getItemProperty(propertyId).setValue(result.getObject(propertyId.toString()));
                    }
                }
                
			} catch (Exception SQLException) {
				
			}

            
        }
        
        /**
         * 
         * @return Container (indexed)
         */
        
        public Container query(){
        	return query(false);
        }
        
        public Container query(Container c){
            return query(false, c);
        }
        
        public Container query(Boolean keepConnection){
            return query(keepConnection, new IndexedContainer());
        }
        
        public Container query(Boolean keepConnection, Container c){
        	
            PreparedStatement statement = null;
            Connection conn = null;
            
            try {
                
        		if(persistentConnection != null){
        			statement = prepareStatement(persistentConnection);
            	}else{
            		statement = prepareStatement();
            	}
            	try {
            	    conn = statement.getConnection();    
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            	
            	 
            	ResultSet result = null;
            	
            	try {
    				result = statement.executeQuery();
    			} catch (SQLException e) {
    				e.printStackTrace();
    			}
            	
    			try {
    				generateContainerFromResultSet(result, c);
    			} catch (UnsupportedOperationException | SQLException e) {
    				e.printStackTrace();
    			}
        	} catch (Exception e) {
        	    
            }finally {
                if(!keepConnection) {
                    pool.releaseConnection(persistentConnection);
                    this.persistentConnection = null;
                    try {
                        if(statement != null) {
                            statement.close();
                        }
                    } catch (SQLException e) {
                        log.error("statement close error {}", e);
                        e.printStackTrace();
                    }
                }
//                pool.releaseConnection(conn);
            }
        	
        	return c;
        }
        
        public int update(){
        	return update(false);
        }
        
        public int update(Boolean keepConnection){
            return update(keepConnection, null);
        }
        
        public int update(Boolean keepConnection, ArrayList<Integer> generatedKeys){
            Integer result = 0;
            PreparedStatement statement = null;
            
            try {
            
                if(persistentConnection != null){
                    statement = prepareStatement(persistentConnection);
                }else{
                    statement = prepareStatement();
                }
                
                try {
                    result = statement.executeUpdate();
                    if(generatedKeys != null){
                        ResultSet rs = statement.getGeneratedKeys();
                        while(rs.next() == true){
                            generatedKeys.add(rs.getInt(1));
                        }
                    }
                        
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            
            } catch (Exception e) {
                
            }finally {
                if(!keepConnection) {
                    pool.releaseConnection(persistentConnection);
                    this.persistentConnection = null;
                    try {
                        if(statement != null) {
                            statement.close();
                        }
                    } catch (SQLException e) {
                        log.error("statement close error {}", e);
                        e.printStackTrace();
                    }
                }
            }
                
           return result;
        }
        
        private Connection generateConnection(){
            return generateConnection(0);
        }
        
        private Connection generateConnection(Integer wait){
            
            long timeout = 500;
            
            try {
                
                if(wait > 0) {
                    log.debug("sleeping for {} ...", timeout);
                    Thread.sleep(timeout + 10*wait);
                }
                
                
                persistentConnection = pool.reserveConnection();
                persistentConnection.setAutoCommit(true);
                
            } catch (SQLException | InterruptedException e1) {
                if(wait < retries && e1 instanceof SQLException) {
                    
                    log.info("too many connections, retry no. {} in {}", (wait+1), timeout);
                    persistentConnection = generateConnection(wait > 0 ? ++wait : 1);
                    
                }else if(wait >= retries && e1 instanceof SQLException) {
                    log.error("failed to retry {} times", wait);
                    
                }else if(e1 instanceof InterruptedException) {
                    log.warn("sleep no.{} was interrupted", wait);
                    persistentConnection = generateConnection();
                }else {
                    log.error("Unknown Exception");
                    e1.printStackTrace();
                }
            }
            
            return persistentConnection;
        }
        
        private PreparedStatement prepareStatement() {
            return prepareStatement(generateConnection());
        }

        private PreparedStatement prepareStatement(Connection conn) {
            
            PreparedStatement statement = null;
            
            try {
                
                String sql = this.CUSTOM_SQL_STRING;

                if (debugNextQuery) {
                    System.out.println("## Debugging Custom Query");

                    System.out.println("  " + sql);
                    debugNextQuery = false;
                }

                if (skipNextQuery) {
                    System.out.println("## Skipping Custom Query");
                    skipNextQuery = false;
                    return null;
                }

                 statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return statement;

        }

        @Override
        public void finalize() {
            if(persistentConnection != null) {
                log.error("persistent connection not released correctly!");
            }
            pool.releaseConnection(persistentConnection);
            this.persistentConnection = null;
        }
    }
    
    /***************************************************************************************************
     * 
     * Spezial Objekte
     * 
     ************************************************************************************************* */
    public class Database_Advanced {

         CMS_Config_Std std = CMS_Config_Std.getInstanceXmlOnly();
        
        private final String SQL_Config_Adresse = std.Standard_Server_URL;
        private final String SQL_Config_Username = std.Standard_Server_Username;
        private final String SQL_Config_Passwort = std.Standard_Server_Passwort;
        private final String SQL_Config_Datenbank = std.Standard_Server_Database;
        private final String SQL_Config_Port = std.Standard_Server_Port;
        private final String SQL_Config_Treiber = "com.mysql.jdbc.Driver";
        private final String SQL_Config_URL_Driver = "jdbc:mysql://";

        public SQLContainer DB_Database_GET_Columns(String Tabelle) {

            try {
                SQLContainer container = new SQLContainer(new FreeformQuery("SELECT column_name FROM information_schema.columns WHERE table_schema='"
                        + SQL_Config_Datenbank + "' AND table_name='" + Tabelle + "';", pool));
                // cona.destroy(); //persistentPooling!
                return container;

            } catch (SQLException e) {
                // TODO Automatisch generierter Erfassungsblock
                e.printStackTrace();
                return null;

            }

        }

        public SQLContainer DB_Database_GET_ColumnsTYP(String Tabelle, String Columnname) {

            try {
                SQLContainer container = new SQLContainer(new FreeformQuery("SELECT  DATA_TYPE FROM information_schema.columns WHERE table_schema='"
                        + SQL_Config_Datenbank + "' AND table_name='" + Tabelle + "' AND column_name='" + Columnname + "';", pool));
                // // cona.destroy(); //persistentPooling!
                return container;

            } catch (SQLException e) {
                // TODO Automatisch generierter Erfassungsblock
                e.printStackTrace();
                return null;

            }

        }

        public SQLContainer DB_Database_GET_ColumnsTYP(String Tabelle) {

            try {
                SQLContainer container = new SQLContainer(new FreeformQuery("SELECT COLUMN_NAME, DATA_TYPE FROM information_schema.columns WHERE table_schema="
                        + SQL_Config_Datenbank + "' AND table_name='" + Tabelle + "';", pool));
                // // cona.destroy(); //persistentPooling!
                return container;

            } catch (SQLException e) {
                // TODO Automatisch generierter Erfassungsblock
                e.printStackTrace();
                return null;

            }

        }

        public SQLContainer DB_Database_GET_ColumnsTYP(ArrayList<String> tableList) {

            try {

                String in = "";

                for (String table : tableList) {
                    in += "'" + table + "', ";
                }
                in = in.substring(0, in.length() - 2); // letztes komma abtrennen

                SQLContainer container = new SQLContainer(new FreeformQuery(
                        "SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE FROM information_schema.columns WHERE table_schema='" + SQL_Config_Datenbank
                                + "' AND table_name IN (" + in + ");", pool));
                return container;

            } catch (SQLException e) {
                // TODO Automatisch generierter Erfassungsblock
                e.printStackTrace();
                return null;

            }

        }

        public SQLContainer DB_Database_GET_ColumnsTYP_TBL(String[][] TBL) {

            String TBL_Query = "";

            try {
                SQLContainer container = new SQLContainer(new FreeformQuery("SELECT  DATA_TYPE FROM information_schema.columns WHERE table_schema='"
                        + SQL_Config_Datenbank + "' AND " + TBL_Query, pool));
                // cona.destroy(); //persistentPooling!
                return container;

            } catch (SQLException e) {
                // TODO Automatisch generierter Erfassungsblock
                e.printStackTrace();
                return null;

            }

        }

        public SQLContainer DB_Database_GET_TABLES() {

            try {
                SQLContainer container = new SQLContainer(new FreeformQuery("SHOW TABLES  FROM " + SQL_Config_Datenbank, pool));
                // cona.destroy(); //persistentPooling!
                return container;

            } catch (SQLException e) {
                // TODO Automatisch generierter Erfassungsblock
                e.printStackTrace();
                return null;

            }

        }

        public void DB_Database_GET_Connection_Close() {

            // cona.destroy(); //persistentPooling!
        }

        public boolean DB_Database_Func_CheckDBExistence() {

            boolean prüfung = false;
            String Datenbank;

            try {
                SQLContainer container = new SQLContainer(new FreeformQuery("SHOW DATABASES LIKE '" + SQL_Config_Datenbank + "'", pool));

                if (container.size() > 0) {
                    // cona.destroy(); //persistentPooling!
                    return true;
                } else {
                    // cona.destroy(); //persistentPooling!
                    return false;
                }

            } catch (SQLException e) {
                // TODO Automatisch generierter Erfassungsblock
                e.printStackTrace();
                // cona.destroy(); //persistentPooling!
                return false;

            }

        }

        public void DB_Database_Create() {
            String url = SQL_Config_URL_Driver + SQL_Config_Adresse + ":" + SQL_Config_Port + "/" + "mysql";
            Connection connection = null;
            Statement statement = null;
            try {
                Class.forName(SQL_Config_Treiber).newInstance();
                // String url = "jdbc:mysql://localhost/mysql";
                connection = conn();

                statement = connection.createStatement();
                String hrappSQL = "CREATE DATABASE " + SQL_Config_Datenbank;
                statement.executeUpdate(hrappSQL);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                    } // nothing we can do
                }
                if (connection != null) {
                    release(connection);
                }
            }

        }

        public void DB_Database_Delete() {
            String url = SQL_Config_URL_Driver + SQL_Config_Adresse + ":" + SQL_Config_Port + "/" + "mysql";
            Connection connection = null;
            Statement statement = null;
            try {
                Class.forName(SQL_Config_Treiber).newInstance();
                // String url = "jdbc:mysql://localhost/mysql";
                connection = conn();

                statement = connection.createStatement();
                String hrappSQL = "CREATE DATABASE " + SQL_Config_Datenbank;
                statement.executeUpdate(hrappSQL);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                    } // nothing we can do
                }
                if (connection != null) {
                    release(connection);
                }
            }

        }

        public boolean DB_Database_Func_Check_Empty() {

            try {
                SQLContainer container = new SQLContainer(new FreeformQuery("SHOW TABLES  FROM " + SQL_Config_Datenbank, pool));

                if (container.size() > 0) {
                    // cona.destroy(); //persistentPooling!
                    return false;
                } else {
                    // cona.destroy(); //persistentPooling!
                    return true;
                }

            } catch (SQLException e) {
                // TODO Automatisch generierter Erfassungsblock
                e.printStackTrace();
                // cona.destroy(); //persistentPooling!
                return false;

            }

        }

        public void DB_Database_Backup(String Speicherpfad) {

            String os = "os.name";

            Properties prop = System.getProperties();

            if (prop.getProperty(os).indexOf("Win") != -1) {

                URL Pfad_URL = getClass().getProtectionDomain().getCodeSource().getLocation();

                String P_temp = Pfad_URL.toString();

                String Pad = P_temp.substring(6, P_temp.length() - 31);

                String pfad = Pad.replace("%20", " ");

                try {
                    String Befehl = "cmd.exe /C cd \"" + pfad + "\" && mysqldump" + " -c " + SQL_Config_Datenbank + " -h" + SQL_Config_Adresse + " -P"
                            + SQL_Config_Port + " -u" + SQL_Config_Username + " -p" + SQL_Config_Passwort + " > \"" + Speicherpfad + "\"";

                    //
                    String line;

                    Process runtimeProcess = Runtime.getRuntime().exec(Befehl);

                    BufferedReader input = new BufferedReader(new InputStreamReader(runtimeProcess.getErrorStream()));
                    while ((line = input.readLine()) != null) {
                        System.out.println(line);
                    }
                    input.close();

                    int processComplete = -1;
                    try {

                        processComplete = runtimeProcess.waitFor();

                    } catch (InterruptedException e) {
                        // TODO Automatisch generierter Erfassungsblock
                        e.printStackTrace();
                    }
                    if (processComplete == 0) {

                        System.out.println("Backup taken successfully");

                    } else {

                        System.out.println("“Could not take mysql backup”");

                    }

                } catch (IOException e) {

                    e.printStackTrace();
                }

            } else {

                // TODO linux

            }

        }

        public void DB_Database_Restore(String Restorepfad) {

            String os = "os.name";

            Properties prop = System.getProperties();

            if (prop.getProperty(os).indexOf("Win") != -1) {

                URL Pfad_URL = getClass().getProtectionDomain().getCodeSource().getLocation();

                String P_temp = Pfad_URL.toString();

                String Pad = P_temp.substring(6, P_temp.length() - 31);

                String pfad = Pad.replace("%20", " ");

                try {
                    String Befehl = "cmd.exe /C cd \"" + pfad + "\" && mysql" + " -c " + SQL_Config_Datenbank + " -h" + SQL_Config_Adresse + " -P"
                            + SQL_Config_Port + " -u" + SQL_Config_Username + " -p" + SQL_Config_Passwort + " < \"" + Restorepfad + "\"";

                    //
                    String line;

                    Process runtimeProcess = Runtime.getRuntime().exec(Befehl);

                    BufferedReader input = new BufferedReader(new InputStreamReader(runtimeProcess.getErrorStream()));
                    while ((line = input.readLine()) != null) {
                        System.out.println(line);
                    }
                    input.close();

                    int processComplete = -1;
                    try {

                        processComplete = runtimeProcess.waitFor();

                    } catch (InterruptedException e) {
                        // TODO Automatisch generierter Erfassungsblock
                        e.printStackTrace();
                    }
                    if (processComplete == 0) {

                        System.out.println("Restore taken successfully");

                    } else {

                        System.out.println("“Could not take mysql backup”");

                    }

                } catch (IOException e) {

                    e.printStackTrace();
                }

            } else {

                // TODO linux

            }

        }

        // Privilegien Check
        public boolean DB_Prüfen_ALL_Privilleg_For_Database(String Host, String Username, String Database) {

            try {
                SQLContainer container = new SQLContainer(new FreeformQuery("SHOW GRANTS FOR '" + Username + "'@'" + Host + "'", pool));

                boolean Prüfung_Privilegien = false;
                boolean Prüfung_Allg = false;
                boolean Prüfung_Master = false;

                String Berechtigung;
                for (Object ItemIda : container.getItemIds()) {
                    // UserId = (int) result.getItem(ItemIda).getItemProperty(this.Spalte_ID)
                    // .getValue();

                    Berechtigung = (String) container.getItem(ItemIda).getItemProperty("Grants for " + Username + "@" + Host).getValue();

                    if ((Berechtigung.indexOf("GRANT ALL PRIVILEGES ON \"" + Database + "\".* TO '" + Username + "'@'" + Host + "' WITH GRANT OPTION") != -1)
                            | (Berechtigung.indexOf("GRANT ALL PRIVILEGES ON *.* TO '" + Username + "'@'" + Host + "' WITH GRANT OPTION") != -1)) {
                        Prüfung_Privilegien = true;
                    }

                    if (Berechtigung.indexOf("GRANT USAGE ON *.* TO '" + Username + "'@'" + Host + "' IDENTIFIED BY") != -1) {
                        Prüfung_Allg = true;
                    }

                    if (Berechtigung.indexOf("GRANT ALL PRIVILEGES ON *.* TO '" + Username + "'@'" + Host + "' IDENTIFIED BY") != -1
                            && Berechtigung.indexOf("WITH GRANT OPTION") != -1) {
                        Prüfung_Master = true;
                    }

                }
                // cona.destroy(); //persistentPooling!

                if ((Prüfung_Privilegien == true && Prüfung_Allg == true) | (Prüfung_Master == true)) {
                    return true;

                } else {
                    return false;

                }

            } catch (SQLException e) {
                // TODO Automatisch generierter Erfassungsblock
                e.printStackTrace();
                // cona.destroy(); //persistentPooling!
                return false;

            }

        }

        public void DB_Set_No_Privilleg_For_ALL_DATABASE(String Host, String Username) {
            String url = SQL_Config_URL_Driver + SQL_Config_Adresse + ":" + SQL_Config_Port + "/" + "mysql";
            Connection connection = null;
            Statement statement = null;
            try {
                Class.forName(SQL_Config_Treiber).newInstance();
                // String url = "jdbc:mysql://localhost/mysql";
                connection = conn();

                statement = connection.createStatement();
                String hrappSQL = "REVOKE ALL PRIVILEGES ON * . * FROM '" + Username + "'@'" + Host + "';";
                statement.executeUpdate(hrappSQL);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                    } // nothing we can do
                }
                if (connection != null) {
                    release(connection);
                }
            }

        }

        public void DB_Set_ALL_Privilleg_For_ALL_DATABASE(String Host, String Username) {
            String url = SQL_Config_URL_Driver + SQL_Config_Adresse + ":" + SQL_Config_Port + "/" + "mysql";
            Connection connection = null;
            Statement statement = null;
            try {
                Class.forName(SQL_Config_Treiber).newInstance();
                // String url = "jdbc:mysql://localhost/mysql";
                connection = conn();

                statement = connection.createStatement();
                String hrappSQL = "GRANT ALL PRIVILEGES ON * . * TO '" + Username + "'@'" + Host
                        + "' WITH GRANT OPTION MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0";
                statement.executeUpdate(hrappSQL);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                    } // nothing we can do
                }
                if (connection != null) {
                    release(connection);
                }
            }

        }

        public void DB_Set_No_Privilleg_For_Database(String Host, String Username, String Database) {
            String url = SQL_Config_URL_Driver + SQL_Config_Adresse + ":" + SQL_Config_Port + "/" + "mysql";
            Connection connection = null;
            Statement statement = null;
            try {
                Class.forName(SQL_Config_Treiber).newInstance();
                // String url = "jdbc:mysql://localhost/mysql";
                connection = conn();

                statement = connection.createStatement();
                String hrappSQL = "REVOKE ALL PRIVILEGES ON " + Database + ". * FROM '" + Username + "'@'" + Host + "';";
                statement.executeUpdate(hrappSQL);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                    } // nothing we can do
                }
                if (connection != null) {
                    release(connection);
                }
            }

        }

        public void DB_Set_ALL_Privilleg_For_Database(String Host, String Username, String Database) {
            String url = SQL_Config_URL_Driver + SQL_Config_Adresse + ":" + SQL_Config_Port + "/" + "mysql";
            Connection connection = null;
            Statement statement = null;
            try {
                Class.forName(SQL_Config_Treiber).newInstance();
                // String url = "jdbc:mysql://localhost/mysql";
                connection = conn();

                statement = connection.createStatement();
                String hrappSQL = "GRANT ALL PRIVILEGES ON " + Database + ".* TO '" + Username + "'@'" + Host
                        + "' WITH GRANT OPTION MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0";
                statement.executeUpdate(hrappSQL);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                    } // nothing we can do
                }
                if (connection != null) {
                    release(connection);
                }
            }

        }

        public void DB_Benutzer_Erstellen(String Host, String Username, String Passwort) {
            String url = SQL_Config_URL_Driver + SQL_Config_Adresse + ":" + SQL_Config_Port + "/" + "mysql";
            Connection connection = null;
            Statement statement = null;
            try {
                Class.forName(SQL_Config_Treiber).newInstance();
                // String url = "jdbc:mysql://localhost/mysql";
                connection = conn();

                statement = connection.createStatement();
                String hrappSQL = "CREATE USER " + Username + "@'" + Host + "' IDENTIFIED BY '" + Passwort + "'";
                statement.executeUpdate(hrappSQL);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                    } // nothing we can do
                }
                if (connection != null) {
                    release(connection);
                }
            }

        }

        public void DB_Benutzer_Loeschen(String Host, String Username) {
            String url = SQL_Config_URL_Driver + SQL_Config_Adresse + ":" + SQL_Config_Port + "/" + "mysql";
            Connection connection = null;
            Statement statement = null;
            try {
                Class.forName(SQL_Config_Treiber).newInstance();
                // String url = "jdbc:mysql://localhost/mysql";
                connection = conn();

                statement = connection.createStatement();
                String hrappSQL = "DROP USER '" + Username + "'" + "@" + "'" + Host + "'";
                statement.executeUpdate(hrappSQL);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                    } // nothing we can do
                }
                if (connection != null) {
                    release(connection);
                }
            }

        }

        public boolean DB_Benutzer_Exist_Prüfung(String Username) {


            try {
                SQLContainer container = new SQLContainer(new FreeformQuery("SELECT *  FROM mysql.user WHERE User='" + Username + "'", pool));

                if (container.size() > 0) {
                    // cona.destroy(); //persistentPooling!
                    return true;
                } else {
                    // cona.destroy(); //persistentPooling!
                    return false;
                }

            } catch (SQLException e) {
                // TODO Automatisch generierter Erfassungsblock
                e.printStackTrace();
                // cona.destroy(); //persistentPooling!
                return false;

            }

        }

        public void DB_Database_Empty() {

            Connection connection = null;
            
            try {
                SQLContainer containera = new SQLContainer(new FreeformQuery("SHOW TABLES  FROM " + SQL_Config_Datenbank, pool));

                String url = SQL_Config_URL_Driver + SQL_Config_Adresse + ":" + SQL_Config_Port + "/" + SQL_Config_Datenbank;

                if (containera.size() > 0) {
                    

                    try {
                        Class.forName(SQL_Config_Treiber);
                    } catch (ClassNotFoundException e) {
                        // TODO Automatisch generierter Erfassungsblock
                        e.printStackTrace();
                    }
                    connection = conn();

                    for (Object ItemIda : containera.getItemIds()) {
                        // UserId = (int) result.getItem(ItemIda).getItemProperty(this.Spalte_ID)
                        // .getValue();

                        PreparedStatement pst = connection.prepareStatement("drop table "
                                + (String) containera.getItem(ItemIda).getItemProperty("Tables_in_" + SQL_Config_Datenbank).getValue());
                        pst.executeUpdate();

                    }

                    release(connection);

                }

            } catch (SQLException e) {
                // TODO Automatisch generierter Erfassungsblock
                e.printStackTrace();

            } finally {
                release(connection);
            }

            // cona.destroy(); //persistentPooling!

        }

        public boolean DB_Database_Func_TBL_Search(String Table) {

            try {
                SQLContainer container = new SQLContainer(new FreeformQuery("SHOW TABLES  FROM " + SQL_Config_Datenbank, pool));

                if (container.size() > 0) {
                    boolean Ergebniss = false;

                    for (Object ItemIda : container.getItemIds()) {
                        // UserId = (int) result.getItem(ItemIda).getItemProperty(this.Spalte_ID)
                        // .getValue();

                        if ((String) container.getItem(ItemIda).getItemProperty("Tables_in_" + SQL_Config_Datenbank).getValue() == Table) {
                            Ergebniss = true;
                        }

                    }

                    // cona.destroy(); //persistentPooling!
                    return Ergebniss;

                } else {
                    // cona.destroy(); //persistentPooling!
                    return false;
                }

            } catch (SQLException e) {
                // TODO Automatisch generierter Erfassungsblock
                e.printStackTrace();
                // cona.destroy(); //persistentPooling!
                return false;

            }

        }

        public boolean DB_Database_Func_Server_is_Online() throws IOException {
            Socket chatSocket; // Objekt für die Datenverbindung zum Server
            PrintWriter out; // Objekt für Schreibeströme
            BufferedReader in; // Objekt für Leseströme

            chatSocket = null;
            out = null;
            in = null;

            boolean Ergebniss = false;

            try {
                chatSocket = new Socket(SQL_Config_Adresse, Integer.parseInt(SQL_Config_Port));
                out = new PrintWriter(chatSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));

                Ergebniss = true;
            } catch (UnknownHostException e) {
                // Fehlermeldung bei unbekanntem host
                System.err.println("Don't know about host: szm.");
                Ergebniss = false;

            } catch (IOException e) {
                // Fehlermeldung 'keine IO-Verbindung'
                System.err.println("Couldn't get I/O for the connection to: szm.");
                Ergebniss = false;

            }

            if (Ergebniss != false) {
                out.close();
                in.close();
                chatSocket.close();
            }

            return Ergebniss;

        }

    }

}
