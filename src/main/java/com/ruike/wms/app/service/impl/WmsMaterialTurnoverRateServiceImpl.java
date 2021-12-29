package com.ruike.wms.app.service.impl;

import com.ruike.wms.api.dto.WmsMaterialTurnoverRateDTO;
import com.ruike.wms.app.service.WmsMaterialTurnoverRateService;
import com.ruike.wms.domain.vo.WmsIqcInspectionDetailsVO;
import com.ruike.wms.domain.vo.WmsMaterialTurnoverRateVO;
import com.ruike.wms.domain.vo.WmsMaterialTurnoverRateVO2;
import com.ruike.wms.infra.mapper.WmsIqcInspectionDetailsMapper;
import com.ruike.wms.infra.mapper.WmsMaterialTurnoverRateMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.inventory.domain.entity.MtInvJournal;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/09/27 17:17
 */
@Service
public class WmsMaterialTurnoverRateServiceImpl implements WmsMaterialTurnoverRateService {

    @Autowired
    private WmsMaterialTurnoverRateMapper wmsMaterialTurnoverRateMapper;

    @Override
    public Page<WmsMaterialTurnoverRateVO> queryList(Long tenantId, WmsMaterialTurnoverRateDTO dto, PageRequest pageRequest) {
        if(StringUtils.isBlank(dto.getStartDate())||StringUtils.isBlank(dto.getEndDate())){
            throw new MtException("exception","开始时间或结束时间为空");
        }
        Page<WmsMaterialTurnoverRateVO> page =
                PageHelper.doPage(pageRequest, () -> wmsMaterialTurnoverRateMapper.queryList(tenantId, dto));
        for(WmsMaterialTurnoverRateVO wmsMaterialTurnoverRateVO:page){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String startDate = null;
            String endDate = null;
            try {
                startDate = sdf.format(sdf.parse(dto.getStartDate()));
                endDate = sdf.format(sdf.parse(dto.getEndDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            wmsMaterialTurnoverRateVO.setStartDate(startDate);
            wmsMaterialTurnoverRateVO.setEndDate(endDate);
            BigDecimal startInventory = BigDecimal.valueOf(0);
            BigDecimal endInventory = BigDecimal.valueOf(0);
            List<WmsMaterialTurnoverRateVO2> wmsMaterialTurnoverRateVO2List = wmsMaterialTurnoverRateMapper.selectQtyByStart(tenantId,wmsMaterialTurnoverRateVO.getMaterialId(),wmsMaterialTurnoverRateVO.getLocatorId(),wmsMaterialTurnoverRateVO.getStartDate());
            for(WmsMaterialTurnoverRateVO2 wmsMaterialTurnoverRateVO2:wmsMaterialTurnoverRateVO2List){
                MtInvJournal mtInvJournal = wmsMaterialTurnoverRateMapper.selectStartInvJournal(wmsMaterialTurnoverRateVO2.getTenantId(),
                        wmsMaterialTurnoverRateVO2.getSiteId(),wmsMaterialTurnoverRateVO2.getMaterialId(),wmsMaterialTurnoverRateVO2.getLocatorId(),
                        wmsMaterialTurnoverRateVO2.getLotCode(),wmsMaterialTurnoverRateVO2.getOwnerId(),wmsMaterialTurnoverRateVO2.getOwnerType(),wmsMaterialTurnoverRateVO.getStartDate());
                BigDecimal startQty = BigDecimal.valueOf(0);
                if(mtInvJournal != null){
                    if(mtInvJournal.getChangeQuantity()>=0){
                        startQty = wmsMaterialTurnoverRateMapper.selectStartQty(wmsMaterialTurnoverRateVO2.getTenantId(),
                                wmsMaterialTurnoverRateVO2.getSiteId(),wmsMaterialTurnoverRateVO2.getMaterialId(),wmsMaterialTurnoverRateVO2.getLocatorId(),
                                wmsMaterialTurnoverRateVO2.getLotCode(),wmsMaterialTurnoverRateVO2.getOwnerId(),wmsMaterialTurnoverRateVO2.getOwnerType(),wmsMaterialTurnoverRateVO.getStartDate(),"Y");
                    }else{
                        startQty = wmsMaterialTurnoverRateMapper.selectStartQty(wmsMaterialTurnoverRateVO2.getTenantId(),
                                wmsMaterialTurnoverRateVO2.getSiteId(),wmsMaterialTurnoverRateVO2.getMaterialId(),wmsMaterialTurnoverRateVO2.getLocatorId(),
                                wmsMaterialTurnoverRateVO2.getLotCode(),wmsMaterialTurnoverRateVO2.getOwnerId(),wmsMaterialTurnoverRateVO2.getOwnerType(),wmsMaterialTurnoverRateVO.getStartDate(),"N");
                    }
                }
                if(startQty!=null){
                    startInventory = startInventory.add(startQty);
                }
            }
            List<WmsMaterialTurnoverRateVO2> wmsMaterialTurnoverRateVO2List1 = wmsMaterialTurnoverRateMapper.selectQtyByEnd(tenantId,wmsMaterialTurnoverRateVO.getMaterialId(),wmsMaterialTurnoverRateVO.getLocatorId(),wmsMaterialTurnoverRateVO.getEndDate());
            for(WmsMaterialTurnoverRateVO2 wmsMaterialTurnoverRateVO2:wmsMaterialTurnoverRateVO2List1){
                MtInvJournal mtInvJournal1 = wmsMaterialTurnoverRateMapper.selectEndInvJournal(wmsMaterialTurnoverRateVO2.getTenantId(),
                        wmsMaterialTurnoverRateVO2.getSiteId(),wmsMaterialTurnoverRateVO2.getMaterialId(),wmsMaterialTurnoverRateVO2.getLocatorId(),
                        wmsMaterialTurnoverRateVO2.getLotCode(),wmsMaterialTurnoverRateVO2.getOwnerId(),wmsMaterialTurnoverRateVO2.getOwnerType(),wmsMaterialTurnoverRateVO.getEndDate());
                BigDecimal endQty = BigDecimal.valueOf(0);
                if(mtInvJournal1 != null){
                    if(mtInvJournal1.getChangeQuantity()>=0){
                        endQty = wmsMaterialTurnoverRateMapper.selectEndQty(wmsMaterialTurnoverRateVO2.getTenantId(),
                                wmsMaterialTurnoverRateVO2.getSiteId(),wmsMaterialTurnoverRateVO2.getMaterialId(),wmsMaterialTurnoverRateVO2.getLocatorId(),
                                wmsMaterialTurnoverRateVO2.getLotCode(),wmsMaterialTurnoverRateVO2.getOwnerId(),wmsMaterialTurnoverRateVO2.getOwnerType(),wmsMaterialTurnoverRateVO.getEndDate(),"Y");
                    }else{
                        endQty = wmsMaterialTurnoverRateMapper.selectEndQty(wmsMaterialTurnoverRateVO2.getTenantId(),
                                wmsMaterialTurnoverRateVO2.getSiteId(),wmsMaterialTurnoverRateVO2.getMaterialId(),wmsMaterialTurnoverRateVO2.getLocatorId(),
                                wmsMaterialTurnoverRateVO2.getLotCode(),wmsMaterialTurnoverRateVO2.getOwnerId(),wmsMaterialTurnoverRateVO2.getOwnerType(),wmsMaterialTurnoverRateVO.getEndDate(),"N");
                    }
                }
                if(endQty!=null){
                    endInventory = endInventory.add(endQty);
                }
            }
            wmsMaterialTurnoverRateVO.setStartInventory(startInventory);
            wmsMaterialTurnoverRateVO.setEndInventory(endInventory);
            if(startInventory.add(endInventory).compareTo(BigDecimal.ZERO) != 0){
                BigDecimal rate = wmsMaterialTurnoverRateVO.getSendQty().multiply(new BigDecimal(100)).divide((startInventory.add(endInventory)).divide(BigDecimal.valueOf(2),BigDecimal.ROUND_HALF_UP),2,BigDecimal.ROUND_HALF_UP);
                String turnoverRate = String.valueOf(rate)+"%";
                wmsMaterialTurnoverRateVO.setTurnoverRate(turnoverRate);
            }
        }
        return page;
    }

    @Override
    public List<WmsMaterialTurnoverRateVO> export(Long tenantId, WmsMaterialTurnoverRateDTO dto, ExportParam exportParam) {
        List<WmsMaterialTurnoverRateVO> wmsMaterialTurnoverRateVOList = wmsMaterialTurnoverRateMapper.queryList(tenantId, dto);
        for(WmsMaterialTurnoverRateVO wmsMaterialTurnoverRateVO:wmsMaterialTurnoverRateVOList){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String startDate = null;
            String endDate = null;
            try {
                startDate = sdf.format(sdf.parse(dto.getStartDate()));
                endDate = sdf.format(sdf.parse(dto.getEndDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            wmsMaterialTurnoverRateVO.setStartDate(startDate);
            wmsMaterialTurnoverRateVO.setEndDate(endDate);
            BigDecimal startInventory = BigDecimal.valueOf(0);
            BigDecimal endInventory = BigDecimal.valueOf(0);
            List<WmsMaterialTurnoverRateVO2> wmsMaterialTurnoverRateVO2List = wmsMaterialTurnoverRateMapper.selectQtyByStart(tenantId,wmsMaterialTurnoverRateVO.getMaterialId(),wmsMaterialTurnoverRateVO.getLocatorId(),wmsMaterialTurnoverRateVO.getStartDate());
            for(WmsMaterialTurnoverRateVO2 wmsMaterialTurnoverRateVO2:wmsMaterialTurnoverRateVO2List){
                MtInvJournal mtInvJournal = wmsMaterialTurnoverRateMapper.selectStartInvJournal(wmsMaterialTurnoverRateVO2.getTenantId(),
                        wmsMaterialTurnoverRateVO2.getSiteId(),wmsMaterialTurnoverRateVO2.getMaterialId(),wmsMaterialTurnoverRateVO2.getLocatorId(),
                        wmsMaterialTurnoverRateVO2.getLotCode(),wmsMaterialTurnoverRateVO2.getOwnerId(),wmsMaterialTurnoverRateVO2.getOwnerType(),wmsMaterialTurnoverRateVO.getStartDate());
                BigDecimal startQty = BigDecimal.valueOf(0);
                if(mtInvJournal != null){
                    if(mtInvJournal.getChangeQuantity()>=0){
                        startQty = wmsMaterialTurnoverRateMapper.selectStartQty(wmsMaterialTurnoverRateVO2.getTenantId(),
                                wmsMaterialTurnoverRateVO2.getSiteId(),wmsMaterialTurnoverRateVO2.getMaterialId(),wmsMaterialTurnoverRateVO2.getLocatorId(),
                                wmsMaterialTurnoverRateVO2.getLotCode(),wmsMaterialTurnoverRateVO2.getOwnerId(),wmsMaterialTurnoverRateVO2.getOwnerType(),wmsMaterialTurnoverRateVO.getStartDate(),"Y");
                    }else{
                        startQty = wmsMaterialTurnoverRateMapper.selectStartQty(wmsMaterialTurnoverRateVO2.getTenantId(),
                                wmsMaterialTurnoverRateVO2.getSiteId(),wmsMaterialTurnoverRateVO2.getMaterialId(),wmsMaterialTurnoverRateVO2.getLocatorId(),
                                wmsMaterialTurnoverRateVO2.getLotCode(),wmsMaterialTurnoverRateVO2.getOwnerId(),wmsMaterialTurnoverRateVO2.getOwnerType(),wmsMaterialTurnoverRateVO.getStartDate(),"N");
                    }
                }
                if(startQty!=null){
                    startInventory = startInventory.add(startQty);
                }
            }
            List<WmsMaterialTurnoverRateVO2> wmsMaterialTurnoverRateVO2List1 = wmsMaterialTurnoverRateMapper.selectQtyByEnd(tenantId,wmsMaterialTurnoverRateVO.getMaterialId(),wmsMaterialTurnoverRateVO.getLocatorId(),wmsMaterialTurnoverRateVO.getEndDate());
            for(WmsMaterialTurnoverRateVO2 wmsMaterialTurnoverRateVO2:wmsMaterialTurnoverRateVO2List1){
                MtInvJournal mtInvJournal1 = wmsMaterialTurnoverRateMapper.selectEndInvJournal(wmsMaterialTurnoverRateVO2.getTenantId(),
                        wmsMaterialTurnoverRateVO2.getSiteId(),wmsMaterialTurnoverRateVO2.getMaterialId(),wmsMaterialTurnoverRateVO2.getLocatorId(),
                        wmsMaterialTurnoverRateVO2.getLotCode(),wmsMaterialTurnoverRateVO2.getOwnerId(),wmsMaterialTurnoverRateVO2.getOwnerType(),wmsMaterialTurnoverRateVO.getEndDate());
                BigDecimal endQty = BigDecimal.valueOf(0);
                if(mtInvJournal1 != null){
                    if(mtInvJournal1.getChangeQuantity()>=0){
                        endQty = wmsMaterialTurnoverRateMapper.selectEndQty(wmsMaterialTurnoverRateVO2.getTenantId(),
                                wmsMaterialTurnoverRateVO2.getSiteId(),wmsMaterialTurnoverRateVO2.getMaterialId(),wmsMaterialTurnoverRateVO2.getLocatorId(),
                                wmsMaterialTurnoverRateVO2.getLotCode(),wmsMaterialTurnoverRateVO2.getOwnerId(),wmsMaterialTurnoverRateVO2.getOwnerType(),wmsMaterialTurnoverRateVO.getEndDate(),"Y");
                    }else{
                        endQty = wmsMaterialTurnoverRateMapper.selectEndQty(wmsMaterialTurnoverRateVO2.getTenantId(),
                                wmsMaterialTurnoverRateVO2.getSiteId(),wmsMaterialTurnoverRateVO2.getMaterialId(),wmsMaterialTurnoverRateVO2.getLocatorId(),
                                wmsMaterialTurnoverRateVO2.getLotCode(),wmsMaterialTurnoverRateVO2.getOwnerId(),wmsMaterialTurnoverRateVO2.getOwnerType(),wmsMaterialTurnoverRateVO.getEndDate(),"N");
                    }
                }
                if(endQty!=null){
                    endInventory = endInventory.add(endQty);
                }
            }
            wmsMaterialTurnoverRateVO.setStartInventory(startInventory);
            wmsMaterialTurnoverRateVO.setEndInventory(endInventory);
            if(startInventory.add(endInventory).compareTo(BigDecimal.ZERO) != 0){
                BigDecimal rate = wmsMaterialTurnoverRateVO.getSendQty().multiply(new BigDecimal(100)).divide((startInventory.add(endInventory)).divide(BigDecimal.valueOf(2),BigDecimal.ROUND_HALF_UP),2,BigDecimal.ROUND_HALF_UP);
                String turnoverRate = String.valueOf(rate)+"%";
                wmsMaterialTurnoverRateVO.setTurnoverRate(turnoverRate);
            }
        }
        return wmsMaterialTurnoverRateVOList;
    }
}
