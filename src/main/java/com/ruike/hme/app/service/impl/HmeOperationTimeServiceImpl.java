package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeOperationTimeDto;
import com.ruike.hme.api.dto.HmeOperationTimeDto2;
import com.ruike.hme.api.dto.HmeOperationTimeDto3;
import com.ruike.hme.api.dto.HmeOperationTimeDto4;
import com.ruike.hme.app.service.HmeOperationTimeService;
import com.ruike.hme.domain.entity.HmeOperationTime;
import com.ruike.hme.domain.entity.HmeOperationTimeMaterial;
import com.ruike.hme.domain.entity.HmeOperationTimeObject;
import com.ruike.hme.domain.entity.HmeProductionVersion;
import com.ruike.hme.domain.repository.HmeOperationTimeMaterialRepository;
import com.ruike.hme.domain.repository.HmeOperationTimeRepository;
import com.ruike.hme.domain.repository.HmeProductionVersionRepository;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * 工艺时效要求表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2020-08-11 11:44:06
 */
@Service
public class HmeOperationTimeServiceImpl implements HmeOperationTimeService {

    @Autowired
    private HmeOperationTimeRepository hmeOperationTimeRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeOperationTimeMaterialRepository hmeOperationTimeMaterialRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private HmeProductionVersionRepository hmeProductionVersionRepository;

    @Override
    public Page<HmeOperationTimeVO> query(Long tenantId, HmeOperationTimeDto4 dto, PageRequest pageRequest) {
        return hmeOperationTimeRepository.query(tenantId, dto, pageRequest);
    }

    @Override
    public List<HmeOperationTime> createOrUpdate(Long tenantId, List<HmeOperationTime> hmeOperationTimeList) {
        for (HmeOperationTime hmeOperationTime : hmeOperationTimeList) {
            if (StringUtils.isEmpty(hmeOperationTime.getTimeCode())) {
                throw new MtException("HME_OP_TIME_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_OP_TIME_0001", "HME", "时效编码"));
            }
            if (StringUtils.isEmpty(hmeOperationTime.getTimeName())) {
                throw new MtException("HME_OP_TIME_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_OP_TIME_0001", "HME", "时效要求描述"));
            }
            if (hmeOperationTime.getStandardReqdTimeInProcess() == null) {
                throw new MtException("HME_OP_TIME_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_OP_TIME_0001", "HME", "时效时长"));
            }
            if (StringUtils.isEmpty(hmeOperationTime.getEnableFlag())) {
                throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_0001", "HME", "是否启用"));
            }
        }
        return hmeOperationTimeRepository.createOrUpdate(tenantId, hmeOperationTimeList);
    }

    @Override
    public Page<HmeOperationTimeVO2> queryMaterial(Long tenantId, HmeOperationTimeDto dto, PageRequest pageRequest) {
        if (StringUtils.isEmpty(dto.getOperationTimeId())) {
            throw new MtException("HME_OP_TIME_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_OP_TIME_0002", "HME"));
        }
        return hmeOperationTimeRepository.queryMaterial(tenantId, dto, pageRequest);
    }

    @Override
    public List<HmeOperationTimeMaterial> createOrUpdateMaterial(Long tenantId, List<HmeOperationTimeMaterial> list) {
        for (HmeOperationTimeMaterial dto : list) {
            if (StringUtils.isEmpty(dto.getOperationTimeId())) {
                throw new MtException("HME_OP_TIME_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_OP_TIME_0002", "HME"));
            }
            if (StringUtils.isEmpty(dto.getMaterialId())) {
                throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_0001", "HME", "物料"));
            }
            if (dto.getStandardReqdTimeInProcess() == null) {
                throw new MtException("HME_OP_TIME_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_OP_TIME_0001", "HME", "特定物料时效要求"));
            }
            if (StringUtils.isEmpty(dto.getEnableFlag())) {
                throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_0001", "HME", "是否启用"));
            }
            if (StringUtils.isEmpty(dto.getOperationTimeMaterialId())) {
                // 2020-08-28 17:49 add by chaonan.hu fou fang.pan
                // 新建时，增加头+物料+版本的唯一性校验
                List<HmeOperationTimeMaterial> hmeOperationTimeMaterials = hmeOperationTimeMaterialRepository.select(new HmeOperationTimeMaterial() {{
                    setTenantId(tenantId);
                    setOperationTimeId(dto.getOperationTimeId());
                    setMaterialId(dto.getMaterialId());
                    setProductionVersionId(dto.getProductionVersionId());
                }});
                if (CollectionUtils.isNotEmpty(hmeOperationTimeMaterials)) {
                    MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(dto.getMaterialId());
                    String productionVersion = "";
                    if(StringUtils.isNotEmpty(dto.getProductionVersionId())){
                        HmeProductionVersion hmeProductionVersion = hmeProductionVersionRepository.selectByPrimaryKey(dto.getProductionVersionId());
                        productionVersion = hmeProductionVersion.getProductionVersion();
                    }
                    throw new MtException("HME_OP_TIME_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_OP_TIME_0004", "HME", mtMaterial.getMaterialCode(), productionVersion));
                }
            }
        }
        return hmeOperationTimeRepository.createOrUpdateMaterial(tenantId, list);
    }

    @Override
    public Page<HmeOperationTimeVO3> queryObject(Long tenantId, HmeOperationTimeDto2 dto, PageRequest pageRequest) {
        if (StringUtils.isEmpty(dto.getOperationTimeId())) {
            throw new MtException("HME_OP_TIME_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_OP_TIME_0002", "HME"));
        }
        if (StringUtils.isNotEmpty(dto.getObjectCode()) && StringUtils.isEmpty(dto.getObjectType())) {
            throw new MtException("HME_OP_TIME_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_OP_TIME_0003", "HME"));
        }
        return hmeOperationTimeRepository.queryObject(tenantId, dto, pageRequest);
    }

    @Override
    public List<HmeOperationTimeObject> createOrUpdateObject(Long tenantId, List<HmeOperationTimeObject> list) {
        for (HmeOperationTimeObject dto : list) {
            if (StringUtils.isEmpty(dto.getOperationTimeId())) {
                throw new MtException("HME_OP_TIME_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_OP_TIME_0002", "HME"));
            }
            if (StringUtils.isEmpty(dto.getObjectType())) {
                throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_0001", "HME", "对象类型"));
            }
            if (StringUtils.isEmpty(dto.getObjectId())) {
                throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_0001", "HME", "特定对象编码"));
            }
            if (dto.getStandardReqdTimeInProcess() == null) {
                throw new MtException("HME_OP_TIME_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_OP_TIME_0001", "HME", "特定对象时效要求"));
            }
            if (StringUtils.isEmpty(dto.getEnableFlag())) {
                throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_0001", "HME", "是否启用"));
            }
        }
        return hmeOperationTimeRepository.createOrUpdateObject(tenantId, list);
    }

    @Override
    public Page<HmeOperationTimeVO4> queryHis(Long tenantId, HmeOperationTimeDto3 dto, PageRequest pageRequest) {
        if (StringUtils.isEmpty(dto.getOperationTimeId())) {
            throw new MtException("HME_OP_TIME_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_OP_TIME_0002", "HME"));
        }
        return hmeOperationTimeRepository.queryHis(tenantId, dto, pageRequest);
    }

    @Override
    public Page<HmeOperationTimeVO5> queryMaterialHis(Long tenantId, HmeOperationTimeDto3 dto, PageRequest pageRequest) {
        if (StringUtils.isEmpty(dto.getOperationTimeId())) {
            throw new MtException("HME_OP_TIME_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_OP_TIME_0002", "HME"));
        }
        return hmeOperationTimeRepository.queryMaterialHis(tenantId, dto, pageRequest);
    }

    @Override
    public Page<HmeOperationTimeVO6> queryObjectHis(Long tenantId, HmeOperationTimeDto3 dto, PageRequest pageRequest) {
        if (StringUtils.isEmpty(dto.getOperationTimeId())) {
            throw new MtException("HME_OP_TIME_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_OP_TIME_0002", "HME"));
        }
        return hmeOperationTimeRepository.queryObjectHis(tenantId, dto, pageRequest);
    }
}
