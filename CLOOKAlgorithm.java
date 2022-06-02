import java.util.*;

public class CLOOKAlgorithm extends ScheduleAlgorithmBase{
	public CLOOKAlgorithm(int initPosition, int maxCylinders, int direction, ArrayList<Integer> q){
		super(initPosition, maxCylinders, direction, q);
	}

	public String getName(){
		return "CLOOK";
	}

	public void calcSequence(){
		ArrayList<Integer> right = new ArrayList<>();
		ArrayList<Integer> left = new ArrayList<>();

		//filling array lists with items from queue based on position
		for(Integer next : referenceQueue){
			if (next > position)
				right.add(next);
			else
				left.add(next);
			Collections.sort(right);
			Collections.sort(left);
		}
		// if starting right
		if(direction == 0){
			for(int i = 0; i < right.size(); i++)
				seekToSector(right.get(i));
			for(int j = 0; j < left.size(); j++)
				seekToSector(left.get(j));
		}
		// if starting left
		else{
			for(int i = left.size()-1; i >= 0; i--)
				seekToSector(left.get(i));
			for(int j = right.size()-1; j >= 0; j--)
				seekToSector(right.get(j));
		
		}
	}
}
