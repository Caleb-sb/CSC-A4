import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;


import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
//model is separate from the view.

public class WordApp {
//shared variables
	static int noWords=4;
	static int totalWords;

   	static int frameX=1000;
	static int frameY=600;
	static int yLimit=480;
	static JLabel caught;
	static JLabel missed;
	static JLabel scr;

	//Communication booleans
	static volatile AtomicBoolean reset = new AtomicBoolean(false);
	static volatile boolean quit;
	static final int DIFFICULTY = 20;
	static volatile AtomicBoolean updatePending = new AtomicBoolean(false);
	static volatile AtomicBoolean scoreUpdatePending = new AtomicBoolean(false);
	static volatile AtomicBoolean checkWord = new AtomicBoolean(false);

	static WordDictionary dict = new WordDictionary(); //use default dictionary, to read from file eventually

	static WordRecord[] words;
	//static volatile boolean done;  //must be volatile

	static 	Score score = new Score();
	static JButton startB;
	static JButton endB;
	static WordPanel w;
	static JFrame frame = new JFrame("WordGame");
	final static JTextField textEntry = new JTextField("",20);


	public static void setupGUI(int frameX,int frameY,int yLimit) {
		// Frame init and dimensions
    	//JFrame frame = new JFrame("WordGame");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(frameX, frameY);

      	JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS));
      	g.setSize(frameX,frameY);


		w = new WordPanel(words,yLimit);
		w.setSize(frameX,yLimit+100);
	    g.add(w);


	    JPanel txt = new JPanel();
	    txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS));
	    caught =new JLabel("Caught: " + score.getCaught() + "    ");
	    missed =new JLabel("Missed:" + score.getMissed()+ "    ");
	    scr =new JLabel("Score:" + score.getScore()+ "    ");
	    txt.add(caught);
	    txt.add(missed);
	    txt.add(scr);

	    //[snip]

			textEntry.setEnabled(false);
	   textEntry.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent evt) {
	          String text = textEntry.getText();
						for (int i = 0; i< noWords; i++)
						{
							if(words[i].matchWord(text))
							{
								synchronized (score)
								{
									score.caughtWord(text.length());
									WordApp.scoreUpdatePending.set(true);
								}
								break;
							}
						}

	          textEntry.setText("");
	          textEntry.requestFocus();
	      }
	    });

	   txt.add(textEntry);
	   txt.setMaximumSize( txt.getPreferredSize() );
	   g.add(txt);

	    JPanel b = new JPanel();
        b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));

				startB = new JButton("Start");

			// add the listener to the jbutton to handle the "pressed" event
			startB.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		    	  Thread t = new Thread(w);
						WordApp.reset.set(false);
						t.start();
						startB.setEnabled(false);
						textEntry.setEnabled(true);
						endB.setEnabled(true);
		    	  textEntry.requestFocus();  //return focus to the text entry field
		      }
		    });

		//END Button
		endB = new JButton("End");
		endB.setEnabled(false);
				// add the listener to the jbutton to handle the "pressed" event
				endB.addActionListener(new ActionListener()
			    {
			      public void actionPerformed(ActionEvent e)
			      {
							startB.setEnabled(true);
							textEntry.setEnabled(false);
			    	  WordApp.reset.set(true);
							endB.setEnabled(false);
			      }
			    });

		JButton quitB = new JButton("Quit"); //Should quit the program (same as x button)

				// add the listener to the jbutton to handle the "pressed" event
				quitB.addActionListener(new ActionListener()
			    {
			      public void actionPerformed(ActionEvent e)
			      {
			    	  System.exit(0);
			      }
			    });

		b.add(startB);
		b.add(endB);
		b.add(quitB);
		g.add(b);

      	frame.setLocationRelativeTo(null);  // Center window on screen.
      	frame.add(g); //add contents to window
        frame.setContentPane(g);
       	//frame.pack();  // don't do this - packs it into small space
        frame.setVisible(true);


	}


public static String[] getDictFromFile(String filename) {
		String [] dictStr = null;
		try {
			Scanner dictReader = new Scanner(new FileInputStream(filename));
			int dictLength = dictReader.nextInt();
			System.out.println("read '" + dictLength+"'");

			dictStr=new String[dictLength];
			for (int i=0;i<dictLength;i++) {
				dictStr[i]=new String(dictReader.next());
				//System.out.println(i+ " read '" + dictStr[i]+"'"); //for checking
			}
			dictReader.close();
		} catch (IOException e) {
	        System.err.println("Problem reading file " + filename + " default dictionary will be used");
					e.printStackTrace();
	    }
		return dictStr;

	}

	public static void main(String[] args) {

		//deal with command line arguments
		totalWords=Integer.parseInt(args[0]);  //total words to fall
		noWords=Integer.parseInt(args[1]); // total words falling at any point
		assert(totalWords>=noWords); // this could be done more neatly
		String[] tmpDict=getDictFromFile(args[2]); //file of words
		if (tmpDict!=null)
			dict= new WordDictionary(tmpDict);

		WordRecord.dict=dict; //set the class dictionary for the words.

		words = new WordRecord[noWords];  //shared array of current words

		//[snip]

		setupGUI(frameX, frameY, yLimit);
    	//Start WordPanel thread - for redrawing animation

		int x_inc=(int)frameX/noWords;
	  	//initialize shared array of current words

		for (int i=0;i<noWords;i++) {
			words[i]=new WordRecord(dict.getNewWord(),i*x_inc,yLimit);
		}


	}

	public static void finishGame(boolean done)
	{
		if(done)
		{
			textEntry.setEnabled(false);
			JOptionPane.showMessageDialog(w, "You don't win or lose");
			startB.setEnabled(true);
			endB.setEnabled(false);
		}
	}

}
