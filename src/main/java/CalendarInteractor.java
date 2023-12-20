import com.clt.script.exp.values.StructValue;


public class CalendarInteractor {
	public boolean newEvent(StructValue args) {
		String title = args.getValue("title").toString().replace("\"", "");
		String occurence = args.getValue("occurence").toString().replace("\"", "");
		String description = args.getValue("description").toString().replace("\"", "");
		String place = args.getValue("place").toString().replace("\"", "");
		boolean result = false;
		
		System.out.println("Creating event '" + title + "' (" + description + ") at '" + place + "'.\n" + occurence);
		
		// Create the event
		
		if (result == true) {
			System.out.println("Created event successfully");
		} else {
			System.out.println("Couldn't create new calendar event");
		}
		return result;
	}
} 