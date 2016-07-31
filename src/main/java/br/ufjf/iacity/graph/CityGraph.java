package br.ufjf.iacity.graph;

import br.ufjf.iacity.helper.coordinate.GeoCoordinate;
import br.ufjf.iacity.model.City;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.text.WordUtils;

/**
 * Grafo de cidades
 */
public class CityGraph 
{
    private final List<CityNodeGraph> nodeList;
    
    public CityGraph()
    {
        this.nodeList = new ArrayList<>();
    }
    
    private static String[] formatStringPattern(String pattern)
    {
        if(pattern == null)
        {
            return null;
        }
        
        // Remove caracteres inicial e final da linha
        pattern = pattern.replaceAll("<", "");
        pattern = pattern.replaceAll(">", "");

        // Capitaliza o texto
        pattern = WordUtils.capitalizeFully(pattern);

        // Remove todos os espaÃ§os
        pattern = pattern.replaceAll(" ", "");

        // Quebra os campos da linha
        return pattern.split(",");
    }
    
    public static String formatVerticePattern(String cityName, double latitude, double longitude, double cost)
    {
        return String.format("<%s, %s, %s, %s>", cityName, latitude, longitude, cost);
    }
    
    public static String formatAdjacencyPattern(String cityA, String cityB, double cost, boolean directional)
    {
        return String.format("<%s, %s, %s, %s>", cityA, cityB, cost, directional);
    }
    
    public static CityNodeGraph createCityNodeGraphFromStringPattern(String vertice)
    {
        if(vertice != null)
        {
            String[] lineSplit = formatStringPattern(vertice);

            // Verifica se possui todos os campos necessÃ¡rios
            if (lineSplit.length == 4) 
            {
                String cityName = lineSplit[0];
                double latitude = Double.parseDouble(lineSplit[1]);
                double longitude = Double.parseDouble(lineSplit[2]);
                double cost = Float.parseFloat(lineSplit[3]);
                
                return new CityNodeGraph(cost, new City(cityName, new GeoCoordinate(latitude, longitude)));
            }
        }
        
        return null;
    }
    
    public static CityGraph createGraphFromStringsList(List<String> verticesList, List<String> adjacencyList)
    {
        CityGraph graph = new CityGraph();
        
        for(String vertice : verticesList)
        {
            String[] lineSplit = formatStringPattern(vertice);

            // Verifica se possui todos os campos necessÃ¡rios
            if (lineSplit.length == 4) 
            {
                String cityName = lineSplit[0];
                double latitude = Double.parseDouble(lineSplit[1]);
                double longitude = Double.parseDouble(lineSplit[2]);
                double cost = Float.parseFloat(lineSplit[3]);

                City newCity = new City(cityName, new GeoCoordinate(latitude, longitude));

                // Adiciona a cidade ao grafo
                graph.addNode(cost, newCity);
            }
        }
        
        for(String adjacency : adjacencyList)
        {
            String[] lineSplit = formatStringPattern(adjacency);

            // Verifica se possui todos os campos necessÃ¡rios
            if (lineSplit.length == 4)
            {
                String firstCityName = lineSplit[0];
                String secondCityName = lineSplit[1];
                float cost = Float.parseFloat(lineSplit[2]);
                boolean directional = Boolean.parseBoolean(lineSplit[3]);

                // Adiciona a adjacÃªncia ao grafo
                graph.addAdjacency(graph.getNode(firstCityName), graph.getNode(secondCityName), cost, directional);
            }
        }
        
        return graph;
    }
    
    public static CityGraph copyGraph(CityGraph srcGraph)
    {
        if(srcGraph == null)
        {
            return null;
        }
        
        CityGraph cpyGraph = new CityGraph();
        
        CityNodeGraph tmpNode;
        Iterator<CityNodeGraph> nodeIt = srcGraph.getNodeIterator();
        
        while(nodeIt.hasNext())
        {
            tmpNode = nodeIt.next();
            
            cpyGraph.addNode(tmpNode.getCost(),
                    new City(tmpNode.getCity().getName(),
                    new GeoCoordinate(tmpNode.getCity().getCoordinate().getLatitude(), tmpNode.getCity().getCoordinate().getLongitude())));
        }
        
        nodeIt = srcGraph.getNodeIterator();
        
        while(nodeIt.hasNext())
        {
            tmpNode = nodeIt.next();
            Iterator<CityNodeAdjacency> nodeAdjIt = tmpNode.getAdjacencyIterator();
            
            CityNodeAdjacency tmpAdj;
            
            while(nodeAdjIt.hasNext())
            {
                tmpAdj = nodeAdjIt.next();
                cpyGraph.addAdjacency(cpyGraph.getNode(tmpNode.getIdNode()), 
                        cpyGraph.getNode(tmpAdj.getAdjNode().getIdNode()), tmpAdj.getCost(), false);
            }
        }
        
        return cpyGraph;
    }
    
    public void resetState()
    {
        CityNodeGraph tmpNode;
        Iterator<CityNodeGraph> nodeIt = this.getNodeIterator();
        
        while(nodeIt.hasNext())
        {
            tmpNode = nodeIt.next();
            Iterator<CityNodeAdjacency> nodeAdjIt = tmpNode.getAdjacencyIterator();
            
            CityNodeAdjacency tmpAdj;
            
            while(nodeAdjIt.hasNext())
            {
                tmpAdj = nodeAdjIt.next();
                tmpAdj.setVisited(false);
            }
        }
    }
    
    public boolean addNode(CityNodeGraph nodeGraph)
    {
        return this.nodeList.add(nodeGraph);
    }
    
    public void addAllNodes(List<CityNodeGraph> nodeGraphList)
    {
        if(nodeGraphList != null)
        {
            for(CityNodeGraph node : nodeGraphList)
            {
                this.nodeList.add(node);
            }
        }
    }
    
    public boolean addNode(double cost, City city)
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
    
    public int getNodeCount()
    {
        return this.nodeList.size();
    }
    
    public boolean addAdjacency(CityNodeGraph city1, CityNodeGraph city2, double cost, boolean directed)
    {
        if(nodeList.contains(city1) && nodeList.contains(city2))
        {
            int index1 = this.nodeList.indexOf(city1);
            nodeList.get(index1).addAdjacency(new CityNodeAdjacency(cost, city2, directed));
            
            if(!directed)
            {
                int index2 = this.nodeList.indexOf(city2);
                nodeList.get(index2).addAdjacency(new CityNodeAdjacency(cost, city1, directed));
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
