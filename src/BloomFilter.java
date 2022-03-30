import hashing.RandomHash;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BloomFilter {
    public static final int NUM_BITS = 1;
    public static final int NUM_ELEMS = 10;
    public static final int NUM_HASHES = 5;

    // public BitSet filter = new BitSet(NUM_BITS*NUM_ELEMS*NUM_HASHES);
    private int[] intFilter = new int[NUM_BITS*NUM_ELEMS*NUM_HASHES];
    // private List<Integer> intList = new ArrayList<Integer>();
    private int[] keys = new int[NUM_ELEMS];

    public RandomHash[] randomHashes = new RandomHash[NUM_HASHES];

    public BloomFilter(int[] keys){
        initializeHashes();
        this.keys = keys;
        addKeys();
    }

    private void initializeHashes(){
        for(int i = 0; i < randomHashes.length; i++){
            randomHashes[i] = new RandomHash(NUM_BITS, NUM_ELEMS, NUM_HASHES);
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
        for (int i = 0; i < NUM_HASHES; i++){
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
        int[] keys = new int[]{1, 2, 3, 4, 5, 6, 6, 7, 8, 9, 10};

        BloomFilter bloomFilter = new BloomFilter(keys);

        int[] queries = new int[]{1, 12, 13, 14, 7, 16, 17, 18, 19, 35};
        for (int i = 0; i < queries.length; i++){
            if(bloomFilter.query(queries[i]) && !bloomFilter.authQuery(queries[i])){
                System.out.println("false positive: " + queries[i]);
            } else if (bloomFilter.query(queries[i])){
                System.out.println("key in array: " +  queries[i]);
            } else {
                System.out.println("key not in array: " + queries[i]);
            }
        }
    }
}
