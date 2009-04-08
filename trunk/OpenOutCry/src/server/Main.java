/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import java.util.Date;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;

/**
 *
 * @author Asish
 */
public class Main extends Thread implements java.rmi.Remote{
     Date startTime;
     MagicalItem[] magicalItems;
     public static final int MAX_COMMODITY = 20;
     ArrayList<WizardSeller> wizards;
     ArrayList<ApprenticeBuyer> apprentices;
     ArrayList<MagicalItemInfo> magicalItemInfo;
     volatile int numberOfUsers=0;

     /**
     * Creates the Diagon Alley Wizards Sellers.
     */    
    void createWizards(String name)
    {
       WizardSeller ws=new WizardSeller(name);
       Random random = new Random();
       int commodity=(random.nextInt())%20, quantity, cost;
  
       int apprenticeNo=findApprentice(magicalItems[commodity].magicalItemInfo);
       if(apprenticeNo == -1)
       {
           quantity=(random.nextInt()+100)%1000;
           cost=(random.nextInt()+100)%1000;
       }
       else
       {
           ApprenticeBuyer ab=apprentices.get(apprenticeNo);
           cost=ab.getTargetCost()-(random.nextInt())%10;
           quantity=ab.getTargetQuantity();           
       }
       CurrentInventoryList ci=new CurrentInventoryList(cost, quantity, magicalItems[commodity].magicalItemInfo);
       ws.currentInventoryList.add(ci);
       wizards.add(ws);
       DiagonAlleySellerAccount dasa=new DiagonAlleySellerAccount(ws);
       magicalItems[commodity].wizards.add(dasa);
    }
    
     /**
     * Finds the wizard selling a particular magical item.
     */    
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

    /**
     * Finds the apprentice buying a particular magical item.
     */    
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
     * Register a user in Diagon Alley.
     */    
    synchronized void register(String name)
    {
        numberOfUsers++;
        if(numberOfUsers%2 == 0)
            createWizards(name);
        else
            createApprentices(name);
    }
    
     /**
     * Creates the Diagon Alley Apprentice Buyers.
     */    
    void createApprentices(String name)
    {
       ApprenticeBuyer ab=new ApprenticeBuyer(name);
       Random random = new Random();
       int commodity=(random.nextInt())%20, quantity, cost;
       int wizardNo=findWizard(magicalItems[commodity].magicalItemInfo);
       if(wizardNo == -1)
       {
           quantity=(random.nextInt()+100)%1000;
           cost=(random.nextInt()+100)%1000;
       }
       else
       {
           WizardSeller ws=wizards.get(wizardNo);
           cost=ws.getTargetCost()+(random.nextInt())%10;
           quantity=ws.getTargetQuantity();
       }
       FutureInventoryList fi=new FutureInventoryList(cost, quantity, magicalItems[commodity].magicalItemInfo);
       ab.futureInventoryList.add(fi);
       apprentices.add(ab);        
       DiagonAlleyBuyerAccount daba=new DiagonAlleyBuyerAccount(ab);
       magicalItems[commodity].apprentice.add(daba);
    }
   
    /**
     * Creates the Diagon Alley magical items.
     */    
    void createMagicalItems()
    {
        magicalItems=new MagicalItem[MAX_COMMODITY];
         try{
            InputStream instream = getClass().getResourceAsStream("DiagonAlleyMagicalItems.csv");
            InputStreamReader infile = new InputStreamReader(instream);
            BufferedReader br = new BufferedReader(infile);
            for(int i=0; i<MAX_COMMODITY; i++)
            {
                magicalItems[i]=new MagicalItem();
                if(br.ready())
                {
                    String line=br.readLine();
                    String elem[]=line.split(",");
                    magicalItems[i].magicalItemInfo= new MagicalItemInfo(elem[0], elem[1], elem[2]);
                }

            }
        }catch(IOException e)
        {
            e.printStackTrace();
            System.exit(0);
        }        
    }
    
     /**
     * Creates a virtual wizard.
     */    
    void createVirtualWizards()
    {
     //TODO: Complete this code.   
    }

     /**
     * Creates a virtual apprentice.
     */    
    void createVirtualApprentices()
    {
     //TODO: Complete this code.   
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
        //TODO:Check if all apprentice and wizards have met their goals
        //if (true) stop the server
    }
    
    Main()
    {
        createMagicalWorld();
    }
}
