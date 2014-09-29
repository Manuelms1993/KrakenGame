package kraken;

import java.util.ArrayList;

public abstract class KrakenPlayer extends Thread{
	
	protected String color;
	protected KrakenState state;
	protected KrakenGame game;
	protected double thinkingTime;
	protected ArrayList<KrakenMove> moveList;
	
	public KrakenPlayer(KrakenState state,KrakenGame game,String color){
		this.color=color;
		this.state=state;
		this.game=game;
		thinkingTime=0;
		moveList = new ArrayList<KrakenMove>();
	}
	
	public String getColor(){
		return color;
	}
	
	public double getThinkingTime() {
		return thinkingTime/1000;
	}
	
	public ArrayList<KrakenMove> getMoveList(){
		return moveList;
	}
	
}
