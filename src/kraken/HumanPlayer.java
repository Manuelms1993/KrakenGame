package kraken;

import java.util.Scanner;

public class HumanPlayer extends KrakenPlayer {

	private static final Scanner SCANNER = new Scanner(System.in);

	public HumanPlayer(KrakenState state,KrakenGame game,String color){
		super(state,game,color);
	}
	
	public void run(){
		while (state.canIMove(this)) {
			if (game.isTerminal(state)){
				state.changePlayerToMove();
				break;
			}
			System.out.println("PLAYER "+color+" MOVE...");
			execute();
			System.out.println(state);
			state.changePlayerToMove();
		}
		game.endGame();
	}
	
	/*
	// private methods
	//
	*/
	
	private void execute() {
		String letters= "ABCDEFGHIJ";
		double start = System.currentTimeMillis();
		while (true){
			System.out.println("Give me a move (Example -->(5A,4A)  or  (help) ): ");
			String text = SCANNER.nextLine().toUpperCase();
			
			if (text.toLowerCase().equals("help")){
				help();
			}else{
				
				if (checkFail(text)){
					System.out.print("The text is wrong, try again...\n");
				}else{
					XYLocation initialLocation = new XYLocation(
							Integer.parseInt(text.charAt(0)+""),
							letters.indexOf(text.charAt(1))
							);
					XYLocation finalLocation = new XYLocation(
							Integer.parseInt(text.charAt(3)+""),
							letters.indexOf(text.charAt(4))
							);
					if (!state.isEmpty(initialLocation)&& finalLocation != null){
						if (state.getValue(initialLocation).legalMove(state, initialLocation, finalLocation) &&
							state.getValue(initialLocation).getColor().equals(color.toLowerCase())
							){
							state = game.getResult(state, new KrakenMove(initialLocation,finalLocation));
							saveEstadistics(start,new KrakenMove(initialLocation,finalLocation));
							break;
						}
					}else
						System.out.print("Wrong move!!\n");
				}
			}
				
		}
	}
	
	@SuppressWarnings("deprecation")
	private boolean checkFail(String text) {
		if (text.length() != 5) return true;
		if (!Character.isDigit(text.charAt(0))) return true;
		if (!Character.isDigit(text.charAt(3))) return true;
		if (!Character.isJavaLetter(text.charAt(1))) return true;
		if (!Character.isJavaLetter(text.charAt(4))) return true;
		if (text.charAt(2) != ',') return true;
		return false;
	}

	private void help(){
		System.out.println("i am calculate the move...wait please");
		MachinePlayerDifficulty difficulty = MachinePlayerDifficulty.getDifficulty(0);
		KrakenMove action = new KrakenAlphaBeta(game, difficulty.getHeuristicFusion(),difficulty.getMaxNodesExpand()).makeDecision(state.clone());
		showMove(action);
	}

	private void showMove(KrakenMove action) {
		String colLetter ="ABCDEFGHIJ";
		System.out.println("If i were you, i will move ...  "+
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



