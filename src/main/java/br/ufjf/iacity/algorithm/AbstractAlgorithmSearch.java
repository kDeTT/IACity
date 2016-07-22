package br.ufjf.iacity.algorithm;

import br.ufjf.iacity.algorithm.helper.SearchNode;
import br.ufjf.iacity.algorithm.helper.SearchTree;
import br.ufjf.iacity.algorithm.events.ISearchStartedEventListener;
import br.ufjf.iacity.algorithm.events.ISearchStatusChangedEventListener;
import br.ufjf.iacity.algorithm.events.ISearchStoppedEventListener;
import br.ufjf.iacity.algorithm.events.initiator.SearchStartedEventInitiator;
import br.ufjf.iacity.algorithm.events.initiator.SearchStatusChangedEventInitiator;
import br.ufjf.iacity.algorithm.events.initiator.SearchStoppedEvenInitiator;
import br.ufjf.iacity.algorithm.transition.ITransition;
import br.ufjf.iacity.graph.CityGraph;
import br.ufjf.iacity.graph.CityNodeAdjacency;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public abstract class AbstractAlgorithmSearch 
{

    /**
     * @return the cityGraph
     */
    public CityGraph getCityGraph() {
        return cityGraph;
    }
    public enum SearchState { Success, Failed, Searching, Started, Stopped }
    
    public enum SearchMode { Backtracking, Breadth, Depth, Ordered, BestFirst, A, IDA }
    
    protected CityGraph cityGraph;
    protected ITransition transition;
    
    protected SearchTree searchTree;
    private SearchState searchState;
    
    // Evento disparado quando a busca é iniciada
    private final SearchStartedEventInitiator searchStartedEventInitiator;
    
    // Evento disparado quando a busca é parada
    private final SearchStoppedEvenInitiator searchStoppedEvenInitiator;
    
    // Evento disparado quando o status da busca é alterado
    private final SearchStatusChangedEventInitiator statusChangedEventInitiator;
    
    public abstract void search();
    
    private double executionTime;
    private List<String> solutionList;
    private String solutionPath;
    private float solutionCost;
    private int solutionDepth;
    private int solutionExpandedNodeCount;
    private int solutionVisitedNodeCount;
    
    public AbstractAlgorithmSearch()
    {
        this.searchStartedEventInitiator = new SearchStartedEventInitiator();
        this.searchStoppedEvenInitiator = new SearchStoppedEvenInitiator();
        this.statusChangedEventInitiator = new SearchStatusChangedEventInitiator();
    }
    
    /**
     * @return the searchStartedEventInitiator
     */
    public SearchStartedEventInitiator getSearchStartedEventInitiator() {
        return searchStartedEventInitiator;
    }
    
    public void addSearchStoppedEventListener(ISearchStoppedEventListener listener)
    {
        this.searchStoppedEvenInitiator.addListener(listener);
    }
    
    /**
     * @return the searchStoppedEvenInitiator
     */
    public SearchStoppedEvenInitiator getSearchStoppedEvenInitiator() {
        return searchStoppedEvenInitiator;
    }
    
    public void addSearchStartedEventListener(ISearchStartedEventListener listener)
    {
        this.searchStartedEventInitiator.addListener(listener);
    }
    
    /**
     * @return the statusChangedEventInitiator
     */
    protected SearchStatusChangedEventInitiator getSearchStatusChangedEventInitiator() {
        return statusChangedEventInitiator;
    }
    
    public void addSearchStatusChangedEventListener(ISearchStatusChangedEventListener listener)
    {
        this.statusChangedEventInitiator.addListener(listener);
    }
    
    /**
     * @return the searchState
     */
    public SearchState getSearchState() {
        return searchState;
    }

    /**
     * @param searchState the searchState to set
     */
    public void setSearchState(SearchState searchState) 
    {
        this.searchState = searchState;
        this.statusChangedEventInitiator.fireEvent(searchState);
        
        if(searchState.equals(SearchState.Searching))
        {
            this.searchStartedEventInitiator.fireEvent(searchState);
        }
        else if(searchState.equals(SearchState.Stopped) || searchState.equals(SearchState.Success) || searchState.equals(SearchState.Failed))
        {
            this.searchStoppedEvenInitiator.fireEvent(searchState);
        }
    }
    
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
    
    private void calculateSolutionCost()
    {
        if(getSearchState().equals(SearchState.Success))
        {
            int cost = 0;
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
            this.solutionCost = Float.POSITIVE_INFINITY;
        }
    }
    
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
    
    public void printPath()
    {
        if(getSearchState().equals(SearchState.Success))
        {
            System.out.println(getSolutionPath());
        }
        else
        {
            System.out.println("A busca não obteve sucesso, não há um caminho!");
        }
    }
    
    public void printDepth()
    {
        System.out.println(String.format("Profundidade da solução: %s", getSolutionDepth()));
    }
    
    public void printCost()
    {
        System.out.println("Custo da Solução: " + getSolutionCost());
    }
    
    public void printExpandedAndVisited()
    {
        System.out.println("Quantidade de nós expandidos: " + getSolutionExpandedNodeCount());
        System.out.println("Quantidade de nós visitados: " + getSolutionVisitedNodeCount());
    }
    
    public void printAverageFactorBranching()
    {
        System.out.println("Fator Médio de Ramificação: " + getSolutionAverageFactorBranching());
    }
    
    protected boolean checkContains(List<SearchNode> openedNodeList, SearchNode nodeGraph)
    {
        if((openedNodeList != null) && (nodeGraph != null))
        {
            return openedNodeList.contains(nodeGraph);
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
    
    protected void addInOpenedNodeList(BreadthAndDepthSearch.SearchMode searchMode, List<SearchNode> openedNodeList, SearchNode newSearchNode)
    {
        if((openedNodeList != null) && (newSearchNode != null) && (searchMode != null))
        {
            if (searchMode.equals(BreadthAndDepthSearch.SearchMode.Breadth)) 
            {
                ((LinkedList) openedNodeList).addLast(newSearchNode);
            } 
            else if (searchMode.equals(BreadthAndDepthSearch.SearchMode.Depth)) 
            {
                ((Stack) openedNodeList).push(newSearchNode);
            }
            else if (searchMode.equals(BreadthAndDepthSearch.SearchMode.Ordered)) 
            {
                openedNodeList.add(newSearchNode);
            }
        }
    }
    
    protected SearchNode getElementFromOpenedNodeList(BreadthAndDepthSearch.SearchMode searchMode, List<SearchNode> openedNodeList)
    {
        Object tmpElement = null;
        SearchNode nodeElement = null;
        
        if((openedNodeList != null) && (searchMode != null))
        {
            if (searchMode.equals(BreadthAndDepthSearch.SearchMode.Breadth) || searchMode.equals(BreadthAndDepthSearch.SearchMode.Ordered)) 
            {
                tmpElement = ((LinkedList) openedNodeList).getFirst();
            } 
            else if (searchMode.equals(BreadthAndDepthSearch.SearchMode.Depth)) 
            {
                tmpElement = ((Stack) openedNodeList).peek();
            }
            
            nodeElement = (SearchNode)tmpElement;
        }
        
        return nodeElement;
    }
    
    protected SearchNode removeFromOpenedNodeList(BreadthAndDepthSearch.SearchMode searchMode, List<SearchNode> openedNodeList)
    {
        Object tmpObj = null;
        
        if((openedNodeList != null) && (searchMode != null))
        {
            if (searchMode.equals(BreadthAndDepthSearch.SearchMode.Breadth) || searchMode.equals(BreadthAndDepthSearch.SearchMode.Ordered)) 
            {
                tmpObj = ((LinkedList) openedNodeList).removeFirst();
            } 
            else if (searchMode.equals(BreadthAndDepthSearch.SearchMode.Depth)) 
            {
                tmpObj = ((Stack) openedNodeList).pop();
            }
        }
        
        return (tmpObj != null) ? (SearchNode)tmpObj : null;
    }
    
    /**
     * @return the searchTree
     */
    public SearchTree getSearchTree() {
        return searchTree;
    }

    /**
     * @return the executionTime
     */
    public double getExecutionTime() {
        return executionTime;
    }

    /**
     * @param executionTime the executionTime to set
     */
    public void setExecutionTime(double executionTime) {
        this.executionTime = executionTime;
    }

    /**
     * @return the solutionList
     */
    public List<String> getSolutionList() 
    {
        this.makeSolutionPath();
        return solutionList;
    }
    
    /**
     * @return the solutionPath
     */
    public String getSolutionPath()
    {
        this.makeSolutionPath();
        return solutionPath;
    }

    /**
     * @return the solutionCost
     */
    public float getSolutionCost() 
    {
        this.calculateSolutionCost();
        return solutionCost;
    }

    /**
     * @return the solutionDepth
     */
    public int getSolutionDepth()
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
     * @return the solutionExpandedNodeCount
     */
    public int getSolutionExpandedNodeCount() 
    {
        this.solutionExpandedNodeCount = 0;
        this.calculateExpandedNodeCount(searchTree.getRootNode());
        return solutionExpandedNodeCount;
    }

    /**
     * @return the solutionVisitedNodeCount
     */
    public int getSolutionVisitedNodeCount()
    {
        this.solutionVisitedNodeCount = 0;
        this.calculateVisitedNodeCount(searchTree.getRootNode());
        return solutionVisitedNodeCount;
    }
    
    public float getSolutionAverageFactorBranching()
    {
        return ((float)getSolutionExpandedNodeCount() / getSolutionVisitedNodeCount());
    }
}