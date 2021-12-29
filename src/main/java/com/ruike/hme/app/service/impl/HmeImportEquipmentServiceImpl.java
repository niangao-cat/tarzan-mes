package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.repository.HmeEquipmentRepository;
import com.ruike.hme.domain.vo.HmeEquipmentImportVO;
import com.ruike.hme.infra.mapper.HmeEquipmentMapper;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.repository.MtModAreaRepository;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 设备台账导入模板
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/21 17:10
 */
@ImportService(templateCode = "HME.EQUIPMENT")
public class HmeImportEquipmentServiceImpl implements IBatchImportService {

    @Autowired
    private HmeEquipmentRepository hmeEquipmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModAreaRepository mtModAreaRepository;

    @Autowired
    private HmeEquipmentMapper hmeEquipmentMapper;

    @Override
    public Boolean doImport(List<String> data) {
        //获取租户Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();

        if (CollectionUtils.isNotEmpty(data)) {
            for (String vo : data) {
                HmeEquipmentImportVO importVO;
                try {
                    importVO = objectMapper.readValue(vo, HmeEquipmentImportVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                //处理数据
                HmeEquipment equipment = new HmeEquipment();
                BeanUtils.copyProperties(importVO,equipment);

                //资产编码
                if(StringUtils.isBlank(equipment.getAssetEncoding())){
                    throw new MtException("HME_EXCEL_IMPORT_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_009", "HME"));
                }


                //设备类别
                if(StringUtils.isBlank(importVO.getEquipmentCategory())){
                    throw new MtException("HME_EXCEL_IMPORT_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_011", "HME"));
                }
                List<LovValueDTO> equipemntCategoryList = lovAdapter.queryLovValue("HME.EQUIPMENT_CATEGORY", tenantId);
                String equipmentCategory = "";
                for (LovValueDTO valueDTO : equipemntCategoryList){
                    if(StringUtils.equals(valueDTO.getMeaning(),importVO.getEquipmentCategory())){
                        equipmentCategory = valueDTO.getValue();
                        break;
                    }
                }
                equipment.setEquipmentCategory(equipmentCategory);
                if(StringUtils.isBlank(equipmentCategory)){
                    throw new MtException("HME_EXCEL_IMPORT_014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_014", "HME"));
                }

                //保管部门
                if(StringUtils.isNotBlank(importVO.getBusinessId())){
                    MtModArea mtModArea = new MtModArea();
                    mtModArea.setEnableFlag("Y");
                    mtModArea.setDescription(importVO.getBusinessId());
                    List<MtModArea> mtModAreas = mtModAreaRepository.select(mtModArea);
                    if(CollectionUtils.isEmpty(mtModAreas)){
                        throw new MtException("HME_EXCEL_IMPORT_015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EXCEL_IMPORT_015", "HME"));
                    }
                    equipment.setBusinessId(mtModAreas.get(0).getAreaId());
                }

                //检验唯一
                HmeEquipment queryEquipment = new HmeEquipment();
                queryEquipment.setTenantId(tenantId);
                queryEquipment.setAssetEncoding(equipment.getAssetEncoding());
                queryEquipment = hmeEquipmentRepository.selectOne(queryEquipment);
                if (queryEquipment != null) {
                    equipment.setEquipmentId(queryEquipment.getEquipmentId());
                }
                this.equipmentBasicUpdate(tenantId, equipment);
            }
        }
        return true;
    }

    /**
     * 更新&新增设备台账信息
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/2/23 17:45
     */
    private void equipmentBasicUpdate(Long tenantId, HmeEquipment dto) {
        //如果为更新
        if (StringUtils.isNotEmpty(dto.getEquipmentId())) {
            //先查询版本号，更新
            HmeEquipment queryEquipment = new HmeEquipment();
            queryEquipment.setEquipmentId(dto.getEquipmentId());
            queryEquipment = hmeEquipmentRepository.selectOne(queryEquipment);
            dto.setTenantId(tenantId);
            dto.setObjectVersionNumber(queryEquipment.getObjectVersionNumber());
            dto.setCid(queryEquipment.getCid());
            dto.setCreatedBy(queryEquipment.getCreatedBy());
            dto.setCreationDate(queryEquipment.getCreationDate());
            dto.setLastUpdateDate(new Date());
            dto.setLastUpdatedBy(queryEquipment.getLastUpdatedBy());
            hmeEquipmentMapper.updateByPrimaryKey(dto);
        } else {
            hmeEquipmentRepository.insertSelective(dto);
        }
    }
}
