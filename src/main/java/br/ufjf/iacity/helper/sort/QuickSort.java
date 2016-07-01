package br.ufjf.iacity.helper.sort;

import br.ufjf.iacity.algorithm.base.SearchNode;
import java.util.LinkedList;
import java.util.List;

/**
 * @see https://gist.github.com/djitz/2152957
 */
public class QuickSort
{
    private QuickSort() {}

    public static List<SearchNode> sort(List<SearchNode> inputList) 
    {
        if (inputList.size() <= 1) 
        {
            return inputList;
        }

        int middle = (int) Math.ceil((double) inputList.size() / 2);
        SearchNode pivotNode = inputList.get(middle);

        List<SearchNode> lessList = new LinkedList<>();
        List<SearchNode> greaterList = new LinkedList<>();

        for (int i = 0; i < inputList.size(); i++)
        {
            if (inputList.get(i).getCost() <= pivotNode.getCost())
            {
                if (i == middle)
                {
                    continue;
                }
                
                lessList.add(inputList.get(i));
            } 
            else 
            {
                greaterList.add(inputList.get(i));
            }
        }

        return concatenate(sort(lessList), pivotNode, sort(greaterList));
    }
    
    private static List<SearchNode> concatenate(List<SearchNode> lessList, SearchNode pivotNode, List<SearchNode> greaterList) {

        List<SearchNode> concatList = new LinkedList<>();
        
        concatList.addAll(lessList);
        concatList.add(pivotNode);
        concatList.addAll(greaterList);

        return concatList;
    }
}
