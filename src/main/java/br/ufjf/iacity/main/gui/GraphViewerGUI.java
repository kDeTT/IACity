package br.ufjf.iacity.main.gui;

import br.ufjf.iacity.graph.CityGraph;
import br.ufjf.iacity.graph.CityNodeAdjacency;
import br.ufjf.iacity.graph.CityNodeGraph;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GraphViewerGUI extends JFrame 
{
    private final int CANVAS_WIDTH = 640;
    private final int CANVAS_HEIGHT = 480;
    
    private final int CIRCLE_ROOT_X = (CANVAS_WIDTH / 2) - 35;
    private final int CIRCLE_ROOT_Y = (CANVAS_HEIGHT / 2) - 35;
    private final int CIRCLE_RADIUS = (CANVAS_HEIGHT / 2) - 35;
    
    private final int NODE_SIZE = 25;
    
    private final mxGraph jGraph;
    private final Object jGraphParent;

    public GraphViewerGUI(CityGraph graph) 
    {
        setTitle("Visualizar Grafo");
        setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        setLocationRelativeTo(null);

        this.jGraph = new mxGraph();
        this.jGraphParent = this.jGraph.getDefaultParent();
        
        this.create(graph);
    }
    
    private void create(CityGraph graph)
    {
        this.updateGraph(graph);
        
        mxGraphComponent graphComponent = new mxGraphComponent(jGraph);
        graphComponent.setEnabled(false);
        
        getContentPane().add(graphComponent);
    }
    
    private void updateGraph(CityGraph graph)
    {
        try
        {
            this.jGraph.getModel().beginUpdate();
            
            Object[] childCells = this.jGraph.getChildCells(jGraphParent, true, false);
            this.jGraph.removeCells(childCells, true);
            
            CityNodeGraph tmpNode;
            Iterator<CityNodeGraph> nodeIt = graph.getNodeIterator();
            
            double angle = 0;
            double variation = ((2 * Math.PI) / graph.getNodeCount());
            
            while(nodeIt.hasNext())
            {
                tmpNode = nodeIt.next();
                this.drawVertices(tmpNode, angle);
                
                angle += variation;
            }
            
            nodeIt = graph.getNodeIterator();
            
            while(nodeIt.hasNext())
            {
                tmpNode = nodeIt.next();
                
                CityNodeAdjacency tmpAdj;
                Iterator<CityNodeAdjacency> adjIt = tmpNode.getAdjacencyIterator();
                
                while(adjIt.hasNext())
                {
                    tmpAdj = adjIt.next();
                    this.drawEdges(tmpNode, tmpAdj.getAdjNode(), tmpAdj.getCost());
                }
            }
            
            
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
        finally
        {
            this.jGraph.getModel().endUpdate();
        }
    }
    
    private void drawVertices(CityNodeGraph nodeGraph, double angle)
    {
        double xCoord  =  CIRCLE_ROOT_X + CIRCLE_RADIUS * Math.cos(angle);
        double yCoord  =  CIRCLE_ROOT_Y + CIRCLE_RADIUS * Math.sin(angle);
        
        this.jGraph.insertVertex(jGraphParent, nodeGraph.getIdNode(), nodeGraph.getIdNode(), xCoord, yCoord, NODE_SIZE, NODE_SIZE);
    }
    
    private void drawEdges(CityNodeGraph nodeA, CityNodeGraph nodeB, float costAdj)
    {
        mxGraphModel graphModel = (mxGraphModel)jGraph.getModel();
        
        mxCell cellA = (mxCell)graphModel.getCell(nodeA.getIdNode());
        mxCell cellB = (mxCell)graphModel.getCell(nodeB.getIdNode());
        
        this.jGraph.insertEdge(jGraphParent, null, costAdj, cellA, cellB, "startArrow=none;endArrow=none;strokeWidth=1;strokeColor=green");
    }
}
