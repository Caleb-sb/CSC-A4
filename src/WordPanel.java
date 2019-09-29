/**
* The View in MVC
* Gets communication from the Controlling class to update
*/

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;
import javax.swing.JButton;
import javax.swing.JPanel;


public class WordPanel extends JPanel implements Runnable {
		public static volatile boolean done;
		private WordRecord[] words;
		private int noWords;
		private int maxY;


		public void paintComponent(Graphics g) {
		    int width = getWidth();
		    int height = getHeight();
		    g.clearRect(0,0,width,height);
		    g.setColor(Color.red);
		    g.fillRect(0,maxY-10,width,height);

		    g.setColor(Color.black);
		    g.setFont(new Font("Helvetica", Font.PLAIN, 26));
		   //draw the words
		   //animation must be added
		    for (int i=0;i<noWords;i++){
		    	//g.drawString(words[i].getWord(),words[i].getX(),words[i].getY());
		    	g.drawString(words[i].getWord(),words[i].getX(),words[i].getY()+20);  //y-offset for skeleton so that you can see the words
		    }

		  }

		WordPanel(WordRecord[] words, int maxY) {
			this.words=words; //will this work?
			noWords = words.length;
			done=false;
			this.maxY=maxY;
		}

		public void run() {
			//Creates Controller Threads
			for(int i = 0; i < noWords; i++)
			{
				Controller c = new Controller(words[i]);
				Thread t = new Thread(c);
				t.start();
			}

			//Takes in communication booleans to update View
			while(!WordApp.reset)
			{
				if(WordApp.updatePending)
				{
					if(WordApp.scoreUpdatePending)
					{
						synchronized (WordApp.score)
						{
							//Updates all the View's counters
							WordApp.missed.setText("Missed: " + WordApp.score.getMissed() + "    ");
							WordApp.caught.setText("Caught: " + WordApp.score.getCaught()+ "    ");
							WordApp.scr.setText("Score: "+ WordApp.score.getScore()+ "    ");
							WordApp.scoreUpdatePending=false;

							//Updates View to end game
							if(WordApp.score.getTotal() >= WordApp.totalWords)
							{
								//WordRecord.easyMode();
								WordApp.reset=true;
								WordApp.finishGame();
							}
						}
					}
					repaint();
					Toolkit.getDefaultToolkit().sync();
					WordApp.updatePending=false;

				}
			}
			//Sets View scores to zero after reset occurs
			synchronized (WordApp.score)
			{
				WordApp.score.resetScore();
				WordApp.missed.setText("Missed: " + WordApp.score.getMissed() + "    ");
				WordApp.caught.setText("Caught: " + WordApp.score.getCaught()+ "    ");
				WordApp.scr.setText("Score: "+ WordApp.score.getScore()+ "    ");
				WordApp.textEntry.setText("");
			}

			//Resets the words to zero position to mimic the skeleton pre-start screen
			WordRecord.easyMode();
			for(int i = 0; i < noWords; i++)
			{

				words[i].resetWord();
			}
			repaint();
		}

	}
