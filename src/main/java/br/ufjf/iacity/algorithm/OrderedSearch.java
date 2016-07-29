package br.ufjf.iacity.algorithm;

import br.ufjf.iacity.algorithm.search.SearchNode;
import br.ufjf.iacity.algorithm.search.SearchTree;
import br.ufjf.iacity.algorithm.search.AlgorithmParameter;
import br.ufjf.iacity.helper.sort.QuickSort;
import br.ufjf.iacity.helper.sort.QuickSort.SortType;
import java.util.LinkedList;
import java.util.List;

public class OrderedSearch extends AbstractAlgorithmSearch
{
    /**
     * 
     * @param parameter Parâmetros de inicialização para o algoritmo de busca
     * 
     * @throws IllegalArgumentException 
     */
    public OrderedSearch(AlgorithmParameter parameter) throws IllegalArgumentException
    {
        if ((parameter.getGraph() == null) || (parameter.getTransition() == null) || 
                (parameter.getStartCityNode() == null) || (parameter.getEndCityNode() == null))
        {
            throw new IllegalArgumentException("Não é permitido nenhum parâmetro nulo para o construtor da classe OrderedSearch");
        }
        
        // Define o grafo do problema e a regra de transição
        this.cityGraph = parameter.getGraph();
        this.transition = parameter.getTransition();
        
        // Inicializa a árvore de busca vazia e define o nó final
        this.searchTree = new SearchTree();
        this.searchTree.setStartNode(new SearchNode(null, 0, parameter.getStartCityNode()));
        this.searchTree.setEndNode(new SearchNode(null, 0, parameter.getEndCityNode()));
        
        // Habilita/Desabilita opções a serem usadas durante a busca
        SearchNode.setEnableDuplicate(parameter.isEnableDuplicated());
        SearchNode.setEnableCost(true);
        
        // Inicializa o estado da busca
        this.setSearchState(SearchState.Stopped);
    }
    
    /**
     *
     * Executa a busca BreadthAndDepthSearch sobre o grafo, usando as regras 
     * de transição definidas na inicialização da classe
     * 
     */
    @Override
    public void search()
    {
        // Dispara evento que a busca foi iniciada
        this.getSearchStartedEventInitiator().fireEvent(getSearchState());
        
        // Marca o tempo inicial
        long startSearchTime = System.nanoTime();
        
        // Muda o estado para buscando
        this.setSearchState(SearchState.Searching);
        
        // Lista de nós abertos
        List<SearchNode> openedNodeList = new LinkedList<>();

        // Cria a lista de nós fechados
        List<SearchNode> closedNodeList = new LinkedList<>();
        
        // Adiciona o nó inicial na lista de abertos
        this.addInOpenedNodeList(SearchMode.Ordered, openedNodeList, getSearchTree().getStartNode());
        
        // Adiciona o nó na árvore de busca
        this.getSearchTree().addChildToCurrentNode(getSearchTree().getStartNode());
        
        // Enquanto não for obtido sucesso ou fracasso, continue a busca
        while (!(getSearchState().equals(SearchState.Success) || getSearchState().equals(SearchState.Failed)))
        {
            // Verifica se a lista de abertos está vazia
            if(openedNodeList.isEmpty())
            {
                // Se sim, a busca terminou com fracasso
                this.setSearchState(SearchState.Failed);
            }
            else
            {
                /**
                 * 
                 * Senão, seleciona o próximo nó da lista de abertos de acordo 
                 * com o modo de busca usado
                 * 
                 */
                SearchNode openedSearchNode = this.getElementFromOpenedNodeList(SearchMode.Ordered, openedNodeList);
                
                // Altera o nó atual para o novo nó
                this.getSearchTree().setCurrentNode(openedSearchNode);
                
                // Define que o nó atual da árvore foi visitado
                this.getSearchTree().getCurrentNode().setVisited(true);
                
                /**
                 *
                 * Remove o elemento atual da lista de abertos e adiciona na
                 * lista de fechados
                 *
                 */
                closedNodeList.add(removeFromOpenedNodeList(SearchMode.Ordered, openedNodeList));
                
                // Verifica se o nó buscado foi encontrado
                if(openedSearchNode.getIdNode().equalsIgnoreCase(getSearchTree().getEndNode().getIdNode()))
                {
                    // A busca teve sucesso
                    this.setSearchState(SearchState.Success);
                        
                    // O nó final foi encontrado
                    this.getSearchTree().setEndNode(openedSearchNode);
                }
                else
                {   
                    /**
                     * 
                     * Aplica cada transição possível sobre o nó atual 
                     * da árvore de busca, adicionando na lista de abertos
                     * 
                     */
                    boolean isExpanded = false;
                    SearchNode currentNode = getSearchTree().getCurrentNode();
                    SearchNode nextSearchNode = this.transition.applyTransition(currentNode);
                    
                    // Enquanto há transição aplicável, continue
                    while(nextSearchNode != null)
                    {
                        // Define o custo do estado da raíz até o nó atual
                        nextSearchNode.setCost(currentNode.getCost() + nextSearchNode.getCost());
                        
                        /**
                         *
                         * Verifica se não há ancestral e se também já não está
                         * nas listas de abertos e fechados
                         *
                         */
                        if (!checkAncestral(nextSearchNode))
                        {
                            // Verifica se podas serão permitidas (enableDuplicated)
                            if(!SearchNode.isEnableDuplicate())
                            {
                                /**
                                 * Verifica se não está na lista de fechados.
                                 * Caso esteja, o novo estado não deve ser adicionado
                                 */
                                if(!checkContains(closedNodeList, nextSearchNode))
                                {
                                    // Verifica se está na lista de abertos
                                    if(checkContains(openedNodeList, nextSearchNode))
                                    {
                                        // Busca pelo estado igual na lista de abertos
                                        for(SearchNode openedNode : openedNodeList)
                                        {
                                            if(openedNode.getIdNode().equalsIgnoreCase(nextSearchNode.getIdNode()))
                                            {
                                                /**
                                                 * Compara o custo dos estados
                                                 * para verificar qual deve ser mantido
                                                 * na lista de abertos
                                                 */
                                                if(nextSearchNode.compareTo(openedNode) < 0)
                                                {
                                                    /**
                                                     * Caso o novo estado tenha um custo menor que
                                                     * o estado que já está na lista de abertos,
                                                     * deve-se remover o estado que já está na lista
                                                     * e adicionar o novo
                                                     */
                                                    openedNodeList.remove(openedNode);
                                                    
                                                    // Adiciona na lista de abertos
                                                    this.addInOpenedNodeList(SearchMode.Ordered, openedNodeList, nextSearchNode);
                                                    
                                                    // Remove o nó antigo da árvore de busca
                                                    this.getSearchTree().removeChildNode(openedNode);
                                                    
                                                    // Adiciona o novo nó na árvore de busca
                                                    this.getSearchTree().addChildToCurrentNode(nextSearchNode);
                                                    
                                                    isExpanded = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        // Caso não esteja, adicionar o novo estado imediatamente
                                        this.addInOpenedNodeList(SearchMode.Ordered, openedNodeList, nextSearchNode);
                                        
                                        // Adiciona o nó na árvore de busca
                                        this.getSearchTree().addChildToCurrentNode(nextSearchNode);
                                        
                                        isExpanded = true;
                                    }
                                }
                            }
                            else
                            {
                                /**
                                 * Caso podas não sejam permitidas, adicionar
                                 * o novo estado imediatamente
                                 */
                                this.addInOpenedNodeList(SearchMode.Ordered, openedNodeList, nextSearchNode);
                                
                                // Adiciona o nó na árvore de busca
                                this.getSearchTree().addChildToCurrentNode(nextSearchNode);
                                
                                isExpanded = true;
                            }
                        }
                        
                        // Aplica a próxima transição
                        nextSearchNode = this.transition.applyTransition(currentNode);
                    }
                    
                    // Verfica se houve expansão do estado
                    if(isExpanded)
                    {
                        // Ordena a lista de abertos usando o custo dos nós
                        openedNodeList = QuickSort.sort(openedNodeList, SortType.Cost);

                        // Define que o nó atual da árvore foi expandido
                        this.getSearchTree().getCurrentNode().setExpanded(true);
                    }
                }
            }
        }
        
        // Marca o tempo final
        long endSearchTime = System.nanoTime();
        
        // Tempo total de execução em milisegundos
        double executionTime = ((endSearchTime - startSearchTime) / 1e6);
        
        // Define o tempo total de execução
        this.setExecutionTime(executionTime);
        
        // Dispara evento que a busca foi terminada
        this.getSearchStoppedEvenInitiator().fireEvent(getSearchState());
    }
}