import hashing.RandomHash;

import java.util.Arrays;

public class BloomFilter {
    private int numBit = 1;
    private int numElems;
    private int numHashes = 5;
    private int primeNumber = 83;
    // used for hashing, must be larger than the largest possible key

    // public BitSet filter = new BitSet(NUM_BITS*NUM_ELEMS*NUM_HASHES);
    private int[] intFilter;
    // private List<Integer> intList = new ArrayList<Integer>();
    private int[] keys;

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
            randomHashes[i] = new RandomHash(numBit, numElems, numHashes, primeNumber);
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

    @Override
    public String toString() {
        String str = "";
        return "keys: " + Arrays.toString(keys) + "\nfilter: " + Arrays.toString(intFilter);
    }

    public void hashString(){
        for (int i = 0; i < randomHashes.length; i++){
            System.out.println(randomHashes[i]);
        }
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

        System.out.println(bloomFilter);
        bloomFilter.hashString();
    }

    private static int[] randomIntArray(int size, int range){
        int[] ints = new int[size];
        for(int i = 0; i < ints.length; i++){
            ints[i] = (int) (Math.random() * range);
        }
        return ints;
    }
}
