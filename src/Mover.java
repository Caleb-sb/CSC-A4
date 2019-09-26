/*
* Created in an effort to parallelise as much as possible
* Each word gets their own mover thread to lower them at their indiv speed
* BRDCAL003
*/
public class Mover implements Runnable{

  private WordRecord word;
  private Score score;

  Mover(WordRecord word)
  {
    this.word = word;
  }

  @Override
  public void run()
  {
    while(!Governor.paused)
    {
      word.drop(1);
      if (word.dropped())
      {
        synchronized (WordApp.score)
        {
          WordApp.score.missedWord();
        }
        Governor.scoreUpdatePending.set(true);
        word.resetWord();
      }
      Governor.updatePending.set(true);

      //System.out.println("lowering?");
      try{
        Thread.sleep(word.getSpeed()/Governor.DIFFICULTY);
      }
      catch(InterruptedException e)
      {
        e.printStackTrace();
      }

    }
  }
}
