package assignment1part2;

public class Node {

    private boolean isLeaf1 = false;
    private String bestAttribut;
    private Node left;
    private Node right;

    private String cat;
    private double prob;



    public Node(String cat, double prob) {
        this.cat = cat;
        this.prob = prob;
        this.isLeaf1 = true;
    }

    public Node(String bestAttribut, Node left, Node right) {
        this.bestAttribut = bestAttribut;
        this.left = left;
        this.right = right;
    }

    public String getBestAttribut() {
        return bestAttribut;
    }

    public boolean isIsleaf1() {
        if(this.isLeaf1){
            return true;
        }
        return false;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public String getCategory() {
        return cat;
    }

    public double getProb() {
        return prob;
    }

    public boolean isLeaf(){
        if(this.getCategory() != null && this.getProb() > 0){
            return true;
        }
        return false;
    }

    public void reportLeaf(String indent){
        System.out.format("%sClass %s, prob=%4.2f\n",
                indent, cat, prob);
        return;
    }

    public void reportD(String indent){
        if(getBestAttribut() == null){reportLeaf("    ");}
        if(getBestAttribut() != null){ System.out.format("%s%s = True:\n", indent, bestAttribut);}

        if(getLeft() != null){
            getLeft().reportD(indent+"  ");
        }
        if(bestAttribut != null){
            System.out.format("%s%s = False:\n", indent, bestAttribut);
        }
        if(getRight() != null){getRight().reportD(indent+"  ");}
    }

}
