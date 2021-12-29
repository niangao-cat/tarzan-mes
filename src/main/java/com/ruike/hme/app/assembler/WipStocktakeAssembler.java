package com.ruike.hme.app.assembler;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeWipStocktakeActual;
import com.ruike.hme.domain.entity.HmeWipStocktakeActualHis;
import com.ruike.hme.domain.repository.HmeWipStocktakeActualRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.util.BeanCopierUtil;
import com.ruike.wms.domain.repository.WmsMaterialLotRepository;
import com.ruike.wms.domain.vo.WmsMaterialLotAttrVO;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.stereotype.Component;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.LoadTypeCode.CONTAINER;
import static com.ruike.wms.infra.constant.WmsConstant.StocktakeType.FIRST_COUNT;

/**
 * <p>
 * 在制品盘点 转换器
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/8 14:55
 */
@Component
public class WipStocktakeAssembler {
    private final WmsMaterialLotRepository materialLotRepository;
    private final HmeWipStocktakeActualRepository wipStocktakeActualRepository;

    public WipStocktakeAssembler(WmsMaterialLotRepository materialLotRepository, HmeWipStocktakeActualRepository wipStocktakeActualRepository) {
        this.materialLotRepository = materialLotRepository;
        this.wipStocktakeActualRepository = wipStocktakeActualRepository;
    }

    public WipStocktakeMaterialLotWorkVO materialLotToWorkVo(WmsMaterialLotAttrVO materialLot) {
        WipStocktakeMaterialLotWorkVO obj = new WipStocktakeMaterialLotWorkVO();
        obj.setMaterialId(materialLot.getMaterialId());
        obj.setMaterialLotId(materialLot.getMaterialLotId());
        obj.setMaterialCode(materialLot.getMaterialCode());
        obj.setMaterialLotCode(materialLot.getMaterialLotCode());
        return obj;
    }

    public HmeWipStocktakeDocDTO11 rangeSaveCommandToAddRangeCommand(WipStocktakeRangeSaveCommandDTO command) {
        HmeWipStocktakeDocDTO11 addCommand = new HmeWipStocktakeDocDTO11();
        addCommand.setStocktakeId(command.getStocktakeId());
        addCommand.setRangeObjectType(command.getRangeList().get(0).getRangeObjectType());
        List<HmeWipStocktakeDocVO4> list = new ArrayList<>();
        command.getRangeList().forEach(rec -> {
            HmeWipStocktakeDocVO4 rangeAdd = new HmeWipStocktakeDocVO4();
            rangeAdd.setRangeObjectId(rec.getRangeObjectId());
            list.add(rangeAdd);
        });
        addCommand.setAddList(list);
        return addCommand;
    }

    public List<HmeWipStocktakeActual> actualSaveCommandToEntities(WipStocktakeActualSaveCommandDTO command) {
        List<HmeWipStocktakeActual> list = new ArrayList<>();
        List<WipStocktakeMaterialLotWorkVO> materialLots = command.getMaterialLots();
        if (CollectionUtils.isEmpty(materialLots)) {
            return list;
        }
        List<WmsMaterialLotAttrVO> materialLotAttrs = materialLotRepository.selectListWithAttrByIds(command.getTenantId(), materialLots.stream().map(WipStocktakeMaterialLotWorkVO::getMaterialLotId).collect(Collectors.toList()));
        Map<String, WmsMaterialLotAttrVO> attrMap = materialLotAttrs.stream().collect(Collectors.toMap(WmsMaterialLotAttrVO::getMaterialLotId, Function.identity()));
        command.getMaterialLots().forEach(rec -> {
            WmsMaterialLotAttrVO attr = attrMap.get(rec.getMaterialLotId());
            HmeWipStocktakeActual actual = new HmeWipStocktakeActual();
            actual.setStocktakeId(command.getStocktakeId());
            actual.setTenantId(command.getTenantId());
            actual.setSiteId(attr.getPlantId());
            actual.setMaterialLotId(attr.getMaterialLotId());
            actual.setLotCode(attr.getLot());
            actual.setMaterialId(attr.getMaterialId());
            actual.setWorkOrderId(rec.getWorkOrderId());
            actual.setProdLineId(rec.getProdLineId());
            actual.setWorkcellId(rec.getWorkcellId());
            actual.setContainerId(attr.getCurrentContainerId());
            actual.setUomId(attr.getPrimaryUomId());
            actual.setCurrentQuantity(attr.getPrimaryUomQty());
            list.add(actual);
        });
        return list;
    }

    public MtMaterialLotVO20 actualToUpdateStocktake(HmeWipStocktakeActual actual, String stocktakeFlag) {
        MtMaterialLotVO20 materialLotUpdate = new MtMaterialLotVO20();
        materialLotUpdate.setMaterialLotId(actual.getMaterialLotId());
        materialLotUpdate.setStocktakeFlag(stocktakeFlag);
        return materialLotUpdate;
    }

    public HmeWipStocktakeExecuteVO executeCommandToEntities(WipStocktakeExecuteCommandDTO command) {
        HmeWipStocktakeExecuteVO hmeWipStocktakeExecuteVO = new HmeWipStocktakeExecuteVO();
        List<HmeWipStocktakeActual> list = new ArrayList<>();
        List<MaterialLotVO> materialLots = command.getMaterialLots();
        if (CollectionUtils.isEmpty(materialLots)) {
            return hmeWipStocktakeExecuteVO;
        }
        List<String> materialLotIds = materialLots.stream().map(MaterialLotVO::getMaterialLotId).collect(Collectors.toList());
        List<WmsMaterialLotAttrVO> wmsMaterialLotAttrVOList = materialLotRepository.selectListWithAttrByIds(command.getTenantId(), materialLotIds);
        Map<String, WmsMaterialLotAttrVO> materialLotAttrMap2 = wmsMaterialLotAttrVOList.stream().collect(Collectors.toMap(WmsMaterialLotAttrVO::getMaterialLotId, Function.identity()));
        list.addAll(wipStocktakeActualRepository.selectByCondition(Condition.builder(HmeWipStocktakeActual.class).where(Sqls.custom().andEqualTo(HmeWipStocktakeActual.FIELD_STOCKTAKE_ID, command.getStocktakeId()).andIn(HmeWipStocktakeActual.FIELD_MATERIAL_LOT_ID, materialLotIds)).build()));
        //2021-08-27 17:02 edit by chaonan.hu for peng.zhao 增加返修SN盘点逻辑
        List<String> repairMaterialLotIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(list)){
            //通过取差集的方式来判断扫描条码中是否存在返修SN
            List<String> materialLotIdList = list.stream().map(HmeWipStocktakeActual::getMaterialLotId).distinct().collect(Collectors.toList());
            materialLotIds.removeAll(materialLotIdList);
            if(CollectionUtils.isNotEmpty(materialLotIds)){
                repairMaterialLotIdList = materialLotIds;
            }
        }else {
            repairMaterialLotIdList = materialLotIds;
        }
        if(CollectionUtils.isNotEmpty(repairMaterialLotIdList)){
            //根据返修SN查询对应的盘点实绩信息
            HmeWipStocktakeActualVO2 hmeWipStocktakeActualVO2 = wipStocktakeActualRepository.queryWipStocktakeActualByRepairMaterialLotId(command.getTenantId(), repairMaterialLotIdList, command.getStocktakeId());
            if(CollectionUtils.isNotEmpty(hmeWipStocktakeActualVO2.getErrorMaterialLotIdList())
                    && !HmeConstants.ConstantValue.YES.equals(command.getFlag())){
                //如果有返修SN未找到盘点实绩信息，则弹窗提示
                StringBuilder errorMaterialLotCode = new StringBuilder();
                for (String errorMaterialLotId:hmeWipStocktakeActualVO2.getErrorMaterialLotIdList()) {
                    WmsMaterialLotAttrVO wmsMaterialLotAttrVO = materialLotAttrMap2.get(errorMaterialLotId);
                    errorMaterialLotCode.append(wmsMaterialLotAttrVO.getMaterialLotCode());
                    errorMaterialLotCode.append("#");
                }
                hmeWipStocktakeExecuteVO.setFlag(HmeConstants.ConstantValue.YES);
                hmeWipStocktakeExecuteVO.setMaterialLotCode(errorMaterialLotCode.toString());
                return hmeWipStocktakeExecuteVO;
            }
            if(CollectionUtils.isNotEmpty(hmeWipStocktakeActualVO2.getHmeWipStocktakeActualList())){
                List<HmeWipStocktakeActual> hmeWipStocktakeActualList = hmeWipStocktakeActualVO2.getHmeWipStocktakeActualList();
                list.addAll(hmeWipStocktakeActualList);
                List<String> repairMaterialLotList = hmeWipStocktakeActualList.stream().map(HmeWipStocktakeActual::getMaterialLotId).collect(Collectors.toList());
                wmsMaterialLotAttrVOList.addAll(materialLotRepository.selectListWithAttrByIds(command.getTenantId(), repairMaterialLotList));
            }
        }
        Map<String, WmsMaterialLotAttrVO> materialLotAttrMap = wmsMaterialLotAttrVOList.stream().collect(Collectors.toMap(WmsMaterialLotAttrVO::getMaterialLotId, Function.identity()));
        Long userId = DetailsHelper.getUserDetails().getUserId();
        boolean firstFlag = FIRST_COUNT.equals(command.getStocktakeTypeCode());
        Date nowDate = DateUtil.date();
        list.forEach(rec -> {
            WmsMaterialLotAttrVO attr = materialLotAttrMap.get(rec.getMaterialLotId());
            if (firstFlag) {
                rec.setFirstcountBy(userId);
                rec.setFirstcountContainerId(CONTAINER.equals(command.getLoadObjectType()) ? command.getLoadObjectId() : null);
                rec.setFirstcountDate(nowDate);
                rec.setFirstcountMaterialId(attr.getMaterialId());
                rec.setFirstcountUomId(attr.getPrimaryUomId());
                rec.setFirstcountProdLineId(command.getProdLineId());
                rec.setFirstcountWorkcellId(command.getWorkcellId());
                rec.setFirstcountQuantity(CONTAINER.equals(command.getLoadObjectType()) ? rec.getCurrentQuantity() : command.getQuantity());
                rec.setFirstcountRemark(command.getRemark());
            } else {
                rec.setRecountBy(userId);
                rec.setRecountContainerId(CONTAINER.equals(command.getLoadObjectType()) ? command.getLoadObjectId() : null);
                rec.setRecountDate(nowDate);
                rec.setRecountMaterialId(attr.getMaterialId());
                rec.setRecountUomId(attr.getPrimaryUomId());
                rec.setRecountProdLineId(command.getProdLineId());
                rec.setRecountWorkcellId(command.getWorkcellId());
                rec.setRecountQuantity(CONTAINER.equals(command.getLoadObjectType()) ? rec.getCurrentQuantity() : command.getQuantity());
                rec.setRecountRemark(command.getRemark());
            }
        });
        hmeWipStocktakeExecuteVO.setHmeWipStocktakeActualList(list);
        return hmeWipStocktakeExecuteVO;
    }

    public HmeWipStocktakeActualHis actualToHis(HmeWipStocktakeActual record, String eventId) {
        HmeWipStocktakeActualHis his = new HmeWipStocktakeActualHis();
        BeanCopierUtil.copy(record, his);
        his.setEventId(eventId);
        return his;
    }

    public WipStocktakeMaterialDetailRepresentationDTO materialDetailToRepresentation(HmeWipStocktakeDocVO3 vo, String stocktakeTypeCode) {
        WipStocktakeMaterialDetailRepresentationDTO obj = new WipStocktakeMaterialDetailRepresentationDTO();
        BeanCopierUtil.copy(vo, obj);
        obj.setFirstcountQuantity(Optional.ofNullable(obj.getFirstcountQuantity()).orElse(BigDecimal.ZERO));
        obj.setRecountQuantity(Optional.ofNullable(obj.getRecountQuantity()).orElse(BigDecimal.ZERO));
        obj.setNotMatchFlag(FIRST_COUNT.equals(stocktakeTypeCode) ? (obj.getFirstcountQuantity().compareTo(obj.getCurrentQuantity()) != 0) : (obj.getRecountQuantity().compareTo(obj.getCurrentQuantity()) != 0));
        return obj;
    }

}
