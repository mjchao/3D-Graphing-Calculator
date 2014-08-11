package mjchao.graph3d.run;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import mjchao.graph3d.ui.MainFrame;

/**
 * runs the program
 * 
 * @author mjchao
 *
 */
public class Main extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final public static void main( String[] args ) {
		System.setProperty("prism.dirtyopts", "false");
		
		SwingUtilities.invokeLater( new Runnable() {
			
			@SuppressWarnings("unused")
			@Override
			public void run() {
				new MainFrame();
			}
		});
	}
}
