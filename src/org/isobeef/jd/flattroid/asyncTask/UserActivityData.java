package org.isobeef.jd.flattroid.asyncTask;

import java.util.List;

import org.shredzone.flattr4j.model.Activity;
import org.shredzone.flattr4j.model.Flattr;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.User;

public interface UserActivityData {
	public List<Activity> getIncoming();
	public List<Activity> getOutgoing();
	public List<Flattr> getFlattrs();
	public List<Thing> getThings();
	public User getUser();
}
