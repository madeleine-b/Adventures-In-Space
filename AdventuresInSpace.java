import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class AdventuresInSpace extends JFrame {
	
 	public static void main (String[] args){
		 JFrame frame = new JFrame();
		 
		 frame.setTitle("Adventures in Space");	
		 frame.setSize(700, 700);	
		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		 frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		 SpaceScene s = new SpaceScene ();
		 
		 Box box = Box.createHorizontalBox();
		 
		 JButton yellow = new JButton("Yellow");
		 JButton purple = new JButton("Purple");
		 JButton blue = new JButton("  Blue  "); //spaces needed for centering in box
		 
		 yellow.setAlignmentX(Component.RIGHT_ALIGNMENT);
		 blue.setAlignmentX(Component.RIGHT_ALIGNMENT);
		 purple.setAlignmentX(Component.RIGHT_ALIGNMENT);
		 
		 purple.addActionListener(new PButtonListener());
		 yellow.addActionListener(new YButtonListener());
		 blue.addActionListener(new BButtonListener());
		 
		 box.add(purple);
		 box.add(yellow);	
		 box.add(blue);
		 
		 frame.add(s);
		 
		 frame.add(box);
		 	
		 frame.setVisible(true);	

	 }	
	 	
	 static class PButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			//System.out.println("PURPLE BUTTON CLICKED");
			SpaceScene.cc=2;
		}
	}
	
	static class YButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			//System.out.println("YELLOW BUTTON CLICKED");
			SpaceScene.cc=1;
		}
	}
	
	static class BButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//System.out.println("BLUE BUTTON CLICKED");
			SpaceScene.cc=0;
		}
	}

}

 @SuppressWarnings("serial")
class SpaceScene extends JComponent {
	 int gsize = 700;
	 double cometX = 0;
	 double cometY = gsize;
	 double scale = 1;
	 double t = 0;
	 boolean stop = false; //has the user clicked the screen, indicating they want to pause the program?
  	 int cometStep = 0;
  	 static int cc = 2;
	 double[] pathPointsX = new double[500];
	 double[] pathPointsY = new double[500];
		
  	TimerListener tl = new TimerListener();
	Timer timer = new Timer(100, tl);
	MyMouseListener m = new MyMouseListener();
		
		
	protected void paintComponent(Graphics g){
		timer.start();
		this.addMouseListener(m);
	
		t=0;
		
		/*taken from a parametric equation for Bezier curves
		 * I created the curve that fit the right path for the comet and then made two arrays
		 * for storing X and Y values for a bunch (500) of different "t" values.
		 * we then iterate through these to determine where the comet should move.
		 */
		for (int i=0; i<pathPointsX.length; i++) {
			pathPointsX[i] = (1-t)*((1-t)*5+t*(0.614*gsize+25))+t*((1-t)*(0.614*gsize+25)+t*(0.614*gsize+8));
			pathPointsY[i] = (1-t)*((1-t)*(gsize-5)+t*(3*(0.171*gsize+0.8+8.5+gsize)/4))+t*((1-t)*(3*(0.171*gsize+0.8+8.5+gsize)/4)+t*(0.171*gsize+0.8+8.5));
			
			//since there are 500 pts, we want t+=1/500 (0.002)
			t+=0.000999*2; //I forget why, but I had to do 0.00999*2. a rounding thing, maybe
		}
		
		if (cometStep>=pathPointsX.length) { //comet has reached end of path, restart at beginning
			cometStep=0;
			cometX=0;
			cometY=gsize;
			scale=1;
		}
		
		
		cometX=pathPointsX[cometStep];
		cometY=pathPointsY[cometStep];
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, gsize, gsize); //the sky
		
		
		
		for (int i=0; i<6; i++) { //large, sparse stars
			int q = 248+(int)(Math.random()*8);
			g2.setColor(new Color(q, q, q, 245 + (int)(Math.random()*11)));
			double size =  (Math.random()*11)+0.5;
			int col = (int) (Math.random()*50);
			if (col>=40 && col<45) {
				size=(Math.random()*17)+0.5;
			}
			double xCoord = (Math.random()*gsize*0.96)+10;
			double yCoord =  (Math.random()*gsize*0.96)+10;
			Shape star1 = new Ellipse2D.Double(xCoord, yCoord, size, size);
			g2.fill(star1);
		}
		
		for (int i=0; i<20; i++) { //small bright stars
			int q = 250+(int)(Math.random()*6);
			int col = (int) (Math.random()*30)+1; 
			g2.setColor(new Color(q, q, q, 240 + (int)(Math.random()*16)));
			if (col==19 || col==1) {
				g2.setColor(new Color(211, 181, 244, 230)); //light purple, opaque
			}
			if (col==14) {
				g2.setColor(new Color(205, 84, 47, 240)); //red-orange
			}
			if (col==4 || col==23) {
				g2.setColor(new Color(255,230, 69));
			}
			double size = (Math.random()*8)+0.4;
			double xCoord = (Math.random()*gsize*0.56)+(gsize*0.02);
			double yCoord = (Math.random()*gsize*0.64)+(gsize*0.3);
			Shape star1 = new Ellipse2D.Double(xCoord, yCoord, size, size);
			g2.fill(star1);
		}
		
		for (int i=0; i<78; i++) { //small white stars
			int q = 250+(int)(Math.random()*6);
			int col = (int) (Math.random()*25)+1; 
			g2.setColor(new Color(q, q, q, 240 + (int)(Math.random()*16)));
			
			double size = (Math.random()*4)+0.75;
			
			if (col==16 || col==1 || col==3) {
				g2.setColor(new Color(181, 244, 216, 250)); //mint green
				size=1.5;
			}
			
			double xCoord = (Math.random()*gsize*0.56);
			double yCoord = (Math.random()*gsize*0.64);
			Shape star1 = new Ellipse2D.Double(xCoord, yCoord, size, size);
			g2.fill(star1);
		}
		
		for (int i=0; i<98; i++) { //more small white stars; lower right
			int q = 250+(int)(Math.random()*6);
			int col = (int) (Math.random()*25)+1; 
			g2.setColor(new Color(q, q, q, 240 + (int)(Math.random()*16)));
			
			double size = (Math.random()*3.3)+0.25;
			
			if (col>16) {
				g2.setColor(new Color(181, 200, 244, 240)); //light blueish 
			}
			
			double xCoord = (Math.random()*gsize*0.56)+(gsize*0.44);
			double yCoord = (Math.random()*gsize);
			Shape star1 = new Ellipse2D.Double(xCoord, yCoord, size, size);
			g2.fill(star1);
		} 
			
		g2.setColor(Color.WHITE);
		Shape star2 = new Ellipse2D.Double(0.071*gsize, 0.1*gsize, 10, 10);
		g2.fill(star2);
		
		Color methane = new Color(76, 205, 208, 230); //blue planet
		g2.setColor(methane);
		Shape planet = new Ellipse2D.Double(0.614*gsize, 0.171*gsize, 16, 17);
		g2.fill(planet); 
		
		//Color iceBlue = new Color(193, 206, 237);
		Color iceBlue = new Color(171, 244, 255, 240);
		Color tail = new Color(212, 249, 255, 170); //gray blue
			
		Color yellowOrange = new Color(255, 222, 33);
		Color otail = new Color(255, 199, 110, 170);
			
		Color purpblue = new Color(36, 89, 212);
		Color ptail = new Color(126, 108, 196, 170);
		
		Color[] cometColors = {iceBlue, yellowOrange, purpblue};
		Color[] tailColors = {tail, otail, ptail};
		
		cometX=0;
		cometY=0;
		g2.setColor(cometColors[cc]);
		Path2D comet = new Path2D.Double();
		comet.moveTo(cometX, cometY);
		//            control 1x, control 1y, control 2x, control 2y, intersect x, intersect y
		comet.curveTo(33+cometX, -20+cometY, 35+cometX, 18+cometY, 10+cometX, 21+cometY);
		comet.curveTo(8+cometX, 26+cometY, -34+cometX, 44+cometY, -40+cometX, 44+cometY);
		comet.moveTo(cometX, cometY);
		comet.curveTo(-5+cometX, -2+cometY, -20+cometX, 5+cometY, -50+cometX, 23+cometY);
		comet.curveTo(-107+cometX, 48+cometY, -97+cometX, 65+cometY, -40+cometX, 44+cometY);
					
		g2.translate(pathPointsX[cometStep], pathPointsY[cometStep]);
		g2.rotate(.4); //get it facing at 0¼
		
		double fakeTan; //shh my variable names are clever
		if (cometStep==pathPointsX.length-1) {
			//previous value
			fakeTan=(pathPointsY[cometStep-1]-pathPointsY[cometStep])/(pathPointsX[cometStep]-pathPointsX[cometStep-1]); 
			fakeTan=fakeTan+5; //make it a little less negative
			fakeTan*=-1;
		}
		else {
			fakeTan = (pathPointsY[cometStep]-pathPointsY[cometStep+1])/(pathPointsX[cometStep+1]-pathPointsX[cometStep]);
		}
		
		if (fakeTan<0 && cometStep!=pathPointsX.length-1) { //so it's rounding back around the curve
			fakeTan=(pathPointsY[cometStep]-pathPointsY[cometStep+1])/(pathPointsX[cometStep]-pathPointsX[cometStep+1]);	
		}
		
		g2.rotate(-Math.atan(fakeTan));

		
		GradientPaint cometColor = new GradientPaint(26+(int)cometX, -5+(int)cometY, cometColors[cc], -40+(int)cometX, 30+(int)cometY, tailColors[cc]);
		g2.setPaint(cometColor); 
		
		g2.scale(scale, scale);
		g2.draw(comet);
		g2.fill(comet);	
		
	}
	
	class TimerListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if (!stop) {
				cometStep++;
				scale-=0.00189;
				repaint();
				
				scale-=0.00189;
				cometStep++;
				repaint();
			}
 		}
	}
	
	class MyMouseListener extends MouseAdapter {

		 public void mouseClicked(MouseEvent e) { //if (stop), pauses the program until the user clicks again
			 stop = !stop;
		 }

	}
	

	
}
