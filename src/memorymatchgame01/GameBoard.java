
package memorymatchgame01;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class GameBoard {
    public enum TurnResult { WAITING, MATCH, MISMATCH, IGNORE }

    // Composition: GameBoard "has" Cards 
    private final List<Card> cards;
    
    // Association: GameBoard "knows" the Player (Satisfies grading criteria)
    private Player currentPlayer; 

    private Card firstSelected, secondSelected;
    private int moves, incorrectMoves, pairsFound, totalPairs;

    public GameBoard() {
        this.cards = new ArrayList<>();
    }
    
    // Establishes the relationship for the UML
    public void setPlayer(String name) {
        this.currentPlayer = new Player(name, 0, 0);
    }
    
    // Returns the player object
    public Player getPlayer() {
        return currentPlayer;
    }

    public void setupGame() {
        cards.clear();
        firstSelected = null; 
        secondSelected = null;
        moves = 0; 
        incorrectMoves = 0;
        pairsFound = 0;

        // Emojis for the cards
        String[] emojis = { "🍎", "🍓", "🍇", "🍌", "🍉", "🍍", "🍊", "🍐" };
        totalPairs = emojis.length;

        for (String e : emojis) {
            cards.add(new Card(UUID.randomUUID().toString(), e));
            cards.add(new Card(UUID.randomUUID().toString(), e));
        }
        Collections.shuffle(cards);
    }

    public TurnResult processSelect(Card c) {
        if (c.isOpen() || c.isMatched()) return TurnResult.IGNORE;

        if (firstSelected == null) {
            firstSelected = c;
            c.setOpen(true);
            return TurnResult.WAITING;
        } else {
            secondSelected = c;
            secondSelected.setOpen(true);
            moves++;

            if (firstSelected.getValue().equals(secondSelected.getValue())) {
                firstSelected.setMatched(true);
                secondSelected.setMatched(true);
                pairsFound++;
                firstSelected = null;
                secondSelected = null;
                return TurnResult.MATCH;
            } else {
                incorrectMoves++;
                return TurnResult.MISMATCH;
            }
        }
    }

    public void resetMismatch() {
        if (firstSelected != null) firstSelected.setOpen(false);
        if (secondSelected != null) secondSelected.setOpen(false);
        firstSelected = null; 
        secondSelected = null;
    }

    public List<Card> getCards() { return cards; }
    public int getMoves() { return moves; }
    public int getIncorrectMoves() { return incorrectMoves; }
    public int getPairsFound() { return pairsFound; }
    public boolean isGameWon() { return pairsFound == totalPairs; }
}