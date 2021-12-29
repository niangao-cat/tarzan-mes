package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeFacYkHis;
import com.ruike.hme.domain.entity.HmeFacYkVO2;
import com.ruike.hme.domain.repository.HmeFacYkHisRepository;
import com.ruike.hme.domain.vo.HmeFacYkHisVO;
import com.ruike.hme.domain.vo.HmeFacYkVO;
import com.ruike.hme.infra.mapper.HmeFacYkMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeFacYk;
import com.ruike.hme.domain.repository.HmeFacYkRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * FAC-Y宽判定标准表 资源库实现
 *
 * @author TAO.XU02@hand-china.com 2021-02-01 10:45:03
 */
@Component
@Transactional
public class HmeFacYkRepositoryImpl extends BaseRepositoryImpl<HmeFacYk> implements HmeFacYkRepository {

    @Autowired
    private HmeFacYkMapper hmeFacYkMapper;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private HmeFacYkHisRepository hmeFacYkHisRepository;

    /**
     * @param pageRequest 分页参数
     * @param hmeFacYk    FAC-Y宽判定标准表
     * @param tenantId    租户ID
     * @return com.ruike.hme.domain.vo.HmeFacYkVO
     * @Description: FAC-Y宽判定标准表分页查询
     * @author: TAO.XU02
     * @date 2021/2/4 14:21
     */
    @Override
    public Page<HmeFacYkVO> selectHmeFacYk(PageRequest pageRequest, HmeFacYkVO2 hmeFacYk, Long tenantId) {
        return PageHelper.doPage(pageRequest, () -> hmeFacYkMapper.selectHmeFacYk(hmeFacYk, tenantId));
    }

    /***
     * @Description: 获取当前时间
     */
    public static Date currentTimeGet() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(currentTime.format(formatter), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    /***
     * @Description: 获取当前用户
     */
    CustomUserDetails curUser = DetailsHelper.getUserDetails();
    Long userId = curUser == null ? -1L : curUser.getUserId();

    /**
     * @param tenantId    租户ID
     * @param hmeFacYkVO2 FAC-Y宽判定标准表
     * @return void
     * @Description: FAC-Y宽判定标准表新建
     * @author: TAO.XU02
     * @date 2021/2/4 14:21
     */
    @Override
    public void insertHmeFacYk(HmeFacYkVO2 hmeFacYkVO2, Long tenantId) {
        List<String> facYkIdList = customDbRepository.getNextKeys("hme_fac_yk_s", hmeFacYkVO2.getWorkcellIdList().size());
        List<String> cidList = customDbRepository.getNextKeys("hme_fac_yk_cid_s", hmeFacYkVO2.getWorkcellIdList().size());
        Integer index = 0;
        for (String workcellId : hmeFacYkVO2.getWorkcellIdList()) {
            HmeFacYk hmeFacYk = new HmeFacYk();
            BeanUtils.copyProperties(hmeFacYkVO2, hmeFacYk);
            hmeFacYk.setWorkcellId(workcellId);
            hmeFacYk.setFacYkId(facYkIdList.get(index));
            hmeFacYk.setCid(Long.valueOf(cidList.get(index++)));
            hmeFacYk.setTenantId(tenantId);
            hmeFacYk.setCreationDate(currentTimeGet());
            hmeFacYk.setLastUpdateDate(currentTimeGet());
            hmeFacYk.setCreatedBy(userId);
            hmeFacYk.setLastUpdatedBy(userId);
            hmeFacYkMapper.insertSelective(hmeFacYk);
        }
    }

    /**
     * @param tenantId    租户ID
     * @param hmeFacYk FAC-Y宽判定标准表
     * @Description: FAC-Y宽判定标准表更新
     * @author: TAO.XU02
     * @date 2021/2/4 14:24
     * @return void
     */
    @Override
    public void updateByHmeFacYk(HmeFacYk hmeFacYk, Long tenantId) {
        Long cids = Long.parseLong(customDbRepository.getNextKey("hme_fac_yk_cid_s"));
        hmeFacYk.setCid(cids);
        hmeFacYk.setTenantId(tenantId);
        hmeFacYk.setCreationDate(currentTimeGet());
        hmeFacYk.setLastUpdateDate(currentTimeGet());
        hmeFacYk.setCreatedBy(userId);
        hmeFacYk.setLastUpdatedBy(userId);
        hmeFacYkMapper.updateByPrimaryKeySelective(hmeFacYk);

        // 生成修改历史
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("HME_FAC-Y_MODIFIED");
        }});
        HmeFacYkHis hmeFacYkHis = new HmeFacYkHis();
        BeanUtils.copyProperties(hmeFacYk, hmeFacYkHis);
        hmeFacYkHis.setEventId(eventId);
        hmeFacYkHisRepository.insertSelective(hmeFacYkHis);
    }

    @Override
    public Page<HmeFacYkHisVO> hisListQuery(Long tenantId, String facYkId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmeFacYkMapper.hisListQuery(tenantId, facYkId));
    }

    @Override
    public List<HmeFacYkVO> facYkExport(Long tenantId, HmeFacYkVO2 hmeFacYkVO2) {
        return hmeFacYkMapper.selectHmeFacYk(hmeFacYkVO2, tenantId);
    }
}
