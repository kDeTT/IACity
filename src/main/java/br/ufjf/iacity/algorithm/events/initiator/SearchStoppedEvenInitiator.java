package br.ufjf.iacity.algorithm.events.initiator;

import br.ufjf.iacity.algorithm.events.ISearchStoppedEventListener;

/**
 *
 * @author Luis Augusto
 */
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
