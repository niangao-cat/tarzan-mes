package io.tarzan.common.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.entity.MtNumrangeHis;

/**
 * 号码段定义历史表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
public interface MtNumrangeHisRepository extends BaseRepository<MtNumrangeHis>, AopProxy<MtNumrangeHisRepository> {

}
