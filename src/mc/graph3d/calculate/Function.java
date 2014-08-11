package mc.graph3d.calculate;

abstract public class Function extends Token {

	final private int m_numArguments;
	
	public Function( String representation , int numArguments ) {
		super( representation );
		this.m_numArguments = numArguments;
	}
	
	public int getNumArguments() {
		return this.m_numArguments;
	}
	
	/**
	 * @param flags		any information the function requires, e.g. degree mode vs. radian mode vs. gradian mode
	 * @param args		the arguments to the function
	 * @return			the result of applying this function to the supplied arguments
	 */
	abstract public Value operate( int[] flags , Value... args );
}
