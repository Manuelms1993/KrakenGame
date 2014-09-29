package kraken;


public class CentralBoardControlHeuristic implements HeuristicFunction{

	@Override
	public double h(Object state) {
		return ((KrakenState)state).centerControl();
	}

}
