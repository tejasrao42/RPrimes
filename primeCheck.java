package RPrimes;
/*
 * primeCheck checks if a number is divisible by any of primenumbers in array
 * returns the first prime number it finds or 0 if not found
 * @author 		Tejas Rao
 * @version 	0.1
 * @since 		2018-11-25
 */
import java.math.BigInteger;

public class primeCheck implements Runnable{
	Integer p[];
	BigInteger value;
	Integer ii;
	private static boolean stopit = false;
	public static Integer foundPrime = 0;
	boolean done = false;
    public primeCheck(Integer[] PrimeSet, BigInteger Value) {
    	this.p = PrimeSet;
    	this.value = Value;
    }
    @Override
    public void run() {
    	foundPrime = -1;
    	done = false;
    	stopit = false;
	     for ( ii = 0; ii < p.length && !stopit; ii++) {
		  BigInteger tmp = BigInteger.valueOf(p[ii]);
		  BigInteger a = value.mod(tmp);
		  if (a == BigInteger.valueOf(0)) {
			stopit = true;
			foundPrime = p[ii];
		  }
	     }
	     done = true;
    }
    public Integer loopCount() {
    	return ii;
    }
    public boolean isdone( ) {
    	return done;
    }
    public Integer finalValue() {
    	return foundPrime;
    }
}
