package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.vo.HmeSnLabCodeVO;
import com.ruike.hme.infra.mapper.HmeSnLabCodeMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeSnLabCode;
import com.ruike.hme.domain.repository.HmeSnLabCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * SN工艺实验代码表 资源库实现
 *
 * @author penglin.sui@hand-china.com 2021-03-19 16:01:36
 */
@Component
public class HmeSnLabCodeRepositoryImpl extends BaseRepositoryImpl<HmeSnLabCode> implements HmeSnLabCodeRepository {

    @Autowired
    private HmeSnLabCodeMapper hmeSnLabCodeMapper;

    @Override
    @ProcessLovValue
    public Page<HmeSnLabCodeVO> listByMaterialLotId(PageRequest pageRequest, HmeSnLabCode hmeSnLabCode, Long tenantId) {
        return PageHelper.doPage(pageRequest, () -> hmeSnLabCodeMapper.listByMaterialLotId(tenantId, hmeSnLabCode));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(HmeSnLabCode hmeSnLabCode, Long tenantId) {
        HmeSnLabCode snLabCode = new HmeSnLabCode();
        snLabCode.setMaterialLotId(hmeSnLabCode.getMaterialLotId());
        snLabCode.setOperationId(hmeSnLabCode.getOperationId());
        snLabCode.setLabCode(hmeSnLabCode.getLabCode());
        snLabCode.setRemark(hmeSnLabCode.getRemark());
        snLabCode.setEnabledFlag(hmeSnLabCode.getEnabledFlag());
        snLabCode.setTenantId(tenantId);
        if(StringUtils.isNotBlank(hmeSnLabCode.getKid())){
            snLabCode.setKid(hmeSnLabCode.getKid());
            hmeSnLabCodeMapper.updateByPrimaryKeySelective(snLabCode);
        }else {
            this.insertSelective(snLabCode);
        }
    }
}
