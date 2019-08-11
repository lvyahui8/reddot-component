package io.github.lvyahui8.reddot.facade.impl;

import io.github.lvyahui8.reddot.facade.ReddotManager;
import io.github.lvyahui8.reddot.model.IReddot;
import io.github.lvyahui8.reddot.model.ReddotInstance;
import io.github.lvyahui8.reddot.repository.ReddotRepository;

import java.util.Map;

/**
 * @author lvyahui (lvyahui8@gmail.com,lvyahui8@126.com)
 * @since 2019/8/11 11:45
 */
public class ReddotManagerImpl implements ReddotManager {

    private ReddotRepository reddotRepository;

    public ReddotManagerImpl(ReddotRepository reddotRepository) {
        this.reddotRepository = reddotRepository;
    }

    @Override
    public boolean enable(Object dimension, IReddot reddot) {
        return enable(dimension, reddot, null);
    }

    @Override
    public boolean enable(Object dimension, IReddot reddot, Long version) {
        if (dimension == null || reddot == null) {
            throw new IllegalArgumentException("dimension and reddot must be not null");
        }
        if (!reddot.isLeaf()) {
            return false;
        }

        if (version != null) {
            ReddotInstance reddotInstance = reddotRepository.queryInstance(dimension.toString(), reddot);
            if(reddotInstance != null
                    && reddotInstance.getVersion() != null
            && reddotInstance.getVersion() >= version) {
                /* 幂等掉拒绝更新 */
                return false;
            }
        }

        ReddotInstance reddotInstance = new ReddotInstance();
        reddotInstance.setActive(true);
        reddotInstance.setVersion(version);

        return false;
    }

    @Override
    public boolean readStatus(Object dimension, IReddot reddot) {
        Map<IReddot, Boolean> statusMap = readStatusMap(dimension, reddot);
        return statusMap.get(reddot);
    }

    @Override
    public Map<IReddot, Boolean> readStatusMap(Object dimension, IReddot... reddots) {
        return null;
    }

    @Override
    public void disable(Object dimension, IReddot... reddots) {

    }
}
