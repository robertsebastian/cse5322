package classdiagrameditor;

public interface ElementObserver {
    void notifyElementChanged(Element e);
    void reregisterObserver(Element e);
}