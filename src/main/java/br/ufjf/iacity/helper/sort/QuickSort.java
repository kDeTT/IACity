package br.ufjf.iacity.helper.sort;

import br.ufjf.iacity.algorithm.search.SearchNode;
import java.util.LinkedList;
import java.util.List;

/**
 * @see https://gist.github.com/djitz/2152957
 */
public class QuickSort
{
    public enum SortType { Cost, EvalFunction };
    
    private QuickSort() {}

    public static List<SearchNode> sort(List<SearchNode> inputList, SortType sortType) 
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
            if(sortType.equals(SortType.Cost))
            {
                if (inputList.get(i).getCost() <= pivotNode.getCost()) {
                    if (i == middle) {
                        continue;
                    }

                    lessList.add(inputList.get(i));
                } else {
                    greaterList.add(inputList.get(i));
                }
            }
            else if(sortType.equals(SortType.EvalFunction))
            {
                if (inputList.get(i).getEvalFunctionValue() <= pivotNode.getEvalFunctionValue()) {
                    if (i == middle) {
                        continue;
                    }

                    lessList.add(inputList.get(i));
                } else {
                    greaterList.add(inputList.get(i));
                }
            }
        }

        return concatenate(sort(lessList, sortType), pivotNode, sort(greaterList, sortType));
    }
    
    private static List<SearchNode> concatenate(List<SearchNode> lessList, SearchNode pivotNode, List<SearchNode> greaterList) {

        List<SearchNode> concatList = new LinkedList<>();
        
        concatList.addAll(lessList);
        concatList.add(pivotNode);
        concatList.addAll(greaterList);

        return concatList;
    }
}
