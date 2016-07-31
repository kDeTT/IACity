package br.ufjf.iacity.algorithm.events;

public class SearchStatusChangedEventInitiator extends AbstractEventInitiator<ISearchStatusChangedEventListener>
{
    @Override
    public void fireEvent(Object event) 
    {
        for(ISearchStatusChangedEventListener listener : getListenerList())
        {
            listener.searchStatusChangedEvent(event);
        }
    }
}
