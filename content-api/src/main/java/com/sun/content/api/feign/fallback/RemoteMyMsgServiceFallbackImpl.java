package com.sun.content.api.feign.fallback;

import com.sun.content.api.feign.RemoteMyMsgService;
import com.sun.content.api.common.utils.R;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteMyMsgServiceFallbackImpl implements RemoteMyMsgService {

	@Setter
	private Throwable cause;

//	/**
//	 * 推送消息
//	 */
//	@Override
//	public R msgHandle(String message, String referer) {
//		log.error("feign 插入日志失败", cause);
//		return null;
//	}

	
}