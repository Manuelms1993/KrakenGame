package kraken;

import java.util.ArrayList;
import java.util.List;

public class Pawn implements KrakenItem{
	private final char symbol='P';
	
	private String color;
	
	public Pawn(String color){
		this.color=color;

	}
	
	@Override
	public char getSymbol() {
		return symbol;
	}

	@Override
	public String getColor() {
		return color;
	}
	
	@Override
	public List<KrakenMove> getMoves(KrakenState state,XYLocation location) {
		ArrayList<KrakenMove> list = new ArrayList<KrakenMove>();
		checkPosition(state,list,location);
		return list;
	}

	@Override
	public boolean legalMove(KrakenState state,XYLocation initialLocation,
			XYLocation finalLocation){
		for (KrakenMove move : getMoves(state,initialLocation))
			if (move.getFinalPlace().equals(finalLocation))
				return true;
		return false;
	}

	// private methods
	//
	//

	private void checkPosition(KrakenState state,ArrayList<KrakenMove> list,XYLocation location) {
		int offset = getColor().equals(KrakenColor.WHITE_ITEM)? -1:1;
		int x = location.getXCoOrdinate()+offset;
		int y = location.getYCoOrdinate();
		
		if (location.getXCoOrdinate()==0 || location.getXCoOrdinate()==state.getRowBoard()-1)
			return;
		// front move
		if (state.isEmpty(x, y))
			list.add(new KrakenMove(location,new XYLocation(x,y)));
		// Right move
		if (y+1 < state.getColBoard() &&
				!state.isEmpty(x, y+1) &&
				!state.getValue(x,y+1).getColor().equals(color)
				)
			list.add(new KrakenMove(location,new XYLocation(x,y+1)));
		// left move
		if (y-1 >= 0 &&
				!state.isEmpty(x, y-1) &&
				!state.getValue(x,y-1).getColor().equals(color)
				)
			list.add(new KrakenMove(location,new XYLocation(x,y-1)));
	}
}
