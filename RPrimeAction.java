package RPrimes;
/*
 * RPrimeAction executes the Tests selected from RPrimesUI in a separate thread
 * The output is redirected to the textArea passed and the Buttons are disabled
 * until the test is complete
 * @author		Tejas Rao
 * @version		0.1
 * @since		2018-11-25
 */

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class RPrimeAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

		private JTextArea textArea;
       
        private JFormattedTextField multiplier;
        private JFormattedTextField base;
        private JFormattedTextField pow;
        private JFormattedTextField offset;
        private JFormattedTextField startN;
        private JFormattedTextField endN;
        private JScrollPane sp;
        private JButton Btn;
        private JButton otherBtn;
        public final static Object lock = new Object();
		public RPrimeAction(JButton Button,JFormattedTextField multiplier,JFormattedTextField base,JFormattedTextField pow,JFormattedTextField offset,JFormattedTextField startN,JFormattedTextField endN,JScrollPane sp, JButton otherButton) {
			this.Btn = Button;
			this.sp = sp;
			this.textArea = (JTextArea) sp.getViewport().getView();
			this.multiplier = multiplier;
			this.base = base;
			this.pow = pow;
			this.offset = offset;
			this.startN = startN;
			this.endN = endN;
			this.otherBtn = otherButton;
		}
		public void actionPerformed(ActionEvent e) {
			redirectSystemStreams();
			
			System.out.println("Starting  " + Btn.getText());

			BigInteger origPrime = new BigInteger(multiplier.getText());
			origPrime = origPrime.multiply(BigInteger.valueOf(Integer.parseInt(base.getText())).pow(Integer.parseInt(pow.getText())));
			origPrime = origPrime.add(BigInteger.valueOf(Integer.parseInt(offset.getText())));
			RPrimes rprime = new RPrimes();
			Integer endNValue = Integer.parseInt(endN.getText());
			if ( endNValue < 0) {
				endNValue = Integer.MAX_VALUE;
			}
			Integer startNValue = Integer.parseInt(startN.getText());
			if ( startNValue < 2 ) {
				System.out.println("StartN Value must atleast be 2");
				return;
			}
			if (Btn.getText() == "Primover Test") {
				int n = JOptionPane.showConfirmDialog(null, "Stop At First N ?", "Run Option",JOptionPane.YES_NO_OPTION);
	            if ( n >= 0) {
	            	findAllPrimoverWithin f = new findAllPrimoverWithin(rprime,origPrime,startNValue,endNValue,n == 0,Btn,otherBtn);
	            	Thread t = new Thread(f);
	            	Btn.setEnabled(false);
	            	otherBtn.setEnabled(false);
	            	t.start();
				}
			} else {
				if (startNValue != endNValue) {
					System.out.println("Only StartN is used for Fermat Divisor Test");
				}
				isGF3Divisor isG = new isGF3Divisor(rprime,origPrime,startNValue,Btn,otherBtn);
				Thread t2 = new Thread(isG);
				Btn.setEnabled(false);
				otherBtn.setEnabled(false);
				t2.start();
			}
			
		}
		private class findAllPrimoverWithin implements Runnable {
			private BigInteger origPrime;
			private Integer startNValue;
			private Integer endNValue;
			private boolean stopAtFirst;
			private RPrimes rprime;
			private JButton Btn, otherBtn;
			public findAllPrimoverWithin(RPrimes rprime,BigInteger origPrime,Integer startNValue,  Integer endNValue, boolean stopAtFirst,JButton Btn, JButton otherBtn) {
				this.origPrime = origPrime;
			    this.startNValue = startNValue;
			    this.endNValue = endNValue;
			    this.stopAtFirst = stopAtFirst;
			    this.rprime = rprime;
			    this.Btn = Btn;
			    this.otherBtn = otherBtn;
			}
			public void run() {
		       	   ArrayList<Integer> primoverN = rprime.findAllPrimoverWithin(origPrime, startNValue,  endNValue, stopAtFirst);
		       	   System.out.println("n such that p2^n+1 is primover: " + primoverN.toString());
		       	   startN.setText(primoverN.get(0).toString());
		       	   Btn.setEnabled(true);
		       	   otherBtn.setEnabled(true);
			}
		}
		private class isGF3Divisor implements Runnable {
			private BigInteger origPrime;
			private Integer startNValue;
			RPrimes rprime;
			private JButton Btn, otherBtn;
			public isGF3Divisor(RPrimes rprime, BigInteger origPrime, Integer startNValue,JButton Btn, JButton otherBtn) {
				this.origPrime = origPrime;
				this.startNValue = startNValue;
				this.rprime = rprime;
			    this.Btn = Btn;
			    this.otherBtn = otherBtn;
			}
			public void run() {
				boolean isFermat = rprime.isGF3Divisor(origPrime, startNValue);
				if (isFermat) {
					Integer res = startNValue-1;
					System.out.println("p2^n+1 divides GF(3," + res);
				}
				else {
					System.out.println("p2^n+1 does not divide any base three GF");
				}
				Btn.setEnabled(true);
				otherBtn.setEnabled(true);
			}
		}

			 
			private void redirectSystemStreams() {

			  OutputStream out = new OutputStream() {
			    @Override
			    public void write(int b) throws IOException {
			    	//updateTextArea(String.valueOf((char) b));
			    	textArea.append(String.valueOf((char) b));
			    	 textArea.setCaretPosition(textArea.getDocument().getLength() );
			    	 textArea.update(textArea.getGraphics());
                     sp.validate();
			    }
			    
			 
			    @Override
			    public void write(byte[] b, int off, int len) throws IOException {
			      //updateTextArea(new String(b, off, len));
			    	textArea.append(new String(b, off, len));
			    	 textArea.setCaretPosition(textArea.getDocument().getLength() );
			    	 textArea.update(textArea.getGraphics());
			    	 sp.validate();
			    }
			    
			 
			    @Override
			    public void write(byte[] b) throws IOException {
			      write(b, 0, b.length);
			    }
			  };
			 
			  System.setOut(new PrintStream(out, true));
			  System.setErr(new PrintStream(out, true));
			}
}
