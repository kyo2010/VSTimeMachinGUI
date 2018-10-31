package KKV.OBS;

import com.alibaba.fastjson.annotation.JSONField;

public class OBSStatus {
	@JSONField(name="message-id")
	private String message_id;
	@JSONField(name="preview-only")
	private Boolean preview_only;
	private Boolean recording;
	private String status;
	private Boolean streaming;
	public String getMessage_id() {
		return message_id;
	}
	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}
	public Boolean getPreview_only() {
		return preview_only;
	}
	public void setPreview_only(Boolean preview_only) {
		this.preview_only = preview_only;
	}
	public Boolean getRecording() {
		return recording;
	}
	public void setRecording(Boolean recording) {
		this.recording = recording;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Boolean getStreaming() {
		return streaming;
	}
	public void setStreaming(Boolean streaming) {
		this.streaming = streaming;
	}
}
