package archenoah.lib.tool.pdf;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.safety.Whitelist;

import archenoah.config.CMS_Config_Std;
import archenoah.lib.tool.comunication.dbclass.DBClass;
import archenoah.lib.tool.pdf.FontProvider.FONT;
import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.HorizontalAlignment;
import be.quodlibet.boxable.ImageCell;
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.VerticalAlignment;
import be.quodlibet.boxable.image.Image;

import com.vaadin.data.Item;

public class PdfPopulater {

    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private PDDocument _pdfDocument;
    private byte[] fileByteArray;
    
    private PDType0Font font;
    
    private Boolean tableDebug = false;
    
    private final static Whitelist whiteTable = Whitelist.none().addTags("p", "i", "b", "br", "ul", "ol", "li");
    private final static Whitelist whiteFields = Whitelist.none();
    private final static HashMap<String, String> replacementList = new HashMap<>(); 
    private final static Document.OutputSettings settings = new Document.OutputSettings(); 
    
    private final static char cnf = '\u25A1';

    
    static {
        settings.prettyPrint(false);
        settings.escapeMode(EscapeMode.xhtml);
        
        // re-place jsoup converted xml characters
        replacementList.put("&lt;", "<");
        replacementList.put("&gt;", ">");
        replacementList.put("&amp;", "&");
        replacementList.put("&quot;", "\"");
        replacementList.put("&#xa0;", " "); // HACK, should use Parser.unescapeEntities(..., false); but the current caracter set does not like &nbsp; 
    }
    
    /**
     * throws IllegalArgumentException when no BLOB could be found
     * @param internalReference
     */
    public PdfPopulater(String internalReference) throws IllegalArgumentException{
        getPdfFromDb(internalReference);
        setFileByteArray(fileByteArray);
    }

    public PdfPopulater(byte[] byteArray) {
        setFileByteArray(byteArray);
    }

    public byte[] getFileByteArray() {
        return fileByteArray;
    }

    public ByteArrayOutputStream finish() {

        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            setFieldsReadonly();
        } catch (Exception e1) {
            // TASK Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            _pdfDocument.save(os);
        } catch (Exception e) {
            // TASK Auto-generated catch block
            e.printStackTrace();
        }
        try {
            _pdfDocument.close();
        } catch (Exception e) {
            // TASK Auto-generated catch block
            e.printStackTrace();
        }

        return os;
    }

    public void cleanup() {
        try {
            _pdfDocument.close();
        } catch (IOException e) {
            // TASK Auto-generated catch block
            log.info("error closing during cleanup: {}", e.getMessage());
            e.printStackTrace();
        }
        _pdfDocument = null;
    }

    public Boolean fillFields(ArrayList<FormElement> fieldList) {

        Boolean hasErrors = false;

        for (FormElement formElement : fieldList) {

            try {
                if (formElement.isString()) {

//                    setField(formElement.getName(), formElement.getString());
                    fitToField(formElement.getName(), formElement.getString());
                }
                if (formElement.isBoolean()) {
                    setCheckBox(formElement.getName(), formElement.getBoolean());
                }
            } catch (IOException e) {
                hasErrors = true;
            }

        }

        return !hasErrors;
    }

    public void setTableDebug(Boolean enabled) {
        tableDebug = enabled;
    }
    
    public Boolean fillTable(String name, List<TableRow> rows) {
        
        Boolean hasErrors = false;
        
        try {
            drawTable(name, rows);
        } catch (Exception e) {
            hasErrors = true;
        }
        
        return !hasErrors;
    }
    
    public Boolean fillTable(List<TableRow> rows, TableParameters params) {
        
        Boolean hasErrors = false;
        
        try {
            drawTable(rows, params);
        } catch (Exception e) {
            hasErrors = true;
        }
        
        return !hasErrors;
    }
    
    private void fitToField(String name, String value) throws IOException {

        PDDocumentCatalog docCatalog = _pdfDocument.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();
        PDField field = acroForm.getField(name);

        if (field != null) {

            float fontSize = 12;
            float leading = 1.5f * fontSize;

            PDRectangle mediabox = field.getWidgets().get(0).getRectangle();
            float margin = 0;
            float width = mediabox.getWidth() - 2 * margin;
            float startX = mediabox.getLowerLeftX() + margin;
            float startY = mediabox.getUpperRightY() - margin;

            if(field.getCOSObject().getDictionaryObject(COSName.DA) != null) {
                String da = field.getCOSObject().getDictionaryObject(COSName.DA).toString();
                Pattern pattern = Pattern.compile("^COSString\\{.+?(\\d+).+\\}$");
                Matcher matcher = pattern.matcher(da);
                if (matcher.find()) {
                    Integer size = Integer.parseInt(matcher.group(1));
                    if(size > 0) {
                        fontSize = size;
                    }
                }
            }
            
            COSDictionary dict = field.getCOSObject();
//            COSString defaultAppearance = (COSString) dict.getDictionaryObject(COSName.DA);
//            if (defaultAppearance != null) {
                dict.setString(COSName.DA, "/LSR " + fontSize + " Tf 0 g");
//            }
            List<String> lines = new ArrayList<String>();
            ArrayList<String> presetLines = new ArrayList<String>();
            
            //remove all html/xml tags
            value = clean(value, whiteFields, false);
            // remove non-printable characters, except CR or LF
            // see http://web.itu.edu.tr/sgunduz/courses/mikroisl/ascii.html
            value = value.trim().replaceAll("(?![\\x0A\\x0D])[\\x00-\\x1F]", "");
            
            for (String string : Arrays.asList(value.split("\n"))) {
                presetLines.add(string.replace("\r", "").replace("\n", ""));
            }
            
            for (String text : presetLines) {
                
                StringBuilder sb = new StringBuilder();
                for (char ch : text.toCharArray()) {
                    //TASK: use dynamic field font
                    sb.append(isEncodable(font, ch) ? ch : cnf);
                }
                
                String test =  sb.toString();
                
                int lastSpace = -1;
                while (text.length() > 0) {
                    int spaceIndex = text.indexOf(' ', lastSpace + 1);
                    if (spaceIndex < 0)
                        spaceIndex = text.length();
                    String subString = text.substring(0, spaceIndex);
                    String testString = test.substring(0, spaceIndex);
                    float size = fontSize * font.getStringWidth(testString) / 1000;
//                    System.out.printf("'%s' - %f of %f\n", subString, size, width);
                    if (size > width) {
                        if (lastSpace < 0)
                            lastSpace = spaceIndex;
                        subString = text.substring(0, lastSpace);
                        lines.add(subString);
                        text = text.substring(lastSpace).trim();
//                        System.out.printf("'%s' is line\n", subString);
                        lastSpace = -1;
                    } else if (spaceIndex == text.length()) {
                        lines.add(text);
//                        System.out.printf("'%s' is line\n", text);
                        text = "";
                    } else {
                        lastSpace = spaceIndex;
                    }
                }
            }

            String result = "";
            
            String br = "";
            
            for (String line : lines) {
                result += br + line;
                
                if(br.equals("")) {
                    br = "\n";
                }
                
            }

            field.setValue(result);

        } else {
            log.debug("No field found with name:" + name);
        }

    }
    
    public void setField(String name, String value) throws IOException {
        PDDocumentCatalog docCatalog = _pdfDocument.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();
        PDField field = acroForm.getField(name);
        if (field != null) {

            field.setValue(value);
        } else {
            log.warn("No field found with name:" + name);
        }
    }
    public void setCheckBox(String name, Boolean checked) throws IOException {
        PDDocumentCatalog docCatalog = _pdfDocument.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();
        PDField field = acroForm.getField(name);

        
        if (field != null && field instanceof PDCheckBox) {
            
            PDCheckBox cb = (PDCheckBox) field;
            
            if (checked) {
                cb.check();
            } else {
                cb.unCheck();
            }

        } 
    }

    @SuppressWarnings("rawtypes")
    public void printFields(){
        PDDocumentCatalog docCatalog = _pdfDocument.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();
        List fields = acroForm.getFields();
        Iterator fieldsIter = fields.iterator();

        System.out.println(new Integer(fields.size()).toString() + " top-level fields were found on the form");

        while (fieldsIter.hasNext()) {
            PDField field = (PDField) fieldsIter.next();
            try {
                processField(field, "|--", field.getPartialName());
            } catch (IOException e) {
                System.out.println("error processing field");
            }
        }
    }
    
   
    @SuppressWarnings("rawtypes")
    public void setFieldsReadonly() throws IOException {
        
        PDDocumentCatalog docCatalog = _pdfDocument.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();
        
        if(acroForm == null) {
            return;
        }
        
        List fields = acroForm.getFields();
        Iterator fieldsIter = fields.iterator();

        while (fieldsIter.hasNext()) {
            PDField field = (PDField) fieldsIter.next();
            field.setReadOnly(true);
        }
    }

    /**
     * used by FontProvider
     * @return
     */
    PDDocument getDocument() {
        return _pdfDocument;
    }
    
    @SuppressWarnings("rawtypes")
    private void processField(PDField field, String sLevel, String sParent) throws IOException {
        
        String outputString = sLevel + sParent + "." + field.getPartialName() + ",  type=" + field.getClass().getName();
        System.out.println(outputString);

    }

    private void drawTable(String name, List<TableRow> rows) throws IOException {
        
        PDDocumentCatalog docCatalog = _pdfDocument.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();
        PDField field = acroForm.getField(name);
        
        PDRectangle box = field.getWidgets().get(0).getRectangle();
        

        //Initialize Document
//        PDPage page = _pdfDocument.getPage(0);
        PDPage page = field.getWidgets().get(0).getPage();
        
        //Initialize table
        float tableWidth = box.getWidth();
        float yStart = box.getUpperRightY();
        float yStartNewPage = page.getMediaBox().getUpperRightY() - 20;

        //Set margins
        float margin = box.getLowerLeftX();
        float bottomMargin = box.getLowerLeftY();
        
        
        drawTable(rows, yStart, yStartNewPage, bottomMargin, tableWidth, margin, page);
        
    }
    
    private void drawTable(List<TableRow> rows, TableParameters params) throws IOException {
        
        PDRectangle box = _pdfDocument.getPage(0).getMediaBox();
        PDPage page = _pdfDocument.getPage(0);
        
        if(params == null) {
            params = new TableParameters();
        }
        
        log.debug("box {}", box);
        
        float tableWidth = box.getWidth() * params.width - (params.leftMarginOnly ? 0 : (2 * box.getWidth() * params.marginSides));
        float yStart = box.getUpperRightY() * (1f - params.marginTopFirstPage);
        float yStartNewPage = box.getUpperRightY() * (1f - params.marginTop);
        float margin = box.getLowerLeftX() + box.getWidth() * params.marginSides;
        float bottomMargin = box.getLowerLeftY() + box.getHeight() * params.marginBottom;

        drawTable(rows, yStart, yStartNewPage, bottomMargin, tableWidth, margin, page);
        
    }
    
    private void drawTable(List<TableRow> rows, float yStart, float yStartNewPage, float bottomMargin, float tableWidth, float margin, PDPage page) throws IOException {
        
        log.debug("yStart {}, yStartNewPage {}, bottomMargin {}, tableWidth {}, margin {}", yStart, yStartNewPage, bottomMargin, tableWidth, margin);
        
        BaseTable table = new BaseTable(yStart, yStartNewPage, bottomMargin, tableWidth, margin, _pdfDocument, page, true, true);
        table.setDrawDebug(tableDebug);
        
        for (TableRow r : rows) {
            
            Row<PDPage> row =table.createRow(r.getHeight());
            for (RowCell c : r.getCells()) {
                
                Cell<PDPage> cell;
                PDFont cFont = c.hasFont() ? c.getFont() : font;

                if(c.getImage() != null) {
                    // image cell handling
                    Image image = new Image(getImage(c.getImage()));
                    image.scaleByHeight(r.getHeight());
                    ImageCell<PDPage> imageCell = row.createImageCell(c.getRatioFraction(), image);
//                    imageCell.scaleToFit();
                    cell = imageCell;
                }else {
                    //default handling
                    
                    String in = c.getValue() != null ? c.getValue() : "";
                    StringBuilder sb = new StringBuilder();
                    for (char ch : in.toCharArray()) {
                        sb.append(isEncodable(cFont, ch) ? ch : cnf);
                    }
                    
                    cell = row.createCell(c.getRatioFraction(), clean(sb.toString(), whiteTable, true));
                }
                
                
                
                cell.setValign(VerticalAlignment.MIDDLE);
                cell.setAlign(c.getAlign());
                
                if(c.hasFont()) {
                    cell.setFont(c.getFont());
                }else {
                    cell.setFont(font);
                }
                
                if(c.hasFontSize()) {
                    cell.setFontSize(c.getFontSize());
                }
                
                if(c.hasSpacing()) {
                    cell.setLineSpacing(c.getSpacing());
                }
                
                if(c.hasColor()) {
                    cell.setTextColor(c.getColor());
                }
                
                if(c.hasBackground()) {
                    cell.setFillColor(c.getBackground());
                }
                
                if(c.isCompact()) {
                    cell.setWrappingFunction(null);
                    cell.setTopPadding(0.5f);
                    cell.setLeftPadding(2.5f);
                    cell.setRightPadding(2.5f);
                    cell.setBottomPadding(0f);
                }
                
                if(!c.hasBorder()) {
                    cell.setBorderStyle(null);
                }
            }
            
        }
        
        table.draw();
    }
    
    private String clean(String value, Whitelist list, boolean transformLinebreaks) {
        
        if(value == null) {
            return null;
        }
        
        String res = Jsoup.clean(transformLinebreaks ? value.replace("\r", "").replace("\n", "<br>") : value, "", whiteTable, settings);
        
        for (Entry<String, String> entry : replacementList.entrySet()) {
            res = res.replace(entry.getKey(), entry.getValue());
        }
        
        return res;
    }
    
    private void getPdfFromDb(String internalReference) {
        CMS_Config_Std std = CMS_Config_Std.getInstance();

        DBClass db = new DBClass();
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("PDF_BLOB");

        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cust_pdf_forms");

        db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cust_pdf_forms", "PDF_ACTIVE", "=", "1", "AND");
        db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cust_pdf_forms", "PDF_INTERNAL_REFERENCE", "=", "'" + internalReference + "'", "");

        db.DB_Data_Get.DB_Ordnen.DB_Ordnen("PDF_UPLOAD_DATE", "DESC");
        db.DB_Data_Get.DB_Limit.DB_LIMIT("1");

        // db.DB_Data_Get.DB_DEBUG_SQL_STRING();

        Item item = db.DB_Data_Get.DB_SEND_AND_GET_FIRST_ITEM();
        if(item != null && item.getItemProperty("PDF_BLOB") != null) {
            fileByteArray = (byte[]) item.getItemProperty("PDF_BLOB").getValue();
        }

    }
    
    private BufferedImage getImage(String internalReference) {
        
        DBClass db = new DBClass();
        db.DB_Data_Get.DB_Spalten.Standard.DB_set_Spalten_Einzeln("PDFI_BLOB");

        db.DB_Data_Get.DB_Tabellen.DB_set_Tabelle_Einzeln("cust_pdf_images");

        db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cust_pdf_images", "PDFI_ACTIVE", "=", "1", "AND");
        db.DB_Data_Get.DB_Filter.DB_WHERE_Allgemein("cust_pdf_images", "PDFI_INTERNAL_REFERENCE", "=", "'" + internalReference + "'", "");

        db.DB_Data_Get.DB_Ordnen.DB_Ordnen("PDFI_UPLOAD_DATE", "DESC");
        db.DB_Data_Get.DB_Limit.DB_LIMIT("1");

        // db.DB_Data_Get.DB_DEBUG_SQL_STRING();

        Item item = db.DB_Data_Get.DB_SEND_AND_GET_FIRST_ITEM();
        
        BufferedImage image = null;
        
        if(item != null && item.getItemProperty("PDFI_BLOB") != null) {
            byte[] blob = (byte[]) item.getItemProperty("PDFI_BLOB").getValue();
            try {
                image = ImageIO.read(new ByteArrayInputStream(blob));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return image;
    }
    
    private Boolean setFileByteArray(byte[] byteArray) throws IllegalArgumentException{
        
        if(byteArray == null) {
            throw new IllegalArgumentException();
        }
        
        InputStream is = new ByteArrayInputStream(byteArray);

        try {
            _pdfDocument = PDDocument.load(is);
            font = FontProvider.load(_pdfDocument, FONT.LiberationSansRegular);
            
            PDDocumentCatalog docCatalog = _pdfDocument.getDocumentCatalog();
            PDAcroForm acroForm = docCatalog.getAcroForm();
            if (acroForm != null) {
                acroForm.setNeedAppearances(true);
                PDResources res = acroForm.getDefaultResources();
                if (res == null) {
                    res = new PDResources();
                }
                res.add(font);
                res.put(COSName.getPDFName("LSR"), font);
                String defaultAppearanceString = "/LSR 0 Tf 0 g";
                acroForm.setDefaultAppearance(defaultAppearanceString);
                acroForm.setDefaultResources(res);
            }
            
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    // why is this even needed
    // font.hasGlyph is not intended for use with characers?
    private boolean isEncodable(PDFont fnt, char character) throws IOException {
        try {
            fnt.encode(Character.toString(character));
            return true;
        } catch (IllegalArgumentException iae) {
            return false;
        }
    }
    
    public static class FormElement {

        private String elementName = null;
        private String StringValue = null;
        private Boolean BooleanValue = null;

        public FormElement(String name, String value) {
            elementName = name;
            StringValue = value;
        }

        public FormElement(String name, Boolean value) {
            elementName = name;
            BooleanValue = value;
        }

        public FormElement(String name, Object value) {

            elementName = name;

            if (value != null) {

                Class c = value.getClass();

                if (c == String.class) {
                    StringValue = (String) value;
                } else if (c == Boolean.class) {
                    BooleanValue = (Boolean) value;
                }else {
                    StringValue = String.valueOf(value);
                }
            }

        }

        /* ************************* */

        public void setName(String name) {
            elementName = name;
        }

        public void setValue(String value) {
            clearValues();
            StringValue = value;
        }

        public void setValue(Boolean value) {
            clearValues();
            BooleanValue = value;
        }

        /* ************************* */

        public String getName() {
            return elementName;
        }

        public String getString() {
            return StringValue;
        }

        public Boolean getBoolean() {
            return BooleanValue;
        }

        /* ************************* */

        public Boolean isBoolean() {
            if (BooleanValue != null) {
                return true;
            }
            ;
            return false;
        }

        public Boolean isString() {
            return (StringValue != null);
        }

        /* ************************* */

        private void clearValues() {
            BooleanValue = null;
            StringValue = null;
        }

    };
    
    public static class TableRow {
        
        private float height;
        private float ratioSum = 0f;
        private ArrayList<RowCell> entries;
        
        public TableRow(float height) {
            entries = new ArrayList<RowCell>();
            setHeight(height);
        }
        
        public float getHeight() {
            return height;
        }
        public void setHeight(float height) {
            this.height = height;
        }
        public float getRatioSum() {
            return ratioSum;
        }
        
        public Boolean addEntry(RowCell cell) {
            
            ratioSum += cell.getRatio();
            cell.setParentRow(this);
            return entries.add(cell);
            
        }
        
        public Boolean removeEntry(RowCell cell) {
            return entries.remove(cell);
        }
        
        public ArrayList<RowCell> getCells() {
            return entries;
        }
        
        public RowCell getCell(Integer id) {
            return entries.get(id);
        }
        
    }
    
    public static class RowCell{
        
        private float ratio;
        private TableRow parentRow;
        
        private PDFont font;
        private Integer fontSize;
        private Float spacing;
        private java.awt.Color color;
        private java.awt.Color background;
        private String value; 
        
        private String imageRef;
        
        private HorizontalAlignment align = HorizontalAlignment.LEFT;
        
        private Boolean borderEnabled = true;
        private Boolean compact = false;
        
        /**
         * used for templating
         */
        public RowCell(){
            this(null, 0);
        }
        
        public RowCell(String value, float ratio) {
            
            if(value != null) {
                value = value.replaceAll("\\r\\n", "<br />").replaceAll("\\n", "<br />"); // linebreaks kill stream writers
            }
            
            setValue(value);
            setRatio(ratio);
        }
        
        public void setParentRow(TableRow parentRow) {
            this.parentRow = parentRow;
        }
        
        public float getRatioFraction() {
            
            if(parentRow == null) {
                return 100f;
            }
            
            return 100 / (parentRow.ratioSum / ratio);
        }
        
        // Font
        public Boolean hasFont() {
            return font != null;
        }
        public PDFont getFont() {
            return font;
        }
        public void setFont(PDFont font) {
            this.font = font;
        }
        
        // FontSize
        public Boolean hasFontSize() {
            return fontSize != null;
        }
        public Integer getFontSize() {
            return fontSize;
        }
        public void setFontSize(Integer fontSize) {
            this.fontSize = fontSize;
        }
        
        // spacing
        public boolean hasSpacing() {
            return spacing != null;
        }
        public float getSpacing() {
            return spacing;
        }

        public void setSpacing(float spacing) {
            this.spacing = spacing;
        }

        // Color
        public Boolean hasColor() {
            return color != null;
        }
        public java.awt.Color getColor() {
            return color;
        }
        public void setColor(java.awt.Color color) {
            this.color = color;
        }
        
        // Background
        public Boolean hasBackground() {
            return background != null;
        }
        public java.awt.Color getBackground() {
            return background;
        }
        public void setBackground(java.awt.Color background) {
            this.background = background;
        }
        
        // Image
        public String getImage() {
            return imageRef;
        }
        public void setImage(String imageRef) {
            this.imageRef = imageRef;
        }
        
        // Ratio
        public float getRatio() {
            return ratio;
        }
        public void setRatio(float ratio) {
            this.ratio = ratio;
        }
        
        // Value
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
        
        public HorizontalAlignment getAlign() {
            return align;
        }

        // Alignment
        public void setAlign(HorizontalAlignment align) {
            this.align = align;
        }

        // Border
        public Boolean hasBorder() {
            return borderEnabled;
        }
        public void setBorder(Boolean enabled) {
            this.borderEnabled = enabled;
        }
        
        // Compact
        public Boolean isCompact() {
            return compact;
        }
        public void setCompact(Boolean enabled) { 
            this.compact = enabled;
        }
        
        public RowCell with(RowCell format) {
            
            if(format.hasFont()) {
                setFont(format.getFont());
            }
            
            if(format.hasFontSize()) {
                setFontSize(format.getFontSize());
            }
            
            if(format.hasSpacing()) {
                setSpacing(format.getSpacing());
            }
            
            if(format.hasColor()) {
                setColor(format.getColor());
            }
            
            if(format.hasBackground()) {
                setBackground(format.getBackground());
            }
            
            setBorder(format.hasBorder());
            setCompact(format.isCompact());
            setAlign(format.getAlign());
            
            return this;
        }
    }
     public static class TableParameters{
         
         public float marginTopFirstPage = 0f;
         public float marginTop = 0f;
         public float marginSides = 0f;
         public float marginBottom = 0f;
         public float width = 1f;
         public boolean leftMarginOnly = false;
     }
}
