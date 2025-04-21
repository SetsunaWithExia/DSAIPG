package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MCTS {
    private final Connect4Node root;
    private final int resource;

    public MCTS(Connect4Node root, int resource) {
        this.root = root;
        root.initializeRoot();
        this.resource = resource;
    }
    
    public Connect4Move getBestMove() {
        int remain = resource;
        while (remain > 0) {
            traverse(root);
            remain--;
        }

        Connect4Node bestChild = root.childWithHighestUCT();
        if (bestChild == null) {
            root.explore();
            bestChild = root.childWithHighestUCT();
            if (bestChild == null) {
                Collection<Connect4Move> moves = root.state().moves(root.state().player());
                List<Connect4Move> moveList = new ArrayList<>(moves);
                if (!moveList.isEmpty()) {
                    return moveList.get(0);
                }
                throw new RuntimeException("No valid moves available");
            }
        }
        
        Connect4State childState = bestChild.state();
        int nextStep = Connect4Position.Getmove(root.state().position(), childState.position());
        return new Connect4Move(root.state().player(), nextStep);
    }
    
    public Connect4State traverse(Connect4Node cur) {
        if (cur.isLeaf()) {
            return visit(cur);
        }
        
        if (cur.children().isEmpty()) {
            cur.explore();
        }
        
        if (cur.fullyExpanded()) {
            Connect4Node bestChild = cur.childWithHighestUCT();
            if (bestChild != null) {
                Connect4State leaf = traverse(bestChild);
                backPropagate(cur, leaf);
                return leaf;
            } else {
                return cur.state();
            }
        } else {
            Connect4Node unvisitedChild = cur.unvisited();
            if (unvisitedChild != null) {
                Connect4State leaf = visit(unvisitedChild);
                backPropagate(cur, leaf);
                return leaf;
            } else {
                throw new RuntimeException("Unvisited child Not Found");
            }
        }
    }
    
    public Connect4State visit(Connect4Node cur) {
        Connect4State leaf = simulate(cur);
        backPropagate(cur, leaf);
        return leaf;
    }
    
    public Connect4State simulate(Connect4Node cur) {
        Connect4State state = new Connect4State(cur.state().game(), cur.state().random(), cur.state().position());
        int player = cur.state().player();
        
        while (!state.isTerminal()) {
            Connect4Move randomMove = state.chooseMove(player);
            state = state.next(randomMove);
            player = player == Connect4.X ? Connect4.O : Connect4.X;
        }
        
        return state;
    }
    
    public void backPropagate(Connect4Node ancestorNode, Connect4State leafState) {
        int leafWin = leafState.winner().isPresent() ? 1 : 0;
        if (ancestorNode.state().player() != leafState.player()) {
            ancestorNode.setUpdateValue(-leafWin);
        } else {
            ancestorNode.setUpdateValue(leafWin);
        }
    }
}