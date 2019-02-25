/*
    |---------------------------------------|
    |                 CHEFS                 |
    |---------------------------------------|
 */
package Logic;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Producer extends Thread {

    private Meson mes;
    private Semaphore mutex, sProd, sCon;
    private int time, type;
    private boolean hired;
    private String name;

    public Producer(Semaphore mutex, Semaphore sProd, Semaphore sCon, int type, String name) {
        this.mutex = mutex;
        this.sProd = sProd;
        this.sCon = sCon;
        this.type = type;
        this.hired = true;
        this.name = name;
    }

    public Producer(Meson mes, Semaphore mutex, Semaphore sProd, Semaphore sCon, int time, int type, String name) {
        this.mes = mes;
        this.mutex = mutex;
        this.sProd = sProd;
        this.sCon = sCon;
        this.time = time;
        this.type = type;
        this.hired = true;
        this.name = name;
    }

    @Override
    public void run() {
        while (this.hired) {
            try {
                this.sProd.acquire();
                Thread.sleep(this.time);
                this.mutex.acquire();
                //System.out.println("Prod " + this.getName() + ' ' + type + " || Queue => " + sProd.getQueueLength());
                switch (this.type) {
                    case 1:
                        this.mes.cook(Restaurant.ePointer, type);
                        Restaurant.ePointer = (Restaurant.ePointer + 1) % this.mes.getSize();
                        Restaurant.addEntryCount();
                        break;
                    case 2:
                        this.mes.cook(Restaurant.mPointer, type);
                        Restaurant.mPointer = (Restaurant.mPointer + 1) % this.mes.getSize();
                        Restaurant.addEntryCount();
                        break;
                    case 3:
                        this.mes.cook(Restaurant.dPointer, type);
                        Restaurant.dPointer = (Restaurant.dPointer + 1) % this.mes.getSize();
                        Restaurant.addDesCount();
                        break;
                }
                this.mutex.release();
                this.sCon.release();
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            } /*catch (ArrayIndexOutOfBoundsException aex) {
                System.out.println("Out of Bound at " + Restaurant.ePointer + ' ' + name + " || " + this.mes.getName());
            }*/
        }
    }

    public void Fire() {
        this.hired = false;
    }
}
