package protothesisv2_city;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationBiPolar;
import org.encog.engine.network.activation.ActivationClippedLinear;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.util.simple.EncogUtility;

import processing.core.*;
import toxi.geom.Vec2D;
import toxi.processing.ToxiclibsSupport;

public class Main extends PApplet {
	public static int SCALE = 40;

	public ToxiclibsSupport gfx;
	boolean delete = false;
	private World world;
	private Tile selected;

	public static final BasicNetwork network = new BasicNetwork();
	public static final MLDataSet DATASET = new BasicMLDataSet();

	private static void initTrainingData() {
		double input[][] = { { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 1, 0, 0, 0, 0 },
				{ 0, 1, 0, 1, 0, 1, 0, 1, 0 }, 
				{ 1, 0, 1, 0, 0, 0, 1, 0, 1 }, 
				{ 1, 1, 1, 1, 0, 1, 1, 1, 1 },
				{ 0, 1, 1, 1, 0, 1, 1, 1, 0 }, 
				{ 0, 0, 1, 0, 0, 0, 0, 1, 0 },
				{ 0, 1, 0, 0, 0, 1, 1, 1, 1 },
				{ 0, 1, 1, 1, 1, 1, 1, 1, 0 },
				{ 0.5, 0.5, 1, 0.5, 0.5, 0.5, 1, 1, 0.5 },};
		double ideal[][] = { { 0.5 }, 
				{ 0.0 }, 
				{ 0.5 }, 
				{ 1.0 }, 
				{ 1.0 }, 
				{ 1.0 }, 
				{ 0.0 },
				{ 1.0 },
				{ 0.0 },
				{ 0.0 }};
		for (int i = 0; i < input.length; i++) {
			MLData trainingIn = new BasicMLData(input[i]);
			MLData idealOut = new BasicMLData(ideal[i]);
			DATASET.add(trainingIn, idealOut);
		}

	}

	private static void initNetwork() {
		network.addLayer(new BasicLayer(null, true, 9));
		network.addLayer(new BasicLayer(new ActivationTANH(), true, 5));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));
		network.getStructure().finalizeStructure();
		network.reset();
	}

	public static void trainNetwork() {

		// train the neural network
		final ResilientPropagation train = new ResilientPropagation(network, DATASET);

		int epoch = 1;

		do {
			train.iteration();
			System.out.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while (train.getError() > 0.01 && epoch < 1000);
		train.finishTraining();

		// test the neural network
		System.out.println("Neural Network Results:");
		for (MLDataPair pair : DATASET) {
			final MLData output = network.compute(pair.getInput());
			System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1) + ", actual="
					+ output.getData(0) + ",ideal=" + pair.getIdeal().getData(0));
		}

		Encog.getInstance().shutdown();

	}

	public static double getOutput(double[] input) {
		MLData data = new BasicMLData(input);
		MLData out = network.compute(data);
		double[] output = out.getData();
		return output[0];
	}

	public static void main(String[] args) {
		initTrainingData();
		initNetwork();
		PApplet.main("protothesisv2_city.Main");

	}

	public void settings() {
		size(800, 800, "processing.javafx.PGraphicsFX2D");
	}

	public void setup() {
		trainNetwork();

		background(0);
		stroke(255);
		gfx = new ToxiclibsSupport(this);
		textSize(24);
		this.world = new WorldSimple(20, SCALE);
		world.init();

	}

	public void draw() {
		background(0);
		stroke(255);
		strokeWeight(1);
		fill(111, 5, 5);

		world.render(this);
		world.step();

		if (selected != null) {
			strokeWeight(5);
			stroke(255, 0, 0);
			selected.render(this);
			Tile[] nearby = world.getNeighborTiles(selected);
			for (int i = 0; i < nearby.length; i++) {
				if (nearby[i] != null) {
					strokeWeight(2);
					stroke(255, 222, 0);
					nearby[i].render(this);
				}
			}
		}

	}

	public void mouseClicked() {
		float mX = mouseX;
		float mY = mouseY;
		Vec2D click = new Vec2D(mX, mY);
		selected = world.getTileBySelection(click);

	}

	public void keyPressed() {

		switch (key) {
		case ' ':
			runFullPass();
			break;
		case 't':
		case 'T':
			getMLPrediction(selected);
			break;
		case 'd':
		case 'D':
			delete = !delete;
			break;
		default:
			System.out.println("key pressed:" + key);
			zoneTile(key);
			break;
		}

	}

	private void runFullPass() {
		List<Tile> allTiles = world.getWorld();
		for (Tile t : allTiles) {
			getMLPrediction(t);
		}
		trainNetwork();

	}

	private void getMLPrediction(Tile selectedTile) {
		if (selectedTile == null) {
			return;
		}
		Tile[] neighbors = world.getNeighborTiles(selectedTile);
		double[] inputArray = new double[9];
		for (int i = 0; i < neighbors.length; i++) {
			Tile t = neighbors[i];
			int inputIndex = i;
			if (i > 3) {
				inputIndex++;
			}
			if (t != null) {
				Zone z = t.getZoning();
				if (z.type().equals(Residential.TYPE)) {
					inputArray[inputIndex] = 0.05;
				} else if (z.type().equals(Commercial.TYPE)) {
					inputArray[inputIndex] = 0.5;
				} else if (z.type().equals(Industrial.TYPE)) {
					inputArray[inputIndex] = 0.7;
				} else {
					inputArray[inputIndex] = 0;
				}
			} else {
				inputArray[inputIndex] = 0.0;
			}
		}

		double shouldBuild = getOutput(inputArray);
		System.out.print("{");
		for (int i = 0; i < inputArray.length; i++) {
			System.out.print(inputArray[i] + ",");
		}
		System.out.println("}");
		System.out.println(shouldBuild);

		if (shouldBuild > 0.85) {
			Zone toZone = new Residential();
			world.zoneTile(selectedTile, toZone);
		} else if (shouldBuild > 0.5) {
			Zone toZone = new Commercial();
			world.zoneTile(selectedTile, toZone);
		} else if (shouldBuild > 0.2) {
			Zone toZone = new Industrial();
			world.zoneTile(selectedTile, toZone);
		} else {
			Zone toZone = new EmptyZone();
			world.zoneTile(selectedTile, toZone);
		}
		if (world.getScore() > 0.5) {
			MLData dataIn = new BasicMLData(inputArray);
			// Feed it garbage
			MLData dataOut = new BasicMLData(new double[] { world.getScore() });
			MLDataPair pair = new BasicMLDataPair(dataIn, dataOut);
			//DATASET.add(pair);
		}

	}

	private boolean zoneTile(char c) {
		if (selected == null) {
			return false;
		}
		Zone toZone = new Residential();
		switch (c) {
		case '1':
			toZone = new Residential();
			break;
		case '2':
			toZone = new Commercial();
			break;
		case '3':
			toZone = new Industrial();
			break;
		case '4':
			break;

		default:
			// toZone = new Residential();
			System.out.println("key pressed:" + key);
			break;
		}
		return world.zoneTile(selected, toZone);

	}

}
