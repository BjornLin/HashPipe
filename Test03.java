public class Test03 {
    public static void main(String[] args){
        HashPipe newHashPipe = new HashPipe();

        newHashPipe.put("S", 0);
        newHashPipe.put("E", 1);
        newHashPipe.put("A", 2);
        newHashPipe.put("R", 3);
        newHashPipe.put("C", 4);
        newHashPipe.put("H", 5);
        newHashPipe.put("E", 6);
        newHashPipe.put("X", 7);
        newHashPipe.put("A", 8);
        newHashPipe.put("M", 9);
        newHashPipe.put("P", 10);
        newHashPipe.put("L", 11);
        newHashPipe.put("E", 12);

        for(int i = 0; i <= newHashPipe.getMaxHeight(); i++) {//**************************************************
            String curKey = newHashPipe.getRootNode().getLevels()[i].getKey();
            System.out.print(curKey);
            while(newHashPipe.control(curKey, i) != null) {
                System.out.print(" -> ");
                curKey = newHashPipe.control(curKey, i);
                System.out.print(curKey);
            }
            System.out.println();
        }
    }
}
}
