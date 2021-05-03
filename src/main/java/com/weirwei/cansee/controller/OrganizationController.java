package com.weirwei.cansee.controller;


import com.fehead.lang.response.CommonReturnType;
import com.fehead.lang.response.FeheadResponse;
import com.weirwei.cansee.controller.vo.OrgListVO;
import com.weirwei.cansee.service.IOrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import com.fehead.lang.controller.BaseController;

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
    public FeheadResponse createOrg(@RequestParam("org_name") String orgName) {
        String uid = (String) req.getAttribute("uid");
        log.info("rui:" + req.getRequestURI() +
                "param:" +
                "uid=" + uid +
                "org_name=" + orgName
        );
        String orgId = organizationService.createOrg(uid, orgName);

        return CommonReturnType.create(orgId);
    }
    @GetMapping("/org")
    public FeheadResponse getOrg(@PageableDefault(size = 6,page = 1) Pageable pageable) {
        String uid = (String) req.getAttribute("uid");
        log.info("rui:" + req.getRequestURI() +
                "param:" +
                "uid=" + uid +
                "pageable=" + pageable
        );

        return CommonReturnType.create(organizationService.getList(pageable, uid));
    }
}

