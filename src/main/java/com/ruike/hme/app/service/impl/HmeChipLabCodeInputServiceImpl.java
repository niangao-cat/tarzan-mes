package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeChipLabCodeInputDTO;
import com.ruike.hme.app.service.HmeChipLabCodeInputService;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.repository.HmeMaterialLotLoadRepository;
import com.ruike.hme.domain.vo.HmeChipLabCodeInputVO;
import com.ruike.hme.domain.vo.HmeChipLabCodeInputVO2;
import com.ruike.hme.domain.vo.HmeMaterialLotLoadVO2;
import com.ruike.hme.infra.mapper.HmeChipLabCodeInputMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 芯片实验代码录入应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2021-11-01 11:05:20
 */
@Service
public class HmeChipLabCodeInputServiceImpl implements HmeChipLabCodeInputService {

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeChipLabCodeInputMapper hmeChipLabCodeInputMapper;
    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;
    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public HmeChipLabCodeInputVO scanBarCode(Long tenantId, String barcode) {
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setMaterialLotCode(barcode);
            setTenantId(tenantId);
        }});
        if(Objects.isNull(mtMaterialLot)){
            //报错扫描条码不存在,请检查!
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        //根据物料批ID查询物料批装载信息
        List<HmeChipLabCodeInputVO2> materialLotLoadList = hmeChipLabCodeInputMapper.materialLotLoadListQuery(tenantId, mtMaterialLot.getMaterialLotId());
        if(CollectionUtils.isEmpty(materialLotLoadList)){
            //当前条码未进行贴片,请检查!
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        long count = materialLotLoadList.stream().filter(item -> StringUtils.isBlank(item.getAttribute4())).count();
        if(count > 0){
            //当前条码未进行贴片,请检查!
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        //盒子信息查询
        HmeChipLabCodeInputVO result = hmeChipLabCodeInputMapper.materialLotInfoQuery(tenantId, mtMaterialLot.getMaterialLotId());
        List<HmeChipLabCodeInputVO2> finalMaterialLotLoadList = new ArrayList<>();
        for (int i = 1; i <= Integer.parseInt(result.getLocationRow()); i++) {
            for (int j = 1; j <= Integer.parseInt(result.getLocationColumn()); j++) {
                Long loadRow = (long) i;
                Long loadColumn = (long) j;
                List<HmeChipLabCodeInputVO2> collect = materialLotLoadList.stream()
                        .filter(t -> t.getLoadRow().equals(loadRow) && t.getLoadColumn().equals(loadColumn))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect)) {
                    finalMaterialLotLoadList.add(collect.get(0));
                } else {
                    finalMaterialLotLoadList.add(new HmeChipLabCodeInputVO2());
                }
            }
        }
        result.setMaterialLotLoadList(finalMaterialLotLoadList);
        return result;
    }

    @Override
    public void confirm(Long tenantId, HmeChipLabCodeInputDTO dto) {
        List<HmeMaterialLotLoad> hmeMaterialLotLoadList = hmeMaterialLotLoadRepository.selectByCondition(Condition.builder(HmeMaterialLotLoad.class)
                .andWhere(Sqls.custom().andIn(HmeMaterialLotLoad.FIELD_MATERIAL_LOT_LOAD_ID, dto.getMaterialLotLoadIdList()))
                .build());
        List<String> sqlList = new ArrayList<>();
        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        Date now = new Date();
        for (HmeMaterialLotLoad hmeMaterialLotLoad:hmeMaterialLotLoadList) {
            hmeMaterialLotLoad.setAttribute19(dto.getChipLabCode());
            hmeMaterialLotLoad.setAttribute20(dto.getChipLabRemark());
            hmeMaterialLotLoad.setLastUpdatedBy(userId);
            hmeMaterialLotLoad.setLastUpdateDate(now);
            hmeMaterialLotLoad.setObjectVersionNumber(hmeMaterialLotLoad.getObjectVersionNumber() + 1);
            sqlList.addAll(mtCustomDbRepository.getUpdateSql(hmeMaterialLotLoad));
        }
        if(CollectionUtils.isNotEmpty(sqlList)){
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }
}
