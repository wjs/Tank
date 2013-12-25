package com.rs.anergine.Pipeline;

import com.rs.anergine.PipelineComponent.PCMatrixMVP;
import com.rs.anergine.PipelineComponent.PCTexture;

public class PipelineTexture extends Pipeline {
  private static final String vertexShaderCode =
      "" +
          "uniform mat4 uMatrixMVP;" +
          "attribute vec4 aPosition;" +
          "attribute vec2 aTexcoord;" +
          "varying vec2 vTexcoord;" +
          "void main() {" +
          "  gl_Position = uMatrixMVP * aPosition;" +
          "  vTexcoord = aTexcoord;" +
          "}";

  private static final String fragmentShaderCode =
      "" +
          "precision mediump float;" +
          "uniform sampler2D uTexture;" +
          "varying vec2 vTexcoord;" +
          "void main() {" +
          "  gl_FragColor = texture2D(uTexture, vTexcoord);" +
          "}";

  public PipelineTexture() {
    addUniform(new PCMatrixMVP());
    addUniform(new PCTexture());

    super.init(vertexShaderCode, fragmentShaderCode);
  }
}
