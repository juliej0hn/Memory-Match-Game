package memorymatchgame01;

import javafx.scene.layout.StackPane;

public class Card extends GameElement {
    private final String value;
    private boolean isOpen = false;
    private boolean isMatched = false;
    
    // EXCLUDE from UML Diagram (GUI Implementation detail)
    private StackPane view; 

    public Card(String id, String value) {
        super(id);
        this.value = value;
    }

    @Override
    public void reset() {
        this.isOpen = false;
        this.isMatched = false;
    }

    public String getValue() { return value; }
    public boolean isOpen() { return isOpen; }
    public void setOpen(boolean open) { this.isOpen = open; }
    public boolean isMatched() { return isMatched; }
    public void setMatched(boolean matched) { this.isMatched = matched; }

    // GUI Linkers
    public void setView(StackPane view) { this.view = view; }
    public StackPane getView() { return view; }
}