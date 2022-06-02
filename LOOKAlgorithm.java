import java.util.*;

public class LOOKAlgorithm extends ScheduleAlgorithmBase {
	public LOOKAlgorithm (int initPosition, int maxCylinders, int direction, ArrayList<Integer> q){
		super(initPosition, maxCylinders, direction, q);
	}
	
	public String getName(){
		return "LOOK";
	}

	public void calcSequence(){
		ArrayList<Integer> right = new ArrayList<>();
		ArrayList<Integer> left = new ArrayList<>();

		//adding items in queue to array lists based on position
		for(Integer next : referenceQueue){
			if(next > position)
				right.add(next);
			else
				left.add(next);

			Collections.sort(right);
			Collections.sort(left);
		}
		//if starting to the right
		if(direction == 0){
			for(int i = 0; i < right.size(); i++)
				seekToSector(right.get(i));
			for(int j = left.size()-1; j >= 0; j--)
				seekToSector(left.get(j));
		}
		//if starting to the left
		else{
			for(int i = left.size()-1; i >= 0; i--)
				seekToSector(left.get(i));
			for(int j = 0; j < right.size(); j++)
				seekToSector(right.get(j));
		}
	}
}
