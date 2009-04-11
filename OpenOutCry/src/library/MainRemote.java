/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package library;
import java.io.Serializable;
import java.rmi.*;

/**
 *
 * @author Asish
 */
public interface MainRemote extends java.rmi.Remote, Serializable{
    EveryoneRemote register(String name) throws RemoteException;
    MagicalItemInfoRemote[] getAllMagicalItems() throws RemoteException;
}
