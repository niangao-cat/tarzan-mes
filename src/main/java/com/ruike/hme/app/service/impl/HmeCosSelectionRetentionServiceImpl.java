package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeCosSelectionRetentionDTO;
import com.ruike.hme.app.service.HmeCosSelectionRetentionService;
import com.ruike.hme.domain.entity.HmeCosFunction;
import com.ruike.hme.domain.entity.HmeNcDetail;
import com.ruike.hme.domain.entity.HmeSelectionDetails;
import com.ruike.hme.domain.repository.HmeCosFunctionRepository;
import com.ruike.hme.domain.vo.HmeCosSelectionRetentionVO;
import com.ruike.hme.domain.vo.HmeInStorageMaterialVO;
import com.ruike.hme.domain.vo.HmeProcessReportVo;
import com.ruike.hme.infra.mapper.HmeCosFunctionMapper;
import com.ruike.hme.infra.mapper.HmeCosSelectionRetentionMapper;
import com.ruike.hme.infra.mapper.HmeSelectionDetailsMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import static java.util.stream.Collectors.toList;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.material.domain.vo.MtMaterialVO;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * COS筛选滞留表
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/02/24 17:52
 */
@Service
public class HmeCosSelectionRetentionServiceImpl implements HmeCosSelectionRetentionService {

    @Autowired
    private HmeCosSelectionRetentionMapper hmeCosSelectionRetentionMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeCosFunctionMapper hmeCosFunctionMapper;
    @Autowired
    private HmeCosFunctionRepository hmeCosFunctionRepository;
    @Autowired
    private LovAdapter lovAdapter;

    @Override
    @ProcessLovValue
    public Page<HmeCosSelectionRetentionVO> queryList(Long tenantId, HmeCosSelectionRetentionDTO dto, PageRequest pageRequest) {
        //HME.COS_ITEM_GROUP 值
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
        if (CollectionUtils.isEmpty(lovValueDTOS)) {
            throw new MtException("HME_COS_PATCH_PDA_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_PATCH_PDA_0015", "HME"));
        }
        List<String> itemGroupList = lovValueDTOS.stream().map(LovValueDTO::getValue).collect(Collectors.toList());

        Page<HmeCosSelectionRetentionVO> result = PageHelper.doPage(pageRequest, () -> hmeCosSelectionRetentionMapper.queryList(tenantId, dto, itemGroupList));

        for (HmeCosSelectionRetentionVO hmeCosSelectionRetentionVO : result) {
            //位置
            hmeCosSelectionRetentionVO.setPosition(String.valueOf((char)(hmeCosSelectionRetentionVO.getLoadRow()+64))+hmeCosSelectionRetentionVO.getLoadColumn());
            //筛选状态 如果没关联到数据则显示未挑选
            if(StringUtils.isBlank(hmeCosSelectionRetentionVO.getSelectionStatusMeaning())){
                hmeCosSelectionRetentionVO.setSelectionStatusMeaning("未挑选");
            }
            //挑选来源位置 oldLoad 转化
            if(StringUtils.isNotBlank(hmeCosSelectionRetentionVO.getOldLoad())) {
                String[] split = hmeCosSelectionRetentionVO.getOldLoad().split(",");
                hmeCosSelectionRetentionVO.setSelectedPosition(String.valueOf((char) (Integer.parseInt(split[0]) + 64)) + split[1]);
            }


            HmeCosFunction hmeCosFunction = new HmeCosFunction();
            hmeCosFunction.setLoadSequence(hmeCosSelectionRetentionVO.getLoadSequence());
            if(StringUtils.isNotBlank(dto.getCurrent())) {
                hmeCosFunction.setCurrent(dto.getCurrent());
            }
            List<HmeCosFunction> hmeCosFunctionList = hmeCosFunctionMapper.select(hmeCosFunction);

            hmeCosSelectionRetentionVO.setListA02(hmeCosFunctionList);
            hmeCosSelectionRetentionVO.setListA04(hmeCosFunctionList);
            hmeCosSelectionRetentionVO.setListA06(hmeCosFunctionList);
            hmeCosSelectionRetentionVO.setListA15(hmeCosFunctionList);

            //筛选 current = 5
            List<HmeCosFunction> collect = hmeCosFunctionList.stream().filter(f -> "5".equals(f.getCurrent())).collect(Collectors.toList());
            if(collect.size() > 0){
                hmeCosSelectionRetentionVO.setA04(collect.get(0).getA04() != null ? collect.get(0).getA04() : BigDecimal.valueOf(0));
                hmeCosSelectionRetentionVO.setA01(collect.get(0).getA01());
                hmeCosSelectionRetentionVO.setA03(collect.get(0).getA03());
            }
        }
        Map<String, List<HmeCosSelectionRetentionVO>> listMap = result.stream().filter(f -> StringUtils.isNotBlank(f.getVirtualNum())).collect(Collectors.groupingBy(HmeCosSelectionRetentionVO::getVirtualNum));
        for (Map.Entry<String, List<HmeCosSelectionRetentionVO>> entry : listMap.entrySet()) {
            List<HmeCosSelectionRetentionVO> hmeCosSelectionRetentionVOList = entry.getValue();
            double avg = hmeCosSelectionRetentionVOList.stream().map(HmeCosSelectionRetentionVO::getA04).filter(Objects::nonNull).collect(toList()).stream().mapToDouble(BigDecimal::doubleValue).summaryStatistics().getAverage();
            for (HmeCosSelectionRetentionVO vo : result) {
                if(StringUtils.isNotBlank(vo.getVirtualNum()) && entry.getKey().equals(vo.getVirtualNum()) && vo.getA04() != null){
                    vo.setAvg(BigDecimal.valueOf(avg));
                }
            }
        }
        return result;
    }

    @Override
    @ProcessLovValue
    public List<HmeCosSelectionRetentionVO> queryRetentionExport(Long tenantId, HmeCosSelectionRetentionDTO dto, HttpServletResponse response) {
        //HME.CHIP_ITEM_GROUP 值
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.CHIP_ITEM_GROUP", tenantId);
        if (CollectionUtils.isEmpty(lovValueDTOS)) {
            throw new MtException("HME_COS_PATCH_PDA_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_PATCH_PDA_0015", "HME"));
        }
        List<String> itemGroupList = lovValueDTOS.stream().map(LovValueDTO::getValue).collect(Collectors.toList());

        List<HmeCosSelectionRetentionVO> result = hmeCosSelectionRetentionMapper.queryList(tenantId, dto, itemGroupList);

        Map<String, List<HmeCosFunction>> hmeCosFunctionOMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(result)){
            List<String> loadSequenceList = result.stream().map(HmeCosSelectionRetentionVO::getLoadSequence).distinct().collect(Collectors.toList());
            List<HmeCosFunction> hmeCosFunctionList = hmeCosFunctionRepository.hmeCosFunctionPropertyBatchGet(tenantId,loadSequenceList,dto.getCurrent());
            hmeCosFunctionOMap = hmeCosFunctionList.stream().filter(f -> StringUtils.isNotBlank(f.getLoadSequence())).collect(Collectors.groupingBy(HmeCosFunction::getLoadSequence));
        }

        for (HmeCosSelectionRetentionVO hmeCosSelectionRetentionVO : result) {
            //位置
            hmeCosSelectionRetentionVO.setPosition(String.valueOf((char)(hmeCosSelectionRetentionVO.getLoadRow()+64))+hmeCosSelectionRetentionVO.getLoadColumn());
            //筛选状态 如果没关联到数据则显示未挑选
            if(StringUtils.isBlank(hmeCosSelectionRetentionVO.getSelectionStatusMeaning())){
                hmeCosSelectionRetentionVO.setSelectionStatusMeaning("未挑选");
            }
            //挑选来源位置 oldLoad 转化
            if(StringUtils.isNotBlank(hmeCosSelectionRetentionVO.getOldLoad())) {
                String[] split = hmeCosSelectionRetentionVO.getOldLoad().split(",");
                hmeCosSelectionRetentionVO.setSelectedPosition(String.valueOf((char) (Integer.parseInt(split[0]) + 64)) + split[1]);
            }


            HmeCosFunction hmeCosFunction = new HmeCosFunction();
            hmeCosFunction.setLoadSequence(hmeCosSelectionRetentionVO.getLoadSequence());
            if(StringUtils.isNotBlank(dto.getCurrent())) {
                hmeCosFunction.setCurrent(dto.getCurrent());
            }

            List<HmeCosFunction> hmeCosFunctionList = new ArrayList<>();
            if (StringUtils.isNotBlank(hmeCosSelectionRetentionVO.getLoadSequence())) {
                hmeCosFunctionList = hmeCosFunctionOMap.getOrDefault(hmeCosSelectionRetentionVO.getLoadSequence(),null);
            }

            hmeCosSelectionRetentionVO.setListA02(hmeCosFunctionList);
            hmeCosSelectionRetentionVO.setListA04(hmeCosFunctionList);
            hmeCosSelectionRetentionVO.setListA06(hmeCosFunctionList);
            hmeCosSelectionRetentionVO.setListA15(hmeCosFunctionList);

            //筛选 current = 5
            List<HmeCosFunction> collect = hmeCosFunctionList.stream().filter(f -> "5".equals(f.getCurrent())).collect(Collectors.toList());
            if(collect.size() > 0){
                hmeCosSelectionRetentionVO.setA04(collect.get(0).getA04() != null ? collect.get(0).getA04() : BigDecimal.valueOf(0));
                hmeCosSelectionRetentionVO.setA01(collect.get(0).getA01());
                hmeCosSelectionRetentionVO.setA03(collect.get(0).getA03());
            }
        }
        Map<String, List<HmeCosSelectionRetentionVO>> listMap = result.stream().filter(f -> StringUtils.isNotBlank(f.getVirtualNum())).collect(Collectors.groupingBy(HmeCosSelectionRetentionVO::getVirtualNum));
        for (Map.Entry<String, List<HmeCosSelectionRetentionVO>> entry : listMap.entrySet()) {
            List<HmeCosSelectionRetentionVO> hmeCosSelectionRetentionVOList = entry.getValue();
            double avg = hmeCosSelectionRetentionVOList.stream().map(HmeCosSelectionRetentionVO::getA04).filter(Objects::nonNull).collect(toList()).stream().mapToDouble(BigDecimal::doubleValue).summaryStatistics().getAverage();
            for (HmeCosSelectionRetentionVO vo : result) {
                if(StringUtils.isNotBlank(vo.getVirtualNum()) && entry.getKey().equals(vo.getVirtualNum()) && vo.getA04() != null){
                    vo.setAvg(BigDecimal.valueOf(avg));
                }
            }
        }
        return result;
    }
}
