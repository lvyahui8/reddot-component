package io.github.lvyahui8.reddot.processor;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * @author lvyahui (lvyahui8@gmail.com,lvyahui8@126.com)
 * @since 2019/8/11 4:13
 */
public class YamlConfigurationTest {
   @Test
   @SuppressWarnings("unchecked")
   public void testLoad() throws Exception {
       Yaml yaml = new Yaml();
       InputStream stream = getClass().getClassLoader().getResourceAsStream("reddot.yml");
       Map<String,Object>  trees = yaml.load(stream);
       Map<String,Object>  rootMap = Collections.singletonMap("_root",trees);
       System.out.println(rootMap);
       Queue<Map.Entry<String, Object>> queue = new LinkedList<Map.Entry<String, Object>>();
       Map.Entry<String, Object> current = rootMap.entrySet().iterator().next();
       queue.add(current);
       while(! queue.isEmpty()) {
           current = queue.poll();
           //System.out.println(current.getKey());
           Object vv = current.getValue();
           if(vv instanceof Map && !((Map) vv).isEmpty()) {
               for (Map.Entry<String,Object> i : ((Map<String,Object>) vv).entrySet()) {
                   queue.offer(i);
                   System.out.println(current.getKey() + "_" + i.getKey() + "(" + current.getKey() + ")");
               }
           }
       }
   }
}
