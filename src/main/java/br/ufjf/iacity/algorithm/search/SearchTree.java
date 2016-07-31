package br.ufjf.iacity.algorithm.search;

import java.util.Iterator;

/**
 * Árvore de busca
 */
public class SearchTree 
{
    private SearchNode rootNode;
    private SearchNode currentNode;
    
    private SearchNode startNode;
    private SearchNode endNode;
    
    private int nodeCount;
    private int depth;
    
    public SearchTree()
    {
        this.rootNode = null;
        
        this.startNode = null;
        this.endNode = null;
        
        this.currentNode = null;
        
        this.nodeCount = 0;
        this.depth = 0;
    }
    
    public void resetTree()
    {
        this.rootNode.removeAllChildNode();
        this.rootNode.setExpanded(false);
        this.rootNode.setVisited(false);
        this.rootNode.setCost(0);
        this.rootNode.setEvalFunctionValue(0);
        
        this.currentNode = rootNode;
        
        this.nodeCount = 1;
        this.depth = 0;
    }
    
    private void calculateDepth(SearchNode rootNode, int currentDepth)
    {
        if(rootNode != null)
        {
            currentDepth++;
            
            if (currentDepth > depth) 
            {
                depth = currentDepth;
            }
            
            SearchNode childNode;
            Iterator<SearchNode> childIt = rootNode.getChildNodeIterator();

            while (childIt.hasNext()) 
            {
                childNode = childIt.next();
                calculateDepth(childNode, currentDepth);
            }
            
            currentDepth--;
        }
    }
    
    public int depth()
    {
        depth = 0;
        calculateDepth(rootNode, depth);
        
        return (depth - 1);
    }
    
    public void addChildToCurrentNode(SearchNode childNode) throws NullPointerException
    {
        if(rootNode == null)
        {
            this.rootNode = childNode;
            this.currentNode = rootNode;
            this.nodeCount++;
        }
        else
        {
            if(currentNode == null)
            {
                throw new NullPointerException("O nó atual da árvore de busca não foi definido");
            }
            
            SearchNode childRootNode = childNode.getRootNode();
            
            if((childRootNode != null) && !currentNode.getIdNode().equalsIgnoreCase(childRootNode.getIdNode()))
            {
                childRootNode = findNode(childRootNode.getIdNode(), childRootNode);
                
                if(childRootNode != null)
                {
                    this.currentNode = childRootNode;
                }
            }
            
            this.currentNode.addChildNode(childNode);
            this.nodeCount++;
        }
    }
    
    public void removeAllChildOfCurrentNode()
    {
        this.currentNode.removeAllChildNode();
    }
    
    public void removeChildNode(SearchNode childNode)
    {
        if(childNode != null)
        {
            // Se o nó possui um pai
            if(childNode.getRootNode() != null)
            {
                SearchNode rootChild = childNode.getRootNode();
                
                // Remove o filho do pai
                rootChild.removeChildNode(childNode);
                
                // Verifica se após a remoção o pai não tem mais filhos
                if(rootChild.getChildNodeCount() == 0)
                {
                    // Volta atrás e marca que o nó não foi expandido
                    rootChild.setExpanded(false);
                }
            }
            
            childNode = null;
            this.nodeCount--;
        }
    }
    
    private SearchNode findNode(String idNode, SearchNode rootNode)
    {
        if(rootNode == null)
        {
            return null;
        }
        
        if(rootNode.getIdNode().equalsIgnoreCase(idNode))
        {
            return rootNode;
        }
        
        SearchNode tmpNode;
        SearchNode childNode;
        Iterator<SearchNode> childIt = rootNode.getChildNodeIterator();
        
        while(childIt.hasNext())
        {
            childNode = childIt.next();
            tmpNode = findNode(idNode, childNode);
            
            if(tmpNode.getIdNode().equalsIgnoreCase(idNode))
            {
                return tmpNode;
            }
        }
        
        return null;
    }

    /**
     * @return the rootNode
     */
    public SearchNode getRootNode() {
        return rootNode;
    }

    /**
     * @return the currentNode
     */
    public SearchNode getCurrentNode() {
        return currentNode;
    }

    /**
     * @param currentNode the currentNode to set
     */
    public void setCurrentNode(SearchNode currentNode) {
        this.currentNode = currentNode;
    }

    /**
     * @return the startNode
     */
    public SearchNode getStartNode() {
        return startNode;
    }

    /**
     * @param startNode the startNode to set
     */
    public void setStartNode(SearchNode startNode) 
    {
        this.startNode = startNode;
    }

    /**
     * @return the endNode
     */
    public SearchNode getEndNode() {
        return endNode;
    }

    /**
     * @param endNode the endNode to set
     */
    public void setEndNode(SearchNode endNode) {
        this.endNode = endNode;
    }

    /**
     * @return the nodeCount
     */
    public int getNodeCount() {
        return nodeCount;
    }

    /**
     * @param nodeCount the nodeCount to set
     */
    public void setNodeCount(int nodeCount) {
        this.nodeCount = nodeCount;
    }
}
