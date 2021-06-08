package serverModule;

import common.utility.Request;
import common.utility.Response;
import common.utility.ResponseCode;
import serverModule.utility.RequestManager;
import serverModule.utility.RequestProcessingThread;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
/**
 * Класс Server.
 * Сторона сервера
 */
public class Server {
    /** Поле порт */
    private int port;
    /** Поле менеджер запросов */
    private RequestManager requestManager;
    /** Поле датаграмм сокет */
    private DatagramSocket socket;
    /** Поле адрес */
    private InetAddress address;
    /** Поле проверка потоков */
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
    /** Поле запрос */
    private Request request;
    /** Поле сканер */
    private Scanner scanner;

    public Server(int port, RequestManager requestManager) {
        this.port = port;
        this.requestManager = requestManager;
    }
    /**
     * Запуск и работа сервера.
     */
    public void run() {
        do_CTRL_C_Thread();
        try {
            System.out.println("Сервер запущен.");
            socket = new DatagramSocket(this.port);
            while (true) {
                try {
                    if (!fixedThreadPool.submit(() -> {
                        try {
                            request = getRequest();
                            System.out.println("Получена команда '" + request.getCommandName() + "'");
                            return true;
                        } catch (ClassNotFoundException | IOException e) {
                            System.out.println("Произошла ошибка при чтении запроса!");
                        }
                        return false;
                    }).get()) break;
                    new RequestProcessingThread(requestManager, request, address, port, socket).start();
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("При чтении запроса произошла ошибка многопоточности!");
                }
            }
        } catch (SocketException e) {
            System.out.println("Произошла ошибка при работе с сокетом!");
        }
    }

    /**
     * Запрос десериализированного "запроса"
     *
     * @return десериализированый запрос.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Request getRequest() throws IOException, ClassNotFoundException {
        byte[] getBuffer = new byte[socket.getReceiveBufferSize()];
        DatagramPacket getPacket = new DatagramPacket(getBuffer, getBuffer.length);
        socket.receive(getPacket);
        address = getPacket.getAddress();
        port = getPacket.getPort();
        return deserialize(getPacket, getBuffer);
    }

    /**
     * Десериализация запроса.
     *
     * @param getPacket
     * @param buffer
     * @return request - запрос.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Request deserialize(DatagramPacket getPacket, byte[] buffer) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getPacket.getData());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Request request = (Request) objectInputStream.readObject();
        byteArrayInputStream.close();
        objectInputStream.close();
        return request;
    }

    /**
     * Обработка Ctrl+C.
     */
    private void do_CTRL_C_Thread() {
        scanner = new Scanner(System.in);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Завершаю программу.");
        }));
    }
}
