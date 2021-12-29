package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeFifthAreaKanbanLoadService;
import com.ruike.hme.app.service.HmeFifthAreaKanbanService;
import com.ruike.hme.domain.entity.HmeFifthAreaKanban;
import com.ruike.hme.domain.entity.HmeFifthAreaKanbanLoad;
import com.ruike.hme.domain.repository.HmeFifthAreaKanbanRepository;
import com.ruike.hme.domain.vo.HmeFifthAreaKanbanVO2;
import com.ruike.hme.infra.mapper.HmeFifthAreaKanbanMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.infra.util.InterfaceUtils;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.sys.CustomSequence;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 五部看板应用服务默认实现
 *
 * @author penglin.sui@hand-china.com 2021-06-08 07:53:30
 */
@Service
public class HmeFifthAreaKanbanServiceImpl implements HmeFifthAreaKanbanService {

    @Autowired
    private HmeFifthAreaKanbanMapper hmeFifthAreaKanbanMapper;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private HmeFifthAreaKanbanLoadService hmeFifthAreaKanbanLoadService;

    @Autowired
    private HmeFifthAreaKanbanRepository hmeFifthAreaKanbanRepository;

    /**
     * 昨天的日期
     *
     * @return java.lang.String
     * @author penglin.sui@hand-china.com 2021/6/8 10:21
     */
    private String preOneDateGet(){
        //获取昨天日期
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        Date d=cal.getTime();
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
        return sp.format(d);
    }

    /**
     * 查询值集
     *
     * @return java.util.Map<java.lang.String , java.lang.String>
     * @author penglin.sui@hand-china.com 2021/6/8 10:34
     */
    private Map<String , String> lovValueGet(Long tenantId){
        //批量查询不良处理方式
        List<LovValueDTO> ncProcessMethodList = lovAdapter.queryLovValue("HME.NC_PROCESS_METHOD", tenantId);
        Map<String , String> ncProcessMethodMap = new HashMap<>();
        ncProcessMethodList.forEach(item -> {
            ncProcessMethodMap.put(item.getValue() , item.getMeaning());
        });
        return ncProcessMethodMap;
    }

    /**
     * 查询不良处理方式
     *
     * @return java.util.Map<java.lang.String , java.lang.String>
     * @author penglin.sui@hand-china.com 2021/6/8 11:41
     */
    private Map<String , String> ncProcessMethodGet(Long tenantId, List<HmeFifthAreaKanbanVO2> hmeFifthAreaKanbanVO2List){
        Map<String , String> ncProcessMethodMap = new HashMap<>();

        if(CollectionUtils.isNotEmpty(hmeFifthAreaKanbanVO2List)) {

            List<HmeFifthAreaKanbanVO2> ncProcessMethodList = new ArrayList<>();
            //分批次查询
            List<List<HmeFifthAreaKanbanVO2>> list = InterfaceUtils.splitSqlList(hmeFifthAreaKanbanVO2List, SQL_ITEM_COUNT_LIMIT);
            list.forEach(item -> {
                List<HmeFifthAreaKanbanVO2> subNcProcessMethodList = hmeFifthAreaKanbanMapper.selectNcProcessMethod(tenantId, item);
                ncProcessMethodList.addAll(subNcProcessMethodList);
            });

            if (CollectionUtils.isNotEmpty(ncProcessMethodList)) {
                //查询值集
                Map<String , String> ncProcessMethodLovMap = lovValueGet(tenantId);
                String processMethodMeaning = "";
                String preEoId = "" , preWorkcellId = "";
                for (HmeFifthAreaKanbanVO2 ncProcessMethod : ncProcessMethodList
                     ) {

                    //取每个 eo + 工位 下最新的一笔
                    if(preEoId.equals(ncProcessMethod.getEoId()) && preWorkcellId.equals(ncProcessMethod.getWorkcellId())){
                        continue;
                    }

                    processMethodMeaning = ncProcessMethodLovMap.getOrDefault(ncProcessMethod.getProcessMethod() , "");
                    if(StringUtils.isNotBlank(processMethodMeaning)){
                        ncProcessMethodMap.put(ncProcessMethod.getEoId() + "#" + ncProcessMethod.getWorkcellId() , processMethodMeaning);
                    }

                    preEoId = ncProcessMethod.getEoId();
                    preWorkcellId = ncProcessMethod.getWorkcellId();
                }

            }
        }

        return ncProcessMethodMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFifthArea(Long tenantId) {
        //获取昨天日期
        String preOneDate = preOneDateGet();
        String preOneStartTime = preOneDate + " 00:00:00";
        String preOneEndTime = preOneDate + " 23:59:59";

        //先批量删除
        hmeFifthAreaKanbanRepository.batchDeleteByAreaId(tenantId , preOneStartTime , preOneEndTime);

        //查询主数据
        List<HmeFifthAreaKanban> hmeFifthAreaKanbanList = hmeFifthAreaKanbanMapper.selectMainData(tenantId , preOneStartTime , preOneEndTime);
        if(CollectionUtils.isEmpty(hmeFifthAreaKanbanList)){
            return;
        }

        List<HmeFifthAreaKanbanVO2> hmeFifthAreaKanbanVO2List = new ArrayList<>();
        List<String> eoIdList = new ArrayList<>();
        for (HmeFifthAreaKanban hmeFifthAreaKanbanVO : hmeFifthAreaKanbanList
             ) {
            HmeFifthAreaKanbanVO2 hmeFifthAreaKanbanVO2 = new HmeFifthAreaKanbanVO2();
            hmeFifthAreaKanbanVO2.setEoId(hmeFifthAreaKanbanVO.getEoId());
            hmeFifthAreaKanbanVO2.setWorkcellId(hmeFifthAreaKanbanVO.getWorkcellId());
            if(!hmeFifthAreaKanbanVO2List.contains(hmeFifthAreaKanbanVO2)){
                hmeFifthAreaKanbanVO2List.add(hmeFifthAreaKanbanVO2);
            }

            if(!eoIdList.contains(hmeFifthAreaKanbanVO.getEoId())){
                eoIdList.add(hmeFifthAreaKanbanVO.getEoId());
            }
        }

        //批量查询不良处理方式
        Map<String , String> ncProcessMethodMap = ncProcessMethodGet(tenantId , hmeFifthAreaKanbanVO2List);

        //批量新增
        hmeFifthAreaKanbanRepository.batchInsertFifthArea(hmeFifthAreaKanbanList , ncProcessMethodMap);

        //批量新增装载信息
        hmeFifthAreaKanbanLoadService.createFifthAreaLoad(tenantId , eoIdList);
    }
}
