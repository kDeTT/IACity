package br.ufjf.iacity.algorithm;

import br.ufjf.iacity.algorithm.base.SearchNode;
import br.ufjf.iacity.algorithm.base.SearchTree;
import br.ufjf.iacity.algorithm.base.transition.ITransition;
import br.ufjf.iacity.graph.CityGraph;
import br.ufjf.iacity.graph.CityNodeAdjacency;
import br.ufjf.iacity.graph.CityNodeGraph;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public abstract class AlgorithmBase 
{
    protected enum SearchState { Success, Failed, Searching, Stopped }
    
    protected CityGraph cityGraph;
    protected ITransition transition;
    
    protected SearchTree searchTree;
    protected SearchState searchState;
    
    public void printPath()
    {
        if(searchState.equals(SearchState.Success))
        {
            StringBuilder path = new StringBuilder();
            SearchNode tmpSearchNode = this.searchTree.getEndNode();

            while (tmpSearchNode != null) 
            {
                path.append(String.format("Nó: %s \n", tmpSearchNode.getIdNode()));
                tmpSearchNode = tmpSearchNode.getRootNode();
            }

            System.out.println(path.toString());
        }
        else
        {
            System.out.println("A busca não obteve sucesso, não há um caminho!");
        }
    }
    
    public void printDepth()
    {
        if(searchState.equals(SearchState.Success))
        {
            System.out.println(String.format("Profundidade da solução: %s", searchTree.getEndNode().getTreeLevel()));
        }
        else
        {
            System.out.println("A busca não obteve sucesso, não há um caminho!");
        }
    }
    
    public void printCost()
    {
        if(searchState.equals(SearchState.Success))
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

            System.out.println("Custo da Solução: " + cost);
        }
        else
        {
            System.out.println("A busca não obteve sucesso, não há um caminho!");
        }
    }
    
    public void printExpandedAndVisited()
    {
         if(searchState.equals(SearchState.Success))
        {
            int expanded = 0;
            int visited = 0;
            
            SearchNode tmpSearchNode = this.searchTree.getEndNode();

            while (tmpSearchNode != null) 
            {
                if(tmpSearchNode.isExpanded())
                {
                    expanded++;
                }
                
                if(tmpSearchNode.isVisited())
                {
                    visited++;
                }
                
                tmpSearchNode = tmpSearchNode.getRootNode();
            }

            System.out.println("Quantidade de nós expandidos: " + expanded);
            System.out.println("Quantidade de nós visitados: " + visited);
        }
        else
        {
            System.out.println("A busca não obteve sucesso, não há um caminho!");
        }
    }
    
    protected boolean checkContains(List<CityNodeGraph> openedNodeList, List<CityNodeGraph> closedNodeList, CityNodeGraph nodeGraph)
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
    protected boolean checkAncestral(CityNodeGraph cityNode)
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
    
    protected void addInOpenedNodeList(BreadthAndDepthSearch.SearchMode searchMode, List<CityNodeGraph> openedNodeList, CityNodeGraph nodeGraph)
    {
        if((openedNodeList != null) && (nodeGraph != null) && (searchMode != null))
        {
            if (searchMode.equals(BreadthAndDepthSearch.SearchMode.Breadth)) 
            {
                ((LinkedList) openedNodeList).addLast(nodeGraph);
            } 
            else if (searchMode.equals(BreadthAndDepthSearch.SearchMode.Depth)) 
            {
                ((Stack) openedNodeList).push(nodeGraph);
            }
        }
    }
    
    protected CityNodeGraph getElementFromOpenedNodeList(BreadthAndDepthSearch.SearchMode searchMode, List<CityNodeGraph> openedNodeList)
    {
        Object tmpElement = null;
        CityNodeGraph nodeElement = null;
        
        if((openedNodeList != null) && (searchMode != null))
        {
            if (searchMode.equals(BreadthAndDepthSearch.SearchMode.Breadth)) 
            {
                tmpElement = ((LinkedList) openedNodeList).getFirst();
            } 
            else if (searchMode.equals(BreadthAndDepthSearch.SearchMode.Depth)) 
            {
                tmpElement = ((Stack) openedNodeList).peek();
            }
            
            nodeElement = (CityNodeGraph)tmpElement;
        }
        
        return nodeElement;
    }
    
    protected CityNodeGraph removeFromOpenedNodeList(BreadthAndDepthSearch.SearchMode searchMode, List<CityNodeGraph> openedNodeList)
    {
        Object tmpObj = null;
        
        if((openedNodeList != null) && (searchMode != null))
        {
            if (searchMode.equals(BreadthAndDepthSearch.SearchMode.Breadth)) 
            {
                tmpObj = ((LinkedList) openedNodeList).removeFirst();
            } 
            else if (searchMode.equals(BreadthAndDepthSearch.SearchMode.Depth)) 
            {
                tmpObj = ((Stack) openedNodeList).pop();
            }
        }
        
        return (tmpObj != null) ? (CityNodeGraph)tmpObj : null;
    }
    
    protected boolean checkContainsSearchNode(List<SearchNode> openedNodeList, List<SearchNode> closedNodeList, SearchNode nodeGraph)
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
    protected boolean checkAncestralSearchNode(SearchNode cityNode)
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
                if(openedNodeList.size() > 0)
                {
                    for (int i = 0; i < openedNodeList.size(); i++) 
                    {
                        if (newSearchNode.getCost() <= openedNodeList.get(i).getCost()) 
                        {
                            openedNodeList.add(i, newSearchNode);
                            break;
                        }
                    }
                }
                else
                {
                    openedNodeList.add(newSearchNode);
                }
                
                
                
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
    
    protected SearchNode getElementFromOpenedNodeListSearchNode(BreadthAndDepthSearch.SearchMode searchMode, List<SearchNode> openedNodeList)
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
    
    protected SearchNode removeFromOpenedNodeListSearchNode(BreadthAndDepthSearch.SearchMode searchMode, List<SearchNode> openedNodeList)
    {
        Object tmpObj = null;
        
        if((openedNodeList != null) && (searchMode != null))
        {
            if (searchMode.equals(BreadthAndDepthSearch.SearchMode.Breadth)) 
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
    
    protected boolean removeFromOpenedNodeListSearchNode(List<SearchNode> openedNodeList, SearchNode toRemoveNode)
    {
        return openedNodeList.remove(toRemoveNode);
    }
}
