package net.loomchild.segment.srx.legacy;

import com.google.re2j.Matcher;
import com.google.re2j.Pattern;

/**
 * Represents {@link MatchResult} that uses {@link ReaderCharSequence} as
 * a text - it takes care of exceptions that are thrown by it. 
 * @author loomchild
 */
public class ReaderMatcher {
	
	private Matcher matcher;
	
	private CharSequence text;
	
	private int oldLength;

	private boolean found;
	
	public ReaderMatcher(Pattern pattern, CharSequence text) {
		this.text = text;
		this.oldLength = text.length();
		this.matcher = pattern.matcher(text);
		this.found = true;
	}

	public void appendReplacement(StringBuffer sb, String replacement) {
		matcher.appendReplacement(sb, replacement);
	}
	
	public StringBuffer appendTail(StringBuffer sb) {
		return matcher.appendTail(sb);
	}

	public int end() {
		return matcher.end();
	}

	public int end(int group) {
		return matcher.end(group);
	}
	
	public boolean find() {
		found = false;
		int end = getEnd();
		try {
			found = matcher.find();
		} catch (IndexOutOfBoundsException e) {
		}
		if (lengthChanged()) {
			int regionStart = Math.max(end, matcher.regionStart());
			int regionEnd = Math.min(text.length(), matcher.regionEnd());
			matcher.reset(text);
			matcher.region(regionStart, regionEnd);
			found = matcher.find();
		}
		return found;
	}

	public boolean find(int start) {
		found = false;
		try {
			found = matcher.find(start);
		} catch (IndexOutOfBoundsException e) {
		}
		if (lengthChanged()) {
			int regionStart = matcher.regionStart();
			int regionEnd = Math.min(text.length(), matcher.regionEnd());
			matcher.reset(text);
			matcher.region(regionStart, regionEnd);
			found = matcher.find(start);
		}
		return found;
	}

	public String group() {
		return matcher.group();
	}

	public String group(int group) {
		return matcher.group(group);
	}

	public int groupCount() {
		return matcher.groupCount();
	}
	
	public boolean hitEnd() {
		return !found;
	}
	
	public boolean lookingAt() {
		boolean result = false;
		try {
			result = matcher.lookingAt();
		} catch (IndexOutOfBoundsException e) {
		}
		if (lengthChanged()) {
			int regionStart = matcher.regionStart();
			int regionEnd = Math.min(text.length(), matcher.regionEnd());
			matcher.reset(text);
			matcher.region(regionStart, regionEnd);
			result = matcher.lookingAt();
		}
		return result;
	}

	public boolean matches() {
		boolean result = false;
		try {
			result = matcher.matches();
		} catch (IndexOutOfBoundsException e) {
		}
		if (lengthChanged()) {
			int regionStart = matcher.regionStart();
			int regionEnd = Math.min(text.length(), matcher.regionEnd());
			matcher.reset(text);
			matcher.region(regionStart, regionEnd);
			result = matcher.matches();
		}
		return result;
	}

	public Pattern pattern() {
		return matcher.pattern();
	}
	
	public void region(int start, int end) {
		matcher.region(start, end);
	}
	
	public int regionEnd() {
		return matcher.regionEnd();
	}

	public int regionStart() {
		return matcher.regionStart();
	}
	
	public String replaceAll(String replacement) {
		String result = null;
		try {
			result = matcher.replaceAll(replacement);
		} catch (IndexOutOfBoundsException e) {
		}
		if (lengthChanged()) {
			// No need to set region because replaceAll resets matcher first.
			matcher.reset(text);
			result = matcher.replaceAll(replacement);
		}
		return result;
	}

	public String replaceFirst(String replacement) {
		String result = null;
		try {
			result = matcher.replaceFirst(replacement);
		} catch (IndexOutOfBoundsException e) {
		}
		if (lengthChanged()) {
			matcher.reset(text);
			// No need to set region because replaceFirst resets matcher first.
			result = matcher.replaceFirst(replacement);
		}
		return result;
	}
	
	public void reset() {
		matcher.reset();
	}

	public void reset(CharSequence input) {
		matcher.reset(input);
	}
	
	public int start() {
		return matcher.start();
	}

	public int start(int group) {
		return matcher.start(group);
	}

	public String toString() {
		return "ReaderMatcher: " + matcher.toString();
	}
	
	private int getEnd() {
		try {
			return matcher.end();
		} catch (IllegalStateException e) {
			return 0;
		}
	}

	private boolean lengthChanged() {
		if (text.length() < oldLength) {
			oldLength = text.length();
			return true;
		} else {
			return false;
		}
	}
	
}
