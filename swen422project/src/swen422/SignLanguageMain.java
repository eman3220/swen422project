import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;

public class SignLanguageMain {

	public static BasicNetwork proofOfConcept;
	public static BasicNetwork singleHandNetwork;
	public static BasicNetwork dualHandNetwork;

	private SimpleListener leapListener;
	private Controller leapController;

	/**
	 * Constructor
	 */
	public SignLanguageMain() {
		// setup the neural networks
		setupProofOfConceptNetwork();
		setupSingleHandNeuralNetwork();
		setupDualHandNeuralNetwork();

		// setup leap motion
		setupLeapMotion();
	}

	private void setupProofOfConceptNetwork() {
		// create basic network
		this.proofOfConcept = new BasicNetwork();

		// add input layer with 120 inputs (60 per hand)
		this.proofOfConcept.addLayer(new BasicLayer(null, true, 60));

		// add hidden layer(s)
		this.proofOfConcept.addLayer(new BasicLayer(new ActivationSigmoid(),
				true, 12));

		// add output layer with 5 outputs (1 per letter)
		this.proofOfConcept.addLayer(new BasicLayer(new ActivationSigmoid(),
				false, 2));

		// finalize and reset structure
		this.proofOfConcept.getStructure().finalizeStructure();
		this.proofOfConcept.reset();

		// get training set data
		ArrayList<ArrayList<Float>> training = new ArrayList<ArrayList<Float>>();
		ArrayList<ArrayList<Float>> ideal = new ArrayList<ArrayList<Float>>();

		int numOfOutputs = 0;

		String[] trainingFilePaths = { "trainingSets\\training_palmDown.csv",
				"trainingSets\\training_palmUp.csv" };

		for (String path : trainingFilePaths) {
			numOfOutputs++;

			// read file
			File f = new File(path);

			try {
				BufferedReader br = new BufferedReader(new FileReader(f));
				String line;
				while ((line = br.readLine()) != null) {
					// get all bone vector values
					ArrayList<Float> instance = new ArrayList<Float>();
					String[] a = line.split(",");

					// System.out.println(a.length);

					// create instance
					for (String value : a) {
						instance.add(Float.parseFloat(value));
					}

					// add to training
					training.add(instance);

					// add ideal output
					if (numOfOutputs == 1) {
						ArrayList<Float> idealOutput = new ArrayList<Float>();
						idealOutput.add((float) 1.0);
						idealOutput.add((float) 0.0);

						ideal.add(idealOutput);
					} else {
						ArrayList<Float> idealOutput = new ArrayList<Float>();
						idealOutput.add((float) 0.0);
						idealOutput.add((float) 1.0);

						ideal.add(idealOutput);
					}
				}

				br.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// System.out.println(training.size()+" "+training.get(0).size());

		// set training set data
		double[][] input = new double[training.size()][training.get(0).size()];
		double[][] output = new double[ideal.size()][ideal.get(0).size()];

		// get as double[][]
		for (int i = 0; i < training.size(); i++) {
			ArrayList<Float> trainingInstance = training.get(i);
			ArrayList<Float> idealOutput = ideal.get(i);

			// turn this into double arrays

			double[] t = new double[trainingInstance.size()];
			for (int j = 0; j < trainingInstance.size(); j++)
				t[j] = trainingInstance.get(j);

			double[] r = new double[idealOutput.size()];
			for (int j = 0; j < idealOutput.size(); j++)
				r[j] = idealOutput.get(j);

			input[i] = t;
			output[i] = r;
		}

		MLDataSet trainingSet = new BasicMLDataSet(input, output);
		// System.out.println(input[1].length);
		final Backpropagation train = new Backpropagation(proofOfConcept,
				trainingSet);

		// train network on training data
		int epoch = 1;

		do {
			if (epoch >= 10000) {
				System.out.println("10000th epoch - Error rate: "
						+ train.getError());
				break;
			}
			train.iteration();
			System.out
					.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while (train.getError() > 0.01);
		train.finishTraining();
	}

	private void setupLeapMotion() {
		this.leapController = new Controller();
		this.leapListener = new SimpleListener();

		this.leapController.addListener(leapListener);

		System.out.println("Press Enter to quit...");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Remove the sample listener when done
		leapController.removeListener(leapListener);
	}

	/**
	 * Neural network specifically for doing simple letters
	 */
	private void setupDualHandNeuralNetwork() {
		// create basic network
		this.dualHandNetwork = new BasicNetwork();

		// add input layer with 120 inputs (60 per hand)
		this.dualHandNetwork.addLayer(new BasicLayer(null, true, 120));

		// add hidden layer(s)
		this.dualHandNetwork.addLayer(new BasicLayer(new ActivationSigmoid(),
				true, 24));

		// add output layer with 5 outputs (1 per letter)
		this.dualHandNetwork.addLayer(new BasicLayer(new ActivationSigmoid(),
				false, 5));

		// finalize and reset structure
		this.dualHandNetwork.getStructure().finalizeStructure();
		this.dualHandNetwork.reset();

		// get training set data

		// set training set data

		// train network on training data
	}

	/**
	 * Neural network specifically for doing simple counting
	 */
	private void setupSingleHandNeuralNetwork() {
		// create basic network
		this.singleHandNetwork = new BasicNetwork();

		// add input layer with 60 inputs (60 per hand)
		this.singleHandNetwork.addLayer(new BasicLayer(null, true, 60));

		// add hidden layer(s)
		this.singleHandNetwork.addLayer(new BasicLayer(new ActivationSigmoid(),
				true, 12));

		// add output layer with 5 outputs (1 per number)
		this.singleHandNetwork.addLayer(new BasicLayer(new ActivationSigmoid(),
				false, 5));

		// finalize and reset structure
		this.singleHandNetwork.getStructure().finalizeStructure();
		this.singleHandNetwork.reset();

		// get training set data

		// set training set data

		// train network on training data
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new SignLanguageMain();
	}

}

class SimpleListener extends Listener {

	public boolean classifying = false;
	public boolean flipper = false;
	public int count = 0;

	float h1MinX = Float.MAX_VALUE;
	float h1MinY = Float.MAX_VALUE;
	float h1MinZ = Float.MAX_VALUE;
	float h2MinX = Float.MAX_VALUE;
	float h2MinY = Float.MAX_VALUE;
	float h2MinZ = Float.MAX_VALUE;

	float h1MaxX = Float.MIN_VALUE;
	float h1MaxY = Float.MIN_VALUE;
	float h1MaxZ = Float.MIN_VALUE;
	float h2MaxX = Float.MIN_VALUE;
	float h2MaxY = Float.MIN_VALUE;
	float h2MaxZ = Float.MIN_VALUE;

	public void onConnect(Controller controller) {
		System.out.println("Connected");
	}

	public void onFrame(Controller controller) {
		//System.out.println("start");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (classifying) {
			System.out.println("Already Classifying Something...");
			return;
		}

		System.out.println("Count " + count++);

		Frame frame = controller.frame();
		//System.out.println("frame");
		HandList hands = frame.hands();
		//System.out.println("hands");
		ArrayList<Float> handData = getHandData(hands);
		//System.out.println("handdata");
		if (handData.isEmpty()) {
			System.out.println("No Data...");
			return;
		}

		ArrayList<Float> normalized = getNormalizedData(handData);
		//System.out.println("normalized");
		double[] transformed = new double[normalized.size()];
		for (int i = 0; i < normalized.size(); i++)
			transformed[i] = (double) normalized.get(0);
		//System.out.println("transform");
		String output = classify(transformed);
		//System.out.println("Output: " + output);
		System.out.println(interpretOutput(output));
	}

	private String interpretOutput(String output) {
		String[] a = output.split(",");
		int index = -1;
		double strongest = Double.MIN_VALUE;
		
		for(int i=0;i<a.length;i++){
			String next = a[i];
			double value = Double.parseDouble(next);
			if(strongest<value){
				strongest = value;
				index = i;
			}
		}
		
		String toReturn = "";
		
		switch (index) {
		case 0:
			toReturn = "This is a palm facing down";
			break;
		case 1:
			toReturn = "This is a palm facing up";
			break;
			
		default:
			toReturn = "I don't know what this is";
			break;
		}
		
		return toReturn;
	}

	private String classify(double[] transformed) {
		classifying = true;
		BasicNetwork net = SignLanguageMain.proofOfConcept;
		final MLData output = net.compute(new BasicMLData(transformed));
		String toReturn = output.getData(0) + "," + output.getData(1);
		// String toReturn = "poo,wee";
		// Encog.getInstance().shutdown();
		classifying = false;
		return toReturn;
	}

	private ArrayList<Float> getNormalizedData(ArrayList<Float> handData) {
		ArrayList<Float> normalized = new ArrayList<Float>();
		for (int i = 0; i < handData.size(); i = i + 3) {

			float x = handData.get(i);
			float y = handData.get(i + 1);
			float z = handData.get(i + 2);

			float nX = 0;
			float nY = 0;
			float nZ = 0;

			if (i < 60) {
				nX = ((x - h1MinX) / (h1MaxX - h1MinX));
				nY = ((y - h1MinY) / (h1MaxY - h1MinY));
				nZ = ((z - h1MinZ) / (h1MaxZ - h1MinZ));

			} else {
				nX = ((x - h1MinX) / (h1MaxX - h1MinX));
				nY = ((y - h1MinY) / (h1MaxY - h1MinY));
				nZ = ((z - h1MinZ) / (h1MaxZ - h1MinZ));

			}

			normalized.add(nX);
			normalized.add(nY);
			normalized.add(nZ);
		}
		return normalized;
	}

	private ArrayList<Float> getHandData(HandList hands) {
		ArrayList<Float> handData = new ArrayList<Float>();

		for (int i = 0; i < hands.count(); i++) {
			Hand hand = hands.get(i);
			// System.out.println();
			// System.out.println("Hand: "+hand);
			for (Finger finger : hand.fingers()) {
				// System.out.println("Finger: "+finger);
				for (Bone.Type boneType : Bone.Type.values()) {
					// System.out.println("Bone");
					Bone bone = finger.bone(boneType);
					// ... Use the bone
					// System.out.println("The bone type is here: "+boneType);
					// System.out.println("The vector we need is here: "+bone.center().toString());

					float x = bone.center().getX();
					float y = bone.center().getY();
					float z = bone.center().getZ();

					if (i == 0) {
						// check max x
						if (x > h1MaxX)
							h1MaxX = x;
						// check min x
						if (x < h1MinX)
							h1MinX = x;

						// check max y
						if (y > h1MaxY)
							h1MaxY = y;
						// check min y
						if (y < h1MinY)
							h1MinY = y;

						// check max z
						if (z > h1MaxZ)
							h1MaxZ = z;
						// check min z
						if (z < h1MinZ)
							h1MinZ = z;
					} else {
						// check max x
						if (x > h2MaxX)
							h2MaxX = x;
						// check min x
						if (x < h2MinX)
							h2MinX = x;

						// check max y
						if (y > h2MaxY)
							h2MaxY = y;
						// check min y
						if (y < h2MinY)
							h2MinY = y;

						// check max z
						if (z > h2MaxZ)
							h2MaxZ = z;
						// check min z
						if (z < h2MinZ)
							h2MinZ = z;
					}

					handData.add(x);
					handData.add(y);
					handData.add(z);
				}
			}
		}
		return handData;
	}
}
