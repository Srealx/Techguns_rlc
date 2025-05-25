package techguns.task;

import net.minecraft.client.Minecraft;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author srealx
 * @date 2025/3/20
 */
public class FovTask {
    private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            1,
            1,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000));


    public static void fovChange(int originFov,int nextFov){
        int difference = originFov - nextFov;
        int absDiff = Math.abs(difference);
        int millis = difference >= 0 ? 5 : 3;
        int number = difference >= 0 ? -1 : 1;
        for (int i = 0; i < absDiff; i++) {
            threadPool.execute(()->{
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                    System.out.println("线程睡眠失败.....");
                    e.printStackTrace();
                }
                Minecraft.getMinecraft().gameSettings.fovSetting += number;
            });
        }
    }
}
