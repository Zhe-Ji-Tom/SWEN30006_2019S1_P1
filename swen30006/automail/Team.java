package automail;

import exceptions.ItemTooHeavyException;
import strategies.Automail;

/**
 * The team of robots may contain two or three robots to delivery heavier mail items
 */
public class Team extends SuperRobot{
	protected SuperRobot[] teamOfRobots;
	private int time;
	
	public Team(SuperRobot[] teamOfRobots) {
		delivery = teamOfRobots[0].delivery;
		this.teamOfRobots=teamOfRobots;
		current_state = RobotState.DELIVERING;
		current_floor = Building.MAILROOM_LOCATION;
		id = "";
		time = 1;
		String out = "";
		for(int i=0;i<teamOfRobots.length;i++) {
			id = id + teamOfRobots[i].id;
			out = out + teamOfRobots[i].id + " ";
		}
		System.out.printf("T: %3d > %7sare a team.\n", Clock.Time(), out);
		
	}
	
	// The speed of team is one third of individual robot
	protected void moveTowards(int destination) {
		if(time==4) {
			time = 1;
			current_floor++;
		}
		
		if(time==1) {
			for(int i=0;i<teamOfRobots.length;i++) {
				teamOfRobots[i].current_floor++;
			}
		}
		
		time++;
    }
	
	/**
     * Prints out the change in state
     * @param nextState the state to which the robot is transitioning
     */
	protected void changeState(RobotState nextState){
    	for(int i=0;i<teamOfRobots.length;i++) {
    		teamOfRobots[i].changeState(nextState);
    	}
    	current_state = nextState;
    	if(nextState==RobotState.RETURNING) {
    		releaseRobots();
    	}
    }
	
	// Add the mail item to the team and execute the same operation to every robots of the team
	public void addToHand(MailItem mailItem) throws ItemTooHeavyException {
		deliveryItem = mailItem;
		destination_floor = mailItem.destination_floor;
		for(int i=0;i<teamOfRobots.length;i++) {
    		teamOfRobots[i].addToHand(mailItem);
    	}
	}
	
	// Dispatch the team from mail pool and execute the same operation to the every robots of the team
	public void dispatch() {
		for(int i=0;i<teamOfRobots.length;i++) {
    		teamOfRobots[i].dispatch();
    	}
    }
	
	// When the mail item is delivered, the team will release all robots and add robots to the list of robot 
	private void releaseRobots() {
		for(SuperRobot robot : teamOfRobots) {
			robot.deliveryItem = null;
			Automail.getInstance().robots[robot.hashCode()] = robot;
		}
	}
	
}
