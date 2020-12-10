package time;

import java.sql.Time;
import java.util.Calendar;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Thread{

	public static void main(String[] args) {
		Time c_actual=new Time(Calendar.HOUR,Calendar.MINUTE,Calendar.SECOND);
		Servidor server=new Servidor(c_actual);
		System.out.println("SERVER CLOCK -> "+server.getCs().toString());
		Cliente client=new Cliente();
		client.start();
		server.start();
		System.out.println(" CLIENTE   -   SERVIDOR");
		while(true) {
			System.out.println(client.getCc().toString()+"   -   "+server.getCs().toString());
			if(!timeOneIsMax(server.getCs(),client.getCc()).equals("igual") ){
				synchronize(client,server,0);
			}
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}


	}
	

	public static String timeOneIsMax(Time h1, Time h2) {
		long time_one = h1.getSeconds() + h1.getMinutes() * 60 + h1.getHours() * 60 * 60;
		long time_two = h2.getSeconds() + h2.getMinutes() * 60 + h2.getHours() * 60 * 60;
		
		if(time_one>time_two) {
			return "mayor";
		}else if(time_one==time_two) {
			return "igual";
		}else {
			return "menor";
		}
	}
	
	public static void synchronize(Cliente client, Servidor server,int delay) {
		Time C=server.getCs();
		C= new Time(C.getHours(),C.getMinutes(),C.getSeconds()+delay+1);
		
		if (timeOneIsMax(C,client.getCc()).equals("mayor")) {
			client.setCc(C);
		}else if(timeOneIsMax(C,client.getCc()).equals("menor")) {
			delay=client.getCc().getSeconds()-C.getSeconds();
			//detenr cliente por delay
			client.setStatus(delay*1000);
		}
	}

}

class Cliente extends Thread {
	

	private int hours, minute, seconds,status;

	private Time Cc = new Time(hours, minute, seconds);
	

	public Cliente() {
		Scanner entrada = new Scanner(System.in);
		System.out.println("ENTER CLIENT TIME IN THIS FORMAT -> hh:mm:ss");
		Cc = Time.valueOf(entrada.next());
		
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	public Time getCc() {
		return Cc;
	}

	public void setCc(Time Cc) {
		this.Cc = Cc;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public void run() {
		while (true) {
			try {
				if(status==0) {
				Cc=new Time(Cc.getHours(),Cc.getMinutes(),Cc.getSeconds()+1);
				sleep(1000);
				}else {
					sleep(status);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}

class Servidor extends Thread {

	private int hours, minute, seconds;


	public Servidor(Time C) {
		Cs=C;
	}
	
	private Time Cs = new Time(hours, minute, seconds);

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	public Time getCs() {
		return Cs;
	}

	public void setCs(Time C) {
		this.Cs = C;
	}

	
	

	public void run() {
		while(true) {
			try {
				Cs=new Time(Cs.getHours(),Cs.getMinutes(),Cs.getSeconds()+1);
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
