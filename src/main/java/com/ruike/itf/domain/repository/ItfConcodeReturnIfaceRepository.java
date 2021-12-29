package com.ruike.itf.domain.repository;

import com.ruike.itf.api.dto.ItfConcodeReturnIfaceDTO;
import com.ruike.itf.api.dto.ItfConcodeReturnIfaceDTO1;
import com.ruike.itf.domain.entity.ItfConcodeReturnIface;
import org.hzero.mybatis.base.BaseRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 成品出入库容器信息返回接口资源库
 *
 * @author taowen.wang@hand-china.com 2021/6/30 13:40
 */
public interface ItfConcodeReturnIfaceRepository extends BaseRepository<ItfConcodeReturnIface> {

    /**
     * 根据容器号查询数据
     *
     * @author taowen.wang@hand-china.com 2021-06-30 15:34
     * @param itfConcodeReturnIfaceDTO1
     * @return
     */
    ItfConcodeReturnIfaceDTO itfConcodeReturnIfaceByContainerCode(Long tenantId,ItfConcodeReturnIfaceDTO1 itfConcodeReturnIfaceDTO1);

}
