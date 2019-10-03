package controleurs.operations.liste.ajoutmessages.forum;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;

public class ConnectToUrl {

	private int sleeptime = 0;
	private String exceptionMsg;
	private int exception;
	private Connection connection = null;
	private Response response = null;
	private Document doc = null;

	public ConnectToUrl() {

	}

	public void setSleeptime(int sleeptime) {
		this.sleeptime = sleeptime;
	}

	public Document getDocumentFromUrl(String url) {

		// if (sleeptime > 0)
		// try {
		// Thread.sleep(sleeptime);
		// } catch (Exception e) {
		// // declenche uniquement si interrompu par un autre thread :
		// // ne devrait pas arriver
		// }
		
		/* Start of Fix */
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
            public void checkClientTrusted(X509Certificate[] certs, String authType) { }
            public void checkServerTrusted(X509Certificate[] certs, String authType) { }

        } };

        SSLContext sc;
		try {
			sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) { return true; }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        /* End of the fix*/

		connection = Jsoup.connect(url)
				.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:47.0) Gecko/20100101 Firefox/47.0")
				.referrer("http://www.google.com").timeout(10000).ignoreHttpErrors(true).followRedirects(true);
		
		try {
			response = connection.execute();
			// System.out.println("response.statusCode()");
			if (response.statusCode() == 200) {
				// OK
				// System.out.println("Connexion ok (200)");
				doc = connection.get();
			} else if (response.statusCode() == 503) {
				// SURCHARGE DU SERVEUR OU MAINTENANCE
				// System.out.println("Exception 3 : " + response.statusCode());
				setException(3);
				setExceptionMsg(String.valueOf(response.statusCode()));
			} else {
				// AUTRES ERREURS
				// System.out.println("Exception 4 : " + response.statusCode());
				setException(4);
				setExceptionMsg(String.valueOf(response.statusCode()));
			}
		} catch (SocketTimeoutException ste) {
			// System.out.println("Exception 1 : " + response.statusCode());
			setException(1);
			setExceptionMsg(ste.getMessage());
		} catch (HttpStatusException htmle) {
			setException(5);
			setExceptionMsg(htmle.getMessage());
		} catch (IOException ioe) {
			// System.out.println("Exception 2 : " + response.statusCode());
			setException(2);
			setExceptionMsg(ioe.getMessage());
		}
		// System.out.println("ConnectToUrl.getException() = " +
		// getException());
		// System.out.println("ConnectToUrl.getExceptionMsg() = " +
		// getExceptionMsg());
		
		return doc;
	}

	public String getExceptionMsg() {
		return exceptionMsg;
	}

	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}

	public int getException() {
		return exception;
	}

	public void setException(int exception) {
		this.exception = exception;
	}

}
