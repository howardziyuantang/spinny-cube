import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cube simulation");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);

        Cube cube = new Cube(300, 1);
        //for(Vertex v : cube.getVertices()) System.out.println(v.getX() + ", " + v.getY() + ", " + v.getZ());
        Panel panel = new Panel(cube);
        frame.add(panel);

//        Vertex test = new Vertex(new double[]{5, Math.PI/4, Math.PI/6}, true);
//        System.out.println(test.getX() + ", " + test.getY() + ", " + test.getZ());

    }

}
