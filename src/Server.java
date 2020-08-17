import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author FH
 * @create 2020-08-16
 */
public class Server {
    private static final int SERVER_PORT = 1026;
    public static Chat_Map<String,PrintStream> clients = new Chat_Map<>();
    public void init(){
        try(
                ServerSocket socket = new ServerSocket(SERVER_PORT);
                )

                {
                    while (true){
                        Socket s = socket.accept();
                        new ServerThread(s).start();
                    }
                }
        catch (IOException e) {
            System.out.println("服务器启动失败，是否端口"+SERVER_PORT+"被占用？");
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.init();;
    }

}

class ServerThread extends Thread{
    Socket socket = null;
    BufferedReader br = null;
    PrintStream ps = null;

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.ps = new PrintStream(socket.getOutputStream());
    }

    public String getRealMsg(String line){
        return line.substring(ChatProtocol.PROTOCOL_LEN,line.length()-ChatProtocol.PROTOCOL_LEN);
    }
    @Override
    public void run() {
        try {
            String line = null;
            while (((line = br.readLine()) != null)){
                if(line.startsWith(ChatProtocol.USER_NAME) && line.endsWith(ChatProtocol.USER_NAME)){

                    String username = getRealMsg(line);
                    if(Server.clients.map.containsKey(username)){
                        System.out.println("用户名重复");
                        ps.println(ChatProtocol.LOGIN_DEFEAT);
                    }else{
                        System.out.println("成功添加用户");
                        ps.println(ChatProtocol.LOGIN_SUCCESS);

                        Server.clients.map.put(username,ps);
                    }

                }
                else if (line.startsWith(ChatProtocol.PRI_MSG) && line.endsWith(ChatProtocol.PRI_MSG)){
                    String userAndMsg = getRealMsg(line);

                    String username = userAndMsg.split(ChatProtocol.SPLIT_SIGN) [0];

                    String Msg = userAndMsg.split(ChatProtocol.SPLIT_SIGN) [1];

                    
                    Server.clients.map.get(username).println(Server.clients.getKey(ps)+"悄悄地对你说："+Msg);

                }else {
                    String msg = getRealMsg(line);
                    String user = msg.split(ChatProtocol.SPLIT_SIGN) [0];
                    for(PrintStream ps1  : Server.clients.valueSet()){
                        if(!Server.clients.getKey(ps1).equals(user)){
                            ps1.println(Server.clients.getKey(ps)+"说："+msg.split(ChatProtocol.SPLIT_SIGN)[1]);
                        }


                    }
                }



            }













            } catch (IOException e) {
                e.printStackTrace();
            }


    }
}
