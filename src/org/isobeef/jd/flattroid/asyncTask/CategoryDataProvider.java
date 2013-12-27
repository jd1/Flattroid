package org.isobeef.jd.flattroid.asyncTask;

import java.util.List;

import org.shredzone.flattr4j.model.CategoryId;
import org.shredzone.flattr4j.model.Thing;

public interface CategoryDataProvider {
	public List<Thing> getThings(CategoryId id);
}
