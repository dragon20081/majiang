package server.mj;

public interface MgsRoom {
	  
	public static final int  CREATED   =  1;
	public static final int  STARTED   =  2;
	public static final int  ENDED     =  3;
	public static final int  DESTORYED =  4;
	public  void createdRoom();
	public void started();
	public void ended();
	public void destoryRoom();
}
