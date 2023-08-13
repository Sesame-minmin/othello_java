package example;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * JPanelのサブクラスとしてアプリを作成した場合
 */
public class MainPane extends JPanel {

	public static JFrame createFrame(String title, JPanel panel) {
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(panel);
		panel.setPreferredSize(new Dimension(350,500));
		frame.pack();
		frame.setVisible(true);
		
		return frame;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				MainPane.createFrame("MainPane", new MainPane());
			}
		});
	}
}
