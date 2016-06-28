package br.ufjf.iacity.algorithm;

import br.ufjf.iacity.algorithm.base.SearchNode;
import br.ufjf.iacity.algorithm.base.SearchTree;
import br.ufjf.iacity.algorithm.base.transition.ITransition;
import br.ufjf.iacity.graph.CityGraph;
import br.ufjf.iacity.graph.CityNodeGraph;

public class BacktrackingSearch extends AlgorithmBase
{   
    /**
     * 
     * @param cityGraph Grafo de cidades do problema
     * @param transition Transição que será aplicada para gerar novos nós de busca
     * @param startCityNode Nó do grafo de cidades que será o início do problema
     * @param endCityNode Nó do grafo de cidades que será o fim do problema
     * @param enableDuplicated Habilita/desabilita a possiblidade de adicionar estados duplicados na árvore de busca
     * @throws IllegalArgumentException 
     */
    public BacktrackingSearch(CityGraph cityGraph, ITransition transition, CityNodeGraph startCityNode, CityNodeGraph endCityNode, boolean enableDuplicated) throws IllegalArgumentException
    {
        if ((cityGraph == null) || (transition == null)
                || (startCityNode == null) || (endCityNode == null)) 
        {
            throw new IllegalArgumentException("Não é permitido nenhum parâmetro nulo para o construtor da classe BacktrackingSearch");
        }
        
        // Define o grafo do problema e a regra de transição
        this.cityGraph = cityGraph;
        this.transition = transition;
        
        // Inicializa a árvore de busca com o primeiro nó e define o nó final
        this.searchTree = new SearchTree(new SearchNode(null, 0, startCityNode));
        this.searchTree.setEndNode(new SearchNode(null, 0, endCityNode));
        
        // Habilita/Desabilita opções a serem usadas durante a busca
        SearchNode.setEnableDuplicate(enableDuplicated);
        SearchNode.setEnableCost(false);
        
        // Inicializa o estado da busca
        this.searchState = SearchState.Stopped;
    }
    
    /**
     * 
     * Executa a busca BacktrackingSearch sobre o grafo, usando as regras de transição
     * definidas na inicialização da classe
     * 
     */
    public void search()
    {
        // Muda o estado para buscando
        this.searchState = SearchState.Searching;
        
        // Define que o nó inicial foi visitado
        this.searchTree.getCurrentNode().setVisited(true);
        
        // Enquanto não for obtido sucesso ou fracasso, continua a busca
        while (!(searchState.equals(SearchState.Success) || searchState.equals(SearchState.Failed)))
        {
            // Aplica a transição sobre o nó atual da árvore de busca
            CityNodeGraph nextCityNodeGraph = transition.applyTransition(searchTree.getCurrentNode().getCityNodeGraph());
            
            /**
             * 
             * Verifica se a transição foi aplicada.
             * 
             * Se sim, existe operador aplicável sobre o nó atual,
             * caso contrário, não existe mais operadores aplicáveis.
             * 
             */
            if(nextCityNodeGraph != null)
            {
                // Verifica se não há ancestral para o nó
                if(!checkAncestral(nextCityNodeGraph))
                {
                    // Cria e adiciona o novo nó na árvore de busca
                    SearchNode newSearchNode = new SearchNode(searchTree.getCurrentNode(),
                            (searchTree.getCurrentNode().getTreeLevel() + 1),
                            nextCityNodeGraph);
                    this.searchTree.addChildToCurrentNode(newSearchNode);
                    
                    // Define que o nó atual foi expandido
                    this.searchTree.getCurrentNode().setExpanded(true);
                    
                    // Altera o nó atual para o novo nó criado
                    this.searchTree.setCurrentNode(newSearchNode);
                    
                    // Define que o nó atual da árvore foi visitado
                    this.searchTree.getCurrentNode().setVisited(true);

                    // Verifica se o nó é o nó buscado
                    if (nextCityNodeGraph.getIdNode().equalsIgnoreCase(searchTree.getEndNode().getIdNode())) 
                    {
                        // A busca teve sucesso
                        searchState = SearchState.Success;
                        
                        // O novo nó é o nó final
                        searchTree.setEndNode(newSearchNode);
                    }
                }
            }
            else
            {
                /**
                 * 
                 * Verifica se o nó atual é o nó inicial, caso seja, significa
                 * que retornei ao ponto inicial e não há mais operadores aplicáveis
                 * e, neste caso, a solução não foi encontrada
                 * 
                 */
                if(searchTree.getCurrentNode().getIdNode().equalsIgnoreCase(searchTree.getRootNode().getIdNode()))
                {
                    searchState = SearchState.Failed;
                }
                else
                {
                    // Retorna para o pai do nó atual
                    this.searchTree.setCurrentNode(searchTree.getCurrentNode().getRootNode());
                }
            }
        }
        
        System.out.println("Busca concluída com: " + searchState);
    }
}
