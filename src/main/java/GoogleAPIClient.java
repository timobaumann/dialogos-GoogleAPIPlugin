import java.io.IOException;

import com.clt.dialog.client.Client;
import com.clt.dialog.client.ConnectionState;
import com.clt.script.exp.Value;
import com.clt.script.exp.values.StringValue;
import com.clt.script.exp.values.StructValue;

public class GoogleAPIClient extends Client {
	CalendarInteractor calendarInteractor;
	SpeechRecognizer recognizer;
	Translator translator;
	JsonReader jsonReader;
	
	public GoogleAPIClient() { 	    
	    calendarInteractor = new CalendarInteractor();
	    recognizer = new SpeechRecognizer();
	    translator = new Translator();
	    jsonReader = new JsonReader();
	}

	@Override
	public void error(Throwable arg0) { }

	@Override
	public String getName() {
		return "GoogleAPI";
	}

	
	@Override
	public void output(Value arg0) {
		if (arg0 instanceof StructValue) {
			StructValue struct = (StructValue) arg0;
			String command = struct.getValue("command").toString();
			StructValue args = (StructValue) struct.getValue("args");
			Value result = null;
			
			System.out.println("Received command " + command);
			
			// Actually differentiate the commands
			if (command.equals("\"recognize\"")) {
				System.out.println("Recognizing speech input...");
				result = StringValue.valueOf(recognizer.recognize(args));
			}
			else if (command.equals("\"new event\"")) {
				System.out.println("Creating new calendar event");
				result = StringValue.valueOf(String.valueOf(calendarInteractor.newEvent(args)));
			}
			else if (command.equals("\"translate\"")) {
				System.out.println("Translating...");
				result = StringValue.valueOf(translator.translate(args));
			}
			else if (command.equals("\"parseJson\"")) {
				System.out.println("Reading file and parsing JSON to struct...");
				result = jsonReader.readAndParse(args);
			}
			
			try {
				send(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public void reset() { }

	@Override
	public void sessionStarted() {	
		System.out.println("session started");
	}

	@Override
	public void stateChanged(ConnectionState arg0) { }

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Client cl = new GoogleAPIClient();
		cl.open();
		System.out.println("main isch over.");
		//cl.output(null);
	}

}
