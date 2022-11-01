package client;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import server.ServerController;

public class UserApp {
	
	public UserApp() {
		frontController();
	}
	
	/* 클라이언트 화면 및 데이터 흐름 제어 */
	private void frontController() {
		Scanner scanner = new Scanner(System.in);
		String mainTitle = this.mainTitle(this.getToday(true));
		String mainMenu = this.getMainMenu();
		boolean isLoop = true;
		String[] accessInfo = new String[2];
		boolean accessResult;
		ServerController ctl = new ServerController();
		TaskManagement task = null;
		String message = null;
		
		while(isLoop) {
			
			for(int idx=0;idx<accessInfo.length; idx++) {
				this.display(mainTitle);
				this.display(this.getAccessLayer(true, accessInfo[0]));
				accessInfo[idx] = this.userInput(scanner);
			}
			this.display(this.getAccessLayer(false, null));
			
			/* ClientData 생성 */
			String[] itemName = {"id", "password"};			
			/* 서버에 로그인 정보 전달 */
			accessResult = ctl.controller(this.makeClientData("1", itemName, accessInfo)).equals("1")? true: false;
			
			/* 서버로부터 받은 로그인 결과에 따른 화면 출력 */
			this.display(this.accessResult(accessResult));
			if(!accessResult) {
				/* 로그인 실패 */
				if(this.userInput(scanner).toUpperCase().equals("N")) {
					isLoop = false;
				}else {
					accessInfo[0] = null;
					accessInfo[1] = null;
				}	
			}else {
				/* 로그인 성공 */
				accessInfo[1] = null;
				while(isLoop) {
					String menuSelection = new String();
					while(true) {
						this.display(mainTitle);
						this.display(mainMenu);
						this.display(message != null? "[Message : " + message + " ]\n": "");
						
						menuSelection = this.userInput(scanner);
						/* 0~4 범위의 값이 아닌 경우 재입력 요구 */
						if(this.isInteger(menuSelection)) {
							if(this.isIntegerRange(Integer.parseInt(menuSelection), 0, 4)) {
								break;
							}else {
								message = "메뉴는 0~4 범위내로 선택해주세요";
							}
						}else {
							message = "메뉴는 숫자로 입력해주세요";
						}
					}
				   /* 0번 선택시 서버에 로그아웃 통보 후 프로그램 종료 */
					if(menuSelection.equals("0")) {
						ctl.controller(this.makeClientData("-1", itemName, accessInfo));
						isLoop = false;
					}else{
						/* TaskManagement Class Call */
						task = new TaskManagement();
						
						if(menuSelection.equals("1")) {
							/* Service Code 생성 */
							String[] userSelection = { 
									"    +++++++++++++++++++++++++++++++++++++ Start Date : ", 
									"    +++++++++++++++++++++++++++++++++++++   End Date : ", 
									"    +++++++++++++++++++++++++ Total  Enable  Disable : "};
							String[] userInput = new String[3];
							int month = 0;							
							while(true) {
								menuSelection = "11";
								
								this.display(mainTitle);
								this.display(message!=null? "   [ " + message + " ]\n": "");
								this.display(task.taskController(
										Integer.parseInt(menuSelection), accessInfo[0], month).toString());
								
								/* Start Date */
								char direction = 'p';
								for(int idx=0; idx<userInput.length; idx++) {
									this.display(userSelection[idx]);
									userInput[idx] = this.userInput(scanner).toUpperCase();
									
									if(this.isInteger(userInput[idx])) {
										if(!this.isIntegerRange(this.convertToInteger(userInput[idx]), 1, this.getLengthOfMonth(month))){
											message = "검색 가능한 일자가 아닙니다.";
											direction = 'c';
											break;
										}else {
											message = null;
											direction = 'p';
										}
									}else {
										if(userInput[idx].equals("P") || userInput[idx].equals("N")) {
											month += userInput[idx].equals("P")? -1 : 1;
											direction = 'c';
											break;
										}else if(userInput[idx].equals("Q")){
											direction = 'b';
											break;
										}else if(idx==userInput.length-1) {
											if(userInput[idx].equals("T") || userInput[idx].equals("E") || userInput[idx].equals("D")){
												message = null;
												direction = 'p';
											}else {
												message = "부적합한 메뉴입니다.";
												direction = 'c';
											}
										}else {
											message = "부적합한 메뉴입니다.";
											direction = 'c';
											break;
										}
										
									}
								}
								
								if(direction == 'b') break;
								if(direction == 'c') continue;
								
								this.display(userInput[0] + " : "+ userInput[1] + " : " + userInput[2]);
								/* STEP 2 */
								
							}
						}else {
							//task.taskController(Integer.parseInt(menuSelection), accessInfo[0]);
						}
					}
				}
			}
		}
		
		this.display("\n\n  x-x-x-x-x-x-x-x-x-x- 프로그램을 종료합니다 -x-x-x-x-x-x-x-x-x-x");	
		scanner.close();
	}
	
	/* TaskList 계속 진행 여부 */
	private boolean isBreak(String keyValue) {
		return keyValue.toUpperCase().equals("Q")? true:false;
	}
	/* TaskList를 처음부터 다시 실행 여부 */
	private boolean isRestart(String keyValue) {
		return keyValue.toUpperCase().equals("P") || keyValue.toUpperCase().equals("N")? true: false;
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

	/* 문자 >> 정수 변환 */
	private int convertToInteger(String value) {
		return Integer.parseInt(value);
	}

	/* 정수의 범의 체크 */
	private boolean isIntegerRange(int value, int starting, int last) {
		return (value >= starting && value <= last)? true: false;
	}
	
	private String makeClientData(String serviceCode, String[] item, String[] userData) {
		StringBuffer clientData = new StringBuffer();
		clientData.append("serviceCode=" + serviceCode);
		for(int idx=0; idx<userData.length; idx++) {
			if(userData[idx] != null) {
				clientData.append("&");
				clientData.append(item[idx] + "=" + userData[idx]);
			}
		}
		return clientData.toString();
	}
	
	/* 프로그램 메인 타이틀 제작 */
	private String mainTitle(String date) {
		StringBuffer title = new StringBuffer();
		
		title.append("\n\n\n");
		title.append("  __________________________________________________________\n\n");
		title.append("     ◀▷◀▷◀▷◀▷◀▷◀▷◀▷◀▷◀▷\n");
		title.append("          T A S K\n"); 
		title.append("          M A N A G E R                      " + date + "\n");
		title.append("     ◀▷◀▷◀▷◀▷◀▷◀▷◀▷◀▷◀▷        Designed by HoonZzang\n");
		title.append("  __________________________________________________________\n");

		return title.toString();
	}
	
	private String getAccessLayer(boolean isItem, String accessCode) {
		StringBuffer accessLayer = new StringBuffer();
		
		if(isItem) {
			accessLayer.append("\n");
			accessLayer.append("     Access ++++++++++++++++++++++++++++++++++++++++++\n");
			accessLayer.append("     +        AccessCode          SecretCode\n");
			accessLayer.append("     + --------------------------------------------\n");
			accessLayer.append("     +         " + ((accessCode!=null)? accessCode+"\t\t": ""));
		}else {
			accessLayer.append("     +++++++++++++++++++++++++++++++++++ Connecting...");
		}
	    return accessLayer.toString();
	}
	
	/* 서버응답에 따른 로그인 결과 출력 */
	private String accessResult(boolean isAccess) {
		StringBuffer accessResult = new StringBuffer();
		
		accessResult.append("\n     >>>>>>>>>>>>>>>>>>>>>>>>> ");
		if(isAccess) {
			accessResult.append("Successful Connection\n"); 
		    accessResult.append("     Move after 2 sceonds...");
		}else {
			accessResult.append("Connection Failed\n");
			accessResult.append("     _______________________________ Retry(y/n) ? ");
		}
		
		return accessResult.toString(); 
	}
	
	/* 메인페이지 출력 */
	private String getMainMenu() {
		StringBuffer mainPage = new StringBuffer();
		
		mainPage.append("\n");
		mainPage.append("     [ MENU SELECTION ] __________________________________\n\n");
		mainPage.append("       1. TASK LIST		2. TASK REGISTRATION\n");
		mainPage.append("       3. MODIFY TASK		4. TASK STATS\n");
		mainPage.append("       0. DISCONNECT    \n");
		mainPage.append("     ________________________________________________ : ");
		   
		return mainPage.toString();
	}
	
	/* 날짜시간 출력 : LocalDateTime Class + DateTimeFormatter Class */
	private String getToday(boolean isDate) {
		String pattern = (isDate)? "yyyy. MM. dd.": "yyyy-MM-dd HH:mm:ss";
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern)); 
	}
	/* 선택한 달의 일수 구하기 */
	private int getLengthOfMonth(int month) {
		return LocalDate.now().plusMonths(month).lengthOfMonth();
	}
	/* 사용자 입력 */
	private String userInput(Scanner scanner) {
		return scanner.next();
	}
	/* 화면 출력 */
	private void display(String text) {
		System.out.print(text);
	}
}
