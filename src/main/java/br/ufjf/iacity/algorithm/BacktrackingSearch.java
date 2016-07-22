package br.ufjf.iacity.algorithm;

import br.ufjf.iacity.algorithm.helper.SearchNode;
import br.ufjf.iacity.algorithm.helper.SearchTree;
import br.ufjf.iacity.algorithm.helper.AlgorithmParameter;

public class BacktrackingSearch extends AbstractAlgorithmSearch
{   
    /**
     * 
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
        
        // Inicializa a árvore de busca com o primeiro nó e define o nó final
        this.searchTree = new SearchTree(new SearchNode(null, 0, parameter.getStartCityNode()));
        this.searchTree.setEndNode(new SearchNode(null, 0, parameter.getEndCityNode()));
        
        // Habilita/Desabilita opções a serem usadas durante a busca
        SearchNode.setEnableDuplicate(parameter.isEnableDuplicated());
        SearchNode.setEnableCost(false);
        
        // Inicializa o estado da busca
        this.setSearchState(SearchState.Stopped);
    }
    
    /**
     * 
     * Executa a busca BacktrackingSearch sobre o grafo, usando as regras de transição
     * definidas na inicialização da classe
     * 
     */
    @Override
    public void search()
    {
        // Dispara evento que a busca foi iniciada
        this.getSearchStartedEventInitiator().fireEvent(getSearchState());
        
        // Muda o estado para buscando
        this.setSearchState(SearchState.Searching);
        
        // Define que o nó inicial foi visitado
        this.getSearchTree().getCurrentNode().setVisited(true);
        
        // Enquanto não for obtido sucesso ou fracasso, continua a busca
        while (!(getSearchState().equals(SearchState.Success) || getSearchState().equals(SearchState.Failed)))
        {
            // Aplica a transição sobre o nó atual da árvore de busca
            SearchNode nextSearchNode = transition.applyTransition(getSearchTree().getCurrentNode());
            
            /**
             * 
             * Verifica se a transição foi aplicada.
             * 
             * Se sim, existe operador aplicável sobre o nó atual,
             * caso contrário, não existe mais operadores aplicáveis.
             * 
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
                    
                    // Define que o nó atual da árvore foi visitado
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
                 * 
                 * Verifica se o nó atual é o nó inicial, caso seja, significa
                 * que retornei ao ponto inicial e não há mais operadores aplicáveis
                 * e, neste caso, a solução não foi encontrada
                 * 
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
        
        // Dispara evento que a busca foi terminada
        this.getSearchStoppedEvenInitiator().fireEvent(getSearchState());
    }
}
