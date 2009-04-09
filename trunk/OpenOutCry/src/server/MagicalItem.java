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
    }
    
     /**
     * Checks if a bid for a magical item meets the expectation of the seller.
     */       
    void executeTrade()
    {
        lock();
        for(int i=0; i<apprentice.size(); i++)
        {
            DiagonAlleyBuyerAccount daba=apprentice.get(i);
            if(daba.time.before(new java.util.Date()))
            {
                for(int j=0; j<wizards.size(); j++)
                {
                    DiagonAlleySellerAccount dasa=wizards.get(i);
                    if(daba.price<=dasa.price)
                    {
                        //Execute trade
                        if(daba.quantity<dasa.quantity)
                        {
                            dasa.quantity-=daba.quantity;
                            CurrentInventoryList cil=daba.apprentice.currentInventoryList.get(i);
                            cil.cost=(daba.price+dasa.price)/2;
                            cil.quantity+=daba.quantity;
                            FutureInventoryList fil=daba.apprentice.futureInventoryList.get(i);
                            fil.quantity-=daba.quantity;
                            daba.quantity=0;
                        }
                        else
                        {
                            daba.quantity-=dasa.quantity;
                            CurrentInventoryList cil=daba.apprentice.currentInventoryList.get(i);
                            cil.cost=(daba.price+dasa.price)/2;
                            cil.quantity+=dasa.quantity;
                            FutureInventoryList fil=daba.apprentice.futureInventoryList.get(i);
                            fil.quantity-=dasa.quantity;
                            dasa.quantity=0;
                        }
                    }
                }
            }
            else
            {
                  FutureInventoryList fil=daba.apprentice.futureInventoryList.get(i);
                  fil.quantity+=daba.quantity;
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
