/**
 * T4
 * @author BlackPearl & HeavenVolkoff & ykane
 */

public class Keys {
	private Boolean onAction; //Defines if it is a Long-Press ou Single-Press Key
	private String actionName;
	private int id; //Key ID Code
	private long startTime; //Long-Press Counter
	private int repeatTime; //Long-Press Repeat Time

    //=========================== Constructors =====================//
    public Keys(){} //empty serialization constructor

	public Keys(String actionName, int id, int repeatTime){
		this.onAction = false;
		this.actionName = actionName;
		this.id = id;
		this.startTime = repeatTime;
		this.repeatTime = repeatTime;
	} //Long-Press Constructor

	public Keys(String actionName, int id){
		this.onAction = true;
		this.actionName = actionName;
		this.id = id;
		this.startTime = 0;
		this.repeatTime = 0;
	} //Single-Press Constructor
    //==============================================================//

	public boolean getOnAction() {
		return onAction;
	}

	public String getActionName() {
		return actionName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public int getRepeatTime() {
		return repeatTime;
	}

	public void setRepeatTime(int repeatTime) {
		this.repeatTime = repeatTime;
	}
}
