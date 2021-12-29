package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeEdginkService;
import io.choerodon.mybatis.util.StringUtil;
import org.hzero.core.redis.RedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HmeEdginkServiceImpl implements HmeEdginkService {
    private final static String REDIS_EDGINK = "tarzan:edgink:";


    @Autowired
    private RedisHelper redisHelper;

    /**
     * @param tenantId
     * @param equipmentCode
     * @Description 获取激光功率计数据
     * @Date 2020-07-29 09:35:58
     * @Author wenzhnag.yu
     */
    @Override
    public String getOphir(Long tenantId, String equipmentCode) {
        //默认为2，不需要切换数据库
        //redisHelper.setCurrentDatabase(2);
        //redisHelper.clearCurrentDatabase();
        String key = REDIS_EDGINK + equipmentCode;
        String result = redisHelper.strGet(key);
        if(StringUtil.isEmpty(result))
        {
            result="0";
        }
        return result;
    }

    /**
     * @param tenantId
     * @param equipmentCode
     * @Description 获取毫瓦功率计数据
     * @Date 2020-07-29 09:35:58
     * @Author wenzhnag.yu
     */
    @Override
    public String getThorlabs(Long tenantId, String equipmentCode) {
        String key = REDIS_EDGINK + equipmentCode;
        String result = redisHelper.strGet(key);
        if(StringUtil.isEmpty(result))
        {
            result="0";
        }
        return result;
    }
}
