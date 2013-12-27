package com.pj.Tank.render;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.pj.Tank.activity.BattleActivity;
import com.pj.Tank.activity.R;
import com.pj.Tank.entity.GameMap;
import com.pj.Tank.entity.Obstacle;
import com.pj.Tank.entity.Point;
import com.pj.Tank.entity.Tank;
import com.pj.Tank.logic.GlobalEnvironment;
import com.pj.Tank.logic.impl.UIOperationImpl;
import com.rs.anergine.io.ComponentSet;
import com.rs.anergine.io.IO;
import com.rs.anergine.io.Position;
import com.rs.anergine.io.component.Button;
import com.rs.anergine.io.component.ButtonListener;
import com.rs.anergine.io.component.ToggleButton;
import com.rs.anergine.render.Renderer;
import com.rs.anergine.render.model.ModelParticle;
import com.rs.anergine.render.model.ModelTexture;
import com.rs.anergine.render.pass.Pass;
import com.rs.anergine.render.util.ThirdPersonCamera;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameRenderer implements com.rs.anergine.GameRenderer {

    public static final float[] SHADOW_COLOR = new float[]{0, 0, 0, 0.35f};
    private static final float l = 1f/ (float) Math.sqrt(3*3+4*4+5*5);
    public static final float[] lightDirection = new float[] { 3*l,4*l,5*l, 0 };
    public static float[] matrixShadow = new float[] {
            lightDirection[1],  0,  0,                  0,
            -lightDirection[0], 0,  -lightDirection[2], 0,
            0,                  0,  lightDirection[1],  0,
            0,                  0,  0,                  lightDirection[1]
    };

    private IO io;
    private ComponentSet cs = new ComponentSet();
    private ThirdPersonCamera gameCamera;

    private ModelSkyBox skyBox;
    private ModelTank tankModel;
    private ArrayList<ModelBlock> obstacles = new ArrayList<ModelBlock>();
    private ModelTexture terrain;
    private LinkedList<ModelParticle> particles = new LinkedList<ModelParticle>();

    public GameRenderer(IO io) {
        this.io = io;
    }

    UIOperationImpl uiOperation = new UIOperationImpl();

    private boolean powerState = false;
    private boolean fire = false;

    @Override
    public void load() {
        ButtonListener powerListener = new ButtonListener() {
            @Override
            public void down() {
                powerState = true;
            }
            @Override
            public void up() {
                powerState = false;
            }
        };
        cs.add(new ToggleButton(new Position(0.3f, -0.7f, Position.PositionType.LEFT), 0.4f, 0.4f, powerListener, R.drawable.power_up, R.drawable.power_down));

        ButtonListener fireListener = new ButtonListener() {
            @Override
            public void down() {
            }
            @Override
            public void up() {
                fire = true;
            }
        };
        cs.add(new Button(new Position(-0.3f, -0.7f, Position.PositionType.RIGHT), 0.4f, 0.4f, fireListener, R.drawable.fire_up, R.drawable.fire_down));

        io.setComponentSet(cs);


        GameMap gameMap = GlobalEnvironment.gameMap;

        terrain = new ModelTexture(R.raw.terrain, R.drawable.terrain);
        float[] matrixWorld = terrain.matrixWorld;
        Matrix.setIdentityM(matrixWorld, 0);
        Matrix.translateM(matrixWorld, 0, gameMap.getWidth() / 2, GlobalEnvironment.TANK_RADIUS / 5, gameMap.getHeight() / 2);
        float sqrt2 = (float) Math.sqrt(2);
        Matrix.scaleM(matrixWorld, 0, GlobalEnvironment.TANK_RADIUS * sqrt2 + gameMap.getWidth() / 2, GlobalEnvironment.TANK_RADIUS / 5, GlobalEnvironment.TANK_RADIUS * sqrt2 + gameMap.getHeight() / 2);
//        Matrix.rotateM(matrixWorld, 0, 90, 1, 0, 0);

        tankModel = new ModelTank();

        ArrayList<Obstacle> obstacles = gameMap.getObstacles();
        for(Obstacle obstacle : obstacles) {
            float left = obstacle.getTopLeft().getX();
            float top = obstacle.getTopLeft().getY();
            float right = obstacle.getBottomRight().getX();
            float bottom = obstacle.getBottomRight().getY();

            this.obstacles.add(new ModelBlock(left, 0, top, right, 100f, bottom, R.drawable.stone));
        }

        skyBox = new ModelSkyBox(R.raw.cubemodel, R.drawable.skybox);

    }

    @Override
    public void onChangeSetCamera() {
        Renderer.getInstance().useGameCamera();
        gameCamera = Renderer.getInstance().getCamera();
        gameCamera.setDistance(400);
        gameCamera.setTarget(1000, 0, 1000);
        gameCamera.setUp(0, 1, 0);
        gameCamera.setFovy(45f);
        gameCamera.setZFar(20000f);
        gameCamera.setZNear(1f);
        gameCamera.setAlpha((float) (Math.PI * 5 / 4f));
        gameCamera.setBeta(0.1f);

        Renderer.getInstance().useIoCamera();
        ThirdPersonCamera ioCamera = Renderer.getInstance().getCamera();
        ioCamera.setAttributes(90.0f, Position.aspect, 0.1f, 100.0f);
        ioCamera.setUp(0.0f, 1.0f, 0.0f);
        ioCamera.setAlpha((float) (Math.PI / 2));
        ioCamera.setTarget(0.0f, 0.0f, 0.0f);
        ioCamera.setZFar(2f);
        ioCamera.setZNear(0.1f);
        ioCamera.setDistance(1f);
    }

    private long lastShoot = -1;
    @Override
    public void render() {
        if(fire && (Renderer.getInstance().getFrameStartTime() - lastShoot < 500))
            fire = false;
        if(fire)
            lastShoot = Renderer.getInstance().getFrameStartTime();
        boolean fire2 = fire;
        fire = false;
        float[] gravity = io.getGravity();
        float v = (float) (gravity[1]/10* Math.PI / 6 /1000);

        //TODO enable this line and start debugging!
        uiOperation.tankMove(powerState ? 2f : -0f, v, fire2);

        Renderer.getInstance().useGameCamera();

        //set camera
        Tank stank = GlobalEnvironment.tanks.get(GlobalEnvironment.selfTankId);
        if(stank!=null) {

            gameCamera.setTarget(stank.getX(), 100, stank.getY());
            gameCamera.setAlpha((float) ((stank.getHeadDirection() + 180) * Math.PI / 180f));

            if(fire2) {
                shoot(stank);
            }

            if(stank.getBeShooted()!=0 || fire2) {
                io.vibrate(50);
            }

            if(stank.getHP() < 0) {
                BattleActivity.instance.exit();
            }
        }

        //Pass Terrain
        terrain.draw(Pass.TERRAIN);
        GLES20.glEnable(GLES20.GL_STENCIL_TEST);
        GLES20.glStencilFunc(GLES20.GL_ALWAYS, 0xff, 0xff);
        GLES20.glStencilOp(GLES20.GL_REPLACE, GLES20.GL_REPLACE, GLES20.GL_REPLACE);
        Renderer.getInstance().getPipelines().allFlush();
        GLES20.glDisable(GLES20.GL_STENCIL_TEST);


        skyBox.draw(Pass.TERRAIN);
        Renderer.getInstance().getPipelines().allFlush();

        //Pass Shadow
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_STENCIL_TEST);
        GLES20.glStencilFunc(GLES20.GL_EQUAL, 0xff, 0xff);
        GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_DECR, GLES20.GL_DECR);

        for(ModelBlock modelBlock : obstacles) {
            modelBlock.draw(Pass.SHADOW);
        }

        for(Tank tank : GlobalEnvironment.tanks.values()) {
            setTankBaseMatrix(tank);
            setTankTurretMatrix(tank);

            tankModel.draw(Pass.SHADOW);
            Renderer.getInstance().getPipelines().allFlush();
        }
        GLES20.glDisable(GLES20.GL_STENCIL_TEST);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);


        //Pass Tank
        for(ModelBlock modelBlock : obstacles) {
            modelBlock.draw(Pass.TANK);
        }
        for(Point point : GlobalEnvironment.explodingPoints) {
            particles.add(new ModelParticle(
                    100, new float[]{point.getX(), 50, point.getY()},
                    new float[]{0,0,0}, 50f,
                    new float[]{0,-200,0},
                    2000, new float[]{1,0.8431372549f,0,1},
                    3)
            );
        }

        for(Tank tank : GlobalEnvironment.tanks.values()) {
            setTankBaseMatrix(tank);
            setTankTurretMatrix(tank);
            if(tank.isShooting())
                shoot(tank);

            tankModel.draw(Pass.TANK);
            Renderer.getInstance().getPipelines().allFlush();
        }


        List<ModelParticle> finishedParticles = new ArrayList<ModelParticle>();
        for(ModelParticle particle : particles) {
            particle.draw(Pass.TANK);
            if(particle.isFinished()) {
                finishedParticles.add(particle);
            }
        }
        for(ModelParticle particle : finishedParticles) {
            particles.remove(particle);
        }

        Renderer.getInstance().getPipelines().allFlush();

        //IO Pass
        GLES20.glClear(GLES20.GL_DEPTH_BITS);
        Renderer.getInstance().useIoCamera();
        io.draw();
        Renderer.getInstance().getPipelines().allFlush();

    }

    private void shoot(Tank tank) {
        double direction = Math.toRadians(tank.getHeadDirection());
        float vx = (float) Math.cos(direction);
        float vy = (float) Math.sin(direction);
        particles.add(new ModelParticle(
                200, new float[]{tank.getX() + vx * 25, 60, tank.getY() + vy * 25},
                new float[]{tank.getRunSpeed() * vx * 2000, 0, tank.getRunSpeed() * vy *2000}, 50f,
                new float[]{0,0,0},
                500, new float[]{0,0,0,0.02f},
                3)
        );
        particles.add(new ModelParticle(
                20, new float[]{tank.getX(), 60, tank.getY()},
                new float[]{vx *5000, 0, vy *5000}, 15f,
                new float[]{0,0,0},
                300, new float[]{0.5882352941f,0.2941176471f,0,1},
                10)
        );
    }

    private void setTankBaseMatrix(Tank tank) {
        float[] matrixWorld = tankModel.base.matrixWorld;
        float x = tank.getX(), y = tank.getY();

        Matrix.setIdentityM(matrixWorld, 0);

        Matrix.translateM(matrixWorld, 0, x,0,y);
        Matrix.scaleM(matrixWorld, 0, GlobalEnvironment.TANK_RADIUS, GlobalEnvironment.TANK_RADIUS / 2, GlobalEnvironment.TANK_RADIUS);
        Matrix.rotateM(matrixWorld, 0, -tank.getHeadDirection() - 90, 0, 1, 0); // -90 to adjust model<>game data definition difference.
        Matrix.translateM(matrixWorld, 0, 0, 1, 0);
    }

    private void setTankTurretMatrix(Tank tank) {
        float[] matrixWorld = tankModel.turret.matrixWorld;
        float x = tank.getX(), y = tank.getY();

        Matrix.setIdentityM(matrixWorld, 0);

        Matrix.translateM(matrixWorld, 0, x,0,y);
        Matrix.rotateM(matrixWorld, 0, -tank.getHeadDirection() - 90, 0, 1, 0); // -90 to adjust model<>game data definition difference.
        Matrix.translateM(matrixWorld, 0, 0, GlobalEnvironment.TANK_RADIUS, -GlobalEnvironment.TANK_RADIUS / 2);
        Matrix.scaleM(matrixWorld, 0, GlobalEnvironment.TANK_RADIUS / 5, GlobalEnvironment.TANK_RADIUS / 5, GlobalEnvironment.TANK_RADIUS);
    }
}
