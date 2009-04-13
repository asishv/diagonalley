/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.util.GregorianCalendar;
import library.WizardSellerRemote;
import library.MagicalItemInfoRemote;

/**
 *
 * @author Asish
 */
public class WizardSeller extends Everyone implements WizardSellerRemote{
    int index;
    private DailyProphet.EventLogger out;
    public WizardSeller(String name, int index, DailyProphet.EventLogger out)
    {
        wizardOrNot=true;
        this.name=name;
        this.index=index;
        this.out=out;
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
     * Gets the target quantity for the magical item to sell.
     */    
    public int getTargetQuantity()
    {        
        for(int i=0; i<currentInventoryList.size(); i++)
        {
            CurrentInventoryList cil=currentInventoryList.get(i);
            if (cil.sellingPriceTarget != 0)
            {
                return cil.quantity+cil.quantityLocked;
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
     * Gets the target cost for the magical item to sell.
     */    
    public int getTargetCost()
    {
        for(int i=0; i<currentInventoryList.size(); i++)
        {
            CurrentInventoryList cil=currentInventoryList.get(i);
            if (cil.sellingPriceTarget != 0)
            {
                return cil.sellingPriceTarget;
            }
        }
        return -1;        
    }

    /**
     * Gets the target magical item to sell for the wizard.
     */    
    public MagicalItemInfoRemote getTargetCommodityInfo()
    {
        for(int i=0; i<currentInventoryList.size(); i++)
        {
            CurrentInventoryList cil=currentInventoryList.get(i);
            if (cil.sellingPriceTarget != 0)
            {
                return (MagicalItemInfoRemote)cil.magicalItem.magicalItemInfo;
            }
        }
        return null;        
    }

    /**
     * Gets the current price for the magical item present on CIL
     */
    int getCurrentPrice(int magicalItemNumber)
    {
        CurrentInventoryList cil = this.currentInventoryList.get(magicalItemNumber);
        return cil.diagonAlleySellerAccount.price;
    }

    /**
     * Gets the current price for the magical item present on CIL
     */
    int getCurrentQuantity(int magicalItemNumber)
    {
        CurrentInventoryList cil = this.currentInventoryList.get(magicalItemNumber);
        return cil.diagonAlleySellerAccount.quantity;
    }

    /**
     * Diagon Alley Wizard Sellers trade a magical item.
     */
    public boolean trade(int price, int quantity, int magicalItemNumber, long m)
    {
        CurrentInventoryList cil = this.currentInventoryList.get(magicalItemNumber);
        
        if(quantity > cil.quantity) {
            out.debug("Trade cannot be placed because the quantity is more than the user holds.");
            return false;
        }
        
        if(cil.sellingPriceTarget != 0 && price<cil.sellingPriceTarget)
        {
            out.debug("Trade cannot be placed because the selling price is less than the target price.");
            return false;
        }
        
        /* Obtain Lock, update the DiagonAlleySellerAccount values in Q,
         * update avg. price in Magical Item, unlock and execute trade.  */
        cil.magicalItem.lock();
        cil.magicalItem.averageSellingPrice = Math.min(cil.magicalItem.averageSellingPrice, price);
        /* Quantity of item decreases by 'x' in CIL, QuantityLocked will be incremented by 'x' */
        cil.quantity = cil.quantity - quantity;
        cil.quantityLocked = cil.quantityLocked + quantity;
        cil.diagonAlleySellerAccount.price = price;
        cil.diagonAlleySellerAccount.quantity = quantity;
        cil.diagonAlleySellerAccount.time=new GregorianCalendar();
        cil.diagonAlleySellerAccount.time.setTimeInMillis(cil.diagonAlleySellerAccount.time.getTimeInMillis()+m);
        cil.magicalItem.unlock();
        out.writeln(this.name+" is trying to sell "+quantity+" nos of item number "+magicalItemNumber+" @"+price);
        cil.magicalItem.executeTrade();
        return true;
    }

     /**
     * Diagon Alley Wizard Sellers modify existing trade for a magical item.
     */       
    public boolean modifyTrade(int price, int quantity, int magicalItemNumber, long m)
    {
        CurrentInventoryList cil = this.currentInventoryList.get(magicalItemNumber);

        int oldQuantity = cil.diagonAlleySellerAccount.quantity;
        if(quantity > cil.quantity + oldQuantity) {
            out.debug("Trade cannot be placed because the quantity is more than the user holds.");
            return false;
        }
        
        if(cil.sellingPriceTarget != 0 && price<cil.sellingPriceTarget)
        {
            out.debug("Trade cannot be placed because the selling price is less than the target price.");
            return false;
        }

        /* Obtain Lock, update the DiagonAlleySellerAccount values in Q,
         * update avg. price in Magical Item, unlock and execute trade. */
        cil.magicalItem.lock();
        cil.magicalItem.averageSellingPrice = Math.min(cil.magicalItem.averageSellingPrice, price);

        /* Modify the quantity and the quantity locked values in CIL */
        cil.quantity = cil.quantity + oldQuantity - quantity;
        cil.quantityLocked = cil.quantityLocked - oldQuantity + quantity;

        cil.diagonAlleySellerAccount.price = price;
        cil.diagonAlleySellerAccount.quantity = quantity;
        cil.diagonAlleySellerAccount.time.setTimeInMillis(cil.diagonAlleySellerAccount.time.getTimeInMillis()+m);
        cil.magicalItem.unlock();
        out.writeln(this.name+" is trying to sell "+quantity+" nos of item number "+magicalItemNumber+" @"+price);
        cil.magicalItem.executeTrade();
        return true;
    }
}
