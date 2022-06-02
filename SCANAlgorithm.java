import java.util.*;

public class SCANAlgorithm extends ScheduleAlgorithmBase {
	public SCANAlgorithm(int initPosition, int maxCylinders, int direction, ArrayList<Integer> q) {
		super(initPosition, maxCylinders, direction, q);
	}
	
	public String getName() {
		return "SCAN";
	}

	public void calcSequence() {
		ArrayList<Integer> right = new ArrayList<>();
		ArrayList<Integer> left = new ArrayList<>();
		
		//filling array lists with queues based on position
		for(Integer next : referenceQueue){
			if(next > position)
				right.add(next);
			else
				left.add(next);
			Collections.sort(right);
			Collections.sort(left);		
		}	
		//if starting right
		if(direction == 0){
			for(int i = 0; i < right.size();i++)
				seekToSector(right.get(i));
		
			//goes to end of the disk	
			seekToSector(numCylinders-1);

			for(int j = left.size()-1; j >= 0;j--)
				seekToSector(left.get(j));
			
		}
		//if starting left
		else{
			for (int i = left.size()-1; i >= 0;i--)
				seekToSector(left.get(i));
		
			//goes to beginning of the disk	
			seekToSector(0);
			
			for (int j = 0; j < right.size(); j++)
				seekToSector(right.get(j));
		}
	}
}
