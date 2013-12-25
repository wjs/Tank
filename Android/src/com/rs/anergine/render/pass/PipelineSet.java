package com.rs.anergine.render.pass;

import com.rs.anergine.render.mesh.Mesh;
import com.rs.anergine.render.pipeline.Pipeline;
import com.rs.anergine.render.pipeline.PipelineColor;
import com.rs.anergine.render.pipeline.PipelineColorBuffer;
import com.rs.anergine.render.pipeline.PipelineTexture;

public class PipelineSet {

    private Pipeline[] pipelines = new Pipeline[Pipeline.PIPELINE_COUNT];
    public PipelineSet() {
        pipelines[Pipeline.COLOR] = new PipelineColor();
        pipelines[Pipeline.COLOR_BUFFER] = new PipelineColorBuffer();
        pipelines[Pipeline.TEXTURE] = new PipelineTexture();
    }

    public void allFlush() {
        for(Pipeline pipeline : pipelines) {
            pipeline.flush();
        }
    }

    public void addMesh(int index, Mesh mesh) {
        pipelines[index].add(mesh);
    }
}