package com.rs.anergine.render.model;

import android.opengl.GLES20;

import com.rs.anergine.render.Renderer;
import com.rs.anergine.render.mesh.MeshParticle;
import com.rs.anergine.render.pass.Pass;
import com.rs.anergine.render.pipeline.Pipeline;
import com.rs.anergine.render.util.Util;

import java.nio.FloatBuffer;
import java.util.Random;

//Model is a mesh with indices & texture.
public class ModelParticle extends Model {
    protected FloatBuffer positionBuffer;
    protected FloatBuffer velocityBuffer;
    protected int particleCount;
    protected float[] center;
    protected long duration;
    protected long startTime;
    protected long stopTime;
    protected float[] acceleration;
    protected float[] color;

    protected float[] time = new float[1];
    protected MeshParticle meshParticle;

    private boolean finished = false;

    //give file id to make a mesh
	public ModelParticle(int particleCount, float[] center, float[] baseVelocity, float velocityLength, float[] acceleration, long duration, float[] color, float pointSize) {
        this.particleCount = particleCount;
        this.center = center;
        this.duration = duration;
        this.startTime = Renderer.getInstance().getFrameStartTime();
        this.stopTime = startTime + duration;
        this.acceleration = acceleration;
        this.color = color;

        Random r = new Random();

        float[] position = new float[particleCount*3];
        float[] velocity = new float[particleCount*3];
        for(int i=0; i!=particleCount; i++) {
            position[i*3  ] = center[0];
            position[i*3+1] = center[1];
            position[i*3+2] = center[2];

            float vx = (r.nextFloat()*2-1);
            float vy = (r.nextFloat()*2-1);
            float vz = (r.nextFloat()*2-1);
            float vl;
            if((vl = vx * vx + vy * vy + vz * vz) > 1) {
                float nvl = (float) (1 / Math.sqrt(vl));
                vx *= nvl;
                vy *= nvl;
                vz *= nvl;
            }
            velocity[i*3  ] = vx * velocityLength + baseVelocity[0];
            velocity[i*3+1] = vy * velocityLength + baseVelocity[1];
            velocity[i*3+2] = vz * velocityLength + baseVelocity[2];
        }

        positionBuffer = Util.getFloatBuffer(position);
        velocityBuffer = Util.getFloatBuffer(velocity);

        meshParticle = new MeshParticle(matrixWorld, positionBuffer, velocityBuffer, acceleration, time, color, pointSize, particleCount, GLES20.GL_POINTS);

        setPasses();
	}

    @Override
    public void prepare() {
        long currentTime = Renderer.getInstance().getFrameStartTime();
        time[0] = ((float)(currentTime - startTime))*0.001f;
        color[3] = (float)(stopTime - currentTime) / (float)duration;
        if(stopTime < currentTime) {
            finished = true;
            meshParticle = null;
        }
    }

    @Override
    public void setPasses() {
        setPass(Pass.TANK, Pipeline.PARTICLE, meshParticle);
    }

    public boolean isFinished() {
        return finished;
    }
}
