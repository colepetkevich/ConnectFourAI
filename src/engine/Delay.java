package engine;

public class Delay
{
    private float time;

    public Delay(float delayTime, Runnable postloopBlock, Scene scene)
    {
        time = 0.0f;

        Coroutine delay = new Coroutine(
                () -> time < delayTime,
                () -> {
                    time += scene.TIME.deltaTime();
                }, postloopBlock, scene);

        delay.start();
    }
}
