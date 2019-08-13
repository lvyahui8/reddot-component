package io.github.lvyahui8.reddot.utils;

import io.github.lvyahui8.reddot.model.IReddot;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lvyahui (lvyahui8@gmail.com,lvyahui8@126.com)
 * @since 2019/8/11 23:45
 */
public class ReddotUtils {
    public static Set<IReddot> getReddotsIncludeAncestors(IReddot reddot) {
        Set<IReddot> reddotKeys = new HashSet<>(reddot.getLevel() + 1);
        do {
            reddotKeys.add(reddot);
        } while((reddot = reddot.getParent()) != null) ;
        return reddotKeys;
    }
}
