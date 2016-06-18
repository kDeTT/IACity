package br.ufjf.iacity.main;

import br.ufjf.iacity.algorithm.BreadthAndDepthSearch;
import br.ufjf.iacity.algorithm.BreadthAndDepthSearch.SearchMode;
import br.ufjf.iacity.algorithm.base.transition.AlphabeticalTransition;
import br.ufjf.iacity.graph.CityGraph;
import br.ufjf.iacity.helper.Coordinate;
import br.ufjf.iacity.model.City;
import java.util.Random;

public class MainProgram 
{
    private static CityGraph randomGraph(int maxCities, int maxAdjacency)
    {
        CityGraph graph = new CityGraph();
        
        for(int i = 0; i < maxCities; i++)
        {
            graph.addNode(-1, new City(String.valueOf(i), new Coordinate(-1, -1)));
        }
        
        float costAdj;
        int rndIndex1;
        int rndIndex2;
        Random rand = new Random();
        
        for(int i = 0; i < maxCities; i++)
        {
            for(int j = 0; j < maxAdjacency; j++)
            {
                costAdj = rand.nextInt(500);
                rndIndex1 = rand.nextInt(maxCities);
                rndIndex2 = rand.nextInt(maxCities);
            
                graph.addAdjacency(graph.getNode(rndIndex1), graph.getNode(rndIndex2), costAdj, false);
            }
        }
        
        return graph;
    }
    
    public static void main(String[] args) 
    {
        try
        {
            City cityA = new City("A", new Coordinate(-1, -1));
            City cityB = new City("B", new Coordinate(-1, -1));
            City cityC = new City("C", new Coordinate(-1, -1));
            City cityD = new City("D", new Coordinate(-1, -1));
            City cityE = new City("E", new Coordinate(-1, -1));
            City cityF = new City("F", new Coordinate(-1, -1));
            
            CityGraph graph = new CityGraph();
            graph.addNode(0, cityA);
            graph.addNode(0, cityB);
            graph.addNode(0, cityC);
            graph.addNode(0, cityD);
            graph.addNode(0, cityE);
            graph.addNode(0, cityF);
            
            graph.addAdjacency(graph.getNode(cityA), graph.getNode(cityB), 20, false);
            graph.addAdjacency(graph.getNode(cityA), graph.getNode(cityC), 10, false);
            graph.addAdjacency(graph.getNode(cityA), graph.getNode(cityD), 5, false);
            
            graph.addAdjacency(graph.getNode(cityB), graph.getNode(cityC), 25, false);
            graph.addAdjacency(graph.getNode(cityB), graph.getNode(cityD), 15, false);
            
            graph.addAdjacency(graph.getNode(cityC), graph.getNode(cityD), 30, false);
            graph.addAdjacency(graph.getNode(cityC), graph.getNode(cityE), 15, false);
            
            graph.addAdjacency(graph.getNode(cityD), graph.getNode(cityE), 10, false);
            
            graph.addAdjacency(graph.getNode(cityE), graph.getNode(cityF), 5, false);
            
//            CityGraph graph = randomGraph(500, 50);
            
            
//            BacktrackingSearch backTrack = new BacktrackingSearch(graph, new AlphabeticalTransition(), graph.getNode(cityA), graph.getNode(cityF));
//            
//            long startTime = System.nanoTime();
//            backTrack.search();
//            long endTime = System.nanoTime();
//            
//            System.out.println("Tempo de Execução: " + ((endTime - startTime) / 1e6) + " ms");
//            backTrack.printCost();
//            backTrack.printDepth();
//            backTrack.printExpandedAndVisited();
//            System.out.println("Caminho:");
//            backTrack.printPath();
            
            BreadthAndDepthSearch breadthSearch = new BreadthAndDepthSearch(graph, new AlphabeticalTransition(), graph.getNode(cityA), graph.getNode(cityF), SearchMode.Breadth);
//            BreadthAndDepthSearch breadthSearch = new BreadthAndDepthSearch(graph, new AlphabeticalTransition(), graph.getNode(cityA), graph.getNode(cityF), SearchMode.Depth);
//            BreadthAndDepthSearch breadthSearch = new BreadthAndDepthSearch(graph, new AlphabeticalTransition(), graph.getNode(0), graph.getNode(799), SearchMode.Depth);
            
            long startTime = System.nanoTime();
            breadthSearch.search();
            long endTime = System.nanoTime();

            System.out.println("Tempo de Execução: " + ((endTime - startTime) / 1e6) + " ms");
            breadthSearch.printCost();
            breadthSearch.printDepth();
            breadthSearch.printExpandedAndVisited();
            System.out.println("Caminho:");
            breadthSearch.printPath();
            
            
////            OrderedSearch orderedSearch = new OrderedSearch(graph, new AlphabeticalTransition(), graph.getNode(cityA), graph.getNode(cityF));
//            OrderedSearch orderedSearch = new OrderedSearch(graph, new AlphabeticalTransition(), graph.getNode(0), graph.getNode(199));
//            
//            long startTime = System.nanoTime();
//            orderedSearch.search();
//            long endTime = System.nanoTime();
//
//            System.out.println("Tempo de Execução: " + ((endTime - startTime) / 1e6) + " ms");
//            orderedSearch.printCost();
//            orderedSearch.printDepth();
//            orderedSearch.printExpandedAndVisited();
//            System.out.println("Caminho:");
//            orderedSearch.printPath();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
