import InterfaceServer.InterfaceServer;

class main {
    public static void main(String args[]) throws Exception {
        String movies_address = "http://138.197.181.131:5000/api/movies";
        String actors_address = "http://138.197.181.131:5000/api/actors";
        String users_address = "http://138.197.181.131:5000/api/users";
        String comments_address = "http://138.197.181.131:5000/api/comments";

        int infoServerPort = 8080;

        InterfaceServer interfaceServer = new InterfaceServer();
        interfaceServer.start(movies_address, actors_address, users_address, comments_address, infoServerPort);
    }
}