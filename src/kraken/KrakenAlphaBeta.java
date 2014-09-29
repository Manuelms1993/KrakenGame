package kraken;

import java.util.*;



public class KrakenAlphaBeta {

	private KrakenGame game;
	private HeuristicFunction heuristic=null;
	private HeuristicFunction[] heuristics=null;
	private int expandedNodes;
	private int maxNodesExpand;
	
	public KrakenAlphaBeta(KrakenGame game,HeuristicFunction heuristic,int maxNodesExpand){
		this.heuristic=heuristic;
		this.game=game;
		this.maxNodesExpand=maxNodesExpand;
	}

	public KrakenAlphaBeta(KrakenGame game, HeuristicFunction[] heuristicFusion,int maxNodesExpand) {
		this.heuristics=heuristicFusion;
		this.game=game;
		this.maxNodesExpand=maxNodesExpand;
	}

	public KrakenMove makeDecision(KrakenState state) {
		XYLocation rivalKing = state.getKingLocation(state.getPlayerToMove().equals(KrakenColor.WHITE_PLAYER)? KrakenColor.BLACK_ITEM:KrakenColor.WHITE_ITEM);
		List<KrakenMove> myMoves= state.actions(state.getPlayerToMove().toLowerCase());
		
		// kill the rival king
		for (int i = 0; i < myMoves.size(); i++) {
			if (myMoves.get(i).getFinalPlace().equals(rivalKing)) return myMoves.get(i);
		}
		// save my king
		for (int i = myMoves.size()-1; i >=0; i--) {
			if (deleteSuicideMove(game.getResult(state.clone(), myMoves.get(i))))
				myMoves.remove(i);	
		}
		// If all the moves kill my king, execute someone
		if (myMoves.size()==0){
			System.out.println("<------CHECK MATE------>");
			myMoves= game.getActions(state);
			return myMoves.get( (int) (Math.random() * myMoves.size() ));
		}
		
		// alpha-Beta search
		expandedNodes = 0;
		double maxValue = Double.NEGATIVE_INFINITY;
		String player = state.getPlayerToMove();
		KrakenMove [] moves = new KrakenMove[myMoves.size()];
		double [] values = new double[myMoves.size()];
		int index = 0;
		for (KrakenMove move : myMoves) {
			double value = minValue(game.getResult(state.clone(), move), player,Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
			if (value >= maxValue) 
				maxValue = value;
			moves[index]= move;
			values[index++]= value;
			expandedNodes = 0;
		}
		return getMaxValue(maxValue,moves,values,state);
	}
	public double maxValue(KrakenState state, String player, double alpha, double beta) {
		if (expandedNodes==maxNodesExpand || game.isTerminal(state)){
			if (heuristics==null)
				return heuristic.h(state);
			else
				return getHeuristicFusion(state);		
		}
		expandedNodes++;
		double value = Double.NEGATIVE_INFINITY;
		for (KrakenMove KrakenMove : game.getActions(state)) {
			value = Math.max(value, minValue( //
					game.getResult(state.clone(), KrakenMove).clone(), player, alpha, beta));
			if (value >= beta)
				return value;
			alpha = Math.max(alpha, value);
		}
		return value;
	}

	public double minValue(KrakenState state, String player, double alpha, double beta) {
		if (expandedNodes==maxNodesExpand || game.isTerminal(state)){
			if (heuristics==null)
				return heuristic.h(state);
			else
				return getHeuristicFusion(state);		
		}
		expandedNodes++;

		double value = Double.POSITIVE_INFINITY;
		for (KrakenMove KrakenMove : game.getActions(state)) {
			value = Math.min(value, maxValue(
					game.getResult(state.clone(), KrakenMove).clone(), player, alpha, beta));
			if (value <= alpha)
				return value;
			beta = Math.min(beta, value);
		}
		return value;
	}
	
	/*
	 * *
	 * *
	 * * PRIVATE METHODS
	 */
	
	private boolean deleteSuicideMove(KrakenState state) {
		XYLocation myKing = state.getKingLocation(state.getPlayerToMove().toLowerCase());
		List<KrakenMove> rivalMoves= state.actions(state.getPlayerToMove().equals(KrakenColor.WHITE_PLAYER)? KrakenColor.BLACK_ITEM:KrakenColor.WHITE_ITEM);
		for (int j = 0; j < rivalMoves.size(); j++) {
			if (rivalMoves.get(j).getFinalPlace().equals(myKing)){
				return true;
			}
		}
		return false;
	}
	
	private double getHeuristicFusion(KrakenState state) {
		return heuristics[0].h(state)* 0.5 +
			   heuristics[1].h(state) +
			   heuristics[2].h(state);
	}
	
	private KrakenMove getMaxValue(double maxValue, KrakenMove[] moves,
			double[] values,KrakenState state) {
		for (int i = 0; i < values.length; i++) {
			if (values[i]==maxValue){
				KrakenItem item = state.getValue(moves[i].getInitialPlace());
				if (item instanceof Pawn) values[i]+= Math.random() * 5 + 1;
				if (item instanceof Bishop) values[i]+= Math.random() * 0.7;
				if (item instanceof Tower) values[i]+= Math.random() * 0.5;
				if (item instanceof Queen) values[i]+= Math.random() * 0.1;
				if (item instanceof King) values[i]+= Math.random() * 0.001;
			}
		}
		return moves[getIndexOfMaxValue(values)];
	}

	private int getIndexOfMaxValue(double[] values) {
		double value=Double.NEGATIVE_INFINITY;
		int index=0;
		for (int i = 0; i < values.length; i++) {
			if (values[i]>= value){ 
				index=i;
				value=values[i];
			}
		}
		return index;
	}


}
