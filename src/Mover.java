/*
* Created in an effort to parallelise as much as possible
* Each word gets their own mover thread to lower them at their indiv speed
* BRDCAL003
*/
public class Mover implements Runnable{

  private WordRecord word;

  Mover(WordRecord word)
  {
    this.word = word;
  }

  @Override
  public void run()
  {
    while(!WordApp.paused)
    {
      word.drop(1);
      WordApp.updatePending = true;
      System.out.println("lowering?");
      try{
        Thread.sleep(word.getSpeed()/(1/WordApp.DIFFICULTY));
      }
      catch(InterruptedException e)
      {
        e.printStackTrace();
      }

    }
  }
}
