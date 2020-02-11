package rushhour;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import search.Action;
import search.State;

/**
 * This class contains methods for reading the puzzle from file, as well as several helper
 * methods that may prove useful when solving the coursework. This class implements the
 * State interface from the search package, but the implementation of four methods from
 * this interface is still missing. 
 * 
 * Your main assignment is to implement these missing methods.
 */
public class GameState implements search.State {

    boolean[][] occupiedPositions;
    List<Car> cars; // target car is always the first one    
    int nrRows;
    int nrCols;
    HashMap<String, int[]> transformations; // transformations matrix for checking adjacent cells

    public GameState(String fileName) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        nrRows = Integer.parseInt(in.readLine().split("\\s")[0]);
        nrCols = Integer.parseInt(in.readLine().split("\\s")[0]);
        String s = in.readLine();
        cars = new ArrayList();
        while (s != null) {
            cars.add(new Car(s));
            s = in.readLine();
        }
        initOccupied();
        initTransformations();
        in.close();
    }

    public GameState(int nrRows, int nrCols, List<Car> cars) {
        this.nrRows = nrRows;
        this.nrCols = nrCols;
        this.cars = cars;
        initOccupied();
        initTransformations();
    }

    public GameState(GameState gs) {
        nrRows = gs.nrRows;
        nrCols = gs.nrCols;
        occupiedPositions = new boolean[nrRows][nrCols];
        for (int i = 0; i < nrRows; i++) {
            for (int j = 0; j < nrCols; j++) {
                occupiedPositions[i][j] = gs.occupiedPositions[i][j];
            }
        }
        cars = new ArrayList();
        for (Car c : gs.cars) {
            cars.add(new Car(c));
        }
        initTransformations();
    }

    public void printState() {
        int[][] state = new int[nrRows][nrCols];

        for (int i = 0; i < cars.size(); i++) {
            List<Position> l = cars.get(i).getOccupyingPositions();
            for (Position pos : l) {
                state[pos.getRow()][pos.getCol()] = i + 1;
            }
        }

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {                
                if (state[i][j] == 0) {
                    System.out.print("  .  ");
                } else {
                    System.out.print("  ");
                    System.out.print(state[i][j] - 1);
                    System.out.print(" ");
                    if (state[i][j] - 1 < 10) {
                        System.out.print(" ");
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private void initOccupied() {
        occupiedPositions = new boolean[nrRows][nrCols];
       
        for (Car c : cars) {
            List<Position> l = c.getOccupyingPositions();
            for (Position pos : l) {
                occupiedPositions[pos.getRow()][pos.getCol()] = true;
            }
        }
    }

    /**
     * Build hashmap of coordinate transformations for checking adjacent cells
     */
    public void initTransformations(){

        transformations = new HashMap<String, int[]>();
        int[] above = {-1, 0, -1}; // row, col, inc(1)/dec(-1)
        int[] below = {1, 0, 1};
        int[] left = {0, -1, -1};
        int[] right = {0, 1, 1};

        transformations.put("above", above);
        transformations.put("below", below);
        transformations.put("left", left);
        transformations.put("right", right);
    }
    
    public boolean isGoal() {
        Car goalCar = cars.get(0);
        return goalCar.getCol() + goalCar.getLength() == nrCols;
    }

    public boolean equals(Object o) {
        if (!(o instanceof GameState)) {
            return false;
        } else {
            GameState gs = (GameState) o;
            return nrRows == gs.nrRows && nrCols == gs.nrCols && cars.equals(gs.cars); // note that we don't need to check equality of occupiedPositions since that follows from the equality of cars
        }
    }

    public int hashCode() {
        return cars.hashCode();
    }

    public void printToFile(String fn) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(fn));
            out.println(nrRows);
            out.println(nrCols);
            for (Car c : cars) {
                out.println(c.getRow() + " " + c.getCol() + " " + c.getLength() + " " + (c.isVertical() ? "V" : "H"));
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public State doAction(Action action) {

        // Create new state to update
        GameState newState = new GameState(this);

        // Move car
        Move m = (Move)action;
        Position pos = m.getPosition();        
        for (Car car : newState.cars) {
            if (car.equals(m.getCar())){
                car.setRow(pos.getRow());
                car.setCol(pos.getCol());
            }
        }
        newState.initOccupied();
        return newState;
    }

    public List<Action> getLegalActions() {
       
        // Loop through cars and add legal actions to legalActions arraylist
        List<Action> legalActions = new ArrayList();
        for (Car car : cars) {
            // Check for spaces above/below for vertical vars, or left/right for horizontal cars
            String[] directions = car.isVertical() ? new String[]{"above", "below"} : new String[]{"left", "right"};
            for (String direction : directions) {
                int incRow = 0;
                int incCol = 0;
                while (true) {
                    int newRow = car.getRow() + this.transformations.get(direction)[0] + (incRow * this.transformations.get(direction)[2]);
                    int newCol = car.getCol() + this.transformations.get(direction)[1] + (incCol * this.transformations.get(direction)[2]);
                    
                    // Check if legal move, and add to list
                    Move newMove = new Move(car, new Position(newRow, newCol));
                    if (this.isLegal(newMove)) {
                        legalActions.add(newMove);

                        // Increment counter to check next space
                        if (direction=="above" || direction=="below") {
                            incRow++;
                        }else{
                            incCol++;
                        }
                    }else{
                        break;
                    }
                }
            }
        }
        return legalActions;
    }

    public Car getCarAtPosition(Position p){
        for (Car car : cars) {
            for (Position pos : car.getOccupyingPositions()) {
                if (p.equals(pos)) {
                    return car;
                }
            }
        }
        return null;
    }

    public boolean isLegal(Action action) {

        // Get car and position to move to
        Move m = ((Move)action);
        Car c = m.getCar();
        Position newP = m.getPosition();

        // Make a copy of c to trial move
        Car newCar = new Car(c);
        newCar.setRow(newP.getRow());
        newCar.setCol(newP.getCol());

        // Loop through positions the car would occupy if moved, and check if move is legal
        List<Position> newcarPositions = newCar.getOccupyingPositions();
        boolean legal = true;
        for (Position p : newcarPositions) {
            // 1- Check if new position is outside game board
            if (p.getRow() < 0 || p.getRow() > this.nrRows-1 || p.getCol() < 0 || p.getCol() > this.nrCols-1) {
                return false;
            }else{
                // 2- Check if cell is occupied by the car we're moving, since that would make the space available
                boolean occupiedByOriginal = false;
                List<Position> cPositions = c.getOccupyingPositions();
                for (Position position : cPositions) {
                    if (p.equals(position)){
                        occupiedByOriginal = true;
                    }
                }
                // 3- Check if cell occupied by another car
                if (!occupiedByOriginal) {
                    if (this.occupiedPositions[p.getRow()][p.getCol()]) {
                        return false;
                    }
                }
            }
        }
        return legal;
    }

    /**
     * Heuristic based on the number of cars between goalcar and goal.
     * nb- cannot overestimate cost (due to use of A* algorithm)
     */
    public int getEstimatedDistanceToGoal() {

        int estimate = 0;
        if (this.isGoal()) {
            return estimate;
        }else{
            estimate += 1;
        }

        // Count number of cars between goal car and goal
        Car goalCar = cars.get(0);
        for (int i = (goalCar.getCol() + goalCar.getLength()); i < this.nrCols; i++) {
            if (this.occupiedPositions[goalCar.getRow()][i]) {
                estimate++;
            }
        }    
        return estimate;
    }

    // public int getEstimatedDistanceToGoal() {
    //     return 0;
    // }

}
