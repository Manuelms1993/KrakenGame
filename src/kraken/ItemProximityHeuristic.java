package kraken;

public class ItemProximityHeuristic implements HeuristicFunction{

	@Override
	public double h(Object state) {
		return ((KrakenState)state).itemProximity();
	}

}
