import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;

import com.leapmotion.leap.Leap;

public class SignLanguageNNTest {

	/**
	 * The input.
	 */
	public static double INPUT[][] = {
			{ 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
					1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
					1.0, 1.0, 1.0 },
			{ 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
					0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
					0.0, 0.0, 0.0 } };

	/**
	 * The ideal data. {set} {{output}}
	 */
	public static double IDEAL[][] = { { 1.0, 0.0 }, { 0.0, 0.0 } };

	public static void main(String[] args) {
		
		BasicNetwork network = new BasicNetwork();

		// input layer - 14 joints to a hand so 28 neurons needed
		network.addLayer(new BasicLayer(null, true, 28));

		// inner layer - hmmm...
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 14));

		// output layer - 26 different outputs for each letter, 1 for testing
		network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 2));

		network.getStructure().finalizeStructure();
		network.reset();

		// need a training set
		// MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);
		MLDataSet trainingSet = new BasicMLDataSet(INPUT, IDEAL);

		// prepare training
		final Backpropagation train = new Backpropagation(network, trainingSet);

		// do training
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

		// test the neural network
		System.out.println("Neural Network Results:");
		for (MLDataPair pair : trainingSet) {
			System.out.println("\""+pair.getInput()+"\"");
			final MLData output = network.compute(pair.getInput());
			System.out.println(pair.getInput().getData(0) + ","
					+ pair.getInput().getData(1) + ", actual="
					+ output.getData(0) + ",ideal="
					+ pair.getIdeal().getData(0));
		}

		Encog.getInstance().shutdown();
	}

}
