public class ClientHandlerTest {

    private Socket clientSocket;
    private ClientHandler handler;
    private PrintWriter out;
    private BufferedReader in;

    @Before
    public void setUp() throws IOException {
        // Create a mock client socket
        clientSocket = mock(Socket.class);
        when(clientSocket.getInputStream()).thenReturn(new ByteArrayInputStream("".getBytes()));
        when(clientSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());

        // Create a new ClientHandler instance
        handler = new ClientHandler(clientSocket);

        // Create input and output streams for the client
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Test
    public void testWelcomeMessage() throws IOException {
        // Start the client handler thread
        Thread thread = new Thread(handler);
        thread.start();

        // Wait for the welcome message to be sent
        String output = in.readLine();

        // Verify the welcome message
        assertEquals("Welcome to the game! Type 'HELP' for a list of commands.", output);
    }

    @Test
    public void testHelpCommand() throws IOException {
        // Start the client handler thread
        Thread thread = new Thread(handler);
        thread.start();

        // Send the HELP command to the client
        out.println("HELP");

        // Wait for the command response
        String output = in.readLine();

        // Verify the command response
        assertEquals("Commands: HELP, PLAYERS, START, ANSWER, SCORES, QUIT", output);
    }

    @Test
    public void testPlayersCommand() throws IOException {
        // Start the client handler thread
        Thread thread = new Thread(handler);
        thread.start();

        // Send the PLAYERS command to the client
        out.println("PLAYERS");

        // Wait for the command response
        String output = in.readLine();

        // Verify the command response
        assertEquals("Players: []", output);
    }

    @Test
    public void testStartCommand() throws IOException {
        // Start the client handler thread
        Thread thread = new Thread(handler);
        thread.start();

        // Send the START command to the client
        out.println("START");

        // Wait for the command response
        String output = in.readLine();

        // Verify the command response
        assertEquals("Game started!", output);

        // Send the START command again
        out.println("START");

        // Wait for the command response
        output = in.readLine();

        // Verify the command response
        assertEquals("Game already started!", output);
    }

}
