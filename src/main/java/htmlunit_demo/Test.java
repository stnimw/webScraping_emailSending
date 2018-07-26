package htmlunit_demo;

import java.util.Timer;
import java.util.TimerTask;

public class Test {

	public static void main(String[] args) {
		final ServiceProgress serviceProgress = new ServiceProgress();
		final Timer timer = new Timer();

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				String divisionNo = "003";// Hospital division (003:Hsinchu)
				String depNo = "CE100";// Department (CE100: Obstetrics and Gynecology)
				String roomNo = "032";// Room number (032: room of physician 劉惠珊 LIU HUI SHAN)
				String myNO = "3";
				String notifiedNO = "4";
				String pageXml = serviceProgress.getProgressEn(divisionNo, depNo);
				MsgVO vo = serviceProgress.extractDataEn(pageXml, roomNo);
				if (vo.getCurrNo() != null && vo.getCurrNo().equals(notifiedNO)) {
					String receiver = "reciever@gmail.com";//Receiver's email
					String title = "Appointment time is close";
					String msg = 
							"Your Number: " + myNO 
							+ "<br>Current Number: " + vo.getCurrNo() 
							+ "<br>Room Number: "+ vo.getRoomNo() 
							+ "<br>Physician: " + vo.getPhysician();
					final Sender sender = new Sender( receiver, title, msg);
					sender.start();
					timer.cancel();
					System.out.println("Timer cancel");
				}
			}
		};
		timer.schedule(task, 1000L, 1000L * 60); //run at every minute

	}

}
