/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

/**
 *
 * @author Asish
 */
public class FutureInventoryList extends Inventory{
    int buyingTargetPrice;
    DiagonAlleyBuyerAccount diagonAlleyBuyerAccount;
    FutureInventoryList(int cost, int quantity, MagicalItem magicalItem)
    {
        this.buyingTargetPrice=cost;
        this.quantity=quantity;
        this.magicalItem=magicalItem;
    }
}
