package br.ufjf.iacity.algorithm.search;

import br.ufjf.iacity.graph.CityNodeGraph;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class SearchNode implements Comparable
{
    private String idNode;
    private SearchNode rootNode;
    private List<SearchNode> childNodeList;
    private int treeLevel;
    private boolean expanded;
    private boolean visited;
    
    private static boolean enableCost;
    private double cost;
    private double evalFunctionValue;
    
    private static boolean enableDuplicate;
    
    private CityNodeGraph cityNodeGraph;
    
    public SearchNode(SearchNode rootNode, int treeLevel, CityNodeGraph cityNodeGraph) throws IllegalArgumentException
    {
        if(cityNodeGraph == null)
        {
            throw new IllegalArgumentException("O nó do grafo de cidades no nó de busca não deve ser nulo");
        }
        
        this.idNode = cityNodeGraph.getIdNode();
        this.rootNode = rootNode;
        this.childNodeList = new ArrayList<>();
        this.treeLevel = treeLevel;
        this.expanded = false;
        this.visited = false;
        
        this.cityNodeGraph = cityNodeGraph;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.idNode);
        hash = 37 * hash + Objects.hashCode(this.rootNode);
        hash = 37 * hash + Objects.hashCode(this.childNodeList);
        hash = 37 * hash + this.treeLevel;
        hash = 37 * hash + (this.expanded ? 1 : 0);
        hash = 37 * hash + (this.visited ? 1 : 0);
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.cost) ^ (Double.doubleToLongBits(this.cost) >>> 32));
        hash = 37 * hash + Objects.hashCode(this.cityNodeGraph);
        return hash;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (obj == null) 
        {
            return false;
        }
        
        if (getClass() != obj.getClass()) 
        {
            return false;
        }
        
        SearchNode other = (SearchNode) obj;
        return Objects.equals(this.idNode, other.idNode);
    }
    
    @Override
    public int compareTo(Object obj)
    {
        // Caso contrário, compara os custos dos estados
        SearchNode other = (SearchNode) obj;
        return Double.compare(this.cost, other.cost);
    }
    
    /**
     * 
     * @return Id do nó da árvore de busca (Coincide com o nome da cidade)
     */
    public String getIdNode()
    {
        return idNode;
    }

    public SearchNode getRootNode()
    {
        return rootNode;
    }
    
    public void setRootNode(SearchNode node)
    {
        this.rootNode = node;
    }
    
    public Iterator<SearchNode> getChildNodeIterator()
    {
        return this.childNodeList.iterator();
    }
    
    public int getChildNodeCount()
    {
        return this.childNodeList.size();
    }
    
    public boolean addChildNode(SearchNode childNode)
    {
        return (this.childNodeList.add(childNode));
    }
    
    public void removeAllChildNode()
    {
        this.childNodeList.removeAll(childNodeList);
    }
    
    public boolean removeChildNode(SearchNode childNode)
    {
        return (this.childNodeList.remove(childNode));
    }

    public CityNodeGraph getCityNodeGraph() 
    {
        return cityNodeGraph;
    }
    
    public int getTreeLevel()
    {
        return this.treeLevel;
    }

    /**
     * @return the visited
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * @param visited the visited to set
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * @return the expanded
     */
    public boolean isExpanded() {
        return expanded;
    }

    /**
     * @param expanded the expanded to set
     */
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    /**
     * @return the cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(double cost) {
        this.cost = cost;
    }
    
    /**
     * @return the enableCost
     */
    public static boolean isEnableCost() {
        return enableCost;
    }
    
    /**
     * @param aEnableCost the enableCost to set
     */
    public static void setEnableCost(boolean aEnableCost) {
        enableCost = aEnableCost;
    }
    
    /**
     * @return the enableDuplicate
     */
    public static boolean isEnableDuplicate() {
        return enableDuplicate;
    }

    /**
     * @param aEnableDuplicate the enableDuplicate to set
     */
    public static void setEnableDuplicate(boolean aEnableDuplicate) {
        enableDuplicate = aEnableDuplicate;
    }

    /**
     * @return the evalFunctionValue
     */
    public double getEvalFunctionValue() {
        return evalFunctionValue;
    }

    /**
     * @param evalFunctionValue the evalFunctionValue to set
     */
    public void setEvalFunctionValue(double evalFunctionValue) {
        this.evalFunctionValue = evalFunctionValue;
    }
}
