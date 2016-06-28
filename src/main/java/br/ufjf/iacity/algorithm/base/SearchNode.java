package br.ufjf.iacity.algorithm.base;

import br.ufjf.iacity.graph.CityNodeGraph;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class SearchNode 
{
    private String idNode;
    private SearchNode rootNode;
    private List<SearchNode> childNodeList;
    private int treeLevel;
    private boolean expanded;
    private boolean visited;
    
    private static boolean enableCost;
    private float cost;
    
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
        hash = 37 * hash + Float.floatToIntBits(this.cost);
        hash = 37 * hash + Objects.hashCode(this.cityNodeGraph);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SearchNode other = (SearchNode) obj;
        if (!Objects.equals(this.idNode, other.idNode)) {
            return false;
        }
        if ((isEnableDuplicate()) && (this.rootNode != null) && (other.rootNode != null) && !Objects.equals(this.rootNode.idNode, other.rootNode.idNode)) {
            return false;
        }
        if (isEnableCost() && (Float.floatToIntBits(this.cost) != Float.floatToIntBits(other.cost))) {
            return false;
        }

        return true;
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
    public float getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(float cost) {
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
}
