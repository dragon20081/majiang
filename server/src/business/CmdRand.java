package business;

import common.Log;

public class CmdRand {
	
	private  int _seed = -1;
	private  int  seed = -1;
	
	public CmdRand()
	{
		setSeed();
	}
	public int next()
	{
		do
		{
			_seed^=(_seed<<21);
			_seed^=(_seed>>21);
			_seed^=(_seed<<4);
//			_seed++;
		}
		while(_seed == 0 || seed == _seed);
		 seed = _seed;
		 Log.log("cmdRand next -->"+seed);
		return _seed;
	}
	public void setSeed()
	{
		int rand  = (int) (Math.random()*10000);
		this._seed =  rand;
		seed = _seed;
		 Log.log("cmdRand setSeed -->"+seed);
	}
	public int getSeed() {
		return seed;
	}
	public void setSeed(int s)
	{
		this._seed =  s;
		seed = _seed;
		 Log.log("cmdRand setSeed 2 -->"+seed);
	}
	
	
	
}
