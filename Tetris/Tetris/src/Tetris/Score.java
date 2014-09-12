package Tetris;

import com.jme3.scene.Node;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by HeavenVolkoff on 12/09/14.
 */
public class Score extends Node {

	private int score;
	private int multiplier;
	private int level;
	private int jump;
	private List<List<String>> numbers;
	float posX;
	float posY;

	public Score(float posX, float posY){
		this.score = 0;
		this.multiplier = 1;
		this.level = 1;
		this.numbers = new ArrayList<List<String>>();
		this.posX = posX;
		this.posY = posY;

		for(int i = 0; i<=9; i++){
			numbers.add(loadFromFile( i+".piece" ));
		}
	}

	public List<String> loadFromFile(String fileName) {
		try {
			String appPath = new File(".").getCanonicalPath();
			Path path = Paths.get(appPath + "/resources/customPieces/numbers/" + fileName);
			return Files.readAllLines(path);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void updateScore(int points, int multiplier){
		this.score += (points * multiplier * this.multiplier);

		if(this.score >= jump){
			level++;
			jump *= 1.2f;
		}
	}

	public void resetScore(){
		this.score = 0;
	}

	public int getScore() {
		return score;
	}



}
