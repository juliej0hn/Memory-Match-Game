
package memorymatchgame01;

public abstract class GameElement {
    protected String id;

    public GameElement(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    // Abstract method (Polymorphism)
    public abstract void reset();
}