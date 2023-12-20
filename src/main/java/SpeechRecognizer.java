import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

import net.sourceforge.javaflacencoder.FLACFileWriter;

import com.clt.script.exp.values.StructValue;
import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GoogleResponse;
import com.darkprograms.speech.recognizer.Recognizer;


public class SpeechRecognizer {
	Microphone mic;
	File file;
	
	public SpeechRecognizer() {
		 Mixer.Info[] infoArray = AudioSystem.getMixerInfo();
	     for(Mixer.Info info : infoArray) {
	        System.out.println("info: " + info.toString());
	     } 

	    mic = new Microphone(FLACFileWriter.FLAC);
	    //file = File.createTempFile("whatever", "lala");
	    file = new File("test.flac");
	}
	
	/**
	 * call Google for recognition
	 * Value should be a Struct with the following structure:
	 * { lang = de-DE | en-US | en-GB | ... (default: de-DE)
	 *   timeout = integer value in milliseconds (default: 5000)
	 * }
	 * sends back the recognized words as StringValue
	 */
	public String recognize(StructValue args) {
		String googleErgebnis = "";
		
		// Argument default values
		String lang = "de-DE";
		int timeout = 5000;
		
		// Argument overrides
		if (args.containsLabel("lang")) {
			System.out.println("Setting lang to " + args.getValue("lang").toString());
			lang = args.getValue("lang").toString();
		}
		if (args.containsLabel("timeout")) {
			System.out.println("Setting timeout to " + args.getValue("timeout").toString());
			timeout = Integer.valueOf(args.getValue("timeout").toString());
		}
		
		
	    try {
	        mic.captureAudioToFile (file);
	      } catch (Exception ex) {
	        //Microphone not available or some other error.
	        System.out.println ("ERROR: Microphone is not availible.");
	        ex.printStackTrace ();
	      }

	      /* User records the voice here. Microphone starts a separate thread so do whatever you want
	       * in the mean time. Show a recording icon or whatever.
	       */
	      try {
	        System.out.println ("Recording...");
	        Thread.sleep (timeout);	//In our case, we'll just wait 5 seconds.
	        mic.close();
	      } catch (InterruptedException ex) {
	        ex.printStackTrace ();
	      }

	      mic.close ();		//Ends recording and frees the resources
	      System.out.println ("Recording stopped.");

	      Recognizer recognizer = new Recognizer(lang, System.getProperty("google-api-key", System.getenv("GOOGLE_API_KEY")));
	      //Although auto-detect is available, it is recommended you select your region for added accuracy.
	      try { 
	        int maxNumOfResponses = 4;
	        System.out.println("Sample rate is: " + (int) mic.getAudioFormat().getSampleRate());
	        GoogleResponse response = recognizer.getRecognizedDataForFlac (file, maxNumOfResponses, (int) mic.getAudioFormat().getSampleRate ());
	        if (response != null && response.getResponse() != null) {
		        googleErgebnis = response.getResponse();
		        System.out.println ("Google Response: " + response.getResponse ());
		        System.out.println ("Google is " + Double.parseDouble (response.getConfidence ()) * 100 + "% confident in" + " the reply");
		        //System.out.println ("Other Possible responses are: ");
		        for (String s:response.getOtherPossibleResponses ()) {
		        	System.out.println ("\t" + s);
		        }
	        }
	      }
	      catch (Exception ex) {
	        System.out.println ("ERROR: Google cannot be contacted");
	        ex.printStackTrace ();
	      }
		
		return googleErgebnis;
	}
}
