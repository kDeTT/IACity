package br.ufjf.iacity.algorithm;

import br.ufjf.iacity.algorithm.base.SearchNode;
import br.ufjf.iacity.algorithm.base.SearchTree;
import br.ufjf.iacity.algorithm.events.ISearchStartedEventListener;
import br.ufjf.iacity.algorithm.events.ISearchStatusChangedEventListener;
import br.ufjf.iacity.algorithm.events.ISearchStoppedEventListener;
import br.ufjf.iacity.algorithm.events.initiator.SearchStartedEventInitiator;
import br.ufjf.iacity.algorithm.events.initiator.SearchStatusChangedEventInitiator;
import br.ufjf.iacity.algorithm.events.initiator.SearchStoppedEvenInitiator;
import br.ufjf.iacity.algorithm.transition.ITransition;
import br.ufjf.iacity.graph.CityGraph;
import br.ufjf.iacity.graph.CityNodeAdjacency;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public abstract class AbstractAlgorithmSearch 
{
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
        if(getSearchState().equals(SearchState.Success))
        {
            StringBuilder path = new StringBuilder();
            StringBuilder reversePath = new StringBuilder();
            
            SearchNode tmpSearchNode = this.searchTree.getEndNode();

            while (tmpSearchNode != null) 
            {
                path.append(String.format("Nó: %s #", tmpSearchNode.getIdNode()));
                tmpSearchNode = tmpSearchNode.getRootNode();
            }
            
            String[] splitPath = path.toString().split("#");
            
            for(int i = (splitPath.length - 1); i >= 0 ; i--)
            {
                reversePath.append(splitPath[i]).append(System.getProperty("line.separator"));
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
            SearchNode tmpSearchNode = this.searchTree.getEndNode();

            while (tmpSearchNode.getRootNode() != null) 
            {
                CityNodeAdjacency adjacency = cityGraph.getAdjacency(tmpSearchNode.getRootNode().getCityNodeGraph(), tmpSearchNode.getCityNodeGraph());
                
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
    
    private void calculateExpandedNodeCount()
    {
        if(getSearchState().equals(SearchState.Success))
        {
            int expanded = 0;
            
            SearchNode tmpSearchNode = this.searchTree.getEndNode();

            while (tmpSearchNode != null) 
            {
                if(tmpSearchNode.isExpanded())
                {
                    expanded++;
                }
                
                tmpSearchNode = tmpSearchNode.getRootNode();
            }

            this.solutionExpandedNodeCount = expanded;
        }
        else
        {
            this.solutionExpandedNodeCount = -1;
        }
    }
    
    private void calculateVisitedNodeCount()
    {
        if(getSearchState().equals(SearchState.Success))
        {
            int visited = 0;
            
            SearchNode tmpSearchNode = this.searchTree.getEndNode();

            while (tmpSearchNode != null) 
            {
                if(tmpSearchNode.isVisited())
                {
                    visited++;
                }
                
                tmpSearchNode = tmpSearchNode.getRootNode();
            }

            this.solutionVisitedNodeCount = visited;
        }
        else
        {
            this.solutionVisitedNodeCount = -1;
        }
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
            this.solutionDepth = searchTree.getEndNode().getTreeLevel();
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
        this.calculateExpandedNodeCount();
        return solutionExpandedNodeCount;
    }

    /**
     * @return the solutionVisitedNodeCount
     */
    public int getSolutionVisitedNodeCount()
    {
        this.calculateVisitedNodeCount();
        return solutionVisitedNodeCount;
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
    
    protected boolean checkContains(List<SearchNode> openedNodeList, List<SearchNode> closedNodeList, SearchNode nodeGraph)
    {
        if((openedNodeList != null) && (closedNodeList != null) && (nodeGraph != null))
        {
            return (openedNodeList.contains(nodeGraph) || (closedNodeList.contains(nodeGraph)));
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
        SearchNode tmpSearchNode = this.searchTree.getCurrentNode();
        
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
                
                
                
//                if(openedNodeList.size() > 0)
//                {
//                    for (int i = 0; i < openedNodeList.size(); i++) 
//                    {
//                        if (newSearchNode.getCost() <= openedNodeList.get(i).getCost()) 
//                        {
//                            openedNodeList.add(i, newSearchNode);
//                            break;
//                        }
//                    }
//                }
//                else
//                {
//                    openedNodeList.add(newSearchNode);
//                }
                
                
                
//                Collections.sort(openedNodeList, new Comparator()
//                {
//                    @Override
//                    public int compare(Object obj1, Object obj2) 
//                    {
//                        SearchNode searchNode1 = (SearchNode)obj1;
//                        SearchNode searchNode2 = (SearchNode)obj2;
//                        
//                        return (searchNode1.getCost() < searchNode2.getCost()) ? -1 : (searchNode1.getCost() > searchNode2.getCost()) ? 1 : 0;
//                    }
//                });
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
}