package archenoah.lib.vaadin.Language.i18n;

/**
 * CaptionBean helper class
 * <pre>
 *   private final static class caption extends I18nCB{
 *       static I18nCB name_of_caption = set();
 *   }
 * </pre> 
 */
public class I18nCB {
    /**
     * use to initialized caption (reflected fields can not be null for I18nManager!)
     * @return
     */
    protected static I18nCB set() {
        return new I18nCB();
    }
}
