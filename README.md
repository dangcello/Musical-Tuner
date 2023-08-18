# Musical-Tuner
This repository contains Java code for a simple pitch detection tuner. The tuner listens to audio input through the computer's microphone and processes the input to determine the detected pitch's frequency and corresponding musical note.

# Contents
Tuner.java: This file contains the main class responsible for capturing audio input, processing it, and detecting the pitch frequency using the Simple AutoCorrelation Algorithm. It also utilizes the Hamming Window technique to enhance the accuracy of frequency analysis.

PitchHandler.java: This file defines a utility class called PitchHandler. It contains methods to handle note frequencies and names based on the 12-tone equal temperament tuning system. It initializes note frequencies and provides functionality to determine the closest note name based on a given frequency.

# How It Works
The Tuner class initializes the pitch detection process by setting up audio input through the microphone.

Audio input is continuously captured in small buffers, and the Root Mean Square (RMS) is calculated to assess the volume of the input.

If the input volume surpasses a predefined threshold, the tuner applies a Hamming Window to the audio data to minimize spectral leakage, enhancing the precision of frequency analysis.

The Simple AutoCorrelation Algorithm is utilized to determine the pitch frequency. It calculates correlations between shifted samples and identifies the lag value with the highest correlation. The lag value is then used to calculate the frequency of the detected pitch.

The detected pitch frequency is passed to the PitchHandler class, which maps the frequency to the closest musical note name using the 12-tone equal temperament system.

The determined pitch frequency and corresponding note name are displayed in the console.

# Note
This is a basic pitch detection tuner implementation and more algorithms and support for multiple tuning systems are in progress.
