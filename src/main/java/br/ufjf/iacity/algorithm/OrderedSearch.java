package br.ufjf.iacity.algorithm;

import br.ufjf.iacity.algorithm.BreadthAndDepthSearch.SearchMode;
import br.ufjf.iacity.algorithm.base.SearchNode;
import br.ufjf.iacity.algorithm.base.SearchTree;
import br.ufjf.iacity.algorithm.base.transition.ITransition;
import br.ufjf.iacity.graph.CityGraph;
import br.ufjf.iacity.graph.CityNodeGraph;
import java.util.LinkedList;
import java.util.List;

public class OrderedSearch extends AlgorithmBase
{
    /**
     * 
     * @param cityGraph Grafo de cidades do problema
     * @param transition Transição que será aplicada para gerar novos nós de busca
     * @param startCityNode Nó do grafo de cidades que será o início do problema
     * @param endCityNode Nó do grafo de cidades que será o fim do problema
     * @throws IllegalArgumentException 
     */
    public OrderedSearch(CityGraph cityGraph, ITransition transition, CityNodeGraph startCityNode, CityNodeGraph endCityNode) throws IllegalArgumentException
    {
        if ((cityGraph == null) || (transition == null) || 
                (startCityNode == null) || (endCityNode == null))
        {
            throw new IllegalArgumentException("Não é permitido nenhum parâmetro nulo para o construtor da classe OrderedSearch");
        }
        
        // Define o grafo do problema e a regra de transição
        this.cityGraph = cityGraph;
        this.transition = transition;
        
        // Inicializa a árvore de busca vazia e define o nó final
        this.searchTree = new SearchTree();
        this.searchTree.setStartNode(new SearchNode(null, 0, startCityNode));
        this.searchTree.setEndNode(new SearchNode(null, 0, endCityNode));
        
        // Inicializa o estado da busca
        this.searchState = SearchState.Stopped;
    }
    
    /**
     *
     * Executa a busca BreadthAndDepthSearch sobre o grafo, usando as regras 
     * de transição definidas na inicialização da classe
     * 
     */
    public void search()
    {
        // Muda o estado para buscando
        this.searchState = SearchState.Searching;
        
        // Lista de nós abertos
        List<SearchNode> openedNodeList = new LinkedList<>();

        // Cria a lista de nós fechados
        List<SearchNode> closedNodeList = new LinkedList<>();
        
        // Adiciona o nó inicial na lista de abertos
        this.addInOpenedNodeList(SearchMode.Ordered, openedNodeList, searchTree.getStartNode());
        
        // Enquanto não for obtido sucesso ou fracasso, continue a busca
        while (!(searchState.equals(SearchState.Success) || searchState.equals(SearchState.Failed)))
        {
            // Verifica se a lista de abertos está vazia
            if(openedNodeList.isEmpty())
            {
                // Se sim, a busca terminou com fracasso
                this.searchState = SearchState.Failed;
            }
            else
            {
                /**
                 * 
                 * Senão, seleciona o próximo nó da lista de abertos de acordo 
                 * com o modo de busca usado
                 * 
                 */
                SearchNode openedSearchNode = this.getElementFromOpenedNodeListSearchNode(SearchMode.Ordered, openedNodeList);
                
                // Define primeiramente o nó atual como o pai do novo nó
                this.searchTree.setCurrentNode(openedSearchNode.getRootNode());
                
                // Adiciona o nó na árvore de busca
                this.searchTree.addChildToCurrentNode(openedSearchNode);
                
                // Altera o nó atual para o novo nó
                this.searchTree.setCurrentNode(openedSearchNode);
                
                // Define que o nó atual da árvore foi visitado
                this.searchTree.getCurrentNode().setVisited(true);
                
                /**
                 *
                 * Remove o elemento atual da lista de abertos e adiciona na
                 * lista de fechados
                 *
                 */
                this.removeFromOpenedNodeListSearchNode(openedNodeList, openedSearchNode);
                closedNodeList.add(openedSearchNode);
                
                // Verifica se o nó buscado foi encontrado
                if(openedSearchNode.getIdNode().equalsIgnoreCase(searchTree.getEndNode().getIdNode()))
                {
                    // A busca teve sucesso
                    this.searchState = SearchState.Success;
                        
                    // O nó final foi encontrado
                    this.searchTree.setEndNode(openedSearchNode);
                }
                else
                {   
                    /**
                     * 
                     * Aplica cada transição possível sobre o nó atual 
                     * da árvore de busca, adicionando na lista de abertos
                     * 
                     */
                    SearchNode nextSearchNode = this.transition.applyTransition(searchTree.getCurrentNode());
                    
                    // Enquanto há transição aplicável, continue
                    while(nextSearchNode != null)
                    {
                        /**
                         *
                         * Verifica se não há ancestral e se também já não está
                         * nas listas de abertos e fechados
                         *
                         */
                        if (!checkAncestralSearchNode(nextSearchNode) && !checkContainsSearchNode(openedNodeList, closedNodeList, nextSearchNode))
                        {
                            this.addInOpenedNodeList(SearchMode.Ordered, openedNodeList, nextSearchNode);
                        }
                        
                        // Aplica a próxima transição
                        nextSearchNode = this.transition.applyTransition(searchTree.getCurrentNode());
                    }

                    // Define que o nó atual da árvore foi expandido
                    this.searchTree.getCurrentNode().setExpanded(true);
                }
            }
        }
        
        System.out.println("Busca concluída com: " + searchState);
    }
}