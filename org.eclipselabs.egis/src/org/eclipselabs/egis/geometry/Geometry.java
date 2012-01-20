package org.eclipselabs.egis.geometry;

import java.io.DataOutput;
import java.io.IOException;

public interface Geometry {

	int getMemorySize();
	
	void expandBounds(Bounds bounds);
	
	void toBinary(DataOutput out) throws IOException;
	
	void accept(Visitor visitor);
	
	public interface Visitor {

		void visitLine(Line line);
		
	}

	
}
