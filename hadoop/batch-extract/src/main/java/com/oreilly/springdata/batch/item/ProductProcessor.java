package com.oreilly.springdata.batch.item;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.transform.FieldSet;

public class ProductProcessor implements ItemProcessor<FieldSet, FieldSet> {

	@Override
	public FieldSet process(FieldSet item) throws Exception {
		String id = item.readString("ID");
		if (id.startsWith("PR1")) {
			return null;
		} else {
			return item;
		}
	}

}
