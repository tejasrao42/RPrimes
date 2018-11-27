package RPrimes;
/*
 * aModulator calculates the remainder for every power of two passed in pow2Flipped
 * from the modulus(divisor) supplied
 * The remainders are stored in the associated blocking queue for retrieval in RPrimes
 * Implementation of the blocking queue allows for parallelization of the further multiplication
 * It is a small but significant optimization when dealing with very large primes
 * @author		Tejas Rao
 * @version		0.1
 * @since		2018-11-25
 */
import java.util.concurrent.BlockingQueue;
import java.math.BigInteger;
public class aModulator implements Runnable {
	private BlockingQueue<BigInteger> queue;
	private Integer[] pow2Flipped;
	private BigInteger a = new BigInteger("3");

	private BigInteger modulus;

	private boolean stopit = false;
	public aModulator(BlockingQueue<BigInteger> queue,  BigInteger modulus, Integer[] pow2Flipped) {
		 this.queue = queue;
		 this.pow2Flipped    = pow2Flipped;
		 this.modulus = modulus;

	}
	@Override

    public void run() {
		boolean status = false;

		int ii = 0;
		for ( int i = 0; i <= pow2Flipped[pow2Flipped.length-1] && !stopit; i++ ) {
			if (i == pow2Flipped[ii]) {
			   status = queue.offer(a);
			   if ( status == false ) {
				   System.out.println("Queue insert failed");
			   }
			   ii += 1;
			}
			a = a.pow(2).mod(modulus);
			   if (i > 0 && i % 10000 == 0 ) {
				   System.out.println( "percent complete " + (float)i/(float)pow2Flipped[pow2Flipped.length-1] * 100 );
			   }
		}
		if (stopit) {
			queue = null;
		}
	}

	public void stop() {
		this.stopit = true;
	}
}
