/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;
import java.util.ArrayList;
/**
 *
 * @author Asish
 */
public class Everyone implements java.rmi.Remote{
    String name;
    boolean wizardOrNot;
    volatile int score;
    Everyone()
    {
    }
    
    ArrayList<CurrentInventoryList> currentInventoryList;
    ArrayList<FutureInventoryList> futureInventoryList;
}
