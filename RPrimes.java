package RPrimes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
/*
 * @author		Tejas Rao
 * @version		0.1
 * @since		2018-11-25
 */
public class RPrimes {

	private Integer[] PrimeSet1 = null;
	private Integer[] PrimeSet2 = null;
	static BigInteger ONE = new BigInteger("1");
	static BigInteger TWO = new BigInteger("2");
	static BigInteger THREE = new BigInteger("3");
	static BlockingQueue<BigInteger> queue = new ArrayBlockingQueue<BigInteger>(100000);
	public RPrimes() {
		//Initialize reading prime list
		Integer[] primelist = readPrimeList();
		// read file and set PrimeSet1 and PrimeSet2
		PrimeSet1 = Arrays.copyOfRange(primelist,0,(int) Math.floor(primelist.length/2));
		PrimeSet2 = Arrays.copyOfRange(primelist,(int) Math.ceil(primelist.length/2),primelist.length);
	}
	public boolean isPrimover(BigInteger origPrime, int n) {
		BigInteger newPrime  = origPrime.multiply(TWO.pow(n));
	    newPrime  = newPrime.add(ONE);

	    Integer e = trialDiv(newPrime);
		if (e > 0) {
			if (e <= PrimeSet2[PrimeSet2.length-1] && BigInteger.valueOf(e).compareTo(newPrime) == 0) {
				System.out.println(" Is Prime! # of Digits = " + e.toString().length());
				return true;
			}
			System.out.println(" Divisible by " + e);
			return false; 
		}
    	BigInteger lim  = newPrime.add(BigInteger.valueOf(-1)).divide(BigInteger.valueOf(2));
    	Integer [] pow2 = findPow2(lim);
    	BigInteger primoverTest = testIfPrimover(pow2,newPrime,n);
        
   
        if ( primoverTest.add(BigInteger.valueOf(1)).equals(newPrime)) {
        	System.out.println(" Is Primover! # of Digits = " + newPrime.toString().length());
        	return true;
        } else {
        	System.out.println(" Not Primover");
        	return false;
        }
    }
	
	public boolean isGF3Divisor(BigInteger origPrime, int n) { 
		Integer[] fermatPow = {n-1};
		if ( n < 1 ) {
			System.out.println("This test is not applicable for n == 0");
			return false;
		}
		BigInteger modFermat = testIfPrimover(fermatPow, origPrime.multiply(TWO.pow(n)).add(ONE),n);
		return modFermat.add(BigInteger.valueOf(-1)).equals(BigInteger.valueOf(0));
	}
	
	//First Primover Index where 2^n<p
	public int findFirstRPrimover(BigInteger origPrime) {
		String b = origPrime.toString(2);
		int highestN = 0; 

		for ( int i=0; i < b.length(); i++) {
			if ( b.charAt(i) == '1') {
				highestN = (b.length()-(i+1));
				break;
			}
		}
	 
		int startN = 2;
		BigInteger newPrime  = origPrime.multiply(TWO.pow(2));
		newPrime  = newPrime.add(ONE);
		
		if (newPrime.mod(THREE).equals(BigInteger.valueOf(0))) {
			startN = 3;
		}
		ArrayList<Integer> result = findAllPrimoverWithin(origPrime, startN, highestN, true);
		return result.get(0);
	}
		
	public ArrayList<Integer> findAllRPrimoverAfter(BigInteger origPrime, int startN) {
		String b = origPrime.toString(2);
		int highestN = 0; 

		for ( int i=0; i < b.length(); i++) {
			if ( b.charAt(i) == '1') {
				highestN = (b.length()-(i+1));
				break;
			}
		}
		ArrayList<Integer> result = findAllPrimoverWithin(origPrime, startN, highestN, false);
		return result;
	}
	
	public ArrayList<Integer> findAllPrimoverWithin(BigInteger origPrime, int startN, int endN, boolean stopAtFirst) {
		ArrayList<Integer> primoverN = new ArrayList<Integer>();;

		BigInteger newPrime  = origPrime.multiply(TWO.pow(startN));
		newPrime  = newPrime.add(ONE);
			  
		if (newPrime.mod(THREE).equals(BigInteger.valueOf(0))) {
			startN += 1;
		}
		for ( int n = startN; n <= endN; n+=2) {
			System.out.print("n = " + n);
			if (isPrimover(origPrime, n)) { 
				primoverN.add(n);
				if (stopAtFirst) {
					return primoverN;
				}
			}
		    	
		}
		return primoverN;
	}
		

	private static BigInteger testIfPrimover(Integer[] pow2, BigInteger modulus, int n) {
		Integer[] pow2Flipped = new Integer[pow2.length];
		int c = 0;
	
		BigInteger mm = new BigInteger("1");
		BigInteger a = new BigInteger("3");

		for ( int i = pow2.length - 1; i >= 0; i-- ) {
			pow2Flipped[c++] = pow2[i];
		}
		
		Thread aMod = new Thread(new aModulator(queue,modulus,pow2Flipped));
		aMod.start();
		for ( int i = 0; i < pow2Flipped.length; i++ ) {
			try {
				a = queue.take();
				mm = mm.multiply(a).mod(modulus);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return mm;
	}
	private static Integer [] findPow2(BigInteger bi) {

		ArrayList<Integer> list = new ArrayList<Integer>();
		String b = bi.toString(2);

		for ( int i=0; i < b.length(); i++) {
			if ( b.charAt(i) == '1') {
				list.add(b.length()-(i+1));
			}
		}
		return list.toArray(new Integer[list.size()]);
	}
	private Integer[] readPrimeList() {
		ArrayList<Integer> primelist = new ArrayList<Integer>();
		System.out.println("Reading Prime List");
		File f = new File("primes.txt");
		FileReader input;
		try {
			input = new FileReader(f);

			BufferedReader in  = new BufferedReader(input);
			String s = in.readLine();
			while (s != null) {
				// Skip empty lines.
				s = s.trim();
				if (s.length() == 0) {
					continue;
				}

				primelist.add(Integer.parseInt(s)); 
				s = in.readLine();
			}

			in.close();
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return primelist.toArray(new Integer[primelist.size()]);
	
	}
	public int trialDiv(BigInteger value) {
	
		primeCheck p1 = new primeCheck(PrimeSet1,value);
		primeCheck p2 = new primeCheck(PrimeSet2,value);
		Thread t1 = new Thread(p1);
		Thread t2 = new Thread(p2);
		t2.start();
		t1.start();
	
		while ( !p1.isdone() && !p2.isdone()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	
		return p1.finalValue();
	
	}
}
 



