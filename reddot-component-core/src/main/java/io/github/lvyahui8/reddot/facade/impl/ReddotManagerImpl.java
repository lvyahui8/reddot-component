package io.github.lvyahui8.reddot.facade.impl;

import io.github.lvyahui8.reddot.facade.ReddotManager;
import io.github.lvyahui8.reddot.model.IReddot;
import io.github.lvyahui8.reddot.model.ReddotInstance;
import io.github.lvyahui8.reddot.repository.ReddotRepository;
import io.github.lvyahui8.reddot.utils.ReddotUtils;

import java.util.*;

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
    public boolean enable(String dimension, IReddot reddot) {
        return enable(dimension, reddot, null);
    }

    @Override
    public boolean enable(String dimension, IReddot reddot, Long version) {
        if (dimension == null || reddot == null) {
            throw new IllegalArgumentException("dimension and reddot must be not null");
        }
        if (!reddot.isLeaf()) {
            return false;
        }

        if (version != null) {
            ReddotInstance reddotInstance = reddotRepository.queryReddotInstance(dimension, reddot);
            if(reddotInstance != null
                    && reddotInstance.getVersion() != null
            && reddotInstance.getVersion() >= version) {
                return false;
            }
        }

        Set<IReddot> reddots = ReddotUtils.getReddotsIncludeAncestors(reddot);
        Map<IReddot,ReddotInstance> reddotMap = reddotRepository.queryReddotInstanceMap(dimension,reddots);
        Map<IReddot,ReddotInstance> updatedMap = new HashMap<>(reddotMap.size());
        IReddot current = reddot;
        IReddot prev = null;
        do {
            ReddotInstance reddotInstance = reddotMap.get(current) != null ? reddotMap.get(current)
                    : new ReddotInstance();
            reddotInstance.setActive(true);
            if(current.isLeaf()) {
                reddotInstance.setVersion(version);
            } else if(prev != null)  {
                if(reddotInstance.getActivatedChildren().contains(prev.getUniqKey())) {
                    break;
                }
                reddotInstance.getActivatedChildren().add(prev.getUniqKey());
            }
            updatedMap.put(current,reddotInstance);
            prev = current;
        } while((current = current.getParent()) != null);
        reddotRepository.saveInstances(dimension,updatedMap);
        return true;
    }

    @Override
    public boolean readStatus(String dimension, IReddot reddot) {
        Map<IReddot, Boolean> statusMap = readStatusMap(dimension, reddot);
        return statusMap.get(reddot);
    }

    @Override
    public Map<IReddot, Boolean> readStatusMap(String dimension, IReddot ... reddots) {
        Map<IReddot, ReddotInstance> reddotInstanceMap = reddotRepository.queryReddotInstanceMap(dimension,
                new HashSet<>(Arrays.asList(reddots)));
        Map<IReddot,Boolean> resultMap = new HashMap<>(reddots.length);
        for (IReddot reddot : reddots) {
            resultMap.put(reddot,reddotInstanceMap.containsKey(reddot) && reddotInstanceMap.get(reddot).isActive());
        }
        return resultMap;
    }

    @Override
    public void disable(String dimension, IReddot... reddots) {
        Set<IReddot> allReddot = new HashSet<>(reddots.length);
        for (IReddot reddot : reddots) {
            allReddot.addAll(ReddotUtils.getReddotsIncludeAncestors(reddot));
        }

        Map<IReddot, ReddotInstance> reddotInstanceMap = reddotRepository.queryReddotInstanceMap(dimension, allReddot);
        Map<IReddot,ReddotInstance> updatedInstanceMap = new HashMap<>(reddotInstanceMap.size());
        for (IReddot reddot : reddots) {
            IReddot current =  reddot;
            IReddot prev = null;
            do {
                ReddotInstance instance = reddotInstanceMap.get(current);
                if(! instance.isActive()) {
                    break;
                }

                if(prev != null) {
                    instance.getActivatedChildren().remove(prev.getUniqKey());
                }

                if(reddot.isLeaf() || instance.getActivatedChildren().isEmpty()) {
                    instance.setActive(false);
                    updatedInstanceMap.put(reddot,instance);
                }
                prev = current;
            } while((current = current.getParent()) != null);
        }
        reddotRepository.saveInstances(dimension,updatedInstanceMap);
    }
}
