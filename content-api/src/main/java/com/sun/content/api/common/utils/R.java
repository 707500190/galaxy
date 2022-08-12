

package com.sun.content.api.common.utils;

import com.sun.content.api.common.constant.CommonConstants;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author lengleng
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class R<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private int code;

	@Getter
	@Setter
	private String msg;

	@Getter
	@Setter
	private T data;

	public static <T> R<T> ok() {
		return restResult(null, CommonConstants.SUCCESS, null);
	}

	public static <T> R<T> ok(T data) {
		return restResult(data, CommonConstants.SUCCESS, null);
	}

	public static <T> R<T> ok(T data, String msg) {
		return restResult(data, CommonConstants.SUCCESS, msg);
	}

	public static <T> R<T> failed() {
		return restResult(null, CommonConstants.FAIL, null);
	}

	public static <T> R<T> failed(String msg) {
		return restResult(null, CommonConstants.FAIL, msg);
	}

	public static <T> R<T> failed(T data) {
		return restResult(data, CommonConstants.FAIL, null);
	}

	public static <T> R<T> failed(T data, String msg) {
		return restResult(data, CommonConstants.FAIL, msg);
	}

	/**
	 * 根据传入的code，msg，返回异常对象信息
	 * @param code
	 * @param msg
	 * @param <T>
	 * @return
	 */
	public static <T> R<T> fail(Integer code, String msg) {
		return restResult(null, code, msg);
	}

	private static <T> R<T> restResult(T data, int code, String msg) {
		R<T> apiResult = new R<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		return apiResult;
	}

}
