package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeNcDetailDTO;
import com.ruike.hme.app.service.HmeNcDetailService;
import com.ruike.hme.domain.entity.HmeNcDetail;
import com.ruike.hme.domain.repository.HmeNcDetailRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Xiong Yi 2020/07/07 18:45
 * @description:
 */
@Service
public class HmeNcDetailServiceImpl implements HmeNcDetailService {

    @Autowired
    private HmeNcDetailRepository repository;

    /**
     * 功能描述: <br>
     * <工序不良报表查询>
     *
     * @Param: [tenantId, dto, pageRequest]
     * @Return: io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeBadnessVO>
     * @Author: xy
     * @Date: 10:31 10:31
     */
    @Override
    public Page<HmeNcDetail> hmeNcDetailList(Long tenantId, HmeNcDetailDTO dto, PageRequest pageRequest) {
        return this.repository.hmeNcDetailList(tenantId,dto,pageRequest);
    }

    /**
     *
     *
     * @param tenantId
     * @param dto
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-15 11:43
     * @return HmeNcDetail
     */
    @Override
    public List<HmeNcDetail> ncExcelExport(Long tenantId, HmeNcDetailDTO dto) {
        return this.repository.ncExcelExport(tenantId,dto);
    }
}
