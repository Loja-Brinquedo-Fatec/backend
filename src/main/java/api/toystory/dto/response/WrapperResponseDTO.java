package api.toystory.dto.response;

public class WrapperResponseDTO<T> {

	private Boolean status;
	private String message;
	private T data;
	
	public WrapperResponseDTO() {

	}
	
	public WrapperResponseDTO(Boolean status, String message, T data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}
