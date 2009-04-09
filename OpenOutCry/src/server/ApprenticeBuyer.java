/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;
import java.util.Calendar;
/**
 *
 * @author Asish
 */
public class ApprenticeBuyer extends Everyone{
    ApprenticeBuyer(String name)
    {
        wizardOrNot = false;
        this.name=name;
    }

    /**
     * Gets the target quantity to buy for the apprentice.
     */    
      int getTargetQuantity()
    {        
        for(int i=0; i<futureInventoryList.size(); i++)
        {
            FutureInventoryList fil=futureInventoryList.get(i);
            if (fil.buyingTargetPrice != 0)
            {
                return fil.quantity;
            }
        }
        return -1;
    }

      /**
     * Gets the target cost for the magical item to buy for the apprentice.
     */    
    int getTargetCost()
    {
        for(int i=0; i<futureInventoryList.size(); i++)
        {
            FutureInventoryList fil=futureInventoryList.get(i);
            if (fil.buyingTargetPrice != 0)
            {
                return fil.buyingTargetPrice;
            }
        }
        return -1;        
    }

    /**
     * Gets the target magical item to buy for the apprentice.
     */    
    MagicalItemInfo getTargetCommodityInfo()
    {
        for(int i=0; i<futureInventoryList.size(); i++)
        {
            FutureInventoryList fil=futureInventoryList.get(i);
            if (fil.buyingTargetPrice != 0)
            {
                return fil.magicalItem.magicalItemInfo;
            }
        }
        return null;        
    }
 
    /**
     * Diagon Alley Apprentice Buyers place a bid to buy a magical item.
     */       
    boolean bid(int price, int quantity, int magicalItemNumber,long msec)
    {
        FutureInventoryList fil=futureInventoryList.get(magicalItemNumber);
        if(quantity>fil.quantity)
           return false; 
        if(price>fil.buyingTargetPrice)
            return false;
        fil.quantity-=quantity;
        fil.quantityLocked+=quantity;
        fil.magicalItem.lock();
        fil.diagonAlleyBuyerAccount.price=price;
        fil.diagonAlleyBuyerAccount.quantity=quantity;
        fil.diagonAlleyBuyerAccount.time.setTimeInMillis(fil.diagonAlleyBuyerAccount.time.getTimeInMillis()+msec);
        fil.magicalItem.unlock();
        fil.magicalItem.executeTrade();
        return true;
    }
    
    /**
     * Diagon Alley Apprentice Buyers modify an existing bid for a magical item.
     */       
    boolean modifyBid(int price, int quantity, int magicalItemNumber, long msec)
    {
        FutureInventoryList fil=futureInventoryList.get(magicalItemNumber);
        if(quantity>fil.quantity)
           return false; 
        if(price>fil.buyingTargetPrice)
           return false;
        fil.quantity-=quantity+fil.diagonAlleyBuyerAccount.quantity;
        fil.quantityLocked+=quantity-fil.diagonAlleyBuyerAccount.quantity;
        fil.magicalItem.lock();
        fil.diagonAlleyBuyerAccount.price=price;
        fil.diagonAlleyBuyerAccount.quantity=quantity;
        fil.diagonAlleyBuyerAccount.time.setTimeInMillis(fil.diagonAlleyBuyerAccount.time.getTimeInMillis()+msec);
        fil.magicalItem.unlock();
        fil.magicalItem.executeTrade(); 
        return true;
    }
}
