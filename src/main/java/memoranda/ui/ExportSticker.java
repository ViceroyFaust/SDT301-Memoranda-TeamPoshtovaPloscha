package memoranda.ui;

import memoranda.EventsManager;
import memoranda.util.Local;
import nu.xom.Element;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class ExportSticker {

    private final String name;
        
        /*public static Document _doc = null;
        static Element _root = null;

        static {
                CurrentStorage.get().openEventsManager();
                if (_doc == null) {
                        _root = new Element("eventslist");
/*                        _root.addNamespaceDeclaration("jnevents", NS_JNEVENTS);
                        _root.appendChild(
                                new Comment("This is JNotes 2 data file. Do not modify.")); */
/*                        _doc = new Document(_root);
                } else
                        _root = _doc.getRootElement();

        }*/

    public ExportSticker(String x) {
        this.name = remove1(x);
    }

    /**
     * Function to eliminate special chars from a string
     */
    public static String remove1(String input) {

        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";

        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
        String output = input;
        for (int i = 0; i < original.length(); i++) {

            output = output.replace(original.charAt(i), ascii.charAt(i));
        }
        return output;
    }

    public boolean export(String src) {
        boolean result = true;
        String fs = System.getProperty("file.separator");

        String contents = getSticker();
        try {
            File file = new File(this.name + "." + src);


            FileWriter fwrite = new FileWriter(file, true);

            fwrite.write(contents);

            fwrite.close();
            JOptionPane.showMessageDialog(null, Local.getString("Documento creado con exito en su carpeta Memoranda =D"));


        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, Local.getString("NO Logramos crear su documento =(..."));
        }


        return result;
    }

    public String getSticker() {
        Map stickers = EventsManager.getStickers();
        String result = "";
        String nl = System.getProperty("line.separator");
        for (Iterator i = stickers.keySet().iterator(); i.hasNext(); ) {
            String id = (String) i.next();
            result += ((Element) stickers.get(id)).getValue() + nl;
        }

        return result;
    }
        
        /*public static String getStickers() {
                String result ="";
                Elements els = _root.getChildElements("sticker");
                for (int i = 0; i < els.size(); i++) {
                        Element se = els.get(i);
                        m.put(se.getAttribute("id").getValue(), se.getValue());
                }
                return m;
        }*/


}