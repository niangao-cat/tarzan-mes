package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeServiceReceiveDTO;
import com.ruike.hme.api.dto.HmeServiceReceiveDTO2;
import com.ruike.hme.api.dto.HmeServiceReceiveDTO4;
import com.ruike.hme.app.service.HmeServiceReceiveService;
import com.ruike.hme.domain.entity.HmeLogisticsInfo;
import com.ruike.hme.domain.entity.HmeServiceReceive;
import com.ruike.hme.domain.repository.HmeLogisticsInfoRepository;
import com.ruike.hme.domain.repository.HmeSapSnMaterialRelRepository;
import com.ruike.hme.domain.repository.HmeServiceReceiveRepository;
import com.ruike.hme.domain.vo.HmeServiceReceiveVO;
import com.ruike.hme.domain.vo.HmeServiceReceiveVO2;
import com.ruike.hme.domain.vo.HmeServiceReceiveVO3;
import com.ruike.hme.infra.mapper.HmeServiceReceiveMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 营销服务部接收拆箱登记表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2020-09-01 14:14:21
 */
@Service
public class HmeServiceReceiveServiceImpl implements HmeServiceReceiveService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeServiceReceiveRepository hmeServiceReceiveRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private HmeServiceReceiveMapper hmeServiceReceiveMapper;
    @Autowired
    private HmeLogisticsInfoRepository hmeLogisticsInfoRepository;
    @Autowired
    private HmeSapSnMaterialRelRepository hmeSapSnMaterialRelRepository;

    @Override
    public HmeServiceReceiveVO scanlogisticsNumber(Long tenantId, String logisticsNumber) {
        if(StringUtils.isEmpty(logisticsNumber)){
            throw new MtException("HME_LOGISTICS_INFO_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOGISTICS_INFO_0002", "HME", "物流单号"));
        }
        return hmeServiceReceiveRepository.scanlogisticsNumber(tenantId, logisticsNumber);
    }

    @Override
    public List<LovValueDTO> receiveDepartment(Long tenantId) {
        return lovAdapter.queryLovValue("HME.RECEIVE_DEPARTMENT", tenantId);
    }

    @Override
    public HmeServiceReceiveVO2 scanSn(Long tenantId, HmeServiceReceiveDTO4 dto) {
        if(StringUtils.isEmpty(dto.getLogisticsInfoId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "物流单ID"));
        }
        if(StringUtils.isEmpty(dto.getSn())){
            throw new MtException("HME_LOGISTICS_INFO_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOGISTICS_INFO_0002", "HME", "SN"));
        }
        //2020-09-23 11:35 add by chaonan.hu for fang.pan 增加校验
        //校验在当前物流单号下，是否已经存在录入的sn信息
        HmeServiceReceive hmeServiceReceive = hmeServiceReceiveRepository.selectOne(new HmeServiceReceive() {{
            setTenantId(tenantId);
            setLogisticsInfoId(dto.getLogisticsInfoId());
            setSnNum(dto.getSn());
        }});
        if(hmeServiceReceive != null){
            HmeLogisticsInfo hmeLogisticsInfo = hmeLogisticsInfoRepository.selectByPrimaryKey(dto.getLogisticsInfoId());
            throw new MtException("HME_LOGISTICS_INFO_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOGISTICS_INFO_0004", "HME", hmeLogisticsInfo.getLogisticsNumber()));
        }
        //2021-03-30 09:28 add by chaonan.hu for fang.pan 增加校验
        //校验当前sn是否存在未返修完成的记录
        Long total = hmeServiceReceiveMapper.getTotalBySn(tenantId, dto.getSn());
        if(total > 0){
            throw new MtException("HME_LOGISTICS_INFO_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOGISTICS_INFO_0005", "HME"));
        }
        HmeServiceReceiveVO2 result = new HmeServiceReceiveVO2();
        //根据扫描的SN查询物料批
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getSn());
        }});
        if(mtMaterialLot == null){
            //2020-09-23 11:35 edit by chaonan.hu for fang.pan
            //若不存在，则先去SAP-sn与物料关系表查找物流，若查找不到则前台提示：序列号无生产记录，请确认是否需要进行注册
            if(StringUtils.isEmpty(dto.getSiteId())){
                throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0006", "HME", "用户默认站点ID"));
            }
            String materialId = hmeServiceReceiveMapper.getMaterialIdBySnNum(tenantId, dto.getSn(), dto.getSiteId());
            if(StringUtils.isEmpty(materialId)){
                result.setFlag(1);
            }else {
                result.setFlag(2);
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(materialId);
                result.setMaterialId(mtMaterial.getMaterialId());
                result.setMaterialCode(mtMaterial.getMaterialCode());
                result.setMaterialName(mtMaterial.getMaterialName());
            }
        }else{
            //若存在，则查询物料信息
            result.setFlag(2);
            result.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtMaterialLot.getMaterialId());
            result.setMaterialId(mtMaterial.getMaterialId());
            result.setMaterialCode(mtMaterial.getMaterialCode());
            result.setMaterialName(mtMaterial.getMaterialName());
        }
        return result;
    }

    @Override
    public Page<HmeServiceReceiveVO3> materialLovQuery(Long tenantId, HmeServiceReceiveDTO dto, PageRequest pageRequest) {
        List<String> itemTypeList = new ArrayList<>();
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.REPAIR_CAN_PICK_MATERIAL", tenantId);
        if(CollectionUtils.isNotEmpty(lovValueDTOS)){
            itemTypeList = lovValueDTOS.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        }
        List<String> finalItemTypeList = itemTypeList;
        return PageHelper.doPageAndSort(pageRequest, ()->hmeServiceReceiveMapper.materialLovQuery(tenantId, dto, finalItemTypeList));
    }

    @Override
    public HmeServiceReceiveDTO2 confirm(Long tenantId, HmeServiceReceiveDTO2 dto) {
        if(StringUtils.isEmpty(dto.getLogisticsInfoId())){
            throw new MtException("HME_LOGISTICS_INFO_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOGISTICS_INFO_0002", "HME", "物流单号"));
        }
        if(CollectionUtils.isEmpty(dto.getMaterialDataList())){
            throw new MtException("HME_LOGISTICS_INFO_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOGISTICS_INFO_0002", "HME", "SN"));
        }
        return hmeServiceReceiveRepository.confirm(tenantId, dto);
    }
}
