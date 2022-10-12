package bomberman;

import bomberman.components.BlockComponent;
import bomberman.components.BombComponent;
import bomberman.components.FlameComponent;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static bomberman.BombermanType.*;
import static bomberman.BombermanApp.*;
import static bomberman.components.BombComponent.*;
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


    @Spawns("brick")
    public Entity newBrick(SpawnData data) {
        var width = (int) data.get("width");
        var height = (int) data.get("height");

        return FXGL.entityBuilder(data)
                .type(BRICK)
                .bbox(new HitBox(BoundingShape.box(width, height)))
                .with(new BlockComponent(104, 104, 999))
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("bomb")
    public Entity newBomb(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BombermanType.BOMB)
                .with(new BombComponent())
                .bbox(new HitBox(new Point2D(2, 2), BoundingShape.circle(radius - 2)))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("wall_bomb")
    public Entity newWallBomb(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BombermanType.WALL_BOMB)
                //.view(new Rectangle(SIZE_BLOCK, SIZE_BLOCK, Color.RED))
                .bbox(new HitBox(new Point2D(0, 0), BoundingShape.box(SIZE_BLOCK, SIZE_BLOCK)))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("flame")
    public Entity newFlame(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(FLAME)
                .with(new FlameComponent(128, 130))
                .bbox(new HitBox(new Point2D(2, 2), BoundingShape.circle(radius - 4)))
                .with(new CollidableComponent(true))
                .build();

    }

    @Spawns("UpperFlame")
    public Entity newUpperFlame(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BombermanType.FLAME)
                .with(new FlameComponent(144, 146))
                .viewWithBBox(new Rectangle(SIZE_BLOCK / 2.0 - 3, SIZE_BLOCK / 2.0 - 3, Color.TRANSPARENT))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .with(new CollidableComponent(true))
                .zIndex(1)
                .build();
    }

    @Spawns("LowerFlame")
    public Entity newLowerFlame(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BombermanType.FLAME)
                .with(new FlameComponent(176, 178))
                .viewWithBBox(new Rectangle(SIZE_BLOCK / 2.0 - 3, SIZE_BLOCK / 2.0 - 3, Color.TRANSPARENT))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .with(new CollidableComponent(true))
                .zIndex(1)
                .build();
    }

    @Spawns("LeftFlame")
    public Entity newLeftFlame(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BombermanType.FLAME)
                .with(new FlameComponent(131, 133))
                .viewWithBBox(new Rectangle(SIZE_BLOCK / 2.0 - 3, SIZE_BLOCK / 2.0 - 3, Color.TRANSPARENT))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .with(new CollidableComponent(true))
                .zIndex(1)
                .build();
    }

    @Spawns("RightFlame")
    public Entity newRightEFlame(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BombermanType.FLAME)
                .with(new FlameComponent(163, 165))
                .viewWithBBox(new Rectangle(SIZE_BLOCK / 2.0 - 3, SIZE_BLOCK / 2.0 - 3, Color.TRANSPARENT))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .with(new CollidableComponent(true))
                .zIndex(1)
                .build();
    }

    @Spawns("brick_break")
    public Entity newBrickBreak(SpawnData data) {
        var boundingShape = BoundingShape.box(
                SIZE_BLOCK / 2.0f - 3,
                SIZE_BLOCK / 2.0f - 3);

        var hitBox = new HitBox(boundingShape);

        return FXGL.entityBuilder(data)
                .type(BombermanType.BRICK_BREAK)
                .with(new BlockComponent(105, 107, 1))
                .bbox(hitBox)
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .zIndex(1)
                .build();
    }


    @Spawns("balloon")
    public Entity newBalloon(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(ENEMY)
                .view(new Rectangle(40, 40, Color.RED))
                .bbox(new HitBox(new Point2D(2, 2), BoundingShape.circle(radius - 2)))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .with(new CollidableComponent(true))
                .build();
    }

}
