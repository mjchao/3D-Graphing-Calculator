package mjchao.graph3d.graph;

import javafx.application.Platform;
import javafx.scene.paint.PhongMaterial;

/**
 * provides all the data necessary to draw a graph on a graph paper
 * 
 * @author mjchao
 *
 */
public class Graph {
	
	GraphPaper3D m_graphPaper;
	Body m_graph = new Body();
	
	/**
	 * @param graphPaper			the graph paper on which this graph will be rendered
	 */
	public Graph( GraphPaper3D graphPaper ) {
		this.m_graphPaper = graphPaper;
	}
	
	/**
	 * clears this graph, so that a new graph can be rendered
	 */
	public void clear() {
		Platform.runLater( new Runnable() {

			@Override
			public void run() {
				Graph.this.m_graph.getChildren().clear();
			}
			
		});
	}
	

	/**
	 * given two cartesian points on this graph (a start and an end point), plots the line formed by 
	 * these two points
	 * 
	 * @param startX
	 * @param startY
	 * @param startZ
	 * @param endX
	 * @param endY
	 * @param endZ
	 * @param material			specifies the appearance of the line (e.g. color)
	 */
	public void plotLine( double startX , double startY , double startZ , 
							double endX , double endY , double endZ , 
							PhongMaterial material ) {
		Line3D toPlot = this.m_graphPaper.createLine( startX , startY, startZ, endX, endY, endZ, material);
		plotLine( toPlot );
	}
	
	private void plotLine( Line3D line ) {
		Platform.runLater( new Runnable() {

			@Override
			public void run() {
				Graph.this.m_graph.addComponent( line );
			}
			
		});
	}
	
	/**
	 * given four cartesian points on this graph, plots the quadrilateral formed by those four points.
	 * the four points should be given in counter-clockwise order.
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @param x3
	 * @param y3
	 * @param z3
	 * @param x4
	 * @param y4
	 * @param z4
	 * @param material			specifies the appearance of the quadrilateral (e.g. color)		
	 */
	public void plotQuadrilateral( double x1 , double y1 , double z1 ,
									double x2 , double y2, double z2 ,
									double x3 , double y3 , double z3 ,
									double x4 , double y4 , double z4 , 
									PhongMaterial material ) {
		Quadrilateral3D toPlot = this.m_graphPaper.createQuadrilateral( x1 , y1 , z1 ,
																		x2 , y2 , z2 ,
																		x3 , y3 , z3 ,
																		x4 , y4 , z4 , material );
		plotQuadrilateral( toPlot );
	}
	
	private void plotQuadrilateral( Quadrilateral3D quad ) {
		Platform.runLater( new Runnable() {

			@Override
			public void run() {
				Graph.this.m_graph.addComponent( quad );
			}
			
		});
	}
	
	/**
	 * returns the graphics used to draw the graph.
	 * 
	 * @return			the graphics of the graph
	 */
	Body getGraphBody() {
		return this.m_graph;
	}
	
	/**
	 * removes this graph from the graph paper
	 */
	public void ungraph() {
		Platform.runLater( new Runnable() {
			
			@Override
			public void run() {
				Graph.this.m_graphPaper.undrawGraph( Graph.this );
			}
		});
	}
	/**
	 * draws this graph on the graph paper to which this graph is attached
	 */
	public void graph() {
		Platform.runLater( new Runnable() {

			@Override
			public void run() {
				Graph.this.m_graphPaper.drawGraph( Graph.this );
			}
			
		});
	}
}
