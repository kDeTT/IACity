package br.ufjf.iacity.helper.file;

import br.ufjf.iacity.algorithm.helper.SearchTree;
import br.ufjf.iacity.graph.CityGraph;
import br.ufjf.iacity.helper.GeoCoordinate;
import br.ufjf.iacity.model.City;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import org.apache.commons.lang3.text.WordUtils;

/**
 *
 * @author Luis Augusto
 */
public class FileHelper 
{
    public static final String WORK_DIR = System.getProperty("user.dir") + File.separator + "workdir" + File.separator;
    
    public static CityGraph loadGraphFile(String filePath) throws Exception
    {
        CityGraph graph;
        
        try
        {
            graph = new CityGraph();

            Path path = FileSystems.getDefault().getPath(WORK_DIR, filePath);
            
            try(BufferedReader reader = new BufferedReader(new FileReader(path.toFile())))
            {
                String line;
                
                // LÃª todas as linhas do arquivo
                while ((line = reader.readLine()) != null) {
                    if (line.equalsIgnoreCase("BEGINVERTICES")) {
                        // LÃª cada vertice e adiciona ao grafo
                        while (((line = reader.readLine()) != null) && !line.equalsIgnoreCase("ENDVERTICES")) {
                            // Remove caracteres inicial e final da linha
                            line = line.replaceAll("<", "");
                            line = line.replaceAll(">", "");
                            
                            // Capitaliza o texto
                            line = WordUtils.capitalizeFully(line);

                            // Remove todos os espaÃ§os
                            line = line.replaceAll(" ", "");

                            // Quebra os campos da linha
                            String[] lineSplit = line.split(",");

                            // Verifica se possui todos os campos necessÃ¡rios
                            if (lineSplit.length == 4) {
                                String cityName = lineSplit[0];
                                double latitude = Double.parseDouble(lineSplit[1]);
                                double longitude = Double.parseDouble(lineSplit[2]);
                                float cost = Float.parseFloat(lineSplit[3]);

                                City newCity = new City(cityName, new GeoCoordinate(latitude, longitude));

                                // Adiciona a cidade ao grafo
                                graph.addNode(cost, newCity);
                            }
                        }
                    } else if (line.equalsIgnoreCase("BEGINEDGES")) {
                        // LÃª cada vertice e adiciona ao grafo
                        while (((line = reader.readLine()) != null) && !line.equalsIgnoreCase("ENDEDGES")) {
                            // Remove caracteres inicial e final da linha
                            line = line.replaceAll("<", "");
                            line = line.replaceAll(">", "");
                            
                            // Capitaliza o texto
                            line = WordUtils.capitalizeFully(line);

                            // Remove todos os espaÃ§os
                            line = line.replaceAll(" ", "");

                            // Quebra os campos da linha
                            String[] lineSplit = line.split(",");

                            // Verifica se possui todos os campos necessÃ¡rios
                            if (lineSplit.length == 4) {
                                String firstCityName = lineSplit[0];
                                String secondCityName = lineSplit[1];
                                float cost = Float.parseFloat(lineSplit[2]);
                                boolean directional = Boolean.parseBoolean(lineSplit[3]);

                                // Adiciona a adjacÃªncia ao grafo
                                graph.addAdjacency(graph.getNode(firstCityName), graph.getNode(secondCityName), cost, directional);
                            }
                        }
                    }
                }
            }
            
            return graph;
        }
        catch(IOException | IllegalArgumentException ex)
        {
            throw ex;
        }
    }
    
    public void saveResultFile(String filePath, SearchTree searchTree)
    {
        
    }
}
