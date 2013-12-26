package com.rs.anergine.render.model;

import android.opengl.GLES20;

import com.rs.anergine.render.Renderer;
import com.rs.anergine.render.mesh.MeshNormalTexture;
import com.rs.anergine.render.pass.Pass;
import com.rs.anergine.render.pipeline.Pipeline;
import com.rs.anergine.render.util.Util;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Scanner;

//Model is a mesh with indices & texture.
public class ModelNormalTexture extends Model {
    protected FloatBuffer verticesBuffer;
    protected FloatBuffer normalBuffer;
    protected FloatBuffer texcoordBuffer;
    protected ShortBuffer indicesBuffer;
    protected int indicesNumber;
    protected int texture;
    protected MeshNormalTexture meshNormalTexture;

	//give file id to make a mesh
	public ModelNormalTexture(int meshFileNumber, int textureId) {
		InputStream inputStream = Renderer.getInstance().getContext().getResources().openRawResource(meshFileNumber);
		Scanner input = new Scanner(inputStream);

        int verticesNumber = Integer.parseInt(input.next());
		float[] vertices = new float[verticesNumber *3];
        float[] normal = new float[verticesNumber *3];
		float[] texcoord = new float[verticesNumber *2];
		for(int i=0; i!= verticesNumber; i++) {
            vertices[i*3  ] = Float.parseFloat(input.next());
            vertices[i*3+1] = Float.parseFloat(input.next());
            vertices[i*3+2] = Float.parseFloat(input.next());
            normal[i*3  ] = Float.parseFloat(input.next());
            normal[i*3+1] = Float.parseFloat(input.next());
            normal[i*3+2] = Float.parseFloat(input.next());
			texcoord[i*2  ] = Float.parseFloat(input.next());
			texcoord[i*2+1] = Float.parseFloat(input.next());
		}
		
		indicesNumber = Integer.parseInt(input.next());
		short[] indices = new short[indicesNumber];
		for(int i=0; i!=indicesNumber; i++) {
			indices[i] = Short.parseShort(input.next());
		}

		//will change to id based resource management system
		verticesBuffer = Util.getFloatBuffer(vertices);
        normalBuffer = Util.getFloatBuffer(normal);
		texcoordBuffer = Util.getFloatBuffer(texcoord);
		indicesBuffer = Util.getShortBuffer(indices);
		texture = Util.getTexture(textureId);

        meshNormalTexture = new MeshNormalTexture(
                matrixWorld, verticesBuffer, normalBuffer, texcoordBuffer, texture, indicesBuffer, indicesNumber, GLES20.GL_TRIANGLES
        );

        setPasses();
	}

    @Override
    public void prepare() {

    }

    @Override
    public void setPasses() {
        setPass(Pass.TERRAIN, Pipeline.TEXTURE, meshNormalTexture);
    }
}
