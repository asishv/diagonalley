/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;
//import DailyProphet.EventLogger;
import DailyProphet.EventLogger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import library.MagicalItemInfo;
/**
 *
 * @author Asish
 */
public class Everyone{
    String name;
    boolean wizardOrNot;
    volatile int score;
    int index; 
    
    public boolean isWizard()
    {
        return wizardOrNot;
    }
    
    Everyone()
    {
        currentInventoryList=new ArrayList();
        futureInventoryList=new ArrayList();
    }
    
    ArrayList<CurrentInventoryList> currentInventoryList;
    ArrayList<FutureInventoryList> futureInventoryList;
    
    public int getScore()
    {
        int score;
        score=this.score;
        return score;
    }

    /**
     * Diagon Alley Wizard Sellers trade a magical item.
     */
    public boolean trade(int price, int quantity, int magicalItemNumber, long m)
    {
        CurrentInventoryList cil = this.currentInventoryList.get(magicalItemNumber);
        
        if(quantity > cil.quantity) {
            EventLogger.debug("Current Quantity: "+cil.quantity);
            EventLogger.debug("Trade cannot be placed because the quantity is more than the user holds.");
            return false;
        }
        
        if(cil.sellingPriceTarget != 0 && price<cil.sellingPriceTarget)
        {
            EventLogger.debug("Selling Price Target: "+cil.sellingPriceTarget);
            EventLogger.debug("Trade cannot be placed because the selling price is less than the target price.");
            return false;
        }
        
        cil.magicalItem.averageSellingPrice = Math.min(cil.magicalItem.averageSellingPrice, price);
        /* Quantity of item decreases by 'x' in CIL, QuantityLocked will be incremented by 'x' */
        cil.quantity = cil.quantity - quantity;
        cil.quantityLocked = cil.quantityLocked + quantity;
        cil.diagonAlleySellerAccount.price = price;
        cil.diagonAlleySellerAccount.quantity = quantity;
        cil.diagonAlleySellerAccount.time=new GregorianCalendar();
        EventLogger.debug("Old Time in millis = "+cil.diagonAlleySellerAccount.time.getTimeInMillis());
        cil.diagonAlleySellerAccount.time.setTimeInMillis(cil.diagonAlleySellerAccount.time.getTimeInMillis()+m);
        EventLogger.debug("New Time in millis = "+cil.diagonAlleySellerAccount.time.getTimeInMillis());
        EventLogger.writeln(this.name+" is trying to sell "+quantity+" nos of "+cil.magicalItem.magicalItemInfo.getName()+" @$"+price);
        if((cil.magicalItem.sellerAccount.get(index)).time!=null)
            EventLogger.debug("Time not set for trade!");
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
            EventLogger.debug("Trade cannot be placed because the quantity is more than the user holds.");
            return false;
        }
        
        if(cil.sellingPriceTarget != 0 && price<cil.sellingPriceTarget)
        {
            EventLogger.debug("Trade cannot be placed because the selling price is less than the target price.");
            return false;
        }

        /* Obtain Lock, update the DiagonAlleySellerAccount values in Q,
         * update avg. price in Magical Item, unlock and execute trade. */
        cil.magicalItem.averageSellingPrice = Math.min(cil.magicalItem.averageSellingPrice, price);

        /* Modify the quantity and the quantity locked values in CIL */
        cil.quantity = cil.quantity + oldQuantity - quantity;
        cil.quantityLocked = cil.quantityLocked - oldQuantity + quantity;

        cil.diagonAlleySellerAccount.price = price;
        cil.diagonAlleySellerAccount.quantity = quantity;
        cil.diagonAlleySellerAccount.time.setTimeInMillis(cil.diagonAlleySellerAccount.time.getTimeInMillis()+m);
        EventLogger.writeln(this.name+" is trying to sell "+quantity+" nos of "+cil.magicalItem.magicalItemInfo.getName()+" @$"+price);
        cil.magicalItem.executeTrade();
        return true;
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
        
        fil.diagonAlleyBuyerAccount.price=price;
        fil.diagonAlleyBuyerAccount.quantity=quantity;
        fil.diagonAlleyBuyerAccount.time=new GregorianCalendar();
        EventLogger.debug("Old Time in millis = "+fil.diagonAlleyBuyerAccount.time.getTimeInMillis());
        fil.diagonAlleyBuyerAccount.time.setTimeInMillis(fil.diagonAlleyBuyerAccount.time.getTimeInMillis()+msec);
        EventLogger.debug("New Time in millis = "+fil.diagonAlleyBuyerAccount.time.getTimeInMillis());
        fil.quantity-=quantity;
        fil.quantityLocked+=quantity;
        EventLogger.writeln(this.name+" is trying to buy "+quantity+" nos of "+fil.magicalItem.magicalItemInfo.getName()+" @$"+price);
        if((fil.magicalItem.buyerAccount.get(index)).time!=null)
            EventLogger.debug("Time not set for trade!");
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
        fil.diagonAlleyBuyerAccount.price=price;
        fil.diagonAlleyBuyerAccount.quantity=quantity;
        fil.diagonAlleyBuyerAccount.time.setTimeInMillis(fil.diagonAlleyBuyerAccount.time.getTimeInMillis()+msec);
        fil.quantity-=quantity+fil.diagonAlleyBuyerAccount.quantity;
        fil.quantityLocked+=quantity-fil.diagonAlleyBuyerAccount.quantity;
        EventLogger.writeln(this.name+" is trying to buy "+quantity+" nos of "+fil.magicalItem.magicalItemInfo.getName()+" @$"+price);
        fil.magicalItem.executeTrade(); 
        return true;
    }
}
