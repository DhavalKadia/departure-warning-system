//Output videos: https://www.youtube.com/DhavalKadia

package pool.tests;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import pool.utils.CVLoader;
import pool.utils.ImgWindow;

public class WYSpace3ChangeCTest2 {
	

	static int H = 480,W = 853, cH = 850;

	static boolean laneW = false, laneY = false;	//V5
	
	static double maxMPosWY, maxMNegWY, turnAngleWY, maxCPosWY = 0, maxCNegWY = 0;	//V7
	static int interXWY, interYWY;		//V7
	
	
	static int LW = 120, indivW = 120;//T1
	static int lowThresholdW = 80;
	static int cFactorW = 10;
	static int minThetaW = 10;
	
	static float dataW[][] = new float[cH][180];
	static float dummyW[][];// = new float[H - 50][180];
	
	static double factorW = 1;
	static double maxLW=0, maxCW=-1, maxTW = 0;
	static double maxCPosW, maxTPosW, maxLPosW = 0, maxCNegW, maxTNegW, maxLNegW = 0, maxMPosW, maxMNegW ;
	static int interXW, interYW;
	static double turnAngleW;
	
	static int cHReducedW = cH / cFactorW;
	
	
	static int LY = 5, indivY = 5;
	static int lowThresholdY = 80;
	static int cFactorY = 10;
	static int minThetaY = 10;
	
	static float dataY[][] = new float[cH][180];
	static float dummyY[][];// = new float[H - 50][180];
	
	static double factorY = 1;
	static double maxLY=0, maxCY=-1, maxTY = 0;
	static double maxCPosY, maxTPosY=0, maxLPosY = 0, maxCNegY, maxTNegY=0, maxLNegY = 0, maxMPosY, maxMNegY ;//Y1
	static int interXY, interYY;
	static double turnAngleY;
	
	static int cHReducedY = cH / cFactorY;
	
	
	public static int getC(int x1, int y1, int x2, int y2)
	{
		int xt1 = x1 - W/2, xt2 = x2 - W/2, yt1 = H - y1, yt2 = H - y2;
		float m;
		
		if(xt2==xt1)
			m=99999;
		else
			m = (float)(yt2 - yt1)/(float)(xt2 - xt1);
		
		//System.out.println(" "+xt1+" "+yt1+" "+xt2+" "+yt2+" : "+m);
		
		return (int)(yt1 - m*xt1);
	}
	
	public static double getTheta(int x1, int y1, int x2, int y2)
	{	
		return Math.toDegrees(Math.atan((float)(y2 - y1)/(float)(x2 - x1)));
	}
	
	public static void putLineW(int x1, int y1, int x2, int y2)
	{
		int xt1 = x1 - W/2, xt2 = x2 - W/2, yt1 = H - y1, yt2 = H - y2;
		int theta = (int)getTheta(xt1, yt1, xt2, yt2);
		int c = getC(x1, y1, x2, y2);
		int length = (int)Math.sqrt(Math.pow(xt2-xt1, 2) + Math.pow(yt2-yt1, 2));
		float curr;
		
		if(c > 0 && c < cH)
		{
			curr = dummyW[c / cFactorW][90 - theta] += length*factorW;	
			
			if(maxLW<curr)
			{
					maxLW = curr;
					maxCW = c;
					maxTW = theta;
			}
			
			if(theta > 0)
			{
				if(maxLPosW<curr)
				{
						maxLPosW = curr;
						maxCPosW = c;
						maxTPosW = theta;
				}
			}
			
			if(theta < 0)
			{
				if(maxLNegW<curr)
				{
						maxLNegW = curr;
						maxCNegW = c;
						maxTNegW = theta;
				}
			}
			
			//System.out.println("("+xt1+", "+yt1+")"+" -> ("+xt2+", "+yt2+")");
			//System.out.println("c="+c+" theta="+theta+" len =" + length*factor);
		}
	}
	
	public static int length(int x1, int y1, int x2, int y2)
	{
		return (int)Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
	}
		
		public static void putLineY(int x1, int y1, int x2, int y2)
		{
			int xt1 = x1 - W/2, xt2 = x2 - W/2, yt1 = H - y1, yt2 = H - y2;
			int theta = (int)getTheta(xt1, yt1, xt2, yt2);
			int c = getC(x1, y1, x2, y2);
			int length = (int)Math.sqrt(Math.pow(xt2-xt1, 2) + Math.pow(yt2-yt1, 2));
			float curr;
			
			if(c > 0 && c < cH)
			{
				curr = dummyY[c / cFactorY][90 - theta] += length*factorY;	
				
				if(maxLY<curr)
				{
						maxLY = length;	//WYCCT2
						maxCY = c;
						maxTY = theta;
				}
				
				if(theta > 0)
				{//System.out.println("Y = "+theta);
					if(maxTPosY < theta)//S1//maxLPosY<curr && 
					{
							maxLPosY = length;	//WYCCT2
							maxCPosY = c;
							maxTPosY = theta;
					}
				}
				
				if(theta < 0)
				{//System.out.println("Y = "+theta);
					if(maxTNegY > theta)//S1//maxLNegY<curr && 
					{
							maxLNegY = length;	//WYCCT2
							maxCNegY = c;
							maxTNegY = theta;
							
							//System.out.println("Y = "+theta);
					}
				}
				
				//System.out.println("("+xt1+", "+yt1+")"+" -> ("+xt2+", "+yt2+")");
				//System.out.println("c="+c+" theta="+theta+" len =" + length*factor);
			}
	}
	
	
	/////////////////////////////////////////////////
	
	public static float[] HSV(int[] rgb)
	{
		float hsv[] = new float[] {-33, -33, -33};
		
		float _r = ((float)rgb[2] / 255);
		float _g = ((float)rgb[1] / 255);
		float _b = ((float)rgb[0] / 255);
		
		float cMin = Math.min(Math.min(_r, _g), _b);
		float cMax = Math.max(Math.max(_r, _g), _b);
		
		//System.out.println("cMin = "+cMin+"cMax = "+cMax);
		
		float delta = (cMax - cMin);
		
		if(cMax != 0)
		{
			hsv[1] = 100 * (delta / cMax);
		}
		else
		{
			hsv[0] = -1;
			hsv[1] = 0;
		}
		
		if(cMax == _r)
		{
			hsv[0] = (60 * (((_g - _b) / delta) % 6));
		}
		else if(cMax == _g)
		{
			hsv[0] = (60 * (((_b - _r) / delta) + 2));
		}
		else if(cMax == _b)
		{
			hsv[0] = (60 * (((_r - _g) / delta) + 4));
		}
		
		if(hsv[0] < 0)
			hsv[0] += 360;
		
		hsv[2] = 100 * cMax;
		
		//if(hsv[1] > 0 && hsv[1] < 20)
		//System.out.println(",,,"+hsv[0]);
		
		return hsv;
	}

	
	public static void main (String[] args) throws InterruptedException 
	{
		CVLoader.load();
		
		VideoCapture video = new VideoCapture();//"C:/Users/dhaval/Videos/Captures/Resolution/v10.mp4");
		video.open("C:/Users/dhaval/Videos/Captures/Resolution/v31.mp4");
		
		//ImgWindow windowY = ImgWindow.newWindow();
		//ImgWindow windowCTY = ImgWindow.newWindow();
		
		//ImgWindow windowW = ImgWindow.newWindow();	
		//ImgWindow windowCTW = ImgWindow.newWindow();		
		
		ImgWindow window  = ImgWindow.newWindow();
		
		Mat img = Highgui.imread("C:/Users/dhaval/Videos/Captures/Resolution/gta9.png");//cx.jpg
		
	
		Mat white = new Mat(480, W, CvType.CV_8UC3);
		Mat yellow = new Mat(480, W, CvType.CV_8UC3);
	

		//Mat CTW = new Mat(cH, 180, 1);
		//Mat CTY = new Mat(cH, 180, 1);
		
		Mat grayW = new Mat();
		
		Mat grayY = new Mat();
		
		if (video.isOpened())
		{			
			while (video.grab()) 	              
			{
				video.retrieve(img);
				
				maxLW=0; maxCW=-1; maxTW = 0;
				maxLPosW = maxLNegW = 0;
				dummyW =  new float[cHReducedW][180];
				
				maxLY=0; maxCY=-1; maxTY = 0;
				maxLPosY = maxLNegY = 0;
				dummyY =  new float[cHReducedY][180];
				
				//Thread.sleep(200);
				
				//video.read(img);
				
				maxLY=0; maxCY=-1; 
				maxTY = 0;
				maxCPosY= maxTPosY= maxLPosY= maxCNegY= maxTNegY= maxLNegY = maxMPosY= maxMNegY=0;
				
				////////////.................
				float[] h ;
				byte[] data = new byte[] {(byte) 155, (byte) 161, (byte) 131};
				/*img.get(81, 321, data);
				
				int[] ii = new int[3];
				
				ii[0] = data[0] & 0xFF;
				//data[0] = (byte) i[0];
				
				ii[1] = data[1] & 0xFF;
				//data[1] = (byte) i[1];
				
				ii[2] = data[2] & 0xFF;
				//data[2] = (byte) i[2];
				
				//System.out.println;
				
				h = HSV(ii);
				System.out.println(ii[0]+","+ii[1]+","+ii[2]+" -> "+(int)h[0]+","+(int)h[1]+","+(int)h[2]);
				*/
				////////////.................
				
				for(int i = 0; i < img.rows(); i++)
					for(int j = 0; j < img.cols(); j++)
					{
						white.put(i, j, new byte[]{(byte) 0, (byte) 0, (byte) 0});
						yellow.put(i, j, new byte[]{(byte) 0, (byte) 0, (byte) 0});
					}
				
				
				
				//if (!img.empty()) 
				{
					
					int[] in = new int[3];
					byte[] out = new byte[3];
					
					for(int i = 0; i < img.rows(); i++)
						for(int j = 0; j < img.cols(); j++)
						{
							img.get(i, j, data);
				
							in[0] = data[0] & 0xFF;
							data[0] = (byte) in[0];
				
							in[1] = data[1] & 0xFF;
							data[1] = (byte) in[1];
			
							in[2] = data[2] & 0xFF;
							data[2] = (byte) in[2];
				
							h = HSV(in);
				
							//if(h[0] > 10 && h[0] < 120 && h[2] > 90)			
							//if(h[0] > 10 && h[0] < 120 && h[2] > 70)
							//if(h[0] > 40 && h[0] < 125 && h[2] > 70)//wnd place
							//T1if(h[0] > 40 && h[0] < 70 && h[2] > 70 && h[2] <90)//test1
							//T1if(h[0] > 40 && h[0] < 70 && h[1] > 30 )//test2best
							if(h[0] > 44 && h[0] < 60 && h[1] > 30 )//T1
							{
								yellow.put(i, j, new byte[]{(byte) 255, (byte) 255, (byte) 255});
							}
				
							//if(h[1] < 20 && h[2] > 80 && !(h[0] > 20 && h[0] < 120 && h[2] > 90))
							//if(h[1] < 8 && h[2] > 90 && !(h[0] > 40 && h[0] < 125 && h[2] > 70))//end place
							if(h[1] < 18 && h[2] > 95 )//&& !(h[0] > 40 && h[0] < 115 && h[2] > 70))
							{
								white.put(i, j, new byte[]{(byte) 255, (byte) 255, (byte) 255});
							}
						}
	
		
					/////////////////////////////////WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
		
		
		
					//Imgproc.cvtColor(white, grayW, Imgproc.COLOR_BGR2GRAY);
					Imgproc.blur(white, white, new Size(6, 6));
		
		
					Mat edgesW = new Mat();
		
					////////////////////
		
		
		
					//Imgproc.cvtColor(yellow, grayY, Imgproc.COLOR_BGR2GRAY);
					Imgproc.blur(yellow, yellow, new Size(6, 6));
		
					Mat edgesY = new Mat();
	
		
					int lowThreshold = 20;	
	
					int ratio = 3;
					Imgproc.Canny(yellow, edgesY, lowThreshold, lowThreshold * ratio);
		
					Mat linesY = new Mat();
					Imgproc.HoughLinesP(edgesY, linesY, 1, Math.PI / 180, 50, 50, 10);
		
		
					maxLW=0; maxCW=-1; maxTW = 0;
					maxCPosW= maxTPosW= maxLPosW= maxCNegW= maxTNegW= maxLNegW = maxMPosW= maxMNegW=0;

					int currL;
					int currT;
					
					for(int i = 0; i < linesY.cols(); i++) 
					{
						double[] val = linesY.get(0, i);
						
						currL = length((int)val[0], (int)val[1], (int)val[2], (int)val[3]);
						currT = (int) getTheta((int)val[0], (int)val[1], (int)val[2], (int)val[3]);
						//System.out.println("("+val[0]+", "+val[1]+")"+" -> ("+val[2]+", "+val[3]+") : "+currL + ", "+currT);
						
						if( currL > 40 && currL <120 && Math.abs(currT) > 40)// && currL <20)
						{
							//Core.line(img, new Point(val[0], val[1]), new Point(val[2], val[3]), new Scalar(255, 255, 15), 2);
							//System.out.println("("+val[0]+", "+val[1]+")"+" -> ("+val[2]+", "+val[3]+") : "+currL);
			
							putLineY((int)val[0], (int)val[1], (int)val[2], (int)val[3]);
						}
					}
		
		/////////////////////////////////YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY
		
					lowThreshold = 160;	
		
					Imgproc.Canny(white, edgesW, lowThreshold, lowThreshold * ratio);
		
					Mat linesW = new Mat();
					Imgproc.HoughLinesP(edgesW, linesW, 1, Math.PI / 180, 50, 50, 10);
		
					//maxLY=0; maxCY=-1; 
					//maxTY = 0;
					//maxCPosY= maxTPosY= maxLPosY= maxCNegY= maxTNegY= maxLNegY = maxMPosY= maxMNegY=0;
		
					for(int i = 0; i < linesW.cols(); i++) 
					{
						double[] val = linesW.get(0, i);
						
						currL = length((int)val[0], (int)val[1], (int)val[2], (int)val[3]);
						currT = (int) getTheta((int)val[0], (int)val[1], (int)val[2], (int)val[3]);
						//System.out.println("("+val[0]+", "+val[1]+")"+" -> ("+val[2]+", "+val[3]+") : "+currL + ", "+currT);
						
						if(currL > indivW)
						{
							//Core.line(img, new Point(val[0], val[1]), new Point(val[2], val[3]), new Scalar(100, 255, 90), 2);
							//System.out.println("("+val[0]+", "+val[1]+")"+" -> ("+val[2]+", "+val[3]+") : "+getC((int)val[0], (int)val[1], (int)val[2], (int)val[3]));
			
							putLineW((int)val[0], (int)val[1], (int)val[2], (int)val[3]);
						}
					}
					/////////////////////////////////*******************************************
		
					//Core.line(img, new Point(W/2, 0), new Point(W/2, H), new Scalar(255, 0, 0), 1);
		
					
				
		
					//for(int row=0;row<cH;row++)		
					//	for(int col=0;col<180;col++)
					//	{
					//		CTW.put(row, col, 0);
						//	CTY.put(row, col, 0);
						//}
			
					double m=0,c=0,x1=0, y1=0, x2=0, y2=0;	//V5
	
					
					laneW = false;
					/*
					for(int row=0;row<cHReducedW;row++)
					{
						CTW.put(cH-1-row*cFactorW, 90, 255);
			
						for(int col=0;col<180;col++)
						{				
							if(row == H)
								CTW.put(cH-1-H, col, 255);
				
							float theta = 90 - col;
				
							if(dummyW[row][col] > LW && Math.abs(theta) > minThetaW)
							{
								laneW = true;
								maxTW = theta;	//V6
								//maxCW = row;	//V7	//V8
								
								CTW.put(cH-1-row*cFactorW, col, 255);
					
								m = Math.tan(Math.toRadians(theta));
								maxCW = c = row * cFactorW;		//V8
					
								x1 = (H-c)/m;
								y1 = H;
					
								//if(Math.abs(x1) < W/2)
								//{
					
								if(theta > 0)
								{
									y2 = m*(-W/2) + c;
									
									if(y2>0)
									{
										x2 = -W/2;						
									}					
									else
									{
										y2 = 0;
										x2 = -c/m;
									}
								}
								else
								{
									y2 = m*(W/2) + c;
						
									if(y2>0)
									{
										x2 = W/2;						
									}					
									else
									{
										y2 = 0;
										x2 = -c/m;
									}
								}
					
								//	System.out.println("c="+c+" theta="+theta+" m="+m);
								//	System.out.println("***("+(int)x1+", "+(int)y1+")"+" -> ("+(int)x2+", "+(int)y2+") ");
					
								//Core.line(img, new Point(x1+W/2, H-y1), new Point(x2+W/2, H-y2), new Scalar(0, 255, 0), 2);
								//}
							}
						}			        
					}
					
					if(laneW)
						Core.line(img, new Point(x1+W/2, H-y1), new Point(x2+W/2, H-y2), new Scalar(255, 0, 0), 2);
					*/
					/*
					if(maxLNegW >= LW || maxLPosW >= LW)// && maxC > 180)//&& (Math.abs(maxTNeg)-Math.abs(maxTPos))<20)
					{
		
						maxMPosW = Math.tan(Math.toRadians(maxTPosW));
						maxMNegW = Math.tan(Math.toRadians(maxTNegW));
		
						interXW = (int) (-(maxCPosW - maxCNegW)/(maxMPosW - maxMNegW));
						interYW = (int)(maxMPosW*interXW + maxCPosW);
		
						//System.out.println("Intersection is "+interX+", "+interY);
		
						if(Math.abs(interXW) < W/2)
						{
							Core.line(img, new Point(W/2, H), new Point(interXW+W/2, H-interYW), new Scalar(255, 0, 0), 4);
		
							turnAngleW = Math.toDegrees(Math.atan((double)interXW/(double)interYW));
		
		
							Core.putText(img, ""+(int)turnAngleW, new Point(interXW+W/2, 440-interYW), 1, 2,  new Scalar(255, 0, 0), 4);
							
						}
					}
					
					///////////////////WWWWWWWWWWWWWWWWWWWWWWWW
		
					*/
					
					
		
					///////////////////YYYYYYYYYYYYYYYYYYYYYYYY	   
					
					laneY = false;
					/*
					for(int row=0;row<cHReducedY;row++)
					{
						//double m=0,c=0,x1=0, y1=0, x2=0, y2=0;
	
						CTY.put(cH-1-row*cFactorY, 90, 255);
	
						for(int col=0;col<180;col++)
						{				
							if(row == H)
								CTY.put(cH-1-H, col, 255);
		
							float theta = 90 - col;
		
							if(dummyY[row][col] > LY && Math.abs(theta) > minThetaY)
							{
								laneY = true;
								maxTY = theta;	//V6
								//maxCY = row;	//V7	//V8
								
								CTY.put(cH-1-row*cFactorY, col, 255);
		
								m = Math.tan(Math.toRadians(theta));
								maxCY = c = row * cFactorY;		//V8
		
								x1 = (H-c)/m;
								y1 = H;
			
								//if(Math.abs(x1) < W/2)
								//{
									
								if(theta > 0)
								{
									y2 = m*(-W/2) + c;
										
									if(y2>0)
									{
										x2 = -W/2;						
									}					
									else
									{
										y2 = 0;
										x2 = -c/m;
									}
								}
								else
								{
									y2 = m*(W/2) + c;
				
									if(y2>0)
									{
										x2 = W/2;						
									}					
									else
									{
										y2 = 0;
										x2 = -c/m;
									}
								}
				
								//	System.out.println("c="+c+" theta="+theta+" m="+m);
								//	System.out.println("***("+(int)x1+", "+(int)y1+")"+" -> ("+(int)x2+", "+(int)y2+") ");
					 
								//Core.line(img, new Point(x1+W/2, H-y1), new Point(x2+W/2, H-y2), new Scalar(0, 160,13), 2);
								//}
							}
						}			        
					}
					
					if(laneY)
						Core.line(img, new Point(x1+W/2, H-y1), new Point(x2+W/2, H-y2), new Scalar(0, 160,13), 2);					
					*/
					/*
					if(maxLNegY >= LY || maxLPosY >= LY)// && maxC > 180)//&& (Math.abs(maxTNeg)-Math.abs(maxTPos))<20)
					{

						maxMPosY = Math.tan(Math.toRadians(maxTPosY));
						maxMNegY = Math.tan(Math.toRadians(maxTNegY));
			
						interXY = (int) (-(maxCPosY - maxCNegY)/(maxMPosY - maxMNegY));
						interYY = (int)(maxMPosY*interXY + maxCPosY);

						//System.out.println("Intersection is "+interX+", "+interY);

						if(Math.abs(interXY) < W/2)
						{
							Core.line(img, new Point(W/2, H), new Point(interXY+W/2, H-interYY), new Scalar(255, 0, 0), 4);
							
							turnAngleY = Math.toDegrees(Math.atan((double)interXY/(double)interYY));


							Core.putText(img, ""+(int)turnAngleY, new Point(interXY+W/2, 440-interYY), 1, 2,  new Scalar(255, 0, 0), 4);

						}
					}
					*/
					///////////////////YYYYYYYYYYYYYYYYYYYYYYYYYYYY
					
			//		*/
					//System.out.println("maxTW ="+maxTW+" maxTY ="+maxTY+" laneW ="+laneW+" laneY ="+laneY);
					//System.out.println("MAXY is "+maxLPosY+", "+maxLNegY);
					String lane = null;
					if((maxLPosW >= LW && maxTY < 0) || (maxLNegW >= LW && maxTY > 0))
					{		
						//System.out.println("Prob"+maxTY);
						
						if(maxTY < 0)
						{							
							lane = "Left";
							
							maxMPosWY = Math.tan(Math.toRadians(maxTPosW));	
							maxMNegWY = Math.tan(Math.toRadians(maxTNegY));	
							
							maxCPosWY = maxCPosW;	
							maxCNegWY = maxCNegY;	
						}
						else if(maxTY > 0)
						{							
							lane = "Right";
							
							maxMPosWY = Math.tan(Math.toRadians(maxTPosY));	
							maxMNegWY = Math.tan(Math.toRadians(maxTNegW));	
							
							maxCPosWY = maxCPosY;	
							maxCNegWY = maxCNegW;
						}
						
						
						
						
						interXWY = (int) (-(maxCPosWY - maxCNegWY)/(maxMPosWY - maxMNegWY));
						interYWY = (int)(maxMPosWY*interXWY + maxCPosWY);

						//System.out.println("Intersection is "+interXWY+", "+interYWY);
						turnAngleWY = Math.toDegrees(Math.atan((double)interXWY/(double)interYWY));
						
						
						if(Math.abs(interXWY) < W/2 && Math.abs(turnAngleWY) < 20)
						{
							
						
							

							if(Math.abs(turnAngleWY) < 2)
							{
								Core.putText(img, "Straight", new Point(350, 120), 1, 2,  new Scalar(0, 255, 0), 4);
								
							}
							else if(lane == "Left")
							{
								Core.putText(img, "Left Side", new Point(333, 470), 1, 2,  new Scalar(145, 0, 255), 4);	
								
								if(turnAngleWY < 3)
									Core.putText(img, "Turn "+(int)Math.abs(turnAngleWY)+" Degree Left", new Point(interXWY+W/2 + 20, 440 - interYWY + 30), 1, 2,  new Scalar(145, 0, 255), 4);
								else
									Core.putText(img, "Turn "+(int)Math.abs(turnAngleWY)+" Degree Right", new Point(interXWY+W/2 + 20, 440 - interYWY + 30), 1, 2,  new Scalar(145, 0, 255), 4);
								
								//Core.line(img, new Point(W/2, H), new Point(interXWY+W/2, H-interYWY), new Scalar(145, 0, 255), 4);
								drawLane(img, maxTPosW, maxCPosW, maxTNegY, maxCNegY, interXWY, interYWY, lane);
							}
							else if(lane == "Right")
							{
								Core.putText(img, "Right Side", new Point(333, 470), 1, 2,  new Scalar(170, 255,0), 4);
								
								if(turnAngleWY < 0)
									Core.putText(img, "Turn "+(int)Math.abs(turnAngleWY)+" Degree Left", new Point(interXWY+W/2 + 20, 440 - interYWY + 30), 1, 2,  new Scalar(175, 222, 2), 4);
								else
									Core.putText(img, "Turn "+(int)Math.abs(turnAngleWY)+" Degree Right", new Point(interXWY+W/2 + 20, 440 - interYWY + 30), 1, 2,  new Scalar(175, 222, 2), 4);
									
								//Core.line(img, new Point(W/2, H), new Point(interXWY+W/2, H-interYWY), new Scalar(175, 222, 2), 4);
								drawLane(img, maxTPosY, maxCPosY, maxTNegW, maxCNegW, interXWY, interYWY, lane);
							}
							
							//System.out.println(maxTY+", "+maxCY);
					//		System.out.println("interX = "+interXWY+" interY = "+interYWY);
					//		System.out.println("CNeg = "+maxCNegWY+" CPos = "+maxCPosWY);
					//		System.out.println("MNeg = "+maxMNegWY+" MPos = "+maxMPosWY);
						}
						
						m = Math.tan(Math.toRadians(maxTPosW));
						c = maxCPosW;
						//maxCPosY = c = row * cFactorY;		//V8
						
						x1 = (H-c)/m;
						y1 = H;
	
						//if(Math.abs(x1) < W/2)
						//{
							
						if(maxTPosW > 0)
						{
							y2 = m*(-W/2) + c;
								
							if(y2>0)
							{
								x2 = -W/2;						
							}					
							else
							{
								y2 = 0;
								x2 = -c/m;
							}
						}
						else
						{
							y2 = m*(W/2) + c;
		
							if(y2>0)
							{
								x2 = W/2;						
							}					
							else
							{
								y2 = 0;
								x2 = -c/m;
							}
						}
						//Core.line(img, new Point(x1+W/2, H-y1), new Point(x2+W/2, H-y2), new Scalar(0, 160,13), 2);
						
						/////////////////////////////////////////////////////////////
						
						m = Math.tan(Math.toRadians(maxTNegW));
						c = maxCNegW;
						
						x1 = (H-c)/m;
						y1 = H;
	
						//if(Math.abs(x1) < W/2)
						//{
							
						if(maxTNegW > 0)
						{
							y2 = m*(-W/2) + c;
								
							if(y2>0)
							{
								x2 = -W/2;						
							}					
							else
							{
								y2 = 0;
								x2 = -c/m;
							}
						}
						else
						{
							y2 = m*(W/2) + c;
		
							if(y2>0)
							{
								x2 = W/2;						
							}					
							else
							{
								y2 = 0;
								x2 = -c/m;
							}
						}
						
						//Core.line(img, new Point(x1+W/2, H-y1), new Point(x2+W/2, H-y2), new Scalar(0, 160,13), 2);
					}
		
					//windowW.setImage(white);
					//windowY.setImage(yellow);
					//windowGrayWhite.setImage(grayW);
					//windowGrayYellow.setImage(grayY);
					//windowCTW.setImage(CTW);
					//windowCTY.setImage(CTY);
		
					window.setImage(img);
				}
			}
		}// END WHILE
	}
	
	public static void drawLane(Mat img, double leftT, double leftC, double rightT, double rightC, int interX, int interY, String LR)
	{
		double laneLT = 0, laneLC = 0, laneRT = 0, laneRC = 0, mL, mR;
		int lX, Y, rX;
		
		if(LR == "Left")
		{
			laneLT = leftT;
			laneLC = leftC;
			
			laneRT = rightT;
			laneRC = rightC;
		}
		else if(LR == "Right")
		{
			laneLT = leftT;
			laneLC = leftC;
			
			laneRT = rightT;
			laneRC = rightC;
		}
		
		mL = Math.tan(Math.toRadians(laneLT));
		mR = Math.tan(Math.toRadians(laneRT));
		
		for(Y = 0; Y < interY; Y += 10)
		{
			lX = (int) ((Y - laneLC) / mL);
			
			rX = (int) ((Y - laneRC) / mR);
			
			if(LR == "Left")				
				Core.line(img, new Point(lX+W/2, H-Y), new Point(rX+W/2, H-Y), new Scalar(145, 0, 255), 1);
			else
				Core.line(img, new Point(lX+W/2, H-Y), new Point(rX+W/2, H-Y), new Scalar(170, 255,0), 1);
		}
	}
}
