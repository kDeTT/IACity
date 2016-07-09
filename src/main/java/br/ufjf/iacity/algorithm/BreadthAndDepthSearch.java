package br.ufjf.iacity.algorithm;

import br.ufjf.iacity.algorithm.helper.SearchNode;
import br.ufjf.iacity.algorithm.helper.SearchTree;
import br.ufjf.iacity.helper.algorithm.AlgorithmParameter;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class BreadthAndDepthSearch extends AbstractAlgorithmSearch
{
    private SearchMode searchMode;

    /**
     * 
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
        
        // Inicializa a árvore de busca vazia e define o nó final
        this.searchTree = new SearchTree();
        this.searchTree.setStartNode(new SearchNode(null, 0, parameter.getStartCityNode()));
        this.searchTree.setEndNode(new SearchNode(null, 0, parameter.getEndCityNode()));
        
        // Habilita/Desabilita opções a serem usadas durante a busca
        SearchNode.setEnableDuplicate(parameter.isEnableDuplicated());
        SearchNode.setEnableCost(false);
        
        // Define o modo de busca que será utilizado (Largura ou Profundidade)
        this.searchMode = parameter.getSearchMode();
        
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
        
        // Muda o estado para buscando
        this.setSearchState(SearchState.Searching);
        
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
            // Busca em largura, define uma fila
            openedNodeList = new LinkedList<>();
        }
        else
        {
            // Senão busca em profundidade, define uma pilha
            openedNodeList = new Stack<>();
        }
        
        // Cria a lista de nós fechados
        List<SearchNode> closedNodeList = new LinkedList<>();
        
        // Adiciona o nó inicial na lista de abertos
        this.addInOpenedNodeList(searchMode, openedNodeList, getSearchTree().getStartNode());
        
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
                SearchNode openedSearchNode = this.getElementFromOpenedNodeList(searchMode, openedNodeList);
                
                if(searchMode.equals(SearchMode.Breadth))
                {
                    // Define primeiramente o nó atual como o pai do novo nó
                    this.getSearchTree().setCurrentNode(openedSearchNode.getRootNode());
                }
                
                // Adiciona o nó na árvore de busca
                this.getSearchTree().addChildToCurrentNode(openedSearchNode);
                
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
                     * 
                     * Aplica cada transição possível sobre o nó atual 
                     * da árvore de busca, adicionando na lista de abertos
                     * 
                     */
                    SearchNode nextSearchNode = this.transition.applyTransition(getSearchTree().getCurrentNode());
                    
                    // Enquanto há transição aplicável, continue
                    while(nextSearchNode != null)
                    {
                        /**
                         *
                         * Verifica se não há ancestral e se também já não está
                         * nas listas de abertos e fechados
                         *
                         */
                        if (!checkAncestral(nextSearchNode))
                        {
                            if(SearchNode.isEnableDuplicate())
                            {
                                tmpList.add(nextSearchNode);
                            }
                            else
                            {
                                if (!checkContains(closedNodeList, nextSearchNode) && !checkContains(openedNodeList, nextSearchNode)) 
                                {
                                    tmpList.add(nextSearchNode);
                                }
                            }
                        }
                        
                        // Aplica a próxima transição
                        nextSearchNode = this.transition.applyTransition(getSearchTree().getCurrentNode());
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
                    this.getSearchTree().getCurrentNode().setExpanded(true);
                }
            }
        }
        
        // Dispara evento que a busca foi terminada
        this.getSearchStoppedEvenInitiator().fireEvent(getSearchState());
    }
}
