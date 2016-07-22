package br.ufjf.iacity.algorithm.helper;

import br.ufjf.iacity.algorithm.AbstractAlgorithmSearch.SearchMode;
import br.ufjf.iacity.algorithm.transition.ITransition;
import br.ufjf.iacity.graph.CityGraph;
import br.ufjf.iacity.graph.CityNodeGraph;

/**
 *
 * @author Luis Augusto
 */
public class AlgorithmParameter 
{
    // Grafo de cidades do problema
    private CityGraph graph;
    
    // Transição que será aplicada para gerar novos nós de busca
    private ITransition transition;
    
    // Nó do grafo de cidades que será o início do problema
    private CityNodeGraph startCityNode;
    
    // Nó do grafo de cidades que será o fim do problema
    private CityNodeGraph endCityNode;
    
    // Tipo de busca que será utilizado, sendo em largura (Breadth) ou profundidade (Depth)
    private SearchMode searchMode;
    
    // Habilita/desabilita a possiblidade de adicionar estados duplicados na árvore de busca
    private boolean enableDuplicated;

    /**
     * @return the graph
     */
    public CityGraph getGraph() {
        return graph;
    }

    /**
     * @param graph the graph to set
     */
    public void setGraph(CityGraph graph) {
        this.graph = graph;
    }

    /**
     * @return the transition
     */
    public ITransition getTransition() {
        return transition;
    }

    /**
     * @param transition the transition to set
     */
    public void setTransition(ITransition transition) {
        this.transition = transition;
    }

    /**
     * @return the startCityNode
     */
    public CityNodeGraph getStartCityNode() {
        return startCityNode;
    }

    /**
     * @param startCityNode the startCityNode to set
     */
    public void setStartCityNode(CityNodeGraph startCityNode) {
        this.startCityNode = startCityNode;
    }

    /**
     * @return the endCityNode
     */
    public CityNodeGraph getEndCityNode() {
        return endCityNode;
    }

    /**
     * @param endCityNode the endCityNode to set
     */
    public void setEndCityNode(CityNodeGraph endCityNode) {
        this.endCityNode = endCityNode;
    }

    /**
     * @return the searchMode
     */
    public SearchMode getSearchMode() {
        return searchMode;
    }

    /**
     * @param searchMode the searchMode to set
     */
    public void setSearchMode(SearchMode searchMode) {
        this.searchMode = searchMode;
    }

    /**
     * @return the enableDuplicated
     */
    public boolean isEnableDuplicated() {
        return enableDuplicated;
    }

    /**
     * @param enableDuplicated the enableDuplicated to set
     */
    public void setEnableDuplicated(boolean enableDuplicated) {
        this.enableDuplicated = enableDuplicated;
    }

}
