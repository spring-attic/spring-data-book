package com.oreilly.springdata.hadoop.streaming;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ControlBusController {
	
	@Autowired
	private MessageChannel inOperationChannel;	
	
	@RequestMapping("/admin")
	public @ResponseBody String simple(@RequestBody String message) {
		
		Message<String> operation = MessageBuilder.withPayload(message).build();
		MessagingTemplate template = new MessagingTemplate();
		Message response = template.sendAndReceive(inOperationChannel, operation);

		if (response != null) {
			return response.getPayload().toString();
		} else {
			return null;
		}
	}
}
