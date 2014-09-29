package kraken;

public class KrakenMove {

	private XYLocation initialPlace;
	private XYLocation finalPlace;
	
	public KrakenMove(XYLocation initialPlace, XYLocation finalPlace) {
		this.initialPlace = initialPlace;
		this.finalPlace = finalPlace;
	}

	public XYLocation getInitialPlace() {
		return initialPlace;
	}

	public XYLocation getFinalPlace() {
		return finalPlace;
	}

}
