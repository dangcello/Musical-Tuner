import javax.sound.sampled.*;

public class Tuner {
	
    private static final int SAMPLE_RATE = 44100; // Sample rate in Hz
    private static final int BUFFER_SIZE = 4096; // Buffer size for processing
    private static final int MIN_LAG_CONVERSION = 2000;  //  lag for pitch detection
    private static final int MAX_LAG_CONVERSION = 20;    // Maximum lag for pitch detection
    private static final double VOLUME_THRESHOLD = 0.30; // Minimum amount of volume needed to output
    
    public static void main(String[] args) {
        try {
        	PitchHandler.equalTemperamentInitializer();
            AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, true);
            TargetDataLine line = AudioSystem.getTargetDataLine(format);
            line.open(format, BUFFER_SIZE);
            line.start();
            byte[] buffer = new byte[BUFFER_SIZE];
            double[] audioData = new double[BUFFER_SIZE / 2];
            System.out.println("Listening for pitch...");
            while (true) {
                int bytesRead = line.read(buffer, 0, buffer.length);  
                for (int i = 0, j = 0; i < bytesRead; i += 2, j++) {
                    short sample = (short) ((buffer[i] & 0xFF) | (buffer[i + 1] << 8));
                    audioData[j] = sample / 32768.0;
                }
                double[] windowedData = applyHammingWindow(audioData);
                double rms = calculateRMS(audioData);
                if (rms > VOLUME_THRESHOLD) {
                	double pitch = detectPitch(windowedData);
                	String noteName = PitchHandler.getNoteName(pitch);
                	System.out.println("Detected pitch: " + pitch + " Hz");
                	System.out.println("Note name: " + noteName);
                }
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    
    /* Implementation of the Hamming Window used to reduce spectral leakage and 
     * improve accuracy of frequency analysis.
     * 
     * @param audioData double array of frequency data
     * @return adjusted frequencies 
     */
    private static double[] applyHammingWindow(double[] audioData) {
        int bufferSize = audioData.length;
        double[] windowedData = new double[bufferSize];
        for (int i = 0; i < bufferSize; i++) {
            windowedData[i] = audioData[i] * (0.54 - 0.46 * Math.cos((2 * Math.PI * i) / (bufferSize - 1)));
        }
        return windowedData;
    }
    
    /* Helper method that finds the Root mean square of a set
     * Used to aid in volume control
     * 
     * @param audioData double array of frequency data
     * @return 
     */
    private static double calculateRMS(double[] audioData) {
        double sum = 0;
        for (double sample : audioData) {
            sum += sample * sample;
        }
        double mean = sum / audioData.length;
        return Math.sqrt(mean);
    }


    /* Finds the given pitch using the Simple AutoCorrelation Algorithm 
     * Potentially change to other algorithms
     * 
     * @param audioData double array of frequency data
     * @return the frequency of the given pitch 
     */
    private static double detectPitch(double[] audioData) {
    	
        int minLag = (int) (SAMPLE_RATE / MIN_LAG_CONVERSION);
        int maxLag = (int) (SAMPLE_RATE / MAX_LAG_CONVERSION);

        double maxCorrelation = 0;
        int maxLagIndex = 0;

        for (int lag = minLag; lag <= maxLag; lag++) {
            double correlation = 0;
            for (int i = 0; i < audioData.length - lag; i++) {
                correlation += audioData[i] * audioData[i + lag];
            }
            correlation /= audioData.length - lag;
            if (correlation > maxCorrelation) {
                maxCorrelation = correlation;
                maxLagIndex = lag;
            }
        }
        double period = maxLagIndex / (double) SAMPLE_RATE;
        double pitch = 1.0 / period;
        return pitch;
    }
}

