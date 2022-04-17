import hashing.RandomHash;

import java.util.Arrays;
import java.util.BitSet;

public class BloomFilter {
    private int numBit = 1;
    private int numElems;
    private int numHashes = 5;
    private int primeNumber = 83;
    // used for hashing, must be larger than the largest possible key

    public BitSet filter;
//    private int[] intFilter;
//    private List<Integer> intList = new ArrayList<Integer>();
    private int[] keys;

    public RandomHash[] randomHashes = new RandomHash[numHashes];

    public BloomFilter(int[] keys){
        this.keys = keys;
        this.numElems = keys.length;
//        this.intFilter = new int[numBit * numElems * numHashes];
        this.filter = new BitSet(numBit*numElems*numHashes);
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
        BloomFilter bloomFilter = new BloomFilter(ints1);

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

        System.out.println(5);
    }

    private static int[] randomIntArray(int size, int range){
        int[] ints = new int[size];
        for(int i = 0; i < ints.length; i++){
            ints[i] = (int) (Math.random() * range);
        }
        return ints;
    }
}
