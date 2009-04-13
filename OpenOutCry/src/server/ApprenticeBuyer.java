/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import DailyProphet.EventLogger;
import java.util.GregorianCalendar;
import library.ApprenticeBuyerRemote;


/**
 *
 * @author Asish
 */
public class ApprenticeBuyer extends Everyone implements ApprenticeBuyerRemote{
    int index;    
    ApprenticeBuyer(String name, int index)
    {
        wizardOrNot = false;
        this.name=name;
        this.index=index;
    }

    /**
     * Gets the target quantity to buy for the buyerAccount.
     */    
    public int getTargetQuantity()
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
     * Gets the target quantity locked to buy for the buyerAccount.
     */    
    public int getTargetQuantityLocked()
    {        
        for(int i=0; i<futureInventoryList.size(); i++)
        {
            FutureInventoryList fil=futureInventoryList.get(i);
            if (fil.buyingTargetPrice != 0)
            {
                return fil.quantityLocked;
            }
        }
        return -1;
    }

    
    /**
     * Gets the target cost for the magical item to buy for the buyerAccount.
     */    
    public int getTargetCost()
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
    
    public int getScore()
    {
        int score;
        lock();
        score=this.score;
        unlock();
        return score;
    }

    /**
     * Gets the target magical item to buy for the buyerAccount.
     */    
    public MagicalItemInfo getTargetCommodityInfo()
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
    public boolean bid(int price, int quantity, int magicalItemNumber,long msec)
    {
        FutureInventoryList fil=futureInventoryList.get(magicalItemNumber);

        if(fil.buyingTargetPrice!=0&&price>fil.buyingTargetPrice)
        {
            EventLogger.debug("Trade cannot be placed because the buying price is more than the target price.");
            return false;
        }
        
        fil.magicalItem.lock();
        fil.diagonAlleyBuyerAccount.price=price;
        fil.diagonAlleyBuyerAccount.quantity=quantity;
        fil.diagonAlleyBuyerAccount.time=new GregorianCalendar();
        EventLogger.debug("Old Time in millis = "+fil.diagonAlleyBuyerAccount.time.getTimeInMillis());
        fil.diagonAlleyBuyerAccount.time.setTimeInMillis(fil.diagonAlleyBuyerAccount.time.getTimeInMillis()+msec);
        EventLogger.debug("New Time in millis = "+fil.diagonAlleyBuyerAccount.time.getTimeInMillis());
        fil.lock();
        fil.quantity-=quantity;
        fil.quantityLocked+=quantity;
        fil.unlock();
        fil.magicalItem.unlock();
        EventLogger.writeln(this.name+" is trying to buy "+quantity+" nos of "+fil.magicalItem.magicalItemInfo.name+" @$"+price);
        fil.magicalItem.executeTrade();
        return true;
    }
    
    /**
     * Diagon Alley Apprentice Buyers modify an existing bid for a magical item.
     */       
    public boolean modifyBid(int price, int quantity, int magicalItemNumber, long msec)
    {
        FutureInventoryList fil=futureInventoryList.get(magicalItemNumber);

        if(fil.buyingTargetPrice!=0&&price>fil.buyingTargetPrice)
        {
            EventLogger.debug("Trade cannot be placed because the buying price is more than the target price.");
            return false;
        }
        fil.magicalItem.lock();
        fil.diagonAlleyBuyerAccount.price=price;
        fil.diagonAlleyBuyerAccount.quantity=quantity;
        fil.diagonAlleyBuyerAccount.time.setTimeInMillis(fil.diagonAlleyBuyerAccount.time.getTimeInMillis()+msec);
        fil.lock();
        fil.quantity-=quantity+fil.diagonAlleyBuyerAccount.quantity;
        fil.quantityLocked+=quantity-fil.diagonAlleyBuyerAccount.quantity;
        fil.unlock();
        fil.magicalItem.unlock();
        EventLogger.writeln(this.name+" is trying to buy "+quantity+" nos of "+fil.magicalItem.magicalItemInfo.name+" @$"+price);
        fil.magicalItem.executeTrade(); 
        return true;
    }
}
