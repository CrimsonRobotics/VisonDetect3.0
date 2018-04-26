package src.com.crimsonsight;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.videoio.*;


import org.opencv.imgproc.Imgproc;

import src.com.crimsonsight.GUI;
import src.com.crimsonsight.MatWindow;
import src.com.crimsonsight.Utils;
public class Main {

	public static void main(String[] args) throws InterruptedException {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat original = new Mat();
		MatWindow window = new MatWindow("HEllo");
		MatWindow threshWindow = new MatWindow("Thresh");
	
		VideoCapture camera = new VideoCapture(0);

		JFrame jFrame = new JFrame("Option");
		jFrame.setSize(400, 400);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setLayout(new FlowLayout());
		
		JPanel panel = new JPanel();

//		JSlider hueSlider = new JSlider(0, 255, 178);
//		panel.add(hueSlider);
//
//		JSlider satSlider = new JSlider(0, 255, 255);
//		panel.add(satSlider);
//
//		JSlider valSlider = new JSlider(10, 255, 247);
//		panel.add(valSlider);
		
		JSlider hueTolSlider = new JSlider(0, 255, 27);
			panel.add(hueTolSlider);
			JLabel hueTolLabel = new JLabel(String.valueOf("Hue Tolerance: "+hueTolSlider.getValue()));
				hueTolLabel.setFont(new Font("Noto Sans",0 ,15));
				panel.add(hueTolLabel);
		
		JSlider satTolSlider = new JSlider(0, 255, 0);
			panel.add(satTolSlider);
			JLabel satTolLabel = new JLabel(String.valueOf("Minimum Saturation: "+satTolSlider.getValue()));
				satTolLabel.setFont(new Font("Noto Sans",0 ,15));
				panel.add( satTolLabel);
				
		JSlider valTolSlider = new JSlider(0, 255, 255);
			panel.add(valTolSlider);
			JLabel valTolLabel = new JLabel(String.valueOf("Minimum Value: "+valTolSlider.getValue()));
				valTolLabel.setFont(new Font("Noto Sans",0 , 15));
				panel.add(valTolLabel);
			
		JButton resetButton = new JButton("Reset Values");
			resetButton.setFont(new Font("Noto Sans",0 ,15));
			panel.add(resetButton);
				resetButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e)
					{
						hueTolSlider.setValue(52);
						satTolSlider.setValue(50);
						valTolSlider.setValue(255
								);
					}
					});
			
		jFrame.setContentPane(panel);
		jFrame.setVisible(true);
		GUI gui = new GUI();
		
		while (true) {

			if (!camera.read(original))
				continue;
			
			Mat threshImage = new Mat();

			Imgproc.cvtColor(original, threshImage, Imgproc.COLOR_RGB2HSV);
			
			Utils.setRes(original);//Labels the camera resolution
			Utils.setLabel(original, "Height(in.)", 10, 185);//sets the height label
			Utils.setLabel(original, "Width(in.)", 10, 235);//sets the width label
			Utils.setLabel(original, "Area(in.)", 10, 285);//sets the Area label
			Utils.setLabel(original, "Angle(relative to width)", 10, 335);//sets the angle label /*will be a little off due to the constantly moving bounding box, but not enough to throw off positioning
			
//			int hue = hueSlider.getValue();
//			int satu = satSlider.getValue();
//			int valu = valSlider.getValue();

				hueTolLabel.setText("Hue Tolerance: "+hueTolSlider.getValue());
				satTolLabel.setText("Minimum Saturation: "+satTolSlider.getValue());
				valTolLabel.setText("Minimum Value: "+valTolSlider.getValue());
				
			Core.inRange(
				threshImage,
				new Scalar(55-hueTolSlider.getValue(),satTolSlider.getValue(),valTolSlider.getValue()),
				new Scalar(55+hueTolSlider.getValue(),255,255),//55
				threshImage);
				/*Core.inRange(
						threshImage,
						new Scalar(110,50,50),
						new Scalar(130,255,255),
						threshImage);*/

			//Imgproc.GaussianBlur(original, original,new Size(5,5), 4.5,4.5 );
			//Imgproc.GaussianBlur(threshImage, threshImage,new Size(5,5), 4.5,4.5 );
			threshWindow.setImage(threshImage);
			
//			Core.inRange(
//					threshImage, 
//					new Scalar(55-tol,80,100),
//					new Scalar(55+tol,255,255),
//					threshImage
//					);
//			threshWindow.setImage(threshImage);

			ArrayList<MatOfPoint> particles = new ArrayList<MatOfPoint>();
			ArrayList<String> d = new ArrayList<String>();
			List<MatOfPoint> particleRect = new ArrayList<MatOfPoint>();
			
			
			Imgproc.findContours(threshImage, particles, new Mat(),
					Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
			//Utils.printArray(particles, "Initial");
			
			Rect biggest = null;
			Rect comparison = null;
			int biggestArea = 4;
			int biggestWidth = 10;
			for (int i = particles.size()-1; i > 0; i--) {
				MatOfPoint contour = particles.get(i);
				comparison = Imgproc.boundingRect(contour);
				int area = (int) Utils.calculateArea(comparison);
				int width = comparison.width;
			   //System.out.println(area);
				if (area > biggestArea && width > biggestWidth) {
					//yellowTotes.remove(i);
					//smallest = Imgproc.boundingRect(contour);
					biggest = Imgproc.boundingRect(contour);
					biggestArea = area;
					//particleWidth.add(contour);
				} else {
					particles.remove(i);
				}
				
			}
//			for(int x =particleWidth.size()-1; x>0; x-- ){
//					MatOfPoint cont = particleWidth.get(x);
//					if(cont.width() > biggestWidth){
//							biggestWidth = cont.width();
//							index = x;
//			}
//				biggest = Imgproc.boundingRect(particleWidth.get(index));	
			
			if (biggest != null) {
				//Utils.pixelRatio(biggest);
				
				System.out.println(Utils.calculateAngleOfBoundingBox(original, biggest));
				Imgproc.line(original,new Point(original.width()/2,0), new Point(original.width()/2,original.height()) , new Scalar(0,500,500));
				Point center = Utils.calculateMidpoint(biggest);//sets center point see Utils for more info
				Double height = Utils.calculateHeight(biggest);//sets height see Utils for more info
				Double width = Utils.calculateWidth(biggest);//sets width see Utils for more info
				Double area = Utils.calculateArea(biggest);//sets area. see Utils class to see how
	//			Utils.putrect(original, comparison);//places rectagle
				Utils.putrect(original, biggest);//places rectangle(bounding box). see Utils class for more
				Utils.putText(original, height, 10, 200);//placed height. see Utils class for more
				Utils.putText(original, width, 10, 250);//placed width. see Utils class for more
				Utils.putText(original, area, 10, 300);//placed area. see Utils class for more
				Utils.putCircle(original,5, -1, biggest);//places circle in the center of bounding box.see Utils class for more
				Utils.putline(original);//places mid-line. see Utils class for more
				Utils.detectSide(original, biggest);//detects the side of the bounding box. see Utils class for more
				Utils.setangle(original, biggest, 10, 350);//sets the angle see Utils class for more
				Utils.calculateFocal(biggest);
				Utils.calculateDistance(biggest);
				//Utils.putAngle(original, biggest);
		
				//System.out.println();
			}
						// Update the image on the window
			window.setImage(original);
			

		}
	}
	
	
	
	}


	
	