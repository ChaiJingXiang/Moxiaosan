package consumer;

import java.util.LinkedHashMap;

public class HashMapUtils {
	
	public  LinkedHashMap<String ,Object> map ;
	{
		map = new LinkedHashMap<String, Object>();
	}
	
	public HashMapUtils putValue(String key ,int value ){
		map.put(key, value);
		return this ;
	}
	public HashMapUtils putValue(String key ,String value ){
		map.put(key, value);
		return this ;
	}
	public HashMapUtils putValue(String key ,boolean value  ){
		map.put(key, value);
		return this ;
	}
	public HashMapUtils putValue(String key ,double value  ){
		map.put(key, value);
		return this ;
	}
	public HashMapUtils putValue(String key ,char value  ){
		map.put(key, value);
		return this ;
	}
	public HashMapUtils putValue(String key ,long value  ){
		map.put(key, value);
		return this ;
	}
	public  LinkedHashMap<String, Object> createMap(){
		return map ;
	}
	
	public boolean isContainsKey(String key){
		if(map== null)
		return false ;
		return map.containsKey(key);
	}
	public void removeObj(String key){
		if(map !=null){
			map.remove(key);
		}
	}
}
