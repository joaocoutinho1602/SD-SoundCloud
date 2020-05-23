/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author anacesar
 * @author joaocoutinho
 */

public class ThreadPool {
    private final PoolWorker[] threads;
    private final LinkedBlockingQueue queueDownloads;
    private final int MAXDOWN = 3;
    public static int nDownloads = 0;
 
    public ThreadPool(int nThreads) {
        queueDownloads = new LinkedBlockingQueue(10);
        threads = new PoolWorker[nThreads];
 
        for (int i = 0; i < nThreads; i++) {
            threads[i] = new PoolWorker();
            threads[i].start();
        }
    }
 
    public void execute(Download download) {
        synchronized (queueDownloads) {
            queueDownloads.add(download);
            queueDownloads.notify();
        }
    }
 
    private class PoolWorker extends Thread {
        public void run() {
            Download download;
            while (true) {
                synchronized (queueDownloads) {
                    try {
                        while (queueDownloads.isEmpty() || nDownloads > MAXDOWN) {
                            queueDownloads.wait();
                        }
                        download = (Download) queueDownloads.poll();
                        download.downloadMedia();
                    } catch (InterruptedException ex) {
                    }

                }
            }
        }
    }
}