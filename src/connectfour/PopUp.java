package connectfour;

/**
 * @Author Cole Petkevich, Zebadiah Quiros, Kestt Van Zyl
 */

import engine.*;

public class PopUp extends Image
{
    private static final String POP_UP_PATH = "res/images/PopUp.png";

    public PopUp(Scene scene)
    {
        super(scene);

        //default image and visibility
        setImage(POP_UP_PATH);
        setLocalScale(Vector2.ZERO);
        setVisibility(false);
    }

    public PopUp(Drawable parent, Scene scene)
    {
        super(parent, scene);

        //default image and visibility
        setImage(POP_UP_PATH);
        setLocalScale(Vector2.ZERO);
        setVisibility(false);
    }

    //spawn coroutine variables
    private static float INITIAL_STEP = 0f;
    private static float STEP_DISTANCE = (float) (2f / 3 * Math.PI);
    private float time;
    private Vector2 finalScale;
    public void spawn(float spawnTime, Vector2 finalScale)
    {
        //resetting time
        time = 0;

        //setting scale to zero
        this.finalScale = finalScale;
        setLocalScale(Vector2.ZERO);

        //making visibility
        setVisibility(true);

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


    public void despawn(float spawnTime)
    {
        //resetting time
        time = 0;

        //setting finalScale to zero
        finalScale = Vector2.ZERO;

        float demon = (float) (Math.sin(INITIAL_STEP + STEP_DISTANCE));

        //creating the drop coroutine
        Coroutine despawn = new Coroutine(
                //loop condition: y position is greater or equal to finalY position
                () -> time <= spawnTime,
                //loop block: change velocity and y position depending on the change in time
                () -> {
                    time += scene.TIME.deltaTime();
                    setLocalScale((float) Math.sin((spawnTime - time) / spawnTime * (INITIAL_STEP + STEP_DISTANCE)) / demon,
                            (float) Math.sin((spawnTime - time) / spawnTime * (INITIAL_STEP + STEP_DISTANCE)) / demon);
                },
                //post loop block: set token to finalY position
                () -> {
                    setLocalScale(finalScale);
                }, scene);

        //starting the spawn coroutine
        despawn.start();
    }
}
