package com.weirwei.cansee.controller;


import com.fehead.lang.controller.BaseController;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.fehead.lang.response.CommonReturnType;
import com.fehead.lang.response.FeheadResponse;
import com.weirwei.cansee.controller.vo.OrgVO;
import com.weirwei.cansee.service.IOrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 组织 前端控制器
 * </p>
 *
 * @author weirwei
 * @since 2021-03-15
 */
@RestController
@RequestMapping("/cansee/organization")
@Slf4j
public class OrganizationController extends BaseController {

    @Resource
    HttpServletRequest req;
    @Resource
    HttpServletResponse rsp;

    @Resource
    IOrganizationService organizationService;

    @PostMapping("/org")
    public FeheadResponse createOrg(@RequestParam("orgName") String orgName) throws BusinessException {
        String uid = (String) req.getAttribute("uid");
        log.info("rui:" + req.getRequestURI() +
                ",param:" +
                "uid=" + uid +
                "&org_name=" + orgName
        );
        if (StringUtils.isEmpty(orgName)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "组织名不能为空");
        }

        OrgVO orgVO = organizationService.createOrg(uid, orgName);

        return CommonReturnType.create(orgVO);
    }

    @GetMapping("/org")
    public FeheadResponse getOrg(@PageableDefault(size = 6, page = 1) Pageable pageable) {
        String uid = (String) req.getAttribute("uid");
        log.info("rui:" + req.getRequestURI() +
                ",param:" +
                "uid=" + uid +
                "&pageable=" + pageable
        );

        return CommonReturnType.create(organizationService.getList(pageable, uid));
    }

    /**
     * todo
     * 删除组织
     *
     * @param orgId orgId
     * @return FeheadResponse
     */
    @DeleteMapping("/org")
    public FeheadResponse delOrg(@RequestParam("orgId") String orgId) {
        String uid = (String) req.getAttribute("uid");
        log.info("rui:" + req.getRequestURI() +
                ",param:" +
                "uid=" + uid +
                "&orgId=" + orgId
        );
        return CommonReturnType.create(null);
    }
}

