package engine;

import java.util.concurrent.Callable;

public class Delay
{
    private float time;

    public Delay(float delayTime, Runnable postloopBlock, Scene scene)
    {
        time = 0.0f;

        Coroutine delay = new Coroutine(
                () -> time < delayTime,
                () -> {
                    time += Time.deltaTime();
                }, postloopBlock, scene);

        delay.start();
    }
}
