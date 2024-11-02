import java.util.Arrays;

public class Cube {

    private Vertex[] vertices;
    private Vertex[][] faces;
    private double size, decel;
    private double[] velocities;

    public Cube(double size, double decel) {
        this.size = size;
        this.decel = decel;
        velocities = new double[3];
        initVertices();
    }

    private void initVertices() {
        vertices = new Vertex[8];
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 2; j++) {
                for(int k = 0; k < 2; k++) {
                    vertices[4*i+2*j+k] = new Vertex(new double[]{((i==0)?1:-1)*size/2, ((j==0)?1:-1)*size/2, ((k==0)?1:-1)*size/2});
                }
            }
        }
        initFaces();
    }

    private void initFaces() {
        faces = new Vertex[][]{{vertices[0],vertices[1],vertices[3],vertices[2]},
                {vertices[2],vertices[3],vertices[7],vertices[6]},
                {vertices[6],vertices[7],vertices[5],vertices[4]},
                {vertices[4],vertices[5],vertices[1],vertices[0]},
                {vertices[0],vertices[2],vertices[6],vertices[4]},
                {vertices[1],vertices[3],vertices[7],vertices[5]}};
    }

    public int[] outermostFaces() {
        double[][] topFaces = new double[3][2];
        for(int i = 0; i < 6; i++) {
            double sum = 0;
            for(int j = 0; j < 4; j++) sum += faces[i][j].getXYZ()[0];
            sum /= 4d;
            for(int j = 0; j < 3; j++) {
                if(sum>topFaces[j][1]) {
                    double[] prevTop = {i,sum};
                    do {
                        double[] temp=topFaces[j];
                        topFaces[j]=prevTop;
                        prevTop = temp;
                        j++;
                    } while(j<3);
                    break;
                }
            }
        }
        return new int[]{(int)topFaces[0][0],(int)topFaces[1][0],(int)topFaces[2][0]};
    }

    public void rotate(int axis, double theta) {
        double[][] matrix;
        switch(axis) {
            case 0:
                matrix = new double[][]{{1, 0, 0},
                        {0, Math.cos(theta), -Math.sin(theta)},
                        {0, Math.sin(theta), Math.cos(theta)}};
                break;
            case 1:
                matrix = new double[][]{{Math.cos(theta), 0, Math.sin(theta)},
                        {0, 1, 0},
                        {-Math.sin(theta), 0, Math.cos(theta)}};
                break;
            case 2:
                matrix = new double[][]{{Math.cos(theta), -Math.sin(theta), 0},
                        {Math.sin(theta), Math.cos(theta), 0},
                        {0, 0, 1}};
                break;
            default:
                matrix = new double[][]{{-1,-1,-1},{-1,-1,-1},{-1,-1,-1}};
        }
        for(Vertex v : vertices) multMatrix(v, matrix);
    }

    public void multMatrix(Vertex v, double[][] matrix) {
        double[] newV = new double[3];
        for(int i = 0; i<3; i++) {
            double sum = 0;
            for(int j = 0; j<3; j++) {
                sum += v.getXYZ()[j]*matrix[i][j];
            }
            newV[i] = sum;
        }
        v.setXYZ(newV);
    }

    public void updatePos(long t) {
        for(int i = 0; i < 3; i++) {
            rotate(i, velocities[i]*t);
            //System.out.println("Rotating Axis " + i + " " + (velocities[i]*t) + " radians");
            velocities[i]=(velocities[i]<0)?Math.min(velocities[i]+decel*t/100000,0):Math.max(velocities[i]-decel*t/100000,0);
        }
    }

    public double[] getVelocities() {return this.velocities;}

    public void setXVelocity(double xV) {velocities[0]=xV;}

    public void setYVelocity(double yV) {velocities[1]=yV;}

    public void setZVelocity(double zV) {velocities[2]=zV;}

    public Vertex[] getVertices() {return vertices;}

    public Vertex[][] getFaces() {return this.faces;}

    public double getSize() {
        return size;
    }
}
