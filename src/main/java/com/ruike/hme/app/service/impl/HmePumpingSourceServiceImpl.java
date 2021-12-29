package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmePumpingSourceDTO;
import com.ruike.hme.app.service.HmePumpingSourceService;
import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.vo.HmePumpingSourceAllVO;
import com.ruike.hme.domain.vo.HmePumpingSourceVO;
import com.ruike.hme.domain.vo.HumepingVO;
import com.ruike.hme.infra.mapper.HmePumpingSourceMapper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.tools.shell.CommandException;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.stereotype.Service;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 泵浦源性能数据展示
 *
 * @author wengang.qiang@hand-china.com 2021/09/01 17:23
 */
@Service
public class HmePumpingSourceServiceImpl implements HmePumpingSourceService {
    private final MtExtendSettingsMapper mtExtendSettingsMapper;
    private final LovAdapter lovAdapter;
    private final HmePumpingSourceMapper hmePumpingSourceMapper;
    private final MtTagRepository mtTagRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;

    public HmePumpingSourceServiceImpl(MtExtendSettingsMapper mtExtendSettingsMapper, LovAdapter lovAdapter,
                                       HmePumpingSourceMapper hmePumpingSourceMapper,
                                       MtTagRepository mtTagRepository, MtErrorMessageRepository mtErrorMessageRepository) {
        this.mtExtendSettingsMapper = mtExtendSettingsMapper;
        this.lovAdapter = lovAdapter;
        this.hmePumpingSourceMapper = hmePumpingSourceMapper;
        this.mtTagRepository = mtTagRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
    }

    @Override
    public HmePumpingSourceAllVO queryHmePumpingSource(Long tenantId, HmePumpingSourceDTO hmePumpingSourceDto,
                                                       PageRequest pageRequest) {
        HmePumpingSourceAllVO hmePumpingSourceAllVO = new HmePumpingSourceAllVO();
        //获取到值集HME_PUMP_SOURCE_WKC下的value,用于查询工序id
        List<LovValueDTO> hmePumpSourceWkc = lovAdapter.queryLovValue("HME_PUMP_SOURCE_WKC", tenantId);

        //根据code查到工位id
        List<String> workCellIdList = hmePumpingSourceMapper.queryWorkcellIdList(tenantId, hmePumpSourceWkc.get(0).getValue());
        if (CollectionUtils.isEmpty(workCellIdList)) {
            //值集【HME_PUMP_SOURCE_WKC】中维护的工艺下未找到对应工位,请检查工艺与工序关系
            throw new MtException("HME_PUMP_PERFORMANCE_001", mtErrorMessageRepository.getErrorMessageWithModule(
                    tenantId, "HME_PUMP_PERFORMANCE_001", "HME"
            ));
        }
        if (CollectionUtils.isEmpty(workCellIdList)) {
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository.getErrorMessageWithModule(
                    tenantId, "HME_EO_JOB_SN_028", "HME"
            ));
        }

        List<HmePumpingSourceVO> hmePumpingSourceVOList = hmePumpingSourceMapper.queryHmePumping(tenantId, hmePumpingSourceDto);
        if (CollectionUtils.isEmpty(hmePumpingSourceVOList)) {
            throw new MtException("HME_PUMP_POSITION_002", mtErrorMessageRepository.getErrorMessageWithModule(
                    tenantId, "HME_PUMP_POSITION_002", "HME")
            );
        }
        List<String> eoIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(hmePumpingSourceVOList)) {
            //拿到vo中的eo_id
            hmePumpingSourceVOList.forEach(hmePumpingSourceVO -> {
                eoIdList.add(hmePumpingSourceVO.getEoId());
            });
        }
        //根据eo_id和workcellId去查找job_id
        List<HmeEoJobSn> jobIdList = hmePumpingSourceMapper.queryJobId(tenantId, eoIdList, workCellIdList);
        // 根据EOId 取最大的jobId
        List<String> newHmeEoJobSn = new ArrayList<>();
        Map<Object, List<HmeEoJobSn>> groupJobSnMap = jobIdList.stream().collect(Collectors.groupingBy(e -> this.splice(e)));
        hmePumpingSourceVOList.forEach(hmePumpingSourceVO -> {
            List<HmeEoJobSn> hmeEoJobSnValue = groupJobSnMap.get(hmePumpingSourceVO.getEoId());
            //按出站时间找出最新的记录拿到job_id
            HmeEoJobSn newEoJobSn = hmeEoJobSnValue.stream().max(Comparator.comparing(HmeEoJobSn::getSiteOutDate)).get();
            //将获取到的最新的记录装进newHmeEoJobSn里
            newHmeEoJobSn.add(newEoJobSn.getJobId());
            //将最新的jobId放进vo里
            hmePumpingSourceVO.setJobId(newEoJobSn.getJobId());
        });

        // 查询扩展字段
        String[] attrNames = {"current"};
        List<MtExtendSettings> settings = new ArrayList<>();

        for (int i = 0; i < attrNames.length; i++) {
            MtExtendSettings setting = new MtExtendSettings();
            setting.setAttrName(attrNames[i]);
            settings.add(setting);
        }
        MtMaterialLot materialId = hmePumpingSourceMapper.queryMaterialId(tenantId, hmePumpingSourceDto);
        if (CollectionUtils.isNotEmpty(hmePumpingSourceVOList)) {
            List<MtExtendAttrVO> values = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                    "mt_material_attr",
                    "MATERIAL_ID",
                    materialId.getMaterialId(), settings);
            String current = CollectionUtils.isNotEmpty(values) ? values.get(0).getAttrValue() : "";
            //拿到到扩展字段对应的值
            List<LovValueDTO> currentValue = lovAdapter.queryLovValue("HME_TAG_CURRENT", tenantId);
            List<LovValueDTO> valueDTOList = currentValue.stream().filter(lovValueDTO -> StringUtils.equals(lovValueDTO.getMeaning(),
                    current)).collect(Collectors.toList());
            //拿到HME_TAG_CURRENT值集中,meaning等于current的所有tag_code
            List<String> tagCodeList = valueDTOList.stream().map(LovValueDTO::getTag).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(tagCodeList)){
                throw new MtException("HME_PUMP_PERFORMANCE_002",mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_PUMP_PERFORMANCE_002","HME"));
            }
            //根据tagCode去查tag,可以拿到tag_id
            List<MtTag> mtTagList = mtTagRepository.selectByCodeList(tenantId, tagCodeList);
            //获取tag_id
            List<String> tagIdList = new ArrayList<>();
            mtTagList.forEach(mtTag -> {
                tagIdList.add(mtTag.getTagId());
            });
            //根据最新的job_id和tag_id去record表查找记录
            List<HmeEoJobDataRecord> recordsList = hmePumpingSourceMapper.queryDataRecord(tenantId, tagIdList, newHmeEoJobSn);
            //对recordsList按tag_id和job_id进行分组
            Map<String, List<HmeEoJobDataRecord>> groupByRecordMap = recordsList.stream().collect(Collectors.groupingBy(e -> this.spliceRecord(e)));
            List<LovValueDTO> lovValueDTOList = lovAdapter.queryLovValue("HME.PUMP_P", tenantId);
            List<String> lovValue = new ArrayList<>();
            BigDecimal powerSum = new BigDecimal(0);
            if (CollectionUtils.isNotEmpty(lovValueDTOList)) {
                lovValue = lovValueDTOList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
            }
            //拿到result，行转列,根据tag_id和job_id去recordsList集合里找对应result
            if (CollectionUtils.isNotEmpty(hmePumpingSourceVOList)) {
                for (HmePumpingSourceVO hmePumpingSourceVO : hmePumpingSourceVOList
                ) {
                    List<HumepingVO> humePingVOList = new ArrayList<>();
                    for (MtTag mtTag : mtTagList) {
                        HumepingVO humepingVO = new HumepingVO();
                        humepingVO.setTagCode(mtTag.getTagCode());
                        humepingVO.setTagDescription(mtTag.getTagDescription());
                        String keyMap = mtTag.getTagId() + hmePumpingSourceVO.getJobId();
                        List<HmeEoJobDataRecord> hmeEoJobDataRecordList = groupByRecordMap.get(keyMap);
                        if (CollectionUtils.isNotEmpty(hmeEoJobDataRecordList)) {
                            humepingVO.setResult(hmeEoJobDataRecordList.get(0).getResult());
                        }
                        if (lovValue.contains(humepingVO.getTagCode())) {
                            //计算功率之和
                            powerSum = powerSum.add(new BigDecimal(humepingVO.getResult()));
                        }
                        humePingVOList.add(humepingVO);
                    }
                    hmePumpingSourceVO.setHumePingVOList(humePingVOList);
                }
                //封装返回给前端总的数据
                hmePumpingSourceAllVO.setHmePumpingSourceVOList(hmePumpingSourceVOList);
                hmePumpingSourceAllVO.setHumePingVOList(hmePumpingSourceVOList.get(0).getHumePingVOList());
                hmePumpingSourceAllVO.setPowerSum(powerSum);
            }
        }
        return hmePumpingSourceAllVO;
    }

    private String spliceRecord(HmeEoJobDataRecord hmeEoJobDataRecord) {
        //用tag_id和job_id构成新对象
        StringBuffer sb = new StringBuffer();
        sb.append(hmeEoJobDataRecord.getTagId()).
                append(hmeEoJobDataRecord.getJobId());
        return sb.toString();
    }

    private String splice(HmeEoJobSn hmeEoJobSn) {
        //用eo_id构成新对象
        StringBuffer sb = new StringBuffer();
        sb.append(hmeEoJobSn.getEoId());
        return sb.toString();
    }

}
