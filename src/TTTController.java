/* Author: John Massy-Greene
 * Program: TicTacTo 2.0
*/
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Using MVC. This is the controller class. It connects the view
//and the model.

public class TTTController {

	//fixed global variables
	private final int NUMSQUARES = 9;
	private final int PLAYERONE = 0;
	private final int PLAYERTWO = 1;
	private final String BORDERB = "<html><table border=\"1\"><tr><td><b>";
	private final String EBORDER = "</td></td></html>";
	private final String ENDBBR = "</b><br>";
	private final String pOneWrong = "Player One: Only alphanumeric(a-z0-9) characters are allowed";
	private final String pTwoWrong = "Player Two: Only alphanumeric(a-z0-9) characters are allowed";
	private final String bothWrong = "Both players: Only alphanumeric(a-z0-9) characters are allowed";
	private final String invalidMove = "Sorry this move is taken. Try again.";
	
//things the controller needs to see
	private TTTView view;
	private TTTModelBoard board;
//an array of warning messages for getting the users names
	private String[] nameWrong = {pOneWrong, pTwoWrong, bothWrong}; 
	
//the current player
	private int currentPlayer;

//This constructs the controller but you can't set up the modal until
//you have the players names. Newgame is disabled because there's
//no use for it yet.
	public TTTController() {
		this.view = new TTTView();
		setUpButtonListeners();
		this.view.boardFrame.setVisible(false);
		this.view.initScreen.setVisible(true);
		view.getNewgame().setEnabled(false);
	}

//this function add listeners to all the buttons in the view
//the listeners are added here so the controller can instruct
//the view on what to do when the buttons are pressed
	private void setUpButtonListeners() {
		
//this is the listener for the Ok button when the users first input their names
		view.getNameOk().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				gettingPlayersNames();
			}
		});

//this adds listeners for all nine buttons of the tictacto board
		for(int i=0; i<NUMSQUARES; i++) {
			view.getSquare(i).addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
//you have to get the source in order to perform actions on these buttons
//because the board buttons themselves are not named. They're in an array.
					JButton clickBut = (JButton) event.getSource();
					boardButtonClicked(clickBut);
				}
			});
		}
//Listener for the quit game button		
		view.getQuit().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				quitGame();
			}
		});
//listener for the reset game button
		view.getReset().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				resetGame();
			}
		});
//listener for the new game button
		view.getNewgame().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				newGame();
			}
		});
	}
//this is the function that is run when the users input their names
	private void gettingPlayersNames() {
		String oneName = view.getP1Name();
		String twoName = view.getP2Name();
		int verify = verifyNames(oneName, twoName);
//if the names are ok then initiate the board and dispose of the introduction
//screens JFrame and make the board visible and ready to use.
		if(verify ==  0) {
			this.board = new TTTModelBoard(oneName, twoName);
			
			this.view.initScreen.setVisible(false);
			this.view.boardFrame.setVisible(true);
			this.view.initScreen.dispose();
			setPlayerInfo();
			this.currentPlayer = 0;
			currentPlayerMsg();
//if the names are not ok then show an error message
		} else {
			JOptionPane.showMessageDialog(this.view.initScreen ,nameWrong[verify-1]);
		}
 	}
	
//0 = Nothing, 1 = 1st Name wrong, 2 = 2nd Name wrong, 3 = both wrong
//This function uses regular expressions to make sure the names are
//only alphanumeric characters
	private int verifyNames(String oneName, String twoName){
		int result = 0;
		boolean N1 = false;
		boolean N2 = false;
		String pattern = "(^\\w+$)";
		Pattern r = Pattern.compile(pattern);
		Matcher m;
		
		m = r.matcher(oneName);
		if(m.find()){
			N1 = true;
		}
		m = r.matcher(twoName);
		if(m.find()){
			N2 = true;
		}
		if((N1 == false) && (N2 == true)) {
			result = 1;
		} else if((N1 == true) && (N2 == false)) {
			result = 2;
		} else if((N1 == false) && (N2 == false)) {
			result = 3;
		}
		
		return result;
	}

//this is the function that is run when any of the board buttons are clicked
//Since the game is basically event driven(user needs to click the buttons for the game to run)
//the backbone of the game progression logic is stored here.
	private void boardButtonClicked(JButton clickBut){
//the buttons names are essentially the move on the board to be done
//the move is then passed to the modal to see if its valid
		int move = Integer.parseInt(clickBut.getName());
		boolean validMove = false;
		int fullVic = 0;

//if the move is valid then register the move with the view
//else show an error message
		validMove = board.doMove(currentPlayer, move);
		if(validMove){
			view.doMove(clickBut, currentPlayer);
			fullVic = board.victoryCond();
			if(fullVic != 0 ) {
//if victory or the board is full then handle it in this function
//if neither of the above conditions have been achieved then
//switch players and keep going
				handleFullVic(fullVic, move);
				view.getNewgame().setEnabled(true);
			} else {
				switchPlayers();
				currentPlayerMsg();
			}
		} else {
			JOptionPane.showMessageDialog(this.view.boardFrame, invalidMove);
		}
	}
//this function handles the code required to end the game	
	private void handleFullVic(int fullVic, int move) {
		String msg = board.endGame(fullVic, move, currentPlayer);
//show the result at the end of the game and update the winners victory count
//switch players so the loser can go first in the next game
		view.endGame(fullVic, currentPlayer);
		JOptionPane.showMessageDialog(this.view.boardFrame, msg);
		board.players[currentPlayer].addVictory();
		setPlayerInfo();
		switchPlayers();
	}

//switch the person whose move it is
	private void switchPlayers() {
		if(currentPlayer == PLAYERONE) {
			currentPlayer = PLAYERTWO;
		} else {
			currentPlayer = PLAYERONE;
		}
	}
	
//this function formats and sets the text in the view that tells
//the players what their names are, whether their noughts or crosses
//and how many victories they have
	private void setPlayerInfo() {
		String playerOneMsg =
				BORDERB+board.players[PLAYERONE].getName()+ENDBBR+
				board.players[PLAYERONE].pieceAndVictory()+EBORDER;
		String playerTwoMsg =
				BORDERB+board.players[PLAYERTWO].getName()+ENDBBR+
				board.players[PLAYERTWO].pieceAndVictory()+EBORDER;

		view.getPlayer(PLAYERONE).setText(playerOneMsg);
		view.getPlayer(PLAYERTWO).setText(playerTwoMsg);
	}
//exits the entire program	
	private void quitGame() {
		System.exit(0);
	}
//resets the game back to it initial state
	private void resetGame() {
		board.resetBoard();
		view.resetBoard();
		board.resetVictories();
		setPlayerInfo();
		this.currentPlayer = 0;
		currentPlayerMsg();
	}
//Resets the board to play again but doesn't erase
//whose turn it is or how many victories each player has
	private void newGame() {
		board.resetBoard();
		view.resetBoard();
		view.getNewgame().setEnabled(false);
		currentPlayerMsg();
	}
//function to show whose turn it is
	private void currentPlayerMsg() {
		String msg = board.players[currentPlayer].getName()+" it's your turn.";
		JOptionPane.showMessageDialog(this.view.boardFrame, msg);
	}

}
