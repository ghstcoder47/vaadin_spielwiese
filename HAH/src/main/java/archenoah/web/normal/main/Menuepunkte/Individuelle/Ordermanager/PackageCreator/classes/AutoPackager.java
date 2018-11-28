package archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.PackageCreator.classes;

import java.util.HashMap;
import java.util.TreeMap;

import org.apache.commons.lang3.tuple.Pair;

import archenoah.lib.custom.MyUtils;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.comunication.dbclass.DBClass.CustomQuery;
import archenoah.web.normal.main.Menuepunkte.Individuelle.Ordermanager.PackageCreator.classes.PackagerConverter.TYPE;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.CustomComponent;

public class AutoPackager extends CustomComponent{

    // {section fields}
    // ****************
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AutoPackager.class);
    
    private Container sizesContainer;
    private HashMap<Pair<String, Integer>, TreeMap<Integer, Integer>> packageSizes;
    
    // {end fields}

    // {section constructors}
    // **********************
    public AutoPackager() {
        packageSizes = dbGetPackageSizes();
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    /**
     * 
     * @param country
     * @param product
     * @param total
     * @return {@literal <<product, sizeId>, count>
     */
    public HashMap<Pair<Integer, Integer>, Integer> calculateSizes(String country, Integer product, Integer total) {
        
        TreeMap<Integer, Integer> map = packageSizes.get(Pair.of(country, product));
        HashMap<Pair<Integer, Integer>, Integer> packs = new HashMap<Pair<Integer,Integer>, Integer>(); 
        
        if(map == null) {
            log.warn("no size list found for country {} product {}", country, product);
            packs.put(Pair.of(product, 0), total);
            return packs;
        }
        
        for (Integer size : map.descendingKeySet()) {
            
            int fit = total / size;

            if (fit > 0) {
                total = total - (size * fit);
                packs.put(Pair.of(product, map.get(size)), fit);
            }
            
        }
        
        if (total > 0) {
            log.warn("total left over for c: {}, p: {} - {}", country, product, total);
            packs.put(Pair.of(product, 0),  total);
        }
        
        return packs;
        
    }
    
    public PackagerConverter getNameConverter() {
        return new PackagerConverter(sizesContainer, TYPE.NAME);
    }
    public PackagerConverter getSizeConverter() {
        return new PackagerConverter(sizesContainer, TYPE.SIZE);
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    // {end privatemethods}

    // {section database}
    // ******************
    private HashMap<Pair<String, Integer>, TreeMap<Integer, Integer>> dbGetPackageSizes() {
        
        //           de      product           size     id
        HashMap<Pair<String, Integer>, TreeMap<Integer, Integer>> map = new HashMap<>();
        
        CustomQuery q = new DBClass().CustomQuery;
        String sql = "select PPS_ID as id, PPS_ID_PRODUCT as product, PPS_NAME as name, PPS_COUNTRY as country, PPS_SIZE as size from cust_prescription_product_sizes";
        q.setSqlString(sql);
        
        sizesContainer = q.query();
        
        for (Object iid : sizesContainer.getItemIds()) {
            Item item = sizesContainer.getItem(iid);
            
            Integer id = MyUtils.getValueFromItem(item, "id", Integer.class);
            Integer product = MyUtils.getValueFromItem(item, "product", Integer.class);
            Integer size = MyUtils.getValueFromItem(item, "size", Integer.class);
            String country = MyUtils.getValueFromItem(item, "country", String.class);
            
            Pair<String, Integer> pk = Pair.of(country, product);
            
            if(map.get(pk) == null) {
                map.put(pk, new TreeMap<Integer, Integer>());
            }
            
            map.get(pk).put(size, id);
            
        }
        
        return map;
        
    }
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
    
//    public static void main(String args[]) {
//        
//        Integer total = 47;
//        HashMap<Integer, Integer> packs = new HashMap<Integer, Integer>(); 
//        
//        ArrayList<Integer> sizes = new ArrayList<Integer>();
//        sizes.add(1);
//        sizes.add(5);
//        sizes.add(4);
//        sizes.add(10);
//        
//        Collections.sort(sizes);
//        Collections.reverse(sizes);
//        
//        for (Integer size : sizes) {
//            
//            int fit = total / size; 
//            
//            if(fit > 0) {
//                total = total - (size * fit);
//                packs.put(size, fit);
//            }
//        }
//        
//        log.info("packs: {}", packs);
//        
//    }
    

}
