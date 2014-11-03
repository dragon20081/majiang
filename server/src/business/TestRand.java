package business;
public class TestRand {

	/**
	 * @param args
	 */
	public int seed = 0;
	public int _seed = 0;
	
	public TestRand()
	{
		seed = _seed = 0;
	}
	public int next()
	{
		_seed^=(_seed<<21);
		_seed^=(_seed>>21);
		_seed^=(_seed<<4);
//		return _seed*MAXRATIO;
		return _seed;
	}
	
	
	public static void main(String[] args) {
		TestRand tr = new TestRand();
		int i=0;
		for(i=0;i<100000;i++)
		{
			System.out.println(i);
			System.out.println(tr.seed);
			System.out.println(tr.next());
			System.out.println("**********");
		}

	}

}
