package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeCosBarCodeExceptionDTO;
import com.ruike.hme.api.dto.HmeCosWorkcellExceptionDTO;
import com.ruike.hme.app.service.HmeCosBarCodeExceptionService;
import com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO;
import com.ruike.hme.infra.mapper.HmeCosBarCodeExceptionMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * COS条码加工异常汇总报表
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/26 15:29
 */
@Service
public class HmeCosBarCodeExceptionServiceImpl implements HmeCosBarCodeExceptionService {

    @Autowired
    private HmeCosBarCodeExceptionMapper hmeCosBarCodeExceptionMapper;


    @Override
    public Page<HmeCosBarCodeExceptionVO> queryList(Long tenantId, HmeCosBarCodeExceptionDTO dto, PageRequest pageRequest) {
        Page<HmeCosBarCodeExceptionVO> result = PageHelper.doPage(pageRequest, () -> hmeCosBarCodeExceptionMapper.queryList(tenantId, dto));
        if(CollectionUtils.isNotEmpty(result.getContent())) {
            List<String> jobIdList = result.getContent().stream().map(HmeCosBarCodeExceptionVO::getJobId).distinct().collect(Collectors.toList());

            List<HmeCosBarCodeExceptionVO> equipmentList = hmeCosBarCodeExceptionMapper.queryEquipmentList(tenantId,jobIdList);
            Map<String,String> equipmentMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(equipmentList)){
                equipmentMap = equipmentList.stream().collect(Collectors.toMap(HmeCosBarCodeExceptionVO::getJobId, HmeCosBarCodeExceptionVO::getAssetEncoding));
            }

            for (HmeCosBarCodeExceptionVO vo : result.getContent()) {
                if (StringUtils.isNotBlank(vo.getHeatSinkLot())) {
                    vo.setHeatSinkType(vo.getHeatSinkLot().substring(7, 8));
                }
                vo.setAssetEncoding(equipmentMap.getOrDefault(vo.getJobId() , ""));
            }
        }
        return result;
    }
}
