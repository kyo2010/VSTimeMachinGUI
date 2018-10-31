package KKV.OBS;


import com.alibaba.fastjson.JSONObject;

public interface Function {
	
	/**
	 * 存在错误信息的
	 * @param error
	 * @param data
	 */
	public void call(JSONObject data);
}
