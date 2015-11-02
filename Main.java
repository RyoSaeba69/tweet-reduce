import java.lang.System;


class Main {
    public static void main(String[] args) {
        System.out.println("Hey I'm the main ! :)");

        final TwitterStreamConsumer streamConsumer = new TwitterStreamConsumer();
        streamConsumer.start();

    }
}
