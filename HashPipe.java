public class HashPipe {

    int size = 0;
    Pipe rootPipe;

    public HashPipe(){ // create an empty symbol table
        rootPipe = new Pipe(32, null, null, null, null);
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

    public void put(String str, Integer val) // put key-value pair into the table
    {
        int height = Integer.numberOfTrailingZeros(str.hashCode() + 1);
        Pipe nextPipe;
        Pipe startPipe = rootPipe;
        Pipe pipeToAdd = new Pipe(height, str, val, null, null);
        char keyAsChar = str.charAt(0);

        if (size == 0) { // if it is the first pipe we add
            rootPipe.right = pipeToAdd; // reference from the from to the new pipe
            pipeToAdd.left = rootPipe; // reference from new pipe to the root
            updateReferences(pipeToAdd); // TODO: Figure out a way to update POINTERS....!!
        }

        for (int i = startPipe.height - 1; i >= 0; i--){
            nextPipe = startPipe.thePipe[i];

            if (nextPipe == null && i == 0){ // if we're at level '0' and there's no pipe to the right...
                // add pipeToAdd here... (after the 'startPipe')
                startPipe.right = pipeToAdd; // reference FROM the ROOT pipe to the new pipe
                pipeToAdd.left = startPipe; // reference FROM the NEW PIPE to the root pipe.
                pipeToAdd.right = null; // // reference FROM the NEW PIPE to null.
                updateReferences(pipeToAdd);
            }
            if (nextPipe == null){continue;} // if level contains NO references.. move down a level and look again..

            // If the reference FOUND refers to a pipe w. a HIGHER value (ie. not height) than pipeToAdd:
            else if (nextPipe.str.charAt(0) > keyAsChar){
                if (nextPipe.left.str.charAt(0) < keyAsChar){ //if FOUND.left is SMALLER than pipeToAdd
                    //Insert pipeToInsert btw FOUND and FOUND.left. update reference....
                    nextPipe.left.right = pipeToAdd; // reference from the left neighbour to the new Pipe
                    pipeToAdd.right = nextPipe; // reference FROM the new Pipe to the right neighbour.
                    pipeToAdd.left = nextPipe.left; // reference FROM the new Pipe to the left neighbour (ie. floor)
                    nextPipe.left = pipeToAdd; // reference FROM the right neighbour to the new Pipe.
                    updateReferences(pipeToAdd);// TODO: update references....!
                    // TODO: update pointers
                }
            }
            // else, if the reference FOUND refers to a pipe w. a SMALLER value (ie. not height), than pipeToAdd;
            else if (nextPipe.str.charAt(0) < keyAsChar) {
                if (nextPipe.right.str.charAt(0) > keyAsChar){ //if FOUND.right is HIGHER than pipeToAdd.
                    // insert pipeToInsert btw FOUND and FOUND.right.
                    pipeToAdd.right = nextPipe.right; // reference FROM the new pipe to it's right neighbour.
                    pipeToAdd.left = nextPipe; // reference FROM the new pipe to it's left neighbour.
                    nextPipe.right.left = pipeToAdd; // reference FROM the right neighbour to the new pipe.
                    nextPipe.right = pipeToAdd; // reference from the left neighbour to the new pipe.
                    updateReferences(pipeToAdd);//
                    // TODO: update pointers......!
                }
                //else 'Move' to the FOUND pipe, and start over from line, this time going down FOUND, not the rootPipe.
                else if (nextPipe.right.str.charAt(0) < keyAsChar){
                    startPipe = nextPipe;
                }
            }
            // the reference FOUND refers to a pipe w. the SAME key.. Update the value only..
            else if (nextPipe.str.charAt(0) == keyAsChar){nextPipe.value = pipeToAdd.value;}
        }
        size++;
    }

    public void updateReferences(Pipe pipeToAdd) {
        for (int i = pipeToAdd.height - 1; i >= 0; i--) {
            // Update references with floorPipe
            if (pipeToAdd.height >= pipeToAdd.left.height) { // if pipeToAdd is at least AS tall as its floor
                //for (int j = pipeToAdd.left.height - 1; j >= 0; j--) {
                    pipeToAdd.thePipe[i].lPointer = pipeToAdd.left; // Update pointers in the new pipe

                    Pipe leftPipe = pipeToAdd.left;// used to compare heights of pipes to the left..
                    while (leftPipe.left != null){
                        if (pipeToAdd.height <= leftPipe.height){ //if new pipe is lower than its left neighbours
                            leftPipe.thePipe[i].rPointer = pipeToAdd; // update reference in the neighbour pipe at height i;
                        }
                        leftPipe = leftPipe.left;               // find the pipe to the left of the new at least as high as the new pipe
                    }
                    // else we hit the root pipe w7o finding another pipe high enough, so we add a pointer to the new pipe at height 'i'
                    if (leftPipe.left == null){leftPipe.thePipe[i].rPointer = pipeToAdd;}
            }
        }
    }

    // returns the value associated with key, uses the floorPipe() method..
    public Integer get(String key) {
        Pipe pipe =  floorPipe(key);
        return null;
    }

    public String floor(String key) // returns largest key less than or equal to key
    {
        return floorPipe(key).str;
    }


    private Pipe floorPipe(String key) // returns the pipe of the floor of the given key.
    {
        char keyAsChar = key.charAt(0);
        //Pipe nextPipe = new Pipe();
        Pipe startPipe = rootPipe;
        for (int i = rootPipe.height - 1; i >= 0; i--)
        {

            Pipe nextPipe = startPipe.thePipe[i];
            if (size == 0){ return rootPipe;} // we haven't added any pipes yet...

            else if (nextPipe == null){return startPipe;}
            else if (startPipe.right == null){return startPipe;}
            // If the reference FOUND refers to a pipe w. a HIGHER key (ie. not height) than pipeToInsert:
            else if (nextPipe.str.charAt(0) > keyAsChar){
                if (nextPipe.left.str.charAt(0) < keyAsChar){return nextPipe.left;}//if FOUND.left key is SMALLER than pipeToInsert
            }
            // else, if the reference FOUND refers to a pipe w. a SMALLER key (ie. not height);
            else if (nextPipe.str.charAt(0) < keyAsChar) {
                if (nextPipe.right.str.charAt(0) > keyAsChar){return nextPipe;} //if FOUND.right key is HIGHER than pipeToInsert.

                //else 'Move' to the FOUND pipe, and start over from line, this time going down FOUND, not the rootPipe.
                else if (nextPipe.right.str.charAt(0) < keyAsChar){
                    startPipe = nextPipe;
                    int j = startPipe.height;
                    /*while (startPipe.thePipe[j] == null){
                        j--;
                    }*/
                    if(startPipe.right == null){return startPipe;} // ie. there's nothing to the right of this pipe.

                }
            }
            // the reference FOUND refers to a pipe w. the SAME key..
            else if (nextPipe.str.charAt(0) == keyAsChar){return nextPipe;}

            startPipe = nextPipe;
        }
        return null;
    }

    public class Pipe{
        Pipe[] thePipe;
        int height;
        Integer value;
        String str;
        //char keyAsChar;
        Pipe left; //keeps track of each pipe's immediate neighbour to the left.
        Pipe right; //keeps track of each pipe's immediate neighbour to the left.
        Pipe lPointer; //the pointer at eachlevel to the LEFT
        Pipe rPointer; //the pointer at eachlevel to the RIGHT

        Pipe(int height, String str, Integer value, Pipe left, Pipe right){
            this.height = height;
            this.str = str;
            this.value = value;
            this.left = left;
            this.right = right;
            thePipe = new Pipe[height];
        }
    }
}
//dfgdg