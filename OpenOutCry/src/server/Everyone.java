/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;
//import DailyProphet.EventLogger;
import DailyProphet.EventLogger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import library.MagicalItemInfo;
/**
 *
 * @author Asish
 */
public class Everyone{
    String name;
    boolean wizardOrNot;
    volatile boolean goalMetOrNot;
    volatile int score;
    int index; 
    
    public boolean isWizard()
    {
        return wizardOrNot;
    }
    
    Everyone()
    {
        goalMetOrNot=false;
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
    
    public int getQuantity(int itemNumber)
    {
//        if(isWizard())
//        {
            CurrentInventoryList cil=currentInventoryList.get(itemNumber);
            return cil.quantity;
//        }
//        return 0;
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
        
        /* Quantity of item decreases by 'x' in CIL, QuantityLocked will be incremented by 'x' */
        cil.quantity = cil.quantity - quantity;
        cil.quantityLocked = cil.quantityLocked + quantity;
        Calendar time=new GregorianCalendar();
        time.setTimeInMillis(time.getTimeInMillis()+m);
        cil.diagonAlleySellerAccount.add(price, quantity, time);
        EventLogger.writeln(this.name+" is trying to sell "+quantity+" nos of "+cil.magicalItem.magicalItemInfo.getName()+" @$"+price);
        cil.magicalItem.executeTrade();
        cil.diagonAlleySellerAccount.getMinimum();
        cil.magicalItem.minimumSellingPrice = cil.diagonAlleySellerAccount.price;
        return true;
    }

     /**
     * Diagon Alley Wizard Sellers modify existing trade for a magical item.
     */       
    public boolean modifyTrade(int id, int price, int quantity, int magicalItemNumber, long m)
    {
        CurrentInventoryList cil = this.currentInventoryList.get(magicalItemNumber);
        cil.diagonAlleySellerAccount.getHistory(id);
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

        /* Modify the quantity and the quantity locked values in CIL */
        cil.quantity = cil.quantity + oldQuantity - quantity;
        cil.quantityLocked = cil.quantityLocked - oldQuantity + quantity;

        cil.diagonAlleySellerAccount.price = price;
        cil.diagonAlleySellerAccount.quantity = quantity;
        cil.diagonAlleySellerAccount.time.setTimeInMillis(cil.diagonAlleySellerAccount.time.getTimeInMillis()+m);
        cil.diagonAlleySellerAccount.modify(id);
        EventLogger.writeln(this.name+" is trying to sell "+quantity+" nos of "+cil.magicalItem.magicalItemInfo.getName()+" @$"+price);
        cil.magicalItem.executeTrade();
        cil.diagonAlleySellerAccount.getMinimum();
        cil.magicalItem.minimumSellingPrice = cil.diagonAlleySellerAccount.price;
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
        Calendar time=new GregorianCalendar();
        time.setTimeInMillis(time.getTimeInMillis()+msec);
        fil.diagonAlleyBuyerAccount.add(price, quantity, time);
        fil.quantity-=quantity;
        fil.quantityLocked+=quantity;
        EventLogger.writeln(this.name+" is trying to buy "+quantity+" nos of "+fil.magicalItem.magicalItemInfo.getName()+" @$"+price);
        fil.magicalItem.executeTrade();
        fil.diagonAlleyBuyerAccount.getMaximum();
        return true;
    }
    
    /**
     * Diagon Alley Apprentice Buyers modify an existing bid for a magical item.
     */       
    public boolean modifyBid(int id, int price, int quantity, int magicalItemNumber, long msec)
    {
        FutureInventoryList fil=futureInventoryList.get(magicalItemNumber);
        fil.diagonAlleyBuyerAccount.getHistory(id);
        if(fil.buyingTargetPrice!=0&&price>fil.buyingTargetPrice)
        {
            EventLogger.debug("Trade cannot be placed because the buying price is more than the target price.");
            return false;
        }
        fil.diagonAlleyBuyerAccount.price=price;
        fil.diagonAlleyBuyerAccount.quantity=quantity;
        fil.diagonAlleyBuyerAccount.time.setTimeInMillis(fil.diagonAlleyBuyerAccount.time.getTimeInMillis()+msec);
        fil.diagonAlleyBuyerAccount.modify(id);
        fil.quantity-=quantity+fil.diagonAlleyBuyerAccount.quantity;
        fil.quantityLocked+=quantity-fil.diagonAlleyBuyerAccount.quantity;
        EventLogger.writeln(this.name+" is trying to buy "+quantity+" nos of "+fil.magicalItem.magicalItemInfo.getName()+" @$"+price);
        fil.magicalItem.executeTrade(); 
        fil.diagonAlleyBuyerAccount.getMaximum();
        return true;
    }
}
