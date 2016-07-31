package br.ufjf.iacity.algorithm;

import br.ufjf.iacity.algorithm.search.SearchNode;
import br.ufjf.iacity.algorithm.search.SearchTree;
import br.ufjf.iacity.algorithm.search.AlgorithmParameter;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Algoritmo de busca em Largura e em Profundidade. Métodos não informados de busca
 */
public class BreadthAndDepthSearch extends AbstractAlgorithmSearch
{
    // Modo de busca que será utilizado
    private SearchMode searchMode;

    /**
     * @param parameter Parâmetros de inicialização para o algoritmo de busca
     * 
     * @throws IllegalArgumentException 
     */
    public BreadthAndDepthSearch(AlgorithmParameter parameter) throws IllegalArgumentException
    {
        if ((parameter.getGraph() == null) || (parameter.getTransition() == null) || (parameter.getStartCityNode() == null)
                || (parameter.getEndCityNode() == null) || (parameter.getSearchMode() == null))
        {
            throw new IllegalArgumentException("Não é permitido nenhum parâmetro nulo para o construtor da classe BreadthAndDepthSearch");
        }
        
        // Define o grafo do problema e a regra de transição
        this.cityGraph = parameter.getGraph();
        this.transition = parameter.getTransition();
        
        // Inicializa a árvore de busca e define o nó inicial e final
        this.searchTree = new SearchTree();
        this.searchTree.setStartNode(new SearchNode(null, 0, parameter.getStartCityNode()));
        this.searchTree.setEndNode(new SearchNode(null, 0, parameter.getEndCityNode()));
        
        // Configura opções a serem usadas durante a busca
        SearchNode.setEnableDuplicate(parameter.isEnableDuplicated());
        SearchNode.setEnableCost(false);
        
        // Define o modo de busca que será utilizado (Largura ou Profundidade)
        this.searchMode = parameter.getSearchMode();
        
        // Define o estado atual da busca
        this.setSearchState(SearchState.Stopped);
    }
    
    /**
     * Executa a busca sobre o grafo, usando as regras 
     * de transição definidas na inicialização da classe
     */
    @Override
    public void search()
    {
        // Dispara evento de que a busca foi iniciada
        this.getSearchStartedEventInitiator().fireEvent(getSearchState());
        
        // Muda o estado para buscando
        this.setSearchState(SearchState.Searching);
        
        // Marca o tempo inicial
        long startSearchTime = System.nanoTime();
        
        // Lista de nós abertos
        List<SearchNode> openedNodeList;
        
        /**
         * Verifica o modo de busca que será utilizado e define qual será
         * a estrutura auxiliar que irá ser utilizada
         */
        if(searchMode.equals(SearchMode.Breadth))
        {
            // Busca em largura, cria uma fila
            openedNodeList = new LinkedList<>();
        }
        else
        {
            // Busca em profundidade, cria uma pilha
            openedNodeList = new Stack<>();
        }
        
        // Cria a lista de nós fechados
        List<SearchNode> closedNodeList = new LinkedList<>();
        
        // Adiciona o nó inicial na lista de abertos
        this.addInOpenedNodeList(searchMode, openedNodeList, getSearchTree().getStartNode());
        
        // Adiciona o nó na árvore de busca
        this.getSearchTree().addChildToCurrentNode(getSearchTree().getStartNode());
        
        // Enquanto não for obtido sucesso ou fracasso
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
                 * Senão, seleciona o próximo nó da lista de abertos de acordo 
                 * com o modo de busca usado
                 */
                SearchNode openedSearchNode = this.getElementFromOpenedNodeList(searchMode, openedNodeList);
                
                // Altera o nó atual para o novo nó
                this.getSearchTree().setCurrentNode(openedSearchNode);
                
                // Define que o novo nó atual da árvore foi visitado
                this.getSearchTree().getCurrentNode().setVisited(true);
                
                /**
                 * Remove o elemento atual da lista de abertos e adiciona na
                 * lista de fechados
                 */
                closedNodeList.add(removeFromOpenedNodeList(searchMode, openedNodeList));
                
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
                    // Lista que guardará temporariamente as transições aplicadas
                    List<SearchNode> tmpList = new LinkedList<>();
                    
                    /**
                     * Aplica cada transição possível sobre o nó atual 
                     * da árvore de busca, adicionando na lista de abertos
                     */
                    SearchNode currentNode = getSearchTree().getCurrentNode();
                    SearchNode nextSearchNode = this.transition.applyTransition(currentNode);
                    
                    // Enquanto há transição aplicável
                    while(nextSearchNode != null)
                    {
                        /**
                         * Verifica se não há ancestral e se também já não está
                         * nas listas de abertos e fechados
                         */
                        if (!checkAncestral(nextSearchNode))
                        {
                            // Verifica se podas serão permitidas (enableDuplicated)
                            if(SearchNode.isEnableDuplicate())
                            {
                                tmpList.add(nextSearchNode);
                            }
                            else
                            {
                                /**
                                 * Caso podas sejam permitidas, verifica se o nó
                                 * já não está nas listas
                                 */
                                if (!checkContains(closedNodeList, nextSearchNode) && 
                                        !checkContains(openedNodeList, nextSearchNode)) 
                                {
                                    tmpList.add(nextSearchNode);
                                }
                            }
                        }
                        
                        // Aplica a próxima transição
                        nextSearchNode = this.transition.applyTransition(currentNode);
                    }
                    
                    // Verifica se houve expansão do estado
                    if(!tmpList.isEmpty())
                    {
                        /**
                         * Adiciona os novos nós na lista de abertos na ordem
                         * correta para manter a prioridade
                         */
                        if (searchMode.equals(SearchMode.Breadth)) 
                        {
                            // Se busca em largura, adicionar na mesma ordem (Fila)
                            for (int i = 0; i < tmpList.size(); i++) 
                            {
                                nextSearchNode = tmpList.get(i);

                                // Adiciona na lista de abertos
                                this.addInOpenedNodeList(searchMode, openedNodeList, nextSearchNode);

                                // Adiciona o nó na árvore de busca
                                this.getSearchTree().addChildToCurrentNode(nextSearchNode);
                            }
                        } 
                        else 
                        {
                            // Se busca em profundidade, adicionar na ordem inversa (Pilha)
                            for (int i = (tmpList.size() - 1); i >= 0; i--) 
                            {
                                nextSearchNode = tmpList.get(i);

                                // Adiciona na lista de abertos
                                this.addInOpenedNodeList(searchMode, openedNodeList, nextSearchNode);

                                // Adiciona o nó na árvore de busca
                                this.getSearchTree().addChildToCurrentNode(nextSearchNode);
                            }
                        }

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
