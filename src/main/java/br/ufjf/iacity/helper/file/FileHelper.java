package br.ufjf.iacity.helper.file;

import br.ufjf.iacity.graph.CityGraph;
import br.ufjf.iacity.graph.CityNodeAdjacency;
import br.ufjf.iacity.graph.CityNodeGraph;
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
                    if (line.equalsIgnoreCase("BEGINVERTICES")) 
                    {
                        // LÃª cada vertice
                        while (((line = reader.readLine()) != null) && !line.equalsIgnoreCase("ENDVERTICES"))
                        {
                            verticesList.add(line);
                        }
                    } 
                    else if (line.equalsIgnoreCase("BEGINEDGES")) 
                    {
                        // LÃª cada adjacência
                        while (((line = reader.readLine()) != null) && !line.equalsIgnoreCase("ENDEDGES"))
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
                writer.write("BEGINVERTICES");
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

                writer.write("ENDVERTICES");
                writer.newLine();
                writer.write("BEGINEDGES");
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

                writer.write("ENDEDGES");
            }
        } 
        catch (IOException | IllegalArgumentException ex) 
        {
            throw ex;
        }
    }
}
