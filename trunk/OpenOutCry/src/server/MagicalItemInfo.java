/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import library.MagicalItemInfoRemote;

/**
 *
 * @author Asish
 */
public class MagicalItemInfo implements MagicalItemInfoRemote{
    String name;
    String symbol;
    String picture;
    MagicalItemInfo(String name, String symbol, String picture)
    {
        this.name=name;
        this.symbol=symbol;
        this.picture=picture;
    }
    public String getName() {
        return this.name;
    }
    public String getSymbol() {
        return this.symbol;
    }
    public String getPicture() {
        return this.picture;
    }
}
