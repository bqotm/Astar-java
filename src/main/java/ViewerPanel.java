import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class ViewerPanel extends JPanel {

    int COLS=30;
    int ROWS=30;
    int NODE_SIZE = 25;
    int WIDTH = NODE_SIZE * COLS;
    int HEIGHT = NODE_SIZE * ROWS;

    double WALL_PROBABILITY = 0.2;
    Cell[][] grid = new Cell[COLS][ROWS];

    Cell startCell, goalCell;
    Cell currentCell;

    PriorityQueue<Cell> openList = new PriorityQueue<>(Comparator.comparingInt(cell -> cell.f));
    ArrayList<Cell> checkedList = new ArrayList<>();

    ArrayList<Cell> thePathSet = new ArrayList<>();

    boolean goalReached = false;

    boolean noSolution = false;
    Random random = new Random();
    private boolean searching = false;

    public ViewerPanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setLayout(new GridLayout(ROWS, COLS));
        this.setDoubleBuffered(true);
        setFocusable(true);
        SwingWorker<Void, Void> searchWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                searching = true;
                while (!goalReached && !noSolution) {
                    search();
                    publish(); // Publishes intermediate results to update the UI
                    try {
                        Thread.sleep(100); // Adjust the sleep duration as needed
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                searching = false;
                return null;
            }

            @Override
            protected void process(List<Void> chunks) {
                // This method runs on the Event Dispatch Thread and is used to update the UI
                repaint(); // Redraw the panel to visualize the search
            }
        };
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j]=new Cell(i, j);
                int finalI = i;
                int finalJ = j;
                grid[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Check if a cell has been selected as the start cell
                        if (startCell == null) {
                            setStartCell(finalI, finalJ);
                        }
                        // Check if a cell has been selected as the goal cell
                        else if (goalCell == null) {
                            setGoalCell(finalI, finalJ, searchWorker);
                        }
                    }
                });
                double randomNumber = random.nextDouble();
                if (randomNumber < WALL_PROBABILITY) {
                    grid[i][j].setSolid();
                }
                this.add(grid[i][j]);
            }
        }
        //setStartCell(9, 9);
        //setGoalCell(1, 2);

    }

    public void setStartCell(int col, int row) {
        grid[col][row].setStart();
        startCell = grid[col][row];
        startCell.g = 0;
        openList.offer(startCell);
    }

    public void setGoalCell(int col, int row, SwingWorker searchWorker) {
        grid[col][row].setGoal();
        goalCell = grid[col][row];
        if (startCell!=null && goalCell!=null && !searching && !goalReached && !noSolution) {
            searchWorker.execute();
        }

    }

    private void setSolidCell(int col, int row) {
        grid[col][row].setSolid();
    }


    private void calculateHcost(Cell cell) {//heuristic : manhattan distance
        int h = Math.abs(cell.col-goalCell.col) + Math.abs(cell.row - goalCell.row);
        cell.h=h;
    }

    private int calculateFcost(Cell cell) {
        calculateHcost(cell);
        return cell.g + cell.h ;
    }

    public void search() {
        if(openList.size()>0 && !goalReached) {
            currentCell = openList.poll();
            int col = currentCell.col;
            int row =currentCell.row;
            addNeighbours(col, row);
            currentCell.setChecked();
            for(Cell neighbour : currentCell.neighbours) {
                neighbour.g = currentCell.g + 1;
                neighbour.f = calculateFcost(neighbour);
                if(checkedList.contains(neighbour))
                    continue;
                if(containsWithLowerPriority(neighbour))
                    continue;
                neighbour.previous=currentCell;
                openList.offer(neighbour);
            }

            if(currentCell==goalCell) {
                goalReached = true;
                System.out.println("path found");
                trackThePath();
                return;
            }
            if(!(checkedList.contains(currentCell))){
                checkedList.add(currentCell);
            }
        } else {
            System.out.println("no solution!");
            noSolution = true;
        }
    }

    private void trackThePath() {
        Cell current = goalCell;
        while(current != startCell) {
            current = current.previous;
            if(current != startCell) {
                current.setAsPath();
                thePathSet.add(current);
            }
        }
    }

    private void openCell(Cell cell) {
        if(cell.open == false && cell.checked == false && cell.solid == false) {
            cell.setIsOpen();
        }
    }

    private boolean containsWithLowerPriority(Cell cell) {
        for (Cell c : openList) {
            if (c.col == cell.col && c.row == cell.row && c.f <= cell.f) {
                return true;
            }
        }
        return false;
    }

    private void addNeighbours(int i, int j) {
        if(!grid[i][j].solid){
            if(i>=1 && !grid[i-1][j].solid && !grid[i-1][j].checked && !grid[i-1][j].open) {
                openCell(grid[i-1][j]);
                grid[i][j].neighbours.add(grid[i-1][j]);
            }
            if(j>=1 && !grid[i][j-1].solid && !grid[i][j-1].checked && !grid[i][j-1].open) {
                grid[i][j].neighbours.add(grid[i][j-1]);
                openCell(grid[i][j-1]);
            }
            if(i+1<COLS && !grid[i + 1][j].solid && !grid[i + 1][j].checked && !grid[i + 1][j].open) {
                grid[i][j].neighbours.add(grid[i + 1][j]);
                openCell(grid[i + 1][j]);
            }
            if(j+1<ROWS && !grid[i][j+1].solid && !grid[i][j+1].checked && !grid[i][j+1].open) {
                grid[i][j].neighbours.add(grid[i][j+1]);
                openCell(grid[i][j+1]);
            }
        }
    }

}
