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
    private final int WINDOW_WIDTH = 800;
    private final int WINDOW_HEIGHT = 600;
    
    private int canvasWidth;
    private int canvasHeight;
    private int circleRootX;
    private int circleRootY;
    private int circleRadius;

    private final int NODE_SIZE = 25;
    
    private final mxGraph jGraph;
    private final Object jGraphParent;

    public GraphViewerGUI(CityGraph graph) 
    {
        setTitle("Visualizar Grafo");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        
        GraphViewerOptionsGUI viewerOptGUI = new GraphViewerOptionsGUI();
        viewerOptGUI.setModal(true);
        viewerOptGUI.setVisible(true);
        
        this.canvasWidth = viewerOptGUI.getCanvasWidth();
        this.canvasHeight = viewerOptGUI.getCanvasHeight();
        
        this.circleRootX = 2 * ((canvasWidth / 2) + 35);
        this.circleRootY = 2 * ((canvasHeight / 2) - 35);
        
        this.circleRadius = 2 * ((canvasHeight / 2) - 35);

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
            double angleVariation = ((2 * Math.PI) / graph.getNodeCount());

            while(nodeIt.hasNext())
            {
                tmpNode = nodeIt.next();
                this.drawVertices(tmpNode, angle);
                
                angle += angleVariation;
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
        double xCoord  =  circleRootX + circleRadius * Math.cos(angle);
        double yCoord  =  circleRootY + circleRadius * Math.sin(angle);
        
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
