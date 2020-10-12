/**
 * Move.java Defines a Pokemon Move
 * @author CS 1331 TA's
 * @version 1.0
 */

public class Move {

    private String name;
    private int power;
    private String type;

    /**
     * The constructor for the Move class
     * @param n name of the move
     * @param p power of the move
     * @param t type of the move
     */

    public Move(String n, int p, String t) {

        name = n;
        power = p;
        type = t;

    }

    /**
     * The getter method for the name of the move
     * @return name of the move
     */

    public String getName() {

        return name;

    }

    /**
     * The getter method for the power of the move
     * @return power of the move
     */

    public int getPower() {

        return power;

    }

    /**
     * The getter method for the type of the move
     * @return type of the move
     */

    public String getType() {

        return type;

    }

}
