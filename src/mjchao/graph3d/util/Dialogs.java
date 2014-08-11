package mjchao.graph3d.util;

import javax.swing.JOptionPane;

/**
 * provides methods to display standard dialogs
 * 
 * @author mjchao
 *
 */
public class Dialogs {

	final public static void showErrorMessage( String message ) {
		JOptionPane.showMessageDialog( null , message , "Error" , JOptionPane.ERROR_MESSAGE );
	}
}
