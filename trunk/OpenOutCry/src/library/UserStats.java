/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package library;

/**
 *
 * @author Asish
 */
public class UserStats {
    private int score;
    private int quantity;
    private int cost;
    private int quantityLocked;
    private MagicalItemInfo m;
    public UserStats(int score, int quantity, int cost, int quantityLocked, MagicalItemInfo m)
    {
        this.score=score;
        this.quantity=quantity;
        this.cost=cost;
        this.quantityLocked=quantityLocked;
        this.m=m;
    }
    
    public int getScore()
    {
        return score;
    }
    
    public int getQuantity()
    {
        return quantity;
    }
    
    public int getQuantityLocked()
    {
        return quantityLocked;
    }
    
    public int getCost()
    {
        return cost;
    }
    
    public MagicalItemInfo getMagicalItemInfo()
    {
        return m;
    }
}