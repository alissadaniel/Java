import java.util.ArrayList;

public class SSTFAlgorithm extends ScheduleAlgorithmBase {
	public SSTFAlgorithm(int initPosition, int maxCylinders, int direction, ArrayList<Integer> q) {
		super(initPosition, maxCylinders, direction, q);
	}
	public String getName(){
		return "SSTF";
	}
	
	public void calcSequence(){
		
		//creating a copy of referenceQueue
		ArrayList<Integer> queueCopy = new ArrayList<Integer>();
		for(int j = 0; j < referenceQueue.size();j++){
			queueCopy.add(referenceQueue.get(j));
		}

		while(!queueCopy.isEmpty()){
			int seekTime[] = new int[queueCopy.size()];
			int index = 0;

			//filling seek time array
			for(Integer next : queueCopy){
				seekTime[index] = Math.abs(position - next);
				index++;
				
			}
			//determining minimum seek time
			int min = numCylinders;
			int result = 0;
			for(int k = 0; k < queueCopy.size(); k++){
				if (seekTime[k] < min){
					min = seekTime[k];
					result = k;
				}	
			
			}
			//adding I/O request to sequence and removing from queue
			seekToSector(queueCopy.get(result));
			queueCopy.remove(result);
		}
	}
}
