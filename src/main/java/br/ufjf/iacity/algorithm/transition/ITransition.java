package br.ufjf.iacity.algorithm.transition;

import br.ufjf.iacity.algorithm.search.SearchNode;

public interface ITransition 
{
    public SearchNode applyTransition(SearchNode currentSearchNode);
}
