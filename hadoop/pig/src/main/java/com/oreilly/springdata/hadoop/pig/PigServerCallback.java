package com.oreilly.springdata.hadoop.pig;

import java.io.IOException;

import org.apache.pig.PigServer;
import org.springframework.dao.DataAccessException;

public interface PigServerCallback {

	void doInPig(PigServer pigServer) throws DataAccessException, IOException;
}
