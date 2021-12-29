package io.tarzan.common.infra.repository.impl;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtThreadPoolRepository;
import tarzan.material.domain.entity.MtMaterialCategory;
import tarzan.material.domain.repository.MtMaterialCategoryRepository;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.method.domain.entity.MtRouter;
import tarzan.method.domain.repository.MtBomRepository;
import tarzan.method.domain.repository.MtRouterRepository;
import tarzan.method.domain.vo.MtBomVO7;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModAreaRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

/**
 * @author peng.yuan
 * @ClassName MtThreadPoolRepositoryImpl
 * @description
 * @date 2019年11月08日 16:52
 */
@Component
public class MtThreadPoolRepositoryImpl implements MtThreadPoolRepository {

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtUomRepository mtUomRepository;


    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Autowired
    private MtModAreaRepository mtModAreaRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtBomRepository mtBomRepository;

    @Autowired
    private MtRouterRepository mtRouterRepository;

    @Autowired
    private MtMaterialCategoryRepository mtMaterialCategoryRepository;

    // private ThreadPoolTaskExecutor poolExecutor = executorConfig.asyncServiceExecutor();

    /**
     * 得到物料集合对象
     * 
     * @Author peng.yuan
     * @Date 2019/11/8 16:55
     * @param tenantId :
     * @param idList :
     * @return java.util.concurrent.Future<java.util.List<tarzan.material.domain.vo.MtMaterialVO>>
     */
    @Override
    public Future<List<MtMaterialVO>> getMaterialFuture(ThreadPoolTaskExecutor poolExecutor, Long tenantId,
                                                        List<String> idList) {
        return poolExecutor.submit(new Callable<List<MtMaterialVO>>() {

            @Override
            public List<MtMaterialVO> call() throws Exception {
                return mtMaterialRepository.materialBasicInfoBatchGet(tenantId, idList);
            }
        });
    }

    /**
     * 得到物料集合
     * 
     * @Author peng.yuan
     * @Date 2019/11/8 17:00
     * @param tenantId :
     * @param idList :
     * @return java.util.concurrent.Future<java.util.List<tarzan.modeling.domain.entity.MtModSite>>
     */
    @Override
    public Future<List<MtModSite>> getModSiteFuture(ThreadPoolTaskExecutor poolExecutor, Long tenantId,
                                                    List<String> idList) {
        return poolExecutor.submit(new Callable<List<MtModSite>>() {

            @Override
            public List<MtModSite> call() throws Exception {
                return mtModSiteRepository.siteBasicPropertyBatchGet(tenantId, idList);
            }
        });
    }

    /**
     * 得到生产线集合
     * 
     * @Author peng.yuan
     * @Date 2019/11/8 17:00
     * @param tenantId :
     * @param idList :
     * @return java.util.concurrent.Future<java.util.List<tarzan.modeling.domain.entity.MtModProductionLine>>
     */
    @Override
    public Future<List<MtModProductionLine>> getModProductionLineFuture(ThreadPoolTaskExecutor poolExecutor,
                                                                        Long tenantId, List<String> idList) {
        return poolExecutor.submit(new Callable<List<MtModProductionLine>>() {

            @Override
            public List<MtModProductionLine> call() throws Exception {
                return mtModProductionLineRepository.prodLineBasicPropertyBatchGet(tenantId, idList);
            }
        });
    }

    /**
     * 得到库位集合
     * 
     * @Author peng.yuan
     * @Date 2019/11/8 17:00
     * @param tenantId :
     * @param idList :
     * @return java.util.concurrent.Future<java.util.List<tarzan.modeling.domain.entity.MtModLocator>>
     */
    @Override
    public Future<List<MtModLocator>> getModLocatorFuture(ThreadPoolTaskExecutor poolExecutor, Long tenantId,
                                                          List<String> idList) {
        return poolExecutor.submit(new Callable<List<MtModLocator>>() {

            @Override
            public List<MtModLocator> call() throws Exception {
                return mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, idList);
            }
        });
    }

    /**
     * 得到单位集合
     * 
     * @Author peng.yuan
     * @Date 2019/11/8 17:01
     * @param tenantId :
     * @param idList :
     * @return java.util.concurrent.Future<java.util.List<tarzan.material.domain.vo.MtUomVO>>
     */
    @Override
    public Future<List<MtUomVO>> getUomFuture(ThreadPoolTaskExecutor poolExecutor, Long tenantId, List<String> idList) {
        return poolExecutor.submit(new Callable<List<MtUomVO>>() {

            @Override
            public List<MtUomVO> call() throws Exception {
                return mtUomRepository.uomPropertyBatchGet(tenantId, idList);
            }
        });
    }

    @Override
    public Future<List<MtModArea>> getModAreaFuture(ThreadPoolTaskExecutor poolExecutor, Long tenantId,
                                                    List<String> areaIds) {
        return poolExecutor.submit(new Callable<List<MtModArea>>() {
            @Override
            public List<MtModArea> call() throws Exception {
                return mtModAreaRepository.areaBasicPropertyBatchGet(tenantId, areaIds);
            }
        });
    }

    @Override
    public Future<List<MtModWorkcell>> getModWorkcellFuture(ThreadPoolTaskExecutor poolExecutor, Long tenantId,
                                                            List<String> workcellIds) {
        return poolExecutor.submit(new Callable<List<MtModWorkcell>>() {
            @Override
            public List<MtModWorkcell> call() throws Exception {
                return mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, workcellIds);
            }
        });
    }

    @Override
    public Future<List<MtBomVO7>> getBomFuture(ThreadPoolTaskExecutor poolExecutor, Long tenantId,
                                               List<String> bomIds) {
        return poolExecutor.submit(new Callable<List<MtBomVO7>>() {
            @Override
            public List<MtBomVO7> call() throws Exception {
                return mtBomRepository.bomBasicBatchGet(tenantId, bomIds);
            }
        });
    }

    @Override
    public Future<List<MtRouter>> getRouterFuture(ThreadPoolTaskExecutor poolExecutor, Long tenantId,
                                                  List<String> routerIds) {
        return poolExecutor.submit(new Callable<List<MtRouter>>() {
            @Override
            public List<MtRouter> call() throws Exception {
                return mtRouterRepository.routerBatchGet(tenantId, routerIds);
            }
        });
    }

    @Override
    public Future<List<MtMaterialCategory>> getMaterialCategoryFuture(ThreadPoolTaskExecutor poolExecutor,
                                                                      Long tenantId, List<String> materialCategoryIds) {
        return poolExecutor.submit(new Callable<List<MtMaterialCategory>>() {
            @Override
            public List<MtMaterialCategory> call() throws Exception {
                return mtMaterialCategoryRepository.materialCategoryPropertyBatchGet(tenantId, materialCategoryIds);
            }
        });
    }
}
