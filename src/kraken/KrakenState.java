package kraken;

import java.util.ArrayList;
import java.util.List;

public class KrakenState implements Cloneable {
	public static final String W = KrakenColor.WHITE_PLAYER;
	public static final String B = KrakenColor.BLACK_PLAYER;
		
	private KrakenItem [][] board;
	private String playerToMove ="W";
	private int rowBoard=6;
	private int colBoard=4;
	private double utility = -1;

	public KrakenState(){
		board= new KrakenItem[rowBoard][colBoard];
	}
	
	public KrakenState(int row,int col){
		checkBoardDimension(row,col);
		board= new KrakenItem[rowBoard][colBoard];
	}

	public String getPlayerToMove() {
		return playerToMove;
	}
	
	public double getUtility() {
		return utility;
	}
	
	public int getRowBoard(){
		return rowBoard;
	}
	
	public int getColBoard(){
		return colBoard;
	}
	
	public boolean isEmpty(XYLocation location) {
		return board[location.getXCoOrdinate()][location.getYCoOrdinate()] == null;
	}

	public boolean isEmpty(int row, int col) {
		return board[row][col] == null;
	}
	
	public KrakenItem getValue(int row, int col) {
		return board[row][col];
	}
	
	public KrakenItem getValue(XYLocation location) {
		return board[location.getXCoOrdinate()][location.getYCoOrdinate()];
	}
	
	public XYLocation getKingLocation(String color) {
		for (int i = 0; i < rowBoard; i++) 
			for (int j = 0; j < colBoard; j++)
				if (getValue(i,j) instanceof King && getValue(i,j).getColor().equals(color))
					return new XYLocation(i,j);
		return null;
	}
	
	public boolean distance(XYLocation kingLocation,XYLocation initialLocation,XYLocation finalLocation) {
		int initialDistance = calculateDistance(kingLocation,initialLocation);
		int finalDistance = calculateDistance(kingLocation,finalLocation);
		return finalDistance<initialDistance;
	}
	
	public boolean checkRange(XYLocation finalLocation) {
		if (finalLocation.getXCoOrdinate()<0 || finalLocation.getXCoOrdinate()>=rowBoard)
			return false;
		if (finalLocation.getYCoOrdinate()<0 || finalLocation.getYCoOrdinate()>=colBoard)
			return false;
		return true;
	}
	
	public boolean setItem(KrakenItem item,XYLocation location) {
		if (!testLocation(location) || board[location.getXCoOrdinate()][location.getYCoOrdinate()] !=null) 
			return false;
		else{
			board[location.getXCoOrdinate()][location.getYCoOrdinate()]=item;
			return true;
		}
	}
	
	public boolean removeItem(XYLocation location) {
		if (!testLocation(location) || board[location.getXCoOrdinate()][location.getYCoOrdinate()] ==null)
			return false;
		else{
			board[location.getXCoOrdinate()][location.getYCoOrdinate()]=null;
			return true;
		}
	}
	
	public boolean moveItem(XYLocation initialLocation,XYLocation finalLocation) {
		if (!testLocation(initialLocation) || !testLocation(finalLocation)
				|| board[initialLocation.getXCoOrdinate()][initialLocation.getYCoOrdinate()] ==null)
			return false;
		
		if (board[initialLocation.getXCoOrdinate()][initialLocation.getYCoOrdinate()] instanceof Pawn &&
				(finalLocation.getXCoOrdinate()==0 || finalLocation.getXCoOrdinate()==rowBoard-1))
			board[finalLocation.getXCoOrdinate()][finalLocation.getYCoOrdinate()]= new Queen(playerToMove.toLowerCase());
		else
			board[finalLocation.getXCoOrdinate()][finalLocation.getYCoOrdinate()]=
			board[initialLocation.getXCoOrdinate()][initialLocation.getYCoOrdinate()];
			
			removeItem(initialLocation);
			analyzeUtility();
			return true;
	}
	
	/*
	 * Thread methods
	 */
	
	public synchronized boolean canIMove(KrakenPlayer player){
		if (!player.getColor().equals(playerToMove)){
			try{
				wait();
			}catch(InterruptedException e) {}
		}
		
		return true;
	}
	
	public synchronized void changePlayerToMove() {
		playerToMove = playerToMove.equals("W")?  B : W;
		notify();
	}
	
	/*
	 * Object override
	 * 
	 */
	
	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		String aux = new String();
		strBuilder.append("    ");
		header(colBoard,new String ("ABCDEFGHIJ"),strBuilder);
		
		for (int i = 0; i < rowBoard; i++) {
			for (int j = 0; j < colBoard; j++) {

				aux =  j==0? i+" ":"" ;
				if (getValue(i, j) != null){
					strBuilder.append(aux + " " + getValue(i, j).getSymbol());
					strBuilder.append(getValue(i, j).getColor());
					strBuilder.append(" ");
				}else{
					strBuilder.append(aux + "  ");
					strBuilder.append("- ");
				}
			}
			strBuilder.append("\n");
		}
		return strBuilder.toString();
	}

	@Override
	public KrakenState clone() {
		KrakenState copy = null;
		try {
			copy = (KrakenState) super.clone();
			KrakenItem [][] newBoard = new KrakenItem[rowBoard][colBoard];
			for(int i =0;i<rowBoard;i++)
				for(int j =0;j<colBoard;j++)
					newBoard[i][j]=board[i][j];
			copy.board =newBoard;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace(); // should never happen...
		}
		return copy;
	}
	
	@Override
	public boolean equals(Object anObj) {
		KrakenState obj = (KrakenState) anObj;
		for(int i =0;i<rowBoard;i++)
			for(int j =0;j<colBoard;j++)
				if (obj.board[i][j]!=board[i][j])
					return false;
		return true;
	}
	
	public List<KrakenMove> actions(String color) {
		ArrayList<KrakenMove> moves=new ArrayList<KrakenMove>();
		for(int i = 0;i<rowBoard;i++)
			for(int j = 0;j<colBoard;j++)
				if (board[i][j] != null  && 
					board[i][j].getColor().equals(color))
					addActionsItem(new XYLocation(i,j),moves);
		return moves;
	}
	
	/****
	 * 
	 * heuristics
	 */
	
	public double itemProximity(){
		if (getKingLocation(getPlayerToMove().toLowerCase())==null) return -10000000;
		if (getKingLocation(getPlayerToMove() == "W"?KrakenColor.BLACK_ITEM:KrakenColor.WHITE_ITEM)==null) return 10000000;
		int myPuntuation=0;
		String itemColor = playerToMove == W ? KrakenColor.WHITE_ITEM:KrakenColor.BLACK_ITEM;
		for(int i = 0;i<rowBoard-1;i++)
			for(int j = 0;j<colBoard-1;j++)
				if(getValue(i,j) != null && getValue(i,j+1) != null &&
					getValue(i,j).getColor().equals(itemColor) && getValue(i,j+1).getColor().equals(itemColor))
					myPuntuation+=185;
		
		for(int i = 0;i<colBoard-1;i++)
			for(int j = 0;j<rowBoard-1;j++)
				if(getValue(j,i) != null && getValue(j,i+1) != null &&
					getValue(j,i).getColor().equals(itemColor) && getValue(j,i+1).getColor().equals(itemColor))
					myPuntuation+=185;
		return myPuntuation;
	}
	
	public double itemPuntuation() {
		if (getKingLocation(getPlayerToMove().toLowerCase())==null) return -10000000;
		if (getKingLocation(getPlayerToMove() == "W"?KrakenColor.BLACK_ITEM:KrakenColor.WHITE_ITEM)==null) return 10000000;
		int p=0,b=0,r=0,q=0,k=0;
		int value=0;
		String itemColor = playerToMove == W ? KrakenColor.BLACK_ITEM:KrakenColor.WHITE_ITEM;
		for(int i = 0;i<rowBoard;i++)
			for(int j = 0;j<colBoard;j++)
				if (board[i][j] != null && board[i][j].getColor().equals(itemColor)){
					if (board[i][j] instanceof Pawn) p+=1;
					if (board[i][j] instanceof Bishop) b+=1;
					if (board[i][j] instanceof Tower) r+=1;
					if (board[i][j] instanceof Queen) q+=1;
					if (board[i][j] instanceof King) k+=1;
				}else{
					if (board[i][j] instanceof Pawn) value+=10;
					if (board[i][j] instanceof Bishop) value+=100;
					if (board[i][j] instanceof Tower) value+=300;
					if (board[i][j] instanceof Queen) value+=10000;
					if (board[i][j] instanceof King) value+=1000000;
				}
		if (b == 0) value +=100;
		if (r == 0) value +=300;
		if (q == 0) value +=10000;
		if (k == 0) value +=1000000;
		
		return (value + ( (4-p)*10) );
	}

	public double centerControl() {
		if (getKingLocation(getPlayerToMove().toLowerCase())==null) return -10000000;
		if (getKingLocation(getPlayerToMove() == "W"? KrakenColor.BLACK_ITEM:KrakenColor.WHITE_ITEM)==null) return 10000000;
		int myPuntuation=0;
		String itemColor = playerToMove == W ? KrakenColor.WHITE_ITEM:KrakenColor.BLACK_ITEM;
		for(int i = 2;i<4;i++)
			for(int j = 0;j<colBoard;j++)
				if (board[i][j] != null && board[i][j].getColor().equals(itemColor)){
					myPuntuation+=100;
				}
		return myPuntuation;
	}
	
	//
	// PRIVATE METHODS
	//
	
	private void analyzeUtility() {
		if (getKingLocation( playerToMove.equals(KrakenColor.WHITE_PLAYER)? KrakenColor.BLACK_ITEM:KrakenColor.WHITE_ITEM) == null || actions(playerToMove.toLowerCase()).size()<1)
			utility = playerToMove.equals("W")? 1:0;
	}
	
	private boolean testLocation(XYLocation location) {
		if (location.getXCoOrdinate()<0 || location.getXCoOrdinate()>rowBoard-1)
			return false;
		if (location.getYCoOrdinate()<0 || location.getYCoOrdinate()>colBoard-1)
			return false;
		return true;
	}
	
	private void addActionsItem(XYLocation location,ArrayList<KrakenMove> moves) {
		moves.addAll(board[location.getXCoOrdinate()][location.getYCoOrdinate()].getMoves(this,location));
	}
	
	private void header(int columns, String string, StringBuilder strBuilder) {
		for (int i = 0; i < columns; i++)
			strBuilder.append(string.charAt(i)+"   ");
		strBuilder.append("\n");
	}
	
	private void checkBoardDimension(int row, int col) {
		rowBoard=row;
		colBoard=col;
		if (row>10)rowBoard=10;
		if (row<6)rowBoard=6;
		if (col>10)colBoard=10;
		if (col<4)colBoard=4;
	}
	
	private int calculateDistance(XYLocation kingLocation,XYLocation location){
		if (kingLocation == null) return -1;
		return  (location.getYCoOrdinate()-kingLocation.getYCoOrdinate()) *
				(location.getYCoOrdinate()-kingLocation.getYCoOrdinate()) +
				(location.getXCoOrdinate()-kingLocation.getXCoOrdinate())*
				(location.getXCoOrdinate()-kingLocation.getXCoOrdinate()
				);
	}
}
