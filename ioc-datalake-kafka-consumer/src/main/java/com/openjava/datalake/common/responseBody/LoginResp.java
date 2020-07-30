package com.openjava.datalake.common.responseBody;

import io.swagger.annotations.ApiModel;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.plugin.sys.vo.UserVO;

import java.io.Serializable;

@ApiModel("登录结果")
public class LoginResp extends BasicApiResponse implements Serializable {

	private UserVO user;

	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
	}
}
