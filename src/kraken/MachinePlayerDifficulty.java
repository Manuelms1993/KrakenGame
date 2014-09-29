package kraken;

public class MachinePlayerDifficulty {
	
	private static final HeuristicFunction [] heuristicFunctions ={new ItemPuntuationHeuristic(),
															new CentralBoardControlHeuristic(),
															new ItemProximityHeuristic()
															};
	private HeuristicFunction heuristic;
	private HeuristicFunction[] heuristicFusion=null;
	private int maxNodesExpand;
	
	public MachinePlayerDifficulty(HeuristicFunction heuristic,int maxNodesExpand){
		this.heuristic=heuristic;
		this.maxNodesExpand=maxNodesExpand;
		heuristicFusion=null;
	}
	
	public MachinePlayerDifficulty(HeuristicFunction[] heuristic,int maxNodesExpand){
		this.heuristicFusion=heuristic;
		this.heuristic=heuristicFusion[0];
		this.maxNodesExpand=maxNodesExpand;
	}
	
	public HeuristicFunction getHeuristic() {
		return heuristic;
	}

	public int getMaxNodesExpand() {
		return maxNodesExpand;
	}

	public HeuristicFunction[] getHeuristicFusion() {
		return heuristicFusion;
	}
	
	public static int numberOfHeuristics(){
		return heuristicFunctions.length;
	}

	public static MachinePlayerDifficulty getDifficulty(int numberOfDifficulty){
		switch(numberOfDifficulty){
			case 0: 
					return new MachinePlayerDifficulty(heuristicFunctions[0],90000);
			case 1: 
					return new MachinePlayerDifficulty(heuristicFunctions[1],90000);
			case 2: 
					return new MachinePlayerDifficulty(heuristicFunctions[2],90000);
			default:
					return getFusionDifficulty();
		}
	}
	
	private static MachinePlayerDifficulty getFusionDifficulty(){
		return new MachinePlayerDifficulty(heuristicFunctions,90000);
	}
	
}
