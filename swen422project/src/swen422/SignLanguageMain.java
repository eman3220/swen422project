import java.util.ArrayList;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;

public class SignLanguageMain {

	private BasicNetwork singleHandNetwork;
	private BasicNetwork dualHandNetwork;

	private SimpleListener leapListener;
	private Controller leapController;

	/**
	 * Constructor
	 */
	public SignLanguageMain() {
		// setup the neural networks
		setupSingleHandNeuralNetwork();
		setupDualHandNeuralNetwork();

		// setup leap motion
		setupLeapMotion();
	}

	private void setupLeapMotion() {
		this.leapController = new Controller();
		this.leapListener = new SimpleListener();

		this.leapController.addListener(leapListener);
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

	public void onConnect(Controller controller) {
		System.out.println("Connected");
	}

	public void onFrame(Controller controller) {
		Frame frame = controller.frame();

		//System.out.println();
		//System.out.println("--- Bone info ---");

		HandList hands = frame.hands();
		//System.out.println("Hands: " + hands.count());

		ArrayList<Float> handData = new ArrayList<Float>();
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

		System.out.println("Frame data collection complete");
		System.out.println("Normalizing...");

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

		System.out.println("Normalizing complete");
		System.out.println("Classifying...");

		// if there is 60 floats, use singleHandNeuralNetwork
		// if there is 120 floats, use dualHandNeuralNetwork

		System.out.println("Classified as...");
	}
}
