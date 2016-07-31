package br.ufjf.iacity.algorithm;

import br.ufjf.iacity.algorithm.search.SearchNode;
import br.ufjf.iacity.algorithm.search.SearchTree;
import br.ufjf.iacity.algorithm.search.AlgorithmParameter;

/**
 * Algoritmo de busca Backtracking. Método não informado de busca
 */
public class BacktrackingSearch extends AbstractAlgorithmSearch
{   
    /**
     * @param parameter Parâmetros de inicialização para o algoritmo de busca
     * 
     * @throws IllegalArgumentException 
     */
    public BacktrackingSearch(AlgorithmParameter parameter) throws IllegalArgumentException
    {
        if ((parameter.getGraph() == null) || (parameter.getTransition() == null)
                || (parameter.getStartCityNode() == null) || (parameter.getEndCityNode() == null)) 
        {
            throw new IllegalArgumentException("Não é permitido nenhum parâmetro nulo para o construtor da classe BacktrackingSearch");
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
        
        //Adiciona o nó inicial na árvore de busca
        this.getSearchTree().addChildToCurrentNode(getSearchTree().getStartNode());
        
        // Define que o nó inicial foi visitado
        this.getSearchTree().getCurrentNode().setVisited(true);
        
        // Enquanto não for obtido sucesso ou fracasso
        while (!(getSearchState().equals(SearchState.Success) || getSearchState().equals(SearchState.Failed)))
        {
            // Aplica a transição sobre o nó atual da árvore de busca
            SearchNode nextSearchNode = transition.applyTransition(getSearchTree().getCurrentNode());
            
            /**
             * Verifica se a transição foi aplicada.
             * 
             * Se sim, existe operador aplicável sobre o nó atual,
             * caso contrário, não existe mais operadores aplicáveis.
             */
            if(nextSearchNode != null)
            {
                // Verifica se não há ancestral para o nó
                if(!checkAncestral(nextSearchNode))
                {
                    // Adiciona o novo nó na árvore de busca
                    this.getSearchTree().addChildToCurrentNode(nextSearchNode);
                    
                    // Define que o nó atual foi expandido
                    this.getSearchTree().getCurrentNode().setExpanded(true);
                    
                    // Altera o nó atual para o novo nó criado
                    this.getSearchTree().setCurrentNode(nextSearchNode);
                    
                    // Define que o novo nó atual da árvore foi visitado
                    this.getSearchTree().getCurrentNode().setVisited(true);

                    // Verifica se o nó é o nó buscado
                    if (nextSearchNode.getIdNode().equalsIgnoreCase(getSearchTree().getEndNode().getIdNode())) 
                    {
                        // A busca teve sucesso
                        setSearchState(SearchState.Success);
                        
                        // O novo nó é o nó final
                        getSearchTree().setEndNode(nextSearchNode);
                    }
                }
            }
            else
            {
                /**
                 * Verifica se o nó atual é o nó inicial, caso seja, significa
                 * que retornei ao ponto inicial e não há mais operadores aplicáveis
                 * e, neste caso, a solução não foi encontrada
                 */
                if(getSearchTree().getCurrentNode().getIdNode().equalsIgnoreCase(getSearchTree().getRootNode().getIdNode()))
                {
                    setSearchState(SearchState.Failed);
                }
                else
                {
                    // Retorna para o pai do nó atual
                    this.getSearchTree().setCurrentNode(getSearchTree().getCurrentNode().getRootNode());
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
