package cn.larry.sample;

public class ThreadTest {
	public static int ticket = 100;
	public static synchronized void subTicket(){
		ticket--;
	}
	public static  void sellTicket(){
		System.out.printf("thread %s is selling ticket%d \n",Thread.currentThread().getName(),ticket);
		ticket --;
		//subTicket();
	}
	public static void main(String[] args) {
		Thread t1 = new Thread(()->{
			while(ticket>0){
				System.out.printf("thread %s is selling ticket%d \n",Thread.currentThread().getName(),ticket);
				ticket --;
				int k;
				for(int j = 0;j<10000;j++)
					k = (int) (j*j-Math.sqrt(j*j*j));
				//Thread.sleep(10);
			}
				
		});
		Thread t2 = new Thread(()->{
			while(ticket>0){
				System.out.printf("thread %s is selling ticket%d \n",Thread.currentThread().getName(),ticket);
				ticket --;
				int k;
				for(int j = 0;j<10000;j++)
					k = (int) (j*j-Math.sqrt(j*j*j));
			}
		});
		t1.start();
		t2.start();
	}
}
