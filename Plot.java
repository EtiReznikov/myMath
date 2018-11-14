package myMath;

import java.awt.Color;
import javax.swing.JFrame;
import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.plots.points.PointRenderer;
import de.erichseifert.gral.ui.InteractivePanel;

/**
 * This class represents the library "Gral". 
 * This code was taken from the site https://github.com/eseifert/gral and was adapted to our needs.
 * The class accepts Polynom, draws it and marks the min and max points of the Polynom.
 * The user has to use frame.setVisible(true); in order to see the plot.
 */

public class Plot extends JFrame {
	
	/**
	 * This methods builds a new plot. it accepts Polynom (represented by polynom) and range (represented by x1 and x2).
	 * The method will search in the range for the min and max points. x1 has to be smaller than x2, else the method will throw exception.
	 * Given a Polynom we will mark it in blue, and after finding the min and max points we will mark them in black.
	 * The calculation of the points will be done by using the function "root" from the Polynom class.
	 * The user has to use frame.setVisible(true); in order to see the plot.
	 * @param polynom the Polynom we want to draw
	 * @param x1 the starting point of the range
	 * @param x2 the ending point of the range
	 * @throws Exception if x1 is bigger than x2
	 */
	
	public Plot(Polynom polynom, double x1, double x2) throws Exception {
		// Throws exception if x1 is bigger than x2
		if (x1>=x2)
			throw new Exception("x1 should be smaller than x2");
		// if the range is legal
		else {
			// Builds the size of the plots frame
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setSize(500, 500);
			
			// Use the Polynom constructor and builds a new Polynom, puts all the points in data
			DataTable data = new DataTable(Double.class, Double.class);
			Polynom PolynomNew=new Polynom(polynom);
			// Go over all the points in the Polynom and builds the plot
			for (double x = x1; x <= x2; x+=0.01) {
				double y = PolynomNew.f(x);
				data.add(x, y);
			}
            // Builds a new Polynom that equals to the derived Polynom
			Polynom DerivativePolynom=new Polynom();
			DerivativePolynom.add(PolynomNew.derivative());
			
			DataTable data2= new DataTable(Double.class, Double.class);
			
			// Search for min and max points in the derived Polynom, puts them in data2
			for (double x=x1; x<x2-0.015; x+=0.015) {
				double root=DerivativePolynom.root(x, x+0.015, 0.01);
					if ( (DerivativePolynom.f(root-0.01)>0 && DerivativePolynom.f(root+0.01)<0) || (DerivativePolynom.f(root-0.01)<0 && DerivativePolynom.f(root+0.01)>0) ) {
						data2.add(x,PolynomNew.f(x));
				}
			}
            // Creates a new plot that contain both datas
			XYPlot plot = new XYPlot(data, data2);

            // Prints the points to the screen
			getContentPane().add(new InteractivePanel(plot));
			LineRenderer lines = new DefaultLineRenderer2D();
			plot.setLineRenderers(data, lines);
			// Change the color of the graph
			Color color = new Color(0.0f, 0.3f, 1.0f);
			plot.getPointRenderers(data).get(0).setColor(color);
			plot.getLineRenderers(data).get(0).setColor(color);	

		}
	
	}
	/**
	 * The main method is used for build a new plot. 
	 * The user has to use frame.setVisible(true); in order to see the plot.
	 * @throws Exception if the user build a invalid Polynom or invalid range.
	 */
	public static void main(String[] args) throws Exception {
		// Example of building a new plot
		Polynom p=new Polynom("x^2+2");
		Plot frame = new Plot(p,-2,6);
		frame.setVisible(true);
	}


}