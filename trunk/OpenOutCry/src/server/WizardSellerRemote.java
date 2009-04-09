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
public interface WizardSellerRemote extends java.rmi.Remote{
    boolean trade(int price, int quantity, int magicalItemNumber, long m) throws RemoteException;
    boolean modifyTrade(int price, int quantity, int magicalItemNumber, long m) throws RemoteException;
    int getScore() throws RemoteException;
    int getTargetQuantity() throws RemoteException;
    int getTargetCost() throws RemoteException;
    int getTargetQuantityLocked() throws RemoteException;
    MagicalItemInfo getTargetCommodityInfo() throws RemoteException;
}
