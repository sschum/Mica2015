package de.tarent.mica.maze.util;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.raysha.lib.jsimpleshell.util.ColoredStringBuilder;

public class LogFormat {

	private static final Pattern PATTERN = Pattern.compile("(([a-zA-Z])(\\{[0-9]{1,}\\}))");
	public static String format(String pattern, Object...args){
		Matcher m = PATTERN.matcher(pattern);
		String newPattern = pattern;

		while(m.find()){
			final String replacement = m.group(1);
			final String colorCode = m.group(2);
			final String parameter = m.group(3);

			final String newValue;
			switch(colorCode){
			case "n": newValue = new ColoredStringBuilder().black(parameter).build(); break;
			case "N": newValue = new ColoredStringBuilder().blackBG(parameter).build(); break;
			case "b": newValue = new ColoredStringBuilder().blue(parameter).build(); break;
			case "B": newValue = new ColoredStringBuilder().blueBG(parameter).build(); break;
			case "c": newValue = new ColoredStringBuilder().cyan(parameter).build(); break;
			case "C": newValue = new ColoredStringBuilder().cyanBG(parameter).build(); break;
			case "g": newValue = new ColoredStringBuilder().green(parameter).build(); break;
			case "G": newValue = new ColoredStringBuilder().greenBG(parameter).build(); break;
			case "m": newValue = new ColoredStringBuilder().magenta(parameter).build(); break;
			case "M": newValue = new ColoredStringBuilder().magentaBG(parameter).build(); break;
			case "r": newValue = new ColoredStringBuilder().red(parameter).build(); break;
			case "R": newValue = new ColoredStringBuilder().redBG(parameter).build(); break;
			case "w": newValue = new ColoredStringBuilder().white(parameter).build(); break;
			case "W": newValue = new ColoredStringBuilder().whiteBG(parameter).build(); break;
			case "y": newValue = new ColoredStringBuilder().yellow(parameter).build(); break;
			case "Y": newValue = new ColoredStringBuilder().yellowBG(parameter).build(); break;
			default:
				newValue = replacement;
			}

			newPattern = newPattern.replace(replacement, newValue);
		}

		return MessageFormat.format(newPattern, args);
	}
}
