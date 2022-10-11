package bomberman;

import bomberman.components.PlayerComponent;
import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyDef;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static bomberman.BombermanType.*;
import static bomberman.BombermanApp.*;
import static bomberman.Constants.*;
import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.geto;


/**
 * @author nghia
 */
public class BombermanFactory implements EntityFactory {

    private final int radius = SIZE_BLOCK/2;

    @Spawns("block")
    public Entity newBlock(SpawnData data) {
        var width = (int) data.get("width");
        var height = (int) data.get("height");

        return FXGL.entityBuilder(data)
                .type(BombermanType.BLOCK)
                .bbox(new HitBox(BoundingShape.box(width, height)))
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .build();
    }


    @Spawns("BG")
    public Entity newBackground(SpawnData data) {
        return FXGL.entityBuilder(data)
                .at(0, 0)
                .view(new Rectangle(9*48,9*48, Color.LIGHTGRAY))
                .zIndex(-10)
                .build();
    }



    @Spawns("Player")
    public Entity newPlayer(SpawnData data) {
        var physics = new PhysicsComponent();
        var fixtureDef = new FixtureDef();
        fixtureDef.setFriction(0);
        fixtureDef.setDensity(0.1f);
        physics.setFixtureDef(fixtureDef);

        var bodyDef = new BodyDef();
        bodyDef.setFixedRotation(true);
        bodyDef.setType(BodyType.DYNAMIC);
        physics.setBodyDef(bodyDef);


        return FXGL.entityBuilder(data)
                .type(PLAYER)
                .bbox(new HitBox(BoundingShape.circle(radius)))
                .atAnchored(new Point2D(radius, radius), new Point2D(radius, radius))
                .with(new CellMoveComponent(SIZE_BLOCK, SIZE_BLOCK, SPEED))
                .with(physics)
                .with(new PlayerComponent())
                .with(new CollidableComponent(true))
                .with(new AStarMoveComponent(new LazyValue<>(() -> geto("grid"))))
                .zIndex(5)
                .build();




    }

    @Spawns("surround")
    public Entity newSurround(SpawnData data) {
        var width = (int) data.get("width");
        var height = (int) data.get("height");

        return FXGL.entityBuilder(data)
                .type(BombermanType.SURROUND)
                .bbox(new HitBox(BoundingShape.box(width, height)))
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("wall")
    public Entity newWall(SpawnData data) {
        var width = (int) data.get("width");
        var height = (int) data.get("height");

        return FXGL.entityBuilder(data)
                .type(BombermanType.WALL)
                .bbox(new HitBox(BoundingShape.box(width, height)))
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .build();
    }






}
