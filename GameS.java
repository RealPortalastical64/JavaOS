package p1;

public class GameS {

	Engine game;
	GUI gui;
	
	public GameS() {
		game = new Engine();
		gui = new GUI(game);
	}
	
	public static void main(String[] args) {
		GameS Solitaire = new GameS();
	}
}
