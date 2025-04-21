package game;

import java.util.ArrayList;
import java.util.Collection;

public class Connect4Node {
    private final Connect4State state;
    private final ArrayList<Connect4Node> children;
    private int val;
    private int vis;
    private final double C = 1.42;

    public boolean isLeaf() {
        return state().isTerminal();
    }
    
    public boolean winner() {
        return state.winner().isPresent();
    }
    
    public Connect4State state() {
        return state;
    }

    public boolean black() {
        return state.player() == state.game().opener();
    }

    public Collection<Connect4Node> children() {
        return children;
    }

    public void addChild(Connect4State state) {
        children.add(new Connect4Node(state));
    }
    
    public Connect4Node unvisited(){
        for (Connect4Node child : children) {
            if (child.vis() == 0) return child;
        }
        return null;
    }
    
    public Connect4Node getChild(int i){
        return children.get(i);
    }
    
    public void setUpdateValue(int updateValue) {
        vis++;
        val += updateValue;
    }
    
    public int val() {
        return val;
    }

    public int vis() {
        return vis;
    }

    public Connect4Node(Connect4State state) {
        this.state = state;
        children = new ArrayList<>();
        initializeNodeData();
    }

    public boolean fullyExpanded(){
        return children.size()<vis;
    }
    
    public void explore() {
        Collection<Connect4Move> possibleMoves = state.moves(state.player());
        for (Connect4Move move : possibleMoves) {
            Connect4State nextState = state.next(move);
            addChild(nextState);
        }
    }
    
    public Connect4Node childWithHighestUCT(){
        Connect4Node ret=null;

        double highestUCT = Double.NEGATIVE_INFINITY;
        for(Connect4Node child : children){
            double uct = (double)child.val() / child.vis() + C * Math.sqrt(Math.log(vis)/child.vis());
            if(uct>highestUCT){
                highestUCT = uct;
                ret = (Connect4Node)child;
            }
        }
        if(ret!=null) return ret;
        System.out.println("children:");
        for(Connect4Node child : children){
            System.out.println("Child vis:"+child.vis()+"Child val:"+child.val());
            System.out.println(child.state());
        }
        return null;
    }

    private void initializeNodeData() {
        vis = val = 0;
    }
    
    public void initializeRoot(){
        vis = 1;
    }
}