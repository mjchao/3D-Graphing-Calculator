3D-Graphing-Calculator
======================

Graphs 3D surfaces and paths

Requirements
-----
This program was written in Java 8. You will need a Java Runtime Environment 8, which can be downloaded here:

http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html

Graph Paper
-----

This program uses a right-handed coordinate system, which obeys the right-hand rule. Before you rotate your view, we have the following properties:

* The x-axis goes left (-) to right (+).
* The y-axis goes into the screen. Higher y-coordinates are "farther" away from you.
* The z-axis goes bottom (-) to up(+).

Click on the graph paper to gain focus. You can use the keyboard to view the graph in different ways.

* The program will attempt to determine the Cartesian coordinates of the point you clicked. Please note that this is not perfect, as it is difficult to choose a point with 3-dimensions when you are clicking on a 3-dimensional screen.
* WASD moves you forward, left, backward, and right, respectively.
* Up, Down, Right, Left arrows rotate your view up, down, right, and left, respectively.
* Shift + Up, Down, Right, Left slides your view up, down, right, and left, respectively.
* Shift + WASD rotates your view up, down, right, and left, respectively.
* Q, E rotates your view counter-clockwise and clockwise, respectively. This might be useful in some situations, but is generally not recommended, as it can cause gimbal lock.
* CTRL+SHIFT+R resets your view.

Window Constraints Panel
-----

Located at the bottom of the frame, the window constraints panel allows you to modify the coordinate axes and coordinate grid (tick marks on the coordinate axes) of the graph paper.

* You can specify the minimum and maximum x, y, and z axis values. These are the bounds the graph paper will use to determine whether or not to display a piece of a graph.
* You can specify the x, y, and z increments. These determine how far apart the tick marks on the coordinate axes are spaced.
* You can specify whether or not you want to see the coordinate axes and coordinate grid.

Add Graph Panel
-----

Located at the right side of the frame, the add graph panel allows you to add graphs to be displayed on the graph paper. When you click "Add Graph" you will be prompted to select a type of graph. The currently available types are

* Functions, of the form z(x, y) = ...
* Parametrics of the form x(t) = ... , y(t) = ... , z(t) = ...

For each individual graph, you must specify the minimum, maximum, and increments of the dependent variable(s). For exmaple, when graphing functions, you must specify the minimum, maximum, and increments of x and y, because the z value is dependent on x and y.

You can also pick a graph color and opt to temporarily ignore the graph. Temporarily ignored graphs are not displayed when you click "Regraph"

WARNING: When you click regraph, there will be a progress bar that indicates the graphing progress. There will also be a button that allows you to terminate the graphing process if it is taking too long. Nevertheless, think carefully when choosing your dependent variable increments. 

