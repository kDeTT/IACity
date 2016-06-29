package br.ufjf.iacity.algorithm;

import br.ufjf.iacity.algorithm.base.SearchNode;
import br.ufjf.iacity.algorithm.base.SearchTree;
import br.ufjf.iacity.algorithm.base.transition.ITransition;
import br.ufjf.iacity.graph.CityGraph;
import br.ufjf.iacity.graph.CityNodeGraph;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class BreadthAndDepthSearch extends AlgorithmBase
{
    public enum SearchMode { Breadth, Depth, Ordered }
    
    private SearchMode searchMode;

    /**
     * 
     * @param cityGraph Grafo de cidades do problema
     * @param transition Transição que será aplicada para gerar novos nós de busca
     * @param startCityNode Nó do grafo de cidades que será o início do problema
     * @param endCityNode Nó do grafo de cidades que será o fim do problema
     * @param searchMode Tipo de busca que será utilizado, sendo em largura (Breadth) ou profundidade (Depth)
     * @param enableDuplicated Habilita/desabilita a possiblidade de adicionar estados duplicados na árvore de busca
     * @throws IllegalArgumentException 
     */
    public BreadthAndDepthSearch(CityGraph cityGraph, ITransition transition, CityNodeGraph startCityNode, CityNodeGraph endCityNode, SearchMode searchMode, boolean enableDuplicated) throws IllegalArgumentException
    {
        if ((cityGraph == null) || (transition == null) || (startCityNode == null)
                || (endCityNode == null) || (searchMode == null))
        {
            throw new IllegalArgumentException("Não é permitido nenhum parâmetro nulo para o construtor da classe BreadthAndDepthSearch");
        }
        
        // Define o grafo do problema e a regra de transição
        this.cityGraph = cityGraph;
        this.transition = transition;
        
        // Inicializa a árvore de busca vazia e define o nó final
        this.searchTree = new SearchTree();
        this.searchTree.setStartNode(new SearchNode(null, 0, startCityNode));
        this.searchTree.setEndNode(new SearchNode(null, 0, endCityNode));
        
        // Habilita/Desabilita opções a serem usadas durante a busca
        SearchNode.setEnableDuplicate(enableDuplicated);
        SearchNode.setEnableCost(false);
        
        // Define o modo de busca que será utilizado (Largura ou Profundidade)
        this.searchMode = searchMode;
        
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
        List<SearchNode> openedNodeList = null;
        
        /**
         * 
         * Verifica o modo de busca que será utilizado e define qual será
         * a estrutura auxiliar que irá ser utilizada
         * 
         */
        if(searchMode.equals(SearchMode.Breadth))
        {
            // Caso busca em largura, define uma fila
            openedNodeList = new LinkedList<>();
        }
        else if(searchMode.equals(SearchMode.Depth))
        {
            // Caso busca em profundidade, define uma pilha
            openedNodeList = new Stack<>();
        }
        
        // Cria a lista de nós fechados
        List<SearchNode> closedNodeList = new LinkedList<>();
        
        // Adiciona o nó inicial na lista de abertos
        this.addInOpenedNodeList(searchMode, openedNodeList, searchTree.getStartNode());
        
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
                SearchNode openedSearchNode = this.getElementFromOpenedNodeList(searchMode, openedNodeList);
                
                if(searchMode.equals(SearchMode.Breadth))
                {
                    // Define primeiramente o nó atual como o pai do novo nó
                    this.searchTree.setCurrentNode(openedSearchNode.getRootNode());
                }
                
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
                closedNodeList.add(removeFromOpenedNodeList(searchMode, openedNodeList));
                
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
                    // Lista que guardará temporariamente as transições aplicadas
                    List<SearchNode> tmpList = new LinkedList<>();
                    
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
                        if (!checkAncestral(nextSearchNode) && !checkContains(openedNodeList, closedNodeList, nextSearchNode))
                        {
                            tmpList.add(nextSearchNode);
                        }
                        
                        // Aplica a próxima transição
                        nextSearchNode = this.transition.applyTransition(searchTree.getCurrentNode());
                    }
                    
                    /**
                     * 
                     * Adiciona os novos nós de busca na lista de abertos,
                     * criados a partir das transições aplicadas, na ordem 
                     * correta para manter a prioridade
                     * 
                     */
                    if (searchMode.equals(SearchMode.Breadth))
                    {
                        // Se busca em largura, adicionar na mesma ordem (Fila)
                        for (int i = 0; i < tmpList.size(); i++) 
                        {
                            this.addInOpenedNodeList(searchMode, openedNodeList, tmpList.get(i));
                        }
                    } 
                    else 
                    {
                        // Se busca em profundidade, adicionar na ordem inversa (Pilha
                        for (int i = (tmpList.size() - 1); i >= 0; i--) 
                        {
                            this.addInOpenedNodeList(searchMode, openedNodeList, tmpList.get(i));
                        }
                    }
                    
                    // Define que o nó atual da árvore foi expandido
                    this.searchTree.getCurrentNode().setExpanded(true);
                }
            }
        }
        
        System.out.println("Busca concluída com: " + searchState);
    }
}
