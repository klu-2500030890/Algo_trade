package managers;

import utils.InputHelper;
import java.util.Random;

public class JwcConnectionManager {

    // Colors for console UI
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";

    // JWC Settings state
    private boolean isConnected = false;
    private String host = "broker.jwc-trading.net";
    private int port = 8443;
    private String clientId = "ALGO-TRADER-101";
    private String authToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiY2xpZW50SWQiOiJBTEdPLVRSQURFUisxMDEiLCJpYXQiOjE1MTYyMzkwMjJ9";
    private int latency = 0; // ms

    public void manageJwcDesk() {
        while (true) {
            System.out.println("\n" + BOLD + CYAN + "=== [8] JWC CONNECTIVITY DESK ===" + RESET);
            System.out.println("  1. Check Connection Status");
            System.out.println("  2. Configure JWC Broker Settings");
            System.out.println("  3. Establish Connection & Handshake");
            System.out.println("  4. Stream Real-time Market Ticks");
            System.out.println("  5. Disconnect / Close Session");
            System.out.println("  6. Back to Main Menu");
            System.out.println(CYAN + "---------------------------------" + RESET);

            int choice = InputHelper.readInt("Select operation (1-6): ", 1, 6);
            if (choice == 6) return;

            switch (choice) {
                case 1:
                    showStatus();
                    InputHelper.pressEnterToContinue();
                    break;
                case 2:
                    configureSettings();
                    break;
                case 3:
                    connect();
                    InputHelper.pressEnterToContinue();
                    break;
                case 4:
                    streamTicks();
                    break;
                case 5:
                    disconnect();
                    InputHelper.pressEnterToContinue();
                    break;
            }
        }
    }

    private void showStatus() {
        System.out.println("\n" + BOLD + "--- JWC CONNECTION PROFILE ---" + RESET);
        System.out.println("  Endpoint Broker : " + host + ":" + port);
        System.out.println("  Client Identity : " + clientId);
        System.out.println("  Auth Token Type : JWT (JSON Web Token)");
        System.out.print("  Link Status     : ");
        if (isConnected) {
            System.out.println(GREEN + "CONNECTED" + RESET + " (Latency: " + latency + "ms)");
            System.out.println("  Channel Type    : Encrypted TLS WebSocket (WSS)");
        } else {
            System.out.println(RED + "DISCONNECTED" + RESET);
        }
    }

    private void configureSettings() {
        System.out.println("\n" + BOLD + "Configure JWC broker endpoint:" + RESET);
        String inputHost = InputHelper.readString("  Broker Hostname [" + host + "]: ");
        if (!inputHost.trim().isEmpty()) {
            host = inputHost.trim();
        }

        int inputPort = InputHelper.readInt("  Broker Port [" + port + "] (1-65535): ", 1, 65535);
        port = inputPort;

        String inputClientId = InputHelper.readString("  Client ID [" + clientId + "]: ");
        if (!inputClientId.trim().isEmpty()) {
            clientId = inputClientId.trim();
        }

        String inputToken = InputHelper.readString("  JWT Bearer Token: ");
        if (!inputToken.trim().isEmpty()) {
            authToken = inputToken.trim();
        }

        System.out.println(GREEN + "Settings successfully configured!" + RESET);
        isConnected = false; // reset connection after changing configuration
    }

    private void connect() {
        System.out.println("\nResolving JWC address: " + host + "...");
        try {
            Thread.sleep(800);
            System.out.println("Opening TCP connection socket on port " + port + "...");
            Thread.sleep(600);
            System.out.println("Initiating WebSocket secure (WSS) handshake...");
            Thread.sleep(800);
            System.out.println("Authenticating via JWT token credential...");
            Thread.sleep(500);

            if (authToken.length() < 10) {
                System.out.println(RED + "Connection failed: Invalid Token signature." + RESET);
                isConnected = false;
                return;
            }

            Random rand = new Random();
            latency = rand.nextInt(45) + 5; // mock latency between 5 and 50 ms
            isConnected = true;

            System.out.println(GREEN + "SUCCESS: Connected to JWC Broker!" + RESET);
            System.out.printf("Handshake completed in %d ms.\n", latency + 120);
        } catch (InterruptedException e) {
            System.out.println(RED + "Handshake interrupted!" + RESET);
        }
    }

    private void streamTicks() {
        if (!isConnected) {
            System.out.println(RED + "Error: Must establish a JWC connection first before streaming ticks." + RESET);
            InputHelper.pressEnterToContinue();
            return;
        }

        System.out.println("\n" + BOLD + GREEN + "Starting real-time feed subscription (JWC TICK STREAM)..." + RESET);
        System.out.println("Press ENTER to unsubscribe and return to menu.");
        System.out.println("-------------------------------------------------------------------");

        Random rand = new Random();
        String[] mockSymbols = {"RELIANCE", "TCS", "INFY", "HDFCBANK", "WIPRO", "AAPL", "GOOGL"};
        
        // Spawn a tick stream reader loop with small delays
        // We will check system input non-blockingly or allow the user to stop
        // In Java terminal, we can check if System.in has available bytes
        try {
            // Clear input buffer before loop
            while (System.in.available() > 0) {
                System.in.read();
            }

            while (System.in.available() == 0) {
                String sym = mockSymbols[rand.nextInt(mockSymbols.length)];
                double delta = (rand.nextDouble() - 0.495) * 2.0; // random movement
                double base = 100.0 + rand.nextDouble() * 2000.0;
                double newPrice = base + delta;
                int size = (rand.nextInt(10) + 1) * 50;

                System.out.printf("  [TICK] %-10s | Last: $%-8.2f | Move: %s%+-6.2f%s | Volume: %-5d | ServerTime: %d ms ago\n",
                        sym, newPrice, (delta >= 0 ? GREEN : RED), delta, RESET, size, rand.nextInt(5));

                Thread.sleep(600);
            }

            // Consume the Enter key that stopped the stream
            while (System.in.available() > 0) {
                System.in.read();
            }
        } catch (Exception e) {
            System.out.println("Stream session terminated.");
        }
        System.out.println("Unsubscribed from tick feed. Channel returning to IDLE state.");
        InputHelper.pressEnterToContinue();
    }

    private void disconnect() {
        if (isConnected) {
            isConnected = false;
            System.out.println(YELLOW + "WebSocket connection cleanly terminated. JWC channel closed." + RESET);
        } else {
            System.out.println("JWC is already disconnected.");
        }
    }
}
