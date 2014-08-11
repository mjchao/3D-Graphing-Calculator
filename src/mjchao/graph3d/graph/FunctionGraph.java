package mjchao.graph3d.graph;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.scene.paint.PhongMaterial;

public class FunctionGraph extends Graph {

	/**
	 * cache for the cartesian points on this graph
	 */
	final ArrayList< QuadrilateralData > m_data = new ArrayList< QuadrilateralData >();
	
	public FunctionGraph( GraphPaper3D graphPaper ) {
		super( graphPaper );
	}
	
	@Override
	public void clear() {
		super.clear();
		this.m_data.clear();
	}
	
	/**
	 * adds a quadrilateral to a list of quadrilaterals that will be graphed later (i.e.
	 * when all the quadrilaterals in this graph are ready to be graphed)
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
	 * @param material				specifies the properties of this quadrilateral (e.g. color)
	 */
	public void addQuadrilateralToGraphLater( double x1 , double y1 , double z1 ,
											double x2 , double y2, double z2 ,
											double x3 , double y3 , double z3 ,
											double x4 , double y4 , double z4 , 
											PhongMaterial material) {
		this.m_data.add( new QuadrilateralData( x1 , y1 , z1 , 
												x2 , y2 , z2 , 
												x3 , y3 , z3 , 
												x4 , y4 , z4 , 
												material ) );
	}
	
	/**
	 * graphs all the quadrilaterals in this function graph
	 */
	public void graphAllQuadrilaterals() {
		Platform.runLater( new Runnable() {

			@Override
			public void run() {
				for ( QuadrilateralData d : FunctionGraph.this.m_data ) {
					Quadrilateral3D toPlot = FunctionGraph.this.m_graphPaper.createQuadrilateral( d.m_x1 , d.m_y1 , d.m_z1 , 
																									d.m_x2 , d.m_y2 , d.m_z2 , 
																									d.m_x3 , d.m_y3 , d.m_z3 , 
																									d.m_x4 , d.m_y4 , d.m_z4 , 
																									d.m_material );
					FunctionGraph.this.m_graph.addComponent( toPlot );
				}
				FunctionGraph.this.graph();
			}
			
		});
	}

	private class QuadrilateralData {
		
		final public double m_x1 , m_y1 , m_z1;
		final public double m_x2 , m_y2 , m_z2;
		final public double m_x3 , m_y3 , m_z3;
		final public double m_x4 , m_y4 , m_z4;
		final public PhongMaterial m_material;
		
		public QuadrilateralData( double x1 , double y1 , double z1 ,
									double x2 , double y2, double z2 ,
									double x3 , double y3 , double z3 ,
									double x4 , double y4 , double z4 , 
									PhongMaterial material ) {
			this.m_x1 = x1; this.m_y1 = y1; this.m_z1 = z1;
			this.m_x2 = x2; this.m_y2 = y2; this.m_z2 = z2;
			this.m_x3 = x3; this.m_y3 = y3; this.m_z3 = z3;
			this.m_x4 = x4; this.m_y4 = y4; this.m_z4 = z4;
			this.m_material = material;
		}
	}
}
