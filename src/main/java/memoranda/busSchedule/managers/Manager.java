package memoranda.busSchedule.managers;

import java.util.List;

public interface Manager<T> {
    List<T> getAll();
    void add(T item);
    void save(String filename) throws Exception;
    void load(String filename) throws Exception;
}
