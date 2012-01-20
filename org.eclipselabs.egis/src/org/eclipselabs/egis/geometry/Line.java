package org.eclipselabs.egis.geometry;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Line implements Geometry {

	public final double[] xPoints;
	public final double[] yPoints;

	public Line(int length) {
		xPoints = new double[length];
		yPoints = new double[length];
	}
	
	public int getMemorySize() {
		return xPoints.length * 8 + yPoints.length * 8 + 4 * 3;
	}
	
	public void toBinary(DataOutput out) throws IOException {
		int length = xPoints.length;
		out.writeInt(length);
		for (int i = 0; i < length; ++i) {
			out.writeDouble(xPoints[i]);
			out.writeDouble(yPoints[i]);
		}
	}

	public void expandBounds(Bounds bounds) {
		for (int i = 0; i < xPoints.length; ++i) {
			if (xPoints[i] < bounds.x1) {
				bounds.x1 = xPoints[i]; 
			}
			if (yPoints[i] < bounds.y1) {
				bounds.y1 = yPoints[i]; 
			}
			if (xPoints[i] > bounds.x2) {
				bounds.x2 = xPoints[i]; 
			}
			if (yPoints[i] > bounds.y2) {
				bounds.y2 = yPoints[i]; 
			}
		}
	}
	
	@Override
	public String toString() {
		if (xPoints.length == 0) {
			return "LINE []";
		}
		
		StringBuilder sb = new StringBuilder("LINE [ \n");
		appendPoint(sb);
		for (int i = 0; i < xPoints.length; ++i) {
			sb.append(",\n");
			appendPoint(sb);
		}
		sb.append("]");
		return sb.toString();
	}

	private void appendPoint(StringBuilder sb) {
		sb.append("\tPOINT[").append(xPoints[0]).append(", ").append(yPoints[0]).append("]");
	}
	
	public static Line fromBinary(DataInput in) throws IOException {
		int length = in.readInt();
		Line line = new Line(length);
		for (int i = 0; i < length; ++i) {
			line.xPoints[i] = in.readDouble();
			line.yPoints[i] = in.readDouble();
		}
		return line;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitLine(this);
	}
}
