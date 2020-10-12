/**
 * Pokemon.java Defines a Pokemon
 * @author CS 1331 TA's and Hemang Dash
 * @version 1.1
 */

public class Pokemon {

    private String name;
    private int level;
    private double maxHP;
    private double currentHP;
    private int atk;
    private Move[] moves;
    private String type;
    private boolean fainted;

    /**
     * The constructor for the Pokemon class
     * @param n name of the Pokemon
     * @param l level of the Pokemon
     * @param h maximum HP of the Pokemon
     * @param a attack of the Pokemon
     * @param t type of the Pokemon
     * @param move moves of the Pokemon
     */

    public Pokemon(String n, int l, double h, int a, String t, Move[] move) {

        name = n;
        level = l;
        maxHP = h;
        atk = a;
        type = t;
        currentHP = maxHP;
        moves = move;

    }

    /**
     * The getter method for the name of the Pokemon
     * @return name of the Pokemon
     */

    public String getName() {

        return name;

    }

    /**
     * The getter method for the level of the Pokemon
     * @return level of the Pokemon
     */

    public int getLevel() {

        return level;

    }

    /**
     * The getter method for the attack of the Pokemon
     * @return attack of the Pokemon
     */
    public int getAtk() {

        return atk;

    }

    /**
     * The getter method for the maximum HP of the Pokemon
     * @return maximum HP of the Pokemon
     */
    public double getMaxHP() {

        return maxHP;

    }

    /**
     * The getter method for the current HP of the Pokemon
     * @return current HP of the Pokemon
     */

    public double getCurrentHP() {

        return currentHP;

    }

    /**
     * The getter method for the moves of the Pokemon
     * @return moves of the Pokemon
     */

    public Move[] getMoves() {

        return moves;

    }

    /**
     * The setter method for the name of the Pokemon
     * @param name name of the Pokemon
     */

    public void setName(String name) {

        this.name = name;

    }

    /**
     * The setter method for the attack of the Pokemon
     * @param atk attack of the Pokemon
     */

    public void setAtk(int atk) {

        this.atk = atk;

    }

    /**
     * The setter method for the maximum HP of the Pokemon
     * @param maxHP maximum HP of the Pokemon
     */

    public void setMaxHP(double maxHP) {

        this.maxHP = maxHP;

    }

    /**
     * The setter method for the level of the Pokemon
     * @param level level of the Pokemon
     */

    public void setLevel(int level) {

        this.level = level;

    }

    /**
     * The setter method for the current HP of the Pokemon
     * @param currentHP current HP of the Pokemon
     */

    public void setCurrentHP(double currentHP) {

        this.currentHP = currentHP;
        if (this.currentHP <= 0) {
            fainted = true;
        }

    }

    /**
     * This method compares the type of Pokemon to the type of the move used on it
     * @param move move used on the Pokemon
     * @return comparison result
     */

    public double compareType(Move move) {

        if (move.getType().equals(type)) {
            return 0.5;
        } else if (move.getType().equals("WATER")) {
            if (type.equals("FIRE")) {
                return 2.0;
            } else if (type.equals("GRASS")) {
                return 0.5;
            }
        } else if (move.getType().equals("GRASS")) {
            if (type.equals("WATER")) {
                return 2.0;
            } else if (type.equals("FIRE")) {
                return 0.5;
            } else if (type.equals("FLYING")) {
                return 0.5;
            }
        } else if (move.getType().equals("FIRE")) {
            if (type.equals("GRASS")) {
                return 2.0;
            } else if (type.equals("WATER")) {
                return 0.5;
            }
        } else if (move.getType().equals("FLYING")) {
            if (type.equals("GRASS")) {
                return 2.0;
            }
        }

        return 1;

    }

    /**
     * This method checks whether the Pokemon is fainted
     * @return true or false
     */

    public boolean isFainted() {

        return fainted;

    }

    /**
     * This method sets whether the Pokemon is fainted
     * @param fainted true or false
     */

    public void setFainted(boolean fainted) {

        this.fainted = fainted;

    }

}
