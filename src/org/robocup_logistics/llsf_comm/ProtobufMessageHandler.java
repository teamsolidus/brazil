package org.robocup_logistics.llsf_comm;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.google.protobuf.GeneratedMessage;

/**
 * The Interface ProtobufMessageHandler is used to handle received protobuf messages. Implement
 * this interface if you want to be able to access the information in the retrieved messages.
 * The ProtobufClient/ProtobufBroadcastPeer automatically passes incoming messages to your handler
 * if you have registered it.
 * 
 * @see ProtobufClient#register_handler(ProtobufMessageHandler)
 * @see ProtobufBroadcastPeer#register_handler(ProtobufMessageHandler handler)
 */
public interface ProtobufMessageHandler {
	
	/**
	 * This method is called by the ProtobufClient/ProtobufBroadcastPeer. The GeneratedMessage
	 * passed to it is an instance of the same type as the protobuf message you received. It is
	 * used to identify the type of the protobuf message. The actual data is contained in the
	 * ByteBuffer. You can read the tutorial to find out how to handle incoming messages correctly.
	 *  
	 * @param in_msg
	 *            the ByteBuffer containing the actual data
	 * @param msg
	 *            the instance of the same type as the protobuf message you received
	 */
	public void handle_message(ByteBuffer in_msg, GeneratedMessage msg);
	public void connection_lost(IOException e);
	public void timeout();

}
