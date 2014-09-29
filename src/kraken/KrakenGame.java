package kraken;

import java.util.ArrayList;
import java.util.List;

public class KrakenGame implements Game<KrakenState, KrakenMove, String> {

	private String OPTIONPLAYERS = "The code is wrong. The code must be: \n"
			+ "0 -> machine - machine\n"
			+ "1 -> machine - human\n"
			+ "2 -> human - machine\n"
			+ "3 -> human - human\n";
	private boolean endGame =false;
	private KrakenState state;
	private KrakenPlayer player1 = null;
	private KrakenPlayer player2 = null;
	private double startTime;
	
	/*typerOfPlayer represent who will player
	 * 0 -> machine - machine
	 * 1 -> machine - human
	 * 2 -> human - machine
	 * 3 -> human - human  */
	public void createPlayer(KrakenState statePlayer,int typeOfPlayer){
		switch (typeOfPlayer) {
		case 0:
			player1 = new MachinePlayer(statePlayer,this,KrakenColor.WHITE_PLAYER);
			player2 = new MachinePlayer(statePlayer,this,KrakenColor.BLACK_PLAYER);
			break;
		case 1:
			player1 = new MachinePlayer(statePlayer,this,KrakenColor.WHITE_PLAYER);
			player2 = new HumanPlayer(statePlayer,this,KrakenColor.BLACK_PLAYER);
			break;
		case 2:
			player1 = new HumanPlayer(statePlayer,this,KrakenColor.WHITE_PLAYER);
			player2 = new MachinePlayer(statePlayer,this,KrakenColor.BLACK_PLAYER);
			break;
		case 3:
			player1 = new HumanPlayer(statePlayer,this,KrakenColor.WHITE_PLAYER);
			player2 = new HumanPlayer(statePlayer,this,KrakenColor.BLACK_PLAYER);
			break;
		default:
			System.out.print(OPTIONPLAYERS);
			break;
		}
	}
	
	public void startGame(){
		if(player1 == null || player2 == null) {
			System.out.print("Players not created");
			return;
		}
		startTime = System.currentTimeMillis();
		System.out.println("GOOO!!");
		System.out.println(state);
		player1.start();
		player2.start();
	}
	
	public void endGame() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e){}
		if(endGame)return;
		endGame=true;
		showStadistic();
		System.exit(0); 
	}

	public KrakenState getActualState(){
		return state;
	}
	
	@Override
	public KrakenState getInitialState() {
		state=new KrakenState();
		putItems();
		return state;
	}

	public KrakenState getInitialState(int row,int col) {
		state=new KrakenState(row,col);
		putItems();
		return state;
	}
	
	public void startingInAGivenState(KrakenState determinatedState) {
		state=new KrakenState();
		state = determinatedState;
	}

	@Override
	public String[] getPlayers() {
		return new String[] { KrakenState.W, KrakenState.B };
	}

	@Override
	public String getPlayer(KrakenState state) {
		return state.getPlayerToMove();
	}

	@Override
	public List<KrakenMove> getActions(KrakenState state) {
		return state.actions(state.getPlayerToMove().toLowerCase());
		
	}

	@Override
	public KrakenState getResult(KrakenState state, KrakenMove action) {
		state.moveItem(action.getInitialPlace(), action.getFinalPlace());
		return state;
	}

	@Override
	public boolean isTerminal(KrakenState state) {
		return state.getUtility() != -1;
	}

	// TODO
	@Override
	public double getUtility(KrakenState state, String player) {
		return -1.0;
	}
	
	
	// PRIVATE METHODS
	//
	//
	
	private void putItems(){
		putBlackItems();
		putWhiteItems();
	}

	private void putPawns(String color) {
		int row= color.equalsIgnoreCase(KrakenColor.BLACK_ITEM) ? 1:state.getRowBoard()-2;
		for (int i=0;i<4;i++)
			state.setItem(new Pawn(color), new XYLocation(row,i));
	}

	private void putWhiteItems() {
		int row= state.getRowBoard()-1;
		putPawns(KrakenColor.WHITE_ITEM);
		state.setItem(new King(KrakenColor.WHITE_ITEM), new XYLocation(row,3));
		state.setItem(new Queen(KrakenColor.WHITE_ITEM), new XYLocation(row,2));
		state.setItem(new Bishop(KrakenColor.WHITE_ITEM), new XYLocation(row,1));
		state.setItem(new Tower(KrakenColor.WHITE_ITEM), new XYLocation(row,0));
	}

	private void putBlackItems() {
		int row= 0;
		putPawns(KrakenColor.BLACK_ITEM);
		state.setItem(new King(KrakenColor.BLACK_ITEM), new XYLocation(row,0));
		state.setItem(new Queen(KrakenColor.BLACK_ITEM), new XYLocation(row,1));
		state.setItem(new Bishop(KrakenColor.BLACK_ITEM), new XYLocation(row,2));
		state.setItem(new Tower(KrakenColor.BLACK_ITEM), new XYLocation(row,3));
	}
	
	private void showStadistic() {
		KrakenPlayer winner = state.getPlayerToMove().equals(KrakenColor.BLACK_PLAYER) ? player1:player2;
		ArrayList<KrakenMove> moves = winner.getMoveList();
		System.out.println("The winner is "+winner.getColor());
		System.out.println("Time "+ (System.currentTimeMillis()-startTime)/1000+" seconds");
		System.out.println("Movements of winner: "+ moves.size());
		System.out.println("MediumThinkingTime of the winner: "+winner.getThinkingTime()/moves.size()+" seconds");
		showMoves(moves);
	}
	
	private void showMoves(ArrayList<KrakenMove> moves){
		System.out.println("Moves of the winner:");
		for (KrakenMove move : moves) 
			System.out.println(move.getInitialPlace()+"-->"+move.getInitialPlace());
	}
		
}
