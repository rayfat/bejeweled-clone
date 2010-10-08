import javax.swing.JFrame;

public class Bejeweled
{
	public static void main(String[] args) 
	{
		BejeweledPanel panel = new BejeweledPanel(6, 6);
		JFrame frame = new JFrame();
		frame.setContentPane(panel);
		frame.setSize(600, 600);
		frame.setTitle("Bejeweled Clone");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
