/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;
import library.MagicalItemInfo;
import DailyProphet.EventLogger;
import java.util.ArrayList;
import java.io.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Asish
 */
public class MagicalItem{
    int index;
    MagicalItemInfo magicalItemInfo;
    volatile int averageSellingPrice;
    ArrayList<DiagonAlleySellerAccount> sellerAccount;
    ArrayList<DiagonAlleyBuyerAccount> buyerAccount;
    
    MagicalItem(int index)
    {
        sellerAccount=new ArrayList();
        buyerAccount=new ArrayList();
        this.index=index;
    }
    
     /**
     * Checks if a bid for a magical item meets the expectation of the seller.
     */       
    void executeTrade()
    {
        EventLogger.debug("Trying to execute trades for "+magicalItemInfo.getName() + " Number of buyers: "+ buyerAccount.size()+" Number of sellers: "+sellerAccount.size());
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
                        DiagonAlleySellerAccount dasa=sellerAccount.get(j);
                        if(dasa.time!=null)
                        {
                            if(dasa.time.after(d)) //Check if the sale is valid
                            {
                                if(daba.price>=dasa.price) //Matching criterion
                                {
                                    //Execute trade
                                    if(daba.quantity<dasa.quantity) //Check if the buyer needs less quantity than seller
                                    {
                                        EventLogger.debug("Quantity available in the market less than what buyer needs");
                                        dasa.quantity-=daba.quantity;  //Reduce the quantity in the sale
                                        CurrentInventoryList cil=daba.e.currentInventoryList.get(index);
                                        int cost=(daba.price+dasa.price)/2; //Calculate the cost for the sale
                                        cil.quantity+=daba.quantity; //Update the quantity bought for the buyer
                                        FutureInventoryList fil=daba.e.futureInventoryList.get(index);
                                        if(fil.quantity-daba.quantity>0)
                                            fil.quantity-=daba.quantity; //Update the goal for the buyer
                                        else
                                            fil.quantity=0;
                                        daba.e.score+=(fil.buyingTargetPrice-cost)*daba.quantity;//Update score for buyer
                                        cil=dasa.e.currentInventoryList.get(index);
                                        cil.quantityLocked-=daba.quantity; //Update the quantity locked for the seller
                                        dasa.e.score+=(cost-cil.sellingPriceTarget)*daba.quantity; //Update score for seller
                                        EventLogger.writeln("Sold "+daba.quantity+" of "+cil.magicalItem.magicalItemInfo.getName()+" for "+cost);
                                        daba.quantity=0; //Update the bid quantity
                                    }
                                    else
                                    {
                                        EventLogger.debug("Quantity bought by the buyer is less than what the seller is selling");
                                        daba.quantity-=dasa.quantity; //Reduce the bid quantity
                                        CurrentInventoryList cil=daba.e.currentInventoryList.get(index);
                                        int cost=(daba.price+dasa.price)/2; //Calculate the cost for the sale
                                        cil.quantity+=dasa.quantity; //Update the quantity bought for the buyer
                                        FutureInventoryList fil=daba.e.futureInventoryList.get(index);
                                        if(fil.quantity-dasa.quantity>0)
                                            fil.quantity-=dasa.quantity; //Update the goal for the buyer
                                        else
                                            fil.quantity=0;
                                        daba.e.score+=(fil.buyingTargetPrice-cost)*daba.quantity;//Update score for buyer
                                        cil=dasa.e.currentInventoryList.get(index);
                                        cil.quantityLocked-=dasa.quantity; //Update the quantity locked for the seller
                                        dasa.e.score+=(cost-cil.sellingPriceTarget)*daba.quantity; //Update score for seller
                                        EventLogger.writeln("Sold "+dasa.quantity+" of "+cil.magicalItem.magicalItemInfo.getName()+" for "+cost);
                                        dasa.quantity=0; //Update the sale quantity
                                    }
                                }
                                else
                                    EventLogger.debug("Buyer's offer less than Seller's demand! ");
                            }
                            else
                            {
                                EventLogger.debug("Trade Time in millis "+dasa.time.getTimeInMillis());
                                EventLogger.writeln("Trade that "+ dasa.e.name+ " placed has expired!");
                                CurrentInventoryList cil=dasa.e.currentInventoryList.get(i);
                                cil.quantity+=dasa.quantity; //Update the quantity because the sale was invalid
                                cil.quantityLocked-=dasa.quantity; //Update the quantitylocked because the sale was invalid
                                dasa.quantity=0;
                                dasa.time=null;
                            }
                        }
                        else
                        {
                            EventLogger.debug("No trade placed by "+ dasa.e.name+ " for "+ this.magicalItemInfo.getName());
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
                      daba.time=null;
                }
            }
            else
            {
                EventLogger.debug("No bid placed by "+ daba.e.name+ " for "+ this.magicalItemInfo.getName());
            }
        }
        EventLogger.debug("Exiting executeTrade");
    }
}
