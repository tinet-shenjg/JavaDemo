package alex.java.interview.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 锁demo
 *
 * @author 程序员Alex
 * @date 2020/05/04
 */
@Slf4j
public class LockDemo {
    public static void main(String[] args) {
        Object r1 = new Object();
        Object r2 = new Object();

        // 线程T1
        new Thread(() -> {
            synchronized (r1) {
                System.out.println(Thread.currentThread().getName() + "获取 r1 成功");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    log.error("线程" + Thread.currentThread().getName() + "发生异常:" + e.getMessage(), e);
                }
                // 试图获取锁 r2
                synchronized (r2) {
                    System.out.println(Thread.currentThread().getName() + "获取 r2 成功");
                }
            }
        }).start();

        // 线程T2
        new Thread(() -> {
            synchronized (r2) {
                System.out.println(Thread.currentThread().getName() + "获取 r2 成功");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    log.error("线程" + Thread.currentThread().getName() + "发生异常:" + e.getMessage(), e);
                }
                // 试图获取锁 r1
                synchronized (r1) {
                    System.out.println(Thread.currentThread().getName() + "获取 r1 成功");
                }
            }
        }).start();
    }
}
