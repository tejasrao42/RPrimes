package RPrimes;
/*
 * RPrimesUI is a simple interface to calling the functions in RPrimes.java
 * The input prime is specified as multiplier * base ^ pow + offset
 * startN is used to restart from a specific value when a previous run is aborted
 * or is used after a run to test if the result of the Primover test 
 * is also a generalized fermat divisor
 * endN is not used in the Generalized Fermat Divisor test
 * @author Tejas Rao
 * @version 0.4
*/
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;
import javax.swing.JFormattedTextField;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.text.NumberFormat;

public class RPrimesUI {
  
//fields for entry
private static JFormattedTextField multiplier;
private static JFormattedTextField base;
private static JFormattedTextField pow;
private static JFormattedTextField offset;
private static JFormattedTextField endN;
private static JFormattedTextField startN;
private static NumberFormat inFormat = NumberFormat.getInstance();
private static JTextArea textArea;
private static JButton rprimeBtn;
private static JButton fermatBtn;
private static JScrollPane scroll;


    
public static void main( String[] args ) {

    JFrame guiFrame = new JFrame();
    JPanel inputPanel = new JPanel();
    JPanel requiredPanel = new JPanel();
    JPanel optionalPanel = new JPanel();
    JPanel outputPanel = new JPanel();
    JPanel submitPanel = new JPanel();

    rprimeBtn = new JButton("Primover Test");
    fermatBtn = new JButton("Fermat Divisior Test");
    textArea = new JTextArea();
    ((DefaultCaret) textArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    guiFrame.setTitle("RPrime Test");
    guiFrame.setSize(400,400);
    guiFrame.setLocationRelativeTo(null);
    guiFrame.setLayout(new BorderLayout());
   
    inFormat.setMaximumIntegerDigits(1000);
    inFormat.setGroupingUsed(false);
    inputPanel.setLayout(new GridLayout(2,1));
    Border border = BorderFactory.createTitledBorder("Specify Prime Number (Required)");
    requiredPanel.setBorder(border);
    requiredPanel.setLayout(new GridLayout(1,1));
    border = BorderFactory.createTitledBorder("Optional");
    optionalPanel.setBorder(border);
    optionalPanel.setLayout(new GridLayout(1,1));
    inputPanel.add(requiredPanel);
    inputPanel.add(optionalPanel);
    guiFrame.getContentPane().add(inputPanel,BorderLayout.PAGE_START);

    border = BorderFactory.createTitledBorder("Output");
    outputPanel.setBorder(border);
    outputPanel.setLayout(new GridLayout(1,1));
    textArea.setRows(50);
    scroll = new JScrollPane(textArea);
    scroll.setViewportView(textArea);
    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    outputPanel.add(scroll,BorderLayout.CENTER);
    //outputPanel.add(textArea,BorderLayout.CENTER);
    guiFrame.getContentPane().add(outputPanel);
    multiplier = addField(requiredPanel,"Multiplier","1");
    base = addField(requiredPanel,"Base","2");
    pow = addField(requiredPanel,"Power","0");
    offset = addField(requiredPanel,"Offset","1");
    startN = addField(optionalPanel,"StartN","2");
    endN = addField(optionalPanel,"EndN","-1");

	rprimeBtn.addActionListener(new RPrimeAction(rprimeBtn,multiplier, base, pow, offset, startN, endN,scroll,fermatBtn));
	fermatBtn.addActionListener(new RPrimeAction(fermatBtn,multiplier, base, pow, offset, startN, endN,scroll,rprimeBtn));

    submitPanel.add(rprimeBtn);
    submitPanel.add(fermatBtn);
    guiFrame.getContentPane().add(submitPanel,BorderLayout.PAGE_END);


    UIManager.put("swing.boldMetal", Boolean.FALSE);
    guiFrame.revalidate();
    guiFrame.setVisible(true);
    
}

private static JFormattedTextField addField(JPanel inputPanel,String lbl, String num) {
	//Dimension d = new Dimension(150,20);
	JPanel mp = new JPanel();
    Border border = BorderFactory.createTitledBorder(lbl);
    mp.setBorder(border);
    JFormattedTextField fld = new JFormattedTextField(inFormat);
    fld.setText(num);

    mp.setLayout(new GridLayout(1,1));
    //inputPanel.setLayout(new GridLayout(1,1));

    //mp.setSize(d);
    mp.add(fld);
    inputPanel.add(mp);
    return fld;
   

}



}

