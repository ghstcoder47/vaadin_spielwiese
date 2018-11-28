package archenoah.config;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import archenoah.lib.tool.java_plugin.system_zugriff.pfad.classes.Pfad;

public class MapsConfig {

    private Document doc;
    private HashMap<String, MapsConfig.XMLMapEntry> entries = new HashMap<String, MapsConfig.XMLMapEntry>();
    private String path = "archenoah/config/mapsConfig.xml";

    public MapsConfig() {

        Pfad PF = new Pfad();

        File fXmlFile = new File(PF.Pfadholen() + this.path);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            this.doc = dBuilder.parse(fXmlFile);

            NodeList nList = this.doc.getElementsByTagName("entry");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node entry = nList.item(temp);

                NamedNodeMap attr = entry.getAttributes();
                String id = attr.getNamedItem("id").getNodeValue();

                Boolean use = Boolean.valueOf(attr.getNamedItem("use").getNodeValue());

                NodeList cList = entry.getChildNodes();

                XMLMapEntry mapEntry = new XMLMapEntry(id, use, cList);

                this.entries.put(id, mapEntry);

            }

        } catch (Exception e) {
            // TASK Auto-generated catch block
            e.printStackTrace();
        }

    }

    class XMLMapEntry {

        private Boolean use;
        private String id;
        private HashMap<String, String> values = new HashMap<String, String>();

        public XMLMapEntry(String id, Boolean use, NodeList cList) {
            this.use = use;
            this.id = id;

            for (int i = 0; i < cList.getLength(); i++) {

                if (cList.item(i).getNodeName().equals("value")) {
                    String cid = cList.item(i).getAttributes().getNamedItem("id").getNodeValue();
                    String cval = "";

                    if (cList.item(i).hasChildNodes()) {
                        cval = cList.item(i).getChildNodes().item(0).getNodeValue();
                    }

                    this.values.put(cid, cval);
                }

            }

        }

        public Boolean enabled() {
            return use;
        }

        public String id() {
            return id;
        }

        public String getValue(String val) {
            return values.get(val);
        }
    }

    public String getEntry(String id, String value) {
        return entries.get(id).getValue(value);
    }

    public Boolean useEntry(String id) {
        return entries.get(id).enabled();
    }

}
