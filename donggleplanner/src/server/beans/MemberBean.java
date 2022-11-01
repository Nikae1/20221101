package server.beans;

/* MemberBean안에 있는 값들의 get, set값을 저장 */
public class MemberBean {

	private String accessCode;
	private String secretCode;
	private String userName;
	private String phoneNumber;
	private int activation;
	private int fileIdx;
	
	
	public String getSecretCode() {
		return secretCode;
	}


	public void setSecretCode(String secretCode) {
		this.secretCode = secretCode;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public int getActivation() {
		return activation;
	}


	public void setActivation(int activation) {
		this.activation = activation;
	}


	public String getAccessCode() {
		return accessCode;
	}


	public void setAccessCode(String accessCode) {
		this.accessCode=accessCode;
	}


	public int getFileIdx() {
		return fileIdx;
	}


	public void setFileIdx(int fileIdx) {
		this.fileIdx = fileIdx;
	}


	/* setAccessCode에 저장하는 
	 * 저장될게 5개이므로 5개가 만들어짐
	 * */
//	public void setAccessCode(String accessCode) {
//		this.accessCode = accessCode;
//	}
	
	/* getAccessCode 가지고 오기*/
	
}
