package mjchao.graph3d.ui;

import java.awt.BorderLayout;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mjchao.graph3d.graph.GraphPaper3D;

/**
 * contains the user interface of this program
 * 
 * @author mjchao
 *
 */
public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final JFXPanel pnlGraph = new JFXPanel();
	GraphPaper3D graphPaper;
	
	final private JPanel pnlCenter = new JPanel( new BorderLayout() );
	final GraphInputsPanel pnlEquations = new GraphInputsPanel();
	final WindowPanel pnlWindow = new WindowPanel();
	
	public MainFrame() {
		
		setLayout( new BorderLayout() );
		this.pnlCenter.add( new JScrollPane( this.pnlGraph ) , BorderLayout.CENTER );
		this.pnlCenter.add( new JScrollPane( this.pnlWindow ) , BorderLayout.SOUTH );
		add( this.pnlCenter , BorderLayout.CENTER );
		add( this.pnlEquations , BorderLayout.EAST );
		
		Platform.runLater( new Runnable() {
			
			@Override
			public void run() {
				MainFrame.this.graphPaper = new GraphPaper3D( 500 , 500 , 500 );
				MainFrame.this.pnlGraph.setScene( MainFrame.this.graphPaper );
				MainFrame.this.pnlEquations.setGraphPaper( MainFrame.this.graphPaper );
				MainFrame.this.pnlWindow.setGraphPaper( MainFrame.this.graphPaper );
				MainFrame.this.pnlWindow.setEquationInputPanel( MainFrame.this.pnlEquations );
			}
		});
		
		setVisible( true );
		pack();
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}

}
