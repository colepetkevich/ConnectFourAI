package connectfour;

import engine.*;

public class PopUp extends Image
{
    private static final String POP_UP_PATH = "res/images/PopUp.png";

    public PopUp(Scene scene)
    {
        super(scene);

        //default image
        setImage(POP_UP_PATH);
    }

    public PopUp(Drawable parent, Scene scene)
    {
        super(parent, scene);

        //default image
        setImage(POP_UP_PATH);
    }

    //spawn coroutine variables
    private static float INITIAL_STEP = 0f;
    private static float STEP_DISTANCE = (float) (2f / 3 * Math.PI);
    private float time = 0.0f;
    private Vector2 finalScale;
    public void spawn(float spawnTime)
    {
        //setting scale to zero
        finalScale = getLocalScale();
        setLocalScale(Vector2.ZERO);

        float demon = (float) (Math.sin(INITIAL_STEP + STEP_DISTANCE));

        //creating the drop coroutine
        Coroutine spawn = new Coroutine(
                //loop condition: y position is greater or equal to finalY position
                () -> time <= spawnTime,
                //loop block: change velocity and y position depending on the change in time
                () -> {
                    time += scene.TIME.deltaTime();
                    setLocalScale((float) Math.sin(time / spawnTime * (INITIAL_STEP + STEP_DISTANCE)) / demon,
                            (float) Math.sin(time / spawnTime * (INITIAL_STEP + STEP_DISTANCE)) / demon);
                },
                //post loop block: set token to finalY position
                () -> {
                    setLocalScale(finalScale);
                }, scene);

        //starting the spawn coroutine
        spawn.start();
    }
}
