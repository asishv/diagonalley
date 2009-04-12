/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package library;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author karthik
 */
public interface MagicalItemInfoRemote extends Remote,Serializable{
    String getName() throws RemoteException;
    String getSymbol() throws RemoteException;
    String getPicture() throws RemoteException;
    int getIndex() throws RemoteException;
}
