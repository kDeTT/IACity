package br.ufjf.iacity.algorithm;

import br.ufjf.iacity.algorithm.search.AlgorithmParameter;
import br.ufjf.iacity.algorithm.search.SearchNode;
import br.ufjf.iacity.algorithm.search.SearchTree;
import br.ufjf.iacity.helper.sort.QuickSort;
import br.ufjf.iacity.model.City;
import java.util.LinkedList;
import java.util.List;

public class IDASearch extends AbstractAlgorithmSearch
{
    /**
     * 
     * @param parameter Parâmetros de inicialização para o algoritmo de busca
     * 
     * @throws IllegalArgumentException 
     */
    public IDASearch(AlgorithmParameter parameter) throws IllegalArgumentException
    {
        if ((parameter.getGraph() == null) || (parameter.getTransition() == null)
                || (parameter.getStartCityNode() == null) || (parameter.getEndCityNode() == null)) 
        {
            throw new IllegalArgumentException("Não é permitido nenhum parâmetro nulo para o construtor da classe IDASearch");
        }
        
        // Define o grafo do problema e a regra de transição
        this.cityGraph = parameter.getGraph();
        this.transition = parameter.getTransition();
        
        // Inicializa a árvore de busca com o primeiro nó e define o nó final
        this.searchTree = new SearchTree();
        this.searchTree.setStartNode(new SearchNode(null, 0, parameter.getStartCityNode()));
        this.searchTree.setEndNode(new SearchNode(null, 0, parameter.getEndCityNode()));
        
        // Habilita/Desabilita opções a serem usadas durante a busca
        SearchNode.setEnableDuplicate(parameter.isEnableDuplicated());
        SearchNode.setEnableCost(true);
        
        // Inicializa o estado da busca
        this.setSearchState(AbstractAlgorithmSearch.SearchState.Stopped);
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
        
        // Marca o tempo inicial
        long startSearchTime = System.nanoTime();
        
        // Muda o estado para buscando
        this.setSearchState(AbstractAlgorithmSearch.SearchState.Searching);
        
        //Adiciona o nó inicial na árvore de busca
        this.getSearchTree().addChildToCurrentNode(getSearchTree().getStartNode());
        
        // Define o valor da função de avaliação para a raíz
        SearchNode startNode = this.getSearchTree().getStartNode();
        SearchNode endNode = this.getSearchTree().getEndNode();
        
        City startCity = startNode.getCityNodeGraph().getCity();
        City endCity = endNode.getCityNodeGraph().getCity();
        
        startNode.setEvalFunctionValue(startCity.getCoordinate().distanceTo(endCity.getCoordinate()));
        
        // Define o patamar
        double patamar = startNode.getEvalFunctionValue();
        double old_patamar = -1;
        
        // Define que o nó inicial foi visitado
        startNode.setVisited(true);
        
        // Lista de descartados
        List<SearchNode> discardedNodeList = new LinkedList<>();
        
        // Enquanto não for obtido sucesso ou fracasso, continua a busca
        while (!(getSearchState().equals(SearchState.Success) || getSearchState().equals(SearchState.Failed)))
        {
            if(old_patamar == patamar)
            {
                 setSearchState(SearchState.Failed);
            }
            else
            {
                SearchNode currentNode = getSearchTree().getCurrentNode();
                
                // Define que o nó atual da árvore foi visitado
                currentNode.setVisited(true);
                
                /**
                 * Verifica se o estado atual é a solução e se atende à regra
                 * f(n) <= patamar
                 */
                if (currentNode.getIdNode().equalsIgnoreCase(getSearchTree().getEndNode().getIdNode())
                        && (currentNode.getEvalFunctionValue() <= patamar))
                {
                    // A busca teve sucesso
                    setSearchState(SearchState.Success);

                    // O novo nó é o nó final
                    getSearchTree().setEndNode(currentNode);
                }
                else
                {
                    /**
                     * Verifica se o valor da função de avaliação do estado atual
                     * é maior que o patamar, caso seja, insere na lista de
                     * descartados
                     */
                    if(currentNode.getEvalFunctionValue() > patamar)
                    {
                        // Insere o nó atual na lista de desccartados
                        discardedNodeList.add(currentNode);
                        
                        // Remove o nó atual da árvore de busca
                        this.getSearchTree().removeChildNode(currentNode);
                        
                        // Retorna para o pai do nó atual
                        this.getSearchTree().setCurrentNode(currentNode.getRootNode());
                        currentNode = getSearchTree().getCurrentNode();
                    }
                    
                    // Aplica a transição sobre o nó atual da árvore de busca
                    SearchNode nextSearchNode = transition.applyTransition(currentNode);
                    
                    // Verifica se a transição foi aplicada
                    if(nextSearchNode != null)
                    {                        
                        // Verifica se não há ancestral para o nó
                        if (!checkAncestral(nextSearchNode))
                        {
                            // Define que o nó atual foi expandido
                            currentNode.setExpanded(true);
                            
                            // Define o custo do estado (Da raíz até o estado atual)
                            double cost = currentNode.getCost() + nextSearchNode.getCost();
                            nextSearchNode.setCost(cost);
                            
                            startCity = nextSearchNode.getCityNodeGraph().getCity();

                            // Define o valor da função de avaliação
                            nextSearchNode.setEvalFunctionValue(cost + 
                                    startCity.getCoordinate().distanceTo(endCity.getCoordinate()));
                            
                             // Adiciona o novo nó na árvore de busca
                            this.getSearchTree().addChildToCurrentNode(nextSearchNode);
                            
                            // Altera o nó atual para o novo nó criado
                            this.getSearchTree().setCurrentNode(nextSearchNode);
                        }
                    }
                    else
                    {
                        // Verifica se retornou para a raíz
                        if(currentNode.getIdNode().equalsIgnoreCase(getSearchTree().getStartNode().getIdNode()))
                        {
                            // Guarda o patamar antigo
                            old_patamar = patamar;
                            
                            if(!discardedNodeList.isEmpty())
                            {
                                // Ordena a lista de descartados usando a função de avaliação dos nós
                                discardedNodeList = QuickSort.sort(discardedNodeList, QuickSort.SortType.EvalFunction);

                                /**
                                 * Define o novo patamar que será utilizado a
                                 * partir da lista de descartados. Como a lista
                                 * se encontra ordenada pelo valor da função de
                                 * avalição, basta pegar o primeiro item da
                                 * lista
                                 */
                                patamar = discardedNodeList.get(0).getEvalFunctionValue();

                                // Limpa a lista de descartados
                                discardedNodeList.clear();
                                
                                // Reseta o estado do grafo
                                this.getCityGraph().resetState();
                                
                                // Reinicia a árvore
                                this.getSearchTree().resetTree();
                            }
                        }
                        else
                        {
                            // Retorna para o pai do nó atual
                            this.getSearchTree().setCurrentNode(currentNode.getRootNode());
                        }
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
