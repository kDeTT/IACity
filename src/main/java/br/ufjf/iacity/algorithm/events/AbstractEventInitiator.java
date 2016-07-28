package br.ufjf.iacity.algorithm.events;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEventInitiator<T> implements IEventInitiator
{
    private final List<T> listenerList;
     
     public AbstractEventInitiator()
     {
         this.listenerList = new ArrayList<>();
     }
     
     /**
     * @return the listenerList
     */
    protected List<T> getListenerList() {
        return listenerList;
    }
     
    public void addListener(T listener) {
        this.getListenerList().add(listener);
    }

    public void removeListener(T listener) {
        this.getListenerList().remove(listener);
    }
}
