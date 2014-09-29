package kraken;

import java.util.ArrayList;
import java.util.List;

public class Tower implements KrakenItem{
	private final char symbol='T';
	
	private String color;
	
	public Tower(String color){
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
		XYLocation kingLocation = state.getKingLocation(color.equals(KrakenColor.WHITE_ITEM)?  "b":"w" );
		int row,column = location.getYCoOrdinate();
		
		//uprigth moves
		for (row = location.getXCoOrdinate()-1;row>-1;row--)
			if (addPosition(state,kingLocation,location,list,new XYLocation(row,column)))
				break;
		for (row = location.getXCoOrdinate()+1;row<state.getRowBoard();row++)
			if (addPosition(state,kingLocation,location,list,new XYLocation(row,column)))
				break;

		// horizontal moves
		row=location.getXCoOrdinate();
		for (column = location.getYCoOrdinate()-1;column>-1;column--)
			if (addPosition(state,kingLocation,location,list,new XYLocation(row,column)))
				break;
		for (column =location.getYCoOrdinate()+1;column<state.getColBoard();column++)
			if (addPosition(state,kingLocation,location,list,new XYLocation(row,column)))
				break;
			
	}

	private boolean addPosition(KrakenState state, XYLocation kingLocation,XYLocation location,
			ArrayList<KrakenMove> list,XYLocation finalLocation){
		
		if (state.distance(kingLocation, location, finalLocation)){
			if (state.isEmpty(finalLocation)){
				list.add(new KrakenMove(location, finalLocation));
				return false;
			}else{
				if (!state.getValue(finalLocation).getColor().equals(color)){
					list.add(new KrakenMove(location, finalLocation));
					return true;
				}else
					return true;
			}
		}else{
			if (state.getValue(finalLocation) != null && !state.getValue(finalLocation).getColor().equals(color)){
				list.add(new KrakenMove(location, finalLocation));
				return true;
			}
		}
		return true;
	}

	

}
