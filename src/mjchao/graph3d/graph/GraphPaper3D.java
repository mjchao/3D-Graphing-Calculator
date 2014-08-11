package mjchao.graph3d.graph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.concurrent.ArrayBlockingQueue;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javax.swing.Timer;

import mc.graph3d.calculate.DefaultCalculate;
import mc.graph3d.calculate.Variable;
import mjchao.graph3d.util.Materials;

/**
 * 3D paper on which 3D graphs may be displayed. Note that 3D graphs have the positive x-axis
 * pointing right, the positive y-axis pointing into the page, and the positive z-axis
 * pointing up. JavaFX has the positive x-axis pointing right, the positive y-axis pointing
 * down, and the positive z-axis pointing into the page.
 * 
 * @author mjchao
 *
 */
public class GraphPaper3D extends Scene {

	final private Body m_root;
	final private Body m_system;
	
	final private double m_width;
	
	final private double m_height;
	
	final private double m_depth;
	
	
	private boolean m_areAxesVisible = true;
	private boolean m_isGridVisible = true;
	
	private Body m_coordinateAxes = new Body();
	private double m_xMin = -1;
	private double m_xMax = 1;
	private double m_xInc = 0.1;
	
	/**
	 * the scale for converting from Cartesian x-coordinates to pixel x-coordinates
	 */
	private double m_scaleX = 0;
	
	private double m_yMin = -1;
	private double m_yMax = 1;
	private double m_yInc = 0.1;
	
	/**
	 * the scale for converting from Cartesian y-coordinates to pixel y-coordinates
	 */
	private double m_scaleY = 0;
	
	private double m_zMin = -1;
	private double m_zMax = 1;
	private double m_zInc = 0.1;
	
	/**
	 * the scale for converting from Cartesian z-coordinates to pixel z-coordinates
	 */
	private double m_scaleZ = 0;
	
	final public static DecimalFormat COORDINATE_TEXT_FORMAT = new DecimalFormat( "#.00" );
	private Text txtShowSelectedPoint = new Text( "SELECTED: (0, 0, 0)" );
	
	final public static int SELECT_POINT_RADIUS = 5;
	private Sphere m_selectedPoint = new Sphere( SELECT_POINT_RADIUS );

	final public static double CAMERA_TRANSLATE_INC = 10;
	final public static double CAMERA_ROTATE_INC = 3;
	
	final private Body m_cameraBody = new Body();
	/**
	 * lens through which user sees this graph paper
	 */
	final private PerspectiveCamera m_camera = new PerspectiveCamera( true );
	
	public GraphPaper3D( double width , double height , double depth ) {
		super( new Body() , width , height );
		this.m_root = (Body) this.getRoot();
		this.m_system = new Body();
		this.m_root.addComponent( this.m_system );
		this.m_width = width;
		this.m_height = height;
		this.m_depth = depth;
		this.setFill( Color.GREY );
		calculatePixelToCartesianConvertData();
		setupListeners();
		
		//objects must be drawn in this order, because with JavaFX, things drawn later get
		//drawn on top of things drawn earlier. The "3D covering" is not realistic, as of JavaFX 8
		
		//we have the graph information on the very top, attached to the root.
		//then, the axes are drawn, and all further graphs get drawn behind the axes
		//as the drawGraph() method puts them at the start of the component list
		drawGraphInformation();
		drawAxes();
		setupCamera();
		
		//displayTestLine();
	}

	private void setupListeners() {
		this.setOnMouseClicked( new EventHandler< MouseEvent >() {

			@Override
			public void handle(MouseEvent event) {
				Node pickedNode = event.getPickResult().getIntersectedNode();
				Point3D selectPoint = event.getPickResult().getIntersectedPoint();
				PixelPoint p;
				if ( pickedNode == null ) {
					p = new PixelPoint( selectPoint.getX() , selectPoint.getY() , selectPoint.getZ() );
				}
				else {
					p = new PixelPoint( selectPoint.getX()+pickedNode.getTranslateX() , selectPoint.getY()+pickedNode.getTranslateY() , selectPoint.getZ()+pickedNode.getTranslateZ() );
				}
				updateSelectedPoint( p , convertPixelToCartesian( p ) );
			}
			
		});
		
		final ArrayBlockingQueue< KeyPressData > keysPressed = new ArrayBlockingQueue< KeyPressData >( 1000 );
		this.setOnKeyPressed( new EventHandler< KeyEvent >() {

			private Timer m_keyHandler = new Timer( 15 , new ActionListener() {
				
				@Override
				public synchronized void actionPerformed( ActionEvent e ) {
					for ( KeyPressData code : keysPressed ) {
						Platform.runLater( new Runnable() {

							@Override
							public void run() {
								handleKeyPressed( code );
							}
							
						});
					}
				}
			});
			
			{
				this.m_keyHandler.start();
			}
			
			@Override
			public void handle( KeyEvent event ) {
				KeyPressData eventData = new KeyPressData( event );
				if ( !keysPressed.contains( eventData ) ) {
					keysPressed.add( eventData );
				}
			}
		});
		
		this.setOnKeyReleased( new EventHandler< KeyEvent >() {

			@Override
			public void handle( KeyEvent event ) {
				keysPressed.remove( new KeyPressData( event ) );
			}
			
		});
	}
	
	/**
	 * contains data about the user's key presses
	 * 
	 * @author mjchao
	 *
	 */
	final private class KeyPressData {
		
		final public KeyCode m_code;
		final public boolean m_shift;
		final public boolean m_ctrl;
		
		public KeyPressData( KeyEvent event ) {
			this.m_code = event.getCode();
			this.m_shift = event.isShiftDown();
			this.m_ctrl = event.isControlDown();
		}
		
		@Override
		public boolean equals( Object o ) {
			if ( o instanceof KeyPressData ) {
				KeyPressData k = ( KeyPressData ) o;
				return this.m_code.equals( k.m_code );
			}
			else {
				return false;
			}
		}
	}
	
	void handleKeyPressed( KeyPressData keyInfo ) {
		KeyCode code = keyInfo.m_code;
		boolean shift = keyInfo.m_shift;
		boolean ctrl = keyInfo.m_ctrl;
		if ( code == KeyCode.W ) {
			if ( shift==true ) {
				rotateUp();
			}
			else {
				moveForward();
			}
		}
		else if ( code == KeyCode.A ) {
			if ( shift==true ) {
				rotateLeft();
			}
			else {
				moveLeft();
			}
		}
		else if ( code == KeyCode.S ) {
			if ( shift==true ) {
				rotateDown();
			}
			else {
				moveBackward();
			}
		}
		else if ( code == KeyCode.D ) {
			if ( shift==true ) {
				rotateRight();
			}
			else {
				moveRight();
			}
		}
		else if ( code == KeyCode.Q ) {
			rotateCounterClockwise();
		}
		else if ( code == KeyCode.E ) {
			rotateClockwise();
		}
		else if ( code == KeyCode.UP ) {
			if ( shift == true ) {
				moveUp();
			}
			else {
				rotateUp();
			}
		}
		else if ( code == KeyCode.DOWN ) {
			if ( shift == true ) {
				moveDown();
			}
			else {
				rotateDown();
			}
		}
		else if ( code == KeyCode.LEFT ) {
			if ( shift==true ) {
				moveLeft();
			}
			else {
				rotateLeft();
			}
		}
		else if ( code == KeyCode.RIGHT ) {
			if ( shift==true ) {
				moveRight();
			}
			else {
				rotateRight();
			}
		}
		else if ( code == KeyCode.R ) {
			if ( shift==true && ctrl==true ) {
				resetCamera();
			}
		}
	}
	
	private void setupCamera() {
		
		this.m_cameraBody.addComponent( this.m_camera );
		this.m_root.addComponent( this.m_cameraBody );
		this.m_cameraBody.setTranslateZ( -1000 );
		
		this.setCamera( this.m_camera );
		this.m_camera.setNearClip( 1 );
		this.m_camera.setFarClip( 10000 );
		
	}
	
	
	private void drawAxes() {
		calculateConvertXData();
		calculateConvertYData();
		calculateConvertZData();
		
		this.m_system.removeComponent( this.m_coordinateAxes );
		this.m_coordinateAxes.getChildren().clear();
		
        PhongMaterial blackMaterial = new PhongMaterial();
        blackMaterial.setDiffuseColor(Color.BLACK);
        blackMaterial.setSpecularColor(Color.BLACK);
        
        boolean xAxisVisible = false;
        boolean yAxisVisible = false;
        boolean zAxisVisible = false;
        
        if ( this.m_areAxesVisible ) {
	        double axisThickness = 5;
			
	        if ( this.m_yMin <= 0 && 0 <= this.m_yMax ) {
	        	if ( this.m_zMin <= 0 && 0 <+ this.m_zMax ) {
	        		xAxisVisible = true;
					Text xAxisLabel = new Text( "x" );
					xAxisLabel.setFont( new Font( 20 ) );
					xAxisLabel.setFill( Color.YELLOW );
					xAxisLabel.setTranslateX( convertCartesianToPixelX( this.m_xMax ) + 10 );
					xAxisLabel.setTranslateY( convertCartesianToPixelY( 0 ) + 5 );
					this.m_coordinateAxes.addComponent( xAxisLabel );
					
			        Box xAxis = new Box( this.m_width , axisThickness , axisThickness );
					xAxis.setMaterial( blackMaterial );
					xAxis.setTranslateX( convertCartesianToPixelX( (this.m_xMax+this.m_xMin)/2 ) );
					xAxis.setTranslateY( convertCartesianToPixelY( 0 ) );
					xAxis.setTranslateZ( convertCartesianToPixelZ( 0 ) );
					this.m_coordinateAxes.addComponent( xAxis );
	        	}
	        }
			
	        if ( this.m_xMin <= 0 && 0 <= this.m_xMax ) {
	        	if ( this.m_zMin <= 0 && 0 <= this.m_zMax ) {
	        		yAxisVisible = true;
					Text yAxisLabel = new Text( "y" );
					yAxisLabel.setFont( new Font( 20 ) );
					yAxisLabel.setFill( Color.YELLOW );
					yAxisLabel.setTranslateX( convertCartesianToPixelX( 0 )-5 );
					yAxisLabel.setTranslateZ( convertCartesianToPixelY( this.m_yMax )+10 );
					this.m_coordinateAxes.addComponent( yAxisLabel );
		        	
					Box yAxis = new Box( axisThickness , axisThickness , this.m_depth );
					yAxis.setMaterial( blackMaterial );
					yAxis.setTranslateX( convertCartesianToPixelX( 0 ) );
					yAxis.setTranslateY( convertCartesianToPixelZ( 0 ) );
					yAxis.setTranslateZ( convertCartesianToPixelY( (this.m_yMax+this.m_yMin)/2 ) );
					this.m_coordinateAxes.addComponent( yAxis );
	        	}
	        }
			
	        if ( this.m_xMin <= 0 && 0 <= this.m_xMax ) {
	        	if ( this.m_yMin <= 0 && 0 <= this.m_yMax ) {
	        		zAxisVisible = true;
					Text zAxisLabel = new Text( "z" );
					zAxisLabel.setFont( new Font( 20 ) );
					zAxisLabel.setFill( Color.YELLOW );
					zAxisLabel.setTranslateX( convertCartesianToPixelX( 0 )-5 );
					zAxisLabel.setTranslateY( convertCartesianToPixelZ( this.m_zMax )-10 );
					zAxisLabel.setTranslateZ( convertCartesianToPixelY( 0 ) );
					this.m_coordinateAxes.addComponent( zAxisLabel );
		        	
					Box zAxis = new Box( axisThickness , this.m_height , axisThickness );
					zAxis.setMaterial( blackMaterial );
					zAxis.setTranslateX( convertCartesianToPixelX( 0 ) );
					zAxis.setTranslateY( convertCartesianToPixelZ( (this.m_zMax + this.m_zMin)/2 ) );
					zAxis.setTranslateZ( convertCartesianToPixelY( 0 ) );
					this.m_coordinateAxes.addComponent( zAxis );
	        	}
	        }
        }
        
        if ( this.m_isGridVisible ) {
			Body coordinateGrid = new Body();
			
			double visibleAxisGridThickness = 1;
			double invisibleAxisGridThickness = 5;
			
			double xGridThickness = invisibleAxisGridThickness;
			if ( xAxisVisible ) {
				xGridThickness = visibleAxisGridThickness;
			}
			for ( double x=this.m_xMin ; x<=this.m_xMax ; x+=this.m_xInc ) {
				Box gridLine = new Box( xGridThickness , xGridThickness , 20 );
				gridLine.setTranslateX( convertCartesianToPixelX( x )  );
				gridLine.setTranslateY( convertCartesianToPixelZ( Math.max( 0 , this.m_zMin ) ) );
				gridLine.setTranslateZ( convertCartesianToPixelY( Math.max( 0 , this.m_yMin ) ) );
				gridLine.setMaterial( blackMaterial );
				coordinateGrid.addComponent( gridLine );
			}
			
			double yGridThickness = invisibleAxisGridThickness;
			if ( yAxisVisible ) {
				yGridThickness = visibleAxisGridThickness;
			}
			for ( double y=this.m_yMin ; y<=this.m_yMax ; y+=this.m_yInc ) {
				Box gridLine = new Box( 20 , yGridThickness , yGridThickness );
				gridLine.setTranslateX( convertCartesianToPixelX( Math.max( 0 , this.m_xMin ) ) );
				gridLine.setTranslateY( convertCartesianToPixelZ( Math.max( 0 , this.m_zMin ) ) );
				gridLine.setTranslateZ( convertCartesianToPixelY( y ) );
				gridLine.setMaterial( blackMaterial );
				coordinateGrid.addComponent( gridLine );
			}
			
			double zGridThickness = invisibleAxisGridThickness;
			if ( zAxisVisible ) {
				zGridThickness = visibleAxisGridThickness;
			}
			for ( double z=this.m_zMin ; z<=this.m_zMax ; z+=this.m_zInc ) {
				Box gridLine = new Box( zGridThickness , zGridThickness , 20 );
				gridLine.setTranslateX( convertCartesianToPixelX( Math.max( 0 , this.m_xMin ) ) );
				gridLine.setTranslateY( convertCartesianToPixelZ( z ) );
				gridLine.setTranslateZ( convertCartesianToPixelY( Math.max( 0 , this.m_yMin ) ) );
				gridLine.setMaterial( blackMaterial );
				coordinateGrid.addComponent( gridLine );
			}
			this.m_coordinateAxes.addComponent( coordinateGrid );
        }
        
		this.m_system.getChildren().add( 0 , this.m_coordinateAxes );
	}
	
	private void drawGraphInformation() {
		this.txtShowSelectedPoint.setFont( new Font( 20 ) );
		this.txtShowSelectedPoint.setTranslateX( -this.m_width/1.5 );
		this.txtShowSelectedPoint.setTranslateY( -this.m_height/1.5 );
		this.txtShowSelectedPoint.setTranslateZ( this.m_depth );
		this.m_root.getChildren().add( this.txtShowSelectedPoint );

		this.m_system.getChildren().add( this.m_selectedPoint );
	}
	
	void updateSelectedPoint( PixelPoint selectLocation , CartesianPoint selectedPoint ) {
		this.txtShowSelectedPoint.setText( "SELECTED: " + selectedPoint.toString() );
		this.m_selectedPoint.setTranslateX( selectLocation.m_pixelX );
		this.m_selectedPoint.setTranslateY( selectLocation.m_pixelY );
		this.m_selectedPoint.setTranslateZ( selectLocation.m_pixelZ );
	}
	
	@SuppressWarnings("unused")
	/**
	 * for testing purposes
	 */
	private void displayTestGraph() {
		Graph g = new Graph( this );
		for ( double x=this.m_xMin ; x<this.m_xMax ; x+=this.m_xInc ) {
			for ( double y=this.m_yMin ; y<this.m_yMax ; y+= this.m_yInc ) {
				double z = x*x - y*y;
				if ( this.m_zMin<=z && z<=this.m_zMax ) {
					double nextX = x+this.m_xInc;
					double zNextXCurrY = nextX*nextX - y*y;
					
					if ( this.m_zMin<=zNextXCurrY && zNextXCurrY<=this.m_zMax ) {
						
						double nextY = y+this.m_yInc;
						double zNextXNextY = nextX*nextX - nextY*nextY;
						if ( this.m_zMin<=zNextXNextY && zNextXNextY<=this.m_zMax ) {
							
							double zCurrXNextY = x*x - nextY*nextY;
							if ( this.m_zMin<=zCurrXNextY && zCurrXNextY<=this.m_zMax ) {
								
								Point3D p1 = convertCartesianToPixel( new CartesianPoint( x , y , z ) ).toPoint3D();
								Point3D p2 = convertCartesianToPixel( new CartesianPoint( nextX , y , zNextXCurrY ) ).toPoint3D();
								Point3D p3 = convertCartesianToPixel( new CartesianPoint( nextX , nextY , zNextXNextY ) ).toPoint3D();
								Point3D p4 = convertCartesianToPixel( new CartesianPoint( x , nextY , zCurrXNextY ) ).toPoint3D();
								g.plotQuadrilateral( p1.getX() , p1.getY() , p1.getZ() ,
													 p2.getX() , p2.getY() , p2.getZ() ,
													 p3.getX() , p3.getY() , p3.getZ() ,
													 p4.getX() , p4.getY() , p4.getZ() , Materials.YELLOW.m_material );
							}
						}
					}
				}
			}
		}
		
		drawGraph( g );
	}
	
	@SuppressWarnings("unused")
	/**
	 * for testing purposes
	 * @param expression
	 */
	private void displayTestGraph2( String expression ) {
		DefaultCalculate c = new DefaultCalculate();
		Variable xVar = new Variable( "x" );
		Variable yVar = new Variable( "y" );
		c.defineVariables( xVar , yVar );
		c.process( expression );
		
		Graph g = new Graph( this );
		for ( double x=this.m_xMin ; x<this.m_xMax ; x+=this.m_xInc ) {
			for ( double y=this.m_yMin ; y<this.m_yMax ; y+= this.m_yInc ) {
				xVar.set( x );
				yVar.set( y );
				double z = c.evaluate().value();
				if ( this.m_zMin<=z && z<=this.m_zMax ) {
					double nextX = x+this.m_xInc;
					xVar.set( nextX );
					yVar.set( y );
					double zNextXCurrY = c.evaluate().value();
					
					if ( this.m_zMin<=zNextXCurrY && zNextXCurrY<=this.m_zMax ) {
						
						double nextY = y+this.m_yInc;
						xVar.set( nextX );
						yVar.set( nextY );
						double zNextXNextY = c.evaluate().value();
						if ( this.m_zMin<=zNextXNextY && zNextXNextY<=this.m_zMax ) {
							
							xVar.set( x );
							yVar.set( nextY );
							double zCurrXNextY = c.evaluate().value();
							if ( this.m_zMin<=zCurrXNextY && zCurrXNextY<=this.m_zMax ) {
								
								Point3D p1 = convertCartesianToPixel( new CartesianPoint( x , y , z ) ).toPoint3D();
								Point3D p2 = convertCartesianToPixel( new CartesianPoint( nextX , y , zNextXCurrY ) ).toPoint3D();
								Point3D p3 = convertCartesianToPixel( new CartesianPoint( nextX , nextY , zNextXNextY ) ).toPoint3D();
								Point3D p4 = convertCartesianToPixel( new CartesianPoint( x , nextY , zCurrXNextY ) ).toPoint3D();
								g.plotQuadrilateral( p1.getX() , p1.getY() , p1.getZ() ,
										 p2.getX() , p2.getY() , p2.getZ() ,
										 p3.getX() , p3.getY() , p3.getZ() ,
										 p4.getX() , p4.getY() , p4.getZ() , Materials.YELLOW.m_material );
							}
						}
					}
				}
			}
		}
		drawGraph( g );
	}
	
	@SuppressWarnings("unused")
	private void displayTestLine() {
		Point3D start = convertCartesianToPixel( new CartesianPoint( 1 , 1 , 0 ) ).toPoint3D();
		Point3D end = convertCartesianToPixel( new CartesianPoint( 1 , 1 , 10 ) ).toPoint3D();
		Line3D l = new Line3D( start , end , Materials.YELLOW.m_material );
		this.m_system.addComponent( l );
	}
	
	/**
	 * a point with pixel coordinates x, y, z, where x is distance
	 * along the width, y is distance along the depth, and z is distance
	 * along the height
	 *  
	 * @author mjchao
	 *
	 */
	protected class PixelPoint {
		
		public double m_pixelX;
		public double m_pixelY;
		public double m_pixelZ;
		
		public PixelPoint( double pixelX , double pixelY , double pixelZ ) {
			this.m_pixelX = pixelX;
			this.m_pixelY = pixelY;
			this.m_pixelZ = pixelZ;
		}
		
		public Point3D toPoint3D() {
			return new Point3D( this.m_pixelX , this.m_pixelY , this.m_pixelZ );
		}
		
		@Override
		public String toString() {
			return "(" + this.m_pixelX + ", " + this.m_pixelY + ", " + this.m_pixelZ + ")";
		}
	}
	
	/**
	 * a point with Cartesian coordinates x, y, z
	 * 
	 * @author mjchao
	 *
	 */
	protected class CartesianPoint {
		
		public double m_cartesianX;
		public double m_cartesianY;
		public double m_cartesianZ;
		
		public CartesianPoint( double x , double y , double z ) {
			this.m_cartesianX = x;
			this.m_cartesianY = y;
			this.m_cartesianZ = z;
		}
		
		@Override
		public String toString() {
			
			return "(" + COORDINATE_TEXT_FORMAT.format( this.m_cartesianX ) + ", " + COORDINATE_TEXT_FORMAT.format( this.m_cartesianY ) + ", " + COORDINATE_TEXT_FORMAT.format( this.m_cartesianZ ) + ")";
		}
	}
	
	/**
	 * sets the minimum, tick increment, and maximum values of the x axis
	 * 
	 * @param xMin
	 * @param xMax
	 * @param xInc
	 */
	public void setXAxisProperties( double xMin , double xMax , double xInc ) {
		this.m_xMin = xMin;
		this.m_xMax = xMax;
		this.m_xInc = xInc;
		calculateConvertXData();
	}
	
	/**
	 * @return			an array containing the x minimum value, x maximum value, and x increment 
	 */
	public double[] getXAxisProperties() {
		return new double[] { this.m_xMin , this.m_xMax , this.m_xInc };
	}
	
	/**
	 * sets the minimum, tick increment, and maximum values of the y axis
	 * 
	 * @param yMin
	 * @param yMax
	 * @param yInc
	 */
	public void setYAxisProperties( double yMin , double yMax , double yInc ) {
		this.m_yMin = yMin;
		this.m_yMax = yMax;
		this.m_yInc = yInc;
		calculateConvertYData();
	}
	
	/**
	 * @return			an array containing the y minimum value, y maximum value, and y increment value 
	 */
	public double[] getYAxisProperties() {
		return new double[] {this.m_yMin , this.m_yMax , this.m_yInc };
	}
	
	/**
	 * sets the minimum, tick increment, and maximum values of the z axis
	 * 
	 * @param zMin
	 * @param zMax
	 * @param zInc
	 */
	public void setZAxisProperties( double zMin , double zMax , double zInc ) {
		this.m_zMin = zMin;
		this.m_zMax = zMax;
		this.m_zInc = zInc;
		calculateConvertZData();
	}
	
	/**
	 * @return			an array containing the z minimum value, z maximum value, and z increment value
	 */
	public double[] getZAxisProperties() {
		return new double[] { this.m_zMin , this.m_zMax , this.m_zInc };
	}
	
	/**
	 * sets if the coordinate axes should be drawn on this graph paper
	 * 
	 * @param b
	 */
	public void setAxesVisible( boolean b ) {
		this.m_areAxesVisible = b;
	}
	
	/**
	 * sets if the coordinate grid should be drawn on this graph paper
	 * 
	 * @param b
	 */
	public void setGridVisible( boolean b ) {
		this.m_isGridVisible = b;
	}
	
	/**
	 * redraws the axes and the coordinate grid and updates cartesian to pixel conversion
	 * data. the graphs ARE NOT redrawn
	 */
	public void refreshDisplay() {
		calculateConvertXData();
		calculateConvertYData();
		calculateConvertZData();
		
		drawAxes();
	}
	
	/**
	 * converts the pixel coordinates of a point on the screen into Cartesian coordinates
	 * 
	 * @param p
	 * @return
	 */
	protected CartesianPoint convertPixelToCartesian( PixelPoint p ) {
		return new CartesianPoint( convertPixelToCartesianX( p.m_pixelX ) , convertPixelToCartesianY( p.m_pixelZ ) , convertPixelToCartesianZ( p.m_pixelY ) );
	}
	
	/**
	 * converts the Cartesian coordinates of a point on a graph to the pixel coordinates of
	 * a point on the screen
	 * 
	 * @param p
	 * @return
	 */
	protected PixelPoint convertCartesianToPixel( CartesianPoint p ) {
		return new PixelPoint( convertCartesianToPixelX( p.m_cartesianX ) , convertCartesianToPixelZ( p.m_cartesianZ ) , convertCartesianToPixelY( p.m_cartesianY ) );
	}
	
	private void calculatePixelToCartesianConvertData() {
		calculateConvertXData();
		calculateConvertYData();
		calculateConvertZData();
	}
	
	private void calculateConvertXData() {
		double pixelInterval = this.m_width;
		double cartesianInterval = this.m_xMax - this.m_xMin;
		this.m_scaleX = pixelInterval/cartesianInterval;
	}
	
	private double convertCartesianToPixelX( double x ) {
		return x*this.m_scaleX;
	}
	
	private double convertPixelToCartesianX( double x ) {
		if ( this.m_scaleX == 0 ) {
			System.err.println( "scale x is 0");
			return 0;
		}
		else {
			return x/this.m_scaleX;
		}
	}

	private void calculateConvertYData() {
		double pixelInterval = this.m_depth;
		double cartesianInterval = this.m_yMax - this.m_yMin;
		this.m_scaleY = pixelInterval/cartesianInterval;
	}
	
	private double convertCartesianToPixelY( double y ) {
		return y*this.m_scaleY;
	}
	
	private double convertPixelToCartesianY( double y ) {
		if ( this.m_scaleY == 0 ) {
			System.err.println( "scale y is 0" );
			return 0;
		}
		else {
			return y/this.m_scaleY;
		}
	}
	
	private void calculateConvertZData() {
		double pixelInterval = this.m_height;
		double cartesianInterval = this.m_zMax - this.m_zMin;
		this.m_scaleZ = pixelInterval/cartesianInterval;
	}
	
	private double convertCartesianToPixelZ( double z ) {
		return -z*this.m_scaleZ;
	}
	
	private double convertPixelToCartesianZ( double z ) {
		if ( this.m_scaleZ == 0 ) {
			System.err.println( "scale z is 0" );
			return 0;
		}
		else {
			return -z/this.m_scaleZ;
		}
	}

	/**
	 * creates the quadrilateral specified by four 3D Cartesian points on this graph.
	 * the points should be provided in counter-clockwise order
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
	 */
	public Quadrilateral3D createQuadrilateral( 
									double x1 , double y1 , double z1 ,
								   double x2 , double y2 , double z2 ,
								   double x3 , double y3 , double z3 ,
								   double x4 , double y4 , double z4 ,
								   PhongMaterial material ) {
		CartesianPoint p1 = new CartesianPoint( x1 , y1 , z1 );
		CartesianPoint p2 = new CartesianPoint( x2 , y2 , z2 );
		CartesianPoint p3 = new CartesianPoint( x3 , y3 , z3 );
		CartesianPoint p4 = new CartesianPoint( x4 , y4 , z4 );
		PixelPoint pixelP1 = convertCartesianToPixel( p1 );
		PixelPoint pixelP2 = convertCartesianToPixel( p2 );
		PixelPoint pixelP3 = convertCartesianToPixel( p3 );
		PixelPoint pixelP4 = convertCartesianToPixel( p4 );
		Quadrilateral3D quadrilateral = new Quadrilateral3D( pixelP1.toPoint3D() , pixelP2.toPoint3D() , pixelP3.toPoint3D() , pixelP4.toPoint3D() , true , material );
		return quadrilateral;
	}
	
	/**
	 * creates the 3D-line specified by the two given points: the start and end points
	 * 
	 * @param startX
	 * @param startY
	 * @param startZ
	 * @param endX
	 * @param endY
	 * @param endZ
	 * @param material
	 * @return
	 */
	public Line3D createLine( double startX , double startY , double startZ , 
								double endX , double endY , double endZ , 
								PhongMaterial material ) {
		CartesianPoint p1 = new CartesianPoint( startX , startY , startZ );
		CartesianPoint p2 = new CartesianPoint( endX , endY , endZ );
		PixelPoint pixelP1 = convertCartesianToPixel( p1 );
		PixelPoint pixelP2 = convertCartesianToPixel( p2 );
		Line3D line = new Line3D( pixelP1.toPoint3D() , pixelP2.toPoint3D() , material );
		return line;
	}
	
	/**
	 * draws the specified graph on this graph paper
	 * 
	 * @param g
	 */
	public void drawGraph( Graph g ) {
		if ( !this.m_system.getChildren().contains( g.getGraphBody() ) ) {
			this.m_system.getChildren().add( 0 , g.getGraphBody() );
		}
	}
	
	/**
	 * removes the specified graph from this graph paper
	 * 
	 * @param g
	 */
	public void undrawGraph( Graph g ) {
		this.m_system.getChildren().remove( g.getGraphBody() );
	}

	/**
	 * erases the specified graph on this graph paper
	 * 
	 * @param g
	 */
	public void removeGraph( Graph g ) {
		this.m_system.removeComponent( g.getGraphBody() );
	}
	
	public void moveForward() {
		this.m_system.translate( 0 , 0 , -CAMERA_TRANSLATE_INC );
	}
	
	public void moveBackward() {
		this.m_system.translate( 0 , 0 , CAMERA_TRANSLATE_INC );
	}
	
	public void moveLeft() {
		this.m_system.translate( CAMERA_TRANSLATE_INC , 0 , 0 );
	}
	
	public void moveRight() {
		this.m_system.translate( -CAMERA_TRANSLATE_INC , 0 , 0 );
	}
	
	public void moveUp() {
		this.m_system.translate( 0 , CAMERA_TRANSLATE_INC , 0 );
	}
	
	public void moveDown() {
		this.m_system.translate(0 ,  -CAMERA_TRANSLATE_INC , 0 );
	}
	
	public void rotateLeft() {
		this.m_system.rotate( 0 , -CAMERA_ROTATE_INC , 0 );
	}
	
	public void rotateRight() {
		this.m_system.rotate( 0 , CAMERA_ROTATE_INC , 0 );
	}
	
	public void rotateUp() {
		this.m_system.rotate( CAMERA_ROTATE_INC , 0 , 0 );
	}
	
	public void rotateDown() {
		this.m_system.rotate( -CAMERA_ROTATE_INC , 0 , 0 );
	}
	
	public void rotateClockwise() {
		this.m_system.rotate( 0 , 0 , CAMERA_ROTATE_INC ); 
	}
	
	public void rotateCounterClockwise() {
		this.m_system.rotate( 0 , 0 , -CAMERA_ROTATE_INC );
	}
	
	/**
	 * shifts the camera in the positive x-direction of the graph paper (i.e. left)
	 */
	public void shiftForwardX() {
		this.m_system.translate( CAMERA_TRANSLATE_INC , 0 , 0 );
	}
	
	/**
	 * resets the scene so that the camera is focused along the positive z-direction (into
	 * the screen) at the origin (0, 0, 0)
	 */
	public void resetCamera() {
		this.m_system.setPositionX( 0 );
		this.m_system.setPositionY( 0 );
		this.m_system.setPositionZ( 0 );
		this.m_system.setRotateX( 0 );
		this.m_system.setRotateY( 0 );
		this.m_system.setRotateZ( 0 );
	}
}
