package Tests;

import Framework.GUI;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// Class to test the method in the GUI

public class GUITest {
    private static JFrame frame;
    private static JPanel image;
    private static String message;

    private void createTestFrame() {
        frame = new JFrame();
        image = new JPanel();
        frame.setLayout(new BorderLayout());
        image.setBackground(Color.WHITE);
        frame.add(image, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Team 10's Project Test");
    }

    public static void main(String[] args) {
        GUITest guiTest = new GUITest();
        guiTest.createTestFrame();

        message = "";
        assert runTest() : message;
        System.out.println("Success");
        System.exit(1);

    }

    private static void setPicture(Image picture) {
        picture = picture.getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(picture));
        image.removeAll();
        image.add(label);
        refresh();
    }

    private static void refresh() {
        frame.validate();
        frame.revalidate();
        frame.repaint();
    }

    private static boolean runTest() {
        final File folder = new File("assets/Images");
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            try {
                String name = fileEntry.getAbsolutePath();
                System.out.println(fileEntry.getName());

                Image fileImage = ImageIO.read(new File(name));
                setPicture(fileImage);
                Thread.sleep(1000);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "An error occurred while opening " + fileEntry.getName(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                message = "An error occurred while opening " + fileEntry.getName();
                return false;
            }
        }
        JOptionPane.showMessageDialog(null,
                "Done",
                "Done", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    @Test
    public void selectImageDropdownOptions() {
        ArrayList<String> dropdownItems = new ArrayList<>();
        dropdownItems.add("apple.jpg");
        dropdownItems.add("beaches.jpg");
        dropdownItems.add("bluehue.jpg");
        dropdownItems.add("carts.jpg");
        dropdownItems.add("family.jpg");
        dropdownItems.add("garden.jpg");
        dropdownItems.add("house.jpg");
        dropdownItems.add("images.jpeg");
        dropdownItems.add("image_2960.jpeg");
        dropdownItems.add("index.jpeg");
        dropdownItems.add("lady.jpg");
        dropdownItems.add("LoisGriffel_Landscape.jpeg");
        dropdownItems.add("pacific-coast-impressionist-art.jpg");
        dropdownItems.add("screenshot-2017-07-13-13.37.15.jpeg");
        dropdownItems.add("sisters.jpeg");
        dropdownItems.add("Wheatfield.jpg");
        dropdownItems.add("working.jpg");
        dropdownItems.add("Browse");

        GUI gui = new GUI();

        assertEquals(gui.addImagesToDropdown(), dropdownItems);
    }

}

