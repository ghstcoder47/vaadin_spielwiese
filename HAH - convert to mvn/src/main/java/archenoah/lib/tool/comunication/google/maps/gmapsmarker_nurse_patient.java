package archenoah.lib.tool.comunication.google.maps;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import archenoah.lib.tool.java_plugin.array_builder.classes.ArrayBuilder_Mehrfach;

import com.google.gson.Gson;
import com.vaadin.ui.Embedded;

public class gmapsmarker_nurse_patient {

    private String Hoehe;
    private String Breite;
    public String URL_PHP;
    private String gson_temp;

    private String PHP_Arr[][];

    public Embedded emb;

    public gmapsmarker_nurse_patient(String Arg_Hoehe, String Arg_Breite) {
        // TODO Automatisch generierter Konstruktorstub
        Hoehe = Arg_Hoehe;
        Breite = Arg_Breite;
    }

    public void Fill_Patient(String LNG_H, String LNG_W, String Anrede, String Name, String Vorname, String Strasse, String PLZ, String Ort) {
        ArrayBuilder_Mehrfach am = new ArrayBuilder_Mehrfach(PHP_Arr, 9);
        PHP_Arr = am.Array_Holen();
        PHP_Arr[PHP_Arr.length - 1][0] = "Patient";
        PHP_Arr[PHP_Arr.length - 1][1] = LNG_H;
        PHP_Arr[PHP_Arr.length - 1][2] = LNG_W;
        PHP_Arr[PHP_Arr.length - 1][3] = Anrede;
        PHP_Arr[PHP_Arr.length - 1][4] = Name;
        PHP_Arr[PHP_Arr.length - 1][5] = Vorname;
        PHP_Arr[PHP_Arr.length - 1][6] = Strasse;
        PHP_Arr[PHP_Arr.length - 1][7] = PLZ;
        PHP_Arr[PHP_Arr.length - 1][8] = Ort;

        // Patient,H,W,......

    }

    public void Fill_Nurse(String LNG_H, String LNG_W, String Anrede, String Name, String Vorname, String Strasse, String PLZ, String Ort) {
        ArrayBuilder_Mehrfach am = new ArrayBuilder_Mehrfach(PHP_Arr, 9);
        PHP_Arr = am.Array_Holen();
        PHP_Arr[PHP_Arr.length - 1][0] = "Nurse";
        PHP_Arr[PHP_Arr.length - 1][1] = LNG_H;
        PHP_Arr[PHP_Arr.length - 1][2] = LNG_W;
        PHP_Arr[PHP_Arr.length - 1][3] = Anrede;
        PHP_Arr[PHP_Arr.length - 1][4] = Name;
        PHP_Arr[PHP_Arr.length - 1][5] = Vorname;
        PHP_Arr[PHP_Arr.length - 1][6] = Strasse;
        PHP_Arr[PHP_Arr.length - 1][7] = PLZ;
        PHP_Arr[PHP_Arr.length - 1][8] = Ort;
    }

    public void Generate() {
        Gson gs = new Gson();

        try {
            gson_temp = URLEncoder.encode(gs.toJson(PHP_Arr), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Automatisch generierter Erfassungsblock
            e.printStackTrace();
        }

        URL_PHP = "http://php.isconet-applications.de/Lizenzverwaltung_Vaadin/gmapsmarker.php?hoehe=" + Hoehe + "px&breite=" + Breite + "px&ARR_MARK=" + gson_temp;
        // System.out.println(URL_PHP);

    }

}
