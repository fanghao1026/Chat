import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author FH
 * @create 2020-08-16
 */
public class Client {
    private static final int SERVER_PORT = 1026;
    private Socket socket;
    public PrintStream ps;
    private BufferedReader br;
    private BufferedReader keyIn;
    private ChatWindow window;
    private JFrame frame;

    public void init(){
        try{

            //keyIn = new BufferedReader(new InputStreamReader(System.in));
            socket = new Socket("127.0.0.1",SERVER_PORT);
            ps = new PrintStream(socket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            window = new ChatWindow();
            window.setPs(ps);
            window.ChatWindow();


            while (true){
                String username = JOptionPane.showInputDialog("输入用户名");
                if(username == null){
                    System.exit(1);
                }
                if(username.length()==0){
                    JOptionPane.showMessageDialog(frame,"用户名不能为空");
                }else{
                    window.setTitle(username);
                    ps.println(ChatProtocol.USER_NAME+username+ChatProtocol.USER_NAME);
                    String result = br.readLine();
                    System.out.println(result);
                    if(result.equals(ChatProtocol.LOGIN_SUCCESS)){
                        break;
                    }else{
                        JOptionPane.showMessageDialog(frame,"用户名重复！");
                        continue;
                    }
                }
            }
        }catch (UnknownHostException ex){
            System.out.println("UnknownHostException");
            closeRs();
            System.exit(1);

        }catch (IOException ex){
            System.out.println("IOException");
            closeRs();
            System.exit(1);
        }

        new ClientThread(br,window).start();


    }

//    private void readAndSend(){
//        try{
//            String line = null;
//            while ((line = keyIn.readLine()) != null) {
//                if(line.indexOf(":")>0 && line.startsWith("//")){
//                    line = line.substring(2);
//                    ps.println(ChatProtocol.PRI_MSG+line.split(":")[0]+ChatProtocol.SPLIT_SIGN+line.split(":")[1]+ChatProtocol.PRI_MSG);
//
//                }else{
//                    ps.println(ChatProtocol.PUB_MSG+line+ChatProtocol.PUB_MSG);
//                }
//            }
//        }catch(IOException e){
//            System.out.println("IOException");
//            closeRs();
//            System.exit(1);
//        }
//
//    }


    private void closeRs(){
        try{
            if(keyIn != null){
                keyIn.close();
            }
            if(br != null){
                br.close();
            }
            if(ps != null){
                ps.close();
            }
            if(socket != null){
                socket.close();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.init();
        //client.readAndSend();
    }
}

class ClientThread extends Thread{

    BufferedReader br = null;
    private ChatWindow window;

    public ClientThread(BufferedReader br, ChatWindow window) {
        this.br = br;
        this.window = window;
    }

    @Override
    public void run() {
        try{
            String line = null;
            while((line = br.readLine()) != null){
                window.insertDocument(window.textPane,"\n"+line,Color.RED);
                //window.textPane.setText(window.textPane.getText()+"\n"+line);
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(br != null){
                    br.close();
                }
            }catch(IOException e){
                  e.printStackTrace();
            }

        }

    }
}
