package com.urise.webapp;

public class DeadLockExample {
    private static Integer counter1 = new Integer(1);
    private static Integer counter2 = new Integer(1);

    private static void incAndGet(Integer counter1, Integer counter2) {
        System.out.println(Thread.currentThread().getName());
        synchronized (counter1) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (counter2) {
                System.out.println(++counter1 + ++counter2);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> incAndGet(counter1, counter2));
        Thread thread2 = new Thread(() -> incAndGet(counter2, counter1));

        thread1.start();
        thread2.start();

        Thread.sleep(1000);
        System.out.println(thread1.getState());
        System.out.println(thread2.getState());
    }

}
