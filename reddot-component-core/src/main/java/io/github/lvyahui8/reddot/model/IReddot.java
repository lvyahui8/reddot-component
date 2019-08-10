package io.github.lvyahui8.reddot.model;

import java.util.Set;

/**
 * @author lvyahui (lvyahui8@gmail.com,lvyahui8@126.com)
 * @since 2019/8/10 17:46
 */
public interface IReddot {
    /**
     * 是否是叶子红点
     *
     * @return  xx
     */
    boolean isLeaf();
    /**
     * 获取上一级红点
     *
     * @return 上一级红点
     */
    IReddot getParent();

    /**
     * 获取孩子红点
     *
     * @return 孩子红点集合
     */
    Set<IReddot> getChildren();

    /**
     * 获取红点层级
     *
     * @return 层级
     */
    int getLevel();
}
