package com.ruike.hme.infra.repository.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeEoJobBeyondMaterial;
import com.ruike.hme.domain.repository.HmeEoJobBeyondMaterialRepository;
import com.ruike.hme.domain.vo.HmeEoJobBeyondMaterialVO;
import com.ruike.hme.infra.mapper.HmeEoJobBeyondMaterialMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 工序作业平台-计划外物料 资源库实现
 *
 * @author liyuan.lv@hand-china.com 2020-07-15 15:27:06
 */
@Component
public class HmeEoJobBeyondMaterialRepositoryImpl extends BaseRepositoryImpl<HmeEoJobBeyondMaterial> implements HmeEoJobBeyondMaterialRepository {
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeEoJobBeyondMaterialMapper hmeEoJobBeyondMaterialMapper;

    @Override
    public List<HmeEoJobBeyondMaterial> list(Long tenantId, HmeEoJobBeyondMaterialVO dto) {
        return hmeEoJobBeyondMaterialMapper.selectByWkc(tenantId, dto);
    }

    @Override
    public List<HmeEoJobBeyondMaterial> batchSave(Long tenantId, List<HmeEoJobBeyondMaterial> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            // 当前没有可保存的计划外物料
            throw new MtException("HME_EO_JOB_SN_041",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_041", "HME"));
        } else {
            dtoList.forEach(material -> {
                self().insertSelective(material);
            });
        }

        return dtoList;
    }

    @Override
    public void batchRemove(List<HmeEoJobBeyondMaterial> dtoList) {
        dtoList.forEach(material -> hmeEoJobBeyondMaterialMapper.deleteByPrimaryKey(material.getBydMaterialId()));
    }
}
