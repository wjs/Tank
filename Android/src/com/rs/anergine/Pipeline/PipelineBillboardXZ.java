package com.rs.anergine.Pipeline;

import com.rs.anergine.PipelineComponent.PCMatrixMVP;
import com.rs.anergine.PipelineComponent.PCTexture;

public class PipelineBillboardXZ extends Pipeline {
  private static final String vertexShaderCode =
      "" +
          "uniform mat4 uMatrixMVP;" +
          "uniform vec2 uSinCos;" +
          "attribute vec4 aPosition;" +
          "attribute vec4 aPosition1;" +
          "attribute vec2 aTexcoord;" +
          "varying vec2 vTexcoord;" +
          "void main() {" +
          "  vec2 diff = vec2(aPosition.x - aPosition1.x, aPosition.z - aPosition1.y);" +
          "  vec4 realPosition = vec4(uSinCos.x * diff.x + aPosition1.x, aPosition.y, uSinCos.y * diff.y + aPosition1.z, 1);" +
          "  gl_Position = uMatrixMVP * realPosition;" +
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

  public PipelineBillboardXZ() {
    addUniform(new PCMatrixMVP());
    addUniform(new PCTexture());
    //TODO addUniform(new PCSinCos());

    super.init(vertexShaderCode, fragmentShaderCode);
  }
}
