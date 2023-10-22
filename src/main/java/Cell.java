import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class Cell extends JButton {

    Cell previous;
    int col, row;
    int g, h, f;
    boolean start = false;
    boolean goal = false;
    boolean solid;
    boolean open;
    boolean checked;

    boolean isPath;

    List<Cell> neighbours;

    public Cell(int col, int row) {
        this.col=col;
        this.row=row;
        setBackground(Color.white);
        setForeground(Color.black);
        this.neighbours = new ArrayList<>(4);
    }

    public void setStart() {
        setBackground(Color.blue);
        setForeground(Color.white);
        setText("S");
        this.start=true;
        this.solid=false;
    }

    public void setGoal() {
        setBackground(Color.RED);
        setForeground(Color.BLACK);
        setText("X");
        this.goal=true;
        this.solid=false;
    }

    public void setSolid() {
        setBackground(Color.black);
        setForeground(Color.black);
        this.solid=true;
    }

    public void setIsOpen() {
        if(start == false && goal == false && checked == false){
            setBackground(Color.LIGHT_GRAY);
            setForeground(Color.black);

        }
        this.open=true;
    }

    public void setChecked() {
        if(start==false && goal==false) {
            setBackground(Color.orange);
            setForeground(Color.black);
        }
        checked = true;
    }

    public void setAsPath() {
        setBackground(Color.green);
        setForeground(Color.black);
        isPath = true;
    }



}
