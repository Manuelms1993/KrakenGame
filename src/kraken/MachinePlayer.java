package kraken;

import java.util.List;
import java.util.Scanner;

public class MachinePlayer extends KrakenPlayer{
	
	private HeuristicFunction heuristicFunction=null;
	private HeuristicFunction [] heuristicFusion=null;
	private KrakenAlphaBeta search;
	private int maxNodesExpand;
	
	public MachinePlayer(KrakenState state,KrakenGame game,String color){
		super(state,game,color);
		getDifficulty();
	}
	
	public void run(){
		while (state.canIMove(this)) {
			if (game.isTerminal(state)){
				state.changePlayerToMove();
				break;
			}
			System.out.println("PLAYER "+color+" MOVE...");
			state = heuristicFunction == null? executeAleatoryMove():execute();
			System.out.println(state);
			state.changePlayerToMove();
		}
		game.endGame();
	}
	
	/*
	//
	//private methods
	//
	*/

	@SuppressWarnings("resource")
	private void getDifficulty(){
		System.out.print("YOU MUST ENTER THE HEURISTIC OF MACHINE PLAYER "+color+ "\n"
				+ "Less than zero -> Aleatory Moves\n"
				+ "0 -> ItemPuntuationHeuristic\n"
				+ "1 -> CentralBoardControlHeuristic\n"
				+ "2 -> ItemProximityHeuristic\n"
				+ "Bigger than "+ (MachinePlayerDifficulty.numberOfHeuristics()-1) + " -> All heuristic\n");
		System.out.println("Choose:");
		int difficulty = new Scanner(System.in).nextInt();
		setHeuristic(difficulty);
	}
	
	private void setHeuristic(int number) {
		if (number<0){
			return;
		}
		MachinePlayerDifficulty difficulty = MachinePlayerDifficulty.getDifficulty(number);
		if (number >= MachinePlayerDifficulty.numberOfHeuristics()){
			maxNodesExpand = difficulty.getMaxNodesExpand();
			heuristicFusion = difficulty.getHeuristicFusion();
			heuristicFunction = heuristicFusion[0];
			search = new KrakenAlphaBeta(game,heuristicFusion,maxNodesExpand);
		}else{
			maxNodesExpand = difficulty.getMaxNodesExpand();
			heuristicFunction = difficulty.getHeuristic();
			search = new KrakenAlphaBeta(game,heuristicFunction,maxNodesExpand);
		}
		
	}

	private KrakenState execute() {
		double start = System.currentTimeMillis();
		KrakenMove action = search.makeDecision(state.clone());
		saveEstadistics(start,action);
		showMove(action);
		return game.getResult(state, action);
	}

	private KrakenState executeAleatoryMove() {
		List<KrakenMove>moves =  state.actions(state.getPlayerToMove().toLowerCase());
		double start = System.currentTimeMillis();
		KrakenMove action = moves.get( (int)(Math.random() * moves.size() ));
		saveEstadistics(start,action);
		showMove(action);
		return game.getResult(state, action);
	}
	
	private void showMove(KrakenMove action) {
		String colLetter ="ABCDEFGHIJ";
		System.out.println("The machine move "+
							action.getInitialPlace().getXCoOrdinate()+
							colLetter.charAt(action.getInitialPlace().getYCoOrdinate())+
							" to "+
							action.getFinalPlace().getXCoOrdinate()+
							colLetter.charAt(action.getFinalPlace().getYCoOrdinate())
							);
	}
	
	private void saveEstadistics(double start, KrakenMove action) {
		thinkingTime += System.currentTimeMillis()-start;
		moveList.add(action);
	}

	
}
