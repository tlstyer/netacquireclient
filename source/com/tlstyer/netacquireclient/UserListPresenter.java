import java.util.*;
import java.util.regex.*;

class ListEntry {
	public int gameNumber;
	public String message;
	public String messageLowercase;

	public ListEntry(int gameNumber_, String message_) {
		gameNumber = gameNumber_;
		message = message_;
		messageLowercase = message_.toLowerCase();
	}
}

class ComparatorListEntryAlphabetically implements Comparator<ListEntry> {
	 public int compare(ListEntry le1, ListEntry le2) {
		 return le1.messageLowercase.compareTo(le2.messageLowercase);
	 }
}

class ComparatorListEntryGameNumber implements Comparator<ListEntry> {
	 public int compare(ListEntry le1, ListEntry le2) {
		 int diff;
		 diff = le1.gameNumber - le2.gameNumber;
		 if (diff != 0) {
			 return diff;
		 }
		 return le1.messageLowercase.compareTo(le2.messageLowercase);
	 }
}

public class UserListPresenter {
	private boolean processingList;

	private String messageStart;
	private ArrayList<ListEntry> listEntries = new ArrayList<ListEntry>();

    private static final Pattern patternListStart = Pattern.compile("\\A# Users: \\d*? \\.\\.\\.\\z");
    private static final Pattern patternListEntry = Pattern.compile("\\A# -> (.+?) (in lobby|\\(playing in game #(\\d*?)\\)|\\(watching game #(\\d*?)\\)) \\.\\.\\.\\z");
	private static final String stringListEnd = "# End of user list.";
	private static final ComparatorListEntryGameNumber comparatorListEntryGameNumber = new ComparatorListEntryGameNumber();
	private static final ComparatorListEntryAlphabetically comparatorListEntryAlphabetically = new ComparatorListEntryAlphabetically();

	public UserListPresenter() {
		init();
	}

	public void init() {
		processingList = false;
		messageStart = null;
		listEntries.clear();
	}

	public static final int DO_AS_USUAL = 0;
	public static final int DONT_OUTPUT_LINE = 1;
	public static final int READY_TO_OUTPUT_LINES = 2;

	public int processMessage(String message) {
		if (!processingList) {
			Matcher matcher = patternListStart.matcher(message);
			if (matcher.find()) {
				processingList = true;
				messageStart = message;
				listEntries.clear();
				return DONT_OUTPUT_LINE;
			} else {
				return DO_AS_USUAL;
			}
		} else {
			Matcher matcher = patternListEntry.matcher(message);
			if (matcher.find()) {
				Integer gameNumber;
				if (matcher.groupCount() >= 3) {
					try {
						gameNumber = Integer.decode(matcher.group(3));
					} catch (Exception e) {
						gameNumber = Integer.MAX_VALUE;
					}
				} else {
					gameNumber = Integer.MAX_VALUE;
				}
				listEntries.add(new ListEntry(gameNumber, message));
				return DONT_OUTPUT_LINE;
			} else if (message.equals(stringListEnd)) {
				processingList = false;
				return READY_TO_OUTPUT_LINES;
			} else {
				return DO_AS_USUAL;
			}
		}
	}

	public static final int SORT_NONE = 0;
	public static final int SORT_ALPHABETICALLY = 1;
	public static final int SORT_GAME_NUMBER = 2;
	public static final int SORT_END = 2;

	public void outputLines() {
		Object[] listEntriesArray = this.listEntries.toArray();
		ListEntry[] listEntries = new ListEntry[listEntriesArray.length];
		for (int i=0; i<listEntriesArray.length; ++i) {
			listEntries[i] = (ListEntry)listEntriesArray[i];
		}
		
		int sortingMethod = SerializedData.getSerializedData().getUserListSortingMethod();
		if (sortingMethod == SORT_ALPHABETICALLY) {
			Arrays.sort(listEntries, comparatorListEntryAlphabetically);
		} else if (sortingMethod == SORT_GAME_NUMBER) {
			Arrays.sort(listEntries, comparatorListEntryGameNumber);
		}

        MessageWindow lobby = Main.getMainFrame().lobby;
		lobby.append(messageStart, MessageWindow.APPEND_DEFAULT);
		for (ListEntry listEntry : listEntries) {
			lobby.append(listEntry.message, MessageWindow.APPEND_DEFAULT);
		}
		lobby.append(stringListEnd, MessageWindow.APPEND_DEFAULT);
	}
}
