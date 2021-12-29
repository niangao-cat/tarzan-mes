package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.api.dto.WmsWarehousePrivilegeBatchQueryDTO;
import com.ruike.wms.api.dto.WmsWarehousePrivilegeQueryDTO;
import com.ruike.wms.domain.entity.WmsDocPrivilege;
import com.ruike.wms.domain.repository.WmsDocPrivilegeRepository;
import com.ruike.wms.domain.vo.WmsDocPrivilegeVO;
import com.ruike.wms.infra.mapper.WmsDocPrivilegeMapper;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.wms.domain.entity.WmsDocPrivilege;
import com.ruike.wms.domain.repository.WmsDocPrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

import java.util.Objects;

import static com.ruike.wms.infra.constant.WmsConstant.ConstantValue.WMS;

import java.util.Objects;

/**
 * 单据授权表 资源库实现
 *
 * @author junfeng.chen@hand-china.com 2021-01-19 20:21:30
 */
@Component
@Slf4j
public class WmsDocPrivilegeRepositoryImpl extends BaseRepositoryImpl<WmsDocPrivilege> implements WmsDocPrivilegeRepository {

    @Autowired
    private WmsDocPrivilegeMapper wmsDocPrivilegeMapper;
    @Autowired
    private MtModLocatorRepository locatorRepository;

    @Override
    @ProcessLovValue
    public Page<WmsDocPrivilegeVO> userPrivilegeForUi(Long tenantId, PageRequest pageRequest, WmsDocPrivilegeVO dto) {
        return PageHelper.doPage(pageRequest, () -> wmsDocPrivilegeMapper.userPrivilegeForUi(tenantId, dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsDocPrivilegeVO save(Long tenantId, WmsDocPrivilegeVO wmsDocPrivilegeVO) {
        log.info("=======================>"+wmsDocPrivilegeVO);
        WmsDocPrivilege wmsDocPrivilege = new WmsDocPrivilege();
        wmsDocPrivilege.setTenantId(tenantId);
        wmsDocPrivilege.setUserOrganizationId(wmsDocPrivilegeVO.getUserOrganizationId());
        wmsDocPrivilege.setLocationType(wmsDocPrivilegeVO.getLocationType());
        wmsDocPrivilege.setDocType(wmsDocPrivilegeVO.getDocType());
        wmsDocPrivilege.setEnableFlag(wmsDocPrivilegeVO.getEnableFlag());
        WmsDocPrivilege wmsDocPrivilegeExist = wmsDocPrivilegeMapper.selectOne(wmsDocPrivilege);
        log.info("=======================>"+wmsDocPrivilege);
        wmsDocPrivilege.setOperationType(wmsDocPrivilegeVO.getOperationType());
        if (StringUtils.isNotBlank(wmsDocPrivilegeVO.getPrivilegeId())) {
            wmsDocPrivilege.setPrivilegeId(wmsDocPrivilegeVO.getPrivilegeId());
            wmsDocPrivilegeMapper.updateByPrimaryKeySelective(wmsDocPrivilege);
        } else {
            this.insertSelective(wmsDocPrivilege);
            wmsDocPrivilegeVO.setPrivilegeId(wmsDocPrivilege.getPrivilegeId());
        }
        return wmsDocPrivilegeVO;
    }

    @Override
    public void isWarehousePrivileged(Long tenantId, WmsWarehousePrivilegeQueryDTO dto) {
        if (wmsDocPrivilegeMapper.selectWarehousePrivilegeCount(tenantId, dto) == 0) {
            MtModLocator locator = locatorRepository.selectByPrimaryKey(dto.getLocatorId());
            String locatorCode = Objects.isNull(locator) ? "" : locator.getLocatorCode();
            WmsCommonUtils.processValidateMessage(tenantId, "WMS_COST_CENTER_0067", WMS, locatorCode);
        }
    }

    @Override
    public Boolean existsWarehousePrivileged(Long tenantId, WmsWarehousePrivilegeBatchQueryDTO dto) {
        return dto.getLocatorIdList().stream().anyMatch(locatorId -> wmsDocPrivilegeMapper.selectWarehousePrivilegeCount(tenantId, WmsWarehousePrivilegeQueryDTO.builder().userId(dto.getUserId()).docType(dto.getDocType()).operationType(dto.getOperationType()).locationType(dto.getLocationType()).locatorId(locatorId).build()) > 0);
    }

    @Override
    public Boolean allWarehousePrivileged(Long tenantId, WmsWarehousePrivilegeBatchQueryDTO dto) {
        return dto.getLocatorIdList().stream().allMatch(locatorId -> wmsDocPrivilegeMapper.selectWarehousePrivilegeCount(tenantId, WmsWarehousePrivilegeQueryDTO.builder().userId(dto.getUserId()).docType(dto.getDocType()).operationType(dto.getOperationType()).locationType(dto.getLocationType()).locatorId(locatorId).build()) > 0);
    }
}
