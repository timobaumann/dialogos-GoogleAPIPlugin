import java.io.IOException;

import com.clt.script.exp.values.StructValue;
import com.darkprograms.speech.translator.GoogleTranslate;


public class Translator {
	
	public Translator() {
	}

	public String translate (StructValue args) {
		String sourceLanguage = args.getValue("srcLang").toString().replace("\"", "");
		String targetLanguage = args.getValue("trgLang").toString().replace("\"", "");
		String text = args.getValue("text").toString().replace("\"", "");
		
		System.out.println("Translating " + text);
		
		String result;
		try {
			result = GoogleTranslate.translate(sourceLanguage, targetLanguage, text);
			System.out.println("Translated to " + result);
		} catch (IOException e) {
			result = "";
			e.printStackTrace();
		}
		return result;
	}
	
}
