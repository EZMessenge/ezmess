package messenger.client.connection;


import java.math.BigInteger;

import messenger.client.Session;
import messenger.client.cryptography.RSA;

public class KeyAccept {
	private String username;
	private long publicKey;
	private long mod;
	private RSA rsa;
	public KeyAccept(String s){
		username = s.split(" ")[1];
		System.out.println("Sono "+Session.getUsername()+" e sto generando la chaive per "+username);
		RSA rsa = new RSA();
		this.rsa = rsa;
		publicKey = rsa.getPublicKey();
		mod = rsa.getMod();//?
		String answer = Session.getConnection().sendRequest("$$sendKey "+username+" "+publicKey+","+mod);
		System.out.println("Ho inviato un sendKey ed ho ricevuto: "+answer);
		
	}
	public void decodeKey(long key){
		
		BigInteger desKey = BigInteger.valueOf(key).modPow(BigInteger.valueOf(rsa.getPrivateKey()), BigInteger.valueOf(mod));
		Session.createDESWithKey(username, desKey.longValue());
	}
}
