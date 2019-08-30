package part3;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class Perceptron {
    private static List<Image> allImages = new ArrayList<Image>();
    private static List<Features> randomFeatures = new ArrayList<Features>();
    private static double learn = 0.2;
    private static double correct;



    public static void main(String[] args) {

        File trainingFile = new File(args[0]);

        for(int q = 0; q < 10; q++ ){
            load(trainingFile);
            createRandomFeatures();
            for(Image i : allImages){
                for(Features f : randomFeatures){
                    checkConnections(i, f); //for each image pass in all feature values
                    double y = findingOutput(f); //this is where i find y
                    compare(i, y); //now i need to compare the y against the d
                }
                System.out.println((correct/new Double(allImages.size())) * 100 + " correct / " + 100);
                correct = 0;
            }
        }

    }

    public static double findingOutput(Features feat){
        //this is going to find y. which is the sum of wf for all features.
        double y = 0;
        y += (feat.getValue() * feat.getWeight());
        return y;
    }

    public static void compare(Image i, double y){
        //compare the y value to the image

        int DValue = 0;
        if(i.dValue.equals('X')){ DValue = 1;}
        //else the images d value was O therefore the dValue = 0;

        if(y >= 0){
            y = 1;
        }else{
            y = 0;
        }


        if(y == DValue){
            correct++;
        }else{
            for(Features ff : randomFeatures){
                double learnWeight = ff.getWeight() + (learn * (DValue - y) * ff.getValue());
                ff.setWeight(learnWeight);
            }
        }

    }
    //only when you have 3 or more connections is a feature value 1
    //only when a feature value is 1 can you multiple it with the weight
    //
    public static double checkConnections(Image image, Features feature){
        //here we need to determine the value of the feature.
        //check the row and cols of a feature
        //if that row and col is occupied by the image (1) then
        //if the value at the image matches the boolean of the featureValue
        //if there are 3 or more matches between in the image and the feature values
        //then the value of the featureValue is 1
        int trueCount = 0;

        if(feature.equals(randomFeatures.get(randomFeatures.size()-1))){//it is the dummy then set the value to 1
            feature.setValue(1.00);
        }

        for(int i = 0; i < feature.getRows().length; i++) { //repeat this 4 times
            if (image.pixels[feature.getRows()[i]][feature.getCols()[i]] == feature.getConnection()[i]) {
                //there is a connection
                trueCount++;
            }
        }

        if(trueCount >= 3){
            //yes there is a connection between the feature value and the image
            feature.setValue(1.0);
            return 1.0;
        }
        return 0.0;
    }

    public static void createRandomFeatures(){
        //creating 50 random features
        for(int i = 0; i < 50; i++){
            int rows[] = new int[4];
            int cols[] = new int[4];
            boolean bools[] = new boolean[4];

            for(int j = 0; j < 4; j++){ //creating the rows
                int rand = ThreadLocalRandom.current().nextInt(0, 10); //creating random numbers between 0 and 10
                int rand2 = ThreadLocalRandom.current().nextInt(0, 10); //creating random numbers between 0 and 10
                boolean randomBool = ThreadLocalRandom.current().nextBoolean();

                rows[j] = rand;
                cols[j] = rand2;
                bools[j] = randomBool;

            }
            double weight = ThreadLocalRandom.current().nextDouble(-1, 1);
            Features features = new Features(rows, cols, bools, weight);
            randomFeatures.add(features);
        }

        int rows[] = new int[4];
        int cols[] = new int[4];
        boolean bools[] = new boolean[4];
        double weight = ThreadLocalRandom.current().nextDouble(-1, 1);
        double dummy = 1.0;
        Features dummFeat = new Features(rows, cols, bools, weight, dummy);
        randomFeatures.add(dummFeat);
    }

    public static void load(File fileName) {

        try {
            java.util.regex.Pattern bit = java.util.regex.Pattern.compile("[01]"); //spliting on a 01
            Scanner f = new Scanner(fileName);

            while(f.hasNext()){ //while there is something in the image data file
                boolean[][] newimage = null; //image is an 2D array of booleans
                if (!f.next().equals("P1")) {
                    System.out.println("Not a P1 PBM file");
                    break;
                }
                String category = f.next().substring(1); //X or 0 without the #
                int rows = f.nextInt(); //get the next int
                int cols = f.nextInt(); //will be rows and cols

                newimage = new boolean[rows][cols]; //
                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < cols; c++) {
                        newimage[r][c] = (f.findWithinHorizon(bit, 0).equals("1")); //populate the image boolean where there is a 1
                    }
                }

                Image image = new Image(category,rows,cols,newimage); //create an image
                allImages.add(image); //add it too a list of images
            }
            f.close();
        } catch (IOException e) {
            System.out.println("Load from file failed");
        }
    }
}
