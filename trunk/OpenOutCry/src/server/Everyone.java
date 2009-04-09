/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;
import java.util.ArrayList;
import java.util.concurrent.locks.*;
/**
 *
 * @author Asish
 */
public class Everyone implements java.rmi.Remote{
    String name;
    boolean wizardOrNot;
    private Lock l;
    volatile int score;
    Everyone()
    {
        l=new ReentrantLock();
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
