import java.io.*;
import java.net.*;
import java.util.LinkedList;



class ServerSomthing extends Thread {

    private Socket socket;
    private InputStreamReader in;
    private OutputStreamWriter out;

    public ServerSomthing(Socket socket) throws IOException {
        this.socket = socket;
        in = new InputStreamReader(socket.getInputStream());
        out = new OutputStreamWriter(socket.getOutputStream());
       // сооюбщений новому поключению
        start(); // вызываем run()
    }
    @Override
    public void run() {
        String word;
        char[] wor = new char[256];
        System.out.println("New _ Player");
        try {
            int i = 0;
            int j = 0;
            // первое сообщение отправленное сюда - это никнейм
            //word = in.readLine();
            try {
                while (true) {
                    in.read(wor);
                    word = new String(wor);

                    while(Server.serverList.size() < 2){
                        i = i+1;
                        if(i % 100000 == 56)
                            Server.serverList.get(0).send("0");
                    }
                    if(j == 0){
                        Server.serverList.get(1).send("1");
                        Server.serverList.get(0).send("1");
                        j++;
                    }
                    if(wor[0] == 1){
                        j = 2;

                    }
                    System.out.println("Echoing: " + word);
                    Server.story.addStoryEl(word);
                    //for (ServerSomthing vr : Server.serverList) {
                    if((socket != Server.serverList.getLast().socket))
                        Server.serverList.get(1).send(word);
                    else
                        Server.serverList.get(0).send(word);// отослать принятое сообщение с привязанного клиента всем остальным влючая его
                    //}
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

    private LinkedList<String> story = new LinkedList<>();

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
                    serverList.add(new ServerSomthing(socket));
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