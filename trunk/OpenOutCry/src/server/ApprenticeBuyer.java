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
    
       int getTargetQuantity()
    {        
        for(int i=0; i<futureInventoryList.size(); i++)
        {
            FutureInventoryList fil=futureInventoryList.get(i);
            if (fil.buyingTargetPrice != 0)
            {
                return fil.quantity;
            }
        }
        return -1;
    }
    
    int getTargetCost()
    {
        for(int i=0; i<futureInventoryList.size(); i++)
        {
            FutureInventoryList fil=futureInventoryList.get(i);
            if (fil.buyingTargetPrice != 0)
            {
                return fil.buyingTargetPrice;
            }
        }
        return -1;        
    }
    
    MagicalItemInfo getTargetCommodityInfo()
    {
        for(int i=0; i<futureInventoryList.size(); i++)
        {
            FutureInventoryList fil=futureInventoryList.get(i);
            if (fil.buyingTargetPrice != 0)
            {
                return fil.magicalItem;
            }
        }
        return null;        
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
