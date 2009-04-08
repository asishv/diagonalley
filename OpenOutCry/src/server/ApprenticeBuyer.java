/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

/**
 *
 * @author Asish
 */
public class ApprenticeBuyer extends Everyone{
    ApprenticeBuyer(String name)
    {
        wizardOrNot = false;
        this.name=name;
    }
    
    /**
     * Diagon Alley Apprentice Buyers place a bid to buy a magical item.
     */       
    void bid(int price, int quantity, int magicalItemNumber)
    {
        
    }
    
    /**
     * Diagon Alley Apprentice Buyers modify an existing bid for a magical item.
     */       
    void modifyBid(int price, int quantity, int magicalItemNumber)
    {
        
    }
}
