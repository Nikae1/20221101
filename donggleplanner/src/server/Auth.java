package server;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


import server.beans.AccessHistoryBean;
import server.beans.MemberBean;

/*로그인, 로그아웃,접속로그기록을 담당하는*/
public class Auth {
	
	public Auth() {
		
	}
	
	
	/* Job : 로그인
	 * 
	 * 1.parameter를 통해 받는 정보 : id, password
	 * 2.id가 DB에 존재하는지 여부 Check
	 *  ----> DAO가 MEMBERS의 전체 레코드를 전달 --> 비교(accessCtl에서 실행)
	 * 	  2-1. true --> 3번으로 이동
	 *    2-2. false --> client
	 * 3. id와 password를 DB와 비교
	 *    3-1. true --> 4번으로 이동
	 *    3-2. false --> client
	 * 4. 접속기록(로그기록) 생성하기(AccessHistory)
	 * 5. client에게 로그인 결과 전달
	 * * */
	/* 정보를 DB와 비교*/
	public boolean accessCtl(String clientData) {
		/* serviceCode=1&id=hoonzzang&password=1234 --> 
		 * split("&") --> {"serviceCode=1", "id=hoonzzang", "password=1234"}
		 * [1].split("=") --> {"id", "hoonzzang"}[1] --> MemberBean.setAccessCode
		 * [2].split("=") --> {"password", "1234"}[1] --> MemberBean.setSecretCode
		 * */
		
		
		/* setMemberBean의 member의 life-cycle이 끝나기 전에 
		 * accessCtl로 참조선을 변경시켜 계속 사용하도록 한다.
		 * */
		MemberBean member = (MemberBean)this.setBean(clientData);
		DataAccessObject dao = new DataAccessObject();
		
		ArrayList<MemberBean> memberList = dao.readDatabase(0);
		AccessHistoryBean historyBean;
		boolean accessResult = false;
		
		/* 접속기록을 남기기 위한 */
		if(this.compareAccessCode(member.getAccessCode(), memberList)) {
			if(this.isAuth(member, memberList)) {
				/* (fileIdx=1) accessCode, (date=> yyyyMMddHHmmss), (accessType=1) */
				historyBean = new AccessHistoryBean();
				historyBean.setFileIdx(1);
				historyBean.setAccessCode(member.getAccessCode());
				historyBean.setAccessDate(this.getDate(false));
				historyBean.setAccessType(1);
				
				accessResult = dao.writeAccessHistory(historyBean);
			}
			
		}
		
		return accessResult;
		
	
	}
	/* 로그인 후 로그아웃을 위한 메소드 */
	public void accessOut(String clientData) {
		DataAccessObject dao = new DataAccessObject();
		AccessHistoryBean history = (AccessHistoryBean)this.setBean(clientData);
		history.setFileIdx(1);
		history.setAccessDate(this.getDate(false));
		history.setAccessType(-1);
		
		dao.writeAccessHistory(history);
	}
	
	
	
	
	
	/* 로그기록을 위한 날짜 생성 */
	private String getDate(boolean isDate) {
		String pattern = (isDate)? "yyyyMMdd" : "yyyyMMddHHmmss";
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
	}
	
	
	
	
	/* MemberBean에 접근하기 위해 member라는 참조변수를 이용하여
	 * Userapp에서 받은 clientData를 AccessCode와 SecretCode로
	 * split하여 참조변수 member로 저장
	 *  */
	private Object setBean(String clientData) {
		/*object값을 사용한 이유는 어느정도의 값이 들어올지 몰라
		 * 가장 커다란 값인 object를 이용하여 사용
		 * 
		 * */ 	
		Object object = null;
		/* splitData라는 배열을 선언하여 하나로 저장해 준다 */
		String[] splitData = clientData.split("&");
	switch(splitData[0].split("=")[1]) {
	case"-1":
	object = new AccessHistoryBean();
	((AccessHistoryBean)object).setAccessCode(splitData[1].split("=")[1]);
	break;
case "1":
	object = new MemberBean();
	((MemberBean)object).setAccessCode(splitData[1].split("=")[1]);
	((MemberBean)object).setSecretCode(splitData[2].split("=")[1]);

	break;

	}
		return object;
	
	}
	
	
	
	/* 아이디의 존재여부 판단(객체지향)*/
	private boolean compareAccessCode(String code, ArrayList<MemberBean> memberList) {
		boolean result = false;
		/* 향상된 for문을 이용하여 ( : ) = memberList에 있는 MemberBean을 하나씩
		 * MemberBean member로 대입하여 반복문을 실행  */
		for(MemberBean member : memberList) {
			if(code.equals(member.getAccessCode())) {
				result = true;
				break;
			}
		}
		
		
		return result;
	}
	
	/* 아이디와 비밀번호 비교 */
	private boolean isAuth(MemberBean member, ArrayList<MemberBean> memberList) {
		boolean result = false;
		/* 향상된 for( : )문을 활용하여 */
		for(MemberBean memberInfo : memberList) {
			if(member.getAccessCode().equals(memberInfo.getAccessCode())) {
				if(member.getSecretCode().equals(memberInfo.getSecretCode())) {
					result = true;
					break;
				}
			}
		}
		
		
		
		return result;
	}
	
}
