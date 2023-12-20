import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;

import com.clt.script.exp.Value;
import com.clt.script.exp.values.ListValue;
import com.clt.script.exp.values.StringValue;
import com.clt.script.exp.values.StructValue;
import com.clt.script.exp.values.BoolValue;
import com.clt.script.exp.values.IntValue;

import org.json.*;


public class JsonReader {
	
	public static StructValue readAndParse(StructValue args) {
		String path = args.getValue("path").toString().replace("\"", "");
		String fileText;
		
		 try {
			fileText = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.err.println("Couldn't read file from given path");
			return new StructValue(new String[0], new StringValue[0]);
		}
		 
		JSONObject fileJson = new JSONObject(fileText);
		StructValue struct = (StructValue) parse(fileJson);
		System.out.println("Decoded to" + struct.toString());
		
		return struct;
	}
	
	private static Value parse(Object obj) {		
		if (obj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject) obj;
			Object[] keys = jsonObj.keySet().toArray();
			String[] stringKeys = new String[keys.length];
			Value[] values = new Value[jsonObj.length()];
			
			for (int i = 0; i < jsonObj.length(); i++) {
				values[i] = parse(jsonObj.get(keys[i].toString()));
				stringKeys[i] = keys[i].toString();
			}
			
			return new StructValue(stringKeys, values);
		}
		
		else if (obj instanceof JSONArray) {
			JSONArray jsonArr = (JSONArray) obj;
			Value[] values = new Value[jsonArr.length()];
			
			for (int i = 0; i < jsonArr.length(); i++) {
				values[i] = parse(jsonArr.get(i));
			}
			
			return new ListValue(values);
		}
		
		else if (obj instanceof String) {
			return new StringValue((String) obj);
		}
		
		else if (obj instanceof Boolean) {
			return new BoolValue((Boolean) obj);
		}
		
		else if (obj instanceof Integer) {
			return new IntValue((Integer) obj);
		}
		
		else {
			System.err.println("Unknown type passed to parse");
			return null;
		}
	}
	
}
