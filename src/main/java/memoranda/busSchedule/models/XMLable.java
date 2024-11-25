package memoranda.busSchedule.models;

import nu.xom.Element;

public interface XMLable {
    Element toXML();

    void fromXML(Element xlmElement);
}
