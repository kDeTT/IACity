package br.ufjf.iacity.graph;

import java.util.Objects;

/**
 * Adjacência do grafo de cidades
 */
public class CityNodeAdjacency 
{
    private String idAdj;
    private double cost;
    private final CityNodeGraph adjNode;
    
    private boolean directed;
    private boolean visited;
    
    public CityNodeAdjacency(double cost, CityNodeGraph adjNode, boolean directed) throws IllegalArgumentException
    {
        if(adjNode == null)
        {
            throw new IllegalArgumentException("O nó na adjacência não deve ser nulo");
        }
        
        this.idAdj = adjNode.getIdNode();
        this.cost = cost;
        this.adjNode = adjNode;
        this.directed = directed;
    }
    
    /**
     * 
     * @return Id da adjacência de um nó do grafo de cidades (Coincide com o nome da cidade)
     */
    public String getIdAdjacency()
    {
        return this.idAdj;
    }

    public double getCost() 
    {
        return cost;
    }

    public void setCost(double cost) 
    {
        this.cost = cost;
    }

    public CityNodeGraph getAdjNode()
    {
        return adjNode;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.idAdj);
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.cost) ^ (Double.doubleToLongBits(this.cost) >>> 32));
        hash = 97 * hash + Objects.hashCode(this.adjNode);
        hash = 97 * hash + (this.directed ? 1 : 0);
        hash = 97 * hash + (this.visited ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CityNodeAdjacency other = (CityNodeAdjacency) obj;
        if (!Objects.equals(this.idAdj, other.idAdj)) {
            return false;
        }
        if (Double.doubleToLongBits(this.cost) != Double.doubleToLongBits(other.cost)) {
            return false;
        }
        if (!Objects.equals(this.adjNode, other.adjNode)) {
            return false;
        }
        if (this.directed != other.directed) {
            return false;
        }
        if (this.visited != other.visited) {
            return false;
        }
        return true;
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
     * @return the directed
     */
    public boolean isDirected() {
        return directed;
    }

    /**
     * @param directed the directed to set
     */
    public void setDirected(boolean directed) {
        this.directed = directed;
    }
}
