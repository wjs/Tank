package com.rs.anergine.Pipeline;

import com.rs.anergine.PipelineComponent.PCColor;
import com.rs.anergine.PipelineComponent.PCMatrixMVP;

public class PipelineLineStrip extends Pipeline {
  private static final String vertexShaderCode =
      "" +
          "uniform mat4 uMatrixMVP;" +
          "attribute vec4 aPosition;" +
          "void main() {" +
          "  gl_Position = uMatrixMVP * aPosition;" +
          "}";

  private static final String fragmentShaderCode =
      "" +
          "precision mediump float;" +
          "uniform vec4 uColor;" +
          "void main() {" +
          "  gl_FragColor = uColor;" +
          "}";

  public PipelineLineStrip() {
    addUniform(new PCMatrixMVP());
    addUniform(new PCColor());

    super.init(vertexShaderCode, fragmentShaderCode);
  }
}
