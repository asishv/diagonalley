/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package library;

import java.io.Serializable;
import java.rmi.Remote;
import java.util.Calendar;

/**
 *
 * @author Asish
 */
public class BidTradeArgs implements Serializable, Remote{
    private int price;
    private int quantity;
    private long time;
    public BidTradeArgs(int price, int quantity, long time)
    {
        this.price=price;
        this.quantity=quantity;
        this.time=time;
    }
    
    public int getPrice()
    {
        return price;
    }
    
    public int getQuantity()
    {
        return quantity;
    }
    
    public long getTime()
    {
        return time;
    }
}
