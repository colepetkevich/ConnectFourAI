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
    private float time = 0.0f;
    private Vector2 deltaScale;
    public void spawn(Vector2 initialScale, Vector2 finalScale, float spawnTime)
    {
        //setting scale to initialScale and calculating deltaScale
        setLocalScale(initialScale);
        deltaScale = new Vector2(finalScale.x - initialScale.x, finalScale.y - initialScale.y);

        //creating the drop coroutine
        Coroutine spawn = new Coroutine(
                //loop condition: y position is greater or equal to finalY position
                () -> time <= spawnTime,
                //loop block: change velocity and y position depending on the change in time
                () -> {
                    time += scene.TIME.deltaTime();
                    setLocalScale(new Vector2(getLocalScale().x + deltaScale.x * scene.TIME.deltaTime() / spawnTime,
                            getLocalScale().y + deltaScale.y * scene.TIME.deltaTime() / spawnTime));
                },
                //post loop block: set token to finalY position
                () -> {
                    setLocalScale(finalScale);
                }, scene);

        //starting the spawn coroutine
        spawn.start();
    }
}
