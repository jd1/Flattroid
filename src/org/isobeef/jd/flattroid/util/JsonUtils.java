package org.isobeef.jd.flattroid.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.model.Resource;

import android.util.Log;

public class JsonUtils {
	public static ArrayList<String> toJson ( List<? extends Resource> resources ) {
		ArrayList<String> json = new ArrayList<String>();
        for(Resource resource : resources) {
        	json.add(resource.toJSON());
        }
        return json;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T createFromJson(String json, Class<T> clazz) {
		try {
			Constructor<?> ctor = clazz.getConstructor(FlattrObject.class);
			Object object = ctor.newInstance(new Object[] { new FlattrObject(json) });
			return (T) object;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			Log.e("JsonUtils", "No constructor for " + clazz.getCanonicalName() + " found.");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassCastException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T> List<T> createFromJson(Collection<String> json, Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		for (String s : json) {
			list.add(createFromJson(s, clazz));
		}
		return list;
	}
}
