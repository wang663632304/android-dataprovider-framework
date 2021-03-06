package net.yoojia.dataprovider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class QueryHelper {
	
	public static Cursor queryAll(ContentResolver resolver, Uri uri){
		return resolver.query(uri, null, null, null, null);
	}
	
	public static Cursor queryLimit(ContentResolver resolver, Uri uri, String orderKey,int limit){
		return resolver.query(uri, null, null, null, String.format("%s LIMIT %d", orderKey,limit)); 
	}
	
	public static Cursor queryLimitOffset(ContentResolver resolver, Uri uri, String orderKey,int limit,int offset){
		return resolver.query(uri, null, null, null, String.format("%s LIMIT %d OFFSET %d", orderKey, limit, offset)); 
	}
	
	static final String KEY_WITH_WHERE = "_query_helper_with_where_";
	static final String KEY_WHERE = "_query_helper_key_where_";
	static final String KEY_WHERE_ARGS_COUNT = "_query_helper_key_where_count";
	static final String KEY_KEEP_ORIGIN = "_query_helper_keep_origin_";

	/**
	 * 替换String类型的键值对Key名
	 * @param values 键值对
	 * @param oldKey 原Key
	 * @param newKey 新Key
	 */
	public static void replaceString(ContentValues values, String oldKey,String newKey){
		if(values.containsKey(oldKey)){
			String value = values.getAsString(oldKey);
			values.remove(oldKey);
			values.put(newKey,value);
		}
	}

	/**
	 * 替换Int类型的键值对Key名
	 * @param values 键值对
	 * @param oldKey 原Key
	 * @param newKey 新Key
	 */
	public static void replaceInt(ContentValues values, String oldKey,String newKey){
		replaceString(values, oldKey, newKey);
	}

	/**
	 * 替换Long类型的键值对Key名
	 * @param values 键值对
	 * @param oldKey 原Key
	 * @param newKey 新Key
	 */
	public static void replaceLong(ContentValues values, String oldKey,String newKey){
		replaceString(values, oldKey, newKey);
	}

	/**
	 * 替换Bool类型的键值对Key名
	 * @param values 键值对
	 * @param oldKey 原Key
	 * @param newKey 新Key
	 */
	public static void replaceBool(ContentValues values, String oldKey,String newKey){
		if(values.containsKey(oldKey)){
			boolean value = values.getAsBoolean(oldKey);
			values.remove(oldKey);
			values.put(newKey,value);
		}
	}
	
	/**
	 * 添加where条件
	 * @param values 键值对
	 * @param where 条件Key
	 * @param whereArgs 条件值
	 */
	public static void addWhere(ContentValues values, String where,String... whereArgs){
		values.put(KEY_WITH_WHERE, true);
		values.put(KEY_WHERE, where);
		values.put(KEY_WHERE_ARGS_COUNT, whereArgs.length);
		for(int i=0;i<whereArgs.length;i++){
			final String KEY_ARGS_ITEM = KEY_WHERE_ARGS_COUNT+i; 
			values.put(KEY_ARGS_ITEM, whereArgs[0]);
		}
	}
	
	/**
	 * 清除所有where条件参数. 在操作数据库前,ContentValues会自动清除,不必手动调用此方法.
	 * @param values 键值对
	 */
	public static void removeWhere(ContentValues values){
		values.remove(KEY_KEEP_ORIGIN);
		if(values.containsKey(KEY_WITH_WHERE)){
			values.remove(KEY_WITH_WHERE);
			values.remove(KEY_WHERE);
			final int argsCount = values.getAsInteger(KEY_WHERE_ARGS_COUNT);
			values.remove(KEY_WHERE_ARGS_COUNT);
			for(int i=0;i<argsCount;i++){
				final String KEY_ARGS_ITEM = KEY_WHERE_ARGS_COUNT+i; 
				values.remove(KEY_ARGS_ITEM);
			}
		}
	}
	
	/**
	 * 设置保持ContentValue源数据不变,拷贝此ContentValue保存到数据库中.
	 * @param values 键值对
	 */
	public static void setKeepOrigin(ContentValues values){
		values.put(KEY_KEEP_ORIGIN, true);
	}
	
	static boolean isKeepOrigin(ContentValues values){
		return values.containsKey(KEY_KEEP_ORIGIN);
	}
	
	static boolean hasWhere(ContentValues values){
		return values.containsKey(KEY_WITH_WHERE);
	}
	
	static String getWhere(ContentValues values){
		return values.getAsString(KEY_WHERE);
	}
	
	static String[] getWhereArgs(ContentValues values){
		int argsCount = values.getAsInteger(KEY_WHERE_ARGS_COUNT);
		String[] args = new String[argsCount];
		for(int i=0;i<argsCount;i++){
			final String KEY_ARGS_ITEM = KEY_WHERE_ARGS_COUNT+i; 
			args[i] = values.getAsString(KEY_ARGS_ITEM);
		}
		return args;
	}
	
}
