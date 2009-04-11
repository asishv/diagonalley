/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;
import java.io.Serializable;
import java.util.concurrent.locks.*;

/**
 *
 * @author Asish
 */
public class Inventory implements Serializable{
    private Lock l;
    Inventory()
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

    MagicalItem magicalItem;
    volatile int quantity;
    volatile int quantityLocked;
}
