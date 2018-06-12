package com.blife.blife_app.utils.db.newdb.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author
 * 
 */
public class EntryUtils {
	/**
	 *
	 * 
	 * @param object
	 */
	public static void toString(Object object) {
		try {
			Map<String, Object> values = objectToMap(object);
			for (Entry<String, Object> value : values.entrySet()) {
				System.out.print("findall=="+value.getKey() + ":" + value.getValue() + " ");
			}
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * 
	 * @param class1
	 * @param filter
	 *
	 * @return
	 */
	public static List<Field> getFields(Class<?> class1, FieldFilter filter) {
		List<Field> fields = new ArrayList<Field>();
		while (class1 != null) {
			Field[] fs = class1.getDeclaredFields();
			for (Field field : fs) {
				if (filter.accept(field)) {
					field.setAccessible(true);
					fields.add(field);
				}
			}
			class1 = class1.getSuperclass();
		}
		return fields;
	}

	/**
	 * <pre>

	 * </pre>
	 * 
	 * @param object
	 * @return
	 */
	public static Map<String, Object> objectToMap(Object object) {
		return objectToMap(object, DEFAULT_FILTER);
	}

	/**

	 * @param object
	 * @return
	 */
	public static String objectToJson(Object object) {
		return objectToJson(object, DEFAULT_FILTER);
	}

	/**

	 * @return
	 */
	public static Map<String, Object> objectToMap(Object object,
												  FieldFilter filter) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Class<?> class1 = object.getClass();
		try {
			while (class1 != null) {
				Field[] fs = class1.getDeclaredFields();
				for (Field field : fs) {
					if (filter.accept(field)) {
						field.setAccessible(true);
						map.put(field.getName(), field.get(object));
					}
				}
				class1 = class1.getSuperclass();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**

	 * @return
	 */
	public static String objectToJson(Object object, FieldFilter filter) {
		Map<String, Object> map = objectToMap(object, filter);
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		boolean has = false;
		for (Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() == null)
				continue;
			builder.append("\"").append(entry.getKey()).append("\"");
			builder.append(":");
			builder.append(entry.getValue());
			builder.append(",");
			has = true;
		}
		if (has)
			builder.deleteCharAt(builder.length() - 1);
		builder.append("}");
		return builder.toString();
	}

	/**

	 * 
	 * @author Administrator
	 * 
	 */
	public static interface FieldFilter {
		/**
		 *
		 * @return
		 */
		public boolean accept(Field field);
	}

	/**
	 * </pre>
	 */
	public static final FieldFilter DEFAULT_FILTER = new FieldFilter() {

		@Override
		public boolean accept(Field field) {
			int modifiers = field.getModifiers();
			return !Modifier.isStatic(modifiers)
					&& !Modifier.isTransient(modifiers);
		}
	};

}
