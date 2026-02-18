package net.thomasbelk.real_facial_expressions;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.atomic.AtomicBoolean;

public class FacePacketReceiver implements Runnable {

    private final int port;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final Gson gson = new Gson();
    private DatagramSocket socket;

    public FacePacketReceiver(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            socket = new DatagramSocket(port);
            byte[] data = new byte[4096];
            DatagramPacket packet = new DatagramPacket(data, data.length);

            while (running.get()) {
                socket.receive(packet);

                String json = new String(
                        packet.getData(),
                        packet.getOffset(),
                        packet.getLength()
                );

                try {
                    FacePacket facePacket = gson.fromJson(json, FacePacket.class);
                    if (facePacket != null && FacePacketStore.INSTANCE.hasValidFaceId(facePacket.faceId())) {
                        FacePacketStore.INSTANCE.putPacket(facePacket.faceId(), facePacket);
                    }
                } catch (JsonSyntaxException ignored) {
                    // malformed packet — drop silently
                }
            }
        } catch (Exception e) {
            System.err.println("Face UDP receiver stopped: " + e.getMessage());
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    public void shutdown() {
        running.set(false);
        if (socket != null && !socket.isClosed()) {
            socket.close(); // unblock receive
        }
    }
}
