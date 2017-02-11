package com.wxjfkg.activiti.explorer.web.identity;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wxjfkg.activiti.explorer.service.UserService;
import com.wxjfkg.activiti.explorer.utils.UserUtil;
import com.wxjfkg.activiti.explorer.web.base.AbstractController;

/**
 * 用户相关控制器
 *
 * @author guorui
 */
@Controller
@RequestMapping("/user")
public class UseController extends AbstractController {

    private static Logger logger = LoggerFactory.getLogger(UseController.class);

    // Activiti Identify Service
    private IdentityService identityService = processEngine.getIdentityService();
    
    @Autowired
    private UserService userService;

    /**
     * 登录系统
     *
     * @param userName
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "/logon")
    public String logon(@RequestParam("username") String userName, @RequestParam("password") String password, HttpSession session) {
        logger.debug("logon request: {username={}, password={}}", userName, password);
//      boolean userExist = identityService.checkPassword(userName, password);
        User user = userService.findUserByName(userName);
        boolean userExist = (user != null )?true:false;
        if (userExist) {
            // 查看用户是否存在
//            User user = identityService.createUserQuery().userId(userName).singleResult();
            UserUtil.saveUserToSession(session, user);

	      /*
	       * 读取角色
	       */
            List<Group> groupList = identityService.createGroupQuery().groupMember(user.getId()).list();
            session.setAttribute("groups", groupList);

            String[] groupNames = new String[groupList.size()];
            for (int i = 0; i < groupNames.length; i++) {
                groupNames[i] = groupList.get(i).getName();
            }
            session.setAttribute("groupNames", ArrayUtils.toString(groupNames));

            return "redirect:/main/index";
        } else {
            return "redirect:/login.jsp?error=true";
        }
    }

    /**
     * 退出登录
     */
    @RequestMapping(value = "/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "/login";
    }

}
