public class HashPipe {

    int size = 0;
    Pipe rootPipe;

    public HashPipe(){ // create an empty symbol table
        rootPipe = new Pipe(32, null, null);
    }

    //Returns the KEY of the pipe that is referenced (pointed) to at the height given by 'height', (counting from below and starting with 0) or null
    public String control(String str, int height) {
        Pipe pipe = floorPipe(str);
        if (str != floorPipe(str).str){return null;} // ie. if the key is NOT in the HashPipe...***************************************************
        //TODO:
        if (pipe.thePipe[height] == null || pipe == null || pipe.height < height) {return null;}
        // returns the key (.str) of the pipe that is referenced ('pointed to') at height 'height'(.thePipe[height]) of 'pipe'
        return pipe.thePipe[height].rPointer.str;
    }

    public int size(){return size;} // return the number of elements

    public void put(String str, Integer val) {// put key-value pair into the table
        //int height = Integer.numberOfTrailingZeros(str.hashCode() + 1);
        Pipe pipeToAdd = new Pipe(Integer.numberOfTrailingZeros(str.hashCode() + 1), str, val);
        Pipe currentPipe = rootPipe;
        int currentLevel = currentPipe.height -1;

        for (int i = currentLevel; i <= 0; i--){

            // if the pointer at this level of currentPipe points to null, OR the pointer points to a pipe w. a HIGHER KEY.
            if (currentPipe.thePipe[i].rPointer == null || (pipeToAdd.str.compareTo(currentPipe.thePipe[i].rPointer.str) < 0)){ //*****************

                continue; // move down the pipe and try again..
            }

            // if the pointer at this level of currentPipe points to a pipe w a LOWER KEY.
            if (pipeToAdd.str.compareTo(currentPipe.thePipe[i].rPointer.str) > 0){ //****************************

                // INSERT THE PIPE
                if (pipeToAdd.height >= i) {// update the reference at this level () ONLY if pipeToAdd is high enough
                    pipeToAdd.thePipe[i].rPointer = currentPipe.thePipe[i].rPointer;// put pointer from the found pipe & put it in currentPipe
                    currentPipe.thePipe[i].rPointer = pipeToAdd; // make the pointer in the found pipe point to pipeToInsert. And...
                }
                currentPipe = currentPipe.thePipe[i].rPointer; // THEN move to the found pipe....
                size++;
                continue;
            }

            // if the pointer at this level of currentPipe points to a pipe w an IDENTICAL KEY
            if (pipeToAdd.str.compareTo(currentPipe.thePipe[i].rPointer.str) == 0){
                // update/overwrite the old value with the new...
                currentPipe.thePipe[i].rPointer.value = pipeToAdd.value;
            }
        }
    }

    // returns the value associated with key, uses the floorPipe() method..
    public Integer get(String key) {
        Pipe pipe =  floorPipe(key);
        return null;
    }

    public String floor(String key){ // returns largest key less than or equal to key
        return floorPipe(key).str;
    }


    private Pipe floorPipe(String key){ // returns the pipe of the floor of the given key.
        //int height = Integer.numberOfTrailingZeros(str.hashCode() + 1);
        //Pipe auxPipe = new Pipe(Integer.numberOfTrailingZeros(str.hashCode() + 1), str, val);

        Pipe currentPipe = rootPipe;
        int currentLevel = currentPipe.height -1;

        for (int i = currentLevel; i <= 0; i--){

            // if the pointer at this level of currentPipe points to null, OR the pointer points to a pipe w. a HIGHER KEY.
            if (currentPipe.thePipe[i].rPointer == null || (key.compareTo(currentPipe.thePipe[i].rPointer.str) < 0)){ //*****************
                continue; // move down the pipe and try again..
            }

            // if the pointer at this level of currentPipe points to a pipe w a LOWER KEY.
            if (key.compareTo(currentPipe.thePipe[i].rPointer.str) > 0){ //****************************
                currentPipe = currentPipe.thePipe[i].rPointer; // move to the found pipe....
                continue;
            }

            // if the pointer at this level of currentPipe points to a pipe w an IDENTICAL KEY
            if (key.compareTo(currentPipe.thePipe[i].rPointer.str) == 0){
                // update/overwrite the old value with the new...
                return currentPipe;
            }
        }
        return null;
    }

    public class Pipe{
        Pipe[] thePipe;
        int height;
        Integer value;
        String str;
        Pipe rPointer; //the pointer at each level to the RIGHT

        Pipe(int height, String str, Integer value){
            this.height = height;
            this.str = str;
            this.value = value;

            thePipe = new Pipe[height];
        }
    }
}
