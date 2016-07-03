package br.ufjf.iacity.algorithm.events.initiator;

import br.ufjf.iacity.algorithm.events.ISearchStatusChangedEventListener;

/**
 *
 * @author Luis Augusto
 */
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
