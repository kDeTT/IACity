package br.ufjf.iacity.main.gui;

import br.ufjf.iacity.algorithm.helper.SearchNode;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Luis Augusto
 */
public class TreeViewerGUI extends JFrame 
{
    private final int rootX = 10;
    private final int rootY = 10;
    private final int NODE_SIZE = 25;
    private final int COLLUMN_WIDTH = 100;
    private final int ROW_HEIGHT = 100;
    
    private final int canvasWidth = 640;
    private final int canvasHeight = 480;
    
    private final mxGraph jGraph;
    private final Object jGraphParent;
    
    /**
     * Creates new form TreeViewerGUI
     * @param rootNode
     */
    public TreeViewerGUI(SearchNode rootNode) 
    {
        this.setSize(canvasWidth, canvasHeight);

        this.jGraph = new mxGraph();
        this.jGraphParent = this.jGraph.getDefaultParent();
        jGraph.setCellsEditable(false);
        this.create(rootNode);
    }
    
    private void create(SearchNode rootNode)
    {
        this.updateGraph(rootNode);
        
        mxGraphComponent graphComponent = new mxGraphComponent(jGraph);
//        graphComponent.setEnabled(false);
        
        getContentPane().add(graphComponent);
    }
    
    private void updateGraph(SearchNode rootNode)
    {
        try
        {
            this.jGraph.getModel().beginUpdate();
            
            Object[] childCells = this.jGraph.getChildCells(jGraphParent, true, false);
            this.jGraph.removeCells(childCells, true);
            this.drawTree(rootNode, 100, 0);
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
    
    private Object drawTree(SearchNode rootNode, int xCoordRoot, int index)
    {
        if(rootNode == null)
        {
            return null;
        }
        
//        int xCoord = (int) (((canvasWidth * (factor)) / (Math.pow(2, rootNode.getTreeLevel()) + 1)) - ((canvasWidth - 10) / 2));
        int xCoord = (int)(xCoordRoot + (xCoordRoot * index));
        
//        int xCoord = ((rootNode.getTreeLevel() * COLLUMN_WIDTH) + rootX);
        int yCoord = ((rootNode.getTreeLevel() * ROW_HEIGHT) + rootY);
        
        Object rootVertex = jGraph.insertVertex(jGraphParent, rootNode.getIdNode(), rootNode.getIdNode(), xCoord, yCoord, NODE_SIZE, NODE_SIZE);
        
        SearchNode childNode;
        Iterator<SearchNode> childIt = rootNode.getChildNodeIterator();
        
        while(childIt.hasNext())
        {
            childNode = childIt.next();
            
            Object childVertex = drawTree(childNode, xCoordRoot, index);

            if (childVertex != null) 
            {
                jGraph.insertEdge(jGraphParent, null, "", rootVertex, childVertex, "startArrow=none;endArrow=none;strokeWidth=1;strokeColor=green");
            }
            
            index++;
        }
        
        return rootVertex;
    }
}