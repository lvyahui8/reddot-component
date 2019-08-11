package io.github.lvyahui8.reddot.repository;

import io.github.lvyahui8.reddot.model.IReddot;
import io.github.lvyahui8.reddot.model.ReddotInstance;

import java.util.Map;

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
    ReddotInstance queryInstance(String dimensionKey, IReddot reddot);

    /**
     * 保存红点实例
     *
     * @param dimensionKey 红点维度, 一般是userId
     * @param instanceMap 红点实例map
     */
    void saveInstances(String dimensionKey, Map<IReddot,ReddotInstance> instanceMap);
}
