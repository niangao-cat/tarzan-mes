package io.tarzan.common.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.entity.MtNumrangeAssign;
import io.tarzan.common.domain.vo.MtNumrangeAssignVO2;

/**
 * 号码段分配表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:43
 */
public interface MtNumrangeAssignRepository
                extends BaseRepository<MtNumrangeAssign>, AopProxy<MtNumrangeAssignRepository> {
    /**
     * numrangeAssignPropertyQuery-依据属性获取号码段分配主键
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/6/25
     */
    List<MtNumrangeAssignVO2> numrangeAssignPropertyQuery(Long tenantId, MtNumrangeAssign dto);
}
