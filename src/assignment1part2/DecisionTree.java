package assignment1part2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DecisionTree {

    public static List<String> catNames; //alive or dead
    private static List<String> attNames; //16 attributes
    private static List<String> attNames2; //16 attributes
    private static Set<Instance> instances; //a node is a line of the data
    private static Set<Instance> testInstances; //a node is a line of the data
    private static int correctCount = 0;
    private static Node root;

    public static void main (String[] args) {

        File trainingFile = new File(args[0]);
        File testFile = new File(args[1]);
        readData(trainingFile);
        readData(testFile);

        attNames2 = new ArrayList<>(attNames);

        root = buildTree(instances, attNames);
        printTree(root);
        for(Instance in : testInstances){
            readTree(in, root);
        }
        System.out.println(correctCount / new Double(testInstances.size()-1) + " Accuracy");
    }

    private static void printTree(Node node) {
        node.reportD("  ");
    }

    public static void readTree(Instance testInts, Node root){
        String currentBestAtt = root.getBestAttribut();
        if(root.isIsleaf1()){
            String outcome = root.getCategory(); //the node you eneded up on was dead or alive
            if(testInts.getCat() == 0 && outcome.equals("alive")){ //the test instance matched the node for being alive
                //it was alive, you are correct
                correctCount++;
                return;
            }else if(testInts.getCat() == 1.0 && outcome.equals("dead")){
                //it was dead
                correctCount++;
                return;
            }
            return;

        }

        int index = attNames2.indexOf(currentBestAtt);

        if(testInts.getAtt(index)){ //if the attribute was true
            readTree(testInts, root.getLeft());
        }else{
            readTree(testInts, root.getRight());
        }
        //}
    }

    public static Node buildTree(Set<Instance> ints, List<String> attributes){

        if(ints.isEmpty()) { //i never get here
            int aliveCount = 0;
            int deadCount = 0;
            List<Instance> instancesList = new ArrayList<Instance>(DecisionTree.instances); //going through the entire instances
            double prob = 0;
            //find which one is most occuring, alive or dead
            for(int i = 0; i < instancesList.size(); i++){//run through all ints
                if(instancesList.get(i).getCat() == 1){
                    aliveCount++;
                }else if(instancesList.get(i).getCat() == 0){
                    deadCount++;
                }
            }

            if(deadCount == aliveCount){
                prob = deadCount / ints.size();
                return new Node("dead", prob);
            }

            if(aliveCount > deadCount){
                prob = aliveCount / ints.size();
                return new Node("alive", prob);
            }else {
                prob = deadCount / ints.size();
                return new Node("dead", prob);
            }
        }

        if(findPure(ints)){ //the set of ints are pure
            String cat = "";
            List<Instance> instanceList = new ArrayList<Instance>(ints);

            if(instanceList.get(0).getCat() == 0.0){
                cat="alive";
            }else{
                cat="dead";
            }
            //dependent on what they are create a lead node
            Node leafNode = new Node(cat, 1.0 );
            return leafNode;
        }
        if(attributes.isEmpty()){ //it never gets here
            double aliveCount = 0;
            double deadCount = 0;
            double prob = 0;

            List<Instance> instanceList = new ArrayList<Instance>(ints);
            for(int i = 0; i < ints.size(); i++){
                if(instanceList.get(i).getCat() == 0){
                    aliveCount++;
                }else{
                    deadCount++;
                }
            }
            if(deadCount == aliveCount){
                prob = deadCount / ints.size();
                return new Node("dead", prob);
            }

            if(aliveCount > deadCount){
                prob = aliveCount / ints.size();
                return new Node("alive", prob);
            }else{
                prob = deadCount / ints.size();
                return new Node("dead", prob);
            }

        }
        double bestImpurity = 1.0;
        String bestAtt = "";
        Set<Instance> bestInstaTrue = new HashSet<>(); //these are going the be passed in for the recursion
        Set<Instance> bestInstaFalse = new HashSet<>();
        //finding the best attribute
        for (String s : attributes) {
            //separate ints into 2 sets
            Set<Instance> trueInstance = new HashSet<Instance>(); //maps the attribute to a true instance
            Set<Instance> falseInstance = new HashSet<Instance>(); //maps the attribute to a true instance
            for (Instance n : ints) { //all the ints
                if (n.getAtt(attNames.indexOf(s))) {
                    trueInstance.add(n);
                } else { //false
                    falseInstance.add(n);
                }
            }
            //computing purity
            double tImpurity = calculateSinglePurity(trueInstance);
            double fImpurity = calculateSinglePurity(falseInstance);

            //calculate the weight
            double trueSize = new Double(trueInstance.size());
            double falseSize = new Double(falseInstance.size());
            double totalTFSize = new Double(ints.size());
            double weightTrue = trueSize / totalTFSize;
            double weightFalse = falseSize / totalTFSize;

            double totalTrueImpuirty = weightTrue * tImpurity;
            double totalFalseImpuirty = weightFalse * fImpurity;
            double weightedAverageImpurity = totalTrueImpuirty + totalFalseImpuirty;
            if (weightedAverageImpurity < bestImpurity) { //setting the bestWAI
                bestAtt = s;
                bestImpurity = weightedAverageImpurity;
                bestInstaTrue = trueInstance; //pass this into the left
                bestInstaFalse = falseInstance; //pass this into the right
            }

        }
        //that is going to give me the node i want to create
        List<String> newAttNamesT = new ArrayList<String>();
        List<String> newAttNamesF = new ArrayList<String>();
        attributes.remove(attributes.indexOf(bestAtt));

        for(String att : attributes){
            newAttNamesT.add(att);
            newAttNamesF.add(att);
        }

        Node left = buildTree(bestInstaTrue, newAttNamesT);
        Node right = buildTree(bestInstaFalse, newAttNamesF);
        return new Node(bestAtt, left, right);
    }

    private static boolean findPure(Set<Instance> instances) {
        List<Instance> list = new ArrayList<>(instances);
        for(int i = 0; i < list.size()-1;i++){
            if(list.get(i).getCat() != list.get(i+1).getCat()){
                return false;
            }
        }
        return true;
    }

    public static double calculateSinglePurity(Set<Instance> tf){
        //find the number of the given that were alive or dead.
        //that will be your m and you n
        //find your total impurity

        double m = 0;
        double n = 0;
        double total = new Double(tf.size());
        for(Instance tfs : tf){
            if(tfs.getCat() == 0){ //it was alive
                m++;
            }else{ //it was dead
                n++;
            }
        }
        double impurity = (m*n)/(Math.pow((m+n), 2.0));
        return impurity;

    }

    private static void readData(File tf) {
        Scanner scan;

        try{
            scan = new Scanner(tf); //reading the file
            catNames = new ArrayList<String>();
            for(Scanner s = new Scanner(scan.nextLine()); s.hasNext();) catNames.add(s.next());
            int numCat = catNames.size();
            System.out.println(numCat + "Categories");


            attNames = new ArrayList<String>();
            for(Scanner s = new Scanner(scan.nextLine()); s.hasNext();) attNames.add(s.next());
            int numAtts = attNames.size();
            System.out.println(numAtts + "attributes");

            if(tf.getName().equals("hepatitis-test.dat")){
                testInstances = readInstances(scan);
                return;
            }

            instances = readInstances(scan);


        }catch(FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static Set<Instance> readInstances(Scanner scan) {
        Set<Instance> instances = new HashSet<Instance>();
        String ln;
        while(scan.hasNext()){
            Scanner line = new Scanner(scan.nextLine()); //this is going through all the boolean values for each attribute
            instances.add(new Instance(catNames.indexOf(line.next()), line));
        }
        System.out.println("Read " + instances.size()+" instances");
        return instances;
    }
}