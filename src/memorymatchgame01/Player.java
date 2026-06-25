/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package memorymatchgame01;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private int timeSeconds;
    private int moves;

    public Player(String name, int timeSeconds, int moves) {
        this.name = name;
        this.timeSeconds = timeSeconds;
        this.moves = moves;
    }

    // Getters
    public String getName() { return name; }
    public int getTimeSeconds() { return timeSeconds; }
    public int getMoves() { return moves; }

    @Override
    public String toString() {
        return "Player: " + name + " | Time: " + timeSeconds + "s | Moves: " + moves;
    }
}