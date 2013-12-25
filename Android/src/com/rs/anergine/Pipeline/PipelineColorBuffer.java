package com.rs.anergine.Pipeline;

import com.rs.anergine.PipelineComponent.PCMatrixMVP;

public class PipelineColorBuffer extends Pipeline {
  private static final String vertexShaderCode =
      "" +
          "uniform mat4 uMatrixMVP;" +
          "attribute vec4 aPosition;" +
          "attribute vec4 aColor;" +
          "varying vec4 vColor;" +
          "void main() {" +
          "  gl_Position = uMatrixMVP * aPosition;" +
          "  vColor = aColor;" +
          "}";

  private static final String fragmentShaderCode =
      "" +
          "precision mediump float;" +
          "varying vec4 vColor;" +
          "void main() {" +
          "  gl_FragColor = vColor;" +
          "}";

  public PipelineColorBuffer() {
    addUniform(new PCMatrixMVP());

    super.init(vertexShaderCode, fragmentShaderCode);
  }
}
