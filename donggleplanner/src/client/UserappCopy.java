package client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import server.ServerController;

public class UserappCopy {
	
	public UserappCopy() {

		frontController();
	}

	/* 클라이언트 화면 및 데이터 흐름 제어 */
	private void frontController() {
		Scanner scanner = new Scanner(System.in);
		
		String Title = this.mainTitle(this.getToday(true));
		String menu = this.makeMenu();
		String message = new String();
		String close = this.close();
		boolean isLoop = true;
		String[] accessInfo = new String[2];
		/*AccessCode와 SecretCode를 입력받기 위한 배열 */
		boolean accessResult;
		/*ServerController의 ctl에 접근하기 위한 참조변수
		 * 위에 선언하는 이유는 while문으로 인한 무한 참조변수의 생성을 막기위해서
		 * */
		TaskManagement task = null;
		ServerController ctl = new ServerController();
		
		/* 로그인 시 완료와 종료의 분기를 위한 반복문 */
		while (isLoop) {
			for (int idx = 0; idx < accessInfo.length; idx++) {
				this.display(Title);
				this.display(this.makeAccess(true, accessInfo[0]));
				accessInfo[idx] = this.userInput(scanner);
			}
			this.display(this.makeAccess(false, null));
			love();
			/*사용자의 id와 비밀번호를 가진 clientData 생성*/
			String[] itemName = {"id", "password"};
			
			/* 서버에 로그인 정보 전달 
			 * makeClientData에서 만든 배열을 파라미터를 통해서 전달
			 * */
			
			accessResult = 
				ctl.controller(this.makeClientData("1",itemName, accessInfo)).equals("1")? true:false;
						
					
			
			
			
			
			/* 서버로부터 받은 로그인 결과에 따른 화면 출력 */
			this.display(this.accessResult(accessResult));
			/*accessResult값으로 인하여 if구문에 변화가 생기므로 주의할 것*/
			
			/* 로그인 실패!!!!! */
			if (!accessResult) {
				if(this.userInput(scanner).toUpperCase().equals("N")) {
					isLoop = false;
				}else {
					/*userInput값을 받기 위한 */
					accessInfo[0] = null;
					accessInfo[1] = null; 
				}
			}else {
				/* 로그인 성공!!!!!*/
			while(isLoop) {
				accessInfo[1] = null; // 로그인이 성공하면 가지고 있던 비밀번호는 폐기
				String menuSelection = new String();
				
				this.display(Title);
				this.display(menu);
				this.display(message != null? "[Message : " + message + " ]\n": "");
				
				menuSelection = this.userInput(scanner);
				/* 0~4 범위의 값이 아닌 경우 재입력 요구 */
//				if(this.isInteger(menuSelection)) {
//					if(this.isIntegerRange(Integer.parseInt(menuSelection), 0, 4)) {
//						break;
//					}else {
//						message = "메뉴는 0~4 범위내로 선택해주세요";
//					}
//				}else {
//					message = "메뉴는 숫자로 입력해주세요";
//				}
				
				/*0.Disconnetion 선택시 프로그램 종료 */
				if(menuSelection.toUpperCase().equals("0")) {
					ctl.controller(this.makeClientData("-1",itemName, accessInfo));
					isLoop = false;
			}
				/* TaskManagement Class를 호출하는 */
				else{
				task = new TaskManagement();
				
				
				if(menuSelection.equals("1")) {
					/* serviceCode를 생성하며, TaskCalender를 불러오기 */
					/* 전,후 달 불러오기, 제대로된 값이 들어오기, 이전화면 
					 * 총 3개이므로 배열로 저장
					 * */
					String[] userInput = new String[3];
					
					while(true) {
						menuSelection = "11";
						this.display(Title);
						this.display(message!=null? "   [ " + message + " ]\n": "");
						this.display(task.taskController(Integer.parseInt(menuSelection), accessInfo[0],
								/* userInput의 값에 따라 task.taskController에 보내지는 값이 달라진다 
								 * userInput의 0번지의 값을 계속 적용시켜 하나의 구문으로 3가지의 경우를 수용하게 만든다
								 * 경우의 수는 "Q" , "P" , "N", 정상적인 달력값
								 * 
								 * */
								userInput[0]==null? 0 : userInput[0].equals("P")? -1 : 1).toString());
						
						
						
						
						
//						boolean check = false;
//						
//						while(check) {
//							this.display("\nStartDate : ");
//							userInput[0] = this.userInput(scanner);
//							int idx = Integer.parseInt(userInput[idx]);
//							
//						for(idx=0; idx<3; idx++) {
//							if(this.isBreak(userInput[0])? check=true : 
//								(this.isRestart(userInput[0])? userInput[idx++]=  : idx++)) 
//								
//								userInput[0] = userInput[idx]; 
//						}
//						
//							
//						} // while(check)
						
						
						
						/* 시작하는 날짜를 입력 */
						this.display("\nStartDate : ");
						userInput[0] = this.userInput(scanner).toUpperCase();
						if(this.isBreak(userInput[0])) break;
						if(this.isRestart(userInput[0])) continue;
						
						/* 끝나는 날짜를 입력 */
						this.display("\nEnd Date : ");
						userInput[1] = this.userInput(scanner).toUpperCase();
						if(this.isBreak(userInput[1])) break;
						
						if(this.isRestart(userInput[1])) 
							{userInput[0] = userInput[1];
							continue;}
						
						/* 화면에 보여지는 여부를 입력 */
						this.display("\nEnabel :  ");
						userInput[2] = this.userInput(scanner).toUpperCase();
						if(this.isBreak(userInput[2])) break;
						
						if(this.isRestart(userInput[2])) 
						{userInput[0] = userInput[2];
						continue;}
						
						/* TaskManagement의 12번으로 이동 */
						menuSelection = "12";
						
						
						
					}
					
				}
				
			}
				
			}
			}
			
		}
		this.display(close);
			scanner.close();
	} // controller
	
	/* TaskList의 사용자 입력값에 따른 진행여부 체크 
	 * Q 값을 입력받으면 이전화면으로 돌아가 메인메뉴가 출력되어야한다
	 * */
	private boolean isBreak(String key) {
		return key.toUpperCase().equals("Q")? true:false;
	}
	/* TaskList의 달력출력 값(P , N) 
	 * P를 입력받으면 이전 달력이 출력
	 * N을 입력받으면 다음 달력이 출력
	 * */
	private boolean isRestart(String key) {
		return key.toUpperCase().equals("P") || 
				key.toUpperCase().equals("N")? true : false;
	}
	
	/* 정수 변환여부 체크 */
	private boolean isInteger(String value) {
		boolean isResult = true;
		try {
			Integer.parseInt(value);
		}catch(Exception e){
			isResult = false;// e.printStackTrace();
		}
		return isResult;
	}
	
	/* 정수의 범의 체크 */
	private boolean isIntegerRange(int value, int starting, int last) {
		return (value >= starting && value <= last)? true: false;
	}
	
	
	
	/* makeClientData 
	 * serviceCode=1&id=*****&password=****으로 저장하기위한 
	 * */
	private String makeClientData(String serviceCode ,String[] item, String[] userData) {
		StringBuffer clientData = new StringBuffer();
		clientData.append("servicecode="+serviceCode);
	
		for(int idx=0; idx<userData.length;idx++) {
			clientData.append("&");
			clientData.append(item[idx] + "=" + userData[idx]);
		}
		return clientData.toString();
	}
	
	
	
	
	
	/* 사용자 입력 */
	private String userInput(Scanner scanner) {
		return scanner.next();
	}

	/* 프로그램 메인 타이틀 제작 */
	private String mainTitle(String date) {
		StringBuffer title = new StringBuffer();

		title.append("◤Donggle◥〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓\n");
		title.append("∥\n");
		title.append("∥  D◎nggle D◎nggle   \n");
		title.append("∥      Planner\n");
		title.append("∥                  ╭◜◝ ͡ ◜◝        ╭◜◝ ͡ ◜◝\n");
		title.append("∥	          ( •‿•。   ) ☆   ( •‿•。   ) ☆\n");
		title.append("∥	           ╰◟◞ ͜ ◟◞        ╰◟◞ ͜ ◟◞\n");
		title.append("∥	                  ╭◜◝ ͡ ◜◝╮ \n");
		title.append("∥	                 ( •‿•。   ) ☆\n");
		title.append("∥                         ╰◟◞ ͜ ◟◞╯\n");
		title.append("∥                       " + date + "\n");
		title.append("∥                      design by. 4조\n");
		title.append("∥\n");
		title.append("●〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓\n");

		return title.toString();
	}// 메인 타이틀

	private String makeAccess(boolean isItem, String AccessCode) {

		StringBuffer access = new StringBuffer();

		if (isItem) {
			access.append("◤ACCESS◥〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓\n");
			access.append("∥\n");
			access.append("∥	  ____________     ____________\n");
			access.append("∥	 ｜AccessCode｜     ｜SecretCode｜    \n");
			access.append("∥	  ￣￣￣￣￣￣         ￣￣￣￣￣￣     \n");
			access.append("∥            " + ((AccessCode != null) ? AccessCode + "          \t" : ""));
		} else {
			access.append("●〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓\n");
			access.append("\n");
			//access.append("\n❥ ❥ ❥ Loading . . . \n");
		
		}

		return access.toString();

	} // Login화면
	
	
	public void love() {

		for (int i = 0; i < 13; i++) {
			System.out.print("❥ ");

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Loading...");
	}

	/* Server 응답에 따른 로그인 결과값 출력 */
	private String accessResult(boolean isAccess) {
		
		StringBuffer accessResult = new StringBuffer();

		if (isAccess) {
			accessResult.append(".....Move after 2 seconds...\n");
			accessResult.append("❥ ❥ ❥ ❥ Successful Connection !!!\n");

		} else {
			accessResult.append("❥ ❥ ❥ ❥ Connection Failed...\n");
			accessResult.append("______________________ Retry(Y/N) ?");
		}

		return accessResult.toString();

	}
	
	private String close() {
		StringBuffer close = new StringBuffer();
		
		close.append("\n◤G◎◎D BYE !◥ 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓\n");
		close.append("∥\n");
		close.append("∥ See you ~ !ლ(●ↀωↀ●)ლ ლ(●ↀωↀ●)ლ \n");
		close.append("∥\n");
		close.append("●〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		
		return close.toString();
		
	}
	

	private String makeMenu() {

		StringBuffer menu = new StringBuffer();

		menu.append("◤MENU◥〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓\n");
		menu.append("∥\n");
		menu.append("∥ 1. TASK LIST         2. TASK SETTINGS\n");
		menu.append("∥ 3. MODIFY TASK       4. TASK STATS\n");
		menu.append("∥ 0. DISCONNECT\n");
		menu.append("∥\n");
		menu.append("●〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓 Select: ");

		return menu.toString();

	}// 메뉴 1번 - 메인 메뉴


	
	
	private String makeMessage(String text) {
		StringBuffer message = new StringBuffer();

		message.append("[message] ");
		message.append(text);
		message.append("\n");

		return message.toString();
	}// Error Message - 입력값이 제대로 들어오지않았을 때

	/*화면에 현재의 날짜를 출력하기 위한 */
	private String getToday(boolean isDate) {
		String pattern = (isDate) ? "yyyy. MM. dd" : "yyyy-MM-dd HH:mm:ss";
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
	}

	private void display(String text) {
		System.out.print(text);
	}// 화면 출력

}// userapp
