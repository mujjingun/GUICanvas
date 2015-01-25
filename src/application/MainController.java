package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

public class MainController implements Initializable {

	public Stage stage;

	@FXML
	private Canvas canvas;
	@FXML
	private ColorPicker colorPicker;
	@FXML
	private ToggleButton eraserToggle;

	private GraphicsContext gc;
	private boolean eraser = false;
	private Color color = Color.BLACK;

	@FXML
	private void save() {
		final WritableImage wim = new WritableImage(400, 400);
		canvas.snapshot(null, wim);
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Image As..");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("PNG", "*.png"));
		final File saveFile = fileChooser.showSaveDialog(stage);
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png",
							saveFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}).start();
	}

	@FXML
	private void fill() {
		System.out.print("fill");
		gc.setFill(color);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	@FXML
	private void eraser() {
		eraser = eraserToggle.isSelected();
		if (eraser) {
			gc.setStroke(Color.WHITE);
			gc.setLineWidth(30);
			canvas.setStyle("-fx-cursor:hand");
		} else {
			gc.setStroke(color);
			gc.setLineWidth(2);
			canvas.setStyle("-fx-cursor:crosshair");
		}
	}

	@FXML
	private void pickColor() {
		color = colorPicker.getValue();
		if (!eraser)
			gc.setStroke(color);
	}

	@FXML
	private void mouseDown(MouseEvent event) {

		first = true;
	}

	private boolean first;

	@FXML
	private void mouseMove(MouseEvent event) {
		float x = (int) event.getX(), y = (int) event.getY();
		if (first) {
			gc.beginPath();
			gc.moveTo(x, y);
			first = false;
		}
		gc.lineTo(x, y);
		gc.stroke();
		gc.beginPath();
		gc.moveTo(x, y);
	}

	@FXML
	private void mouseUp(MouseEvent event) {
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		gc = canvas.getGraphicsContext2D();
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(2);
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.setLineCap(StrokeLineCap.ROUND);
		colorPicker.setValue(Color.BLACK);
	}

}
