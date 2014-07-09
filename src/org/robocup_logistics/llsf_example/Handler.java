package org.robocup_logistics.llsf_example;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import org.robocup_logistics.llsf_comm.ProtobufMessageHandler;
import org.robocup_logistics.llsf_msgs.BeaconSignalProtos.BeaconSignal;
import org.robocup_logistics.llsf_msgs.GameStateProtos.GameState;
import org.robocup_logistics.llsf_msgs.MachineInfoProtos.Machine;
import org.robocup_logistics.llsf_msgs.MachineInfoProtos.MachineInfo;
import org.robocup_logistics.llsf_msgs.MachineReportProtos.MachineReportInfo;
import org.robocup_logistics.llsf_msgs.RobotInfoProtos.Robot;
import org.robocup_logistics.llsf_msgs.RobotInfoProtos.RobotInfo;
import org.robocup_logistics.llsf_msgs.TeamProtos.Team;
import org.robocup_logistics.llsf_msgs.TimeProtos.Time;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;

public class Handler implements ProtobufMessageHandler {
	
	public void handle_message(ByteBuffer in_msg, GeneratedMessage msg) {
		
		if (msg instanceof RobotInfo) {
			
			byte[] array = new byte[in_msg.capacity()];
			in_msg.rewind();
			in_msg.get(array);
			RobotInfo info;
			
			try {
				info = RobotInfo.parseFrom(array);
				int count = info.getRobotsCount();
				System.out.println("robot info:");
				System.out.println("  number of robots: " + count);
				List<Robot> robots = info.getRobotsList();
				for (int i = 0; i < robots.size(); i++) {
					Robot robot = robots.get(i);
					String name = robot.getName();
					String team = robot.getTeam();
					int number = robot.getNumber();
					System.out.println("    robot #" + number + ": " + name + " - " + team);
				}
				
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			
		} else if (msg instanceof BeaconSignal) {

			byte[] array = new byte[in_msg.capacity()];
			in_msg.rewind();
			in_msg.get(array);
			BeaconSignal bs;
			Time t;
			
			try {
				bs = BeaconSignal.parseFrom(array);
				t = bs.getTime();
				System.out.println("beacon signal:");
				System.out.println("  sec: " + t.getSec() + ", nanosec: " + t.getNsec());
				System.out.println("  name: " + bs.getPeerName() + " " + bs.getTeamName());
				
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			
		} else if (msg instanceof GameState) {
			
			byte[] array = new byte[in_msg.capacity()];
			in_msg.rewind();
			in_msg.get(array);
			GameState state;
			
			try {
				state = GameState.parseFrom(array);
				User.gameStateReceived(state);
				
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			
		} else if (msg instanceof MachineInfo) {
			
			byte[] array = new byte[in_msg.capacity()];
			in_msg.rewind();
			in_msg.get(array);
			MachineInfo info;
			
			try {
				info = MachineInfo.parseFrom(array);
				int count = info.getMachinesCount();
				System.out.println("machine info:");
				System.out.println("  number of machines: " + count);
				List<Machine> machines = info.getMachinesList();
				for (int i = 0; i < machines.size(); i++) {
					Machine machine = machines.get(i);
					String name = machine.getName();
					String type = machine.getType();
					Team color = machine.getTeamColor();
					System.out.println("    machine " + name + ": " + type + " - " + color.toString());
				}
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			
		} else if (msg instanceof MachineReportInfo) {
			
			byte[] array = new byte[in_msg.capacity()];
			in_msg.rewind();
			in_msg.get(array);
			MachineReportInfo info;
			
			try {
				info = MachineReportInfo.parseFrom(array);
				Team t = info.getTeamColor();
				System.out.println("machine report info:");
				System.out.println("  team color: " + t.toString());
				int count = info.getReportedMachinesCount();
				System.out.println("  number of reported machines: " + count);
				List<String> machines = info.getReportedMachinesList();
				for (int i = 0; i < machines.size(); i++) {
					String machine = machines.get(i);
					System.out.println("    machine " + machine);
				}
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public void connection_lost(IOException e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void timeout() {
		// TODO Auto-generated method stub	
	}
}
