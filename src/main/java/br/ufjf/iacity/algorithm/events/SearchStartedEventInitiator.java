package br.ufjf.iacity.algorithm.events;

import br.ufjf.iacity.algorithm.events.ISearchStartedEventListener;

/**
 *
 * @author Luis Augusto
 */
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
