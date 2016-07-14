package br.ufjf.iacity.helper.file;

import br.ufjf.iacity.algorithm.AbstractAlgorithmSearch;
import br.ufjf.iacity.graph.CityGraph;
import br.ufjf.iacity.graph.CityNodeAdjacency;
import br.ufjf.iacity.graph.CityNodeGraph;
import br.ufjf.iacity.helper.GeoCoordinate;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Luis Augusto
 */
public class FileHelper 
{
    public static final String WORK_DIR = System.getProperty("user.dir") + File.separator + "workdir" + File.separator;
    
    public static CityGraph loadGraphFile(String filePath) throws Exception
    {
        try
        {
            List<String> verticesList = new ArrayList<>();
            List<String> adjacencyList = new ArrayList<>();
            
            Path path = FileSystems.getDefault().getPath(WORK_DIR, filePath);
            
            try(BufferedReader reader = new BufferedReader(new FileReader(path.toFile())))
            {
                String line;
                
                // LÃª todas as linhas do arquivo
                while ((line = reader.readLine()) != null) 
                {
                    if (line.equalsIgnoreCase("BEGIN_VERTICES")) 
                    {
                        // LÃª cada vertice
                        while (((line = reader.readLine()) != null) && !line.equalsIgnoreCase("END_VERTICES"))
                        {
                            verticesList.add(line);
                        }
                    } 
                    else if (line.equalsIgnoreCase("BEGIN_EDGES")) 
                    {
                        // LÃª cada adjacência
                        while (((line = reader.readLine()) != null) && !line.equalsIgnoreCase("END_EDGES"))
                        {
                            adjacencyList.add(line);
                        }
                    }
                }
            }
            
            return CityGraph.createGraphFromStringsList(verticesList, adjacencyList);
        }
        catch(IOException | IllegalArgumentException ex)
        {
            throw ex;
        }
    }
    
    public static void saveGraphFile(String filePath, CityGraph graph) throws IOException 
    {
        try 
        {
            Path path = FileSystems.getDefault().getPath(WORK_DIR, filePath);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))) 
            {
                writer.write("BEGIN_VERTICES");
                writer.newLine();

                CityNodeGraph tmpNode;
                Iterator<CityNodeGraph> nodeIt = graph.getNodeIterator();

                while (nodeIt.hasNext()) 
                {
                    tmpNode = nodeIt.next();

                    writer.write(String.format("<%s, %s, %s, %s>",
                            tmpNode.getIdNode(),
                            tmpNode.getCity().getCoordinate().getLatitude(),
                            tmpNode.getCity().getCoordinate().getLongitude(),
                            tmpNode.getCost()));
                    writer.newLine();
                }

                writer.write("END_VERTICES");
                writer.newLine();
                writer.write("BEGIN_EDGES");
                writer.newLine();

                CityNodeAdjacency tmpAdj;
                nodeIt = graph.getNodeIterator();

                while (nodeIt.hasNext()) 
                {
                    tmpNode = nodeIt.next();

                    Iterator<CityNodeAdjacency> adjIt = tmpNode.getAdjacencyIterator();

                    while (adjIt.hasNext()) 
                    {
                        tmpAdj = adjIt.next();

                        writer.write(String.format("<%s, %s, %s, %s>",
                                tmpNode.getIdNode(),
                                tmpAdj.getAdjNode().getIdNode(),
                                tmpAdj.getCost(),
                                true));
                        writer.newLine();
                    }
                }

                writer.write("END_EDGES");
            }
        } 
        catch (IOException | IllegalArgumentException ex) 
        {
            throw ex;
        }
    }
    
    public static void saveResultFile(String filePath, AbstractAlgorithmSearch algorithmSearch) throws IOException 
    {
        try 
        {
            Path path = FileSystems.getDefault().getPath(WORK_DIR, filePath);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))) 
            {
                writer.write("BEGIN_SEARCHINFO");
                writer.newLine();
                
                writer.write(String.format("Tempo de Execução: %s ms", algorithmSearch.getExecutionTime()));
                writer.newLine();
                
                writer.write(String.format("Custo da Solução: %s", algorithmSearch.getSolutionCost()));
                writer.newLine();
                
                writer.write(String.format("Profundidade da Solução: %s", algorithmSearch.getSolutionDepth()));
                writer.newLine();
                
                writer.write(String.format("Quantidade de Nós Expandidos: %s", algorithmSearch.getSolutionExpandedNodeCount()));
                writer.newLine();
                
                writer.write(String.format("Quantidade de Nós Visitados: %s", algorithmSearch.getSolutionVisitedNodeCount()));
                writer.newLine();
                
                writer.write(String.format("Fator Médio de Ramificação: %s", algorithmSearch.getSolutionAverageFactorBranching()));
                writer.newLine();
                
                writer.write("END_SEARCHINFO");
                writer.newLine();
                writer.write("BEGIN_SOLUTION");
                writer.newLine();
                
                String idNode;
                CityNodeGraph tmpNodeGraph;
                List<String> solutionList = algorithmSearch.getSolutionList();
                
                for(int i = (solutionList.size() - 1); i >= 0 ; i--)
                {
                    idNode = solutionList.get(i);
                    
                    tmpNodeGraph = algorithmSearch.getCityGraph().getNode(idNode);
                    GeoCoordinate coord = tmpNodeGraph.getCity().getCoordinate();
                    
                    writer.write(String.format("<%s, %s, %s>", idNode, coord.getLatitude(), coord.getLongitude()));
                    writer.newLine();
                }
                
                writer.write("END_SOLUTION");
            }
        } 
        catch (IOException | IllegalArgumentException ex) 
        {
            throw ex;
        }
    }
}
