import javax.swing.*;
import java.awt.*;


public class Util {

    public static ImageIcon getIcon(String filename, JButton button) {
        ImageIcon icon = new ImageIcon(filename);
        Image originalImage = icon.getImage();
        // Get the button's dimensions
        int buttonWidth = button.getWidth();
        int buttonHeight = button.getHeight();
        // Scale the image to match the button's dimensions
        Image scaledImage = originalImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
        // Create a new ImageIcon with the scaled image
        return new ImageIcon(scaledImage);
    }

    public static int manhattanDistance(Cell c1, Cell c2) {
        return Math.abs(c1.col-c2.col) + Math.abs(c1.row - c2.row);
    }

    public static int diagonalDistance(Cell c1, Cell c2) {
        int dx = Math.abs(c1.col - c2.col);
        int dy = Math.abs(c1.row - c2.row);
        return Math.max(dx, dy);
    }

    public static int euclideanDistanceSquared(Cell c1, Cell c2) {
        int dx = c1.col - c2.col;
        int dy = c1.row - c2.row;
        return dx * dx + dy * dy;
    }

}
