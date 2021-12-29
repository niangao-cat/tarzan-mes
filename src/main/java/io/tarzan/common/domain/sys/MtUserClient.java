package io.tarzan.common.domain.sys;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.common.HZeroService;
import org.hzero.core.redis.RedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.infra.feign.MtRemoteIamService;

/**
 * @author : MrZ
 * @date : 2019-12-31 16:32
 **/
@Component
public class MtUserClient {
    private static final String REDIS_USER_INFO = "hiam:user";
    /**
     * 微服务接口获取最大用户数
     */
    private static final Integer MAX_USER_SIZE = 400;

    /**
     * 微服务接口获取首个数据
     */
    private static final Integer MIN_USER_PAGE = 0;

    /**
     * 微服务接口获取最大用户数
     */
    private static final Integer USER_BATCH = 5;

    @Autowired
    private RedisHelper redisHelper;
    @Autowired
    private MtRemoteIamService remoteIamService;

    /**
     * 获取当前用户ID，如果获取不到则赋值默认-2，区别于数据库默认-1
     *
     * @author chuang.yang
     * @date 2020/4/13
     * @param
     */
    public static Long getCurrentUserId() {
        Long userId = -2L;
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }

        return userId;
    }

    /**
     * 获取当前用户当前角色ID，如果获取不到则赋值默认-2
     *
     * @author chuang.yang
     * @date 2020/4/13
     * @param
     */
    public static Long getCurrentRoleId() {
        Long roleId = -2L;
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getRoleId() != null) {
            roleId = DetailsHelper.getUserDetails().getRoleId();
        }

        return roleId;
    }

    public MtUserInfo userInfoGet(Long tenantId, Long userId) {
        MtUserInfo result = new MtUserInfo();
        if (null == userId || null == tenantId) {
            return result;
        }
        // 先从HZERO平台的redis中获取用户信息
        try {
            this.redisHelper.setCurrentDatabase(HZeroService.Iam.REDIS_DB);
            String userStr = this.redisHelper.hshGet(REDIS_USER_INFO, userId.toString());
            if (StringUtils.isNotEmpty(userStr)) {
                result = redisHelper.fromJson(userStr, MtUserInfo.class);
            }
        } finally {
            this.redisHelper.clearCurrentDatabase();
        }
        // 如果没有，进行微服务调用获取用户信息
        if (null == result) {
            ResponseEntity<MtUserInfo> userRes = remoteIamService.userInfoRemoteGet(tenantId, userId);
            if (userRes != null && userRes.getBody() != null) {
                result = userRes.getBody();
            }
        }
        return result;
    }

    public Map<Long, MtUserInfo> userInfoBatchGet(Long tenantId, List<Long> userIds) {
        List<MtUserInfo> result = new ArrayList<>();
        if (null == tenantId || CollectionUtils.isEmpty(userIds)) {
            return new HashMap<>();
        }
        List<Long> disUserIds = new ArrayList<>(userIds).stream().distinct().collect(Collectors.toList());
        List<String> userIdsStr =
                disUserIds.stream().filter(Objects::nonNull).map(Object::toString).collect(Collectors.toList());
        // 先从HZERO平台的redis中获取用户信息
        try {
            this.redisHelper.setCurrentDatabase(HZeroService.Iam.REDIS_DB);
            List<String> userStr = this.redisHelper.hshMultiGet(REDIS_USER_INFO, userIdsStr);
            if (CollectionUtils.isNotEmpty(userStr)) {
                for (String ever : userStr) {
                    MtUserInfo one = redisHelper.fromJson(ever, MtUserInfo.class);
                    if (null != one) {
                        // 这里将找到的用户信息从传入参数中移除，方便进行后面进行未找到的用户的单独查询
                        disUserIds.remove(one.getId());
                        result.add(one);
                    }
                }
            }
        } finally {
            this.redisHelper.clearCurrentDatabase();
        }
        // 如果存在没有找到用户的用户ID，进行微服务调用获取
        if (CollectionUtils.isNotEmpty(disUserIds)) {
            if (disUserIds.size() <= USER_BATCH) {
                // 如果数据量小于等于5，直接循环调用微服务即可
                for (Long userId : disUserIds) {
                    ResponseEntity<MtUserInfo> userRes = remoteIamService.userInfoRemoteGet(tenantId, userId);
                    if (userRes != null && userRes.getBody() != null) {
                        result.add(userRes.getBody());
                    }
                }
            } else {
                // 如果数据量大于5，分批调用所有用户，直到全部查完
                ResponseEntity<Page<MtUserInfo>> userPageRes =
                        remoteIamService.userAllInfoRemoteGet(tenantId, MIN_USER_PAGE, MAX_USER_SIZE);
                if (userPageRes != null && userPageRes.getBody() != null) {
                    if (CollectionUtils.isNotEmpty(userPageRes.getBody().getContent())) {
                        List<MtUserInfo> containUsers = userPageRes.getBody().getContent().stream()
                                .filter(t -> disUserIds.contains(t.getId())).collect(Collectors.toList());
                        // 将结果放到结果集中
                        result.addAll(containUsers);
                        // 将筛选到数据的用户ID移除
                        disUserIds.removeAll(containUsers.stream().map(MtUserInfo::getId).collect(Collectors.toList()));
                    }

                    // 如果还有数据没有获取到用户信息
                    for (int i = 1; i < userPageRes.getBody().getTotalPages(); ++i) {
                        if (CollectionUtils.isEmpty(disUserIds)) {
                            break;
                        }
                        ResponseEntity<Page<MtUserInfo>> userPageResOne =
                                remoteIamService.userAllInfoRemoteGet(tenantId, i, MAX_USER_SIZE);
                        if (userPageResOne != null && userPageResOne.getBody() != null) {
                            if (CollectionUtils.isNotEmpty(userPageResOne.getBody().getContent())) {
                                List<MtUserInfo> containUsers = userPageResOne.getBody().getContent().stream()
                                        .filter(t -> disUserIds.contains(t.getId()))
                                        .collect(Collectors.toList());
                                // 将结果放到结果集中
                                result.addAll(containUsers);
                                // 将筛选到数据的用户ID移除
                                disUserIds.removeAll(containUsers.stream().map(MtUserInfo::getId)
                                        .collect(Collectors.toList()));
                            }
                        }

                    }

                }
            }
        }
        return result.stream().collect(Collectors.toMap(MtUserInfo::getId, t -> t));
    }

    public Map<Long, MtUserInfo> userInfoAllGet(Long tenantId) {
        List<MtUserInfo> result = new ArrayList<>();
        // 先从HZERO平台的redis中获取用户信息
        try {
            this.redisHelper.setCurrentDatabase(HZeroService.Iam.REDIS_DB);
            Map<String, String> userStrMap = this.redisHelper.hshGetAll(REDIS_USER_INFO);
            if (CollectionUtils.isNotEmpty(userStrMap.values())) {
                for (String ever : userStrMap.values()) {
                    MtUserInfo one = redisHelper.fromJson(ever, MtUserInfo.class);
                    if (null != one) {
                        // 这里将找到的用户信息从传入参数中移除，方便进行后面进行未找到的用户的单独查询
                        result.add(one);
                    }
                }
            }
        } finally {
            this.redisHelper.clearCurrentDatabase();
        }


        // 如果没有找到数据，调用微服务
        if (CollectionUtils.isEmpty(result)) {
            ResponseEntity<Page<MtUserInfo>> userPageRes =
                    remoteIamService.userAllInfoRemoteGet(tenantId, MIN_USER_PAGE, MAX_USER_SIZE);
            if (userPageRes != null && userPageRes.getBody() != null) {
                if (CollectionUtils.isNotEmpty(userPageRes.getBody().getContent())) {
                    // 将结果放到结果集中
                    result.addAll(userPageRes.getBody().getContent());
                }

                // 如果还有数据没有获取到用户信息
                for (int i = 1; i < userPageRes.getBody().getTotalPages(); ++i) {
                    ResponseEntity<Page<MtUserInfo>> userPageResOne =
                            remoteIamService.userAllInfoRemoteGet(tenantId, i, MAX_USER_SIZE);
                    if (userPageResOne != null && userPageResOne.getBody() != null) {
                        if (CollectionUtils.isNotEmpty(userPageResOne.getBody().getContent())) {
                            // 将结果放到结果集中
                            result.addAll(userPageResOne.getBody().getContent());
                        }
                    }
                }
            }
        }

        return result.stream().collect(Collectors.toMap(MtUserInfo::getId, t -> t));
    }

}
