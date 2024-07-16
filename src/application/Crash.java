package application;

public class Crash extends Thread {
	GameProper game;

	public Crash(GameProper game)
	{
		this.game = game;
	}
	@Override
	public void run() {
		try{
			game.crashed();
			for(int i = 0; i<3; i++)
			{
				game.blink();
				Thread.sleep(500);
				if(i==0)
					game.uncrashed();
				game.unblink();
				Thread.sleep(500);
				if(i==0)
					game.stopCrashSFX();
			}

            game.reset(game);
        }
        catch(Exception e){
            System.out.println(e);
        }
	}
}
