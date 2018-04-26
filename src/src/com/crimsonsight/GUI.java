package src.com.crimsonsight;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class GUI extends JFrame {
	private JButton button;
	public GUI(){}
	public static void frame(){
		JFrame jFrame = new JFrame("Options");
		jFrame.setSize(400, 400);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setLayout(new FlowLayout());

		JPanel panel = new JPanel();

//		JSlider hueSlider = new JSlider(0, 255, 255); //178
//		panel.add(hueSlider);
//
//		JSlider satSlider = new JSlider(0, 255, 255);
//		panel.add(satSlider);
//
//		JSlider valSlider = new JSlider(10, 255, 255); //247
//		panel.add(valSlider);

		JSlider tolSlider = new JSlider(0, 255, 85); //136
		panel.add(tolSlider);

		jFrame.setContentPane(panel);//dd
		jFrame.setVisible(true);
	}
}
