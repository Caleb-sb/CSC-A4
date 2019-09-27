/*
* Created in an effort to parallelise as much as possible
* Each word gets their own Controller thread to lower them at their indiv speed
* The Controller in MVC. Alters Model and View.
* Animates, adds and removes words, Updates Model counters
* BRDCAL003
*/
public class Controller implements Runnable{

  private WordRecord word;
  private Score score;

  Controller(WordRecord word)
  {
    this.word = word;
  }

  @Override
  public void run()
  {
    while(!WordApp.reset.get())
    {
      word.drop(1);
      if (word.dropped())
      {
        synchronized (WordApp.score)
        {
          WordApp.score.missedWord();
        }
        WordApp.scoreUpdatePending.set(true);
        word.resetWord();
      }
      WordApp.updatePending.set(true);

      //System.out.println("lowering?");
      try{
        Thread.sleep(word.getSpeed()/WordApp.DIFFICULTY);
      }
      catch(InterruptedException e)
      {
        e.printStackTrace();
      }

    }
  }
}
