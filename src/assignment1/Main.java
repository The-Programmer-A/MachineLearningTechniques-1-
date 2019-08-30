package assignment1;

import java.io.*;
import java.util.*;

public class Main {


    private static List<FlowerNode> trainingNodes;
    private static List<FlowerNode> testNodes;


    //private static PriorityQueue<Double> nodeDistances = new PriorityQueue<Double>();

    private static double sLengthRange;
    private static double sWidthRange;
    private static double pLengthRange;
    private static double pWidthRange;


    public static void main(String[] args) {
	// write your code here
        if(args[0] == null || args[1] == null || args.length != 2){
            throw new IllegalArgumentException("please enter a training file and a test file");
        }

        File trainingFile = new File(args[0]);
        File testFile = new File(args[1]);
        trainingNodes = readNodes(trainingFile);
        testNodes = readNodes(testFile);
        int k = 1;

        KNearestNeighbour(k);
    }

    public static void KNearestNeighbour(int k){



        int rightCount = 0;

        for(int i = 0; i < testNodes.size(); i++) {
            //new training node that were calculating against
            //ArrayList<FlowerNode> kNeighbours = new ArrayList<>();
            int countSentosa = 0;
            int countVersicolor = 0;
            int countVirginica = 0;
            int kNCount = 0;
            double nearestKN = 0;
            FlowerNode f = null;

            TreeMap<Double, FlowerNode> nodeDistances = new TreeMap<Double, FlowerNode>(); //sort on natural order of the key

            for (int j = 0; j < trainingNodes.size(); j++) {
                double distance = calcDistance(testNodes.get(i), trainingNodes.get(j)); //this will get the dist from testN to all trainN
                nodeDistances.put(distance, trainingNodes.get(j)); //put the distance and the associated training node into the map
                //the map should sort by natural order of keys
            }
            //get the top k values from the map
            //find the common occuring
            //set the test node to be the common occuring
            while (kNCount < k) {

                nearestKN = nodeDistances.firstKey();
                //kNeighbours.add(nodeDistances.get(nearestKN));
                f = nodeDistances.get(nearestKN);
                if (f.getFlower().equals("Iris-setosa")) {
                    countSentosa++;
                } else if (f.getFlower().equals("Iris-versicolor")) {
                    countVersicolor++;
                } else if (f.getFlower().equals("Iris-virginica")) {
                    countVirginica++;
                }
                nodeDistances.remove(nearestKN);
                kNCount++;
            }

            if (countSentosa > countVersicolor && countSentosa > countVirginica) {
                testNodes.get(i).setPrediction("Iris-setosa");
            } else if (countVersicolor > countSentosa && countVersicolor > countVirginica) {
                testNodes.get(i).setPrediction("Iris-versicolor");
            } else if (countVirginica > countSentosa && countVirginica > countVersicolor) {
                testNodes.get(i).setPrediction("Iris-virginica");
            } else {
                //there is no distinct winner. in that case make it the closest kNeighbour
                testNodes.get(i).setPrediction(f.getFlower());
            }

        }
        //now test id you got it right
        for(int i = 0; i < testNodes.size(); i++){
            if(testNodes.get(i).getFlower().equals(testNodes.get(i).getPrdiction())){
                System.out.println(testNodes.get(i).getPrdiction() + "\t"+ "\t" +testNodes.get(i).getFlower() + "\t" + "\tMatch");
                rightCount++;

            }else{
                System.out.println(testNodes.get(i).getPrdiction() + "\t"+ "\t" +testNodes.get(i).getFlower() + "\t" + "\tNo Match");
            }

        }
        System.out.println(rightCount + " Matches");
    }

    public static double calcDistance(FlowerNode train, FlowerNode test){
        double sLCalc = Math.pow(train.getsLength() - test.getsLength(),2)/Math.pow(sLengthRange,2);
        double sWCalc = Math.pow(train.getsWidth() - test.getsWidth(),2)/Math.pow(sWidthRange,2);
        double pLCalc = Math.pow(train.getpLength() - test.getpLength(),2)/Math.pow(pLengthRange,2);
        double pWCalc = Math.pow(train.getpWidth() - test.getpWidth(),2)/Math.pow(pWidthRange,2);
        return Math.sqrt(sLCalc + sWCalc + pLCalc + pWCalc);
    }


    private static List readNodes(File tf){

        BufferedReader br;
        List<FlowerNode> ls = new ArrayList<>();

        double minSLength = Double.MAX_VALUE;
        double maxSLength = Double.MIN_VALUE;

        double minSWidth = Double.MAX_VALUE;
        double maxSWidth = Double.MIN_VALUE;

        double minPLength = Double.MAX_VALUE;
        double maxPLength = Double.MIN_VALUE;

        double minPWidth = Double.MAX_VALUE;
        double maxPWidth = Double.MIN_VALUE;

        try{
            br = new BufferedReader(new FileReader(tf)); //reading the file
            String line;
            while ((line = br.readLine()) != null) { //get the lines

                String[] tokens = line.split("  "); //split the lines by spaces
                 //I HAVE A FEELING THAT THERE IS SOMETHING WRONG HERE
                if(tokens[0].isEmpty()) {break;}

                double sLength = Double.parseDouble(tokens[0]);
                if(sLength < minSLength){minSLength = sLength;}
                if(sLength > maxSLength){maxSLength = sLength;}


                double sWidth = Double.parseDouble(tokens[1]);
                if(sWidth < minSWidth){minSWidth = sWidth;}
                if(sWidth > maxSWidth){maxSWidth = sWidth;}

                double pLength = Double.parseDouble(tokens[2]);
                if(pLength < minPLength){minPLength = pLength;}
                if(pLength > maxPLength){maxPLength = pLength;}

                double pWidth = Double.parseDouble(tokens[3]);
                if(pWidth < minPWidth){minPWidth = pWidth;}
                if(pLength > maxPLength){maxPWidth = pWidth;}

                String flower = tokens[4];

                FlowerNode fl = new FlowerNode(sLength, sWidth, pLength, pWidth, flower);
                ls.add(fl);
            }

        }catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sLengthRange = maxSLength - minSLength;
        sWidthRange = maxSWidth - minSWidth;
        pLengthRange = maxPLength - minPLength;
        pWidthRange = maxPWidth - minPWidth;

        return ls;
    }


}
