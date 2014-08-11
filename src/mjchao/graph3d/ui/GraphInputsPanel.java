package mjchao.graph3d.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import mjchao.graph3d.graph.GraphPaper3D;
import mjchao.graph3d.util.Dialogs;
import mjchao.graph3d.util.Text;

/**
 * panel for inputing information describing multiple graphs for the program to display
 * 
 * @author mjchao
 *
 */
public class GraphInputsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final private Component EXTRA_SPACING = Box.createVerticalGlue();
	
	final private JPanel pnlEquations = new JPanel();
	
	final public static String[] AVAILABLE_GRAPH_TYPES = { Text.GraphType.FUNCTION , Text.GraphType.PARAMETRIC };
	
	final private JPanel pnlCommands = new JPanel();
		final private JButton cmdAddFunction = new JButton( Text.EquationInputPanel.ADD_GRAPH );
		final private JButton cmdRegraph = new JButton( Text.EquationInputPanel.REGRAPH );
		
	final private JPanel pnlProgress = new JPanel();
		final JLabel lblProgress = new JLabel();
		final JProgressBar progProgress = new JProgressBar();
		final private JButton cmdCancel = new JButton( Text.EquationInputPanel.CANCEL_REGRAPH );

	final ArrayList< EquationPanel > m_equations = new ArrayList< EquationPanel >();
	
	GraphPaper3D m_graphPaper;
	
	public GraphInputsPanel() {
		setLayout( new BorderLayout() );
		
		this.pnlEquations.setLayout( new BoxLayout( this.pnlEquations , BoxLayout.Y_AXIS ) );
		this.pnlEquations.add( this.EXTRA_SPACING );
		add( new JScrollPane( this.pnlEquations ) , BorderLayout.CENTER );
		
		this.pnlCommands.setLayout( new FlowLayout( FlowLayout.CENTER ) );
		this.pnlCommands.add( this.cmdAddFunction );
		this.pnlCommands.add( this.cmdRegraph );
		add( this.pnlCommands , BorderLayout.SOUTH );
		
		this.pnlProgress.setLayout( new FlowLayout( FlowLayout.CENTER ) );
		this.pnlProgress.add( this.lblProgress );
		this.pnlProgress.add( this.progProgress );
		this.pnlProgress.add( this.cmdCancel );
		
		addListeners();
	}
	
	public void setGraphPaper( GraphPaper3D graphPaper ) {
		this.m_graphPaper = graphPaper;
	}
	
	private void addListeners() {
		this.cmdRegraph.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed( ActionEvent e ) {
				regraphEquations();
			}
			
		});
		
		this.cmdAddFunction.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String graphToAdd = requestEquationType();
				if ( graphToAdd == null ) {
					return;
				}
				else {
					if ( graphToAdd.equals( Text.GraphType.FUNCTION ) ) {
						GraphInputsPanel.this.addEquation( new FunctionEquationPanel( GraphInputsPanel.this.m_graphPaper ) );
					}
					else if ( graphToAdd.equals( Text.GraphType.PARAMETRIC ) ) {
						GraphInputsPanel.this.addEquation( new ParametricEquationPanel( GraphInputsPanel.this.m_graphPaper ) );
					}
				}
			}
		});
		
		this.cmdCancel.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed( ActionEvent e ) {
				cancelRegraph();
			}
			
		});
	}

	/**
	 * asks the user for a type of equation to add
	 * 
	 * @return			the type of equation the user wanted to add, or null if the user
	 * 					clicked cancel
	 */
	public String requestEquationType() {
		Object choice = JOptionPane.showInputDialog( null , Text.GraphType.DIALOG_PROMPT , Text.GraphType.DIALOG_TITLE , JOptionPane.PLAIN_MESSAGE , null , AVAILABLE_GRAPH_TYPES , AVAILABLE_GRAPH_TYPES[ 0 ] );
		if ( choice == null ) {
			return null;
		}
		else {
			return (String) choice;
		}
	}
	
	public void addEquation( final EquationPanel equationPanel ) {
		this.pnlEquations.remove( this.EXTRA_SPACING );
		this.pnlEquations.add( equationPanel );
		this.pnlEquations.add( this.EXTRA_SPACING );
		equationPanel.addRemoveListener( new ActionListener() {

			@Override
			public void actionPerformed( ActionEvent e ) {
				removeEquation( equationPanel );
			}
			
		});
		this.m_equations.add( equationPanel );
		revalidate();
	}
	
	public void removeEquation( EquationPanel equationPanel ) {
		equationPanel.removeGraph();
		this.m_equations.remove( equationPanel );
		this.pnlEquations.remove( equationPanel );
		revalidate();
	}
	
	public void graphEquation( EquationPanel p , Thread regraphThread , JProgressBar progress ) {
		try {
			p.createGraph( regraphThread , progress );
		}
		catch ( RuntimeException error ) {
			Dialogs.showErrorMessage( Text.EquationInputPanel.getGraphErrorMessage( p.getEquationText() , error.getMessage() ) );
		}
	}
	
	Thread m_regraphThread;
	int m_equationBeingGraphed = 0;
	int m_totalEquations = 0;
	/**
	 * ungraphs and then regraphs every equation added to this equation input panel
	 */
	public void regraphEquations() {
		reflectGraphingState();
		this.m_regraphThread = new Thread() {
			
			@Override
			public void run() {
				GraphInputsPanel.this.m_equationBeingGraphed = 0;
				GraphInputsPanel.this.m_totalEquations = GraphInputsPanel.this.m_equations.size();
				for ( EquationPanel p : GraphInputsPanel.this.m_equations ) {
					GraphInputsPanel.this.m_equationBeingGraphed++;
					SwingUtilities.invokeLater( new Runnable() {

						@Override
						public void run() {
							GraphInputsPanel.this.lblProgress.setText( Text.EquationInputPanel.getTotalProgress( GraphInputsPanel.this.m_equationBeingGraphed ,  GraphInputsPanel.this.m_totalEquations ) );
						}
						
					});
					if ( GraphInputsPanel.this.m_regraphThread.isInterrupted() ) {
						break;
					}
					else {
						graphEquation( p , GraphInputsPanel.this.m_regraphThread , GraphInputsPanel.this.progProgress );
					}
				}
				reflectNonGraphingState();
			}
		};
		this.m_regraphThread.start();
	}
	
	/**
	 * cancels the regraphing of equations
	 */
	public void cancelRegraph() {
		this.m_regraphThread.interrupt();
		reflectNonGraphingState();
	}
	
	public void reflectGraphingState() {
		for ( EquationPanel p : this.m_equations ) {
			p.disableModification();
		}
		this.remove( this.pnlCommands );
		this.add( this.pnlProgress , BorderLayout.SOUTH );
		revalidate();
	}
	
	public void reflectNonGraphingState() {
		for ( EquationPanel p : this.m_equations ) {
			p.enableModification();
		}
		this.remove( this.pnlProgress );
		this.add( this.pnlCommands , BorderLayout.SOUTH );
		revalidate();
	}
}
