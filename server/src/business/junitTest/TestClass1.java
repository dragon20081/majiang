package business.junitTest;

import server.mj.MgsPlayer;

public class TestClass1  extends ClassParent1 {

	
//	private MgsPlayer player;
//
//	public MgsPlayer getPlayer() {
//		return player;
//	}
//
//	public void setPlayer(MgsPlayer player) {
//		this.player = player;
//	}
	
	public void setbytes()
	{
		super.setbytes();
		System.out.println("child setbytes");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestClass1  t1 = new TestClass1();
		t1.setPlayer(new MgsPlayer());
		t1.setbytes();
	}

}
