package com.pj.Tank.render;

import android.opengl.GLES20;
import android.opengl.Matrix;

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
import com.rs.anergine.render.model.ModelQuad;
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

    private ModelTank tankModel;
    private ArrayList<ModelBlock> obstacles = new ArrayList<ModelBlock>();
    private ModelQuad terrain;
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
        cs.add(new ToggleButton(new Position(0.3f, -0.7f, Position.PositionType.LEFT), 0.4f, 0.4f, powerListener, R.drawable.button_up, R.drawable.button_down));

        ButtonListener fireListener = new ButtonListener() {
            @Override
            public void down() {
            }
            @Override
            public void up() {
                fire = true;
            }
        };
        cs.add(new Button(new Position(-0.3f, -0.7f, Position.PositionType.RIGHT), 0.4f, 0.4f, fireListener, R.drawable.button_up, R.drawable.button_down));

        io.setComponentSet(cs);


        GameMap gameMap = GlobalEnvironment.gameMap;

        terrain = new ModelQuad(R.drawable.terrain);
        float[] matrixWorld = terrain.matrixWorld;
        Matrix.setIdentityM(matrixWorld, 0);
        Matrix.scaleM(matrixWorld, 0, gameMap.getWidth() / 2, 1, gameMap.getHeight() / 2);
        Matrix.translateM(matrixWorld, 0, 1, 0, 1);
        Matrix.rotateM(matrixWorld, 0, 90, 1, 0, 0);

        tankModel = new ModelTank(R.drawable.cube_texcoord_explained);

        ArrayList<Obstacle> obstacles = gameMap.getObstacles();
        for(Obstacle obstacle : obstacles) {
            float left = obstacle.getTopLeft().getX();
            float top = obstacle.getTopLeft().getY();
            float right = obstacle.getBottomRight().getX();
            float bottom = obstacle.getBottomRight().getY();

            this.obstacles.add(new ModelBlock(left, 0, top, right, 100f, bottom, R.drawable.cube_texcoord_explained));
        }

    }

    @Override
    public void onChangeSetCamera() {
        Renderer.getInstance().useGameCamera();
        gameCamera = Renderer.getInstance().getCamera();
        gameCamera.setDistance(400);
        gameCamera.setTarget(1000, 0, 1000);
        gameCamera.setUp(0, 1, 0);
        gameCamera.setFovy(45f);
        gameCamera.setZFar(10000f);
        gameCamera.setZNear(1f);
        gameCamera.setAlpha((float) (Math.PI *5/4f));
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

    @Override
    public void render() {
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
                particles.add(new ModelParticle(
                        20, new float[]{stank.getX(), 70, stank.getY()},
                        new float[]{(float) (stank.getRunSpeed() * Math.cos(Math.toRadians(stank.getHeadDirection())))*2000, 0, (float) (stank.getRunSpeed() * Math.sin(Math.toRadians(stank.getHeadDirection()))*2000)}, 30f,
                        new float[]{0,0,0},
                        500, new float[]{1,0.8431372549f,0,1})
                );
            }

            if(stank.getBeShooted()!=0 || fire2) {
                io.vibrate(100);
            }
        }

        //Pass Terrain
        terrain.draw(Pass.TERRAIN);
        Renderer.getInstance().getPipelines().allFlush();

        //Pass Shadow
        for(ModelBlock modelBlock : obstacles) {
            modelBlock.draw(Pass.SHADOW);
        }

        for(Tank tank : GlobalEnvironment.tanks.values()) {
            setTankModelMatrix(tank);

            tankModel.draw(Pass.SHADOW);
        }

        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        Renderer.getInstance().getPipelines().allFlush();
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        //Pass Tank
        for(ModelBlock modelBlock : obstacles) {
            modelBlock.draw(Pass.TANK);
        }
        for(Point point : GlobalEnvironment.explodingPoints) {
            particles.add(new ModelParticle(
                    100, new float[]{point.getX(), 50, point.getY()},
                    new float[]{0,0,0}, 30f,
                    new float[]{0,-200,0},
                    2000, new float[]{1,0.8431372549f,0,1})
            );
        }

        for(Tank tank : GlobalEnvironment.tanks.values()) {
            setTankModelMatrix(tank);

            tankModel.draw(Pass.TANK);
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

    private void setTankModelMatrix(Tank tank) {
        float[] matrixWorld = tankModel.matrixWorld;
        float x = tank.getX(), y = tank.getY();

        Matrix.setIdentityM(matrixWorld, 0);

        Matrix.translateM(matrixWorld, 0, x,0,y);
        Matrix.scaleM(matrixWorld, 0, GlobalEnvironment.TANK_RADIUS,GlobalEnvironment.TANK_RADIUS/2,GlobalEnvironment.TANK_RADIUS);
        Matrix.translateM(matrixWorld, 0, 0,1,0);
        Matrix.rotateM(matrixWorld, 0, -tank.getHeadDirection() - 90, 0,1,0); // -90 to adjust model<>game data definition difference.
    }
}
