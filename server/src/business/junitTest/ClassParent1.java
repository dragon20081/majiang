package business.junitTest;

import server.mj.MgsPlayer;

public class ClassParent1{

	private MgsPlayer player;

	public MgsPlayer getPlayer() {
		return player;
	}

	public void setPlayer(MgsPlayer player) {
		this.player = player;
	}
	public void setbytes()
	{
		if(player == null)
			System.out.println("player == null");
		else
			System.out.println("player != null");
	}

}
