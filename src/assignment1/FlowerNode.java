package assignment1;

public class FlowerNode {

    private final double sLength;
    private final double sWidth;
    private final double pLength;
    private final double pWidth;
    private final String flower;
    private String prediction;

    /**
     *
     * @param sLength
     * @param sWidth
     * @param pLength
     * @param pWidth
     * @param flower
     */

    public FlowerNode(double sLength, double sWidth, double pLength, double pWidth, String flower) {
        this.sLength = sLength;
        this.sWidth = sWidth;
        this.pLength = pLength;
        this.pWidth = pWidth;
        this.flower = flower;
    }

    public double getsLength() {
        return sLength;
    }

    public double getsWidth() {
        return sWidth;
    }

    public double getpLength() {
        return pLength;
    }

    public double getpWidth() {
        return pWidth;
    }

    public String getFlower() {
        return flower;
    }

    public void setPrediction(String flowerName){
        this.prediction = flowerName;
    }

    public String getPrdiction(){
        return prediction;
    }
    @Override
    public String toString(){
        String s = this.sLength + "";
        String s1 = this.sWidth + "";
        String s2 = this.pLength + "";
        String s3 = this.pWidth + "";

        String sCat = s + " sLenght" + s1 +" sw" + s2 + " pl" + s3 + " pw" + getFlower();
        return sCat;
    }
}
