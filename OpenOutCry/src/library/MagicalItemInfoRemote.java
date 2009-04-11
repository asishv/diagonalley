/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package library;

import java.rmi.RemoteException;

/**
 *
 * @author karthik
 */
public interface MagicalItemInfoRemote {
    String getName() throws RemoteException;
    String getSymbol() throws RemoteException;
    String getPicture() throws RemoteException;
}
