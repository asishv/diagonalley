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
    int averageSellingPrice;
    ArrayList<DiagonAlleySellerAccount> wizards;
    ArrayList<DiagonAlleyBuyerAccount> apprentice;
    Lock l;
    
    MagicalItem()
    {
        l=new ReentrantLock();
    }
    
     /**
     * Checks if a bid for a magical item meets the expectation of the seller.
     */       
    void executeTrade()
    {
        
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
