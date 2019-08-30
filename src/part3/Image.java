package part3;

public class Image { //this is going to

    String dValue;
    int row;
    int cols;
    boolean pixels[][];

    public Image(String dValue, int row, int cols, boolean[][] pixels) {
        this.dValue = dValue;
        this.row = row;
        this.cols = cols;
        this.pixels = pixels;
    }


}
