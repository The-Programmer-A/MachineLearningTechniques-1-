package part3;
public class Features {
    private int rows[];
    private int cols[];
    private boolean connection[];
    double weight = 0;

    double value = 0;

    double dummy;

    public Features(int[] rows, int[] cols, boolean[] connection, double weight) {
        this.rows = rows;
        this.cols = cols;
        this.connection = connection;
        this.weight = weight;
    }

    public Features(int[] rows, int[] cols, boolean[] connection, double weight, double dummy) {
        this.rows = rows;
        this.cols = cols;
        this.connection = connection;
        this.weight = weight;
        this.dummy = dummy;
    }

    public int[] getRows() {
        return rows;
    }

    public int[] getCols() {
        return cols;
    }

    public boolean[] getConnection() {
        return connection;
    }

    public double getWeight() {
        return weight;
    }

    public double getDummy() {
        return dummy;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
