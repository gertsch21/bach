package main.management;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Diese Klasse ist fuer das auslesen von ueberwiegend Textfiles zustaendig, etwa von HTML Dateien
 * @author Gerhard
 *
 */
public class FileLoader {
	
	private String stringpfad;
	
	public FileLoader(String path) {
		this.stringpfad = path;
	}
	
	public String getTextFromFile() throws IOException {
		Path path = Paths.get(stringpfad);
		if(!Files.exists(path))
			throw new FileNotFoundException("Das angegebene File mit dem Pfad existiert nicht!");
		byte[] b = Files.readAllBytes(path);
		System.out.println("HTML: "+(new String(b)));
		return new String(b);
	}
	
	public static void main(String[] args) throws IOException {
		FileLoader f = new FileLoader("files\\test.txt");
		System.out.println(f.getTextFromFile());
	}

}
