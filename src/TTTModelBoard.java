/* Author: John Massy-Greene
 * Program: TicTacTo 2.0
*/

//Using MVC this is the modal. It stores the data and does some of the
//calculations for the controller.

public class TTTModelBoard {

//global variables for the modal
	private final int ROWCOL = 3;
	private final int PLAYERONE = 0;
	private final int PLAYERTWO = 1;
	private final int DIAGS = 2;
	private final char BLANKS = '-';

 //store the tictacto board as a 3x3 array
	char[][] titato = new char[ROWCOL][ROWCOL];
	TTTModelPlayer[] players = new TTTModelPlayer[2];
	TTTModelPlayer one;
	TTTModelPlayer two;
	
//constructor requires the names of both the players
	public TTTModelBoard(String oneName, String twoName) {

		for(int i = 0; i < ROWCOL; i++) {
			for(int j = 0; j < ROWCOL; j++) {
				titato[i][j] = BLANKS;
			}
		}
		one = new TTTModelPlayer(oneName, 0);
		two = new TTTModelPlayer(twoName, 1);
		players[0] = one;
		players[1] = two;
		
	}

//victory condition function. attempt to deduce if the victory occurred
//and how the victory occurred
	public int victoryCond() {
		int result = 0;
		boolean fullCond = false;
//0 = nothing, 1-3 = Rows, 4-6 = Columns, 7&8=Diags, 9 = full
		if(result == 0) {
			result = checkAcross();
		}
		if(result == 0) {
			result = checkDown();
		}
		if(result == 0) {
			result = checkDiag();
		}
		if(result == 0) {
			fullCond = checkFull();
			if(fullCond) {
				result = 9;
			}
		}
		return result;
	}
	
	

//this function checks if the move a player wants to do has already
//been done. If the square matches the blank it returns true which
//means the move is valid
	
	public boolean checkTaken(int x, int y) {
		boolean result = false;
		if(titato[x][y] == BLANKS) {
			result = true;
		}
		return result;
	}
	
	public int checkAcross() {
		int found = 0;
		boolean same;
//this function checks all the rows
//it stores the moves performed in a rows
//in an array and checks if they were performed by the same player
		char[] checker = new char[3];
		int x = 0;
		int y = 0;
		
		while((found==0) && (y<3)) {

			for(x=0; x<ROWCOL; x++){
				checker[x] = titato[x][y];
			}

			same = checkSame(checker);
			if(same == true) {
				found = (y+1);
			}
			y++;
		}
		return found;
	}
	
	public int checkDown() {
		int found = 0;
		boolean same;
//this function checks all the columns
//it stores the moves performed in a column
//in an array and checks if they were performed by the same player
		char[] checker = new char[3];
		int x = 0;
		int y = 0;
		
		while((found==0) && (x<3)) {
//
			for(y=0; y<ROWCOL; y++){
				checker[y] = titato[x][y];
			}
//
			same = checkSame(checker);
			if(same == true) {
				found = (x+4);
			}
			x++;
		}
		return found;
	}
//same function as checkAcross and checkDown
//but for the diagonals
	public int checkDiag() {
		int found = 0;
		boolean same;
		char[][] checker = new char[2][3];
		int x = 0;
		int y = 0;
		
		for(x=0; x<ROWCOL; x++, y++){
			checker[0][x] = titato[x][y];
		}
		for(x=2, y=0; y<ROWCOL; x--,y++) {
			checker[1][y] = titato[x][y];
		}
		for(x=0; ((x<DIAGS) && (found==0)); x++) {
			same = checkSame(checker[x]);
			if(same){
				found = (x+7);
			}
		}
		return found;
	}
//helper function that checks if the moves performed
//were done by the same person
	public boolean checkSame(char[] ArrOfMove) {
		int x;
		boolean same = true;
		for(x=0; (x<(ROWCOL-1)&&(same)); x++){
			if(ArrOfMove[x] == ArrOfMove[x+1]) {
				if(ArrOfMove[x]!=BLANKS){
					same = true;
				} else {
					same = false;
				}
			} else {
				same = false;
			}
		}
		return same;
	}

//checks if the board is full of moves
	public boolean checkFull(){
		boolean result = true;
		for(int x=0; x<ROWCOL; x++) {
			for(int y=0; y<ROWCOL; y++) {
				if(titato[x][y] == BLANKS){
					result = false;
				}
			}
		}
		return result;
	}
//records a move with the board if its valid
	public boolean doMove(int p, int move){
		boolean validMove = false;
		int x = 0;
		int y = 0;
		move++;
		
		x = detXCoor(move);
		y = detYCoor(move);
		validMove = checkTaken(x,y);
		if(validMove == true) {
			titato[x][y] = players[p].getPiece();
		}
		return validMove;
	}
//helper method to determine the x-coordinate
	public int detXCoor(int move) {
		int result = 0;
		if((move%ROWCOL)>0) {
			result = ((move%ROWCOL)-1);
		} else {
			result = ((ROWCOL)-1);
		}
		return result;
	}
//helper methods to determine the y-coordinate
	public int detYCoor(int move) {
		int result = 0;
		if((move%ROWCOL)>0) {
			result = (move/ROWCOL);
		} else {
			result = ((move/ROWCOL)-1);
		}
		return result;
	}
//resets the board back to its original state
	public void resetBoard(){
		for(int x=0; x<ROWCOL; x++) {
			for(int y=0; y<ROWCOL; y++) {
				titato[x][y] = BLANKS;
			}
		}
	}
//resets each players victory count
	public void resetVictories(){
		players[PLAYERONE].resetVictory();
		players[PLAYERTWO].resetVictory();
	}

//prepares the messages that display who won the game
//and how they won it
	public String endGame(int vic, int move, int currentPlayer) {

		int x;
		int y;
		String msg;
		int player;
		move++;
		
		x = detXCoor(move);
		y = detYCoor(move);
		move = titato[x][y];
		player = currentPlayer;
		msg = (players[player].getName()); 
		
		switch(vic) {
			case 1:
				msg = msg+" won on the 1st row";
				break;
			case 2:
				msg = msg+" won on the 2nd row";
				break;
			case 3:
				msg = msg+" won on the 3rd row";
				break;
			case 4:
				msg = msg+" won on the 1st column";
				break;
			case 5:
				msg = msg+" won on the 2nd column";
				break;
			case 6:
				msg = msg+" won on the 3rd column";
				break;
			case 7:
				msg = msg+" won on the upper left diagonal";
				break;
			case 8:
				msg = msg+" won on the upper right diagonal";
				break;
			case 9:
				msg = "The board is full. DRAW!";
				break;
		}
		return msg;
	}
}



