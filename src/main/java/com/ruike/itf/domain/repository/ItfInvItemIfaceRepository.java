package com.ruike.itf.domain.repository;

import com.ruike.itf.api.dto.RfcParamDTO;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.itf.domain.entity.ItfInvItemIface;

import java.util.Map;

/**
 * 物料接口表资源库
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
public interface ItfInvItemIfaceRepository extends BaseRepository<ItfInvItemIface> {


    /**
     * 获取接口参数
     *
     * @param tenantId
     * @param map
     * @author jiangling.zheng@hand-china.com 2020/7/23 17:00
     * @return
     */
    RfcParamDTO getSapParams(Long tenantId, Map<String, String> map);
}
