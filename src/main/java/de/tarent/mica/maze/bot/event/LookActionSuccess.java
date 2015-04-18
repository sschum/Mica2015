package de.tarent.mica.maze.bot.event;

import java.util.List;

public class LookActionSuccess extends ActionSuccess {

	private List<Field> fields;

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public static class Field {
		private boolean isWall;
		private boolean hasLeftBranch;
		private boolean hasRightBranch;
		private Integer buttonNumber;

		public Field() {
			this(false, false, null);
			this.isWall = true;
		}

		public Field(boolean hasLeftBranch, boolean hasRightBranch, Integer buttonNumber) {
			this.isWall = false;
			this.hasLeftBranch = hasLeftBranch;
			this.hasRightBranch = hasRightBranch;
			this.buttonNumber = buttonNumber;
		}

		public boolean isWall() {
			return isWall;
		}
		public boolean hasLeftBranch() {
			return hasLeftBranch;
		}
		public boolean hasRightBranch() {
			return hasRightBranch;
		}
		public Integer getButtonNumber() {
			return buttonNumber;
		}
	}
}
