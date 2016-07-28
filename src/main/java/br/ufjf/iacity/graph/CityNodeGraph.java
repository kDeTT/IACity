package br.ufjf.iacity.graph;

import br.ufjf.iacity.model.City;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class CityNodeGraph 
{
    private String idNode;
    private double cost;
    private final List<CityNodeAdjacency> adjacencyList;
    private final City city;
    
    public CityNodeGraph(double cost, City city) throws IllegalArgumentException
    {
        if(city == null)
        {
            throw new IllegalArgumentException("A cidade do nó do grafo não deve ser nula");
        }
        
        this.idNode = city.getName();
        this.cost = cost;
        this.adjacencyList = new ArrayList<>();
        this.city = city;
    }
    
    public boolean addAdjacency(CityNodeAdjacency adjNode)
    {
        return (this.adjacencyList.add(adjNode));
    }
    
    public boolean removeAdjacency(CityNodeAdjacency adjNode)
    {
        return (this.adjacencyList.remove(adjNode));
    }
    
    public CityNodeAdjacency getAdjacency(int adjId)
    {
        return (this.adjacencyList.get(adjId));
    }
    
    public CityNodeAdjacency getAdjacency(CityNodeGraph node)
    {
        CityNodeAdjacency nodeAdj = null;
        
        for(CityNodeAdjacency adj : adjacencyList)
        {
            if(node.getIdNode().equalsIgnoreCase(adj.getIdAdjacency()))
            {
                nodeAdj = adj;
                break;
            }
        }
        
        return nodeAdj;
    }
    
    public int getAdjacencyCount()
    {
        return (this.adjacencyList.size());
    }
    
    public Iterator<CityNodeAdjacency> getAdjacencyIterator()
    {
        return (this.adjacencyList.iterator());
    }
    
    /**
     * 
     * @return Id do nó do grafo de cidades (Coincide com o nome da cidade)
     */
    public String getIdNode()
    {
        return this.idNode;
    }

    public double getCost() 
    {
        return cost;
    }
    
    public void setCost(double cost) 
    {
        this.cost = cost;
    }
    
    public City getCity()
    {
        return city;
    }

    @Override
    public int hashCode() 
    {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.idNode);
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.cost) ^ (Double.doubleToLongBits(this.cost) >>> 32));
        hash = 17 * hash + Objects.hashCode(this.adjacencyList);
        hash = 17 * hash + Objects.hashCode(this.city);
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
        final CityNodeGraph other = (CityNodeGraph) obj;
        if (!Objects.equals(this.idNode, other.idNode)) {
            return false;
        }
        if (Double.doubleToLongBits(this.cost) != Double.doubleToLongBits(other.cost)) {
            return false;
        }
        if (!Objects.equals(this.adjacencyList, other.adjacencyList)) {
            return false;
        }
        if (!Objects.equals(this.city, other.city)) {
            return false;
        }
        return true;
    }
}
