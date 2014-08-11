package mjchao.graph3d.graph;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

/**
 * represents some 3-dimensional system that can be translated, rotated, and scaled
 * 
 * @author mjchao
 *
 */
class Body extends Group {

	final public static Translate TRANSLATE_IDENTITY() {
		return new Translate();
	}
	
	final public static Rotate ROTATE_X_AXIS() {
		Rotate rtn = new Rotate();
		rtn.setAxis( Rotate.X_AXIS );
		return rtn;
	}
	
	final public static Rotate ROTATE_Y_AXIS() {
		Rotate rtn = new Rotate();
		rtn.setAxis( Rotate.Y_AXIS );
		return rtn;
	}
	
	final public static Rotate ROTATE_Z_AXIS() {
		Rotate rtn = new Rotate();
		rtn.setAxis( Rotate.Z_AXIS );
		return rtn;
	}
	
	final public static Scale DEFAULT_SCALE() {
		return new Scale();
	}
		
	/**
	 * provides rotation order options. For example, XYZ is equivalent to rotating about
	 * an x-directional axis, then a y-directional  axis, then a z-directional axis. it
	 * is important to note that 3D rotations are not commutative. 
	 */
	public enum RotateOrder {
		XYZ , XZY , YXZ , YZX , ZXY , ZYX;
	}
	
	final private RotateOrder m_rotationOrder;
	private Translate m_position = new Translate();
	private Translate m_pivot = new Translate();
	private Translate m_inversePivot = new Translate();
	private Rotate m_rotX = new Rotate();
	private Rotate m_rotY = new Rotate();
	private Rotate m_rotZ = new Rotate();
	private Scale m_scale = new Scale();
	
	/**
	 * creates a default body with XYZ rotation. the body is rotated about a x-directional
	 * axis first, then a y-directional axis, then an z-directional axis. it is important
	 * to note that 3D rotations are not commutative.
	 */
	public Body() {
		this( RotateOrder.XYZ );
	}
	
	/**
	 * creates a body with the specified rotation order. it is important to note that
	 * 3D rotations are not commutative.
	 * 
	 * @param rotOrder	the order in which rotations should be performed on this body
	 */
	public Body( RotateOrder rotOrder ) {
		this( rotOrder , TRANSLATE_IDENTITY() );
	}
	
	/**
	 * creates a body with the specified position and rotaion order
	 * 
	 * @param position	position of the body
	 * @param rotOrder	the order in which rotations should be performed on this body
	 */
	public Body( Translate position , RotateOrder rotOrder ) {
		this( position , rotOrder , TRANSLATE_IDENTITY() , ROTATE_X_AXIS() , ROTATE_Y_AXIS() , ROTATE_Z_AXIS() );
	}
	
	/**
	 * creates a body with the specified rotation order and pivot.
	 * 
	 * @param pivot		the point about which rotations occur 
	 * @param rotOrder	the order in which rotations should be performed on this body
	 */
	public Body( RotateOrder rotOrder , Translate pivot ) {
		this( TRANSLATE_IDENTITY() , rotOrder , pivot ,  ROTATE_X_AXIS() , ROTATE_Y_AXIS() , ROTATE_Z_AXIS() );
	}
	
	/**
	 * creates the body with the specified position and rotation properties
	 * 
	 * @param position	the position of the body
	 * @param rotOrder	the order in which rotations should be performed on this body
	 * @param pivot		the point about which this body should be rotated
	 * @param rotX		the first axis of rotation
	 * @param rotY		the second axis of rotation
	 * @param rotZ		the third axis of rotation
	 */
	public Body( Translate position , RotateOrder rotOrder , Translate pivot , Rotate rotX , Rotate rotY , Rotate rotZ ) {
		this( position , rotOrder , pivot , rotX , rotY , rotZ , DEFAULT_SCALE() );
	}
	
	public Body( Translate position , RotateOrder rotOrder , Translate pivot , Rotate rotX , Rotate rotY , Rotate rotZ , Scale scale ) {
		this.m_position = position;
		
		setPivot( pivot );
		this.m_rotX = rotX;
		this.m_rotY = rotY;
		this.m_rotZ = rotZ;
		
		super.getTransforms().addAll( position , pivot );
		
		this.m_rotationOrder = rotOrder;
		switch( rotOrder ) {
			case XYZ:
				super.getTransforms().addAll( rotX , rotY , rotZ );
				break;
			case XZY:
				super.getTransforms().addAll( rotX , rotZ , rotY );
				break;
			case YXZ:
				super.getTransforms().addAll( rotY , rotX , rotZ );
				break;
			case YZX:
				super.getTransforms().addAll( rotY , rotZ , rotX );
				break;
			case ZXY:
				super.getTransforms().addAll( rotZ , rotX , rotY );
				break;
			case ZYX:
				super.getTransforms().addAll( rotZ , rotY , rotX );
				break;
			default:
				break;
		}
		this.m_scale = scale;
		super.getTransforms().addAll( scale , this.m_inversePivot );
	}
	
	/**
	 * sets the position of this body
	 * 
	 * @param p			the position of this body
	 */
	public void setPosition( Translate p ) {
		setPosition( p.getX() , p.getY() , p.getZ() );
	}
	
	/**
	 * sets the position of this body
	 * 
	 * @param x 
	 * @param y 
	 * @param z	
	 */
	public void setPosition( double x , double y , double z ) {
		setPositionX( x );
		setPositionY( y );
		setPositionZ( z );
	}
	
	/**
	 * sets the x-coordinate of the position of this body
	 * 
	 * @param x
	 */
	public void setPositionX( double x ) {
		this.m_position.setX( x );
	}
	
	/**
	 * sets the y-coordinate of the position of this body
	 * 
	 * @param y
	 */
	public void setPositionY( double y ) {
		this.m_position.setY( y );
	}
	
	/**
	 * sets the z-coordinate of the position of this body
	 * 
	 * @param z
	 */
	public void setPositionZ( double z ) {
		this.m_position.setZ( z );
	}
	
	public void translate( double dx , double dy , double dz ) {
		this.setPosition( new Translate( this.m_position.getX()+dx , this.m_position.getY()+dy , this.m_position.getZ()+dz ) );
	}
	
	/**
	 * @return	the order in which rotations are performed on this body
	 */
	public RotateOrder getRotationOrder() {
		return this.m_rotationOrder;
	}
	
	/**
	 * @return	 the point about which rotations are performed on this body
	 */
	public Translate getPivot() {
		return this.m_pivot;
	}
	
	/**
	 * sets the pivot about which rotations are performed on this body
	 * @param p
	 */
	public void setPivot( Translate p ) {
		this.m_pivot = p;
		this.m_inversePivot.setX( p.getX()*-1 );
		this.m_inversePivot.setY( p.getY()*-1 );
		this.m_inversePivot.setZ( p.getZ()*-1 );
		
		this.m_rotX.setPivotX( p.getX() );
		this.m_rotX.setPivotY( p.getY() );
		this.m_rotX.setPivotZ( p.getZ() );
		
		this.m_rotY.setPivotX( p.getX() );
		this.m_rotY.setPivotY( p.getY() );
		this.m_rotY.setPivotZ( p.getZ() );
		
		this.m_rotZ.setPivotX( p.getX() );
		this.m_rotZ.setPivotY( p.getY() );
		this.m_rotZ.setPivotZ( p.getZ() );
	}
	
	/**
	 * sets the amount of rotation about the x, y, and z axes of rotation
	 * 
	 * @param rotXDegrees
	 * @param rotYDegrees
	 * @param rotZDegrees
	 */
	public void setRotate( double rotXDegrees , double rotYDegrees , double rotZDegrees ) {
		setRotateX( rotXDegrees );
		setRotateY( rotYDegrees );
		setRotateZ( rotZDegrees );
	}
	
	/**
	 * sets the amount of rotation about the x-axis of rotation
	 * 
	 * @param rotDegrees
	 */
	public void setRotateX( double rotDegrees ) {
		this.m_rotX.setAngle( rotDegrees );
	}
	
	/**
	 * @return	the amount of rotation about the x-axis of rotation
	 */
	public double getRotateX() {
		return this.m_rotX.getAngle();
	}
	
	/**
	 * sets the amount of rotation about the y-axis of rotation
	 * 
	 * @param rotDegrees
	 */
	public void setRotateY( double rotDegrees ) {
		this.m_rotY.setAngle( rotDegrees );
	}
	
	/**
	 * @return		the amount of rotation about the y-axis of rotation
	 */
	public double getRotateY() {
		return this.m_rotY.getAngle();
	}
	
	/**
	 * sets the amount of rotation about the z-axis of rotation
	 * 
	 * @param rotDegrees
	 */
	public void setRotateZ( double rotDegrees ) {
		this.m_rotZ.setAngle( rotDegrees );
	}
	
	/**
	 * @return		the amount of rotation about the z-axis of rotation
	 */
	public double getRotateZ() {
		return this.m_rotZ.getAngle();
	}
	
	/**
	 * rotates this body by the specified degree amounts about each axis
	 * 
	 * @param dRotX		degrees to rotate about x axis
	 * @param dRotY		degrees to rotate about y axis
	 * @param dRotZ		degrees to rotate about z axis
	 */
	public void rotate( double dRotX , double dRotY , double dRotZ ) {
		this.setRotate( this.getRotateX()+dRotX , this.getRotateY()+dRotY , this.getRotateZ()+dRotZ );
	}
	
	/**
	 * sets the scaling factor of this body
	 * 
	 * @param scale
	 */
	public void setScale( Scale scale ) {
		setScale( scale.getX() , scale.getY() , scale.getZ() );
	}
	
	/**
	 * sets the scaling factor of this body
	 * 
	 * @param magnitudeX
	 * @param magnitudeY
	 * @param magnitudeZ
	 */
	public void setScale( double magnitudeX , double magnitudeY , double magnitudeZ ) {
		setBodyScaleX( magnitudeX );
		setBodyScaleY( magnitudeY );
		setBodyScaleZ( magnitudeZ );
	}
	
	/**
	 * sets the scaling factor of this body in the x-axis direction
	 * 
	 * @param magnitude
	 */
	public void setBodyScaleX( double magnitude ) {
		this.m_scale.setX( magnitude );
	}
	
	/**
	 * sets the scaling factor of this body in the y-axis direction
	 * 
	 * @param magnitude
	 */
	public void setBodyScaleY( double magnitude ) {
		this.m_scale.setY( magnitude );
	}
	
	/**
	 * sets the scaling factor of this body in the z-axis direction
	 * @param magnitude
	 */
	public void setBodyScaleZ( double magnitude ) {
		this.m_scale.setZ( magnitude );
	}
	
	/**
	 * adds the specified component to this body. in essence, calls getChildren.add( n );
	 * 
	 * @param n
	 */
	public void addComponent( Node n ) {
		super.getChildren().add( n );
	}
	
	/**
	 * removes the specified component from this body. in essence, calls getChildren.remove( n )
	 * @param n
	 */
	public void removeComponent( Node n ) {
		super.getChildren().remove( n );
	}
}
