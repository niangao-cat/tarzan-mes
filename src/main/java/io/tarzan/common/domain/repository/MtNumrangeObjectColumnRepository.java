package io.tarzan.common.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.entity.MtNumrangeObjectColumn;

/**
 * 编码对象属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
public interface MtNumrangeObjectColumnRepository
                extends BaseRepository<MtNumrangeObjectColumn>, AopProxy<MtNumrangeObjectColumnRepository> {

    /**
     * propertyLimitNumrangeObjectColumnQuery-根据编码对象列属性获取对象列
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/6/19
     */
    List<MtNumrangeObjectColumn> propertyLimitNumrangeObjectColumnQuery(Long tenantId, MtNumrangeObjectColumn dto);
}
