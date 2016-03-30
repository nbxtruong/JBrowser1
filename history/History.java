package history;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class History<E> extends Observable {

    private List<E> list = new ArrayList<E>();

    private int index = -1;
    
    public History() {
        super();
    }
    
    private void notifyObs() {
        setChanged();
        notifyObservers();    	
    }
    
    public void add(E e) {
        if (index < list.size() - 1) {
            for (int i = list.size() - 1; i > index; i--) {
                list.remove(i);
            }
        }
        list.add(e);
        index++;
        notifyObs();
    }

    public E get() {
        return list.get(index);
    }

    public E forward() {
        if (index < list.size() - 1) {
            index++;
        }
        notifyObs();
        return list.get(index);
    }

    public E backward() {
        if (index > 0) {
            index--;
        }
        notifyObs();
        return list.get(index);
    }

    public boolean atFirst() {
        return index == 0;
    }

    public boolean atLast() {
        return index == list.size() - 1;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (E e : list) {
            sb.append(e.toString() + "\n");
        }
        return sb.toString();
    }
}
