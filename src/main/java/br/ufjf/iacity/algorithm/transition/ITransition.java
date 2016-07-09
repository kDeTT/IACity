package br.ufjf.iacity.algorithm.transition;

import br.ufjf.iacity.algorithm.helper.SearchNode;

public interface ITransition 
{
    public SearchNode applyTransition(SearchNode currentSearchNode);
}
