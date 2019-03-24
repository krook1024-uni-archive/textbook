/*
	Mandelb osztály. A Mandelbrot halmaz számítására
	valamint ábrázolására.
*/

import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.input.MouseEvent;
import javafx.event.*;

public class Mandelb extends Application {
	private static final int N = 500;
	private static final int M = 500;

	private static double mousex, mousey;

	@Override
	public void start(Stage stage) {
		stage.setTitle("Hello, Mandelbrot!");

		Group root = new Group();

		Scene s = new Scene(root, N, M, Color.BLACK);

		final Canvas canvas = new Canvas(N, M);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		root.getChildren().add(canvas);

		final double MAXX = 0.7;
		final double MINX = -2.0;
		final double MAXY = 1.35;
		final double MINY = -1.35;
		Mandelb m = new Mandelb();
		m.drawMandel(gc, m.calculateMandelbrot(MAXX, MINX, MAXY, MINY));


		root.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mousex = event.getX();
				mousey = event.getY();
				//System.out.printf("coordinate X: %.2f, coordinate Y: %.2f\n", mousex, mousey);
			}
		});

		s.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// System.out.println("click! " + mousex + " " + mousey);
				double dx = (MAXX - MINX) / N;
				double dy = (MAXY - MINY) / M;

				double nMAXX, nMINX, nMAXY, nMINY;
				double range = 60;

				nMINX = MINX + (mousex*dx);
				nMAXX = MINX + (mousex*dx) + (range*dx);
				nMINY = MAXY - (mousey*dy) - (range*dy);
				nMAXY = MAXY - (mousey*dy);

				m.drawMandel(gc, m.calculateMandelbrot(nMAXX, nMINX, nMAXY, nMINY));
			}
		});


		stage.setScene(s);
		stage.show();
	}

	public static void main(String[] args) {
		launch();

		Mandelb m = new Mandelb();
	}

	public static void drawMandel(GraphicsContext gc, int[][] tomb) {
		for(int i = 0; i < tomb.length; i++) {
			for(int j = 0; j < tomb[i].length; j++) {
				gc.setFill(Color.rgb(tomb[i][j], tomb[i][j], tomb[i][j]));
				gc.fillRect(i, j, 1, 1);
			}
		}
	}

	public static int[][] calculateMandelbrot(double MAXX, double MINX, double MAXY, double MINY) {
		int[][] tomb = new int[N][M];
		int i, j, k;

		double dx = (MAXX - MINX) / N;
		double dy = (MAXY - MINY) / M;

		Komplex C = new Komplex();
		Komplex Z = new Komplex();
		Komplex Zuj = new Komplex();

		int iteracio;

		for(i = 0; i < M; i++) {
			for(j = 0; j < N; j++) {
				C.re = MINX + j * dx;
				C.im = MAXY - i * dy;

				Z.re = 0;
				Z.im = 0;
				iteracio = 0;

				while(Z.re * Z.re + Z.im * Z.im < 4 && iteracio++ < 255)
				{
					Zuj.re = Z.re * Z.re - Z.im * Z.im + C.re;
					Zuj.im = 2 * Z.re * Z.im + C.im;
					Z.re = Zuj.re;
					Z.im = Zuj.im;
				}

				tomb[i][j] = 256 - iteracio;
			}
		}

		return tomb;
	}

	public static class Komplex {
		public double re, im;
	}
}
