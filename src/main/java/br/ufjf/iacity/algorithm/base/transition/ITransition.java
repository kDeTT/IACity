package br.ufjf.iacity.algorithm.base.transition;

import br.ufjf.iacity.algorithm.base.SearchNode;
import br.ufjf.iacity.graph.CityNodeGraph;

public interface ITransition 
{
    public CityNodeGraph applyTransition(CityNodeGraph currentNodeGraph);
    public SearchNode applyTransition(SearchNode currentSearchNode);
}
