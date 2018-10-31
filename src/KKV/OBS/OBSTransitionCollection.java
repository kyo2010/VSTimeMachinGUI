package KKV.OBS;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 场景转换
 * @author Star
 *
 */
public class OBSTransitionCollection {

	public static class Transition{
		private String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	@JSONField(name="message-id")
	private String  message_id;
	@JSONField(name="current-transition")
	private String  current_transition;
	private String status;
	private List<Transition>transitions;
	public String getMessage_id() {
		return message_id;
	}
	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}
	public String getCurrent_transition() {
		return current_transition;
	}
	public void setCurrent_transition(String current_transition) {
		this.current_transition = current_transition;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<Transition> getTransitions() {
		return transitions;
	}
	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}
}
