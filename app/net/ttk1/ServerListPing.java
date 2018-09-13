package net.ttk1;

import play.mvc.*;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ServerListPing extends Controller {
    private static final int PROTOCOL_VERSION = 0;

    public Result ping(String host, int port) {
        return badRequest();

        //String response = sendPing(host, port);
        //return ok(response).as("application/json");
    }

    private static String sendPing(String host, int port) {
        try {
            Socket s = new Socket(host, port);

            // 送信
            OutputStream os = s.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            List<Byte> req = new ArrayList<>();

            // handshaking
            //// packet ID
            req.add((byte) 0x00);

            //// protocol version
            addVarInt(req, PROTOCOL_VERSION);

            //// server address
            addString(req, host);

            //// server port
            addShort(req, port);

            //// next state
            req.add((byte) 1);

            //// add length
            addLength(req, req.size());

            //// send packet
            dos.write(toByteArray(req));


            // status
            req = new ArrayList<>();

            //// packet ID
            req.add((byte) 0x00);

            //// add length
            addLength(req, req.size());

            //// send packet
            dos.write(toByteArray(req));

            // 受信
            InputStream is = s.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            //BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            int packetLen = readVarInt(dis);
            System.out.println(packetLen);
            //// packet ID
            byte packetID = dis.readByte();

            //// Server List Ping
            String msg;
            if (packetID == 0x00) {
                int len = readVarInt(dis);
                byte[] res = new byte[len];
                for (int i = 0; i < len; i++) {
                    res[i] = dis.readByte();
                }
                msg = new String(res, "UTF-8");
            } else {
                msg = "{none}";
            }
            // ストリームを閉じる
            dis.close();
            dos.close();

            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            return "{error}";
        }
    }

    private static void addVarInt(List<Byte> list, int value) {
        do {
            byte temp = (byte)(value & 0b01111111);
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            list.add(temp);
        } while (value != 0);
    }

    private static int readVarInt(DataInputStream dis) throws IOException {
        int numRead = 0;
        int result = 0;
        byte read;
        do {
            read = dis.readByte();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > 5) {
                throw new RuntimeException("VarInt is too big");
            }
        } while ((read & 0b10000000) != 0);

        return result;
    }

    private static void addLength(List<Byte> list, int value) {
        List<Byte> list0 = new ArrayList<>();
        do {
            byte temp = (byte)(value & 0b01111111);
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            list0.add(temp);
        } while (value != 0);
        list.addAll(0, list0);
    }

    private static void addShort(List<Byte> list, int value) {
        // javaでunsigned shortが扱えないのでintの下位2byteを使う
        byte[] bs = ByteBuffer.allocate(4).putInt(value).array();
        list.add(bs[2]);
        list.add(bs[3]);
    }

    private static void addByte(List<Byte> list, int value) {
        // javaでunsigned shortが扱えないのでintの下位2byteを使う
        byte[] bs = ByteBuffer.allocate(4).putInt(value).array();
        list.add(bs[3]);
    }

    private static void addString(List<Byte> list, String str) throws UnsupportedEncodingException {
        byte[] bs = str.getBytes("UTF-8");
        if (!str.isEmpty()) {
            addByte(list, str.length());
            for (byte b : bs) {
                list.add(b);
            }
        }
    }

    private static byte[] toByteArray(List<Byte> list){
        byte[] bytes = new byte[list.size()];
        for (int i = 0; i < list.size(); i++){
            bytes[i] = list.get(i);
        }
        return bytes;
    }
}
