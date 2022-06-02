import java.util.ArrayList;

public class ArrayManager {
        int numFrames;
        int frameSize;
        int[] data;
        ArrayList<Integer> freeFrameList;

	public ArrayManager(int numFrames, int frameSize) {
	        this.numFrames = numFrames;
                this.frameSize = frameSize;
		data = new int[numFrames*frameSize];
         	freeFrameList = new ArrayList<Integer>(numFrames);
               
		 //filling the freeFrameList
		for(int i = 0; i < numFrames; i++){
                    freeFrameList.add(i);
                }
	}

	public Array createArray(int size) throws OutOfMemoryException{
                
	        String getMessage = "Cannot create array of size " + size + " with " + freeFrameList.size() + " frames of " +  frameSize + " available";
                int[] pageTable;
                PagedArray pagedArray;
           
                //if array size is greater than available memory, throw OutOfMemoryException
                if(size > (freeFrameList.size() * frameSize)){
                    throw new OutOfMemoryException(getMessage);
                }
                else{ //else assign free frames to array and create corresponding pageTable and pagedArray
                    // checking if the number of frames needs to be rounded up
		    if(size % frameSize !=0){
			 pageTable = new int[(int)((double)size/(double)frameSize) +1];   
			 for(int i = 0; i <= (int)(size/frameSize); i++){
        	                pageTable[i] = freeFrameList.get(0);
				freeFrameList.remove(0);
                   	     }
                    }
                    else{
			    pageTable = new int[size/frameSize];
         		    for(int i = 0; i < size/frameSize; i++){     
		            	pageTable[i] = freeFrameList.get(0);
				freeFrameList.remove(0);
                            }
                    }
                    pagedArray = new PagedArray(pageTable, size);
                }         	
		return pagedArray;
	}

	public Array aliasArray(Array a) {
		//accessing Array a's page table
		PagedArray pagedA = (PagedArray) a;
		int[] arrPageTable= pagedA.getPageTable();
		//creating alias array of Array a and returning it
		PagedArray aliasArray = new PagedArray(arrPageTable, a.length());     
		return aliasArray;
	}

	public void deleteArray(Array a) {
		//accessing Array a's page table
		PagedArray pagedA = (PagedArray) a;
		int[] arrPageTable = pagedA.getPageTable();
		//for each element in Array a's page table, add it to the freeFrameList
		for(int i = 0; i < arrPageTable.length;i++){
			freeFrameList.add(i, arrPageTable[i]);
		}
		pagedA.setLength(0);
	}

	public void resizeArray(Array a, int newSize) throws OutOfMemoryException {		
		String getMessage = "Cannot create array of size " + newSize + " with " + freeFrameList.size() + " frames of " + frameSize + " available";
		PagedArray pagedA = (PagedArray) a;
		int[] arrPageTable = pagedA.getPageTable();
		int tableDifference = 1;
		
		//checking if number of frames needs to be rounded up
		int[] largePageTable;
		if(newSize % frameSize != 0){
			largePageTable = new int[(int)((double)newSize/(double)frameSize) + 1];
		}
		else{
			largePageTable = new int[newSize/frameSize];
		}	

		//if the new size is smaller than the original...
		if(newSize < a.length()){
		
			//returning frames to freeFrameList
			for(int i = arrPageTable.length-1; i > largePageTable.length; i--){		
				freeFrameList.add(i, arrPageTable[i]);
				tableDifference++;
			}

			//creating new page table of smaller size
			int[] smallPageTable = new int[arrPageTable.length - tableDifference];
			for(int k = 0; k < smallPageTable.length;k++){
				smallPageTable[k] = arrPageTable[k];
			}	

			//updating the paged array
			pagedA.setLength(newSize);
			pagedA.setPageTable(smallPageTable);
		}
		//if the new size is larger than the original...
		else if(newSize > a.length()){
		
			//filling new page table with original page table elements
			for(int k = 0; k < arrPageTable.length;k++){
				largePageTable[k] = arrPageTable[k];
			}

			//adding frames from freeFrameList to new page table and removing from list		
			for (int i = largePageTable.length-1; i > arrPageTable.length-1; i--){
				largePageTable[i] = freeFrameList.get(0);
				freeFrameList.remove(0);
			}
			
			//updating the paged array
			pagedA.setLength(newSize);
			pagedA.setPageTable(largePageTable);
			
		}
		//if there are not enough free frames available...
		else if(newSize - a.length() > freeFrameList.size()*frameSize){
			throw new OutOfMemoryException(getMessage);
		}
	}

	public void printMemory() {
                double percent = 100 * (1.00 - ((double) freeFrameList.size()/ (double) numFrames));
                System.out.printf("Memory [%dx%d] %.2f%% full\n", numFrames, frameSize, percent);
              
                for (int i = 0; i < numFrames; i++){
                    boolean ans = freeFrameList.contains(i);
                    if (ans){
                       System.out.printf("-");
                    }
                    else{
                       System.out.printf("X");
                    } 
	        }
                System.out.printf("\n");

        }

	private class PagedArray implements Array {
		int[] pageTable;
                int length;

		public PagedArray(int pageTable[], int length) {
		        this.pageTable = pageTable;
                        this.length = length;
		}

		public int getValue(int index) throws SegmentationViolationException {
			int expectedRange = length-1;
			String getMessage = "Index " + index + " is out of range.  Expected 0->" + expectedRange;
			int getVal; 
			
			//checking if index is out of range
			if(index > expectedRange || index < 0){
				throw new SegmentationViolationException(getMessage);		
	
			}
			else{
				int getPage = (int)index/frameSize;
				int getOffset = (int)index % frameSize;
				int getFrame = pageTable[getPage];
				getVal =  data[(getFrame*frameSize) + getOffset];
			}
			return getVal;
		}

		public void setValue(int index, int val) throws SegmentationViolationException {
			int expectedRange = length-1;
			String getMessage = "Index " + index + " is out of range.  Expected 0->" + expectedRange;
			
			//checking if index is out of range
			if(index > expectedRange || index < 0){
				throw new SegmentationViolationException(getMessage);
			}
			else{
				int setPage = (int)index/frameSize;
				int setOffset = (int)index % frameSize;
				int setFrame = pageTable[setPage];
				data[(setFrame*frameSize) + setOffset] = val;
			}
		}

		public String toString() {
                       //converting page table to string
		       String pageTableToString = "";
                       for(int i = 0; i < pageTable.length; i++){
				pageTableToString += pageTable[i] + " ";
                       }
                       return "length " + length + " " + "@" + pageTableToString ;
		      
                }

		public int[] getPageTable(){
			return pageTable;
		}
		
		public void setPageTable(int[] pageTable) {
			this.pageTable = pageTable;
		}
		
		public int length() {	
			return length;
		}
		
		public void setLength(int length) {
			this.length = length;
		}
	}
}
