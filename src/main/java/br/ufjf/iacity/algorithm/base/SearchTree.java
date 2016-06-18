package br.ufjf.iacity.algorithm.base;

public class SearchTree 
{
    private SearchNode rootNode;
    private SearchNode currentNode;
    
    private SearchNode startNode;
    private SearchNode endNode;
    
    public SearchTree()
    {
        this.rootNode = null;
        this.currentNode = null;
        this.endNode = null;
    }
    
    public SearchTree(SearchNode rootNode)
    {
        this.rootNode = rootNode;
        this.currentNode = rootNode;
        this.endNode = null;
    }
    
    public void addChildToCurrentNode(SearchNode childNode) throws NullPointerException
    {
        if(rootNode == null)
        {
            this.rootNode = childNode;
            this.currentNode = rootNode;
        }
        else
        {
            if(currentNode == null)
            {
                throw new NullPointerException("O nó atual da árvore de busca não foi definido");
            }
            
            this.currentNode.addChildNode(childNode);
        }
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
}
