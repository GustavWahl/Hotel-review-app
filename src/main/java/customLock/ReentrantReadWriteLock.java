package customLock;

import java.util.HashMap;
import java.util.Map;

/**
 * A custom reentrant read/write lock that allows:
 * 1) Multiple readers (when there is no writer). Any thread can acquire multiple read locks
 * (if nobody is writing).
 * 2) One writer (when nobody else is writing or reading).
 * 3) A writer is allowed to acquire a read lock while holding the write lock.
 * 4) A writer is allowed to acquire another write lock while holding the write lock.
 * 5) A reader can NOT acquire a write lock while holding a read lock.
 *
 * Use ReentrantReadWriteLockTest to test this class.
 * The code is modified from the code of Prof. Rollins.
 */
public class ReentrantReadWriteLock {

    private int readers, writers;
    private Map<Long, int[]> threadMap;     //TODO remove from map if both become 0



    /**
     * Constructor for ReentrantReadWriteLock
     */
    public ReentrantReadWriteLock() {
        readers = 0;
        writers = 0;
        threadMap = new HashMap<>();
    }

    /**
     * Return true if the current thread holds a read lock.
     *
     * @return true or false
     */
    public synchronized boolean isReadLockHeldByCurrentThread() {

        int[] thread = threadMap.get(Thread.currentThread().getId());
        if (thread == null) return false;

        int isReading = thread[0];
        return isReading > 0;
    }

    /**
     * Return true if the current thread holds a write lock.
     *
     * @return true or false
     */
    public synchronized boolean isWriteLockHeldByCurrentThread() {

        int[] thread = threadMap.get(Thread.currentThread().getId());
        if (thread == null) return false;

        int isWriting = thread[1];

        return isWriting > 0;
    }

    /**
     * Non-blocking method that attempts to acquire the read lock and acquires it, if it can.
     * Returns true if successful.
     * Checks conditions (whether it can acquire the read lock), and if they are true,
     * acquires the lock (updates readers info).
     *
     * Note that if conditions are false (can not acquire the read lock at the moment), this method
     * does NOT wait, just returns false
     * @return
     */
    public synchronized boolean tryAcquiringReadLock() {
        long currentThreadId = Thread.currentThread().getId();


        if (isWriteLockHeldByCurrentThread()){
            int[] thread = threadMap.get(currentThreadId);
            if (thread == null){
                thread = new int[2];
                threadMap.put(currentThreadId,thread);

            }

            thread[0] = thread[0] + 1;

            readers++;

            return true;
        }

        else if (writers > 0) { //TODO combine the if
            return false;
        } else {

            int[] thread = threadMap.get(currentThreadId);
            if (thread == null) thread = new int[2];

            thread[0] = thread[0] + 1;

            threadMap.put(currentThreadId, thread);

            readers++;

            return true;
        }
    }

    /**
     * Non-blocking method that attempts to acquire the write lock, and acquires it, if it is available.
     * Returns true if successful.
     * Checks conditions (whether it can acquire the write lock), and if they are true,
     * acquires the lock (updates writers info).
     *
     * Note that if conditions are false (can not acquire the write lock at the moment), this method
     * does NOT wait, just returns false
     *
     * @return
     */
    public synchronized boolean tryAcquiringWriteLock() {
        long currentThreadId = Thread.currentThread().getId();


        if (isWriteLockHeldByCurrentThread()){
            int[] thread = threadMap.get(currentThreadId);
            if (thread == null) thread = new int[2];
            thread[1] = thread[1] + 1;

            threadMap.put(currentThreadId,thread);
            writers++;
            return true; //TODO simplify / combine
        }

        if (writers > 0 || readers > 0) return false;


        int[] thread = threadMap.get(currentThreadId);
        if (thread == null) thread = new int[2];
        thread[1] = thread[1] + 1;

        threadMap.put(currentThreadId,thread);
        writers++;
        // lockWrite();
        //lockRead();

        return true; // do not forget to change this
    }

    /**
     * Blocking method that will return only when the read lock has been
     * acquired.
     * Calls tryAcquiringReadLock, and as long as it returns false, waits.
     * Catches InterruptedException.
     *
     */
    public synchronized void lockRead() {
        while (!tryAcquiringReadLock()){
            try {
                this.wait();
            }
            catch (InterruptedException e){
                System.out.println(e);
            }
        }

    }

    /**
     * Releases the read lock held by the calling thread. Other threads might
     * still be holding read locks. If no more readers after unlocking, calls notifyAll().
     */
    public synchronized void unlockRead() {

        long currentThreadId = Thread.currentThread().getId();

        if (readers == 0) return;

        int[] thread = threadMap.get(currentThreadId); // TODO handle null cases, defensive programming
        thread[0] = thread[0] - 1;

        threadMap.put(currentThreadId,thread);

        readers--;

        if (readers == 0)
            this.notifyAll();



    }

    /**
     * Blocking method that will return only when the write lock has been
     * acquired.
     * Calls tryAcquiringWriteLock, and as long as it returns false, waits.
     * Catches InterruptedException.
     */
    public synchronized void lockWrite() {
        while (!tryAcquiringWriteLock()){
            try {
                this.wait();
            }
            catch (InterruptedException e){
                System.out.println(e);
            }
        }

    }

    /**
     * Releases the write lock held by the calling thread. The calling thread
     * may continue to hold a read lock.
     * If the number of writers becomes 0, calls notifyAll.
     */

    public synchronized void unlockWrite() {
        if (writers == 0) return;

        long currentThreadId = Thread.currentThread().getId();

        int[] thread = threadMap.get(currentThreadId);
        thread[1] = thread[1] - 1;

        threadMap.put(currentThreadId,thread);
        writers--;
        if (writers == 0) {
            this.notifyAll();
        }
    }
}
