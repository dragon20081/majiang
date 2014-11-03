package business.conut;

public class Sts_MuteBlacklist {
	
	private int id;
	private String nick;
	private int muteDays;
	private int blackDays;
	private boolean black;
	private boolean mute;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public int getMuteDays() {
		return muteDays;
	}
	public void setMuteDays(int muteDays) {
		this.muteDays = muteDays;
	}
	public boolean isBlack() {
		return black;
	}
	public void setBlack(boolean black) {
		this.black = black;
	}
	public int getBlackDays() {
		return blackDays;
	}
	public void setBlackDays(int blackDays) {
		this.blackDays = blackDays;
	}
	public boolean isMute() {
		return mute;
	}
	public void setMute(boolean mute) {
		this.mute = mute;
	}
}
