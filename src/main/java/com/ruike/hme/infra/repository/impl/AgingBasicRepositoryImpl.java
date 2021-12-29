package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeAgingBasicDTO;
import com.ruike.hme.domain.entity.HmeAgingBasic;
import com.ruike.hme.domain.vo.HmeAgingBasicVO;
import com.ruike.hme.infra.mapper.HmeAgingBasicMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.repository.AgingBasicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 老化基础数据 资源库实现
 *
 * @author junfeng.chen@hand-china.com 2021-03-02 11:56:36
 */
@Component
@Slf4j
public class AgingBasicRepositoryImpl extends BaseRepositoryImpl<HmeAgingBasic> implements AgingBasicRepository {

    @Autowired
    private HmeAgingBasicMapper hmeAgingBasicMapper;
    @Autowired
    private AgingBasicRepository agingBasicRepository;

    @Override
    @ProcessLovValue
    public Page<HmeAgingBasicVO> pageList(PageRequest pageRequest, HmeAgingBasicDTO dto, Long tenantId) {
        Page<HmeAgingBasicVO> list = PageHelper.doPage(pageRequest, () -> hmeAgingBasicMapper.pageList(tenantId, dto));
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(HmeAgingBasicVO vo, Long tenantId) {
        HmeAgingBasic hmeAgingBasic = new HmeAgingBasic();
        hmeAgingBasic.setMaterialId(vo.getMaterialId());
        hmeAgingBasic.setCosModel(vo.getCosModel());
        hmeAgingBasic.setChipCombination(vo.getChipCombination());
        hmeAgingBasic.setTenantId(tenantId);
        hmeAgingBasic.setCurrent(vo.getCurrent());
        hmeAgingBasic.setDuration(vo.getDuration());
        hmeAgingBasic.setEnableFlag(vo.getEnableFlag());
        //修改或新增
        if(StringUtils.isNotBlank(vo.getBasicId())){
            hmeAgingBasic.setBasicId(vo.getBasicId());
            hmeAgingBasicMapper.updateByPrimaryKeySelective(hmeAgingBasic);
        }else {
            this.insertSelective(hmeAgingBasic);
        }
    }
}
