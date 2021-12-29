package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeNcDetailDTO;
import com.ruike.hme.domain.entity.HmeNcDetail;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.core.base.AopProxy;

import java.util.List;

/**
 * @author Xiong Yi 2020/07/11 0:25
 * @description:
 */
public interface HmeNcDetailRepository extends AopProxy<HmeNcDetail> {

    Page<HmeNcDetail> hmeNcDetailList(Long tenantId, HmeNcDetailDTO dto, PageRequest pageRequest);


    List<HmeNcDetail> ncExcelExport(Long tenantId, HmeNcDetailDTO dto);
}