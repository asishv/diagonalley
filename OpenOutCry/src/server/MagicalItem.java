/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;
import DailyProphet.EventLogger;
import java.util.ArrayList;
import java.io.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.locks.*;
import library.EventLoggerRemote;

/**
 *
 * @author Asish
 */
public class MagicalItem implements Serializable{
    int index;
    MagicalItemInfo magicalItemInfo;
    volatile int averageSellingPrice;
    ArrayList<DiagonAlleySellerAccount> sellerAccount;
    ArrayList<DiagonAlleyBuyerAccount> buyerAccount;
    private Lock l;
    
    MagicalItem(int index)
    {
        l=new ReentrantLock();
        sellerAccount=new ArrayList();
        buyerAccount=new ArrayList();
        this.index=index;
    }
    
     /**
     * Checks if a bid for a magical item meets the expectation of the seller.
     */       
    void executeTrade()
    {
        EventLogger.debug("Trying to exexute trades for "+magicalItemInfo.name + " Number of buyers: "+ buyerAccount.size());
        lock();
        for(int i=0; i<buyerAccount.size(); i++)
        {
            //Get the buyer account
            DiagonAlleyBuyerAccount daba=buyerAccount.get(i);
            Calendar d=new GregorianCalendar();
            if(daba.time!=null)
            {
                if(daba.time.after(d)) //Check if the bid is valid
                {
                    for(int j=0; j<sellerAccount.size(); j++)
                    {
                        DiagonAlleySellerAccount dasa=sellerAccount.get(i);
                        if(dasa.time!=null)
                        {
                            if(dasa.time.after(d)) //Check if the sale is valid
                            {
                                if(daba.price<=dasa.price) //Matching criterion
                                {
                                    //Execute trade
                                    if(daba.quantity<dasa.quantity) //Check if the buyer needs less quantity than seller
                                    {
                                        dasa.quantity-=daba.quantity;  //Reduce the quantity in the sale
                                        CurrentInventoryList cil=daba.e.currentInventoryList.get(i);
                                        int cost=(daba.price+dasa.price)/2; //Calculate the cost for the sale
                                        cil.lock();
                                        cil.quantity+=daba.quantity; //Update the quantity bought for the buyer
                                        cil.unlock();
                                        FutureInventoryList fil=daba.e.futureInventoryList.get(i);
                                        fil.lock();
                                        fil.quantity-=daba.quantity; //Update the goal for the buyer
                                        fil.unlock();
                                        daba.e.lock();
                                        daba.e.score+=(fil.buyingTargetPrice-cost)*daba.quantity;//Update score for buyer
                                        daba.e.unlock();
                                        cil=dasa.e.currentInventoryList.get(i);
                                        cil.lock();
                                        cil.quantityLocked-=daba.quantity; //Update the quantity locked for the seller
                                        cil.unlock();
                                        dasa.e.lock();
                                        dasa.e.score+=(cost-cil.sellingPriceTarget)*daba.quantity; //Update score for seller
                                        dasa.e.unlock();
                                        EventLogger.writeln("Sold "+daba.quantity+" of "+cil.magicalItem.magicalItemInfo.name+" for "+cost);
                                        daba.quantity=0; //Update the bid quantity
                                    }
                                    else
                                    {
                                        daba.quantity-=dasa.quantity; //Reduce the bid quantity
                                        CurrentInventoryList cil=daba.e.currentInventoryList.get(i);
                                        int cost=(daba.price+dasa.price)/2; //Calculate the cost for the sale
                                        cil.lock();
                                        cil.quantity+=dasa.quantity; //Update the quantity bought for the buyer
                                        cil.unlock();
                                        FutureInventoryList fil=daba.e.futureInventoryList.get(i);
                                        fil.lock();
                                        fil.quantity-=dasa.quantity; //Update the goal for the buyer
                                        fil.unlock();
                                        daba.e.lock();
                                        daba.e.score+=(fil.buyingTargetPrice-cost)*daba.quantity;//Update score for buyer
                                        daba.e.unlock();
                                        cil=dasa.e.currentInventoryList.get(i);
                                        cil.lock();
                                        cil.quantityLocked-=daba.quantity; //Update the quantity locked for the seller
                                        cil.unlock();
                                        dasa.e.lock();
                                        dasa.e.score+=(cost-cil.sellingPriceTarget)*daba.quantity; //Update score for seller
                                        dasa.e.unlock();
                                        EventLogger.writeln("Sold "+dasa.quantity+" of "+cil.magicalItem.magicalItemInfo.name+" for "+cost);
                                        dasa.quantity=0; //Update the sale quantity
                                    }
                                }
                            }
                            else
                            {
                                EventLogger.debug("Trade Time in millis "+dasa.time.getTimeInMillis());
                                EventLogger.writeln("Trade that "+ dasa.e.name+ " placed has expired!");
                                CurrentInventoryList cil=dasa.e.currentInventoryList.get(i);
                                cil.quantity+=dasa.quantity; //Update the quantity because the sale was invalid
                                cil.quantityLocked-=dasa.quantity; //Update the quantitylocked because the sale was invalid
                                dasa.quantity=0;
                            }
                        }
                    }
                }
                else
                {
                      EventLogger.debug("Bid Time in millis is "+daba.time.getTimeInMillis()+" current time in millis is "+d.getTimeInMillis());
                      EventLogger.writeln("Bid that "+ daba.e.name+ " placed has expired!");
                      FutureInventoryList fil=daba.e.futureInventoryList.get(i);
                      fil.quantity+=daba.quantity; //Update the target quantity for the seller because the bid was invalid.
                      fil.quantityLocked+=daba.quantity; //Update the quantityLocked for the buyer
                      daba.quantity=0;
                }
            }
            else
            {
                EventLogger.debug("No bid placed by "+ daba.e.name+ " for "+ this.magicalItemInfo.name);
            }
        }
        EventLogger.debug("Exiting executeTrade");
        unlock();
    }

    /**
     * Locks the magical item.
     */       
    public void lock()
    {
        l.lock();
    }

    /**
     * Unlocks the magical item.
     */       
    public void unlock()
    {
        l.unlock();
    }
}
