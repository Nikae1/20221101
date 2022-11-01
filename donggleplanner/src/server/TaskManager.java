package server;

import java.util.ArrayList;

import server.beans.ToDoBean;

public class TaskManager {

	public TaskManager() {
		
	}
	
	
	/* 특정 계정의 특정한 달의 등록되어 있는 할 일의 날짜 리스트 가져오기 */
	/* 1.clientData : serviceCode=9&accessCode=hoonzzang&date=202210 --> Bean Data 
	 * 2. todo --> Dao.getToDoList --> ArrayList<ToDoBean>
	 * 
	 * */
	public String getToDoDateCtl(String clientData) {
		DataAccessObject dao = new DataAccessObject();
		ArrayList<ToDoBean> toDoList = null;
		
		ToDoBean todo = (ToDoBean)this.setBean(clientData);
		todo.setFileIdx(2);
		return this.convertServerData(dao.getToDoList(todo));
		
		
		
		
//		return this.convertServerData(toDoList);
//		return this.convertServerData
//				(dao.getToDoList((ToDoBean)
//						this.setBean(clientData))); 
	}
	
	public String getToDoListCtl(String clientData) {
		DataAccessObject dao = new DataAccessObject();
		return this.convertServerData(dao.getToDoList2((ToDoBean)this.setBean(clientData)));
	}
	
	
	
	/* 등록된 할 일을 불러오기 위한 메소드 */
	private String convertServerData2(ArrayList<ToDoBean> list) {
		StringBuffer serverData = new StringBuffer();
		
		for(ToDoBean todo : list) {
			serverData.append(todo.getStartDate() != null? todo.getStartDate()+ ",":"");
			serverData.append(todo.getEndDate() != null? todo.getEndDate()+ ",":"");
			serverData.append(todo.getContent() != null? todo.getContent()+ ",":"");
			serverData.append(todo.getStatus() != null? todo.getStatus()+ ",":"");
			serverData.append(todo.isIsactive()+ ",");
			serverData.append(todo.getComment() != null? todo.getComment() +",":"");
			if(serverData.charAt(serverData.length()-1) == ',') {
				serverData.deleteCharAt(serverData.length()-1);
			}
			serverData.append(":");
		}
			
		if(serverData.charAt(serverData.length()-1) == ':') {
			serverData.deleteCharAt(serverData.length()-1);
		}
		
		return serverData.toString();
	}
	
	
	
	
	private String convertServerData(ArrayList<ToDoBean> list) {
		StringBuffer serverData = new StringBuffer();
		/* 예제 = 1:10:15:16:20:일때 ----> 마지막 콜론은 떼주기  */
		for(ToDoBean todo : list) {
			serverData.append(todo.getStartDate().substring(6, 8));
			serverData.append(":");
			
		}
		
		/* 마지막 추가된  " : " 없애기 */
		if(serverData.length() != 0) {
			
			serverData.deleteCharAt(serverData.length()-1);
		}
		return serverData.toString();	
		}
	
	
	
	private Object setBean(String clientData) {
		Object object = null;
		String[] splitData = clientData.split("&");
		switch(splitData[0].split("=")[1]) {
		case "9":
			/* serviceCode=9&accessCode=hoonzzang&date=202210  
			 * servicecode를 임의로 9로 설정하여 사용
			 * 
			 * */
			object = new ToDoBean();
			((ToDoBean)object).setAccessCode(splitData[1].split("=")[1]);
			((ToDoBean)object).setStartDate(splitData[2].split("=")[1]);
			break;
		
		case "12":
			/* serviceCode=12&access
			 * Code=hoonzzang&startDate=12&endDate=12&visibleType=T */
			object = new ToDoBean();
			((ToDoBean)object).setAccessCode(splitData[1].split("=")[1]);
			((ToDoBean)object).setStartDate(splitData[2].split("=")[1]);
			((ToDoBean)object).setEndDate(splitData[3].split("=")[1]);
			String visibleType = splitData[4].split("=")[1];
			if(!visibleType.equals("T")) {
				((ToDoBean)object).setVisibleType(visibleType.equals("E")? "A" : "I" );
			}
			
		}
				
		return object;
	} 
	
	
	
	
	
	
}
