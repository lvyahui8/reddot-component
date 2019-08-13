package io.github.lvyahui8.reddot.repository;

import io.github.lvyahui8.reddot.model.IReddot;
import io.github.lvyahui8.reddot.model.ReddotInstance;

import java.util.Map;
import java.util.Set;

/**
 * @author lvyahui (lvyahui8@gmail.com,lvyahui8@126.com)
 * @since 2019/8/11 11:49
 */
public interface ReddotRepository {
    /**
     * 按维度查询红点示例
     *
     * @param dimensionKey 红点维度, 一般是userId
     * @param reddot 要查询的红点
     * @return 红点示例
     */
    ReddotInstance queryReddotInstance(String dimensionKey, IReddot reddot);

    /**
     * 保存红点实例
     *
     * @param dimension 红点维度, 一般是userId
     * @param instanceMap 红点实例map
     */
    void saveInstances(String dimension, Map<IReddot,ReddotInstance> instanceMap);

    /**
     * 根据红点查询红点实例
     *
     * @param dimension 红点维度, 一般是userId
     * @param reddots 要查询的红点集合
     * @return 红点与红点实例映射
     */
    Map<IReddot, ReddotInstance> queryReddotInstanceMap(String dimension, Set<IReddot> reddots);
}
