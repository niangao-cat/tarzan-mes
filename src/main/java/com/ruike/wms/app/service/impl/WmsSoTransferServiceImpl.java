package com.ruike.wms.app.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.ruike.itf.api.dto.ItfMaterialLotConfirmIfaceDTO;
import com.ruike.itf.app.service.ItfMaterialLotConfirmIfaceService;
import com.ruike.itf.app.service.ItfObjectTransactionIfaceService;
import com.ruike.itf.domain.vo.ItfMaterialLotConfirmIfaceVO4;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.wms.api.dto.WmsSoTransferDTO;
import com.ruike.wms.api.dto.WmsSoTransferDTO2;
import com.ruike.wms.api.dto.WmsSoTransferReturnDTO;
import com.ruike.wms.app.service.WmsSoTransferService;
import com.ruike.wms.domain.entity.WmsObjectTransaction;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsSoTransferRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO;
import com.ruike.wms.domain.vo.WmsSoTransferReturnVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.util.StringUtil;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName WmsSoTransferServiceImpl
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/22 11:16
 * @Version 1.0
 **/
@Service
public class WmsSoTransferServiceImpl implements WmsSoTransferService {

    @Autowired
    private WmsSoTransferRepository wmsSoTransferRepository;

    @Autowired
    private MtUserOrganizationRepository userOrganizationRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;

    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;

    @Autowired
    private ItfObjectTransactionIfaceService itfObjectTransactionIfaceService;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private ItfMaterialLotConfirmIfaceService itfMaterialLotConfirmIfaceService;

    @Autowired
    private MtMaterialLotMapper mtMaterialLotMapper;

    @Override
    @ProcessLovValue
    public Page<WmsSoTransferReturnDTO> querySo(Long tenantId, PageRequest pageRequest, WmsSoTransferDTO dto) {
        if (StringUtil.isEmpty(dto.getSiteId())) {
            Long userId = DetailsHelper.getUserDetails().getUserId();
            MtUserOrganization userOrganization = new MtUserOrganization();
            userOrganization.setUserId(userId);
            userOrganization.setOrganizationType("SITE");
            // 获取用户有权限的生产线
            List<MtUserOrganization> userOrganizations =
                    userOrganizationRepository.userOrganizationPermissionQuery(tenantId, userOrganization);
            List<String> siteIds = userOrganizations.stream().map(MtUserOrganization::getOrganizationId).collect(Collectors.toList());
            dto.setSiteIds(siteIds);
        }
        return PageHelper.doPage(pageRequest, () -> wmsSoTransferRepository.querySo(tenantId, dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void confirmSo(Long tenantId, List<WmsSoTransferDTO2> dtos) {
        //创建事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("SO_TRANSFER");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        List<WmsObjectTransactionRequestVO> wmsObjectTransactionList = new ArrayList<>();
        //循环处理
        for (WmsSoTransferDTO2 dto :
                dtos) {
            MtMaterialLot mtMaterialLot =
                    mtMaterialLotRepository.materialLotPropertyGet(tenantId, dto.getMaterialLotId());
            if ((StringUtil.isEmpty(dto.getSoLineNum()) && StringUtil.isNotEmpty(dto.getSoNum())) || (StringUtil.isEmpty(dto.getSoNum()) && StringUtil.isNotEmpty(dto.getSoLineNum()))) {
                throw new MtException("WMS_SO_TRANSFER_0002", mtErrorMessageRepository.getErrorMessageWithModule(
                        tenantId, "WMS_SO_TRANSFER_0002", "WMS", mtMaterialLot.getMaterialLotCode()));
            }
            if (StringUtil.isNotEmpty(dto.getSoLineNum())) {
                if (dto.getSoNum().equals(dto.getTransferSoNum()) && dto.getSoLineNum().equals(dto.getTransferSoLineNum())) {
                    throw new MtException("WMS_SO_TRANSFER_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                            tenantId, "WMS_SO_TRANSFER_0001", "WMS", mtMaterialLot.getMaterialLotCode()));
                }
            }

            //更新物料批
            mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                setEventId(eventId);
                setMaterialLotId(dto.getMaterialLotId());
            }}, "N");

            //更新扩展表
            List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("SO_NUM");
            mtExtendVO5.setAttrValue(dto.getTransferSoNum());
            mtExtendVO5List.add(mtExtendVO5);

            MtExtendVO5 mtExtendVO51 = new MtExtendVO5();
            mtExtendVO51.setAttrName("SO_LINE_NUM");
            mtExtendVO51.setAttrValue(dto.getTransferSoLineNum());
            mtExtendVO5List.add(mtExtendVO51);
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", dto.getMaterialLotId(), eventId, mtExtendVO5List);

            //调用{ objectTransactionSync }记录转单事务
            WmsObjectTransactionRequestVO objectTransactionRequestVO = new WmsObjectTransactionRequestVO();
            if (StringUtil.isEmpty(dto.getTransferSoLineNum())) {
                objectTransactionRequestVO.setTransactionTypeCode("WMS_SO_TO_NORMAL");
            } else {
                objectTransactionRequestVO.setTransactionTypeCode("WMS_NORMAL_TO_SO");
            }
            objectTransactionRequestVO.setEventId(eventId);
            objectTransactionRequestVO.setMaterialLotId(dto.getMaterialLotId());
            objectTransactionRequestVO.setMaterialId(mtMaterialLot.getMaterialId());
            objectTransactionRequestVO.setTransactionQty(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));
            objectTransactionRequestVO.setLotNumber(mtMaterialLot.getLot());
            objectTransactionRequestVO.setTransferLotNumber(mtMaterialLot.getLot());
            if (!StringUtils.isBlank(mtMaterialLot.getPrimaryUomId())) {
                MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterialLot.getPrimaryUomId());
                if (Objects.nonNull(mtUom)) {
                    objectTransactionRequestVO.setTransactionUom(mtUom.getUomCode());
                }
            }
            objectTransactionRequestVO.setTransactionTime(new Date());
            objectTransactionRequestVO.setTransactionReasonCode("销售订单变更");
            objectTransactionRequestVO.setPlantId(mtMaterialLot.getSiteId());
            if (!StringUtils.isBlank(mtMaterialLot.getLocatorId())) {
                objectTransactionRequestVO.setLocatorId(mtMaterialLot.getLocatorId());
                objectTransactionRequestVO.setTransferLocatorId(mtMaterialLot.getLocatorId());
                MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtMaterialLot.getLocatorId());
                if (Objects.nonNull(mtModLocator)) {
                    objectTransactionRequestVO.setWarehouseId(mtModLocator.getParentLocatorId());
                    objectTransactionRequestVO.setTransferWarehouseId(mtModLocator.getParentLocatorId());
                }
            }
            objectTransactionRequestVO.setTransferPlantId(mtMaterialLot.getSiteId());
            objectTransactionRequestVO.setRemark("销售订单变更");
            WmsTransactionType wmsTransactionType = wmsTransactionTypeRepository.selectOne(new WmsTransactionType() {{
                setTenantId(tenantId);
                setTransactionTypeCode(objectTransactionRequestVO.getTransactionTypeCode());
            }});
            if (ObjectUtil.isNotEmpty(wmsTransactionType)) {
                objectTransactionRequestVO.setMoveType(wmsTransactionType.getMoveType());
            }
            objectTransactionRequestVO.setSoNum(dto.getSoNum());
            objectTransactionRequestVO.setSoLineNum(dto.getSoLineNum());
            objectTransactionRequestVO.setTransferSoNum(dto.getTransferSoNum());
            objectTransactionRequestVO.setTransferSoLineNum(dto.getTransferSoLineNum());
            wmsObjectTransactionList.add(objectTransactionRequestVO);
        }
        List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS = wmsObjectTransactionRepository.objectTransactionSync(tenantId, wmsObjectTransactionList);
        itfObjectTransactionIfaceService.sendSapMaterialMove(tenantId, wmsObjectTransactionResponseVOS);
        List<String> materialLotIdList = dtos.stream().map(WmsSoTransferDTO2::getMaterialLotId).collect(Collectors.toList());
        List<WmsSoTransferReturnVO> wmsSoTransferReturnVOS = mtMaterialLotMapper.selectLocator(tenantId,materialLotIdList);
        List<String> materialLotCodes = new ArrayList<>();
        for(WmsSoTransferReturnVO wmsSoTransferReturnVO:wmsSoTransferReturnVOS){
            if("AUTO".equals(wmsSoTransferReturnVO.getLocatorType())){
                materialLotCodes.add(wmsSoTransferReturnVO.getMaterialLotCode());
            }
        }
        if(CollectionUtils.isNotEmpty(materialLotCodes)){
            ItfMaterialLotConfirmIfaceDTO itfMaterialLotConfirmIfaceDTO = new ItfMaterialLotConfirmIfaceDTO();
            itfMaterialLotConfirmIfaceDTO.setBarcodeList(materialLotCodes);
            List<ItfMaterialLotConfirmIfaceVO4> itfMaterialLotConfirmIfaceVO4s = itfMaterialLotConfirmIfaceService.itfMaterialLotConfirmOrChangeIface(tenantId,itfMaterialLotConfirmIfaceDTO);
            for(ItfMaterialLotConfirmIfaceVO4 itfMaterialLotConfirmIfaceVO4:itfMaterialLotConfirmIfaceVO4s){
                String msg = "";
                if(!itfMaterialLotConfirmIfaceVO4.getSuccess()){
                    msg = msg+itfMaterialLotConfirmIfaceVO4.getBarcode()+":"+itfMaterialLotConfirmIfaceVO4.getErrorMessage()+itfMaterialLotConfirmIfaceVO4.getMessage();
                }
                if(StringUtils.isNotBlank(msg)){
                    throw new MtException("exception",msg);
                }
            }
        }
    }
}
