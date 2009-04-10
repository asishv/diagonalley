/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;
import java.util.ArrayList;
import java.io.*;
import java.util.concurrent.locks.*;

/**
 *
 * @author Asish
 */
public class MagicalItem implements java.rmi.Remote{
    MagicalItemInfo magicalItemInfo;
    volatile int averageSellingPrice;
    ArrayList<DiagonAlleySellerAccount> wizards;
    ArrayList<DiagonAlleyBuyerAccount> apprentice;
    private Lock l;
    
    MagicalItem()
    {
        l=new ReentrantLock();
        wizards=new ArrayList();
        apprentice=new ArrayList();
    }
    
     /**
     * Checks if a bid for a magical item meets the expectation of the seller.
     */       
    void executeTrade()
    {
        lock();
        for(int i=0; i<apprentice.size(); i++)
        {
            //Get the buyer account
            DiagonAlleyBuyerAccount daba=apprentice.get(i);
            if(daba.time.before(new java.util.Date())) //Check if the bid is valid
            {
                for(int j=0; j<wizards.size(); j++)
                {
                    DiagonAlleySellerAccount dasa=wizards.get(i);
                    if(dasa.time.before(new java.util.Date())) //Check if the sale is valid
                    {
                        if(daba.price<=dasa.price) //Matching criterion
                        {
                            //Execute trade
                            if(daba.quantity<dasa.quantity) //Check if the buyer needs less quantity than seller
                            {
                                dasa.quantity-=daba.quantity;  //Reduce the quantity in the sale
                                CurrentInventoryList cil=daba.apprentice.currentInventoryList.get(i);
                                int cost=(daba.price+dasa.price)/2; //Calculate the cost for the sale
                                cil.lock();
                                cil.quantity+=daba.quantity; //Update the quantity bought for the buyer
                                cil.unlock();
                                FutureInventoryList fil=daba.apprentice.futureInventoryList.get(i);
                                fil.lock();
                                fil.quantity-=daba.quantity; //Update the goal for the buyer
                                fil.unlock();
                                daba.apprentice.lock();
                                daba.apprentice.score+=(fil.buyingTargetPrice-cost)*daba.quantity;//Update score for buyer
                                daba.apprentice.unlock();
                                cil=dasa.wizard.currentInventoryList.get(i);
                                cil.lock();
                                cil.quantityLocked-=daba.quantity; //Update the quantity locked for the seller
                                cil.unlock();
                                dasa.wizard.lock();
                                dasa.wizard.score+=(cost-cil.sellingPriceTarget)*daba.quantity; //Update score for seller
                                dasa.wizard.unlock();
                                daba.quantity=0; //Update the bid quantity
                            }
                            else
                            {
                                daba.quantity-=dasa.quantity; //Reduce the bid quantity
                                CurrentInventoryList cil=daba.apprentice.currentInventoryList.get(i);
                                int cost=(daba.price+dasa.price)/2; //Calculate the cost for the sale
                                cil.lock();
                                cil.quantity+=dasa.quantity; //Update the quantity bought for the buyer
                                cil.unlock();
                                FutureInventoryList fil=daba.apprentice.futureInventoryList.get(i);
                                fil.lock();
                                fil.quantity-=dasa.quantity; //Update the goal for the buyer
                                fil.unlock();
                                daba.apprentice.lock();
                                daba.apprentice.score+=(fil.buyingTargetPrice-cost)*daba.quantity;//Update score for buyer
                                daba.apprentice.unlock();
                                cil=dasa.wizard.currentInventoryList.get(i);
                                cil.lock();
                                cil.quantityLocked-=daba.quantity; //Update the quantity locked for the seller
                                cil.unlock();
                                dasa.wizard.lock();
                                dasa.wizard.score+=(cost-cil.sellingPriceTarget)*daba.quantity; //Update score for seller
                                dasa.wizard.unlock();
                                dasa.quantity=0; //Update the sale quantity
                            }
                        }
                    }
                    else
                    {
                        CurrentInventoryList cil=dasa.wizard.currentInventoryList.get(i);
                        cil.quantity+=dasa.quantity; //Update the quantity because the sale was invalid
                        cil.quantityLocked-=dasa.quantity; //Update the quantitylocked because the sale was invalid
                        dasa.quantity=0;
                    }
                }
            }
            else
            {
                  FutureInventoryList fil=daba.apprentice.futureInventoryList.get(i);
                  fil.quantity+=daba.quantity; //Update the target quantity for the seller because the bid was invalid.
                  fil.quantityLocked+=daba.quantity; //Update the quantityLocked for the buyer
                  daba.quantity=0;
            }
        }
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
