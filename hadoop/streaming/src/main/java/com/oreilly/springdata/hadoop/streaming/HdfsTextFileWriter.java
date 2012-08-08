package com.oreilly.springdata.hadoop.streaming;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHandlingException;
import org.springframework.util.Assert;

public class HdfsTextFileWriter extends AbstractHdfsWriter implements HdfsWriter {

	private FileSystem fileSystem;
	private FSDataOutputStream fsDataOutputStream;

	private volatile String charset = "UTF-8";
	
	public HdfsTextFileWriter(FileSystem fileSystem) {
		Assert.notNull(fileSystem, "Hadoop FileSystem must not be null.");
		this.fileSystem = fileSystem;
	}
	

	@Override
	public void write(Message<?> message) throws IOException {
		prepareOutputStream();
		copy(getPayloadAsBytes(message), this.fsDataOutputStream);
	}
	
	private void prepareOutputStream() throws IOException {
		boolean found = false;
		Path name = null;
		
		//TODO improve algorithm to be less chatty.
		while (!found) {
			name = new Path(getPathName());
			// If it doesn't exist, create it.  If it exists, return false
			if (fileSystem.createNewFile(name)) {	
				found = true;
				this.fsDataOutputStream = this.fileSystem.append(name);
			}
			else {
				//TODO keep track of bytes written ourselves.
				if (fileSystem.getLength(name) >= getRolloverThresholdInBytes()) {
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
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void copy(byte[] in, OutputStream out) throws IOException {
		Assert.notNull(in, "No input byte array specified");
		Assert.notNull(out, "No OutputStream specified");
		out.write(in);	
		out.flush();
	}

	//TODO from TcpMessageMapper
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

	/*
	public void createOutputStream() throws IOException {
		Path path = new Path(getPathName());
		//TODO logic to see if file already exists....allow for overwrite of file
		//TODO support overloaded options, buffersize, replication..
		this.fsDataOutputStream = this.fileSystem.create(path);
	}*/


}
