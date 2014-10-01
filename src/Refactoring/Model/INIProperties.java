package Refactoring.Model;

/**
 * Created by HeavenVolkoff on 01/10/14.
 */
public interface INIProperties {
	void saveINIFile(String file);

	void createINIFile(String file);

	void loadINIFile(String file);

	void setupINIProperties();
}
