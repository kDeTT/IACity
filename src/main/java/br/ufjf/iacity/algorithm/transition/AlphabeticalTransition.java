package br.ufjf.iacity.algorithm.transition;

import br.ufjf.iacity.algorithm.search.SearchNode;
import br.ufjf.iacity.graph.CityNodeAdjacency;
import br.ufjf.iacity.graph.CityNodeGraph;
import java.util.Iterator;

/**
 * Estratégia de controle para os algoritmos de busca usando a ordem alfabética
 * do nome das cidades
 */
public class AlphabeticalTransition implements ITransition
{
    @Override
    public SearchNode applyTransition(SearchNode currentSearchNode) 
    {
        if((currentSearchNode != null) && (currentSearchNode.getCityNodeGraph().getAdjacencyCount() > 0))
        {
            CityNodeGraph currentCityNode = currentSearchNode.getCityNodeGraph();
            Iterator<CityNodeAdjacency> childCityNodeIterator = currentCityNode.getAdjacencyIterator();
            
            CityNodeAdjacency tmpAdj;
            CityNodeAdjacency nextCityNodeAdj = null;
            
            /**
             * Escolhe o primeiro nó ainda não aberto para ser comparado 
             * alfabeticamente com o restante
             */
            while(childCityNodeIterator.hasNext())
            {
                tmpAdj = childCityNodeIterator.next();
                
                if(!tmpAdj.isVisited())
                {
                    nextCityNodeAdj = tmpAdj;
                    break;
                }
            }
            
            // Verifica se uma adjacência foi escolhida
            if (nextCityNodeAdj != null) 
            {
                // Compara alfabéticamente o nó adjacente escolhido com o restante
                while (childCityNodeIterator.hasNext()) 
                {
                    tmpAdj = childCityNodeIterator.next();

                    // Verifica se o nó adjacente ainda não foi aberto e a ordem alfabética
                    if ((!tmpAdj.isVisited()) && nextCityNodeAdj.getAdjNode().getIdNode().compareTo(tmpAdj.getAdjNode().getIdNode()) > 0) 
                    {
                        nextCityNodeAdj = tmpAdj;
                    }
                }

                // Marca que a adjacência foi visitada
                nextCityNodeAdj.setVisited(true);
                
                // Novo nó de busca
                SearchNode newSearchNode = new SearchNode(
                                currentSearchNode, // Nó atual é o pai do novo nó
                                (currentSearchNode.getTreeLevel() + 1), // Define o nível da árvore com base no nível do pai
                                nextCityNodeAdj.getAdjNode());
                
                // Se o custo está habilitado, define o custo do nó de busca
                if(SearchNode.isEnableCost())
                {
                    newSearchNode.setCost(nextCityNodeAdj.getCost());
                }
                
                return newSearchNode;
            }
        }
        
        // Não foi possível aplicar nenhuma transição
        return null;
    }
}
