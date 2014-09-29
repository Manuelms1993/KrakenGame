package kraken;


public class ItemPuntuationHeuristic implements HeuristicFunction{

	@Override
	public double h(Object state) {
		return ((KrakenState)state).itemPuntuation();
	}

}
