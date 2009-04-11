/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;
import library.EveryoneRemote;
import java.util.ArrayList;
import java.util.concurrent.locks.*;
/**
 *
 * @author Asish
 */
public class Everyone implements EveryoneRemote{
    String name;
    boolean wizardOrNot;
    private Lock l;
    volatile int score;
    
    public boolean isWizard()
    {
        return wizardOrNot;
    }
    
    Everyone()
    {
        l=new ReentrantLock();
        currentInventoryList=new ArrayList();
        futureInventoryList=new ArrayList();
    }
    
    void lock()
    {
        l.lock();
    }
    
    void unlock()
    {
        l.unlock();
    }
    
    ArrayList<CurrentInventoryList> currentInventoryList;
    ArrayList<FutureInventoryList> futureInventoryList;
}
