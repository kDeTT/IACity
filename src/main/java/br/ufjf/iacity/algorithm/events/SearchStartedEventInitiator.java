package br.ufjf.iacity.algorithm.events;

public class SearchStartedEventInitiator extends AbstractEventInitiator<ISearchStartedEventListener>
{
    @Override
    public void fireEvent(Object event) 
    {
        for(ISearchStartedEventListener listener : getListenerList())
        {
            listener.searchStartedEvent(event);
        }
    }
}
