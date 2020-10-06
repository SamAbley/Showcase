package Framework;

import MovieMaker.MainRecorder;
import SoundTrack.SoundTrack;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;


public class GUI {
    final public JFrame frame = new JFrame("Team 10's Project");
    final private JPanel imageContainer = new JPanel();
    public JButton start, download;
    private JSlider blurStrength = new JSlider(JSlider.HORIZONTAL);
    private String soundTrackFile;
    private String imagePath;
    private String imageName;
    private String oimagePath;
    private boolean checkboxValue = false;
    JComboBox<String> select = new JComboBox<>();

    void createFrame() {
        imageContainer.setBackground(Color.WHITE);

        frame.setLayout(new BorderLayout());

        frame.add(createNorthContainerPanel(), BorderLayout.NORTH);
        frame.add(imageContainer, BorderLayout.CENTER);
        frame.add(createSouthContainerPanel(), BorderLayout.SOUTH);

        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    private Box createTopButtonsContainerPanel() {
        JButton reset = new JButton("Reset Image");
        JButton remove = new JButton("Remove Image");
        addImagesToDropdown();
        select.addActionListener(e -> {
            try{
                final String item = select.getSelectedItem().toString();

                if (item.equals("Browse")){
                    selectImage();

                }
                else if (!item.equals("Select Image")){
                    Image image = ImageIO.read(new File("assets/Images/"+item));
                    setPicture(image);
                }
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        });
        reset.addActionListener(e -> resetImage());
        remove.addActionListener(e -> removeImage());
        Box topButtons = Box.createHorizontalBox();
        topButtons.add(Box.createHorizontalStrut(100));
        topButtons.add(select);
        topButtons.add(Box.createHorizontalStrut(50));
        topButtons.add(reset);
        topButtons.add(Box.createHorizontalStrut(50));
        topButtons.add(remove);
        topButtons.add(Box.createHorizontalStrut(100));

        return topButtons;
    }

    private Box createUrlContainerPanel() {
        JLabel urlLabel = new JLabel("Add URL: ");
        JTextField urlText = new JTextField(10);
        urlText.addActionListener(actionEvent -> getImageFromUrl(urlText.getText()));

        Box bottomURL = Box.createHorizontalBox();
        bottomURL.add(urlLabel);
        bottomURL.add(Box.createHorizontalStrut(10));
        bottomURL.add(urlText);

        return bottomURL;
    }

    private Box createNorthContainerPanel() {
        Box north = Box.createVerticalBox();
        north.add(createTopButtonsContainerPanel());
        north.add(Box.createVerticalStrut(10));
        north.add(createUrlContainerPanel());
        return north;
    }

    private Box createSouthContainerPanel() {
        Box south = Box.createHorizontalBox();

        createStartButton();

        south.add(new JLabel("Select\nSoundtrack: "));
        south.add(createSoundtracksDropdown());
        south.add(Box.createHorizontalStrut(20));
        south.add(new JLabel("Video"));
        JCheckBox isVideoRecorded = new JCheckBox();
        isVideoRecorded.addActionListener(e -> setCheckboxValue(isVideoRecorded.isSelected()));
        south.add(isVideoRecorded);
        south.add(Box.createHorizontalStrut(20));
        south.add(start);
        south.add(Box.createHorizontalStrut(20));
        south.add(createDownloadButton());
        Box sliderBox = Box.createVerticalBox();
        sliderBox.add(new JLabel("Select Blur Level:"));

        this.blurStrength.setMajorTickSpacing(2);
        this.blurStrength.setMinorTickSpacing(1);
        this.blurStrength.setPaintTicks(true);
        this.blurStrength.setMaximum(10);
        sliderBox.add(this.blurStrength);
        south.add(sliderBox);

        return south;
    }

    public void setPicture(Image picture) {
        JLabel label;

        if (picture == null) {
            label = new JLabel();
            start.setEnabled(false);
        } else {
            picture = picture.getScaledInstance(imageContainer.getWidth(), imageContainer.getHeight(), Image.SCALE_SMOOTH);
            label = new JLabel(new ImageIcon(picture));
            start.setEnabled(true);
        }
        System.out.println(select.getSelectedItem());
        if (select.getSelectedItem() != "Browse"){
        imageName = select.getSelectedItem().toString();
        imagePath = "assets/Images/"+imageName;
        oimagePath = "assets/Images/"+imageName;
        System.out.println("NOW: "+imageName);}
        imageContainer.removeAll();
        imageContainer.add(label);
        download.setEnabled(false);
        this.refresh();
    }

    public void refresh() {
        frame.validate();
        frame.revalidate();
        frame.repaint();
    }

    public String getSoundTrackFile() {
        return soundTrackFile;
    }

    private void setSoundTrackFile(String newSoundTrackFile) {
        soundTrackFile = newSoundTrackFile;
    }

    private void selectImage() {
        try {
            FileNameExtensionFilter fileType
                    = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());

            File selectedFile = selectFile(fileType);

            if (selectedFile != null) {
                imagePath = selectedFile.getAbsolutePath();
                oimagePath = selectedFile.getAbsolutePath();
                imageName = selectedFile.getName();
                Image fileImage = ImageIO.read(selectedFile);
                this.setPicture(fileImage);

                start.setEnabled(true);

            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame,
                    "An error occurred while opening the image",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetImage() {
        try {
            Image resetImage = ImageIO.read(new File(this.oimagePath));
            download.setEnabled(false);
            start.setEnabled(true);
            this.setPicture(resetImage);
            this.refresh();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void removeImage() {
        try {
            select.setSelectedIndex(0);
            download.setEnabled(false);
            start.setEnabled(false);
            this.setPicture(null);
            this.refresh();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getImageFromUrl(String url) {
        if (url.length() > 0) {
            try {
                Image imageFromUrl = ImageIO.read(new URL(url));
                saveImage(url);
                this.setPicture(imageFromUrl);
                start.setEnabled(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame,
                        "Error: Invalid URL!!!", "Error Message",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveImage(String text) {
        try {
            URL url = new URL(text);
            String fileName = url.getFile();
            String destName = "assets/Images" + fileName.substring(fileName.lastIndexOf("/"));

            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destName);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
            File downloadedImage = new File(destName);
            if (downloadedImage.exists()) {
                imagePath = downloadedImage.getAbsolutePath();
                imageName = downloadedImage.getName();
            }
            System.out.println("Downloaded image name : " + imageName);
            System.out.println("Downloaded image path : " + imagePath);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame,
                    "An error trying to open the image",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JComboBox<String> createSoundtracksDropdown() {
        JComboBox<String> soundTracksDropdown = new JComboBox<>();
        soundTracksDropdown.setMaximumSize(new Dimension(100, 50));
        soundTracksDropdown.addItem("None");

        setSoundTrackFile(soundTracksDropdown.getItemAt(0));

        final String soundTracksPath = "Ffmpeg/soundtracks";

        ArrayList<String> soundtracks = collectFilesFromDirectory(soundTracksPath);
        try {
            for (String soundtrack : soundtracks) {
                soundTracksDropdown.addItem(soundtrack);
            }

            soundTracksDropdown.addItem("Browse");

            soundTracksDropdown.addActionListener(e -> {
                String selectedOption = Objects.requireNonNull(soundTracksDropdown.getSelectedItem()).toString();

                if (selectedOption.equals("Browse")) {
                    File selectedFile = selectFile(null);

                    if (selectedFile != null) {
                        setSoundTrackFile(selectedFile.getName());
                    }
                } else {
                    setSoundTrackFile(selectedOption);
                }
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame,
                    "An error occurred while getting the soundtracks",
                    "Error", JOptionPane.ERROR_MESSAGE);

            return soundTracksDropdown;
        }

        return soundTracksDropdown;
    }

    private JButton createDownloadButton() {
        download = new JButton("Download Video");
        download.setEnabled(false);
        download.addActionListener(e -> downloadVideo());

        return download;
    }

    public ArrayList<String> addImagesToDropdown() {
        final String imagesPath = "assets/Images";

        final ArrayList<String> images = collectFilesFromDirectory(imagesPath);

        select.addItem("Select Image");

        for (String image : images){
            select.addItem(image);
        }

        select.addItem("Browse");

        ArrayList<String> dropdownItems = new ArrayList<>();

        for (int i = 0; i < select.getItemCount(); i++){
            dropdownItems.add(select.getItemAt(i));
        }

        return dropdownItems;
    }

    private void downloadVideo() {
        File file = new File("Ffmpeg/final_output.mp4");
        if (file.exists()) {
            JFileChooser chooser = new JFileChooser();
            chooser.setApproveButtonText("Save");
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle("Save File");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                try {
                    String name;
                    while (true) {
                        name = JOptionPane.showInputDialog(frame, "Supply a name for the file: ");
                        if (!validateName(name)) {
                            JOptionPane.showMessageDialog(frame,
                                    "Not An appropriate name.",
                                    "Error", JOptionPane.PLAIN_MESSAGE);
                        } else {
                            break;
                        }
                    }
                    if (file.renameTo(new File(chooser.getSelectedFile() + "\\" + name + ".mp4"))) {
                        JOptionPane.showMessageDialog(frame,
                                "Done.",
                                "Video Saved", JOptionPane.PLAIN_MESSAGE);
                        download.setEnabled(false);
                    } else {
                        System.out.println("Failed");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } else {
                System.out.println("No Selection ");
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "No video to download.",
                    "No video", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateName(String name) {
        return name.length() > 0 &&
               !name.equals(" ") &&
               !name.contains("<") &&
               !name.contains(">") &&
               !name.contains(":") &&
               !name.contains("\"") &&
               !name.contains("/") &&
               !name.contains("\\") &&
               !name.contains("|") &&
               !name.contains("?") &&
               !name.contains("*");
    }

    private void createStartButton() {
        start = new JButton("Start");
        start.setEnabled(false);
        start.addActionListener(new MainRecorder(this));
    }

    public String getImagePath() {
        System.out.println(imagePath);
        return imagePath;
    }

    public String getImageName() {
        return imageName;
    }

    private ArrayList<String> collectFilesFromDirectory(String path) {
        final File folder = new File(path);

        ArrayList<String> files = new ArrayList<>();

        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isFile()) {
                files.add(fileEntry.getName());
            }
        }

        return files;
    }

    private File selectFile(FileNameExtensionFilter fileType) {
        JFileChooser fileChooser = new JFileChooser();

        if (fileType != null) {
            fileChooser.setFileFilter(fileType);
        }

        fileChooser.showOpenDialog(null);

        return fileChooser.getSelectedFile();
    }

    public JSlider getBlurStrength() {
        return blurStrength;
    }

    public boolean isCheckboxValue() {
        return checkboxValue;
    }

    private void setCheckboxValue(Boolean newValue) {
        checkboxValue = newValue;
    }

    public void setImageName(String s) {
        imageName = s;
    }
}
