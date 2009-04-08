/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;
import java.util.ArrayList;
import java.io.*;

/**
 *
 * @author Asish
 */
public class MagicalItem implements java.rmi.Remote{
    MagicalItemInfo magicalItemInfo;
    int averageSellingPrice;
    ArrayList<WizardSeller> wizards;
    ArrayList<ApprenticeBuyer> apprentice;
    
    MagicalItem()
    {
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
        
    }

    /**
     * Unlocks the magical item.
     */       
    public void unlock()
    {
        
    }
}
