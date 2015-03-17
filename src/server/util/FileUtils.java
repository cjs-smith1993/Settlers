package server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {

	private static final Map<String, String> MIMETYPES = makeMimeTypes();

	private static Map<String, String> makeMimeTypes() {
		Map<String, String> mimeTypes = new HashMap<String, String>();
		mimeTypes.put(".js", "application/javascript");
		mimeTypes.put(".css", "text/css");
		mimeTypes.put(".html", "text/html");

		return mimeTypes;
	}

	public static byte[] readFile(String path) throws IOException {
		File file = new File(path);
		int size = (int) file.length();
		byte[] contents = new byte[size];
		FileInputStream in = new FileInputStream(file);
		in.read(contents);
		in.close();

		return contents;
	}

	public static String getMimeType(String path) {
		String ending = path.substring(path.lastIndexOf('.'), path.length());
		if (MIMETYPES.get(ending) != null) {
			return MIMETYPES.get(ending);
		}
		else {
			return "";
		}
	}
}
