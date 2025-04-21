package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MCTS {

    public MCTS(TicTacToeNode root, int resource) {
        this.root = root;
        root.initializeRoot();
        this.resource = resource;
    }
    
    public TicTacToeMove getBestMove() {
        int remain = resource;
        while (remain > 0) {
            traverse(root);
            remain--;
        }

        TicTacToeNode bestChild = root.childWithHighestUCT();
        if (bestChild == null) {
            root.explore();
            bestChild = root.childWithHighestUCT();
            if (bestChild == null) {
                Collection<TicTacToeMove> moves = root.state().moves(root.state().player());
                List<TicTacToeMove> moveList = new ArrayList<>(moves);
                if (!moveList.isEmpty()) {
                    return moveList.get(0);
                }
                throw new RuntimeException("No valid moves available");
            }
        }
        
        TicTacToeState childState = bestChild.state();
        int[] nextStep = Position.Getmove(root.state().position(), childState.position());
        return new TicTacToeMove(root.state().player(), nextStep[0], nextStep[1]);
    }
    
    public TicTacToeState traverse(TicTacToeNode cur) {
        if (cur.isLeaf()) {
            return visit(cur);
        }
        if (cur.children().isEmpty()) {
            cur.explore();
        }
        
        if (cur.fullyExpanded()) {
            TicTacToeNode bestChild = cur.childWithHighestUCT();
            if (bestChild != null) {
                TicTacToeState leaf = traverse(bestChild);
                backPropagate(cur, leaf);
                return leaf;
            } else {
                return cur.state();
            }
        } else {
            TicTacToeNode unvisitedChild = cur.unvisited();
            if (unvisitedChild != null) {
                TicTacToeState leaf = visit(unvisitedChild);
                backPropagate(cur, leaf);
                return leaf;
            } else {
                throw new RuntimeException("Unvisited child Not Found");
            }
        }
    }
    
    public TicTacToeState visit(TicTacToeNode cur) {
        TicTacToeState leaf = simulate(cur);
        backPropagate(cur, leaf);
        return leaf;
    }
    
    public TicTacToeState simulate(TicTacToeNode cur) {
        TicTacToeState state = new TicTacToeState(cur.state().game(), cur.state().random(), cur.state().position());
        int player = cur.state().player();
        
        while (!state.isTerminal()) {
            TicTacToeMove randomMove = state.chooseMove(player);
            state = state.next(randomMove);
            player = player == TicTacToe.X ? TicTacToe.O : TicTacToe.X;
        }
        
        return state;
    }
    
    public void backPropagate(TicTacToeNode ancestorNode, TicTacToeState leafState) {
        int leafWin = leafState.winner().isPresent() ? 1 : 0;
        if (ancestorNode.state().player() != leafState.player()) {
            ancestorNode.setUpdateValue(-leafWin);
        } else {
            ancestorNode.setUpdateValue(leafWin);
        }
    }

    private final TicTacToeNode root;
    private final int resource;
}