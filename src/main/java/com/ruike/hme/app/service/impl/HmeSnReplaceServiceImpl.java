package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeSnReplaceDTO;
import com.ruike.hme.api.dto.HmeSnReplaceDTO2;
import com.ruike.hme.app.service.HmeSnReplaceService;
import com.ruike.hme.domain.repository.HmeSnReplaceRepository;
import com.ruike.hme.domain.vo.HmeSnReplaceVO;
import com.ruike.hme.infra.mapper.HmeNewOldSnRelMapper;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import com.ruike.wms.domain.vo.WmsMaterialLotExtendAttrVO;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO2;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.vo.MtEoVO;
import tarzan.order.domain.vo.MtEoVO38;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * HmeSnReplaceServiceImpl
 *
 * @author: chaonan.hu@hand-china.com 2020-11-03 22:29:34
 **/
@Service
public class HmeSnReplaceServiceImpl implements HmeSnReplaceService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageService;
    @Autowired
    private WmsSiteRepository wmsSiteRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private HmeSnReplaceRepository hmeSnReplaceRepository;
    @Autowired
    private HmeNewOldSnRelMapper hmeNewOldSnRelMapper;

    @Override
    public HmeSnReplaceDTO2 cmsSnReplace(Long tenantId, HmeSnReplaceDTO2 dto2) {
        //userId塞值
        List<HmeSnReplaceDTO> dtoList = dto2.getDtoList();
        if(StringUtils.isEmpty(dto2.getUserName())) {
            throw new MtException("HME_NC_0006", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "用户"));
        }
        Long userId = hmeNewOldSnRelMapper.getUserId(dto2.getUserName());
        if(Objects.isNull(userId)){
            throw new MtException("HME_INSPECTOR_ITEM_GROUP_REL_0002", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_INSPECTOR_ITEM_GROUP_REL_0002", "HME", dto2.getUserName()));
        }
        DetailsHelper.setCustomUserDetails(userId, "zh_CN");
        snReplace(tenantId, dtoList);
        return dto2;
    }

    @Override
    public List<HmeSnReplaceDTO> snReplace(Long tenantId, List<HmeSnReplaceDTO> dtoList) {
        //必输校验
        for (HmeSnReplaceDTO dto:dtoList) {
            if(StringUtils.isEmpty(dto.getOldMaterialLotCode())){
                throw new MtException("HME_NC_0006", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_NC_0006", "HME", "原SN编码"));
            }
            if(StringUtils.isEmpty(dto.getNewMaterialLotCode())){
                throw new MtException("HME_NC_0006", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_NC_0006", "HME", "新SN编码"));
            }
        }
        //业务校验
        HmeSnReplaceVO hmeSnReplaceVO = checkSnReplaceData(tenantId, dtoList);
        //数据更新
        hmeSnReplaceRepository.updateSnReplaceData(tenantId, hmeSnReplaceVO);
        return dtoList;
    }

    @Override
    public HmeSnReplaceVO checkSnReplaceData(Long tenantId, List<HmeSnReplaceDTO> dtoList) {
        HmeSnReplaceVO hmeSnReplaceVO = new HmeSnReplaceVO();
        //查询当前用户默认站点
        String siteId = wmsSiteRepository.userDefaultSite(tenantId);
        if(StringUtils.isEmpty(siteId)){
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_013", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_013", "HME"));
        }
        List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
        List<MtEoVO> eoMessageList = new ArrayList<>();
        //业务校验
        for (HmeSnReplaceDTO dto:dtoList) {
            //原SN校验 {条码不存在, 请检查}
            MtMaterialLot oldMtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                setTenantId(tenantId);
                setSiteId(siteId);
                setMaterialLotCode(dto.getOldMaterialLotCode());
            }});
            if(Objects.isNull(oldMtMaterialLot)){
                throw new MtException("WMS_COST_CENTER_0006", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0006", "WMS", dto.getOldMaterialLotCode()));
            }
            //条码已失效,请检查!
            if(!"Y".equals(oldMtMaterialLot.getEnableFlag())){
                throw new MtException("HME_WO_INPUT_0004", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_WO_INPUT_0004", "HME", dto.getOldMaterialLotCode()));
            }
            //条码不为在制品,无法升级
            MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
            mtMaterialLotAttrVO2.setMaterialLotId(oldMtMaterialLot.getMaterialLotId());
            mtMaterialLotAttrVO2.setAttrName("MF_FLAG");
            List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if(CollectionUtils.isEmpty(mtExtendAttrVOS) || !"Y".equals(mtExtendAttrVOS.get(0).getAttrValue())){
                throw new MtException("HME_SN_REPLACE_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_SN_REPLACE_0001", "HME", dto.getOldMaterialLotCode()));
            }
            //新SN校验  新SN已存在,请通过工序投料进行升级
            MtMaterialLot newMtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                setTenantId(tenantId);
                setSiteId(siteId);
                setMaterialLotCode(dto.getNewMaterialLotCode());
            }});
            if(Objects.nonNull(newMtMaterialLot)){
                throw new MtException("HME_SN_REPLACE_0002", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_SN_REPLACE_0002", "HME", dto.getNewMaterialLotCode()));
            }
            //新SN已作为eo标识无法更新
            int count = mtEoRepository.selectCount(new MtEo() {{
                setTenantId(tenantId);
                setIdentification(dto.getNewMaterialLotCode());
            }});
            if(count > 0){
                throw new MtException("HME_SN_REPLACE_0003", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_SN_REPLACE_0003", "HME", dto.getNewMaterialLotCode()));
            }
            //封装数据
            MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
            mtMaterialLotVO20.setMaterialLotId(oldMtMaterialLot.getMaterialLotId());
            mtMaterialLotVO20.setMaterialLotCode(dto.getNewMaterialLotCode());
            mtMaterialLotVO20.setIdentification(dto.getNewMaterialLotCode());
            mtMaterialLotVO20List.add(mtMaterialLotVO20);
            if(StringUtils.isNotEmpty(oldMtMaterialLot.getEoId())){
                MtEoVO mtEoVO = new MtEoVO();
                mtEoVO.setEoId(oldMtMaterialLot.getEoId());
                mtEoVO.setIdentification(dto.getNewMaterialLotCode());
                eoMessageList.add(mtEoVO);
            }
        }
        hmeSnReplaceVO.setMtMaterialLotVO20List(mtMaterialLotVO20List);
        hmeSnReplaceVO.setEoMessageList(eoMessageList);
        hmeSnReplaceVO.setHmeSnReplaceDTOList(dtoList);
        return hmeSnReplaceVO;
    }
}
