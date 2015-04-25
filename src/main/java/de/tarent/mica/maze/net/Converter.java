package de.tarent.mica.maze.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import de.tarent.mica.maze.bot.action.Action;
import de.tarent.mica.maze.bot.action.Drop;
import de.tarent.mica.maze.bot.action.Get;
import de.tarent.mica.maze.bot.action.Look;
import de.tarent.mica.maze.bot.action.Push;
import de.tarent.mica.maze.bot.action.StartGame;
import de.tarent.mica.maze.bot.action.TurnLeft;
import de.tarent.mica.maze.bot.action.TurnRight;
import de.tarent.mica.maze.bot.action.Walk;
import de.tarent.mica.maze.bot.event.ActionFail;
import de.tarent.mica.maze.bot.event.ActionSuccess;
import de.tarent.mica.maze.bot.event.ButtonActionSuccess;
import de.tarent.mica.maze.bot.event.Event;
import de.tarent.mica.maze.bot.event.LookActionSuccess;
import de.tarent.mica.maze.bot.event.LookActionSuccess.Field;

public class Converter {
	private static String MESSAGE_KEY_ACTION = "action";
	private static String MESSAGE_KEY_NAME = "name";
	private static String MESSAGE_KEY_MAZE = "maze";

	private static String MESSAGE_KEY_RESULT = "result";
	private static String MESSAGE_KEY_MESSAGE = "message";
	private static String MESSAGE_KEY_BUTTON = "button";

	private ObjectMapper mapper = new ObjectMapper();

	private static Converter instance;
	private Converter(){

	}

	public static final Converter getInstance(){
		if(instance == null){
			instance = new Converter();
		}
		return instance;
	}

	public String convertToMessage(Action action) throws IOException{
		Map<String, String> raw = new HashMap<String, String>();

		final String sAction;

		if(action instanceof Drop){
			sAction = "drop";
		}else if(action instanceof Get){
			sAction = "get";
		}else if(action instanceof Look){
			sAction = "look";
		}else if(action instanceof Push){
			sAction = "push";
		}else if(action instanceof TurnLeft){
			sAction = "left";
		}else if(action instanceof TurnRight){
			sAction = "right";
		}else if(action instanceof Walk){
			sAction = "walk";
		}else{
			sAction = null;

			if(action instanceof StartGame){
				raw.put(MESSAGE_KEY_NAME, ((StartGame)action).getPlayerName());
				raw.put(MESSAGE_KEY_MAZE, "");
			}
		}

		if(sAction != null){
			raw.put(MESSAGE_KEY_ACTION, sAction);
		}

		final String message = mapper.writeValueAsString(raw);
		return message;
	}

	public Event convertToEvent(String message) throws IOException {
		final Map<String, String> raw = mapper.readValue(message,
				new TypeReference<Map<String,String>>(){});

		if(raw.containsKey(MESSAGE_KEY_RESULT)){
			Event result = null;
			if(raw.get(MESSAGE_KEY_RESULT).equalsIgnoreCase("ok")){
				if(raw.containsKey(MESSAGE_KEY_BUTTON)){
					result = buildButtonActionSuccess(raw);
				}else if(raw.containsKey("1")){
					result = buildLookActionSuccess(raw);
				}else{
					result = new ActionSuccess();
				}
			}else if(raw.get(MESSAGE_KEY_RESULT).equalsIgnoreCase("fail")){
				result = new ActionFail();
			}else{
				throw new IllegalArgumentException("Unkown message!");
			}

			result.setMessage(raw.get(MESSAGE_KEY_MESSAGE));
			return result;
		}

		throw new IllegalArgumentException("Unkown message!");
	}

	private ButtonActionSuccess buildButtonActionSuccess(final Map<String, String> raw) {
		ButtonActionSuccess result = new ButtonActionSuccess();

		try{
			result.setButtonNumber(Integer.parseInt(raw.get(MESSAGE_KEY_BUTTON)));
		}catch(NumberFormatException e){}

		return result;
	}

	private Pattern lookButtonNumberPattern = Pattern.compile(".*([0-9]{1,}).*");
	private static final int MAX_LOOK_RANGE = 5;
	private LookActionSuccess buildLookActionSuccess(final Map<String, String> raw) {
		LookActionSuccess result = new LookActionSuccess();
		result.setFields(new ArrayList<Field>());

		for(int i=1; i <= MAX_LOOK_RANGE; i++){
			if(raw.containsKey(String.valueOf(i))){
				final String fieldRaw = raw.get(String.valueOf(i)).toLowerCase();
				Matcher buttonNumberMatcher = lookButtonNumberPattern.matcher(fieldRaw);

				boolean isWall = fieldRaw.contains("#");
				Field field;

				if(isWall){
					field = new Field();
				}else{
					boolean right = fieldRaw.contains("l");
					boolean left = fieldRaw.contains("r");
					Integer button = buttonNumberMatcher.matches() ? Integer.parseInt(buttonNumberMatcher.group(1)) : null;

					field = new Field(
							right,
							left,
							button);
				}

				result.getFields().add(i - 1, field);
			}
		}

		return result;
	}
}
