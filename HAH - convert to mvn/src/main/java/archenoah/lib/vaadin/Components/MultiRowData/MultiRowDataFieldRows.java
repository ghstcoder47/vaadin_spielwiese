package archenoah.lib.vaadin.Components.MultiRowData;

import java.util.ArrayList;
import java.util.Arrays;

public class MultiRowDataFieldRows {

    private ArrayList<ArrayList<MultiRowDataField>> fieldSets;

    // {section fields}
    // ****************
    // {end fields}

    // {section constructors}
    // **********************
    public MultiRowDataFieldRows() {

        fieldSets = new ArrayList<ArrayList<MultiRowDataField>>();
    }
    // {end constructors}

    // {section gettersandsetters}
    // ***************************
    public ArrayList<ArrayList<MultiRowDataField>> getRows() {
        return fieldSets;
    }
    // {end gettersandsetters}

    // {section publicmethods}
    // ***********************
    public void addFieldRow(MultiRowDataField... fields) {
        fieldSets.add(new ArrayList<MultiRowDataField>(Arrays.asList(fields))); // Arrays.asList Arraylist is not the same as java.util.ArrayList!
    }
    // {end publicmethods}

    // {section privatemethods}
    // ************************
    // {end privatemethods}

    // {section database}
    // ******************
    // {end database}

    // {section layout}
    // ****************
    // {end layout}
}
