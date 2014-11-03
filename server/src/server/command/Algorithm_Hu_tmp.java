package server.command;

import org.junit.Test;

/**   
万牌:   一万至九万,用1~9表示   
条牌:   一条至九条,用21~29表示   
筒牌:   一筒至九筒,用41~49表示   
字牌:   东61,   南64,   西67,   北70,   中73,   发76,   白79   
百搭:   用100表示   
  
麻将中以十四张牌计算胡牌   
胡牌的基本牌型   
aa   xxx   xxx   xxx   xxx   
(注：aa=将或对子   xxx=任意的顺子(如123)、刻子(如444))   
 */   
public class Algorithm_Hu_tmp {

	public final int 	 maxg  = 14; //the   max.   number   of   groups   divided  
	public final int	 maxc  = 28; //the   max.   number   of   groups   created   
	public final int	 bd    = 100;//wild   card   
	private int[]   	mjlist  = new int[14];   
	private int[]   	select  = new int[14];   
	private int[][][]   glist   = new int[maxg][maxc][3];     
	private int[][]     trace   = new int[maxg][3];   
	private int   node;   
	
	private  int   creategroup(int d)
	{
		int   c,c1,i,j,k,t,nbd;   
		  
		nbd   =   0;   
		for(i=0;   i<14;   i++)   
		{   
		if(mjlist[i]   ==   bd)   
		nbd++;   
		}   
		  
		  
		for(i=0;   i<d;   i++)   
		{   
		if(trace[i][1]==-1   ||   trace[i][2]!=-1)   
		continue;   
		if(mjlist[trace[i][0]]   !=   mjlist[trace[i][1]])   
		nbd--;   
		  
		}   
		  
		if   (nbd<0)   
		{   
		  
		return   -1;   
		}   
		  
		for(c=0;   c<maxc;   c++)   
		{   
			glist[d][c][0]   =   -1;   
			glist[d][c][1]   =   -1;   
			glist[d][c][2]   =   -1;   
		}   
		c   =    0;   
		t   =   -1;   
		for(i=0;   i<14;   i++) //   search   ke   zi   
		{   
		if(select[i]==1   ||   t==mjlist[i]   ||   mjlist[i]==bd)   
		continue;   
		t   =   mjlist[i];   
		k   =   0;   
		for(j=i;   j<14;   j++)   
		if(mjlist[j]==t)   
		{   
		glist[d][c][k]   =   j;   
		k++;   
		if(k==3)   
		break;   
		}   
		c++;   
		}   
		  
		c1   =   c;   
		t   =   -1;   
		for(i=0;   i<14;   i++) //   search   shun   zi   
		{   
		  
		if(select[i]==1   ||   t==mjlist[i]   ||   mjlist[i]==bd)   
		continue;   
		t   =   mjlist[i];   
		  
		k   =   0;   
		for(j=i;   j<14;   j++)   
		if(mjlist[j]==t+1   &&   select[j]==0)   
		{   
		k++;   
		glist[d][c][k]   =   j;   
		  
		break;   
		}   
		  
		for(j=i;   j<14;   j++)   
		if(mjlist[j]==t+2   &&   select[j]==0)   
		{   
		k++;   
		glist[d][c][k]   =   j;   
		  
		break;   
		}   
		if(k+nbd   >=   2)   
		{   
		glist[d][c][0]   =   i;       
		c++;   
		c1   -=   k;     
		}   
		}   
		if(c1>5)   
		return   -1;   
		return   c;   
	}
	private void   makegroup(int[] gr)
	{
		int   i;   
		for   (i=0;   i<3;   i++)   
		{   
		if(gr[i]   !=   -1)   
		select[gr[i]]   =   1;   
		}   
	}
	private int   search(int   d)
	{
		node++;   
		int   count,   i,   j,   res;   
		int[]   orgsel =  new int[14];   
		count   = creategroup(d);   
		if(count==0)   
		return   iswin();   
		  
		if(d==5   &&   count>0)   
		return   0;   
		  
		for(i=0;   i<count;   i++)   
		{   
		for(j=0;   j<14;   j++)   
		orgsel[j]   =   select[j];   
		  
		  
		makegroup(glist[d][i]);   
		  
		  
		trace[d][0]   =   glist[d][i][0];   
		trace[d][1]   =   glist[d][i][1];   
		trace[d][2]   =   glist[d][i][2];   
		  
		  
		  
		res   =   search(d+1);   
		  
		for(j=0;   j<14;   j++) //   unmake   
		select[j]   =   orgsel[j];   
		  
		if(res==0)   
		{   
		trace[d][0]   =   -2;   
		trace[d][1]   =   -2;   
		trace[d][2]   =   -2;   
		}   
		  
		if   (res!= 0)   
		return   1;   
		}   
		return   0;   
	}
	private int   iswin()
	{
		int   d,   i,   sum;   
		int[]   grlen = new int[maxg]; //   group   length   
		  
		for(d=0;   d<maxg;   d++)   
		{   
		grlen[d]   =   0;   
		}   
		  
		for(d=0;   ;d++)   
		{   
		if(trace[d][0]==-2)   
		break;   
		  
		int   t   =   mjlist[trace[d][1]]   -   mjlist[trace[d][0]];   
		if(t==1   ||   t==2) //   shun   zi   group   
		grlen[d]   =   3;   
		else   
		{   
		for(i=0;   i<3;   i++)   
		if(trace[d][i]   !=   -1)   
		grlen[d]++;   
		if(grlen[d]<2)   
		grlen[d]   =   2;   
		}   
		}   
		  
		sum   =   0;   
		for(d=0;   d<maxg;   d++)   
		{   
		sum   +=   grlen[d];   
		}   
		  
		  
		if(sum   <=   14) //   sum   is   the   minimum   length     
		return   1;   
		  
		else   
		return   0;   
	}
	private void   disp()
	{
		int   i,   j,   n;   
		int[][]   gdisp = new int[maxg][3];   
		  
		for(i=0;   i<maxg;   i++)   
		{   
		gdisp[i][0]   =   0;   
		gdisp[i][1]   =   0;   
		gdisp[i][2]   =   0;   
		}   
		  
		n   =   0;   
		for(i=0;   i<14;   i++)   
		if(mjlist[i]   ==   bd)   
		n++;   
		  
		  
		for(i=0;   i<maxg;   i++)     
		{   
		if(trace[i][0]   ==   -2)   
		break;   
		  
		gdisp[i][0]   =   mjlist[trace[i][0]];   
		  
		  
		if   (trace[i][1]   !=   -1)   
		gdisp[i][1]   =   mjlist[trace[i][1]];   
		  
		if   (trace[i][2]   !=   -1)   
		gdisp[i][2]   =   mjlist[trace[i][2]];   
		  
		  
		}   
		  
		for(i=0;   i<maxg;   i++)     
		{   
		if(n==0   ||   gdisp[i][0]==0)   
		break;   
		  
		if(gdisp[i][1]   ==   0)   
		{   
		gdisp[i][1]   =   bd;   
		n--;   
		continue;   
		}   
		  
		if(gdisp[i][2]==0   &&   gdisp[i][0]!=gdisp[i][1])   
		{   
		gdisp[i][2]   =   bd;   
		n--;   
		}   
		  
		}   
		for(i=0;   ;i++)   
		{   
		if(n==0)   
		break;   
		if(gdisp[i][0]==0)   
		{   
		gdisp[i][0]   =   bd;   
		n--;   
		if(n==0)   
		break;   
		}   
		  
		if(gdisp[i][1]==0)   
		{   
		gdisp[i][1]   =   bd;   
		n--;   
		if(n==0)   
		break;   
		}   
		  
		if(gdisp[i][2]==0)   
		{   
		gdisp[i][2]   =   bd;   
		n--;   
		if(n==0)   
		break;   
		}   
		  
		}   
		  
		for(i=0;   i<maxg;   i++)     
		{   
		if(gdisp[i][0]==0)   
		break;   
		  
		for(j=0;   j<3;   j++)   
		{   
		if(gdisp[i][j]   ==   0)   
		break;   
		System.out.println(gdisp[i][j] );
		}   
		System.out.println("endl" );
		  
		}   
		System.out.println("endl" );
}

	/**
		//		cout   <<   "万牌:   一万至九万,用1~9表示"<<endl;   
		//		cout   <<   "条牌:   一条至九条,用21~29表示"<<endl;   
		//		cout   <<   "筒牌:   一筒至九筒,用41~49表示"<<endl;   
		//		cout   <<   "字牌:   东61,   南64,   西67,   北70,   中73,   发76,   白79"<<endl;   
		//		cout   <<   "百搭:   用100表示"<<endl<<endl;   
		//		cout   <<   "请按从小到大输入牌局："<<endl;   
 * */
	
	public void start(int[] mj)
	{
		int   i,   j;   
		  
		for(i=0;   i<14;   i++)   
		{   
			mjlist[i]   =   mj[i];   
		}   
		for(i=0;   i<14;   i++)   
		select[i]   =   0;   
		for(i=0;   i<maxg;   i++)     
		for(j=0;   j<3;   j++)     
		trace[i][j]   =   -2;   
		node   =   0;   
		int   r;   
		r   =   search(0);   
		if(r == 0)   
		{   
		disp();   // win!
		System.out.print("win");
		}   
		else  
			System.out.print("fail");
			//fail !
	}
	@Test
	public void test1()
	{
		start(new int[]{1,2,3, 5,6,7, 21,21,21, 23,23,23, 70,70});
	}
  
}
