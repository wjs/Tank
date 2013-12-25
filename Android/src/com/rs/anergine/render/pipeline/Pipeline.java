package com.rs.anergine.render.pipeline;

import android.opengl.GLES20;

import com.rs.anergine.render.mesh.Mesh;
import com.rs.anergine.render.pipelineComponent.PipelineComponent;

import java.util.ArrayList;
import java.util.List;

public abstract class Pipeline {

    private static int pipelinePointer = 0;
    public final static int COLOR = pipelinePointer++;
    public final static int COLOR_BUFFER = pipelinePointer++;
    public final static int TEXTURE = pipelinePointer++;

    public final static int PIPELINE_COUNT = pipelinePointer;


    public static int MATRIXMVP_ULOCATION = -1;
    public static int TEXTURE_ULOCATION = -1;
    public static int COLOR_ULOCATION = -1;


    private static int alocationPointer = 0;
    public final static int POSITIONBUFFER_ALOCATION = alocationPointer++;
    public final static int COLORBUFFER_ALOCATION = alocationPointer++;
    public final static int TEXCOORDBUFFER_ALOCATION = alocationPointer++;
    public final static String POSITIONBUFFER_NAME = "aPosition";
    public final static String COLORBUFFER_NAME = "aColor";
    public final static String TEXCOORDBUFFER_NAME = "aTexcoord";


    //loaded program
    protected int program;
    //load shader. GLES20.GL_VERTEX_SHADER for vs and GLES20.GL_FRAGMENT_SHADER for ps.
    private static int loadShader(int type, String shaderCode) {
        //TODO cache here.

        //create, bind, compile.
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }


    //components
    private List<PipelineComponent> componentList = new ArrayList<PipelineComponent>();

    public void addComponent(PipelineComponent component) {
        componentList.add(component);
    }


    //load, bind, link, get uLocations.
    public void init(String vs, String ps) {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vs);
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, ps);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, pixelShader);

        GLES20.glBindAttribLocation(program, POSITIONBUFFER_ALOCATION, POSITIONBUFFER_NAME);
        GLES20.glBindAttribLocation(program, COLORBUFFER_ALOCATION, COLORBUFFER_NAME);
        GLES20.glBindAttribLocation(program, TEXCOORDBUFFER_ALOCATION, TEXCOORDBUFFER_NAME);

        GLES20.glLinkProgram(program);

        for(PipelineComponent component : componentList) {
            component.init(program);
        }
    }


    //add meshes and flush
    private ArrayList<Mesh> renderList = new ArrayList<Mesh>();

    public void add(Mesh mesh) {
        renderList.add(mesh);
    }

    public void flush() {
        if(renderList.isEmpty()) {
            return;
        }

        GLES20.glUseProgram(program);
        GLES20.glEnableVertexAttribArray(Pipeline.POSITIONBUFFER_ALOCATION);
        GLES20.glEnableVertexAttribArray(Pipeline.COLORBUFFER_ALOCATION);
        GLES20.glEnableVertexAttribArray(Pipeline.TEXCOORDBUFFER_ALOCATION);

        for(PipelineComponent component : componentList) {
            component.set();
        }

        for(Mesh mesh : renderList) {
            mesh.set();
            mesh.draw();
        }

        renderList.clear();
    }
}