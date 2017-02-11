package com.wxjfkg.activiti.explorer.web;

import javax.servlet.http.HttpSession;

import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wxjfkg.activiti.explorer.service.UserService;
import com.wxjfkg.activiti.explorer.utils.UserUtil;

/**
 * 首页控制器
 *
 * @author guorui
 */
@Controller
@RequestMapping("/main")
public class MainController {

	@Autowired
    private UserService userService;
	
    @RequestMapping(value = { "index", "" })
    public String index(String username, HttpSession session) {
		if (UserUtil.getUserFromSession(session) == null) {
			if(StringUtils.isNotBlank(username)) {
				User user = userService.findUserByName(username);
		        boolean userExist = (user != null )?true:false;
				if (userExist) {
					UserUtil.saveUserToSession(session, user);
					return "/main/index";
				} else {
					return "/error/403";
				}
			} else {
				return "/error/403";
			}
		}
        return "/main/index";
    }

    @RequestMapping(value = "/welcome")
    public String welcome() {
        return "/main/welcome";
    }

}
