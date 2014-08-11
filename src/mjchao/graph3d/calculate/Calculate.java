package mjchao.graph3d.calculate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

import mjchao.graph3d.calculate.functions.Arccosine;
import mjchao.graph3d.calculate.functions.Arcsine;
import mjchao.graph3d.calculate.functions.Arctangent;
import mjchao.graph3d.calculate.functions.Cosecant;
import mjchao.graph3d.calculate.functions.Cosine;
import mjchao.graph3d.calculate.functions.Cotangent;
import mjchao.graph3d.calculate.functions.Secant;
import mjchao.graph3d.calculate.functions.Sine;
import mjchao.graph3d.calculate.functions.Tangent;
import mjchao.graph3d.calculate.operators.Minus;
import mjchao.graph3d.calculate.operators.Multiply;
import mjchao.graph3d.calculate.operators.Negate;
import mjchao.graph3d.util.Text;

/**
 * an environment for performing calculations. every environment has its own set of
 * allowed variables, operators, and functions.
 * 
 * @author mjchao
 *
 */
public class Calculate {
	
	final public static Multiply MULTIPLY = new Multiply();
	final public static Negate NEGATE = new Negate();
	
	private ArrayList< Operator > m_operators = new ArrayList< Operator >();
	private ArrayList< Function > m_functions = new ArrayList< Function >();
	private ArrayList< Variable > m_variables = new ArrayList< Variable >();
	private ArrayList< Constant > m_constants = new ArrayList< Constant >();
	
	final public static String[] DIGITS = { "0" , "1" , "2", "3", "4" , "5" , "6" , "7", "8" , "9", "." };
	final public static OpenParenthesis OPEN_PAREN = new OpenParenthesis();
	final public static CloseParenthesis CLOSE_PAREN = new CloseParenthesis();
	final public static Token[] PARENTHESES = { OPEN_PAREN , CLOSE_PAREN };
	final public static Token[] DELIMITERS = { new Comma() };
	
	private ArrayList< Token > m_tokenizedExpression = new ArrayList< Token >();
	
	private Stack< Token > m_operatorsStack = new Stack< Token >();
	
	private LinkedList< Token > m_outputQueue = new LinkedList< Token >();
	
	private Stack< Value > m_evaluationStack = new Stack< Value >();
	
	final public static int DEGREE_MODE = 1;
	final public static int RADIAN_MODE = 2;
	private int m_angleType = DEGREE_MODE;
	
	/**
	 * creates a calculate environment with no predefined functions, operators, or constants
	 */
	public Calculate() {
		
	}
	
	/**
	 * resets the data stored in this calculate environment, namely the postfix expression
	 * and evaluation data
	 */
	public void resetPostfixProcessing() {
		this.m_tokenizedExpression.clear();
		this.m_operatorsStack.clear();
		this.m_outputQueue.clear();
	}
	
	public void resetEvaluationProcessing() {
		this.m_evaluationStack.clear();
	}
	
	final public void setAngleType( int type ) {
		this.m_angleType = type;
	}
	
	final public int getAngleType() {
		return this.m_angleType;
	}
	
	/**
	 * converts the given expression to postfix and then evaluates it
	 * 
	 * @param expression
	 * @return
	 * @throws RuntimeException
	 */
	public Value evaluate( String expression ) throws RuntimeException {
		process( expression );
		return evaluate();
	}
	/**
	 * processes an expression so that it is converted to postfix and ready to be 
	 * evaluated
	 * 
	 * @param expression			a mathematical expression
	 * @throws RuntimeException
	 */
	public void process( String expression ) throws RuntimeException {
		resetPostfixProcessing();
		tokenize( expression );
		addImplicitOperators();
		convertToPostfix();
	}
	
	/**
	 * evaluates the current processed expression
	 * 
	 * @return						the result of evaluating the currently processed mathematical expression
	 * @throws RuntimeException 
	 */
	public Value evaluate() throws RuntimeException {
		resetEvaluationProcessing();
		evaluatePostfix();
		return this.m_evaluationStack.get( 0 );
	}

	
	/**
	 * tokenizes a string into values, variables, functions, and infix operators.
	 * 
	 * @param expression
	 */
	final private void tokenize( String expression ) {
		
		ArrayList< Token > predefinedNonNumericalTokens = new ArrayList< Token >();
		predefinedNonNumericalTokens.addAll( this.m_constants );
		predefinedNonNumericalTokens.addAll( this.m_functions );
		predefinedNonNumericalTokens.addAll( this.m_operators );
		predefinedNonNumericalTokens.addAll( this.m_variables );
		for ( Token t : PARENTHESES ) {
			predefinedNonNumericalTokens.add( t );
		}
		for ( Token t : DELIMITERS ) {
			predefinedNonNumericalTokens.add( t );
		}
		
		//we initially assume every character in the string is a part of a real number.
		//if we find out the character actually belongs to part of a variable name, function,
		//or infix operator, then we will deal with it in a different manner
		int startOfNumber = 0;
		int endOfNumber = 0;
		
		for ( int expressionIdx=0 ; expressionIdx<expression.length() ; ) {
			
			boolean isNumber = true;
			
			//go through every predefined, non-numerical token
			//and see if it is at the current location in the expression
			for ( Token t : predefinedNonNumericalTokens ) {
				if ( isTokenAtCurrentExpressionIdx( expression , expressionIdx , t ) ) {
					isNumber = false;
					
					//there may have been a number right before this predefined, non-numerical
					//token, so we want to add that to the tokenized list as well
					String previousNumber;
					if ( startOfNumber < endOfNumber ) {
						previousNumber = expression.substring( startOfNumber , endOfNumber );
					}
					else {
						previousNumber = "";
					}
					addNonNumericalToken( previousNumber , t );
					
					expressionIdx += t.getRepresentation().length();
					startOfNumber = expressionIdx;
				}
			}
			
			//if we passed a part of a number (i.e. a digit), then 
			//keep on reading, as there may be more digits to read
			if ( isNumber ) {
				expressionIdx++;
				endOfNumber = expressionIdx;
			}
		}
		
		//there may be a number at the very end of the expression,
		//and we don't want to forget to add that to the tokenized list!
		if ( startOfNumber < endOfNumber ) {
			createAndAddNumber( expression.substring( startOfNumber , endOfNumber ) );
		}
	}
	
	/**
	 * adds implicit negative signs and multiplication
	 */
	final private void addImplicitOperators() {
		
		//if the first token is a minus sign, it must actually be a negative sign
		if ( this.m_tokenizedExpression.size() > 0 ) {
			if ( this.m_tokenizedExpression.get( 0 ) instanceof Minus ) {
				this.m_tokenizedExpression.set( 0 , NEGATE );
			}
		}
		
		for ( int i=1 ; i<this.m_tokenizedExpression.size()-1 ; i++ ) {
			if ( this.m_tokenizedExpression.get( i ) instanceof Minus ) {
				
				//in order for this minus operator to actually be a minus operator,
				//there must be a value before it and after it.
				//otherwise, this minus operator must actually be a negate operator
				
				//check if there is a value before this minus sign
				if ( !( this.m_tokenizedExpression.get( i-1 ) instanceof Value || this.m_tokenizedExpression.get( i-1 ) instanceof CloseParenthesis ) ) {
					this.m_tokenizedExpression.set( i , NEGATE );
					continue;
				}
				
				//check if there is a value following this minus sign
				if ( !( this.m_tokenizedExpression.get( i+1 ) instanceof Value || this.m_tokenizedExpression.get( i+1 ) instanceof OpenParenthesis ) ) {
					this.m_tokenizedExpression.set( i , NEGATE );
					continue;
				}
			}
		}
	
		for ( int i=0 ; i<this.m_tokenizedExpression.size()-1 ; i++ ) {
			if ( this.m_tokenizedExpression.get( i ) instanceof Value || this.m_tokenizedExpression.get( i ) instanceof CloseParenthesis ) {
				if ( this.m_tokenizedExpression.get( i+1 ) instanceof Value || this.m_tokenizedExpression.get( i+1 ) instanceof OpenParenthesis ) {
					this.m_tokenizedExpression.add( i+1 , MULTIPLY );
				}
			}
		}
	}
	
	/**
	 * tries to parse the given token at the given location in the provided mathematical expression.
	 * and returns if the parse was successful or not
	 * 
	 * @param expression			a mathematical expression
	 * @param expressionIdx			the current location in the mathematical expression
	 * @param t						the token to try to parse
	 * @return 						if the token is at the current location in the expression
	 */
	final private boolean isTokenAtCurrentExpressionIdx( String expression , int expressionIdx , Token t ) {
		int lengthOfToken = t.getRepresentation().length();
		
		//make sure we don't go out of bounds when comparing the token's textual representation
		//to the chunk of the expression we're examining
		if ( expressionIdx + lengthOfToken <= expression.length() ) {
			if ( expression.substring( expressionIdx , expressionIdx+lengthOfToken ).equals( t.getRepresentation() ) ) {
				
				//functions are a special case - they must have an open parentheses right after them,
				//as some function names may be a substring of another function
				if ( t instanceof Function ) {
					if ( expressionIdx+lengthOfToken+1 <= expression.length() ) {
						if ( expression.substring( expressionIdx+lengthOfToken , expressionIdx+lengthOfToken+1 ).equals( OpenParenthesis.REPRESENTATION ) ) {
							return true;
						}
					}
				}
				else {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * adds a non-numerical token to the tokenized expression, as well as the number the came
	 * before it, if applicable
	 * 
	 * @param expression				inputted expression by the user
	 * @param startOfPrevNumber			start of the previous number
	 * @param endOfPrevNumber			end of the previous number
	 * @param t							tokenized expression
	 */
	private void addNonNumericalToken( String previousNumber , Token t ) {
		createAndAddNumber( previousNumber );
		this.m_tokenizedExpression.add( t );
	}
	
	/**
	 * attempts to create and add a number to the tokenized list
	 * 
	 * @param numberRepresentation
	 */
	private void createAndAddNumber( String numberRepresentation ) {
		if ( !numberRepresentation.trim().equals( "" ) ) {
			try {
				addTokenToTokenizedExpression( new Value( Double.parseDouble( numberRepresentation.trim() ) ) );
			}
			catch ( NumberFormatException e ) {
				throw new RuntimeException( Text.Calculate.UNRECOGNIZED_EXPRESSION_ERROR + numberRepresentation );
			}
		}
	}
	
	private void addTokenToTokenizedExpression( Token t ) {
		this.m_tokenizedExpression.add( t );
	}
	
	/**
	 * converts the tokenized expression into postfix for easy evaluation
	 */
	final private void convertToPostfix() {
		for ( Token t : this.m_tokenizedExpression ) {
			if ( t instanceof Function ) {
				handleFunction( (Function) t );
			}
			else if ( t instanceof Operator ) {
				handleOperator( (Operator) t );
			}
			else if ( t instanceof Value ) {
				addToOutput( t );
			}
			else if ( t instanceof OpenParenthesis ) {
				handleOpenParen( (OpenParenthesis) t );
			}
			else if ( t instanceof CloseParenthesis ) {
				handleCloseParen( (CloseParenthesis) t );
			}
			else if ( t instanceof Comma ) {
				handleComma( (Comma) t );
			}
			else if ( t instanceof Value ) {
				addToOutput( t );
			}
			else {
				throw new RuntimeException( Text.Calculate.UNRECOGNIZED_EXPRESSION_ERROR + t.getRepresentation() );
			}
		}
		while( this.m_operatorsStack.size() > 0 ) {
			if ( this.m_operatorsStack.peek() instanceof OpenParenthesis ) {
				throw new RuntimeException( Text.Calculate.MISMATCHED_PARENTHESES_ERROR );
			}
			addToOutput( this.m_operatorsStack.pop() );
		}
	}
	
	final private void handleFunction( Function f ) {
		this.m_operatorsStack.push( f );
	}
	
	final private void handleOperator( Operator o ) {
		
		int precedence = o.getPrecedence();
		int prevPrecedence = -1;
		if ( this.m_operatorsStack.size() > 0 ) {
			Token prev = this.m_operatorsStack.peek();
			if ( prev instanceof Operator ) {
				prevPrecedence = ( (Operator) prev ).getPrecedence();
				while( prev instanceof Operator && 
						( (precedence<prevPrecedence) || (o.isLeftAssociative() && precedence <= prevPrecedence ) ) ) {
					if ( this.m_operatorsStack.size() == 0 ) {
						break;
					}
					addToOutput( this.m_operatorsStack.pop() );
					if ( this.m_operatorsStack.size() == 0 ) {
						break;
					}
					prev = this.m_operatorsStack.peek();
					if ( prev instanceof Operator ) {
						prevPrecedence = ( ( Operator ) prev ).getPrecedence();
					}
				}
			}
		}
		this.m_operatorsStack.push( o );
	}
	
	@SuppressWarnings("unused")
	final private void handleComma( Comma c ) {
		while( !( this.m_operatorsStack.peek() instanceof OpenParenthesis ) ) {
			if ( this.m_operatorsStack.size() == 0 ) {
				throw new RuntimeException( Text.Calculate.MISPLACED_COMMA_ERROR );
			}
			else {
				addToOutput( this.m_operatorsStack.pop() );
			}
		}
	}
	
	final private void handleOpenParen( OpenParenthesis p ) {
		this.m_operatorsStack.push( p );
	}
	
	@SuppressWarnings("unused")
	final private void handleCloseParen( CloseParenthesis p ) {
		if ( this.m_operatorsStack.size() == 0 ) {
			throw new RuntimeException( Text.Calculate.MISMATCHED_PARENTHESES_ERROR );
		}
		else {
			while( !( this.m_operatorsStack.peek() instanceof OpenParenthesis ) ) {
				addToOutput( this.m_operatorsStack.pop() );
				if ( this.m_operatorsStack.size() == 0 ) {
					throw new RuntimeException( Text.Calculate.MISMATCHED_PARENTHESES_ERROR );
				}
			}
		}
		
		//remove the open parenthesis
		this.m_operatorsStack.pop();
		if ( this.m_operatorsStack.size() > 0 ) {
			if ( this.m_operatorsStack.peek() instanceof Function ) {
				addToOutput( this.m_operatorsStack.pop() );
			}
		}
	}
	
	final private void addToOutput( Token t ) {
		this.m_outputQueue.add( t );
	}
	
	final private void evaluatePostfix() {
		LinkedList< Token > copyOfPostfix = new LinkedList< Token >();
		for ( Token t : this.m_outputQueue ) {
			copyOfPostfix.add( t );
		}
		
		while( copyOfPostfix.size() > 0 ) {
			Token next = copyOfPostfix.poll();
			if ( next instanceof Value ) {
				this.m_evaluationStack.push( (Value)next );
			}
			else if ( next instanceof Operator ) {
				Operator o = ( Operator ) next;
				Value operationResult = o.operate( getArguments( o , this.m_evaluationStack , o.getNumArguments() ) );
				this.m_evaluationStack.push( operationResult );
			}
			else if ( next instanceof Function ) {
				Function f = ( Function ) next;
				Value operationResult = f.operate( getFlags( f ) , getArguments( f , this.m_evaluationStack , f.getNumArguments() ) );
				this.m_evaluationStack.push( operationResult );
			}
			else {
				throw new RuntimeException( Text.Calculate.UNRECOGNIZED_EXPRESSION_ERROR +  "\"" + next.getRepresentation() + "\"" );
			}
		}
		if ( this.m_evaluationStack.size() > 1 ) {
			throw new RuntimeException( Text.Calculate.TOO_MANY_NUMBERS_ERROR );
		}
		else if ( this.m_evaluationStack.size() < 1 ) {
			throw new RuntimeException( Text.Calculate.TOO_FEW_NUMBERS_ERROR );
		}
	}
	
	/**
	 * gets a number of arguments from the evaluation stack to be supplied to an operator
	 * or function
	 * 
	 * @param operator				the operator or function, which will be used to determine an error message
	 * 								if the user's inputed expression was syntactically incorrect
	 * @param evaluationStack
	 * @param numArguments
	 * @return
	 */
	final private Value[] getArguments( Token t , Stack< Value > evaluationStack , int numArguments ) {
		LinkedList< Value > arguments = new LinkedList< Value >();
		for ( int i=0 ; i< numArguments ; i++ ) {
			if ( evaluationStack.size() > 0 ) {
				arguments.add( evaluationStack.pop() );
			}
			else {
				throw new RuntimeException( Text.Calculate.TOO_FEW_ARGUMENTS + "\"" + t.getRepresentation() + "\"" );
			}
		}
		Collections.reverse( arguments );
		Value[] rtn = new Value[ arguments.size() ];
		for ( int i=0 ; i<arguments.size() ; i++ ) {
			rtn[ i ] = arguments.get( i );
		}
		return rtn;
	}
	
	/**
	 * @param f
	 * @return			any flags for the given function that should be applied in this calculate
	 * 					environment
	 */
	final private int[] getFlags( Function f ) {
		int[] rtn = new int[ 0 ];
		if ( f instanceof Sine || f instanceof Cosine || f instanceof Tangent || 
				f instanceof Cosecant || f instanceof Secant || f instanceof Cotangent || 
				f instanceof Arcsine || f instanceof Arccosine || f instanceof Arctangent ) {
			rtn = new int[]{ this.m_angleType };
		}
		return rtn;
	}
	
	final public static Value getRadAngleFromFlag( int angleFlag , Value angle ) {
		switch( angleFlag ) {
			case Calculate.DEGREE_MODE:
				return Value.convertDegToRad( angle );
			case Calculate.RADIAN_MODE:
				return angle;
			default:
				return angle;
		}
	}
	
	final public static Value getAngleFromRadAngle( int angleFlag , Value angle ) {
		switch( angleFlag ) {
			case Calculate.DEGREE_MODE:
				return Value.convertRadToDeg( angle );
			case Calculate.RADIAN_MODE:
				return angle;
			default: 
				return Value.convertRadToDeg( angle );
		}
	}
	
	/**
	 * adds the given operators to the list of allowed operators in this calculate
	 * environment
	 * 
	 * @param operators
	 */
	public void defineOperators( Operator... operators ) {
		for ( Operator o : operators ) {
			this.m_operators.add( o );
		}
	}
	
	/**
	 * adds the given functions to the list of allowed functions in this calculate
	 * environment
	 * 
	 * @param functions
	 */
	public void defineFunctions( Function... functions ) {
		for ( Function f : functions ) {
			this.m_functions.add( f );
		}
	}
	
	/**
	 * adds the given variables to the list of allowed variables in this calculate
	 * environment
	 * 
	 * @param variables
	 */
	public void defineVariables( Variable... variables ) {
		for ( Variable v : variables ) {
			this.m_variables.add( v );
		}
	}
	
	public void setVariableValue( Variable var , double value ) {
		for ( Variable v : this.m_variables ) {
			if ( v.equals( var ) ) {
				var.set( value );
			}
		}
	}
	
	/**
	 * adds the given constants to the list of allowed constants in this calculate 
	 * environment
	 * 
	 * @param constants
	 */
	public void defineConstants( Constant... constants ) {
		for ( Constant c : constants ) {
			this.m_constants.add( c );
		}
	}
}
