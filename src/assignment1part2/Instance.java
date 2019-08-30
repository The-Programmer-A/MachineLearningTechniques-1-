package assignment1part2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static assignment1part2.DecisionTree.catNames;

public class Instance {

    protected double cat; // 0= live, 1= die
    protected List<Boolean> result; //this is the boolean values for all attributes

    public Instance(int cat, Scanner s) {
        this.cat = cat;
        this.result = new ArrayList<Boolean>();
        while (s.hasNextBoolean()) result.add(s.nextBoolean());
    }

    public boolean getAtt(int index) {return result.get(index);} //get the result at a spesific attribute index 0 - 15

    public double getCat(){ return cat;} //get the status - alive or dead of an instance

    @Override
    public  String toString(){ //this will return a line in the data
        StringBuilder ans = new StringBuilder(catNames.get((int)cat));
        ans.append(" ");
        for (Boolean res : result){ans.append(res?"true  ":"false ");}
        return ans.toString();
    }





}
