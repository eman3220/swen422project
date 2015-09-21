import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;

/**
 * Writes to CSV normalized bone vector information
 * 
 * @author Emmanuel
 *
 */
public class BoneVectorDataCollector {

	public static void main(String[] args) {
		// Create a sample listener and controller
		MyListener listener = new MyListener();
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

class MyListener extends Listener {

	public void onConnect(Controller controller) {
		System.out.println("Connected");
	}

	public void onFrame(Controller controller) {
		// System.out.println("Frame available");

		Frame frame = controller.frame();

		System.out.println();
		System.out.println("--- Bone info ---");

		HandList hands = frame.hands();
		System.out.println("Hands: " + hands.count());

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
		
		if(handData.isEmpty()){
			System.out.println("No data found");
			return;
		}

		System.out.println("Frame data collection complete");
		System.out.println("Normalizing...");

		StringBuilder normalized = new StringBuilder();
		
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

			normalized.append(nX+",");
			normalized.append(nY+",");
			normalized.append(nZ+",");
		}
		
		// remove trailing ','
		String data = normalized.toString().trim();
		data = data.substring(0, data.length()-1);

		System.out.println("Normalizing complete");
		System.out.println("Writing...");
		
		String filename = "boneData.csv";
		File file = new File(filename);
		
		String prevData = "";
		
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.err.println("Failed to create file");
				e.printStackTrace();
			}
		}else{
			// read file for previous contents
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line;
				
				while((line=br.readLine())!=null){
					prevData += line+"\n";
				}
				
				br.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// append data to prevData
		prevData += data;
		data = prevData;
		
		// write data
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(data);
			bw.close();
			
		} catch (IOException e) {
			System.err.println("Failed to write data");
			e.printStackTrace();
		}
		
		
		System.out.println("Writing done");
		//System.exit(0);
	}
}
