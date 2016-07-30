package br.ufjf.iacity.algorithm;

import br.ufjf.iacity.algorithm.search.SearchNode;
import br.ufjf.iacity.algorithm.search.SearchTree;
import br.ufjf.iacity.algorithm.events.ISearchStartedEventListener;
import br.ufjf.iacity.algorithm.events.ISearchStatusChangedEventListener;
import br.ufjf.iacity.algorithm.events.ISearchStoppedEventListener;
import br.ufjf.iacity.algorithm.events.SearchStartedEventInitiator;
import br.ufjf.iacity.algorithm.events.SearchStatusChangedEventInitiator;
import br.ufjf.iacity.algorithm.events.SearchStoppedEvenInitiator;
import br.ufjf.iacity.algorithm.transition.ITransition;
import br.ufjf.iacity.graph.CityGraph;
import br.ufjf.iacity.graph.CityNodeAdjacency;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Classe que contém a base para todos os algoritmos de busca IA
 * 
 * @author Luis Augusto
 */
public abstract class AbstractAlgorithmSearch 
{
    // Estados que a busca pode assumir
    public enum SearchState { Success, Failed, Searching, Started, Stopped }
    
    // Modos de busca que podem ser usados
    public enum SearchMode { Backtracking, Breadth, Depth, Ordered, BestFirst, A, IDA }
    
    // Grafo de cidades do problema
    protected CityGraph cityGraph;
    
    // Regra de transição de estados que será utilizada
    protected ITransition transition;
    
    // Árvore de busca gerada pelo algoritmo
    protected SearchTree searchTree;
    
    // Estado atual da busca
    private SearchState searchState;
    
    // Evento disparado quando a busca é iniciada
    private final SearchStartedEventInitiator searchStartedEventInitiator;
    
    // Evento disparado quando a busca é parada
    private final SearchStoppedEvenInitiator searchStoppedEvenInitiator;
    
    // Evento disparado quando o status da busca é alterado
    private final SearchStatusChangedEventInitiator statusChangedEventInitiator;
    
    // Variáveis para o resultado da busca
    private double executionTime;
    private List<String> solutionList;
    private String solutionPath;
    private double solutionCost;
    private int solutionDepth;
    private int solutionExpandedNodeCount;
    private int solutionVisitedNodeCount;
    
    // Método que deve ser implementado por cada algoritmo de busca IA
    public abstract void search();
    
    public AbstractAlgorithmSearch()
    {
        this.searchStartedEventInitiator = new SearchStartedEventInitiator();
        this.searchStoppedEvenInitiator = new SearchStoppedEvenInitiator();
        this.statusChangedEventInitiator = new SearchStatusChangedEventInitiator();
    }
    
    /**
     * Verifica se um nó já foi adicionado na lista
     * 
     * @param nodeList Lista de nós
     * @param nodeGraph Nó do grafo de cidades
     * @return (true) se o nó já está na lista, (false) caso contrário
     */
    protected boolean checkContains(List<SearchNode> nodeList, SearchNode nodeGraph)
    {
        if((nodeList != null) && (nodeGraph != null))
        {
            return nodeList.contains(nodeGraph);
        }
        
        return false;
    }
    
    /**
     * Verifica se o nó já foi adicionado na árvore como ancestral
     * 
     * @param cityNode Nó a ser verificado
     * @return (true) se já possui ancestral, (false) caso contrário
     */
    protected boolean checkAncestral(SearchNode cityNode)
    {
        SearchNode tmpSearchNode = this.getSearchTree().getCurrentNode();
        
        while(tmpSearchNode != null)
        {
            if(tmpSearchNode.getIdNode().equalsIgnoreCase(cityNode.getIdNode()))
            {
                return true;
            }
            
            tmpSearchNode = tmpSearchNode.getRootNode();
        }
        
        return false;
    }
    
    /**
     * Adiciona um estado na lista de abertos de acordo com o tipo de busca que
     * está sendo usada. 
     * 
     * Exemplo: SearchMode.Breadth => Adicionar no final da lista (Fila)
     * 
     * @param searchMode Modo de busca
     * @param openedNodeList Lista de nós abertos
     * @param newSearchNode Nó que será adicionado na lista
     */
    protected void addInOpenedNodeList(SearchMode searchMode, List<SearchNode> openedNodeList, SearchNode newSearchNode)
    {
        if((openedNodeList != null) && (newSearchNode != null) && (searchMode != null))
        {
            switch(searchMode)
            {
                case Breadth:
                    ((LinkedList) openedNodeList).addLast(newSearchNode);
                    break;
                case Depth:
                    ((Stack) openedNodeList).push(newSearchNode);
                    break;
                default:
                    openedNodeList.add(newSearchNode);
                    break;
            }
        }
    }
    
    /**
     * Retorna um elemento da lista de abertos de acordo com o modo de busca 
     * utilizado
     * 
     * @param searchMode Modo de busca
     * @param openedNodeList Lista de nós abertos
     * @return SearchNode
     */
    protected SearchNode getElementFromOpenedNodeList(SearchMode searchMode, List<SearchNode> openedNodeList)
    {
        Object tmpElement;
        SearchNode nodeElement = null;
        
        if((searchMode != null) && (openedNodeList != null))
        {
            switch(searchMode)
            {
                case Depth:
                    tmpElement = ((Stack) openedNodeList).peek();
                    break;
                default:
                    tmpElement = ((LinkedList) openedNodeList).getFirst();
            }

            nodeElement = (SearchNode)tmpElement;
        }
        
        return nodeElement;
    }
    
    /**
     * Remove um elemento da lista de nós abertos de acordo com o modo de busca
     * que está sendo utilizado
     * 
     * @param searchMode Modo de busca
     * @param openedNodeList
     * @return 
     */
    protected SearchNode removeFromOpenedNodeList(SearchMode searchMode, List<SearchNode> openedNodeList)
    {
        Object tmpObj = null;
        
        if((openedNodeList != null) && (searchMode != null))
        {
            switch(searchMode)
            {
                case Depth:
                    tmpObj = ((Stack) openedNodeList).pop();
                    break;
                default:
                    tmpObj = ((LinkedList) openedNodeList).removeFirst();
            }
        }
        
        return (tmpObj != null) ? (SearchNode)tmpObj : null;
    }
    
    public SearchStartedEventInitiator getSearchStartedEventInitiator() 
    {
        return searchStartedEventInitiator;
    }
    
    public void addSearchStoppedEventListener(ISearchStoppedEventListener listener)
    {
        this.searchStoppedEvenInitiator.addListener(listener);
    }
    
    public SearchStoppedEvenInitiator getSearchStoppedEvenInitiator() 
    {
        return searchStoppedEvenInitiator;
    }
    
    public void addSearchStartedEventListener(ISearchStartedEventListener listener)
    {
        this.searchStartedEventInitiator.addListener(listener);
    }
    
    protected SearchStatusChangedEventInitiator getSearchStatusChangedEventInitiator() 
    {
        return statusChangedEventInitiator;
    }
    
    public void addSearchStatusChangedEventListener(ISearchStatusChangedEventListener listener)
    {
        this.statusChangedEventInitiator.addListener(listener);
    }
    
    /**
     * @return Grafo de cidades do problema
     */
    public CityGraph getCityGraph()
    {
        return cityGraph;
    }
    
    /**
     * @return Árvore de busca gerada
     */
    public SearchTree getSearchTree() 
    {
        return searchTree;
    }
    
    /**
     * @return Estado atual da busca
     */
    public SearchState getSearchState() 
    {
        return searchState;
    }

    /**
     * Define o estado da busca
     * 
     * @param searchState Estado atual da busca
     */
    protected void setSearchState(SearchState searchState) 
    {
        this.searchState = searchState;
        this.statusChangedEventInitiator.fireEvent(searchState);
    }
    
    /**
     * Constrói o caminho da solução
     */
    private void makeSolutionPath()
    {
        this.solutionList = new ArrayList<>();
        
        if(getSearchState().equals(SearchState.Success))
        {
            
            StringBuilder reversePath = new StringBuilder();
            
            SearchNode tmpSearchNode = this.getSearchTree().getEndNode();

            while (tmpSearchNode != null) 
            {
                this.solutionList.add(tmpSearchNode.getIdNode());
                
                tmpSearchNode = tmpSearchNode.getRootNode();
            }
            
            for(int i = (solutionList.size() - 1); i >= 0 ; i--)
            {
                reversePath.append(solutionList.get(i)).append(System.getProperty("line.separator"));
            }
            
            this.solutionPath = reversePath.toString();
        }
        else
        {
            this.solutionPath = "";
        }
    }
    
    /**
     * Calcula o custo da solução
     */
    private void calculateSolutionCost()
    {
        if(getSearchState().equals(SearchState.Success))
        {
            double cost = 0;
            SearchNode tmpSearchNode = this.getSearchTree().getEndNode();

            while (tmpSearchNode.getRootNode() != null) 
            {
                CityNodeAdjacency adjacency = getCityGraph().getAdjacency(tmpSearchNode.getRootNode().getCityNodeGraph(), tmpSearchNode.getCityNodeGraph());
                
                if(adjacency != null)
                {
                    cost += adjacency.getCost();
                }
                
                tmpSearchNode = tmpSearchNode.getRootNode();
            }

            this.solutionCost = cost;
        }
        else
        {
            this.solutionCost = Double.POSITIVE_INFINITY;
        }
    }
    
    /**
     * Calcula a quantidade de nós expandidos
     * 
     * @param rootNode Nó inicial
     */
    private void calculateExpandedNodeCount(SearchNode rootNode)
    {
        if(rootNode != null)
        {
            if(rootNode.isExpanded())
            {
                this.solutionExpandedNodeCount++;
            }
            
            SearchNode tmpNode;
            Iterator<SearchNode> childIt = rootNode.getChildNodeIterator();
            
            while(childIt.hasNext())
            {
                tmpNode = childIt.next();
                calculateExpandedNodeCount(tmpNode);
            }
        }
    }
    
    /**
     * Calcula a quantidade de nós visitados
     * 
     * @param rootNode Nó inicial
     */
    private void calculateVisitedNodeCount(SearchNode rootNode)
    {
        if(rootNode != null)
        {
            if(rootNode.isVisited())
            {
                this.solutionVisitedNodeCount++;
            }
            
            SearchNode tmpNode;
            Iterator<SearchNode> childIt = rootNode.getChildNodeIterator();
            
            while(childIt.hasNext())
            {
                tmpNode = childIt.next();
                calculateVisitedNodeCount(tmpNode);
            }
        }
    }
    
    /**
     * @return Tempo de execução da busca
     */
    public double getExecutionTime()
    {
        return executionTime;
    }

    /**
     * Define o tempo de execução da busca
     * 
     * @param executionTime Tempo de execução da busca
     */
    protected void setExecutionTime(double executionTime) 
    {
        this.executionTime = executionTime;
    }

    /**
     * @return Lista com passos da solução
     */
    public List<String> getSolutionList() 
    {
        this.makeSolutionPath();
        return solutionList;
    }
    
    /**
     * @return Caminho da solução
     */
    public String getSolutionPath()
    {
        this.makeSolutionPath();
        return solutionPath;
    }

    /**
     * @return Custo da solução
     */
    public double getSolutionCost() 
    {
        this.calculateSolutionCost();
        return solutionCost;
    }

    /**
     * @return Profundidade da solução
     */
    public int getSolutionSearchDepth()
    {
        if(getSearchState().equals(SearchState.Success))
        {
            this.solutionDepth = getSearchTree().getEndNode().getTreeLevel();
        }
        else
        {
            this.solutionDepth = -1;
        }
        
        return solutionDepth;
    }
    
    /**
     * @return Profundidade da árvore
     */
    public int getSolutionTreeDepth()
    {
        return this.getSearchTree().depth();
    }
    
    /**
     * @return Quantidade de nós da árvore de busca
     */
    public int getSolutionTreeNodeCount()
    {
        return this.searchTree.getNodeCount();
    }

    /**
     * @return Quantidade de nós expandidos
     */
    public int getSolutionExpandedNodeCount() 
    {
        this.solutionExpandedNodeCount = 0;
        this.calculateExpandedNodeCount(searchTree.getRootNode());
        
        return solutionExpandedNodeCount;
    }

    /**
     * @return Quantidade de nós visitados
     */
    public int getSolutionVisitedNodeCount()
    {
        this.solutionVisitedNodeCount = 0;
        this.calculateVisitedNodeCount(searchTree.getRootNode());
        
        return solutionVisitedNodeCount;
    }
    
    /**
     * @return Fator médio de ramificação da árvore
     */
    public double getSolutionAverageFactorBranching()
    {
        return ((double)getSolutionTreeNodeCount() / getSolutionExpandedNodeCount());
    }
}