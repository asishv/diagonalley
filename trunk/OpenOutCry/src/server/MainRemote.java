/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;
import java.rmi.*;

/**
 *
 * @author Asish
 */
public interface MainRemote extends java.rmi.Remote{
    Everyone register(String name) throws RemoteException;
    MagicalItemInfo[] getAllMagicalItems() throws RemoteException;
}
