package eliasyaoyc.github.io.smtp.utils;

import java.util.concurrent.ThreadFactory;

/**
 * @author: siran.yao
 * @date: 2020/6/26 21:48
 */
public class ThreadFactoryUtil {

    public static ThreadFactory createFactory(final String threadName){

        return new ThreadFactory(){

            int sequence;

            @Override
            public Thread newThread(Runnable r) {
                sequence += 1;

                StringBuilder sb =
                        new StringBuilder()
                                .append("[")
                                .append(Thread.currentThread().getThreadGroup().getName())
                                .append("] ")
                                .append(threadName).append(" - ").append(sequence);
                Thread thread = new Thread(r, sb.toString());

                thread.setDaemon(false);

                if (thread.getPriority() != Thread.NORM_PRIORITY){
                    thread.setPriority(Thread.NORM_PRIORITY);
                }

                return thread;
            }
        };
    }
}
