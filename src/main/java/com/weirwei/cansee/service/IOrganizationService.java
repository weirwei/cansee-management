package com.weirwei.cansee.service;

import com.fehead.lang.error.BusinessException;
import com.weirwei.cansee.controller.vo.OrgListVO;
import com.weirwei.cansee.controller.vo.OrgVO;
import com.weirwei.cansee.mapper.dao.Organization;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.util.List;

/**
 * <p>
 * 组织 服务类
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
public interface IOrganizationService extends IService<Organization> {

    OrgVO createOrg(String uid, String orgName);

    OrgListVO getList(@PageableDefault(size = 6, page = 1) Pageable pageable, String uid);

    void delOrg(String uid, String orgId) throws BusinessException;
}
