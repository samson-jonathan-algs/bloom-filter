import hashing.RandomHash;

import java.util.Arrays;
import java.util.BitSet;

public class BloomFilter {
    private int numBits = 8;
    private int numElems;
    private int numHashes = 2;
    private double fpRate = 0.01;
    private int primeNumber = 83;
    // used for hashing, must be larger than the largest possible key

    public BitSet filter;
//    private int[] intFilter;
//    private List<Integer> intList = new ArrayList<Integer>();
    private int[] keys;

    public RandomHash[] randomHashes;

    public BloomFilter(int[] keys){
        this.keys = keys;
        this.numElems = keys.length;
//        this.intFilter = new int[numBit * numElems * numHashes];
        this.filter = new BitSet(numBits *numElems*numHashes);
        initializeHashes();
        addKeys();
    }

    public BloomFilter(int[] keys, int numBits, double fpRate){
        this.keys = keys;
        this.numElems = keys.length;
        this.numBits = numBits;
        this.fpRate = fpRate;
        while (calcFpRate(numHashes, this.numBits) > this.fpRate && calcFpRate(numHashes+1, this.numBits) < calcFpRate(numHashes, this.numBits)) {
            numHashes++;
        }
        this.filter = new BitSet(this.numBits*numElems);
        initializeHashes();
        addKeys();
    }

    public BloomFilter(int[] keys, int numBits) {
        this(keys, numBits, 0.05);
    }

    private static double calcFpRate(int numHashes, int numBits) {
        return Math.pow(1.0 - Math.exp(-numHashes/(double)numBits), numHashes);
    }

    private void initializeHashes(){
        randomHashes = new RandomHash[numHashes];
        for(int i = 0; i < randomHashes.length; i++){
            randomHashes[i] = new RandomHash(numBits, numElems, numHashes, primeNumber);
        }
    }

    public void addKey(int key){
        for(int i = 0; i < randomHashes.length; i++){
//            intFilter[randomHashes[i].hash(key)] = 1;
            filter.set(randomHashes[i].hash(key));
        }
        // intList.add(key);
    }

    public void addKeys(){
        for(int i=0; i < keys.length; i++){
            addKey(keys[i]);
        }
    }

    public boolean query(int key){
//        for (int i = 0; i < numHashes; i++){
//            if(intFilter[randomHashes[i].hash(key)] == 0)
//                return false;
//        }
        for (int i = 0; i < numHashes; i++){
            if(!filter.get(randomHashes[i].hash(key)))
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
        return "keys: " + Arrays.toString(keys) + "\nfilter: " + filter;
    }

    public void hashString(){
        for (int i = 0; i < randomHashes.length; i++){
            System.out.println(randomHashes[i]);
        }
    }

    public static void main(String[] args){
        int[] ints1 = randomIntArray(10, 20);
        BloomFilter bloomFilter = new BloomFilter(ints1, 6);

        int[] queries1 = randomIntArray(100, 5);
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
