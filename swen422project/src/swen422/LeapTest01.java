import java.io.IOException;
import java.util.ArrayList;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;

public class LeapTest01 {

	public static void main(String[] args) {
		// Create a sample listener and controller
		SampleListener listener = new SampleListener();
		Controller controller = new Controller();

		// Have the sample listener receive events from the controller
		controller.addListener(listener);

		// Keep this process running until Enter is pressed
		System.out.println("Press Enter to quit...");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Remove the sample listener when done
		controller.removeListener(listener);
	}

}

class SampleListener extends Listener {

	public void onConnect(Controller controller) {
		System.out.println("Connected");
	}

	public void onFrame(Controller controller) {
		// System.out.println("Frame available");

		Frame frame = controller.frame();

		// System.out.println("Frame id: " + frame.id() + ", timestamp: "
		// + frame.timestamp() + ", hands: " + frame.hands().count()
		// + ", fingers: " + frame.fingers().count() + ", tools: "
		// + frame.tools().count() + ", gestures "
		// + frame.gestures().count());

		System.out.println();
		System.out.println("--- Bone info ---");

		HandList hands = frame.hands();
		System.out.println("Hands: "+hands.count());
		
		ArrayList<Float> handData = new ArrayList<Float>();

		for (Hand hand : hands) {
			System.out.println();
			System.out.println("Hand: "+hand);
			for (Finger finger : hand.fingers()) {
				System.out.println("Finger: "+finger);
				for (Bone.Type boneType : Bone.Type.values()) {
					//System.out.println("Bone");
					Bone bone = finger.bone(boneType);
					// ... Use the bone
					System.out.println("The bone type is here: "+boneType);
					System.out.println("The vector we need is here: "+bone.center().toString());
					
					handData.add(bone.center().getX());
					handData.add(bone.center().getY());
					handData.add(bone.center().getZ());
				}
			}
		}
		
		System.out.println("Hand Data size: "+handData.size());
	}
}
