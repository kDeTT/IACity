package br.ufjf.iacity.main;

import br.ufjf.iacity.algorithm.AbstractAlgorithmSearch.SearchState;
import br.ufjf.iacity.algorithm.BacktrackingSearch;
import br.ufjf.iacity.algorithm.events.ISearchStatusChangedEventListener;
import br.ufjf.iacity.algorithm.transition.AlphabeticalTransition;
import br.ufjf.iacity.graph.CityGraph;
import br.ufjf.iacity.helper.GeoCoordinate;
import br.ufjf.iacity.algorithm.helper.AlgorithmParameter;
import br.ufjf.iacity.model.City;
import java.util.Random;

public class MainProgram
{
    private static CityGraph randomGraph(int maxCities, int maxAdjacency)
    {
        CityGraph graph = new CityGraph();
        
        for(int i = 0; i < maxCities; i++)
        {
            graph.addNode(-1, new City(String.valueOf(i), new GeoCoordinate(-1, -1)));
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
        class EventTest implements ISearchStatusChangedEventListener {

            @Override
            public void searchStatusChangedEvent(Object event) {
                if (event instanceof SearchState) {
                    SearchState state = (SearchState) event;
                    System.out.println(String.format("Search status changed: %s", state.toString()));
                }
            }
        }
        
        try
        {
            //            CityGraph graph = randomGraph(100, 50);
            
            
            City cityA = new City("A", new GeoCoordinate(-1, -1));
            City cityB = new City("B", new GeoCoordinate(-1, -1));
            City cityC = new City("C", new GeoCoordinate(-1, -1));
            City cityD = new City("D", new GeoCoordinate(-1, -1));
            City cityE = new City("E", new GeoCoordinate(-1, -1));
            City cityF = new City("F", new GeoCoordinate(-1, -1));
            
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
            
            AlgorithmParameter parameter = new AlgorithmParameter();
            
            parameter.setGraph(graph);
            parameter.setTransition(new AlphabeticalTransition());
            parameter.setStartCityNode(graph.getNode(cityA));
            parameter.setEndCityNode(graph.getNode(cityF));
            parameter.setEnableDuplicated(true);
            
            
            long startTime;
            long endTime;
            
            
            
            BacktrackingSearch backTrack = new BacktrackingSearch(parameter);
            
            // Testando evento
            EventTest testEvent = new EventTest();
            backTrack.addSearchStatusChangedEventListener(testEvent);
            
            startTime = System.nanoTime();
            backTrack.search();
            endTime = System.nanoTime();
            
            System.out.println("Tempo de Execução: " + ((endTime - startTime) / 1e6) + " ms");
            backTrack.printCost();
            backTrack.printDepth();
            backTrack.printExpandedAndVisited();
            backTrack.printAverageFactorBranching();
            System.out.println("Caminho:");
            backTrack.printPath();
            
            
            
//            parameter.setSearchMode(SearchMode.Breadth);
//            
//            BreadthAndDepthSearch breadthSearch = new BreadthAndDepthSearch(parameter);
//
//            startTime = System.nanoTime();
//            breadthSearch.search();
//            endTime = System.nanoTime();
//
//            System.out.println("Tempo de Execução: " + ((endTime - startTime) / 1e6) + " ms");
//            breadthSearch.printCost();
//            breadthSearch.printDepth();
//            breadthSearch.printExpandedAndVisited();
//            System.out.println("Caminho:");
//            breadthSearch.printPath();
            
//            parameter.setSearchMode(SearchMode.Depth);
//            
//            BreadthAndDepthSearch depthSearch = new BreadthAndDepthSearch(parameter);
//
//            startTime = System.nanoTime();
//            depthSearch.search();
//            endTime = System.nanoTime();
//
//            System.out.println("Tempo de Execução: " + ((endTime - startTime) / 1e6) + " ms");
//            depthSearch.printCost();
//            depthSearch.printDepth();
//            depthSearch.printExpandedAndVisited();
//            System.out.println("Caminho:");
//            depthSearch.printPath();
            
            
            
//            OrderedSearch orderedSearch = new OrderedSearch(parameter);
//            
//            startTime = System.nanoTime();
//            orderedSearch.search();
//            endTime = System.nanoTime();
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
