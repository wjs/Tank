package com.rs.anergine.io;

//A "Position" indicates a point on screen.
//Can have 4 kinds of coordinate system and can convert from one to another.
//Screen width & height must be updated once the surface is changed.
public class Position {
	
	public enum PositionType {
		LEFT,
		MIDDLE,
		RIGHT,
		SCREEN
	}
	
	public static float width, height;	//must be updated once the surface is changed.
	public static float width_d2, height_d2;
	public static float aspect;
	private static float width_d2i, height_d2i;
	
	//update width & height. must be called once the surface is changed.
	public static void setSize(int width, int height) {
		Position.width = width;
		Position.height = height;
		width_d2 = ((float)width)/2;
		height_d2 = -((float)height)/2;
		width_d2i = 1/width_d2;
		height_d2i = 1/height_d2;
		aspect = (float)width / (float)height;
	}
	
	//all private data is here.
	//position type indicates which coordinate system is used.
	//(x,y) is the position.
	private PositionType pt = PositionType.SCREEN;
	public float x=0, y=0;
	
	public Position(float x, float y, PositionType pt) {
        set(x, y, pt);
	}

    private Position set(float x, float y, PositionType pt) {
        this.x = x;
        this.y = y;
        this.pt = pt;
        return this;
    }

    //another = clone.
    //all to* method changes "this" directly.

    public Position another() {
		return new Position(x,y,pt);
	}

    public Position toLeft() {
        switch(pt) {
        case LEFT:
            return this;
        case MIDDLE:
            return set(x + aspect, y, PositionType.LEFT);
        default:
            return this.toMiddle().toLeft();
        }
    }

    public Position toMiddle() {
        switch(pt) {
        case MIDDLE:
            return this;
        case LEFT:
            return set(x - aspect, y, PositionType.MIDDLE);
        case RIGHT:
            return set(x + aspect, y, PositionType.MIDDLE);
        default:
            return set((x * width_d2i - 1) * aspect, y * height_d2i + 1, PositionType.MIDDLE);
        }
    }

    public Position toRight() {
        switch(pt) {
        case RIGHT:
            return this;
        case MIDDLE:
            return set(x - aspect, y, PositionType.RIGHT);
        default:
            return this.toMiddle().toRight();
        }
    }

    public Position toScreen() {
        switch(pt) {
        case SCREEN:
            return this;
        case MIDDLE:
            return set(x / aspect * width_d2 + width_d2, y * height_d2 + height_d2, PositionType.SCREEN);
        default:
            return this.toMiddle().toScreen();
        }
    }
}
