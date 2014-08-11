package mjchao.graph3d.util;

/**
 * the Strings used by the program. these can be translated so the program can be easily 
 * distributed in other languages
 * 
 * @author mjchao
 *
 */
public class Text {

	final public static class Calculate {
		
		public static String UNRECOGNIZED_EXPRESSION_ERROR = "Unrecognized expression: ";
		public static String MISMATCHED_PARENTHESES_ERROR = "Mismatched parentheses";
		public static String MISPLACED_COMMA_ERROR = "Misplaced comma";
		public static String TOO_FEW_NUMBERS_ERROR = "Too few numbers";
		public static String TOO_MANY_NUMBERS_ERROR = "Too many numbers";
		public static String TOO_FEW_ARGUMENTS = "Too few arguments for operator/function: ";
	}
	
	final public static class EquationInputPanel {
		
		public static String ADD_GRAPH = "Add Graph";
		public static String REGRAPH = "Regraph";
		public static String getGraphErrorMessage( String graphEquation , String errorMessage) {
			return "There was an error graphing \n" + graphEquation + "\nThe problem is the following: \n" + errorMessage;
		}
		
		public static String getTotalProgress( int currGraph , int totalGraphs ) {
			return "Graphing " + currGraph + " of " + totalGraphs;
		}
		
		public static String CANCEL_REGRAPH = "Cancel";
		
	}
	
	public static class GraphType {
		
		public static String DIALOG_TITLE = "Select Graph Type";
		public static String DIALOG_PROMPT = "Please select a graph type: ";
		public static String FUNCTION = "Function: z(x, y)=";
		public static String PARAMETRIC = "Parametric: x(t)=, y(t)=, z(t)=";
	}
	
	final public static class EquationPanel {
		
		public static String IGNORE_EQUATION = "Temporarily Ignore";
		public static String REMOVE_EQUATION = "Delete";
		
		public static String SELECT_COLOR = "Graph Color:";
		
		public static String DIMENSION_MIN = "min:";
		public static String DIMENSION_MAX = "max:";
		public static String DIMENSION_INC = "inc:";
		
		public static String INPUT_FUNCTION = "z(x, y)=";
		public static String INPUT_FUNCTION_X_MIN = "x min:";
		public static String X_DIMENSION = "x";
		public static String Y_DIMENSION = "y";
		public static String Z_DIMENSION = "z";
		
		public static String INPUT_X_PARAMETRIC = "x(t)=";
		public static String INPUT_Y_PARAMETRIC = "y(t)=";
		public static String INPUT_Z_PARAMETRIC = "z(t)=";
		public static String T_DIMENSION = "t";
		
		public static String INPUT_FUNCTION_X_MAX = "x max:";
		public static String INPUT_FUNCTION_X_INC = "x inc:";
		public static String INPUT_FUNCTION_Y_MIN = "y min:";
		public static String INPUT_FUNCTION_Y_MAX = "y max:";
		public static String INPUT_FUNCTION_Y_INC = "y inc:";
	}
	
	final public static class Materials {
		
		public static String RED = "Red";
		public static String GREEN = "Green";
		public static String BLUE = "Blue";
		public static String YELLOW = "Yellow";
		public static String ORANGE = "Orange";
		public static String WHITE = "White";
	}
	
	final public static class WindowPanel {
		
		public static String SHOW_XYZ_AXES = "Show x-y-z axes";
		public static String SHOW_COORDINATE_GRID = "Show coordinate grid";
		public static String UPDATE = "Update View";
	}
	
}
