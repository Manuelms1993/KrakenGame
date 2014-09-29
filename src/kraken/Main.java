package kraken;

public class Main {

	public static void main(String[] args) {
		KrakenGame game = new KrakenGame();
		KrakenState state = game.getInitialState();
		game.createPlayer(state, 0);
		game.startGame();
	}
}
