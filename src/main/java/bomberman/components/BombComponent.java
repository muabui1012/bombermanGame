package bomberman.components;

import bomberman.BombermanType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static bomberman.Constants.*;
import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

/**
 * @author nghia
 */
public class BombComponent extends Component {

    private int radius;

    private AnimatedTexture texture;
    private AnimationChannel anim;


    Entity wallBomb;
    public BombComponent() {
        onCollisionEnd(BombermanType.BOMB, BombermanType.PLAYER, (b, p) -> {
            if (entity != null) {
                wallBomb = FXGL.spawn("wall_bomb", new SpawnData(entity.getX(), entity.getY()));
            }
        });


        anim = new AnimationChannel(image("sprites.png"), 16, SIZE_BLOCK, SIZE_BLOCK,
                Duration.seconds(0.7), 72, 74);
        texture = new AnimatedTexture(anim);
        texture.loop();

    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {

    }

    private ArrayList<Entity> listOfFlame = new ArrayList<>();
    private List<Entity> listRightBlock = new ArrayList<>();
    private List<Entity> listLeftBlock = new ArrayList<>();
    private List<Entity> listAboveBlock = new ArrayList<>();
    private List<Entity> listBottomBlock = new ArrayList<>();

    public void explode() {
        int flameX = (int) (entity.getX() % SIZE_BLOCK > SIZE_BLOCK / 2
                ? entity.getX() + SIZE_BLOCK - entity.getX() % SIZE_BLOCK + 1
                : entity.getX() - entity.getX() % SIZE_BLOCK + 1);
        int flameY = (int) (entity.getY() % SIZE_BLOCK > SIZE_BLOCK / 2
                ? entity.getY() + SIZE_BLOCK - entity.getY() % SIZE_BLOCK + 1
                : entity.getY() - entity.getY() % SIZE_BLOCK + 1);
//        Entity flame = spawn("flame", new SpawnData(flameX, flameY));
//        Entity UpperFlame = spawn("UpperFlame", new SpawnData(entity.getX(),entity.getY() - SIZE_BLOCK));
//        Entity LowerFlame = spawn("LowerFlame", new SpawnData(entity.getX() ,entity.getY() + SIZE_BLOCK));
//        Entity LeftFlame = spawn("LeftFlame", new SpawnData(entity.getX() - SIZE_BLOCK,entity.getY()));
//        Entity RightFlame = spawn("RightFlame", new SpawnData(entity.getX() + SIZE_BLOCK,entity.getY()));

            listOfFlame.add(spawn("flame", new SpawnData(entity.getX(),entity.getY())));
                listOfFlame.add(spawn("LeftFlame", new SpawnData(entity.getX() - SIZE_BLOCK,entity.getY())));
                listOfFlame.add(spawn("RightFlame", new SpawnData(entity.getX() + SIZE_BLOCK,entity.getY())));
                listOfFlame.add(spawn("UpperFlame", new SpawnData(entity.getX(),entity.getY() - SIZE_BLOCK)));
                listOfFlame.add(spawn("LowerFlame", new SpawnData(entity.getX() ,entity.getY() + SIZE_BLOCK)));

        RemoveFlame();
    }

    private void RemoveFlame() {
        FXGL.getGameTimer().runOnceAfter(() -> {
            for (Entity e : listOfFlame) {
                e.removeFromWorld();
            }
        }, Duration.seconds(0.2));
        if (wallBomb != null) wallBomb.removeFromWorld();
        entity.removeFromWorld();
    }

}
