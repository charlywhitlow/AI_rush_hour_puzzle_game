/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rushhour;

import java.util.List;
import search.Action;
import search.Astar;
import search.Node;
import search.State;

/**
 * This class contains the main method to run your code. In case a solution is found,
 * it prints the following information to the screen: the amount of time it took to find the
 * solution, the number of nodes that were expanded and the cost of the solution. The puzzle
 * that needs to be solved is passed as a command line argument. 
 * 
 * You don’t need to make any changes to this class.
 * 
 * @author steven
 */
public class RushHour {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        GameState gs = new GameState(args[0]);
        gs.printState();
        Astar as = new Astar(gs);
        long startTime = System.currentTimeMillis();
        Node goal = as.findPathToGoal();
        long endTime = System.currentTimeMillis();
        if(goal!=null)
            System.out.println(args[0] + " " + (endTime - startTime) + " " + as.getNodesExpanded() + " " + goal.getCost());
        else
            System.out.println("No solution was found.");
    }
}
