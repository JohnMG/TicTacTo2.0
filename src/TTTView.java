/* Author: John Massy-Greene
 * Program: TicTacTo 2.0
*/
import javax.swing.JFrame;
import javax.swing.JPanel;
//import javax.swing.SwingUtilities;
import javax.swing.JLabel;
//import java.awt.event.*;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.ImageIcon;
//import javax.swing.AbstractButton;
//import javax.swing.JOptionPane;
import javax.swing.JTextField;


//TO DO: Find out the commands to make the JFrame resizeable

//According to MVC, this is the view it represents how the users
//see the data.

public class TTTView {

//global variables
	private final int NUMSQUARES = 9;
	private final int ROWCOL = 3;
	private final int BOXSIZE = 100;
	private final int INFOWIDTH = 200;
	private final int INFOHEIGHT = 50;
	private final int butWidth = 100;
	private final int butHeight = 50;

	
//the two main frames required for UI
	public JFrame boardFrame;
	public JFrame initScreen;
	
//the components for the main game
	private JButton[] squares = new JButton[NUMSQUARES];
	public int[] arrayA = new int[9];
	private JButton quit;
	private JButton newGame;
	private JButton resetB;
	private JLabel playerOne;
	private JLabel playerTwo;
	
//components for The inital screen
	private JTextField pOne;
	private JTextField pTwo;
	private JLabel pOneName;
	private JLabel pTwoName;
	private JButton nameOk;
	
//The images that represent the pieces for noughts and crosses
//and the the images to represent how the person won.
	private ImageIcon BLANK = new ImageIcon("images/nomove.jpg");
	private ImageIcon NOUGHT = new ImageIcon("images/nought.jpg");
	private ImageIcon NOUGHTA = new ImageIcon("images/noughtA.jpg");
	private ImageIcon NOUGHTD = new ImageIcon("images/noughtD.jpg");
	private ImageIcon CROSS = new ImageIcon("images/cross.jpg");
	private ImageIcon CROSSA = new ImageIcon("images/crossA.jpg");
	private ImageIcon CROSSD = new ImageIcon("images/crossD.jpg");
	private ImageIcon CROSSDR = new ImageIcon("images/crossDR.jpg");
	private ImageIcon CROSSDL = new ImageIcon("images/crossDL.jpg");
	private ImageIcon NOUGHTDR = new ImageIcon("images/noughtDR.jpg");
	private ImageIcon NOUGHTDL = new ImageIcon("images/noughtDL.jpg");
	
	private int change = 0; 

//the constructor for the view initilises both the frames
	public TTTView() {
		initBoardFrame();
		initInitScreen();
	}

//the inital frame that is used to get the users Names
	private void initBoardFrame() {
		boardFrame = new JFrame();
		boardFrame.setTitle("Noughts and Crosses");
		boardFrame.setSize(800,450);
		boardFrame.setLocationRelativeTo(null);
		boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
//set the layout to be null so you can have better control over where the
//components are
		panel.setLayout(null);
		
		setUpButtons();
		setUpBoardInfo();
		
		for(int i=0; i<NUMSQUARES; i++){
			panel.add(squares[i]);
		}
		panel.add(playerOne);
		panel.add(playerTwo);
		panel.add(quit);
		panel.add(resetB);
		panel.add(newGame);
		
		boardFrame.getContentPane().add(panel);
		//String hello = JOptionPane.showInputDialog("SUP");
		//System.out.println(hello);
	}

//this function sets up the frame that has the main game on it
	private void initInitScreen(){
		initScreen = new JFrame();
		initScreen.setTitle("Noughts and Crosses");
		initScreen.setSize(500,300);
		initScreen.setLocationRelativeTo(null);
		initScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(null);
		
		pOneName = new JLabel("Player One Name");
		pTwoName = new JLabel("Player Two Name");
		pOne = new JTextField(15);
		pTwo = new JTextField(15);
		nameOk = new JButton("OK");
		Dimension size = pOne.getPreferredSize();
		
		pOneName.setBounds(80, 30, 200, 100);
		pTwoName.setBounds(280, 30, 200, 100);
		pOne.setBounds(60, 100, size.width, size.height);
		pTwo.setBounds(260, 100, size.width, size.height);
		nameOk.setBounds(180, 150, 100, 50);

		panel2.add(pOneName);
		panel2.add(pTwoName);
		panel2.add(pOne);
		panel2.add(pTwo);
		panel2.add(nameOk);
		initScreen.getContentPane().add(panel2);
		
		//TO DO: Set the default close operation to say that you cant do that without putting in names
		//that or close the entire program
	}
	
//this sets up the buttons required for the main game
	private void setUpButtons(){
		for(int i=0; i<NUMSQUARES; i++) { 
			squares[i] = new JButton(BLANK);
			setUpSquare(squares[i], i);
		}
		quit = new JButton("QUIT");
		quit.setBounds(400, 155, butWidth, butHeight);
		resetB = new JButton("RESET");
		resetB.setBounds(510, 155, butWidth, butHeight);
		newGame = new JButton("New Game");
		newGame.setBounds(620, 155, (butWidth+10), butHeight);
	}
//this function attempts to automate the setUp and placement of the
//individual squares on the board
	private void setUpSquare(JButton square, int i) {
		int x = detXCoor(i);	
		int y = detYCoor(i);
		x = detStartPos(x);
		y = detStartPos(y);
		square.setBounds(x, y, BOXSIZE, BOXSIZE);
		square.setName(Integer.toString(i));
	}
	
//this is each players info
	private void setUpBoardInfo() {
		playerOne = new JLabel("Loading");
		playerOne.setBounds(400, 50, INFOWIDTH, INFOHEIGHT);
		
		playerTwo = new JLabel("Loading");
		playerTwo.setBounds(600, 50, INFOWIDTH, INFOHEIGHT);
	}
//helper function that uses an mathematical function to place the buttons
	private int detStartPos(int i) {
		i++;
		i = ((i*100)-(50-((i-1)*5)));
		return i;
	}
//determine the x-coordinate of the move that just occurred	
	private int detXCoor(int move) {
		int result = 0;
		move++;
		if((move%ROWCOL)>0) {
			result = ((move%ROWCOL)-1);
		} else {
			result = ((ROWCOL)-1);
		}
		return result;
	}
//determine the y-coordinate of the move that just occurred
	private int detYCoor(int move) {
		int result = 0;
		move++;
		if((move%ROWCOL)>0) {
			result = (move/ROWCOL);
		} else {
			result = ((move/ROWCOL)-1);
		}
		return result;
	}
//set the squares icon according to which player clicked
//the button
	public void doMove(JButton clickBut, int currentPlayer) {
		if(currentPlayer == 0) {
			clickBut.setIcon(NOUGHT);
		} else {
			clickBut.setIcon(CROSS);
		}
	}
//function which sets the icons of the board to show who
//won and how they won
//1-3 = player won in a row
//4-6 = player won in a column
//7-8 = player won in a diagonal
	public void endGame(int vic, int currentPlayer) {
		switch(vic) {
			case 1:
				for(int i=0; i<3; i++) {
					setSquares(NOUGHTA, CROSSA, currentPlayer, i);
				}
				break;
			case 2:
				for(int i=3; i<6; i++) {
					setSquares(NOUGHTA, CROSSA, currentPlayer, i);
				}
				break;
			case 3:
				for(int i=6; i<9; i++) {
					setSquares(NOUGHTA, CROSSA, currentPlayer, i);
				}
				break;
			case 4:
				for(int i=0; i<7; i=i+3) {
					setSquares(NOUGHTD, CROSSD, currentPlayer, i);
				}
				break;
			case 5:
				for(int i=1; i<8; i=i+3) {
					setSquares(NOUGHTD, CROSSD, currentPlayer, i);
				}
				break;
			case 6:
				for(int i=2; i<9; i=i+3) {
					setSquares(NOUGHTD, CROSSD, currentPlayer, i);
				}
				break;
			case 7:
				for(int i=0; i<9; i=i+4) {
					setSquares(NOUGHTDR, CROSSDR, currentPlayer, i);
				}
				break;
			case 8:
				for(int i=2; i<7; i=i+2) {
					setSquares(NOUGHTDL, CROSSDL, currentPlayer, i);
				}
				break;
		}
	}
//helper function for endGame()	
	private void setSquares(ImageIcon A, ImageIcon B, int currentPlayer, int i) {
		if(currentPlayer==0) {
			squares[i].setIcon(A);
		} else {
			squares[i].setIcon(B);
		}
	}
//resets the board to its original state. blank
	public void resetBoard(){
		for(int i=0; i<NUMSQUARES; i++) {
			squares[i].setIcon(BLANK);
		}
	}

//getter functions use to help the controller	
	public JButton getQuit() {
		return this.quit;
	}
	public JButton getSquare(int i) {
		return this.squares[i];
	}
	public JLabel getPlayer(int i) {
		if(i == 0) {
			return this.playerOne;
		} else {
			return this.playerTwo;
		}
	}
	public int getChange() {
		return this.change;
	}
	public void setChange(int i) {
		this.change = i;
	}
	public String getP1Name() {
		return this.pOne.getText();
	}
	public String getP2Name() {
		return this.pTwo.getText();
	}
	public JButton getNameOk() {
		return this.nameOk;
	}
	public JButton getReset() {
		return this.resetB;
	}
	public JButton getNewgame(){
		return this.newGame;
	}
	public JButton[] getButArray() {
		return this.squares;
	}
	
}
