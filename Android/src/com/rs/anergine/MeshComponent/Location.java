package com.rs.anergine.MeshComponent;

public class Location {

  //component flag
//  public static final int TOPOLOGY        = 1 << 1;
//  public static final int POSITIONBUFFER  = 1 << 2;
//  public static final int COLOR           = 1 << 3;
//  public static final int COLORBUFFER     = 1 << 4;
//  public static final int TEXTURE         = 1 << 5;
//  public static final int INDEXBUFFER     = 1 << 6;
//  public static final int SIZE            = 1 << 7;
//  public static final int VERTEXCOUNT     = 1 << 8;

  //attribute location
  public static final int[] POSITIONBUFFER_ALOCATIONS = new int[]{0,1};
  public static final int COLORBUFFER_ALOCATION = 2;
  public static final int[] TEXCOORDBUFFER_ALOCATIONS = new int[]{3,4};

  //uniform location, set when a pipeline flushes.
  public static int COLOR_ULOCATION = -1;
  public static int[] TEXTURE_ULOCATIONS = new int[]{-1,-1};
  public static int MATRIXMVP_ULOCATION = -1;
  public static int SINCOS_ULOCATION = -1;

}
