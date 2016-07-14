package plugin.pdfextractor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class DownloadFile {
	public String downloadFile(String webAddress, String saveTo) throws IOException{
		
		try {
			URL website = new URL(webAddress);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(saveTo);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
			return saveTo;
		} catch (MalformedURLException e) {
			return webAddress;
		}
	}
		
}
