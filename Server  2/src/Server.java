import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

class ServerSomthing extends Thread {

    private Socket socket;
    private InputStreamReader in;
    private OutputStreamWriter out;
    public OutputStreamWriter out_2;

    public ServerSomthing(Socket socket) throws IOException {
        this.socket = socket;
        in = new InputStreamReader(socket.getInputStream());
        out = new OutputStreamWriter(socket.getOutputStream());
        Server.story.addStoryEl("$$$$$$");
        start(); // вызываем run()
    }
    @Override
    public void run() {
        String word;
        char[] wor = new char[256];
        char[]w = new char[256];
        System.out.println("New _ Player");
        try {
            int j = 0;
            //word = in.readLine();
            try {
                while (true) {
                    //Server.serverList.get(0).send("lolicon4ik228");
                    //wor = new char[2];
                    //wor[0] = '1';
                    //wor[1] = '9';
                    //out.write(wor);
                    //if(){
                    //    socket.close();
                    //}


                    out.flush();
                    if(j == 0 && Server.serverList.size() == 1) {
                        continue;

                    }else{

                        if(j == 0 && Server.serverList.size() == 2){
                            if ((socket != Server.serverList.getLast().socket))
                                Server.serverList.get(1).send("1");
                            else
                                Server.serverList.get(0).send("1");// отосла
                            System.out.print("1 to second? - ");
                            System.out.println(!(socket == Server.serverList.getLast().socket));

                            if ((socket != Server.serverList.getLast().socket)) {
                                out_2 = new OutputStreamWriter(Server.serverList.get(1).socket.getOutputStream());

                            }
                            else {
                                out_2 = new OutputStreamWriter(Server.serverList.get(0).socket.getOutputStream());
                            }



                            j = j+1;
                        }else
                            wor = new char[256];
                            int bytesReceived = in.read(wor);

                        System.out.print("Received bytes are converted to ");
                        System.out.println(new String(wor));
                        System.out.println(bytesReceived);
                            //word = word.split();
                        char[] bytes = new char[bytesReceived];
                        for (int i = 0; i < bytesReceived; i++)
                        {
                            bytes[i] = (char)wor[i];
                        }
                            wor = w;
                            word = new String(bytes);
                            wor = bytes;
                            //word = new String(wor);
                            if(wor[0] == '\0'){
                                continue;
                            }
                            if(wor.length > 1 && wor[1] != '\0') {
                                System.out.println("WOOOORD");
                                System.out.println(word.length());
                                //System.out.println(word);
                                //System.out.println(Server.story.story.getLast());
                                if (word.equalsIgnoreCase(Server.story.story.getLast())) {

                                    System.out.println("CHEEEEATER");
                                    downService();
                                    break;
                                }
                            }
                            Server.story.addStoryEl(word);
                    }
                    //}

                    System.out.println("REAAAAAD");

                    //while(Server.serverList.size() == 1){
                   //     i = i+1;
                    //}
/*
                    while(Server.serverList.size() == 1){
                        i = i+1;
                        if(i % 1000000 == 56)
                            Server.serverList.get(0).send("0");
                        word ="";

                        //System.out.println("0");
                    }
*/

                    if(j == 0 && Server.serverList.size() == 2){
                        if ((socket != Server.serverList.getLast().socket)) {
                            Server.serverList.get(1).send("1");
                            System.out.print("1 to second");
                        }
                        else {
                            Server.serverList.get(0).send("1");// отосла
                            System.out.print("1 to first");

                        }
                        //System.out.println(!(socket == Server.serverList.getLast().socket));
                        j = j+1;
                    } else
                    if('1' == wor[0]){

                        if((socket == Server.serverList.getLast().socket)) {
                            Server.serverList.get(0).send("1");

                            System.out.print("1 send to 1");

                            //Server.serverList.get(1).send("0");
                        }
                        else{
                            //Server.serverList.get(0).send("0");
                            System.out.print("2 send to 2");

                            Server.serverList.get(1).send("2");
                        }


                        //System.out.println(!(socket == Server.serverList.getLast().socket));
                        word = "";

                    }else
                        if('$' == wor[0]){

                            if((socket == Server.serverList.getLast().socket)) {
                                //Server.serverList.get(0).send("2");
                                Server.serverList.get(0).send("1");
                                System.out.print("1 send to 1");

                            }
                            else{
                                Server.serverList.get(1).send("2");
                                System.out.print("2 send to 2");

                                //Server.serverList.get(1).send("2");
                            }
                        }
                        else{

                            System.out.println("You make this:    "+word);

                        //for (ServerSomthing vr : Server.serverList) {
                        //if ((socket != Server.serverList.getLast().socket)) {
                            //Server.serverList.get(1).send(word);
                            //OutputStreamWriter out_2 = new OutputStreamWriter(Server.serverList.get(1).socket.getOutputStream());
                            //out = new OutputStreamWriter(());
                            out_2.write(word);
                            out_2.flush();
                            //out_2.flush();
                        //}
                        //else{
                            //Server.serverList.get(0).send(word);// отослать принятое сообщение с привязанного клиента всем остальным влючая его

                            //OutputStreamWriter out_2 = new OutputStreamWriter(Server.serverList.get(0).socket.getOutputStream());
                            //out = new OutputStreamWriter(());
                            //out_2.write(wor);

                            //out_2.flush();
                        //}

                        }
                }
            } catch (NullPointerException ignored) {}


        } catch (IOException e) {
            this.downService();
        }
    }

    private void send(String msg) {
        try {
            out.write(msg);
            out.flush();
        } catch (IOException ignored) {}

    }

    private void downService() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ServerSomthing vr : Server.serverList) {
                    if(vr.equals(this)) vr.interrupt();
                    Server.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {}
    }
}

/**
 * класс хранящий в ссылочном приватном
 * списке информацию о последних 10 (или меньше) сообщениях
 */

class Story {

    public LinkedList<String> story = new LinkedList<>();

    /**
     * добавить новый элемент в список
     * @param el
     */

    public void addStoryEl(String el) {
        // если сообщений больше 10, удаляем первое и добавляем новое
        // иначе просто добавить
        if (story.size() >= 10) {
            story.removeFirst();
            story.add(el);
        } else {
            story.add(el);
        }
    }

    /**
     * отсылаем последовательно каждое сообщение из списка
     * в поток вывода данному клиенту (новому подключению)
     * @param writer
     */

}

public class Server {

    public static final int PORT = 8000;
    public static LinkedList<ServerSomthing> serverList = new LinkedList<>(); // список всех нитей - экземпляров
    // сервера, слушающих каждый своего клиента
    public static Story story; // история переписки

    /**
     * @param args
     * @throws IOException
     */

    public static void main(String[] args) throws IOException {

        ServerSocket server = new ServerSocket(PORT);
        story = new Story();
        System.out.println("Server Started");
        try {
            while (true) {
                // Блокируется до возникновения нового соединения:
                Socket socket = server.accept();

                try {
/*
                    int randomNum = ThreadLocalRandom.current().nextInt(0, 1 + 1);
                    //serverList.add(new ServerSomthing(socket));
                    if(randomNum == 0){
                        serverList.addFirst(new ServerSomthing(socket));
                    }else{
                        serverList.addLast(new ServerSomthing(socket));
                    }
*/

                    serverList.addLast(new ServerSomthing(socket));
                    // добавить новое соединенние в список
                } catch (IOException e) {
                    // Если завершится неудачей, закрывается сокет,
                    // в противном случае, нить закроет его:
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }
}