/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import java.util.Date;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Asish
 */
public class Main extends Thread implements java.rmi.Remote{
     Date startTime;
     MagicalItem[] magicalItems;
     final int MAX_COMMODITY = 20;
     ArrayList<WizardSeller> wizards;
     ArrayList<ApprenticeBuyer> apprentices;
     /**
     * Creates the Diagon Alley Wizards Sellers.
     */    
    void createWizards(String name)
    {
       WizardSeller ws=new WizardSeller(name);
       Random random = new Random();
       int commodity=(random.nextInt()+1)%20, quantity, cost;
  
       int apprenticeNo=findApprentice(magicalItems[commodity].magicalItemInfo);
       quantity=(random.nextInt()+1)%1000;
       cost=(random.nextInt()+1)%1000;
       CurrentInventoryList ci=new CurrentInventoryList(cost, quantity, magicalItems[commodity].magicalItemInfo);
       ws.currentInventoryList.add(ci);
       wizards.add(ws);
    }
    
    int findWizard(MagicalItemInfo m)
    {
        for(int i=0; i<wizards.size(); i++)
        {
            WizardSeller ws=wizards.get(i);
            if(ws.getTargetCommodityInfo() == m)
                return i;
        }
        return -1;
    }
    
    int findApprentice(MagicalItemInfo m)
    {
        for(int i=0; i<apprentices.size(); i++)
        {
            ApprenticeBuyer ab=apprentices.get(i);
            if(ab.getTargetCommodityInfo() == m)
                return i;
        }
        return -1;
    }

    
     /**
     * Creates the Diagon Alley Apprentice Buyers.
     */    
    void createApprentices(String name)
    {
       ApprenticeBuyer ab=new ApprenticeBuyer(name);
       Random random = new Random();
       int commodity=(random.nextInt()+1)%20, quantity, cost;
       int wizardNo=findWizard(magicalItems[commodity].magicalItemInfo);
       if(wizardNo == -1)
       {
           quantity=(random.nextInt()+1)%1000;
           cost=(random.nextInt()+1)%1000;
       }
       else
       {
           WizardSeller ws=wizards.get(wizardNo);
           cost=ws.getTargetCost()+random.nextInt()+1;
           quantity=ws.getTargetQuantity();
       }
       FutureInventoryList fi=new FutureInventoryList(cost, quantity, magicalItems[commodity].magicalItemInfo);
       ab.futureInventoryList.add(fi);
       apprentices.add(ab);        
    }
   
    /**
     * Creates the Diagon Alley magical items.
     */    
    void createMagicalItems()
    {
        magicalItems=new MagicalItem[MAX_COMMODITY];
        for(int i=0; i<MAX_COMMODITY; i++)
        {
            magicalItems[i]=new MagicalItem();
            magicalItems[i].magicalItemInfo=new MagicalItemInfo();
            magicalItems[i].populateMagicalItemInfo();
        }
        
    }
    
    void createVirtualWizards()
    {
        
    }
    
    void createVirtualApprentices()
    {
        
    }

    /**
     * Creates the Diagon Alley magical world.
     */    
    void createMagicalWorld()
    {
        createMagicalItems();
        createVirtualWizards();
        createVirtualApprentices();
    }
    
    public void run()
    {
        //Check if all apprentice and wizards have met their goals
        //if (true) stop the server
    }
    
    Main()
    {
        
    }
}
