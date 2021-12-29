package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeCosFunctionMaterialService;
import com.ruike.hme.app.service.HmeCosFunctionMaterialTimeService;
import com.ruike.hme.domain.entity.HmeCosFunctionMaterial;
import com.ruike.hme.domain.entity.HmeCosFunctionMaterialTime;
import com.ruike.hme.domain.entity.HmeFifthAreaKanban;
import com.ruike.hme.domain.repository.HmeCosFunctionMaterialRepository;
import com.ruike.hme.domain.repository.HmeCosFunctionMaterialTimeRepository;
import com.ruike.hme.domain.vo.HmeCosFunctionMaterialVO;
import com.ruike.hme.domain.vo.HmeCosFunctionMaterialVO2;
import com.ruike.hme.domain.vo.HmeEoJobSnBatchVO4;
import com.ruike.hme.infra.mapper.HmeCosFunctionMaterialMapper;
import com.ruike.hme.infra.mapper.HmeCosFunctionMaterialTimeMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.ws.Action;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * COS投料性能表应用服务默认实现
 *
 * @author penglin.sui@hand-china.com 2021-06-22 20:50:13
 */
@Service
public class HmeCosFunctionMaterialServiceImpl implements HmeCosFunctionMaterialService {

    @Autowired
    private HmeCosFunctionMaterialMapper hmeCosFunctionMaterialMapper;

    @Autowired
    private HmeCosFunctionMaterialTimeMapper hmeCosFunctionMaterialTimeMapper;

    @Autowired
    private HmeCosFunctionMaterialTimeService hmeCosFunctionMaterialTimeService;

    @Autowired
    private HmeCosFunctionMaterialRepository hmeCosFunctionMaterialRepository;

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private HmeCosFunctionMaterialTimeRepository hmeCosFunctionMaterialTimeRepository;

    @Autowired
    private WmsSiteRepository wmsSiteRepository;

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * 获取预挑选查询条件
     *
     * @param hmeCosFunctionMaterialVOList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosFunctionMaterialVO2>
     */
    private List<HmeCosFunctionMaterialVO2> getSelectionQueryParas(List<HmeCosFunctionMaterialVO> hmeCosFunctionMaterialVOList) {
        List<HmeCosFunctionMaterialVO2> hmeCosFunctionMaterialVO2List = new ArrayList<>();

        if(CollectionUtils.isEmpty(hmeCosFunctionMaterialVOList)){
            return hmeCosFunctionMaterialVO2List;
        }

        //循环数据采集组编码截取第三部分的数字作为目标电流点
        hmeCosFunctionMaterialVOList.forEach(item -> {
            String[] split = item.getTagCode().replace(item.getAlias() + "-", "").split("-");
            if (split.length == 3) {
                for (String str : split) {
                    if (Pattern.matches("^(\\d)+A$", str)) {
                        String current = str.replace("A", "");
                        HmeCosFunctionMaterialVO2 hmeCosFunctionMaterialVO2 = new HmeCosFunctionMaterialVO2();
                        hmeCosFunctionMaterialVO2.setMaterialId(item.getMaterialId());
                        hmeCosFunctionMaterialVO2.setCurrent(current);
                        hmeCosFunctionMaterialVO2List.add(hmeCosFunctionMaterialVO2);
                    }
                }
            }
        });

        return hmeCosFunctionMaterialVO2List;
    }

    /**
     * 获取性能查询条件
     *
     * @param selectionList
     * @param selectionQueryParasMap
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosFunctionMaterialVO2>
     */
    private List<HmeCosFunctionMaterialVO2> getFunctionQueryParas(List<HmeCosFunctionMaterialVO2> selectionList , Map<String,List<HmeCosFunctionMaterialVO2>> selectionQueryParasMap) {
        List<HmeCosFunctionMaterialVO2> functionQueryParaList = new ArrayList<>();

        if(CollectionUtils.isEmpty(selectionList)){
            return functionQueryParaList;
        }

        for (HmeCosFunctionMaterialVO2 item : selectionList
             ) {
            List<HmeCosFunctionMaterialVO2> hmeCosFunctionMaterialVO2List = selectionQueryParasMap.getOrDefault(item.getMaterialId() , new ArrayList<>());
            if(CollectionUtils.isNotEmpty(hmeCosFunctionMaterialVO2List)){
                List<String> currentList = hmeCosFunctionMaterialVO2List.stream()
                        .filter(Objects::nonNull)
                        .map(HmeCosFunctionMaterialVO2::getCurrent)
                        .collect(Collectors.toList());

                for (String current : currentList
                ) {
                    HmeCosFunctionMaterialVO2 hmeCosFunctionMaterialVO2 = new HmeCosFunctionMaterialVO2();
                    hmeCosFunctionMaterialVO2.setLoadSequence(item.getLoadSequence());
                    hmeCosFunctionMaterialVO2.setCurrent(current);
                    functionQueryParaList.add(hmeCosFunctionMaterialVO2);
                }
            }
        }

        //去重
        if(CollectionUtils.isNotEmpty(functionQueryParaList)){
            functionQueryParaList = functionQueryParaList.stream()
                    .filter(distinctByKey(item -> item.getLoadSequence() + "#" + item.getCurrent()))
                    .collect(Collectors.toList());
        }

        return functionQueryParaList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long tenantId , List<HmeCosFunctionMaterial> hmeCosFunctionMaterialList){
        List<String> loadSequenceList = hmeCosFunctionMaterialList.stream()
                .filter(Objects::nonNull)
                .map(HmeCosFunctionMaterial::getLoadSequence)
                .distinct()
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(loadSequenceList)){
            return;
        }

        List<HmeCosFunctionMaterial> existsHmeCosFunctionMaterialList = hmeCosFunctionMaterialMapper.selectByCondition(Condition.builder(HmeCosFunctionMaterial.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(HmeCosFunctionMaterial.FIELD_TENANT_ID, tenantId)
                        .andIn(HmeCosFunctionMaterial.FIELD_LOAD_SEQUENCE , loadSequenceList))
                .build());
        if(CollectionUtils.isEmpty(existsHmeCosFunctionMaterialList)){
            return;
        }

        List<String> cosFunctionMaterialIdList = existsHmeCosFunctionMaterialList.stream()
                .map(HmeCosFunctionMaterial::getCosFunctionMaterialId)
                .collect(Collectors.toList());

        //删除
        hmeCosFunctionMaterialRepository.batchDeleteByPrimary(cosFunctionMaterialIdList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchSave(Long tenantId , List<HmeCosFunctionMaterial> hmeCosFunctionMaterialList){

        if(CollectionUtils.isEmpty(hmeCosFunctionMaterialList)){
            return;
        }

        //按load_sequence批量删除
        batchDelete(tenantId , hmeCosFunctionMaterialList);

        List<String> idList = customSequence.getNextKeys("hme_cos_function_material_time_s", hmeCosFunctionMaterialList.size());
        List<String> cidList = customSequence.getNextKeys("hme_cos_function_material_time_cid_s", hmeCosFunctionMaterialList.size());
        int index = 0;

        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        //当前时间
        Date date = CommonUtils.currentTimeGet();

        for (HmeCosFunctionMaterial hmeCosFunctionMaterial : hmeCosFunctionMaterialList
        ) {
            hmeCosFunctionMaterial.setTenantId(tenantId);
            hmeCosFunctionMaterial.setCosFunctionMaterialId(idList.get(index));
            hmeCosFunctionMaterial.setCid(Long.valueOf(cidList.get(index++)));
            hmeCosFunctionMaterial.setObjectVersionNumber(1L);
            hmeCosFunctionMaterial.setCreatedBy(userId);
            hmeCosFunctionMaterial.setCreationDate(date);
            hmeCosFunctionMaterial.setLastUpdatedBy(userId);
            hmeCosFunctionMaterial.setLastUpdateDate(date);
        }

        //批量新增数据
        hmeCosFunctionMaterialRepository.myBatchInsert(hmeCosFunctionMaterialList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCosFunctionMaterial(Long tenantId) {

        //获取本次执行时间
        HmeCosFunctionMaterialTime hmeCosFunctionMaterialTime = hmeCosFunctionMaterialTimeService.selectCurrentTime(tenantId);

        try {
            //查询数据收集组
            List<HmeCosFunctionMaterialVO> hmeCosFunctionMaterialVOList = hmeCosFunctionMaterialMapper.selectTags(tenantId);
            if (CollectionUtils.isEmpty(hmeCosFunctionMaterialVOList)) {
                return;
            }

            //获取预挑选明细查询条件
            List<HmeCosFunctionMaterialVO2> selectionQueryParasList = getSelectionQueryParas(hmeCosFunctionMaterialVOList);
            if (CollectionUtils.isEmpty(selectionQueryParasList)) {
                return;
            }

            //获取物料ID
            List<String> materialIdList = selectionQueryParasList.stream()
                    .filter(Objects::nonNull)
                    .map(HmeCosFunctionMaterialVO2::getMaterialId)
                    .distinct()
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(materialIdList)) {
                return;
            }

            //查询预挑选明细
            List<HmeCosFunctionMaterialVO2> selectionList = hmeCosFunctionMaterialMapper.selectSelection(tenantId, materialIdList,hmeCosFunctionMaterialTime);
            if (CollectionUtils.isEmpty(selectionList)) {
                return;
            }

            //预挑选明细查询转map
            Map<String, List<HmeCosFunctionMaterialVO2>> selectionQueryParasMap = selectionQueryParasList.stream().collect(Collectors.groupingBy(e -> e.getMaterialId()));

            //获取性能查询条件
            List<HmeCosFunctionMaterialVO2> functionQueryParasList = getFunctionQueryParas(selectionList, selectionQueryParasMap);
            if (CollectionUtils.isEmpty(functionQueryParasList)) {
                return;
            }

            //获取用户默认站点
            String defaultSiteId = wmsSiteRepository.userDefaultSite(tenantId);

            //批量查询性能数据
            List<HmeCosFunctionMaterial> hmeCosFunctionMaterialList = new ArrayList<>();
            List<List<HmeCosFunctionMaterialVO2>> splitSqlList = InterfaceUtils.splitSqlList(functionQueryParasList, 350);
            for (List<HmeCosFunctionMaterialVO2> domains : splitSqlList) {
                List<HmeCosFunctionMaterial> subHmeCosFunctionMaterialList = hmeCosFunctionMaterialMapper.selectCosFunction(tenantId, defaultSiteId, domains, hmeCosFunctionMaterialTime);
                if (CollectionUtils.isNotEmpty(subHmeCosFunctionMaterialList)) {
                    hmeCosFunctionMaterialList.addAll(subHmeCosFunctionMaterialList);
                }
            }

            if (CollectionUtils.isEmpty(hmeCosFunctionMaterialList)) {
                return;
            }

            //批量新增数据
            batchSave(tenantId, hmeCosFunctionMaterialList);
        }catch (Exception e){
            throw new MtException("createCosFunctionMaterial Exception", e.getMessage());
        }finally {
            //保存本次的时间范围
            hmeCosFunctionMaterialTimeRepository.saveCosFunctionMaterialTime(tenantId , hmeCosFunctionMaterialTime.getStartTime() , hmeCosFunctionMaterialTime.getEndTime());
        }
    }
}
