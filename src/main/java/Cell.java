import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Cell extends JButton {

    Cell previous;
    int col, row;
    int g, h, f;
    boolean isStart = false;
    boolean isGoal = false;
    boolean isSolid;
    boolean isOpen;
    boolean isChecked;

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
        this.setIcon(Util.getIcon("start.png", this));
        this.isStart=true;
        this.isSolid=false;
    }

    public void setGoal() {
        this.setIcon(Util.getIcon("flag.png", this));
        this.isGoal=true;
        this.isSolid=false;
    }

    public void setSolid() {
        setBackground(Color.black);
        setForeground(Color.black);
        this.isSolid=true;
    }

    public void setIsOpen() {
        if(isStart == false && isGoal == false && isChecked == false){
            setBackground(Color.LIGHT_GRAY);
            setForeground(Color.black);
        }
        this.isOpen=true;
    }

    public void setChecked() {
        if(isStart==false && isGoal==false) {
            setBackground(Color.orange);
            setForeground(Color.black);
        }
        this.isChecked = true;
    }

    public void setAsPath() {
        setBackground(Color.green);
        setForeground(Color.black);
        this.isPath = true;
    }



}
