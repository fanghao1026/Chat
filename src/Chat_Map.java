import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author FH
 * @create 2020-08-16
 */
public class Chat_Map<K,V> {

    public Map<K,V> map = Collections.synchronizedMap(new HashMap<K,V>());

    public synchronized Set<V> valueSet()
    {
        Set<V> valueSet = new HashSet<>();
        map.forEach(new BiConsumer<K, V>() {
            @Override
            public void accept(K k, V v) {
                V value = map.get(k);
                valueSet.add(value);
            }
        });
        return valueSet;
    }

    public synchronized K getKey(V value){

        for(K key  : map.keySet()){
            if(map.get(key) == value || map.get(key).equals(value)){
                return key;
            }
        }
        return null;
    }

    public synchronized boolean put(K key,V value){
        for(K mapKey  : map.keySet()){
            if(mapKey == key || mapKey.equals(key)){
                return false;
            }
        }
        for(K mapKey : map.keySet()){
            if(map.get(mapKey) == value || map.get(mapKey).equals(value)){
                return false;
            }
        }
        map.put(key,value);
        return true;
    }

    public synchronized void removeByValue(V value){
        K key = getKey(value);
        map.remove(key);
    }

}
