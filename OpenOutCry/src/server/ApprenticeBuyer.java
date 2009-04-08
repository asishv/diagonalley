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
     * Gets the target quantity to buy for the apprentice.
     */    
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

      /**
     * Gets the target cost for the magical item to buy for the apprentice.
     */    
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

    /**
     * Gets the target magical item to buy for the apprentice.
     */    
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
        MagicalItem m=Main.getMagicalItem(magicalItemNumber);
    }
    
    /**
     * Diagon Alley Apprentice Buyers modify an existing bid for a magical item.
     */       
    void modifyBid(int price, int quantity, int magicalItemNumber)
    {
        
    }
}
