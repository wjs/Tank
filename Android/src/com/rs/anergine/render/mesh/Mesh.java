package com.rs.anergine.render.mesh;

import android.opengl.GLES20;

import com.rs.anergine.render.meshComponent.MeshComponent;

import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

public abstract class Mesh {
    public int primitiveType;
    public int primitiveCount;
    public ShortBuffer indexBuffer;

    private List<MeshComponent> components = new ArrayList<MeshComponent>();

    protected void addComponent(MeshComponent meshComponent) {
        components.add(meshComponent);
    }

    public final void set() {
        for(MeshComponent component : components) {
            component.set();
        }
    }

    public void draw() {
        GLES20.glDrawElements(primitiveType, primitiveCount, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
    }
}
