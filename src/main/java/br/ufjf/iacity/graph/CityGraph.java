package br.ufjf.iacity.graph;

import br.ufjf.iacity.model.City;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CityGraph 
{
    private final List<CityNodeGraph> nodeList;
    
    public CityGraph()
    {
        this.nodeList = new ArrayList<>();
    }
    
    public boolean addNode(float cost, City city)
    {
        return (this.nodeList.add(new CityNodeGraph(cost, city)));
    }
    
    public boolean removeNode(CityNodeGraph nodeGraph)
    {
        return (this.nodeList.remove(nodeGraph));
    }
    
    public CityNodeGraph getNode(int idNode)
    {
        return (this.nodeList.get(idNode));
    }
    
    public CityNodeGraph getNode(String idNode)
    {
        CityNodeGraph nodeGraph = null;
        
        for(CityNodeGraph node : nodeList)
        {
            if(node.getIdNode().equalsIgnoreCase(idNode))
            {
                nodeGraph = node;
                break;
            }
        }
        
        return nodeGraph;
    }
    
    public CityNodeGraph getNode(City city)
    {
        CityNodeGraph nodeGraph = null;
        
        for(CityNodeGraph node : nodeList)
        {
            if(node.getIdNode().equalsIgnoreCase(city.getName()))
            {
                nodeGraph = node;
                break;
            }
        }
        
        return nodeGraph;
    }
    
    public Iterator<CityNodeGraph> getNodeIterator()
    {
        return (this.nodeList.iterator());
    }
    
    public boolean addAdjacency(CityNodeGraph city1, CityNodeGraph city2, float cost, boolean directed)
    {
        if(nodeList.contains(city1) && nodeList.contains(city2))
        {
            int index1 = this.nodeList.indexOf(city1);
            nodeList.get(index1).addAdjacency(new CityNodeAdjacency(cost, city2));
            
            if(!directed)
            {
                int index2 = this.nodeList.indexOf(city2);
                nodeList.get(index2).addAdjacency(new CityNodeAdjacency(cost, city1));
            }
            
            return true;
        }
        
        return false;
    }
    
    public CityNodeAdjacency getAdjacency(CityNodeGraph city1, CityNodeGraph city2)
    {
        if((city1 != null) && nodeList.contains(city1) && (city2 != null) && nodeList.contains(city2))
        {
            int index1 = this.nodeList.indexOf(city1);
            return nodeList.get(index1).getAdjacency(city2);
        }
        
        return null;
    }
}
