package mjchao.graph3d.calculate;

/**
 * a mathematical infix operator, e.g. +, -, *, /, %, ^
 * @author mjchao
 *
 */
abstract public class Operator extends Token {

	final private int m_precedence;
	final private boolean m_leftAssociative;
	final private int m_numArguments;
	
	/**
	 * creates an operator with the given representation , precedence, and left associativity.
	 * the higher the precedence value, the earlier the operator is applied
	 * 
	 * @param numArguments
	 * @param representation
	 * @param precedence
	 * @param leftAssociative
	 */
	public Operator( String representation , int numArguments , int precedence , boolean leftAssociative ) {
		super( representation );
		this.m_numArguments = numArguments;
		this.m_precedence = precedence;
		this.m_leftAssociative = leftAssociative;
	}
	
	public int getNumArguments() {
		return this.m_numArguments;
	}
	
	public int getPrecedence() {
		return this.m_precedence;
	}
	
	public boolean isLeftAssociative() {
		return this.m_leftAssociative;
	}
	
	/**
	 * @param left
	 * @param right
	 * @return			the result of applying this operator to a left and right value
	 */
	abstract public Value operate( Value... args );
}
