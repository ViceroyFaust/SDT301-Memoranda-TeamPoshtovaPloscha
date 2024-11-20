package memoranda.busSchedule.managers;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;

import java.io.FileOutputStream;
import java.util.List;

public class GenericManager<T> implements Manager<T>{
    List<T> items;

    public GenericManager(List<T> items) {
        this.items = items;
    }
    public GenericManager(){}

    @Override
    public List<T> getAll() {
        return items;
    }

    @Override
    public void add(T item) {
        items.add(item);
    }

    @Override
    public void save(String filename) throws Exception {
        Element root = XMLUtils.convertListToXML(items);
        Document doc = new Document(root);
        FileOutputStream out = new FileOutputStream(filename);
        Serializer serializer = new Serializer(out, "UTF-8");
        serializer.setIndent(3);
        serializer.write(doc);
        out.close();
    }

    @Override
    public void load(String filename) throws Exception {

    }
}
