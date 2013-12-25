package com.rs.anergine.render.pipelineComponent;

import com.rs.anergine.render.pipeline.Pipeline;

public abstract class PipelineComponent {
    public PipelineComponent(Pipeline pipeline) {
        pipeline.addComponent(this);
    }

    public abstract void init(int program);

    public abstract void set();
}
