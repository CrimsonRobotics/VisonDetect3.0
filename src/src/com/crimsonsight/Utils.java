package src.com.crimsonsight;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
//This class contains all the utilities used in the main class
public class Utils {
	//constants
	private static final double cameraAngle = 60.0;//happy
	private static final Scalar redText = new Scalar (0,500,500);
	private static final Scalar greenText = new Scalar (0,128,0);
	private static final int targetLength = 8;//inches
	private static final int targetHeight = 11;//inches
	private static int focalLength = 265;
	private static int distance = 59;
	
	//16.375
	//constructor
	public Utils(){}
	public static void pixelRatio(Rect rect) {
		Double pixels = rect.width/6.5; //16.375 is width of random piece of paper.
	//	Core.putText(img, text, org, fontFace, fontScale, color);
		System.out.println(pixels);
	}
	public static int calculateFocal(Rect rect){
		int focal = (rect.width*distance)/14;
	    //System.out.println("Focal Length");
		//System.out.println(focal);
		return focal;
	}
	public static int calculateDistance(Rect rect){
		int dis = (14*665)/rect.width;
		int tdis = dis + 1; 
		//System.out.println("Distance(inch.)");
		//System.out.println(tdis);
		return tdis;
	}
	public static Point calculateMidpoint(Rect mat){
		 Point center = new Point(
				(mat.tl().x + mat.br().x) / 2,
				(mat.tl().y + mat.br().y) / 2);
				 return center;
	}
	public static double calculateHeight(Rect in){
		double high = in.height/28.39;
		return high;
	}
	public static double calculateWidth(Rect in){
		double wid = in.width/32.4;
		return wid;
	}
	public static double getRestriction(double restriction){
		return restriction;
	}
	public static double calculateArea(Rect in){
		return(calculateHeight(in)*calculateWidth(in));
	}
	public static void putText(Mat mat,double size, int x, int y){
		Imgproc.putText(mat, String.valueOf(size),new Point(x,y), Core.FONT_HERSHEY_PLAIN,1,redText);
	}
	public static void setLabel(Mat mat, String string, int x, int y){
		Imgproc.putText(mat, string,new Point(x,y), Core.FONT_HERSHEY_PLAIN,1,redText);
	}
	public static void putCircle(Mat mat,int diameter, int thick, Rect rect){
		Imgproc.circle(mat,calculateMidpoint(rect), diameter, greenText,-1);
	}
	public static void putline(Mat mat){
		Imgproc.line(mat,new Point(mat.width()/2,0), new Point(mat.width()/2,mat.height()) , redText );
	}
	public static void putrect(Mat mat, Rect rec){
		Imgproc.rectangle(mat, rec.tl(), rec.br(),redText );
		}
	public static String res(Mat mat){
		String resolution = "width " + mat.width() + " height " + mat.height();
		return resolution;
	}
	public static void setRes(Mat mat){
		Imgproc.putText(mat, res(mat), new Point(0,10), Core.FONT_HERSHEY_PLAIN, 1, new Scalar(0, 500, 500));
	}
	public static void detectSide(Mat mat,Rect rect){
		if(calculateMidpoint(rect).x==mat.width()/2 && calculateMidpoint(rect).x<=mat.width()/2 +100&& calculateMidpoint(rect).x>=mat.width()/2 - 100){
			//System.out.println("Your thing is on the line");
		}
		else if(calculateMidpoint(rect).x< mat.width()/2){
			//System.out.println("Your thing is on the right of the line");
		}
		else{
			//System.out.println("Your thing is on the left side");
		}
	}
	public static double calculateAngleOfBoundingBox(Mat orig, Rect rect){
		Point midline = new Point((orig.width()/2), 0);
		Point midpointOfObject = new Point(calculateMidpoint(rect).x, 0);
		double distance = distance(midpointOfObject, midline);
		double angle = ((cameraAngle/2)*distance)/(orig.width()/2);
		// debug stuff System.out.println(midline.toString()+ midpointOfObject.toString());
		return angle;
			}
	public static double distance(Point obj, Point mid)
	{
		return obj.x - mid.x;
	}
	public static void setangle(Mat mat, Rect rect, int x, int y){
		Imgproc.putText(mat, String.valueOf(calculateAngleOfBoundingBox(mat,rect)),new Point(x,y), Core.FONT_HERSHEY_PLAIN,1,redText);
	}
	
	
}
