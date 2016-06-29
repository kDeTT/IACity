package br.ufjf.iacity.algorithm.base.transition;

import br.ufjf.iacity.algorithm.base.SearchNode;

public interface ITransition 
{
    public SearchNode applyTransition(SearchNode currentSearchNode);
}
