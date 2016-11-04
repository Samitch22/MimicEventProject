/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eventqueueserver;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Mitchell
 */
public class MimicEventQueue {
    
    private final ConcurrentLinkedQueue q;
    private final Lock lock = new ReentrantLock();
    
    /**
     *
     */
    public MimicEventQueue() {
        q = new ConcurrentLinkedQueue();
    }

    /**
     *
     * @return
     */
    public ConcurrentLinkedQueue getQ() {
        return q;
    }
    
    public void push(Object me) {
        q.add(me);
    }
    
    public synchronized Object pop() {
        Object returnObj;
        
        try {
            lock.tryLock(10, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            System.out.println("Interrupted Exception: " + ex.getMessage());
        }
        
        while ( q.isEmpty() ) {}
        
        returnObj = q.remove();
        System.out.println("Out to client: " + returnObj.toString());
        lock.unlock();
        return returnObj;
    }
    
    public synchronized Boolean isEmpty() {
        return q.isEmpty();
    }
}
