import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;

public class Panel extends JPanel implements ActionListener, MouseInputListener {

    private Cube cube;
    private Dimension screenSize;
    private Timer timer;
    private long prevTime, threshold;
    private double prevX, prevY, centerX, centerY, size, avgR;
    private boolean released;

    public Panel(Cube cube) {
        super();
        this.cube = cube;
        this.size = cube.getSize()*1;
        //cube.rotate(1,Math.PI/6);
        //cube.rotate(2, Math.PI/3);
        setBackground(Color.BLACK);
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        centerX = screenSize.width/2d; centerY = screenSize.height/2d;
//        System.out.println("Center: " + centerX + ", " + centerY);
        timer = new Timer(10, this);
        timer.start();
        addMouseListener(this);
        addMouseMotionListener(this);
        prevTime = -1; prevX = -1; prevY = -1;
        threshold=100;
        avgR = (centerX+centerY)/4d+size/2d;
        released = true;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Polygon[] polys = getPolys(cube.getFaces());
        Color[] colors = new Color[]{Color.white, Color.red, Color.blue, Color.green, Color.yellow, Color.pink};
//        g.setColor(Color.white);
        int[] indices = cube.outermostFaces();
        for(int i = 0; i < 3; i++) {
            g.setColor(colors[indices[i]]);
            g.fillPolygon(polys[indices[i]]);
        }
//        g.setColor(Color.red);
//        g.drawOval((int)centerX-1,(int)centerY-1,2,2);
        g.setColor(new Color(255, 255, 255, 50));
        g.drawOval((int)(centerX-size), (int)(centerY-size), (int)(2*size), (int)(2*size));
    }

    private Polygon[] getPolys(Vertex[][] faces) {
        Polygon[] polys = new Polygon[6];
        for(int i = 0; i < 6; i++) {
            int[] xs = new int[4], ys = new int[4];
            for(int j = 0; j < 4; j++) {
                Vertex current = faces[i][j];
                xs[j] = convertY(current.getXYZ()[1]); ys[j] = convertZ(current.getXYZ()[2]);
            }
            polys[i] = new Polygon(xs, ys, 4);
        }
        return polys;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        cube.rotate(0, -Math.PI/100);
//        cube.rotate(1, -Math.PI/100);
//        cube.rotate(2, -Math.PI/100);
        cube.updatePos(timer.getDelay());
        repaint();
    }

    private int convertY(double y) {
        return (int) (y + screenSize.width / 2d);
    }

    private int convertZ(double z) {
        return (int) (screenSize.height / 2d - z);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        long currTime = System.currentTimeMillis(), timeDiff = currTime-prevTime;
        if(timeDiff<10) return;
        else if(timeDiff>threshold);
        else {
            double currX = e.getX(), currY = e.getY(), currA = getAngle(currX, currY), xDiff = currX-prevX, yDiff = currY-prevY;
            double[] vs = cube.getVelocities();
            if(inCircle(e,size)) {
                cube.setZVelocity(((released)?vs[2]:0)+xDiff/(timeDiff*avgR));
                cube.setYVelocity(((released)?vs[1]:0)+yDiff/(timeDiff*avgR));
            } else {
                double bX = currX-centerX, bY = currY-centerY, cX = -bY, cY = bX, vNX = (xDiff*bX+yDiff*bY)/(Math.sqrt(bX*bX+bY*bY)*timeDiff*avgR);
                cube.setXVelocity(((released)?vs[0]:0)-(xDiff*cX+yDiff*cY)/(Math.sqrt(cX*cX+cY*cY)*timeDiff*avgR));
                cube.setZVelocity(((released)?vs[2]:0)+vNX*Math.cos(getAngle(currX, currY)));
                cube.setYVelocity(((released)?vs[1]:0)-vNX*Math.sin(getAngle(currX, currY)));
            }
//            if(released) released=false;
        }
        initVals(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        initVals(e);
        //regular (getX, getY) matches panel, don't use onScreen (off by 22-23)
//        System.out.println("Regular: " + e.getX() + ", " + e.getY());
//        System.out.println("On Screen: " + e.getXOnScreen() + ", " + e.getYOnScreen());
//        System.out.println((getAngle(e.getX(),e.getY())*180/Math.PI));
    }

    private void initVals(MouseEvent e) {
        prevTime = System.currentTimeMillis();
        prevX = e.getX(); prevY = e.getY();
    }

    private boolean inCircle(MouseEvent e, double r) {
        return (Point.distance(e.getX(),e.getY(),centerX,centerY)<=r);
    }

    private double getAngle(double currX, double currY) {
        double angle;
        if(currX==centerX) angle = (currY>centerY)?-Math.PI/2:(currY<centerY)?Math.PI/2:0;
        else angle = Math.atan((centerY-currY)/(currX-centerX))+((currX<centerX)?Math.PI:0);
        if(angle<0) angle+=2*Math.PI;
        return angle;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
//        released=true;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
