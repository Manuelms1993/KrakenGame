package kraken;

import java.util.List;

public interface KrakenItem {

	public abstract char getSymbol();
	public abstract String getColor();
	public abstract List<KrakenMove> getMoves(KrakenState state,XYLocation location);
	public abstract boolean legalMove(KrakenState state,XYLocation initialLocation, XYLocation finalLocation);
	
}

