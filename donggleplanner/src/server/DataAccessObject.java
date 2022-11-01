package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import server.beans.AccessHistoryBean;
import server.beans.MemberBean;
import server.beans.ToDoBean;

/*Data File과의 통신을 위한(DAO)
 * DB에서 정보만 전달하는 역할을 수행
 * */
public class DataAccessObject {
	/* txt파일의 주소 */
 String[] fileInfo = {
		 "C:\\java\\data\\woong\\donggleplanner\\src\\database\\MEMBERS.txt",
		 "C:\\java\\data\\woong\\donggleplanner\\src\\database\\ACCESSHISTORY.txt",
		 "C:\\java\\data\\woong\\donggleplanner\\src\\database\\DONGGLE.txt"};
	public DataAccessObject() {

	}

	/* 회원정보 수집 (List는 Collection의 최상위 ) */
	public ArrayList<MemberBean> readDatabase(int fileIdx) {
		String line;
		ArrayList<MemberBean> memberList = null;
		/* Generic<> list를 활용할때에는 list에 넣은 값들은 타입이 자기의 속성값이 아닌
		 * 가장 큰 값인 object로 바뀌어, 빈으로 바꿀때엔 다운캐스트가 필요하다
		 * Generic은 <>안에 넣어주면 자동으로 다운캐스트를 도와준다
		 *  */
		MemberBean member;
		BufferedReader buffer = null;
		
try {
	/* buffer의 파라미터 안에 파일을 읽어올 수 있는 FileReader를 만들고 
	 * FileReader의 파라미터 안에
	 * File을 만들며, 그 파라미터 안에 DB의 주소값을 가진 fileInfo를 넣어
	 * 별도로 참조변수 선언을 하여 메모리를 할당하지 않고 원하는 값을 가져온다.
	 *  */
	buffer = new BufferedReader(new FileReader
			(new File(fileInfo[fileIdx])));
	memberList = new ArrayList<MemberBean>();
	/* list의 종류인 Array를 사용하여 사용
	 * 배열과의 차이점은 배열은 크기를 미리 설정해야하지만
	 * ArrayList는 크기를 미리 정하지않고 원하는 만큼 가능하다
	 *  */
	
	while((line = buffer.readLine())!= null) 
	/* filereader는 char(한글자)로 읽어오므로 readLine을 통하여 줄로 읽어온다 */
	{
		/* 불러온 MEMBERS의 
		 * (jiwoong,123,김지웅,01066248449,1)을 split
		 * */
		String[] record = line.split(",");
		member = new MemberBean();
		member.setAccessCode(record[0]);
		member.setSecretCode(record[1]);
		member.setUserName(record[2]);
		member.setPhoneNumber(record[3]);
		member.setActivation(Integer.parseInt(record[4]));
		memberList.add(member);
	}
	
} catch (FileNotFoundException e) {
	/* Exception은 자바가 처리할 수 없는 모든 예외처리를 맡는다 */
	System.out.println("파일이 존재하지 않습니다.");
	e.printStackTrace();
	/* printStackTrace는 어디서 에러가 발생했는지 나타내는 */
}catch (IOException e) {
	memberList = null;
	/* catch구문에서 오류가 발생하여 초기화가 되지 않을 것을 대비 */
	System.out.println("파일로부터 데이터를 가져올 수 없습니다.");
	e.printStackTrace();
}finally {
	/*finally =  try - catch구문에 상관없이 실행 */
	try {
		buffer.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
}
     return memberList;
	}

	
	
	
	/* 접속기록(로그정보) 작성 */
	public boolean writeAccessHistory(AccessHistoryBean accessInfo) {
		boolean result = false;
		BufferedWriter writer = null;
		
		try {
			writer = new BufferedWriter
					(new FileWriter
					(new File(this.fileInfo[accessInfo.getFileIdx()]),true));
			/* 실제로 쓰는 건 FileWriter
			 * true - FileWriter는 원래 한 줄만 기록이 저장되므로, 
			 * 옵션값을 추가하여 다른 줄이 추가되도록 해야한다 
			 * FileWriter라는 생성자를 호출할 때, true값을 넣어주어야 한다
			 * */
			writer.write(accessInfo.getAccessCode()+","
			 +accessInfo.getAccessDate()+","
					+accessInfo.getAccessType());
			 
				writer.newLine();
				writer.flush();
				result = true;
		
		}catch(IOException e){
			e.printStackTrace();
		}finally {
			try {writer.close();} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
		
		
		
	}
	
	/* TODO List 읽어오기  */
	public ArrayList<ToDoBean> getToDoList(ToDoBean searchInfo){
		ArrayList<ToDoBean> dayList = null;
		ToDoBean toDo = null;
		String line;
		BufferedReader buffer = null;
		int date, recordCount = 1;
		int[] dateRange = new int[2];

		/* 시작 날과 마지막 날을 담을 int형 배열 */
		LocalDate userDate = LocalDate.of(Integer.parseInt(searchInfo.getStartDate().substring(0,4)),
				Integer.parseInt(searchInfo.getStartDate().substring(4)),1);
		
		try {
			buffer = new BufferedReader
					(new FileReader
							(new File(fileInfo[searchInfo.getFileIdx()])));
			
			
			while((line=buffer.readLine()) !=null) {
				/* recordCount를 1로 설정한 이유는 모든 *달은 1일로 시작하므로
				 * 새로운 달이 시작되면 1값을 주어 ArrayList<ToDoBean>를 생성한다
				 *  */
				if(recordCount == 1) dayList = new ArrayList<ToDoBean>();
				
				String[] record = line.split(",");
				date = Integer.parseInt(searchInfo.getStartDate());  //예정된 입력값
				dateRange[0] = Integer.parseInt(record[1].substring(0, 8));
				dateRange[1] = Integer.parseInt(record[2].substring(0, 8));
					
				if(date > dateRange[0]/100) dateRange[0] = Integer.parseInt(date + "01");
				if(date < dateRange[1]/100) {
					dateRange[1] = Integer.parseInt(date + "" + userDate.lengthOfMonth());
				}

				for(int idx=dateRange[0]; idx<=dateRange[1]; idx++) {
					if(recordCount !=1) {
						if(this.duplicateCheck(dayList, idx+"")) {
							continue;
						}
					}
					
					toDo = new ToDoBean();
					/* 문자열로 저장하기 위해서 + " " */
					toDo.setStartDate(idx+"");
					dayList.add(toDo);
				}
				
				recordCount++;
				
				
			}
			buffer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {if(buffer !=null) buffer.close();} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return dayList;
		}
	
	/* 특정기간의 할 일 목록 가져오기 */
	public ArrayList<ToDoBean> getToDoList2(ToDoBean searchInfo){
		/* return값이 ArrayList기 때문에 맨 처음 리스트를 생성 
		 * 두 번 이상 사용할 내용을 미리 선언
		 * */
		ArrayList<ToDoBean> toDoList = new ArrayList<ToDoBean>();
		ToDoBean toDo = null;
		BufferedReader reader = null;
		String line = null;
		String[] record = null;
		
		try {
			reader = new BufferedReader(new FileReader(new File(fileInfo[searchInfo.getFileIdx()])));
			/* line의 값이 null이 아닐 때 */
			while((line = reader.readLine()) != null)  {
				/* 1.accessCode 2.startDate ~ endDate 3.visibleType */
				/* 
				 * continue의 ! 구문을 이용항여 값이 아닐 시 계속 진행되게 만들어 원하는
				 * 값을 얻는 구문
				 * record[0]의 값이 searchInfo.accessCode의 값과 같지 않다면 */
				if(!record[0].equals(searchInfo.getAccessCode())) continue;
				/* startDate의 값이 일반적인 구문이라면 작거나 같다이지만 계속 진행을 시켜 
				 * 분기시키지 위해 continue구문을 이용 */
				if(Integer.parseInt(searchInfo.getStartDate()) < Integer.parseInt(record[1].substring(0, 8))) continue;
				if(Integer.parseInt(searchInfo.getEndDate()) > Integer.parseInt(record[2].substring(0, 8))) continue;
				
				if(searchInfo.getVisibleType() != null) {
					if(!record[5].equals(searchInfo.getVisibleType())) continue;
				}
				
				toDo = new ToDoBean();
				toDo.setStartDate(record[1]);
				toDo.setEndDate(record[2]);
				toDo.setContent(record[3]);
				toDo.setStatus(record[4]);
				toDo.setIsactive(record[5].equals("A")? true: false);
				toDo.setComment(record[6]);
				toDoList.add(toDo);
			}
				
				
		} catch (FileNotFoundException e) {
			System.out.println("파일을 찾을 수 없습니다.");
			e.printStackTrace();
		}catch(IOException e) {
			System.out.println("파일을 읽을 수 없습니다.");
			e.printStackTrace();
		}finally {
			/* 처음의 reader에 값이 없을 경우 */
			if(reader != null) try{reader.close();} catch(IOException e) {e.printStackTrace();}
		}
		
		return toDoList;
	}
	
	
	
	
	
	
	
	
	/* 같은 날 할 일이 2개 이상일때 같은 날이 생성되어 추가되는 것을 막기위해서 */
	private boolean duplicateCheck(ArrayList<ToDoBean> dayList, String compareValue) {
		boolean result = false;
		for(int listIdx=0; listIdx<dayList.size(); listIdx++) {
			if(compareValue.equals(dayList.get(listIdx).getStartDate())) {
				result = true;
				break;
			}
		}
		return result;
	}
	

}
