package com.oreilly.springdata.hadoop.streaming;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.springframework.data.hadoop.fs.FsShell;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHandlingException;
import org.springframework.util.Assert;

public class HdfsTextFileWriter extends AbstractHdfsWriter implements HdfsWriter {

	private FileSystem fileSystem;
	private FSDataOutputStream fsDataOutputStream;
	private FsShell fsShell;
	private volatile boolean initialized;

	private volatile String charset = "UTF-8";
	
	public HdfsTextFileWriter(FileSystem fileSystem) {
		Assert.notNull(fileSystem, "Hadoop FileSystem must not be null.");
		this.fileSystem = fileSystem;
		fsShell = new FsShell(fileSystem.getConf(), this.fileSystem);
	}
	

	@Override
	public void write(Message<?> message) throws IOException {
		initializeCounterIfNecessary();
		prepareOutputStream();
		copy(getPayloadAsBytes(message), this.fsDataOutputStream);
	}
	
	private void initializeCounterIfNecessary() {
		if (!initialized) {
			int maxCounter = 0;
			Collection<FileStatus> fileStats = fsShell.ls(this.getBasePath());
			for (FileStatus fileStatus : fileStats) {
				String shortName = fileStatus.getPath().getName();
				int counterFromName = getCounterFromName(shortName);
				if (counterFromName > maxCounter) {
					maxCounter = counterFromName;
				}
			}
			if (maxCounter != 0) {
				this.setCounter(maxCounter+1);
			}
			initialized = true;
		}
	}


	private int getCounterFromName(String shortName) {
		Pattern pattern = Pattern.compile("([\\d+]{1,})");
		Matcher matcher = pattern.matcher(shortName);
		if (matcher.find()) {
			return Integer.parseInt(matcher.group());
		} 
		return 0;			
	}


	private void prepareOutputStream() throws IOException {
		boolean found = false;
		Path name = null;
		
		//TODO improve algorithm
		while (!found) {
			name = new Path(getFileName());
			// If it doesn't exist, create it.  If it exists, return false
			if (fileSystem.createNewFile(name)) {	
				found = true;
				this.resetBytesWritten();
				this.fsDataOutputStream = this.fileSystem.append(name);
			}
			else {
				if (this.getBytesWritten() >= getRolloverThresholdInBytes()) {
					close();
					incrementCounter();
				}
				else {
					found = true;
				}
			}
		}
	}
	
	/**
	 * Simple not optimized copy
	 */
	public void copy(byte[] in, FSDataOutputStream out) throws IOException {
		Assert.notNull(in, "No input byte array specified");
		Assert.notNull(out, "No OutputStream specified");
		out.write(in);	
		incrementBytesWritten(in.length);
	}

	//TODO note, taken from TcpMessageMapper
	/**
	 * Extracts the payload as a byte array.  
	 * @param message
	 * @return
	 */
	private byte[] getPayloadAsBytes(Message<?> message) {
		byte[] bytes = null;
		Object payload = message.getPayload();
		if (payload instanceof byte[]) {
			bytes = (byte[]) payload;
		}
		else if (payload instanceof String) {
			try {
				bytes = ((String) payload).getBytes(this.charset);
			}
			catch (UnsupportedEncodingException e) {
				throw new MessageHandlingException(message, e);
			}
		}
		else {
			throw new MessageHandlingException(message,
					"HdfsTextFileWriter expects " +
					"either a byte array or String payload, but received: " + payload.getClass());
		}
		return bytes;
	}

	@Override
	public void close() {
		IOUtils.closeStream(fsDataOutputStream);
	}

}
