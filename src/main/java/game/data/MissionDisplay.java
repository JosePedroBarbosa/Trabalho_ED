package game.data;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Scanner;

/**
 * The MissionDisplay class is responsible for displaying the image associated
 * with a specific mission in the game.
 */
public class MissionDisplay {
    /**
     * The current mission's image file.
     */
    private static String currentMissionImage;

    /**
     * The default directory where mission images are stored.
     */
    private static String defaultMissionsPaths = ".\\src\\missions\\";

    private static final int maxWidth = 500;  // Image Width Size
    private static final int maxHeight = 500; // Image Heigth Size
    private static final int xOffset = 20; // Distância do canto direito
    private static final int yOffset = 50; // Distância do topo

    /**
     * Sets the path to the image file for the current mission.
     *
     * @param missionName The name of the mission whose image is to be displayed.
     */
    public static void setCurrentMissionImage(String missionName){
        currentMissionImage = defaultMissionsPaths + missionName + "_Image.png";
    }

    /**
     * Displays the current mission's map image in a new window. If the image file does not exist,
     * a message is displayed indicating that the mission does not have a map.
     *
     * @param scanner A Scanner instance to pause the program and wait for user input.
     */
    public static void showImage(Scanner scanner) {

        File file = new File(currentMissionImage);
        if (!file.exists()) {
            System.out.println("This mission does not have a map.");
            return;
        }

        JFrame frame = new JFrame("Mapa do Jogo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Carregar a imagem original
        ImageIcon originalIcon = new ImageIcon(currentMissionImage);
        Image originalImage = originalIcon.getImage();

        // Redimensionar a imagem para caber nos limites
        int width = originalIcon.getIconWidth();
        int height = originalIcon.getIconHeight();

        if (width > maxWidth || height > maxHeight) {
            double scale = Math.min((double) maxWidth / width, (double) maxHeight / height);
            width = (int) (width * scale);
            height = (int) (height * scale);
        }

        Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        // Exibir a imagem redimensionada
        JLabel label = new JLabel(resizedIcon);

        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.pack();

        // Obter resolução da tela
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Calcular posição no canto superior direito com deslocamento
        int xPosition = screenSize.width - frame.getWidth() - xOffset;
        int yPosition = yOffset;

        // Configurar posição e visibilidade
        frame.setLocation(xPosition, yPosition);
        frame.setAlwaysOnTop(true);
        frame.toFront();
        frame.requestFocus();
        frame.setVisible(true);

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
        frame.dispose();
    }

}