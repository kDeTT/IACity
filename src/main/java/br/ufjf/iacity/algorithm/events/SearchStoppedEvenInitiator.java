package br.ufjf.iacity.algorithm.events;

public class SearchStoppedEvenInitiator extends AbstractEventInitiator<ISearchStoppedEventListener>
{
    @Override
    public void fireEvent(Object event) 
    {
        for(ISearchStoppedEventListener listener : getListenerList())
        {
            listener.searchStoppedEvent(event);
        }
    }
}
