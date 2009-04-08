/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

/**
 *
 * @author Asish
 */
public class WizardSeller extends Everyone{
    public WizardSeller(String name)
    {
        wizardOrNot=true;
        this.name=name;
    }

     /**
     * Gets the target quantity for the magical item to sell.
     */    
    int getTargetQuantity()
    {        
        for(int i=0; i<currentInventoryList.size(); i++)
        {
            CurrentInventoryList cil=currentInventoryList.get(i);
            if (cil.sellingPriceTarget != 0)
            {
                return cil.quantity;
            }
        }
        return -1;
    }

    /**
     * Gets the target cost for the magical item to sell.
     */    
    int getTargetCost()
    {
        for(int i=0; i<currentInventoryList.size(); i++)
        {
            CurrentInventoryList cil=currentInventoryList.get(i);
            if (cil.sellingPriceTarget != 0)
            {
                return cil.sellingPriceTarget;
            }
        }
        return -1;        
    }

      /**
     * Gets the target magical item to sell for the wizard.
     */    
      MagicalItemInfo getTargetCommodityInfo()
    {
        for(int i=0; i<currentInventoryList.size(); i++)
        {
            CurrentInventoryList cil=currentInventoryList.get(i);
            if (cil.sellingPriceTarget != 0)
            {
                return cil.magicalItem;
            }
        }
        return null;        
    }
    
     /**
     * Diagon Alley Wizard Sellers trade a magical item.
     */       
    void trade(int price, int quantity, int magicalItemNumber)
    {
        
    }

     /**
     * Diagon Alley Wizard Sellers modify existing trade for a magical item.
     */       
    void modifyTrade(int price, int quantity, int magicalItemNumber)
    {
        
    }

}
