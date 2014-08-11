package mjchao.graph3d.ui;

/**
 * stores the user inputed minimum, maximum and increment for a dimension of a graph
 * 
 * @author mjchao
 *
 */
public class DimensionData {

	final public String m_dimension;
	final public double m_min;
	final public double m_max;
	final public double m_inc;
	
	public DimensionData( String dimension , double min , double max , double inc ) {
		this.m_dimension = dimension;
		this.m_min = min;
		this.m_max = max;
		this.m_inc = inc;
	}
	
	@Override
	public boolean equals( Object o ) {
		if ( o instanceof DimensionData ) {
			DimensionData d = ( DimensionData ) o;
			return this.m_min == d.m_min && this.m_max == d.m_max && this.m_inc == d.m_inc;
		}
		else {
			return false;
		}
	}
}
