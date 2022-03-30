import hashing.RandomHash;

public class BloomFilter {
    private int numBit = 1;
    private int numElems;
    private int numHashes = 5;

    // public BitSet filter = new BitSet(NUM_BITS*NUM_ELEMS*NUM_HASHES);
    private int[] intFilter;
    // private List<Integer> intList = new ArrayList<Integer>();
    private int[] keys = new int[numElems];

    public RandomHash[] randomHashes = new RandomHash[numHashes];

    public BloomFilter(int[] keys){
        this.keys = keys;
        this.numElems = keys.length;
        this.intFilter = new int[numBit * numElems * numHashes];
        initializeHashes();
        addKeys();
    }

    private void initializeHashes(){
        for(int i = 0; i < randomHashes.length; i++){
            randomHashes[i] = new RandomHash(numBit, numElems, numHashes);
        }
    }

    public void addKey(int key){
        for(int i = 0; i < randomHashes.length; i++){
            intFilter[randomHashes[i].hash(key)] = 1;
        }
        // intList.add(key);
    }

    public void addKeys(){
        for(int i=0; i < keys.length; i++){
            addKey(keys[i]);
        }
    }

    public boolean query(int key){
        for (int i = 0; i < numHashes; i++){
            if(intFilter[randomHashes[i].hash(key)] == 0)
                return false;
        }
        return true;
    }

    public boolean authQuery(int key){
        for (int i = 0; i < keys.length; i++){
            if(keys[i] == key)
                return true;
        }
        return false;
    }

    public static void main(String[] args){
        int[] ints1 = randomIntArray(10, 36);
        BloomFilter bloomFilter = new BloomFilter(ints1);

        int[] queries1 = randomIntArray(10, 36);
        for (int i = 0; i < queries1.length; i++){
            int currElem = queries1[i];

            if(bloomFilter.query(currElem) && !bloomFilter.authQuery(currElem)){
                System.out.println("false positive: " + currElem);
            } else if (bloomFilter.query(currElem)){
                System.out.println("key in array: " +  currElem);
            } else {
                System.out.println("key not in array: " + currElem);
            }
        }
    }

    private static int[] randomIntArray(int size, int range){
        int[] ints = new int[size];
        for(int i = 0; i < ints.length; i++){
            ints[i] = (int) (Math.random() * range);
        }
        return ints;
    }
}
