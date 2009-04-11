/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package library;
import java.io.Serializable;
import java.rmi.*;
import library.MagicalItemInfoRemote;


/**
 *
 * @author Asish
 */
public interface WizardSellerRemote extends java.rmi.Remote,Serializable{
    boolean trade(int price, int quantity, int magicalItemNumber, long m) throws RemoteException;
    boolean modifyTrade(int price, int quantity, int magicalItemNumber, long m) throws RemoteException;
    int getScore() throws RemoteException;
    int getTargetQuantity() throws RemoteException;
    int getTargetCost() throws RemoteException;
    int getTargetQuantityLocked() throws RemoteException;
    MagicalItemInfoRemote getTargetCommodityInfo() throws RemoteException;
}
