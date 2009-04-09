/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

/**
 *
 * @author Asish
 */
public class Inventory {
    MagicalItem magicalItem;
    volatile int quantity;
    volatile int quantityLocked;
}
