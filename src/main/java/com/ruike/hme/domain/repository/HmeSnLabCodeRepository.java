package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeSnLabCodeVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeSnLabCode;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * SN工艺实验代码表资源库
 *
 * @author penglin.sui@hand-china.com 2021-03-19 16:01:36
 */
public interface HmeSnLabCodeRepository extends BaseRepository<HmeSnLabCode> {

    /**
     * SN工艺实验代码表资源库 根据条码查询
     *
     * @param pageRequest
     * @param hmeSnLabCode
     * @param tenantId
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-22 14:11
     * @return Page<HmeSnLabCodeVO>
     */
    Page<HmeSnLabCodeVO> listByMaterialLotId(PageRequest pageRequest, HmeSnLabCode hmeSnLabCode, Long tenantId);

    /**
     * SN工艺实验代码表资源库 保存
     *
     * @param hmeSnLabCode
     * @param tenantId
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-22 14:12
     * @return void
     */
    void save(HmeSnLabCode hmeSnLabCode, Long tenantId);
}
