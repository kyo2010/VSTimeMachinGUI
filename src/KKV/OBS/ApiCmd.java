package KKV.OBS;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import org.eclipse.jetty.websocket.api.Session;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.jetty.websocket.api.Session;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;

/**
 * 命令行类
 * @author Administrator
 */
public class ApiCmd {
	
	private static int _generateMessageId = 0;
	private static Map<String,Function> _responseCallbacks = Maps.newConcurrentMap();
	/**
	 * 删除callback
	 * @param messageid
	 * @return
	 */
	public static Function remove(String messageid) {
		return _responseCallbacks.remove(messageid);
	}

	//获取callback
	public static Function get(String messageid) {
		return _responseCallbacks.get(messageid);
	}
	
	private Session session;
	
	public ApiCmd(Session session)throws Exception{
		if(null!=session && session.isOpen()){
			this.session = session;
		}else{
			throw new Exception("Session is down");
		}
	}
	
	 //字符串 SHA 加密
	private String SHA256(final String strText) {
		// 是否是有效字符串
		if (strText != null && strText.length() > 0) {
			// SHA 加密开始
			try {
				// 创建加密对象 并傳入加密類型
				MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
				// 传入要加密的字符串
				messageDigest.update(strText.getBytes());
				// 得到 byte 類型结果
				byte byteBuffer[] = messageDigest.digest();
				return Base64.encodeBase64String(byteBuffer);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * 检查验证密码
	 * @param password
	 */
	public void CheckAuthenticate(final String password,final AdapterFunction af){
		GetAuthRequired(new Function() {
			@Override
			public void call(JSONObject json) {
				if (null != json) {
					if (json.getBooleanValue("authRequired")) {
						String salt = json.getString("salt");
						String challenge = json.getString("challenge");
						System.out.println("salt → " + salt + ", challenge →" + challenge);
					  String utf8AuthHash =SHA256(password+ salt);  
						System.out.println("utf8AuthHash → " + utf8AuthHash );
						String authRespB64 =SHA256(utf8AuthHash +challenge);
						System.out.println("authRespB64 → " + authRespB64 );
						Authenticate(authRespB64,af);
					}
				}
			}
		});
	}
	
	/**
	 * 发送obs接口命令
	 * @param requestType
	 * @param args
	 * @param callback
	 */
	private void send(String requestType,Map<String,Object> args,Function callback){
		try {
			if(null == args){
				 args = Maps.newHashMap();
			}
			if(null==callback){
				callback = new AdapterFunction();
			}
			_generateMessageId = _generateMessageId +1;
			 args.put("message-id","msg_"+_generateMessageId);
			 args.put("request-type",requestType);
			 _responseCallbacks.put("msg_"+_generateMessageId, callback);
			 if(session.isOpen()){
				 //SerializerFeature.BrowserCompatible 中文字符uncode 表示
				 String response = JSON.toJSONString(args,SerializerFeature.BrowserCompatible);
				 System.out.println("RESPONSE → "+response);
				 session.getRemote().sendString(response);
			 }
		} catch (Throwable t) {
			System.err.println(t.getMessage());
		}
	}
	
	/**获取服务端版本**/
	public void GetVersion(Function callback){
			send("GetVersion", null, callback);
	}
	
	/**服务端验证**/
	public void Authenticate(String auth,Function callback){
		Map<String,Object> args = Maps.newHashMap();
		args.put("auth", auth);
		send("Authenticate", args, callback);
	}
	
	/**检查是否需要密码 （我没有加什么密码这个方法以后再说）**/
	public  void GetAuthRequired(Function callback){
		send("GetAuthRequired", null, callback);
	}
	/**获取当前场景**/
	public  void GetCurrentScene(Function callback){
			send("GetCurrentScene", null, callback);
	}
	/**设置当前场景**/
	public  void SetCurrentScene(String sceneName ,Function callback){
			Map<String,Object> args = Maps.newHashMap();
			args.put("scene-name", sceneName);
			send("SetCurrentScene", args, callback);
	}
	/**
	 * @param err {object} - Only populated if an error occurred during the request.
	 * @param data {object}
	 * @param data.currentScene {string} - Name of the currently active scene.
	 * @param data.scenes {Array.<OBSScene>} - Array of {@link OBSScene}s.
	 * */
	public  void  GetSceneList(Function callback){
			send("GetSceneList", null, callback);
	}
	
	/**
	  * @param sourceName {string} - Name of the source.
	  * @param visible {bool} - Indicates whether the source should be visible or not.
	 * @param api
	 * @param source
	 * @param render
	 * @param callback
	 */
	public  void SetSourceRender(String source,boolean render,Function callback){
			Map<String,Object> args = Maps.newHashMap();
			args.put("source", source);
			args.put("render", render);
			send("SetSourceRender", args, callback);
	}
	
	/**设置场景源位置大小**/
	public void SetSceneItemPositionAndSize(){
		
	}
	
	public  void StartStopStreaming(){
			send("StartStopStreaming", null, null);
	}
	public  void StartStopRecording(){
			send("StartStopRecording", null, null);
	}
	/**
	 * @param err {object} - Only populated if an error occurred during the request.
	 * @param data {object}
	 * @param data.streaming {bool} - Indicates whether OBS is currently streaming.
	 * @param data.recording {bool} - Indicates whether OBS is currently recording.
	 * @param data.previewOnly {bool} - Always false.
	 * @param data.bytesPerSec {int=} - Current bitrate of the stream.
	 * @param data.strain {double=} - Percentage of dropped frames.
	 * @param data.totalStreamTime {int=} - Total uptime of the stream.
	 * @param data.numTotalFrames {int=} - Total number of frames since the start of stream.
	 * @param data.numDroppedFrames {int=} - Total number of dropped frames since the start of stream.
	 * @param data.fps {double=} - Current Frames per Second of the stream.
	 * @param api
	 * @param callback
	 */
	public  void GetStreamingStatus(Function callback){
			send("GetStreamingStatus", null, callback);
	}
	/** @param err {object} - Only populated if an error occurred during the request.
	 * @param data {object}
	 * @param data.currentTransition {string} - Name of the currently active transition.
	 * @param data.transitions {Array.<string>} - Array of available transitions by name.
	 * */
	public  void GetTransitionList(Function callback){
			send("GetTransitionList", null, callback);
	}
	
	/*
	 * 获得当前场景过度
	 * @param err {object} - Only populated if an error occurred during the request.
	 * @param data {object}
	 * @param data.name {string} - Name of the currently active transition.
	 **/
	public  void GetCurrentTransition(Function callback){
			send("GetCurrentTransition", null, callback);
	}
	/**设置当前场景过度**/
	public  void SetCurrentTransition(String transitionName){
			Map<String,Object> args = Maps.newHashMap();
			args.put("transition-name", transitionName);
			send("SetCurrentTransition", args, null);
	}
	
	/**设置音量值 volume 在0.0~1.0 之间**/
	public void SetVolume(String name,double volume){
		Map<String,Object> args = Maps.newHashMap();
		args.put("source", name);
		args.put("volume", volume);
		send("SetVolume", args, null);
	}
	
	/**获取某个源的音量值**/
	public void GetVolume(String name,Function callback){
		Map<String,Object> args = Maps.newHashMap();
		args.put("source", name);
		send("GetVolume", args, callback);
	}
	
	/**静音非静音交换**/
	public void ToggleMute(String source,Function callback){
		Map<String,Object> args = Maps.newHashMap();
		args.put("source", source);
		send("ToggleMute", args, callback);
	}
	
	/**设置静音**/
	public void SetMute(String source,boolean is,Function callback){
		Map<String,Object> args = Maps.newHashMap();
		args.put("source", source);
		args.put("mute", is);
		send("SetMute", args, callback);
	}
	
	/**移动选项位置**/
	public void SetSceneItemPosition(String item,double x,double y,Function callback){
		Map<String,Object> args = Maps.newHashMap();
		args.put("item", item);
		args.put("x", x);
		args.put("y", y);
		send("SetSceneItemPosition", args, callback);
	}
	
	/**选项 xScale ,yScale 原始图片比例 rotation 旋转角度**/
	public void SetSceneItemTransform(String item,double xScale , double yScale,double rotation,Function callback){
		Map<String,Object> args = Maps.newHashMap();
		args.put("item", item);
		args.put("x-scale", xScale);
		args.put("y-scale", yScale);
		args.put("rotation", rotation);
		send("SetSceneItemTransform", args, callback);
	}
	
	/**设置当前 profilename**/
	public void SetCurrentProfile(String prefilename ,Function callback){
		Map<String,Object> args = Maps.newHashMap();
		args.put("profile-name", prefilename);
		send("SetCurrentProfile", args, callback);
	}
	
	/**获取当前 profilename**/
	public void GetCurrentProfile(Function callback){
		send("GetCurrentProfile", null, callback);
	}
	
	/**实时更新profile server ,key**/
	public void UpdateService(String server,String key ,Function callback){
		Map<String,Object> args = Maps.newHashMap();
		args.put("key", key);
		args.put("server", server);
		send("UpdateService", args, callback);
	}
	
	/**开始串流**/
	public  void StartStreaming(){
			send("StartStreaming", null, null);
	}
  
  public  void StartRecording(){
			send("StartRecording", null, null);
	}
  
  public  void StopRecording(){
			send("StopRecording", null, null);
	}
  
	
	/**停止串流**/
	public  void StopStreaming(){
			send("StopStreaming", null, null);
	}
	
	/**
	 * 设置源的setting String 类型
	 * @param source_name
	 * @param source_setting_key
	 * @param source_setting_val
	 * @param callback
	 */
	public void UpdateSourceString(String source_name,String source_setting_key,String source_setting_val,Function callback){
		Map<String,Object> args = Maps.newHashMap();
		args.put("source-name", source_name);
		args.put("source-setting-key", source_setting_key);
		args.put("source-setting-val", source_setting_val);
		send("UpdateSourceString", args, callback);
	}
	
	/**
	 * 设置源的setting Integer 类型
	 * @param source_name
	 * @param source_setting_key
	 * @param source_setting_val
	 * @param callback
	 */
	public void UpdateSourceInt(String source_name,String source_setting_key,Integer source_setting_val,Function callback){
		Map<String,Object> args = Maps.newHashMap();
		args.put("source-name", source_name);
		args.put("source-setting-key", source_setting_key);
		args.put("source-setting-val", source_setting_val);
		send("UpdateSourceInt", args, callback);
	}
	
	/**
	 * 设置源的setting Bool 类型
	 * @param source_name
	 * @param source_setting_key
	 * @param source_setting_val
	 * @param callback
	 */
	public void UpdateSourceBool(String source_name,String source_setting_key,Boolean source_setting_val,Function callback){
		Map<String,Object> args = Maps.newHashMap();
		args.put("source-name", source_name);
		args.put("source-setting-key", source_setting_key);
		args.put("source-setting-val", source_setting_val);
		send("UpdateSourceBool", args, callback);
	}
	
	/**
	 * 设置源的setting Double 类型
	 * @param source_name
	 * @param source_setting_key
	 * @param source_setting_val
	 * @param callback
	 */
	public void UpdateSourceDouble(String source_name,String source_setting_key,Double source_setting_val,Function callback){
		Map<String,Object> args = Maps.newHashMap();
		args.put("source-name", source_name);
		args.put("source-setting-key", source_setting_key);
		args.put("source-setting-val", source_setting_val);
		send("UpdateSourceDouble", args, callback);
	}
}
