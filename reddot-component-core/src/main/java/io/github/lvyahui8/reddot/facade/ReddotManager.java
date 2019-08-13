package io.github.lvyahui8.reddot.facade;

import io.github.lvyahui8.reddot.model.IReddot;

import java.util.Map;

/**
 * @author lvyahui (lvyahui8@gmail.com,lvyahui8@126.com)
 * @since 2019/8/10 17:44
 */
public interface ReddotManager {
    /**
     * 激活红点, 只有叶子红点可以激活
     *
     * @param dimension 红点维度, 一般使用userId
     * @param reddot 要激活的红点
     * @return 是否激活成功
     */
    boolean enable(String dimension, IReddot reddot);

    /**
     * 激活红点, 只有叶子红点可以激活
     *
     * @param dimension 红点维度, 一般使用userId
     * @param reddot 要激活的红点
     * @param version 如果传递版本号, 意味着激活需要控制幂等.
     * @return 是否激活成功
     */
    boolean enable(String dimension, IReddot reddot,Long version);

    /**
     * 读取红点状态
     *
     * @param dimension 红点维度, 一般使用userId
     * @param reddot 要读取的红点
     * @return 红点是否处于激活中
     */
    boolean readStatus(String dimension, IReddot reddot) ;

    /**
     * 一次性读取多个红点的状态
     *
     * @param dimension 红点维度, 一般使用userId
     * @param reddots 红点列表
     * @return 红点与激活状态的映射map
     */
    Map<IReddot,Boolean> readStatusMap(String dimension, IReddot ... reddots);

    /**
     * 关闭红点.
     *
     * @param dimension 红点维度, 一般使用userId
     * @param reddots 要关闭的红点列表
     */
    void disable(String dimension,IReddot ... reddots);
}
