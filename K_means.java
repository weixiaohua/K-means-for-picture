package wei.hua.kmeans;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class K_means
{
	static DataItem[] center;
	static DataItem[] centerSum;
	static DataItem[][] data;
	public K_means()
	{
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		for (int i = 1; i <= 10; i++)
		{
			
			kmeans("mww.jpg",i);	
		}
		kmeans("mww.jpg",20);
		kmeans("mww.jpg",50);
		kmeans("mww.jpg",100);
		kmeans("mww.jpg",200);
	}
	public static int [][]  dataIn(String path)
	{
		BufferedImage bi = null;
		try
		{
			bi = ImageIO.read(new File(path));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		int width = bi.getWidth();
		int height = bi.getHeight();
		int [][] data = new int[width][height];
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				data[i][j] = bi.getRGB(i, j);//rgb
				//System.out.print(grey[i][j]);
			}
			//System.out.println();
		}
		return data;
	}
	public static void  dataOut(String path)
	{
		BufferedImage nbi = new BufferedImage(data.length, data[0].length, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < data.length; i++)
		{
			for (int j = 0; j < data[0].length; j++)
			{
				//center[data[i][j].group]
				Color c = new Color(center[data[i][j].group].r, center[data[i][j].group].g,center[data[i][j].group].b);
				nbi.setRGB(i, j, c.getRGB());
				//System.out.print(c.getRGB()+" ");
			}
			//System.out.println();
		}
		try
		{
			ImageIO.write(nbi, "jpg", new File(path));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	public static void  kmeans(String path , int k)
	{
		data=initData(dataIn(path));
		initCenterCluster(k);
		int sum = 0;
		int j = 0;
		while(sum!=center.length)
		{
			j++;
			setGroup();
			sum = setNewCenter();			
		}
		System.out.println("第"+j+"次迭代完成，聚类中心为：");
		for (int i = 0; i < center.length; i++)
		{				
			System.out.println("("+center[i].x+","+center[i].y+","+center[i].r+","+center[i].g+","+center[i].b+")");
		}
				
		System.out.println("迭代总次数："+j);
		dataOut(k+"-"+path);
	}

	public static int setNewCenter()
	{
		int sum = 0;
		for (int i = 0; i < centerSum.length; i++)
		{
			if(center[i].x==(int)(centerSum[i].x/centerSum[i].group)&&
					center[i].y==(int)(centerSum[i].y/centerSum[i].group)&&
					center[i].r==(int)(centerSum[i].r/centerSum[i].group)&&
					center[i].g==(int)(centerSum[i].g/centerSum[i].group)&&
					center[i].b==(int)(centerSum[i].b/centerSum[i].group))
			{
				sum++;
			}else{
				center[i].x=(int)(centerSum[i].x/centerSum[i].group);
				center[i].y=(int)(centerSum[i].y/centerSum[i].group);
				center[i].r=(int)(centerSum[i].r/centerSum[i].group);
				center[i].g=(int)(centerSum[i].g/centerSum[i].group);
				center[i].b=(int)(centerSum[i].b/centerSum[i].group);
			}
		}
		return sum;
	}
	public static void setGroup()
	{
		int dis = 100000;
		int group = -1;
		for (int k = 0; k < data.length; k++)
		{
			for (int q = 0; q < data[0].length; q++)
			{
				for (int i = 0; i < center.length; i++)
				{
					if (dis >= getDis(center[i], data[k][q]))
					{
						dis = getDis(center[i], data[k][q]);
						group = i;
					}				
				}
				data[k][q].group = group;
				centerSum[group].x += data[k][q].x;
				centerSum[group].y += data[k][q].y;
				centerSum[group].r += data[k][q].r;
				centerSum[group].g += data[k][q].g;
				centerSum[group].b += data[k][q].b;
				centerSum[group].group += 1;
				group = -1;
				dis = 100000;
			}
		}

	}
	public static void initCenterCluster(int k)
	{
		center = new DataItem[k];
		centerSum = new DataItem[k];
		
		int width;int height;
		System.out.println("---随机初始化k个中心点---");
		for (int i = 0; i < k; i++)
		{
			DataItem cen = new DataItem();
			DataItem cen1 = new DataItem();
			width = (int)(Math.random()*data.length);//chongfu
			height = (int)(Math.random()*data[0].length);
			cen.group=i;
			cen.x=data[width][height].x;
			cen.y=data[width][height].y;
			cen.r=data[width][height].r;
			cen.g=data[width][height].g;
			cen.b=data[width][height].b;
			center[i]=cen;
			cen1.x=0;
			cen1.y=0;
			cen1.r=0;
			cen1.g=0;
			cen1.b=0;
			cen1.group=0;
			centerSum[i]=cen1;
			width=0;height=0;
			System.out.println("("+center[i].x+","+center[i].y+","+center[i].r+","+center[i].g+","+center[i].b+")");
		}
		System.out.println("-------------------");
	}
	
	public static DataItem [][] initData(int[][] data)
	{
		DataItem [][] dataitems = new DataItem[data.length][data[0].length];
		for (int i = 0; i < data.length; i++)
		{
			for (int j = 0; j < data[0].length; j++)
			{
				DataItem di = new DataItem();
				Color c = new Color(data[i][j]);
				int r = c.getRed();
				int g = c.getGreen();
				int b = c.getBlue();
				di.x=i;
				di.y=j;
				di.r=r;
				di.g=g;
				di.b=b;
				di.group=-1;
				dataitems[i][j]=di;
				//System.out.println(di.x);
			}
		}
		return dataitems;
	}
	
	public static int  getDis(DataItem k,DataItem d)
	{
		double distance = 0 ;
		distance=Math.sqrt(Math.pow((k.r-d.r), 2)+Math.pow((k.g-d.g), 2)+Math.pow((k.b-d.b), 2)
				+Math.pow((k.x-d.x), 2)+Math.pow((k.y-d.y), 2));
		//System.out.println((int)distance);
		return (int)distance;
	}
}
