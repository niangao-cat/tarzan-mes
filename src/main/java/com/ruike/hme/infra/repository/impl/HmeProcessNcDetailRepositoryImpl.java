package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeProcessNcLine;
import com.ruike.hme.domain.vo.HmeProcessNcDetailVO;
import com.ruike.hme.domain.vo.HmeProcessNcLineVO;
import com.ruike.hme.infra.mapper.HmeProcessNcDetailMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeProcessNcDetail;
import com.ruike.hme.domain.repository.HmeProcessNcDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 工序不良明细表 资源库实现
 *
 * @author li.zhang13@hand-china.com 2021-01-21 09:36:44
 */
@Component
public class HmeProcessNcDetailRepositoryImpl extends BaseRepositoryImpl<HmeProcessNcDetail> implements HmeProcessNcDetailRepository {

    @Autowired
    private HmeProcessNcDetailMapper hmeProcessNcDetailMapper;
    @Override
    public Page<HmeProcessNcDetailVO> selectDetail(Long tenantId, String lineId, PageRequest pageRequest) {
        Page<HmeProcessNcDetailVO> page = PageHelper.doPage(pageRequest, () -> hmeProcessNcDetailMapper.selectDetail(tenantId,lineId));
        for(HmeProcessNcDetailVO a : page){
            if(StringUtils.isNotBlank(a.getMinValue())){
                BigDecimal min = new BigDecimal(a.getMinValue());
                a.setMinValue(min.stripTrailingZeros().toPlainString());
            }
            if(StringUtils.isNotBlank(a.getMaxValue())){
                BigDecimal max = new BigDecimal(a.getMaxValue());
                a.setMaxValue(max.stripTrailingZeros().toPlainString());
            }
        }
        return page;
    }

    @Override
    public void deleteByLine(Long tenantId, HmeProcessNcLine hmeProcessNcLine) {
        hmeProcessNcDetailMapper.deleteByLine(tenantId,hmeProcessNcLine);
    }

    @Override
    public void deleteDetailByHeader(Long tenantId, String headerId) {
        hmeProcessNcDetailMapper.deleteDetailByHeader(tenantId,headerId);
    }
}
