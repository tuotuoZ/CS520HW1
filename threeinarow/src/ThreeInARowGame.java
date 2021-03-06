import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;

/**
 * Java implementation of the 3 in a row game, using the Swing framework.
 *
 * This quick-and-dirty implementation violates a number of software engineering
 * principles and needs a thorough overhaul to improve readability,
 * extensibility, and testability.
 */
public class ThreeInARowGame {
    public static final String GAME_END_NOWINNER = "Game ends in a draw";

    public JFrame gui = new JFrame("Three in a Row");
    public ThreeInARowBlock[][] blocksData;
    public CoordinateButton[][] blocks;
    public JButton reset = new JButton("Reset");
    public JTextArea playerturn = new JTextArea();

    // New fields for the nXm feature
	private final int dimRow, dimCol;


    /**
     * The current player taking their turn
     */
    public String player = "1";
    public int movesLeft;

    /**
     * Starts a new game in the GUI.
     */
    public static void main(String[] args) {
        ThreeInARowGame game = new ThreeInARowGame(5, 5 );
        game.gui.setVisible(true);

    }

    /**
     * Creates a new game initializing the GUI.
     */
    public ThreeInARowGame(int inputRow, int inputCol) {
    	dimRow = inputRow;
    	dimCol = inputCol;

    	// New update for the nXm grid.
    	blocksData =  new ThreeInARowBlock[dimRow][dimCol];
    	blocks  = new CoordinateButton[dimRow][dimCol];
    	movesLeft = dimRow * dimCol;

        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(new Dimension(dimCol*150, dimRow*115));
        gui.setResizable(true);

        JPanel gamePanel = new JPanel(new FlowLayout());
        JPanel game = new JPanel(new GridLayout(dimRow,dimCol));
        gamePanel.add(game, BorderLayout.CENTER);

        JPanel options = new JPanel(new FlowLayout());
        options.add(reset);
        JPanel messages = new JPanel(new FlowLayout());
        messages.setBackground(Color.white);

        gui.add(gamePanel, BorderLayout.NORTH);
        gui.add(options, BorderLayout.CENTER);
        gui.add(messages, BorderLayout.SOUTH);

        messages.add(playerturn);
        playerturn.setText("Player 1 to play 'X'");

        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });

        // Initialize a JButton for each cell of the 3x3 game board.
        for(int row = 0; row<dimRow; row++) {
            for(int column = 0; column<dimCol ;column++) {
				blocksData[row][column] = new ThreeInARowBlock(this);
				// The last row contains the legal moves
				blocksData[row][column].setContents("");
				blocksData[row][column].setIsLegalMove(row == dimRow-1);
                blocks[row][column] = new CoordinateButton(row, column);
                blocks[row][column].setPreferredSize(new Dimension(75,75));
				updateBlock(row,column);
                game.add(blocks[row][column]);
                blocks[row][column].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
						move((CoordinateButton) e.getSource());
                    }
                });
            }
        }

	}

	/**
	 * Draw and update the content for the clicked button in the blcoksData.
	 *
	 * @param row2 The x
	 * @param col2 The y
	 *
	 */
	protected void drawMove(int row2, int col2){
		if(player.equals("1")){
			blocksData[row2][col2].setContents("X");
			blocksData[row2][col2].setIsLegalMove(false);
			updateBlock(row2, col2);
		}
		else{
			blocksData[row2][col2].setContents("O");
			blocksData[row2][col2].setIsLegalMove(false);
			updateBlock(row2, col2);
		}
		if(row2 > 0){
			blocksData[row2-1][col2].setIsLegalMove(true);
			updateBlock(row2-1, col2);
		}
	}

	/**
	 * Check if any player wins the game
	 */

	protected boolean checkWinnter(int row, int col){
		// Check horizontal match first
		if(col - 1 >= 0 && col + 1 < dimCol && blocksData[row][col-1].getContents().equals(blocksData[row][col].getContents()) &&
				blocksData[row][col+1].getContents().equals(blocksData[row][col].getContents())){
			return true;
		}else if(col - 2 >= 0  && blocksData[row][col-2].getContents().equals(blocksData[row][col-1].getContents()) &&
				blocksData[row][col-1].getContents().equals(blocksData[row][col].getContents())){
			return true;
		}else if(col + 2 < dimCol&& blocksData[row][col+2].getContents().equals(blocksData[row][col+1].getContents()) &&
				blocksData[row][col+1].getContents().equals(blocksData[row][col].getContents())){
			return true;
		}

		// Check vertical match
		if(row - 1 >= 0 && row + 1 < dimRow && blocksData[row+1][col].getContents().equals(blocksData[row][col].getContents()) &&
				blocksData[row-1][col].getContents().equals(blocksData[row][col].getContents())){
			return true;
		}else if(row - 2 >= 0 && blocksData[row-1][col].getContents().equals(blocksData[row-2][col].getContents()) &&
				blocksData[row-1][col].getContents().equals(blocksData[row][col].getContents())){
			return true;
		}else if(row + 2 < dimRow && blocksData[row+1][col].getContents().equals(blocksData[row+2][col].getContents()) &&
				blocksData[row+1][col].getContents().equals(blocksData[row][col].getContents())){
			return true;
		}

		// Check diagonal match
		// T is the new-placed button. o represents a checking point. x represents a placeholder and will not be checked.
		if(row - 1 >= 0 && col -1 >= 0 && row + 1 < dimRow && col + 1 < dimCol && (blocksData[row+1][col+1].getContents().equals(blocksData[row][col].getContents()) &&
				blocksData[row-1][col-1].getContents().equals(blocksData[row][col].getContents()) | blocksData[row+1][col-1].getContents().equals(blocksData[row][col].getContents()) &&
				blocksData[row-1][col+1].getContents().equals(blocksData[row][col].getContents()))){
			/*
			o x o
			x T x
			o x o
			 */
			return true;
		}else if(row - 2 >= 0 && col - 2 >= 0 && row + 1 < dimRow && col + 1 < dimCol && blocksData[row-2][col-2].getContents().equals(blocksData[row-1][col-1].getContents()) &&
				blocksData[row-1][col-1].getContents().equals(blocksData[row][col].getContents())){
			/*
			o x x
			x o x
			x x T
			 */
			return true;
		}else if(row - 1 >= 0 && col - 1 >= 0 && row + 2 < dimRow && col + 2 < dimCol && blocksData[row+2][col+2].getContents().equals(blocksData[row+1][col+1].getContents()) &&
				blocksData[row+1][col+1].getContents().equals(blocksData[row][col].getContents())){
			/*
			T x x
			x o x
			x x o
			 */
			return true;
		}else if(row - 2 >= 0 && col - 1 >= 0 && row + 1 < dimRow && col + 2 < dimCol && blocksData[row-2][col+2].getContents().equals(blocksData[row-1][col+1].getContents()) &&
				blocksData[row-1][col+1].getContents().equals(blocksData[row][col].getContents())){
			/*
			x x o
			x o x
			T x x
			 */
			return true;
		}else if(row - 1 >= 0 && col - 2 >= 0 && row + 2 < dimRow && col + 1 < dimCol && blocksData[row+2][col-2].getContents().equals(blocksData[row+1][col-1].getContents()) &&
				blocksData[row+1][col-1].getContents().equals(blocksData[row][col].getContents())){
			/*
			x x T
			x o x
			o x x
			 */
			return true;
		}

		return false;
	}
    /**
     * Moves the current player into the given block.
     *
     * @param block The block to be moved to by the current player
     */
    protected void move(CoordinateButton block) {
		--movesLeft;
		if(movesLeft%2 == 1) {
			playerturn.setText("'X': Player 1");
		} else{
			playerturn.setText("'O': Player 2");
		}
		// Update the block
		int col = block.getCol();
		int row = block.getRow();
		drawMove(row, col);
		// Check winner
		if(movesLeft < dimRow*dimCol-4){
			if(checkWinnter(row, col)){
				playerturn.setText(String.format("Player %s wins!", player));
				endGame();
			}else if(movesLeft ==0){
				playerturn.setText(GAME_END_NOWINNER);
				endGame();
			}
		}
		// Update the player string
		if ("1".equals(player)) {
			player = "2";
		}else{
			player = "1";
		}
    }

    /**
     * Updates the block at the given row and column 
     * after one of the player's moves.
     *
     * @param row The row that contains the block
     * @param column The column that contains the block
     */
    protected void updateBlock(int row, int column) {
	blocks[row][column].setText(blocksData[row][column].getContents());
	blocks[row][column].setEnabled(blocksData[row][column].getIsLegalMove());
    }

    /**
     * Ends the game disallowing further player turns.
     */
    public void endGame() {
		for(int row = 0;row<dimRow;row++) {
			for(int column = 0;column<dimCol;column++) {
				blocks[row][column].setEnabled(false);
			}
		}
    }

    /**
     * Resets the game to be able to start playing again.
     */
    public void resetGame() {
        for(int row = 0;row<dimRow;row++) {
            for(int column = 0;column<dimCol;column++) {
                blocksData[row][column].reset();
				// Enable the bottom row
				blocksData[row][column].setIsLegalMove(row == dimRow-1);
				updateBlock(row,column);
            }
        }
        player = "1";
        movesLeft = dimRow * dimCol;
        playerturn.setText("Player 1 to play 'X'");
    }
}
