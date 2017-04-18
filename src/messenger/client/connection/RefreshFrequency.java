package messenger.client.connection;

import messenger.client.Session;

public class RefreshFrequency implements Runnable{

	@Override
	public void run() {
		try {
			while(true){
			Thread.sleep(5000);
			if(Session.getMainWindow()!=null)
				Session.getConnection().refreshOnline();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
