package kraken;

import java.util.ArrayList;
import java.util.List;



public class King implements KrakenItem{
	private final char symbol='K';
	
	private String color;
	
	public King(String color){
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

	private void checkPosition(KrakenState state, ArrayList<KrakenMove> list,
			XYLocation location) {
		XYLocation kingLocation = state.getKingLocation(color.equals(KrakenColor.WHITE_ITEM)?  "b":"w" );
		int x= location.getXCoOrdinate();
		int y= location.getYCoOrdinate();
		addPosition(state,list,location,kingLocation,new XYLocation(x-1,y-1));
		addPosition(state,list,location,kingLocation,new XYLocation(x-1,y));
		addPosition(state,list,location,kingLocation,new XYLocation(x-1,y+1));
		addPosition(state,list,location,kingLocation,new XYLocation(x,y+1));
		addPosition(state,list,location,kingLocation,new XYLocation(x+1,y+1));
		addPosition(state,list,location,kingLocation,new XYLocation(x+1,y));
		addPosition(state,list,location,kingLocation,new XYLocation(x+1,y-1));
		addPosition(state,list,location,kingLocation,new XYLocation(x,y-1));
		
	}

	private void addPosition(KrakenState state, ArrayList<KrakenMove> list,
			XYLocation location, XYLocation kingLocation, XYLocation finalLocation) {
		
		if (state.checkRange(finalLocation)){
			if (state.distance(kingLocation, location, finalLocation)){
				if (state.isEmpty(finalLocation))
					list.add(new KrakenMove(location, finalLocation));
				else
					if (!state.getValue(finalLocation).getColor().equals(color))
						list.add(new KrakenMove(location, finalLocation));
			}else
				if (state.getValue(finalLocation) != null && !state.getValue(finalLocation).getColor().equals(color))
					list.add(new KrakenMove(location, finalLocation));
		}
	}

}
