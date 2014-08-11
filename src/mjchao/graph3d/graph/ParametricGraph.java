package mjchao.graph3d.graph;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.scene.paint.PhongMaterial;

public class ParametricGraph extends Graph {

	final ArrayList< LineData > m_data = new ArrayList< LineData >();
	
	public ParametricGraph( GraphPaper3D graphPaper ) {
		super( graphPaper );
	}
	
	@Override
	public void clear() {
		super.clear();
		this.m_data.clear();
	}
	
	/**
	 * adds a line to a list of lines that will be graphed later (i.e. when 
	 * all the lines in this parametric graph are ready to be graphed)
	 * 
	 * @param startX
	 * @param startY
	 * @param startZ
	 * @param endX
	 * @param endY
	 * @param endZ
	 * @param material
	 */
	public void addLineToGraphLater( double startX , double startY , double startZ , 
										double endX , double endY , double endZ , 
										PhongMaterial material ) {
		this.m_data.add( new LineData( startX , startY , startZ , 
										endX , endY , endZ , 
										material ) );
	}

	/**
	 * graphs all the lines in this parametric graph
	 */
	public void graphAllLines() {
		Platform.runLater( new Runnable() {

			@Override
			public void run() {
				for( LineData d : ParametricGraph.this.m_data ) {
					Line3D toPlot = ParametricGraph.this.m_graphPaper.createLine( d.m_startX , d.m_startY , d.m_startZ , 
																					d.m_endX , d.m_endY , d.m_endZ , 
																					d.m_material );
					ParametricGraph.this.m_graph.addComponent( toPlot );
				}
				ParametricGraph.this.graph();
			}
			
		});
	}
	
	private class LineData {
		
		final public double m_startX , m_startY , m_startZ;
		final public double m_endX , m_endY , m_endZ;
		final public PhongMaterial m_material;
		
		public LineData( double startX , double startY , double startZ , double endX , double endY , double endZ , PhongMaterial material ) {
			this.m_startX = startX; this.m_startY = startY; this.m_startZ = startZ;
			this.m_endX = endX; this.m_endY = endY; this.m_endZ = endZ;
			this.m_material = material;
		}
	}
}
