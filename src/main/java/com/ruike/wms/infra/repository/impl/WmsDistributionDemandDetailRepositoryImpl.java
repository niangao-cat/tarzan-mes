package com.ruike.wms.infra.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.ruike.wms.domain.vo.WmsDistDemandCreateVO;
import com.ruike.wms.domain.vo.WmsDistributionListQueryVO3;
import com.ruike.wms.infra.mapper.WmsDistributionDemandDetailMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.wms.domain.entity.WmsDistributionDemandDetail;
import com.ruike.wms.domain.repository.WmsDistributionDemandDetailRepository;
import org.springframework.stereotype.Component;

/**
 * 配送需求明细表 资源库实现
 *
 * @author penglin.sui@hand-china.com 2020-08-04 11:08:57
 */
@Component
public class WmsDistributionDemandDetailRepositoryImpl extends BaseRepositoryImpl<WmsDistributionDemandDetail> implements WmsDistributionDemandDetailRepository {

    private final WmsDistributionDemandDetailMapper wmsDistributionDemandDetailMapper;

    public WmsDistributionDemandDetailRepositoryImpl(WmsDistributionDemandDetailMapper wmsDistributionDemandDetailMapper) {
        this.wmsDistributionDemandDetailMapper = wmsDistributionDemandDetailMapper;
    }

    @Override
    public List<WmsDistributionDemandDetail> selectListByDemandId(Long tenantId, String distDemandId) {
        return wmsDistributionDemandDetailMapper.selectListByDemandId(tenantId, distDemandId);
    }

    @Override
    public List<WmsDistDemandCreateVO> selectCreateListByDemandIdList(Long tenantId, List<String> idList) {
        return getLocator(wmsDistributionDemandDetailMapper.selectCreateListByDemandIdList(tenantId, idList));
    }

    @Override
    public List<WmsDistDemandCreateVO> selectCreateListByDemandIdListNew(Long tenantId, List<String> idList) {
        return getLocator(wmsDistributionDemandDetailMapper.selectCreateListByDemandIdListNew(tenantId, idList));
    }

    /**
     * 根据优先级，优先配送库位，没有配送库位的使用默认存储库位
     * @author xin.t@raycuslaser.com 2021-07-19
     */
    private List<WmsDistDemandCreateVO> getLocator(List<WmsDistDemandCreateVO> list){
        List<WmsDistDemandCreateVO> resList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)){
            Map<String, List<WmsDistDemandCreateVO>> locatorMap = list.stream().collect(Collectors.groupingBy(WmsDistDemandCreateVO::getDemandDetailId));
            Set<String> keyList = locatorMap.keySet();
            for(String key:keyList){
                List<WmsDistDemandCreateVO> setList = locatorMap.get(key);
                if (CollectionUtils.isNotEmpty(setList)){
                    Map<String,WmsDistDemandCreateVO> setMap = setList.stream().collect(Collectors.toMap(WmsDistDemandCreateVO::getLocatorType,c->c));
                    WmsDistDemandCreateVO locatorX = setMap.get("27");
                    if(locatorX != null){
                        resList.add(locatorX);
                    }else{
                        WmsDistDemandCreateVO locatorY = setMap.get("DEFAULT_STORAGE");
                        if(locatorY != null){
                            resList.add(locatorY);
                        }
                    }
                }
            }
        }
        return resList;
    }

    @Override
    public List<WmsDistributionDemandDetail> selectListByDetailIdList(Long tenantId, List<String> idList) {
        return wmsDistributionDemandDetailMapper.selectListByDetailIdList(tenantId, idList);
    }

    @Override
    public List<WmsDistributionDemandDetail> selectAttritionListByDetailIdList(Long tenantId, List<String> idList) {
        return wmsDistributionDemandDetailMapper.selectAttritionListByDetailIdList(tenantId, idList);
    }

    @Override
    public List<String> getSubstitute(Long tenantId, String distDemandId, String workOrderId) {
        return wmsDistributionDemandDetailMapper.getSubstitute(tenantId, distDemandId,workOrderId);
    }

    @Override
    public List<WmsDistributionListQueryVO3> selectByInstructionId(Long tenantId, String instructionDocId, List<String> instructionIds) {
        return wmsDistributionDemandDetailMapper.selectByInstructionId(tenantId, instructionDocId,instructionIds);
    }

    @Override
    public int updateByPrimaryKey(WmsDistributionDemandDetail detail) {
        return wmsDistributionDemandDetailMapper.updateByPrimaryKey(detail);
    }

    @Override
    public List<WmsDistributionDemandDetail> batchUpdateByPrimaryKey(List<WmsDistributionDemandDetail> list) {
        list.forEach(wmsDistributionDemandDetailMapper::updateByPrimaryKey);
        return list;
    }
}
