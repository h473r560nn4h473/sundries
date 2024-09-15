package schedule_001;

import java.time.LocalDate;     // 날짜 출력
import java.nio.file.Files;     // 파일 존재유무, 생성
import java.nio.file.Paths;     // Files 라이브러리 보조, 파일 경로 확인
import java.util.List;          // 할 일 목록, 상태
import java.util.ArrayList;     // List 라이브러리 보조, 동적 배열
import java.io.FileReader;      // 파일 내용 읽어오기, 파일을 읽기 위해 바이트 스트림을 문자 스트림으로 변환, 한번에 한 문자만 읽어옴
import java.io.BufferedReader;  // FileReader 보조, 버퍼를 사용하여 많은 데이터를 한 번에 읽어옴
import java.io.FileWriter;      // 파일 내용 쓰기, 파일에 데이터를 쓰기 위해 문자 스트림을 바이트 스트림으로 변환, 한번에 한 문자만 씀
import java.io.BufferedWriter;  // FileWriter 보조, 버퍼를 사용하여 많은 데이터를 한 번에 씀
import java.io.IOException;     // 파일 읽기/쓰기 권한 없음, 파일 부재, 디스크 오류 등으로 발생할 수 있는 IOException 예외 처리
import java.util.Scanner;       // 사용자 입력 처리

public class Print_001 {
  private static final String FILE_NAME = "schedule_001.txt";   // 파일 이름
  private static List<String> stuffs = new ArrayList<>();        // 할일 목록
  private static List<Boolean> stuffStatus = new ArrayList<>(); // 할일 상태

  public static void main(String[] args) {
    isItThere();        // 파일 확인
    whenIsIt();         // 오늘 날짜 출력
    callStuff();        // 할일 불러오기
    listStuff();        // 할일에 줄 번호와 상태 표시하여 출력
    coolOnesHeels();    // 커맨드 입력 처리
  }

  private static void isItThere() {                 // 파일 확인
    try {                                           // IOException try-catch 블록
      if (!Files.exists(Paths.get(FILE_NAME))) {    // 파일이 존재하지 않으면
        Files.createFile(Paths.get(FILE_NAME));     // 빈 파일 생성
      }
    } catch (IOException e) {
      e.printStackTrace();                          // 예외 정보 출력
    }
  }

  private static void whenIsIt() {
    System.out.println("오늘 날짜: " + LocalDate.now());
  }

  private static void callStuff() {
    try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {   // new FileReader(FILE_NAME)는 파일에서 문자를 읽어오는 클래스인 FileReader 객체를 생성하여 지정된 파일(FILE_NAME)을 읽고, new BufferedReader(new FileReader(FILE_NAME))는 BufferedReader 객체를 생성하여 FileReader를 감싼다(FileReader를 포함하여 그 기능을 확장한다). FileReader를 사용하여 파일에서 데이터를 읽어오지만, 내부적으로 버퍼링을 통해 더 효율적으로 데이터를 처리할 수 있게 된다. 
      String line;                                      // 할일을 한줄마다 저장하기 위한 변수를 선언
      while ((line = reader.readLine()) != null) {      // BufferedReader 객체의 메소드인 reader.readLine()으로 파일을 한 줄씩 읽고 line 변수에 저장한다. reader.readLine()은 일의 끝에 도달할 경우 null을 반환하는데, 반복문이 참일 조건이 null이 아닐 때이므로 반복문은 파일의 끝에 도달할 때까지 반복된다.  
        stuffs.add(line);                                // 할일 목록을 저장하는 동적 배열인 stuffs에 파일에서 읽어온 할일을 한 줄씩 저장
        stuffStatus.add(false);                         // 기본 할일 상태를 체크되지 않음으로 설정
      }
    } catch (IOException e) {
      e.printStackTrace();                              // 예외 정보 출력
    }
  }

  private static void listStuff() {
    for (int i = 0; i < stuffs.size(); i++) {            // 할일 목록에 줄 번호와 상태를 추가하는 반복문
      String status = stuffStatus.get(i) ? "■" : "□";   // 할일 상태 표시
      System.out.println((i + 1) + ". " + status + " " + stuffs.get(i)); // 할일 줄 번호(컴퓨터는 숫자를 0부터 세기 시작하므로 1을 더해줘야 한다), 상태, 할일 내용 출력
    }
    System.out.println("\n---\n");
    System.out.println("숫자를 입력하고 Enter키로 커맨드 실행:");
    System.out.println("1. 체크 / 체크해제");
    System.out.println("2. 할일 내용 변경");
    System.out.println("3. 프로그램 종료");
  }

  private static void coolOnesHeels() {
    Scanner scanner = new Scanner(System.in);   // 사용자의 입력(System.in)을 받기 위한 객체 생성
    while (true) {  // 프로그램 종료를 선택하기 전까지 명령을 계속 받아야 하므로 반복문 조건을 true로 둬서 무한루프
      int command = scanner.nextInt();  // scanner.nextInt()가 Scanner 객체를 사용하여 사용자가 입력한 정수를 읽어서(입력받은 값을 정수로 변환하여 반환하면) command 변수에 저장한다.
      switch (command) {
        case 1:
          toggleStuff(scanner);
          break;
        case 2:
          changeStuffMenu(scanner);
          break;
        case 3:
          saveStuff();
          System.out.println("프로그램 종료");
          return;
        default:
          System.out.println("잘못된 입력");
      }
    }
  }

  private static void toggleStuff(Scanner scanner) {    // Scanner 객체를 매개변수로 받아 할일의 체크 상태를 변경한다
    System.out.println("체크/체크해제할 할일 번호를 입력하세요:");
    int stuffNumber = scanner.nextInt() - 1;    // 사용자가 입력한 번호를 읽어와 stuffNumber 변수에 저장한다. 인덱스는 0부터 시작하므로 1을 빼줘야 한다.
    if (stuffNumber >= 0 && stuffNumber < stuffs.size()) {   // 유효한 줄 번호인지 확인
      stuffStatus.set(stuffNumber, !stuffStatus.get(stuffNumber));  // 체크 상태 변경
      listStuff();  // 변경된 할일 목록 출력
    } else {    // 빈 파일이거나, 선택한 번호에 해당하는 줄에 내용이 없을 경우
      System.out.println("파일이 비어 있거나 할일이 충분하게 입력되지 않았습니다. 입력한 번호에 저장할 할일 내용을 입력하세요:");
      scanner.nextLine(); // nextInt() 메소드가 숫자만 읽고 입력 버퍼에 남아 있는 개행 문자(엔터 키)를 소비하지 않으므로 바로 nextLine()을 호출할 경우 입력 버퍼에 남아 있는 개행 문자를 읽고 빈 문자열을 반환한다. 이를 방지하기 위해 scanner.nextLine();을 한 번 호출하여 입력 버퍼에 남아 있는 개행 문자를 소비한 후 다음 과정을 진행한다. 
      String newStuff = scanner.nextLine(); // 사용자가 입력한 새 할일 내용을 저장하는 변수 선언
      addNewStuff(stuffNumber, newStuff);   // 할일 내용 갱신
    }
  }

  private static void changeStuffMenu(Scanner scanner) {
    while (true) {
      System.out.println("1. 변경할 할일 번호 입력");
      System.out.println("2. 메인 메뉴로 돌아가기");
      int subCommand = scanner.nextInt();   // 사용자가 입력한 번호를 읽어서 subCommand 변수에 저장
      switch (subCommand) { // subCommand에 따라 다른 작업 실행
        case 1:
          changeStuff(scanner);
          return;
        case 2:
          return;
        default:
          System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
      }
    }
  }

  private static void changeStuff(Scanner scanner) {
    System.out.println("변경할 할일 번호를 입력하세요:");
    int stuffNumber = scanner.nextInt() - 1;    // 사용자가 입력한 번호를 읽어와 stuffNumber 변수에 저장한다. 인덱스는 0부터 시작하므로 1을 빼줘야 한다.
    scanner.nextLine(); // 바로 전 과정에서 Enter를 입력하여 버퍼에 개행 문자가 남아있으므로 개행 문자를 한번 소비해줘야 한다.
    if (stuffNumber >= 0 && stuffNumber < stuffs.size()) {   // 입력한 번호가 유효한 줄 번호인지 확인
      System.out.println("기존 할일 내용: " + stuffs.get(stuffNumber));    // 입력한 번호가 유효한 줄 번호일 경우 기존 할일 내용을 출력
      System.out.println("새 할일 내용을 입력하세요:");
      String newStuff = scanner.nextLine(); // 입력받은 새 할일 내용을 읽어와서 newStuff 변수에 저장
      stuffs.set(stuffNumber, newStuff); // 입력한 줄 번호의 할일을 newStuff 변수에 저장된 내용으로 갱신
      stuffStatus.set(stuffNumber, false); // 상태를 체크되지 않은 상태로 변경
      listStuff();  // 변경된 할일 목록 출력
    } else {    // 빈 파일이거나, 선택한 번호에 해당하는 줄에 내용이 없을 경우
      System.out.println("잘못된 번호입니다. 새 할일 내용을 입력하세요:");
      String newStuff = scanner.nextLine(); // 사용자가 입력한 새 할일 내용을 저장하는 변수 선언
      addNewStuff(stuffNumber, newStuff);   // 할일 내용 갱신
    }
  }

  private static void addNewStuff(int stuffNumber, String newStuff) {   // stuffNumber, newStuff를 매개변수로 함
    while (stuffs.size() <= stuffNumber) {   // 유효하지 않은 줄 번호를 입력했을 때(빈 파일이거나 입력한 줄 번호가 기존 목록보다 클 경우 등) 목록 크기를 확장하기 위한 반복문
      stuffs.add("");    // 빈 문자열을 추가
      stuffStatus.add(false);   // 빈 문자열이라도 어쨌든 줄마다 상태를 표시해야 하고, 기본 상태는 "체크되지 않음" 이어야 한다.
    }
    stuffs.set(stuffNumber, newStuff);   // stuff 목록의 stuffNumber 인덱스에 newStuff를 설정
    stuffStatus.set(stuffNumber, false);    // 새 할일의 상태는 "체크되지 않음" 으로 되어야 한다.
    listStuff();    // 변경된 할일 목록 출력
  }

  private static void saveStuff() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {   // BufferedWriter로 FileWriter를 감쌌으므로 버퍼를 사용하여 여러 데이터를 한번에 모아서 쓴다 
      for (String stuff : stuffs) {  // stuffs 목록의 각 문자열 요소를 stuff 변수에 할당한다
        writer.write(stuff);    // stuff 변수에 저장된 문자열을 파일에 쓴다
        writer.newLine();   // 할일은 한줄에 하나씩이므로 다음 할일을 쓰기 위해 줄바꿈
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
