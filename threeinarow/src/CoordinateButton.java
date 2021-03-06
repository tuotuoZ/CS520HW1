import javax.swing.JButton;

/**
 * The CoordinateButton class is a JButton with X and Y coordinates information.
 */
public class CoordinateButton extends JButton{
    private final int row;
    private final int col;

    public CoordinateButton(int rowInput, int colInput){
        super();
        row = rowInput;
        col = colInput;
    }

    public int getRow() {return this.row;}

    public int getCol() {return this.col;}
}
