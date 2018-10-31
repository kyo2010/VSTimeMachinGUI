package KKV.OBS;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 场景集合类
 * @author Star
 *
 */
public class OBSSceneCollection {

	@JSONField(name="current-scene")
	private String current_scene;
	@JSONField(name="message-id")
	private String message_id;
	private List<OBSScene> scenes;
	private String status;
	public String getCurrent_scene() {
		return current_scene;
	}
	public void setCurrent_scene(String current_scene) {
		this.current_scene = current_scene;
	}
	public String getMessage_id() {
		return message_id;
	}
	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}
	public List<OBSScene> getScenes() {
		return scenes;
	}
	public void setScenes(List<OBSScene> scenes) {
		this.scenes = scenes;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
