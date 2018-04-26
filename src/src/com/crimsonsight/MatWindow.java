package src.com.crimsonsight;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Mat;

class Panel extends JPanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage image;

	// Create a constructor method
	public Panel() {
		super();
	}

	private BufferedImage getimage() {
		return image;
	}

	public void setimage(BufferedImage newimage) {
		image = newimage;
		return;
	}

	public void setimagewithMat(Mat newimage) {
		image = this.matToBufferedImage(newimage);
		return;
	}

	/**
	 * Converts/writes a Mat into a BufferedImage.
	 * 
	 * @param matrix
	 *            Mat of type CV_8UC3 or CV_8UC1
	 * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY
	 */
	public BufferedImage matToBufferedImage(Mat matrix) {
		int cols = matrix.cols();
		int rows = matrix.rows();
		int elemSize = (int) matrix.elemSize();
		byte[] data = new byte[cols * rows * elemSize];
		int type;
		matrix.get(0, 0, data);
		switch (matrix.channels()) {
		case 1:
			type = BufferedImage.TYPE_BYTE_GRAY;
			break;
		case 3:
			type = BufferedImage.TYPE_3BYTE_BGR;
			// bgr to rgb
			byte b;
			for (int i = 0; i < data.length; i = i + 3) {
				b = data[i];
				data[i] = data[i + 2];
				data[i + 2] = b;
			}
			break;
		default:
			return null;
		}
		BufferedImage image2 = new BufferedImage(cols, rows, type);
		image2.getRaster().setDataElements(0, 0, cols, rows, data);
		return image2;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// BufferedImage temp=new BufferedImage(640, 480,
		// BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage temp = getimage();
		// Graphics2D g2 = (Graphics2D)g;
		if (temp != null)
			g.drawImage(temp, 10, 10, temp.getWidth(), temp.getHeight(), this);
	}
}

public class MatWindow {

	Mat image;
	JFrame frame;
	Panel panel;
	JLabel fpsLabel;
	
	Date lastUpdateTime;

	public MatWindow(int xSize, int ySize, String name) {
		frame = new JFrame(name);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		frame.setSize(xSize, ySize);
		
		
		
		frame.setBounds(0, 20, frame.getWidth(), frame.getHeight());

		lastUpdateTime = new Date();
		panel = new Panel();
		frame.setContentPane(panel);
		frame.setVisible(true);
		
		fpsLabel = new JLabel("FPS: 0");
		frame.add(fpsLabel);
	}
	
	public MatWindow(String name) {
		this(0, 0, name);
	}

	public void setImage(Mat image) {
		this.image = image;
		updateImage();
	}

	public void updateImage() {
		Date currentTime = new Date();
		fpsLabel.setText("FPS: " + (int)(1.0/((currentTime.getTime() - lastUpdateTime.getTime())/1000.0)));
		
		lastUpdateTime = currentTime;
		
		frame.setSize(image.width() + 40, image.height() + 100);
		panel.setimagewithMat(image);
		panel.repaint();
		
		
	}

}


