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
    
    int getTargetCost()
    {
        for(int i=0; i<currentInventoryList.size(); i++)
        {
            CurrentInventoryList cil=currentInventoryList.get(i);
            if (cil.sellingPriceTarget != 0)
            {
                return cil.cost;
            }
        }
        return -1;        
    }
    
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
