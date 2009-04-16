/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package library;

import java.util.Calendar;

/**
 *
 * @author Asish
 */
public class History
{
    public int price;
    public int quantity;
    public Calendar time;
    public History(int price, int quantity, Calendar time)
    {
        this.price=price;
        this.quantity=quantity;
        this.time=time;
    }
}
