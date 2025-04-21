package game;

import java.util.ArrayList;
import java.util.Collection;

public class TicTacToeNode {

	/**
     * @return true if this node is a leaf node (in which case no further exploration is possible).
     */
    public boolean isLeaf() {
        return state().isTerminal();
    }
    
    public boolean winner() {
        return state.winner().isPresent();
    }
    
    /**
     * @return the State of the Game G that this Node represents.
     */
    public TicTacToeState state() {
        return state;
    }

    /**
     * Method to determine if the player who plays to this node is the opening player (by analogy with chess).
     * For this method, we assume that O goes first so is "black."
     * NOTE: this assumes a two-player game.
     *
     * @return true if this node represents a "white" move; false for "black."
     */
    public boolean black() {
        return state.player() == state.game().opener();
    }

    /**
     * @return the children of this Node.
     */
    public Collection<TicTacToeNode> children() {
        return children;
    }

    /**
     * Method to add a child to this Node.
     *
     * @param state the State for the new chile.
     */
    public void addChild(TicTacToeState state) {
        children.add(new TicTacToeNode(state));
    }
    
    public TicTacToeNode unvisited() {
        for (TicTacToeNode child : children) {
            if (child.vis() == 0) return child;
        }
        return null;
    }
    
    public TicTacToeNode getChild(int i) {
        return children.get(i);
    }
    
    /**
     * This method update the number of wins and playouts according to the children states.
     */
    public void setUpdateValue(int updateValue) {
        vis++;
        val += updateValue;
    }
    
    /**
     * @return the score for this Node and its descendents a win is worth 2 points, a draw is worth 1 point.
     */
    public int val() {
        return val;
    }

    /**
     * @return the number of playouts evaluated (including this node). A leaf node will have a playouts value of 1.
     */
    public int vis() {
        return vis;
    }

    public TicTacToeNode(TicTacToeState state) {
        this.state = state;
        children = new ArrayList<>();
        initializeNodeData();
    }

    public boolean fullyExpanded(){
        return children.size()<vis;
    }
    
    public void explore() {
        Collection<TicTacToeMove> possibleMoves = state.moves(state.player());
        for (TicTacToeMove move : possibleMoves) {
            TicTacToeState nextState = state.next(move);
            addChild(nextState);
        }
    }
    
    public TicTacToeNode childWithHighestUCT(){
        TicTacToeNode ret=null;

        double highestUCT = Double.NEGATIVE_INFINITY;
        for(TicTacToeNode child : children){

            double uct = (double)child.val() / child.vis() +
                    C * Math.sqrt(Math.log(vis)/child.vis());

            if(uct>highestUCT){
                highestUCT = uct;
                ret = (TicTacToeNode)child;
            }
        }
        return ret;

    }

    private void initializeNodeData() {
        vis = val = 0;
    }
    
    public void initializeRoot() {
        vis = 1;
    }
    
    private final TicTacToeState state;
    private final ArrayList<TicTacToeNode> children;
    private int val;
    private int vis;
    private final double C = 1.42;
}