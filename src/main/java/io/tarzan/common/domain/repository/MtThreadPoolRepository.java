package io.tarzan.common.domain.repository;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import tarzan.material.domain.entity.MtMaterialCategory;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.method.domain.entity.MtRouter;
import tarzan.method.domain.vo.MtBomVO7;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtModWorkcell;

/**
 * @author peng.yuan
 * @ClassName MtThreadPoolRepository
 * @description
 * @date 2019年11月08日 17:15
 */
public interface MtThreadPoolRepository {
    /**
     * 得到物料集合对象
     *
     * @Author peng.yuan
     * @Date 2019/11/8 16:55
     * @param tenantId :
     * @param idList :
     * @return java.util.concurrent.Future<java.util.List<tarzan.material.domain.vo.MtMaterialVO>>
     */
    Future<List<MtMaterialVO>> getMaterialFuture(ThreadPoolTaskExecutor poolExecutor, Long tenantId,
                                                 List<String> idList);

    /**
     * 得到物料集合
     *
     * @Author peng.yuan
     * @Date 2019/11/8 17:00
     * @param tenantId :
     * @param idList :
     * @return java.util.concurrent.Future<java.util.List<tarzan.modeling.domain.entity.MtModSite>>
     */
    Future<List<MtModSite>> getModSiteFuture(ThreadPoolTaskExecutor poolExecutor, Long tenantId, List<String> idList);

    /**
     * 得到生产线集合
     *
     * @Author peng.yuan
     * @Date 2019/11/8 17:00
     * @param tenantId :
     * @param idList :
     * @return java.util.concurrent.Future<java.util.List<tarzan.modeling.domain.entity.MtModProductionLine>>
     */
    Future<List<MtModProductionLine>> getModProductionLineFuture(ThreadPoolTaskExecutor poolExecutor, Long tenantId,
                                                                 List<String> idList);

    /**
     * 得到库位集合
     *
     * @Author peng.yuan
     * @Date 2019/11/8 17:00
     * @param tenantId :
     * @param idList :
     * @return java.util.concurrent.Future<java.util.List<tarzan.modeling.domain.entity.MtModLocator>>
     */
    Future<List<MtModLocator>> getModLocatorFuture(ThreadPoolTaskExecutor poolExecutor, Long tenantId,
                                                   List<String> idList);

    /**
     * 得到单位集合
     *
     * @Author peng.yuan
     * @Date 2019/11/8 17:01
     * @param tenantId :
     * @param idList :
     * @return java.util.concurrent.Future<java.util.List<tarzan.material.domain.vo.MtUomVO>>
     */
    Future<List<MtUomVO>> getUomFuture(ThreadPoolTaskExecutor poolExecutor, Long tenantId, List<String> idList);

    /**
     * 获取区域集合
     *
     * @Author Xie.yiyang
     * @Date 2019/11/11 16:19
     * @param poolExecutor, tenantId, areaIds
     * @return java.util.concurrent.Future<java.util.List<tarzan.modeling.domain.entity.MtModArea>>
     */
    Future<List<MtModArea>> getModAreaFuture(ThreadPoolTaskExecutor poolExecutor, Long tenantId, List<String> areaIds);

    /**
     * 获取工作单元集合
     *
     * @Author Xie.yiyang
     * @Date 2019/11/11 16:26
     * @param poolExecutor, tenantId, workcellIds
     * @return java.util.concurrent.Future<java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>>
     */
    Future<List<MtModWorkcell>> getModWorkcellFuture(ThreadPoolTaskExecutor poolExecutor, Long tenantId,
                                                     List<String> workcellIds);

    /**
     * 获取装配清单集合
     *
     * @Author Xie.yiyang
     * @Date 2019/11/11 16:34
     * @param poolExecutor, tenantId, bomIds
     * @return java.util.concurrent.Future<java.util.List<tarzan.method.domain.vo.MtBomVO7>>
     */
    Future<List<MtBomVO7>> getBomFuture(ThreadPoolTaskExecutor poolExecutor, Long tenantId, List<String> bomIds);

    /**
     * 获取工艺路线集合
     *
     * @Author Xie.yiyang
     * @Date 2019/11/11 16:36
     * @param poolExecutor, tenantId, routerIds
     * @return java.util.concurrent.Future<java.util.List<tarzan.method.domain.entity.MtRouter>>
     */
    Future<List<MtRouter>> getRouterFuture(ThreadPoolTaskExecutor poolExecutor, Long tenantId, List<String> routerIds);

    /**
     * 获取工艺物料类别集合
     *
     * @Author Xie.yiyang
     * @Date 2019/11/11 19:02
     * @param poolExecutor, tenantId, strings
     * @return java.util.concurrent.Future<java.util.List<tarzan.material.domain.vo.MtMaterialVO>>
     */
    Future<List<MtMaterialCategory>> getMaterialCategoryFuture(ThreadPoolTaskExecutor poolExecutor, Long tenantId,
                                                               List<String> materialCategoryIds);
}
