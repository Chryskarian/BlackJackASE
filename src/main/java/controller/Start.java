package controller;

public class Start {
	static LogInController controllerA;

	public static void main(String... args) {
		controllerA = new LogInController();
		controllerA.showView();
	}
}
