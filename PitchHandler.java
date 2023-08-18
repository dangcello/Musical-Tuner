import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/* Pitch Handler Class that gives each corresponding note its list of identifying frequencies. 
 * Supports multiple octaves that are based on the 12 Tone equal temperament tuning system
 */
public class PitchHandler {
	
    private static final double A_VALUE = 440.00; // A = 440.00 Hz Tune Value
    private static final double EQUAL_TEMPERAMENT_INTERVAL = Math.pow(2.0, (1.0 / 12.0)); // Equal Temperament ratio interval of 12th root of 2:1
    private static final Map<String, ArrayList<Double> > noteMap = new LinkedHashMap<>();
        static {
            noteMap.put("A", new ArrayList<Double>());
            noteMap.put("A#", new ArrayList<Double>());
            noteMap.put("B", new ArrayList<Double>());
            noteMap.put("C", new ArrayList<Double>());
            noteMap.put("C#", new ArrayList<Double>());
            noteMap.put("D", new ArrayList<Double>());
            noteMap.put("D#", new ArrayList<Double>());
            noteMap.put("E", new ArrayList<Double>());
            noteMap.put("F", new ArrayList<Double>());
            noteMap.put("F#", new ArrayList<Double>());
            noteMap.put("G", new ArrayList<Double>());
            noteMap.put("G#", new ArrayList<Double>());
        }
        
        /*
         * Initializes each note with its list of identifying frequencies though multiple octaves
         */
        public static void equalTemperamentInitializer() {
        	double starting_note = 0;
        	for(double val = A_VALUE; val>13 ; val=val/EQUAL_TEMPERAMENT_INTERVAL) {
        		starting_note = val;
        	}
        	double tracker_note = starting_note;
        	double temp = 0;
        	
        	while(tracker_note < 4500) {
        		for (Map.Entry<String, ArrayList<Double>> entry : noteMap.entrySet()) {
        			temp = Math.round(tracker_note * 1000.0) / 1000.0;
        			entry.getValue().add(temp);
        			tracker_note *= EQUAL_TEMPERAMENT_INTERVAL;
        		}
        	}
        }

        /**
         * @param the given frequency
         * @return	the closest note name of the given frequency
         */
        public static String getNoteName(double frequency) {
        	
            String currNote = "Unknown";   
            
    		for (Map.Entry<String, ArrayList<Double>> entry : noteMap.entrySet()) {
    			currNote = entry.getKey();
    			ArrayList<Double> octaveList = entry.getValue();
    			for(Double noteFrequency : octaveList) {		
                    if ((frequency >= ((noteFrequency + (noteFrequency / EQUAL_TEMPERAMENT_INTERVAL)) / 2)) && 
                    	(frequency <= ((noteFrequency + (noteFrequency * EQUAL_TEMPERAMENT_INTERVAL)) / 2))) {
                        return currNote;
                    }
    			}
            }
            return currNote;
        }
    }