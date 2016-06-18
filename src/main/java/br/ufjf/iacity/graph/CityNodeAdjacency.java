package br.ufjf.iacity.graph;

import java.util.Objects;

public class CityNodeAdjacency 
{
    private String idAdj;
    private float cost;
    private final CityNodeGraph adjNode;
    
    private boolean visited;
    
    public CityNodeAdjacency(float cost, CityNodeGraph adjNode) throws IllegalArgumentException
    {
        if(adjNode == null)
        {
            throw new IllegalArgumentException("O nó na adjacência não deve ser nulo");
        }
        
        this.idAdj = adjNode.getIdNode();
        this.cost = cost;
        this.adjNode = adjNode;
    }
    
    /**
     * 
     * @return Id da adjacência de um nó do grafo de cidades (Coincide com o nome da cidade)
     */
    public String getIdAdjacency()
    {
        return this.idAdj;
    }

    public float getCost() 
    {
        return cost;
    }

    public void setCost(float cost) 
    {
        this.cost = cost;
    }

    public CityNodeGraph getAdjNode()
    {
        return adjNode;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.idAdj);
        hash = 71 * hash + Float.floatToIntBits(this.cost);
        hash = 71 * hash + Objects.hashCode(this.adjNode);
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
        final CityNodeAdjacency other = (CityNodeAdjacency) obj;
        if (!Objects.equals(this.idAdj, other.idAdj)) {
            return false;
        }
        if (Float.floatToIntBits(this.cost) != Float.floatToIntBits(other.cost)) {
            return false;
        }
        if (!Objects.equals(this.adjNode, other.adjNode)) {
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
}
