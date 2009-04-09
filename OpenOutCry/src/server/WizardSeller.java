/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

/**
 *
 * @author Asish
 */
public class WizardSeller extends Everyone implements WizardSellerRemote{
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
                return cil.magicalItem.magicalItemInfo;
            }
        }
        return null;        
    }

    /**
     * Gets the current price for the magical item present on CIL
     */
    int getCurrentPrice(int magicalItemNumber)
    {
        CurrentInventoryList cil = this.currentInventoryList.get(magicalItemNumber);
        return cil.diagonAlleySellerAccount.price;
    }

    /**
     * Gets the current price for the magical item present on CIL
     */
    int getCurrentQuantity(int magicalItemNumber)
    {
        CurrentInventoryList cil = this.currentInventoryList.get(magicalItemNumber);
        return cil.diagonAlleySellerAccount.quantity;
    }

    /**
     * Diagon Alley Wizard Sellers trade a magical item.
     */
    public boolean trade(int price, int quantity, int magicalItemNumber, long m)
    {
        CurrentInventoryList cil = this.currentInventoryList.get(magicalItemNumber);
        if(cil == null) {
            //ERROR: Node doesnt exist.
            System.out.println("Error setting up trade");
            return false;
        }
        
        if(quantity > cil.quantity) {
            return false;
        }
        
        /* Obtain Lock, update the DiagonAlleySellerAccount values in Q,
         * update avg. price in Magical Item, unlock and execute trade.  */
        cil.magicalItem.lock();
        cil.magicalItem.averageSellingPrice = Math.min(cil.magicalItem.averageSellingPrice, price);
        /* Quantity of item decreases by 'x' in CIL, QuantityLocked will be incremented by 'x' */
        cil.quantity = cil.quantity - quantity;
        cil.quantityLocked = cil.quantityLocked + quantity;
        cil.diagonAlleySellerAccount.price = price;
        cil.diagonAlleySellerAccount.quantity = quantity;
        cil.diagonAlleySellerAccount.time.setTimeInMillis(cil.diagonAlleySellerAccount.time.getTimeInMillis()+m);
        cil.magicalItem.unlock();
        cil.magicalItem.executeTrade();
        return true;
    }

     /**
     * Diagon Alley Wizard Sellers modify existing trade for a magical item.
     */       
    public boolean modifyTrade(int price, int quantity, int magicalItemNumber, long m)
    {
        CurrentInventoryList cil = this.currentInventoryList.get(magicalItemNumber);
        if(cil == null) {
            return false;
        }
        int oldQuantity = cil.diagonAlleySellerAccount.quantity;
        if(quantity > cil.quantity + oldQuantity) {
            return false;
        }

        /* Obtain Lock, update the DiagonAlleySellerAccount values in Q,
         * update avg. price in Magical Item, unlock and execute trade. */
        cil.magicalItem.lock();
        cil.magicalItem.averageSellingPrice = Math.min(cil.magicalItem.averageSellingPrice, price);

        /* Modify the quantity and the quantity locked values in CIL */
        cil.quantity = cil.quantity + oldQuantity - quantity;
        cil.quantityLocked = cil.quantityLocked - oldQuantity + quantity;

        cil.diagonAlleySellerAccount.price = price;
        cil.diagonAlleySellerAccount.quantity = quantity;
        cil.diagonAlleySellerAccount.time.setTimeInMillis(cil.diagonAlleySellerAccount.time.getTimeInMillis()+m);
        cil.magicalItem.unlock();
        cil.magicalItem.executeTrade();
        return true;
    }
}
