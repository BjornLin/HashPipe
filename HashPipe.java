public class HashPipe {

    int size = 0;
    Pipe rootPipe;
    int maxPipeHeight = 0;

    public HashPipe() { // create an empty symbol table
        rootPipe = new Pipe(32, null, null);
    }

    //Returns the KEY of the pipe that is referenced (pointed) to at the height given by 'height', (counting from below and starting with 0) or null
    public String control(String str, int height) {
        Pipe floorPipe = floorPipe(str);
        if (height < floorPipe.height && floorPipe.thePipe[height] != null)
            return floorPipe.thePipe[height].str;
        return null;
        //Pipe pipe = floorPipe(str);
        //String stringToReturn = null;
        //if(pipe.thePipe[height] == null){return stringToReturn;} // if the pointer is null, return a null String object*******************

        //stringToReturn = pipe.thePipe[height].str;
        // else {stringToReturn = pipe.thePipe[height].str;} //**********************************Use this instead?

//        //if (str != floorPipe(str).str){return null;} // ie. if the key is NOT in the HashPipe...***************************************************
//
//        if (pipe.thePipe[height] == null || pipe == null || pipe.height < height) {return null;}
//        // returns the key (.str) of the pipe that is referenced ('pointed to') at height 'height'(.thePipe[height]) of 'pipe'
        //return pipe.thePipe[height].rPointer.str;

        //if(stringToReturn != str){return null;}    // IE. if the key IS NOT IN THE HASHPIPE
        //if (pipe.thePipe[height] == null || pipe == null || pipe.height < height){return null;}
        //return pipe.thePipe[height].str; // return the key of the pipe pointed to at this level
    }

    public int size() {
        return size;
    } // return the number of elements

    public void put2(String str, Integer val) {// put key-value pair into the table
        int height = Integer.numberOfTrailingZeros(str.hashCode()) + 1;
        Pipe pipeToAdd = new Pipe(height, str, val);
        Pipe currentPipe = rootPipe;
        int currentLevel = currentPipe.height - 1;

        //****************************************************** is this necessary?
        if (size == 0) { // if its the first pipe we add
            //  we add a pointer to the new pipe for each level of the rootpipe (from the height of the new pipe (-1) down to '0'...
            for (int j = pipeToAdd.height - 1; j >= 0; j--) {
                rootPipe.thePipe[j] = pipeToAdd; // ...add a pointer to the new pipe
            }
            size++;
            //if (maxPipeHeight < pipeToAdd.height){maxPipeHeight = pipeToAdd.height;}
            return;
        }

        for (int i = currentLevel; i >= 0; i--) {
            // if the pointer found points to 'null' AND were at level '0' - ie. we're at the rightmost pipe at level 0
            if (i == 0 && currentPipe.thePipe[i] == null) {
                currentPipe.thePipe[i] = pipeToAdd;
            } // 'add' the pipe here, by adding a reference ...

            // if the pointer at this level of currentPipe points to null, OR the pointer points to a pipe w. a HIGHER KEY.
            if (currentPipe.thePipe[i] == null || (pipeToAdd.str.compareTo(currentPipe.thePipe[i].str) < 0)) { //*****************

                continue; // move down the pipe and try again..
            }

            // if the pointer at this level of currentPipe points to a pipe w a LOWER KEY.
            if (pipeToAdd.str.compareTo(currentPipe.thePipe[i].str) > 0) { //****************************

                // In case pipeToAdd is AT LEAST as high as currentLevel:
                if (pipeToAdd.height >= i) {// update the reference at this level () ONLY if pipeToAdd is high enough
                    pipeToAdd.thePipe[i] = currentPipe.thePipe[i];// put pointer from the found pipe & put it in currentPipe
                    currentPipe.thePipe[i] = pipeToAdd; // make the pointer in the found pipe point to pipeToInsert. And...
                }
                currentPipe = currentPipe.thePipe[i]; // THEN move to the found pipe....

                continue;
            }

            // if the pointer at this level of currentPipe points to a pipe w an IDENTICAL KEY
            if (pipeToAdd.str.compareTo(currentPipe.thePipe[i].str) == 0) {
                // update/overwrite the old value with the new...
                currentPipe.thePipe[i].value = pipeToAdd.value;
            }
        }
        size++;
    }

    public void put(String key, Integer val) {
        Pipe floorPipe = floorPipe(key);

        // if the pointer at this level of currentPipe points to a pipe w an IDENTICAL KEY - IE: if the floorPipe() method returned the pipe w. the same key..
        if (floorPipe != null && floorPipe.str != null && floorPipe.str.compareTo(key) == 0) {
            floorPipe.value = val; // simply update/overwrite the old value with the new... and return
            return;
        }

        int height = Integer.numberOfTrailingZeros(key.hashCode()) + 1;
        Pipe pipeToAdd = new Pipe(height, key, val);
        pipeToAdd.previousPipe = floorPipe;
        // if the floorPipe() method did NOT return a pipe w. the same key
        if (floorPipe.thePipe[0] != null) {
            // 'insert' the new pipe by letting the previousPipe of the pipe to the right of the floor refer back to the new pipeToAdd.
            floorPipe.thePipe[0].previousPipe = pipeToAdd;
        }
        references(pipeToAdd); // update remaining references
        size++; // pretty self-explanatory I feel...
    }

    public void references(Pipe pipeToAdd) {
        Pipe leftPipe = pipeToAdd.previousPipe;
        for (int i = 0; i < pipeToAdd.height; i++) {
            while (leftPipe.height <= i){leftPipe = leftPipe.previousPipe;}
            //update the pointer at level 'i' of the new pipeToAdd. (points to what is now left pipe's OLD neighbour)
            pipeToAdd.thePipe[i] = leftPipe.thePipe[i];
            // update the pointer from the left pipe to now point to the new pipeToAdd
            leftPipe.thePipe[i] = pipeToAdd;
        }
    }

    // returns the value associated with the key, uses the floorPipe() method..
    public Integer get(String key) {
        Pipe pipe = floorPipe(key);
        return pipe.value;
    }

    public String floor(String key) {
        return floorPipe(key).str;
    } // returns largest key less than or equal to key


    private Pipe floorPipe2(String key) { // returns the pipe of the floor of the given key.
        //int height = Integer.numberOfTrailingZeros(str.hashCode() + 1);
        //Pipe auxPipe = new Pipe(Integer.numberOfTrailingZeros(str.hashCode() + 1), str, val);

        Pipe currentPipe = rootPipe;
        int currentLevel = currentPipe.height - 1;

        // if the HashPipe is empty, we simply return the rootPipe
        if (size == 0) {
            return rootPipe;
        }

        for (int i = currentLevel; i >= 0; i--) {
            // if the pointer at this level of currentPipe points to null, OR the pointer points to a pipe w. a HIGHER KEY than the key were looking for...
            if (currentPipe.thePipe[i] == null || (key.compareTo(currentPipe.thePipe[i].str) < 0)) { //****************
                continue; // move down the pipe and try again...
            }

            // if the pointer at this level of currentPipe points to a pipe w a LOWER KEY.
            if (key.compareTo(currentPipe.thePipe[i].str) > 0) { //****************************
                currentPipe = currentPipe.thePipe[i]; // move to the found pipe....
                continue;
            }

            // if the pointer at this level of currentPipe points to a pipe w an IDENTICAL KEY
            if (key.compareTo(currentPipe.thePipe[i].str) == 0) {
                // update/overwrite the old value with the new...
                return currentPipe;
            }
        }
        return null;
    }

    private Pipe floorPipe(String key) {
        Pipe floorPipe = rootPipe;
        for (int i = rootPipe.height - 1; i >= 0; i--) {
            if (floorPipe.thePipe[i] == null) {continue;}

            while (floorPipe.thePipe[i] != null && floorPipe.thePipe[i].str.compareTo(key) <= 0){
                // Update this variable to hold a reference to the next pipe, for as long as the key we're looking for is smaller
                // than or equal to the key found. As soon as we hit a larger key, we....
                floorPipe = floorPipe.thePipe[i];
            }
        }
        return floorPipe; // ... return the pipe with the LARGEST KEY SMALLER THAN THE KEY WE'RE LOOKING FOR!
    }

    public class Pipe {
        Pipe[] thePipe;
        int height;
        Integer value;
        String str;
        Pipe previousPipe;

        Pipe(int height, String str, Integer value) {
            this.height = height;
            this.str = str;
            this.value = value;
            thePipe = new Pipe[height];
        }
    }
}
