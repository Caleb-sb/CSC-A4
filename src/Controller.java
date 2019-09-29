/**
* @author Caleb Bredekamp
* @author BRDCAL003
* Created in an effort to parallelise as much as possible
* Each word gets their own Controller thread to lower them at their indiv speed
* The Controller in MVC. Alters Model and View.
* Animates, adds and removes words, Updates Model counters
*/
public class Controller implements Runnable{

  private WordRecord word;
  private long difficulty;
  //private Score score;

  Controller(WordRecord word)
  {
    this.word = word;
  }

  @Override
  public void run()
  {
    difficulty = 0;
    while(!WordApp.reset)
    {
      word.drop(1);
      if (word.dropped())
      {
        synchronized (WordApp.score)
        {
          WordApp.score.missedWord();
        }
        WordApp.scoreUpdatePending = true;
        word.resetWord();
      }
      WordApp.updatePending= true;

      //System.out.println("lowering?");
      try{

        Thread.sleep(word.getSpeed()/10);//word.getSpeed()/10);
        //System.out.println(word.getSpeed());
      }
      catch(InterruptedException e)
      {
        e.printStackTrace();
      }

    }
  }
}
