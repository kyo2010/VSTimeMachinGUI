package KKV.OBS;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class OBSScene {

	@JSONField(name="message-id")
    private String message_id;
	private String name;
	private List<OBSSource> sources;
	private String status;
	public String getMessage_id() {
		return message_id;
	}
	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<OBSSource> getSources() {
		return sources;
	}
	public void setSources(List<OBSSource> sources) {
		this.sources = sources;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
