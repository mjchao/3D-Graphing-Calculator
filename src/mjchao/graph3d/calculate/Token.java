package mjchao.graph3d.calculate;

/**
 * represents a piece of a mathematical expression. for example, in "x^2+54", the tokens
 * are "x", "^", "2", "+" and "54"
 * 
 * @author mjchao
 *
 */
public class Token {

	private String m_representation;
	
	public Token( String representation ) {
		this.m_representation = representation;
	}
	
	public String getRepresentation() {
		return this.m_representation;
	}
	
	protected void setRepresentation( String representation ) {
		this.m_representation = representation;
	}
	
	@Override
	public String toString() {
		return this.m_representation;
	}
}
