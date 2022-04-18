import hashing.RandomHash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Scanner;

public class BloomFilter {
    private int numBits = 8;
    private int numElems;
    private int numHashes = 2;
    private double fpRate = 0.01;
    private int primeNumber = 7919/*83*/;
    // used for hashing, must be larger than the largest possible key

    public BitSet filter;
//    private int[] intFilter;
//    private List<Integer> intList = new ArrayList<Integer>();
    private int[] keys;

    public RandomHash[] randomHashes;

    public BloomFilter(int[] keys){
        this.keys = keys;
        this.numElems = keys.length;
        this.filter = new BitSet(numBits *numElems*numHashes);
//        this.primeNumber = nextPrimeNumber();
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
//        this.primeNumber = nextPrimeNumber();
        initializeHashes();
        addKeys();
    }

    public BloomFilter(int[] keys, int numBits) {
        this(keys, numBits, 0.05);
    }

//    private int nextPrimeNumber() {
//        primeNumber = numElems*numBits;
//        for (int i = 2; i < primeNumber; i++) {
//            if(primeNumber%i == 0) {
//                primeNumber++;
//                i=2;
//            } else {
//                continue;
//            }
//        }
//        System.out.println(primeNumber);
//        return primeNumber;
//    }

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
        return "keys: " + Arrays.toString(keys) + "\nfilter:\n" + filterString() + "\nhashes:\n" + hashString();
    }
    
    public String filterString(){
        String s = "";
        for (int i = 0; i < filter.length(); i++){
            s += String.format("%4d", i);
        }
        s += "\n| ";
        for (int i = 0; i < filter.length(); i++){
            if(filter.get(i)){
                s += "1";
            } else {
                s += "0";
            }

            if(i != filter.length() - 1) {
                s += " | ";
            }
        }
        s += "|";
        return s;
    }

    public String hashString(){
        String s = "";
        for (int i = 0; i < randomHashes.length; i++){
            s += randomHashes[i] + "\n";
        }
        return s;
    }

    public static void main(String[] args){
        ArrayList<Integer> allKeys = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            allKeys.add(i);
        }
//        int[] ints1 = randomIntArray(10, 2000);
        int[] ints1 = new int[10];
        int[] queries1 = new int[10];
        for (int i = 0; i < ints1.length; i++) {
            ints1[i] = allKeys.remove((int)(Math.random() * allKeys.size()));
            queries1[i] = allKeys.remove((int)(Math.random() * allKeys.size()));
        }
        BloomFilter bloomFilter = new BloomFilter(ints1, 6, 0.01);

//        int numQueries = 5000;
//        int[] queries1 = randomIntArray(numQueries, 2000);
        int falsePositives = 0;
        for (int i = 0; i < queries1.length; i++){
            int currElem = queries1[i];

            if(bloomFilter.query(currElem) && !bloomFilter.authQuery(currElem)){
                System.out.println("false positive: " + currElem);
                falsePositives++;
            } else if (bloomFilter.query(currElem)){
                System.out.println("key in array: " +  currElem);
            } else {
                System.out.println("key not in array: " + currElem);
            }
        }
        System.out.println("Tested false positive rate: " + falsePositives/(double)queries1.length + "(" + falsePositives + "/" + queries1.length + ")");

        System.out.println(bloomFilter);
        bloomFilter.hashString();

        Scanner input = new Scanner(System.in);
        System.out.println("\nHi! To create a bloom filter, first enter how many elements will be in the bloom filter:");
        boolean loopCond = true;
        int[] userInts = new int[0];
        while (loopCond) {
            if (input.hasNextInt()) {
                userInts = new int[input.nextInt()];
                loopCond = false;
            } else {
                input.nextLine();
                System.out.println("Enter a valid Integer value.");
            }
        }
        System.out.println("Great! Now enter the elements you want inserted in the bloom filter one at a time.");
        for (int i = 0; i < userInts.length; i++) {
            loopCond = true;
            while (loopCond) {
                if (input.hasNextInt()) {
                    userInts[i] = input.nextInt();
                    loopCond = false;
                } else {
                    input.nextLine();
                    System.out.println("Enter a valid Integer value.");
                }
            }
        }
        System.out.println("Next, you have the option to specify a desired number of bits per element. If you'd like to use the default value, just enter -1");
        int userBits = 0;
        loopCond = true;
        while (loopCond) {
            if (input.hasNextInt()) {
                userBits = input.nextInt();
                loopCond = false;
            } else {
                input.nextLine();
                System.out.println("Enter a valid Integer value.");
            }
        }
        double userFPRate = -1;
        if (userBits != -1) {
            System.out.println("Finally, you have the option to specify a desired false positive rate. If you'd like to use the default value, just enter -1");
            loopCond = true;
            while (loopCond) {
                if (input.hasNextDouble()) {
                    userFPRate = input.nextDouble();
                    loopCond = false;
                } else {
                    input.nextLine();
                    System.out.println("Enter a valid Double value.");
                }
            }
        }
        BloomFilter userFilter;
        if (userFPRate == -1 && userBits == -1) {
            userFilter = new BloomFilter(userInts);
        } else if (userFPRate == -1) {
            userFilter = new BloomFilter(userInts, userBits);
        } else {
            userFilter = new BloomFilter(userInts, userBits, userFPRate);
        }
        System.out.println("Great! Your bloom filter is all set up now.\nHere are its details:\n" + userFilter + "You can now enter any value to query it.");
        int query;
        while (true) {
            if (input.hasNextInt()) {
                query = input.nextInt();
                if(userFilter.query(query) && !userFilter.authQuery(query)){
                    System.out.println("false positive: " + query);
//                    falsePositives++;
                } else if (userFilter.query(query)){
                    System.out.println("key in array: " +  query);
                } else {
                    System.out.println("key not in array: " + query);
                }
            } else {
                input.nextLine();
                System.out.println("Enter a valid Integer value.");
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
